<template>
  <!--search-->
<div>
  <el-drawer title="泵站设置" :visible.sync="setDrawer" wrapperClosable
             :direction="rtl" >
    <el-button type="primary" @click="initAlarmDialog" style="margin-top: 60px;margin-left: 22px">水位报警设置</el-button>
  </el-drawer>
  <div style="margin:10px"  @click="drawerMove">
    <el-card dis-hover class="card-top">
      <div slot="header" style="text-align: center">宣城市宣州区敬亭圩排涝站水位监测</div>
      <div>
        <el-card dis-hover class="card-content">
          <div slot="header" style="font-size: xx-small">外河水位</div>
          <div style="font-size: small;text-align: center"> {{outsideVal}}&nbsp;米</div>
        </el-card>
        <el-card dis-hover class="card-content">
          <div slot="header" style="font-size: xx-small">前池水位</div>
          <div style="font-size: small;text-align: center;">{{foreVal}}&nbsp;米</div>
        </el-card>
        <el-card dis-hover class="card-content" :class="isAlarm ? 'warnFont' : '' ">
          <div slot="header" style="font-size: xx-small">内河水位</div>
          <div style="font-size: small;text-align: center" > {{insideVal}}&nbsp;米</div>
        </el-card>
        <div style="display: inline-block;" v-for="count in pumpCount" :value="count" :key="count">
          <el-card dis-hover class="card-building" >
            <div slot="header" style="font-size: x-small">{{count}}#机组</div>
            <p style="font-size: xx-small">A相电压:{{pumpData[count-1].va}}V；A相电流:{{pumpData[count-1].aa}}A</p>
            <p style="font-size: xx-small">B相电压:{{pumpData[count-1].vb}}V；B相电流:{{pumpData[count-1].ab}}A</p>
            <p style="font-size: xx-small">C相电压:{{pumpData[count-1].vc}}V；C相电流:{{pumpData[count-1].ac}}A</p>
          </el-card>
        </div>
      </div>
    </el-card>
    <el-dialog title="水位告警配置"  width="45" :visible.sync='alarmDialogModal.modal' closable
              @close="alarmDialogCancel">

      <el-form :model="alarmForm" ref="alarmForm" :label-width="90">
        <el-form-item label="告警名称:" prop="alarmName">
          <el-input v-model="alarmForm.alarmName" placeholder="请输入告警名称" style="width:400px"
                    :maxlength="64"/>
        </el-form-item>
        <el-form-item label="告警水位:" prop="alarmLine">
          <el-input type="number" v-model="alarmForm.alarmLine" style="width: 70px"/>&nbsp;米
        </el-form-item>
        <el-form-item label="告警开关:" prop="status">
          <el-switch size="large" v-model="alarmForm.status ==1 ?true :false" @on-change="switchChange">
            <template #open>
              <span>ON</span>
            </template>
            <template #close>
              <span>OFF</span>
            </template>
          </el-switch>
        </el-form-item>
        <el-form-item label="告警频率:" prop="frequency">
          <el-select v-model="alarmForm.frequency" style="width: 100px;">
            <el-option v-for="(f,index) in frequencyList" :value="f" :key="index">{{f}}分钟/次</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="通知人:" prop="receivers">
          <el-select v-model="alarmForm.receivers" multiple filterable style="width: 400px;" placeholder="请选择通知人">
            <el-option v-for="item in receiverList" :value="item.openid" :key="item.id">{{item.nickName}}</el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot='footer'>
        <el-button type='text' size='large' @click='alarmDialogCancel'>取消</el-button>
        <el-button type='primary' size='large' :loading="alarmDialogModal.loading" @click='alarmDialogSubmit'>保存
        </el-button>
      </div>
    </el-dialog>
  </div>
</div>
</template>

<script>
  import {getWaterLine, getWXReceivers, getPumpData, alarmConfigSave, getAlarmConfig} from '@/api/monitor/centerControl'
  import Qs from 'qs'
  import ELMessage from 'element-ui'

  export default {
    name: 'CenterControl',
    data: function () {
      return {
        setDrawer: false,
        initStationId: 1,
        isAlarm: false,
        insideVal: 0.01,
        foreVal: 0.01,
        outsideVal: 0.01,
        timer: '',
        receiverList: [],
        pumpData: [],
        pumpCount: 4,
        frequencyList: ["5", "10", "15", "30", "45", "60"],
        alarmDialogModal: {
          modal: false,
          closable: false,
          loading: false
        },
        alarmForm: {
          id: null,
          stationId: 1,
          alarmName: "",
          alarmLine: 0.00,
          frequency: 5,
          receivers: [],
          status: 0
        }
      }
    },
    methods: {
      init: function () {
        this.getWaterLine()
        this.getPumpData()
        this.timer = setInterval(() => {
          this.initData()
        }, 10000)
      },
      initData: function () {
        this.getWaterLine()
        this.getPumpData()
      },
      drawerMove: function () {
        this.setDrawer = true
      },
      // drawerCLose: function(){
      //   this.setDrawer = false
      // },
      getWXReceivers: function () {
        getWXReceivers().then(data => {
          if (null != data) {
            this.receiverList = data
          }
        }).catch(function (reason) {
          console.log(reason)
        })
      },
      getPumpData: function () {
        this.pumpData = []
        getPumpData(this.initStationId).then(data => {
          if (null != data) {
            this.pumpCount = data.length
            this.pumpData = data
          }
        }).catch(function (reason) {
          console.log(reason)
        })
      },
      getWaterLine: function () {
        getWaterLine(this.initStationId).then(data => {
          let code = data.code;
          if ("200" !== code) {
            return
          }
          const alarmLine = data.alarmLine
          this.insideVal = data.insideVal
          this.outsideVal = data.outsideVal
          this.foreVal = data.foreVal
          if (alarmLine !== 0 && this.insideVal >= alarmLine) {
            this.isAlarm = true
          } else {
            this.isAlarm = false
          }
        }).catch(function (reason) {
          console.log(reason)
        })
      },
      alarmDialogSubmit: function () {
        this.alarmDialogModal.loading = true
        if (!this.formValidate()) {
          return
        }
        let reqData = Qs.stringify(this.alarmForm, {allowDots: true})
        alarmConfigSave(reqData).then((resp) => {
          if (resp) {
            // this.$Message.success('报警设置成功！')
          } else {
            this.confirmAlert('报警设置失败！')
          }
        }).catch(function (reason) {
          console.log(reason)
        })
        this.alarmDialogCancel()
      },
      formValidate: function () {
        if (this.alarmForm.alarmName == '') {
          this.confirmAlert('报警名称不能为空')
          return false
        }
        if (this.alarmForm.alarmLine == '') {
          this.confirmAlert('报警水位不能为空')
          return false
        }
        if (this.alarmForm.receivers.length == 0) {
          this.confirmAlert('通知人不能为空')
          return false
        }
        return true
      },
      confirmAlert: function (message) {
        const content = '<p>' + message + '</p>'
        // this.$Message.warning({
        //   content: content,
        //   duration: 3
        // })
        ELMessage({
          message: content,
          type: 'warning',
          duration: 3
        })
      },
      alarmMessage: function (alarmLine) {
        ELMessage({
          message: '当前内河水位已超' + alarmLine + '米的警戒水位，请及时处置！',
          type: 'warning',
          duration: 3
        })
      },
      switchChange: function (flag) {
        this.alarmForm.status = flag ? 1 : 0
      },
      initAlarmDialog: function () {
        this.getWXReceivers()
        getAlarmConfig(this.initStationId).then((form) => {
          if (form) {
            this.alarmForm.id = form.id
            this.alarmForm.alarmName = form.alarmName
            this.alarmForm.alarmLine = form.alarmLine
            this.alarmForm.receivers = form.receivers
            this.alarmForm.frequency = form.frequency
            this.alarmForm.status = form.status
          }
          this.alarmDialogModal.modal = true
        }).catch(function (reason) {
          console.log(reason)
        })
      },
      alarmDialogCancel: function () {
        this.alarmDialogModal.modal = false
        this.alarmDialogModal.closable = true
        this.alarmDialogModal.loading = false
        this.alarmForm.id = null
        this.alarmForm.status = 0
        this.alarmForm.frequency = 5
        this.alarmForm.alarmName = ""
        this.alarmForm.alarmLine = 0.00
        this.alarmForm.receivers = []
      }
    },
    created() {
      this.init()
    },
    beforeDestroy() {
      clearInterval(this.timer)
    }
  }
</script>

<style>
  .card-top {
    margin-top: 10px;
  }

  .card-content {
    margin: 2px;
    height: 90px;
    width: 90px;
    display: inline-block;
    background: #a5def3;
  }

  .card-building {
    margin: 2px;
    background: #fffff9;
    height: 150px;
    width: 270px
    /*text-align: center;*/
    /*align-content: center;*/
  }

  .warnFont {
    -webkit-text-fill-color: red;
  }

</style>
