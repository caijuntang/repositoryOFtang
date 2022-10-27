<template>
  <!--search-->
  <div style="margin:10px" @click="drawerMove" >
    <Drawer title="泵站设置" v-model="setDrawer" placement="left" :closable="false">
      <Select style="margin-top: 60px;width: 200px;margin-left: 15px" v-model="selectStationId" :label-in-value="true" @on-change="stationChange">
        <Option v-for="station in stationList" :value="station.id" :key="station.id">{{station.name}}</Option>
      </Select>
      <Button type="primary" @click="initAlarmDialog" style="margin-top: 60px;margin-left: 15px ;width:200px">水位报警设置</Button>
    </Drawer>
    <card dis-hover class="card-top" >
      <p slot="title" style="text-align: center">{{selectStationName}}</p>
      <div>
        <card dis-hover class="card-content">
          <p slot="title" style="font-size: xx-small">外河水位</p>
          {{outsideVal}}&nbsp;米
        </card>
        <card dis-hover class="card-content">
          <p slot="title" style="font-size: xx-small">前池水位</p>
          {{foreVal}}&nbsp;米
        </card>
        <card dis-hover class="card-content" :class="isAlarm ? 'warnFont' : '' ">
          <p slot="title" style="font-size: xx-small">内河水位</p>
          {{insideVal}}&nbsp;米
        </card>
        <div style="display: inline-block;" v-for="count of pumpCount" :value="count" :key="count">
          <card dis-hover class="card-building">
            <p slot="title" style="font-size: xx-small">{{count}}#机组</p>
              <p>A相电压:{{pumpData[count-1].va}}V；A相电流:{{pumpData[count-1].aa}}A</p>
              <p>B相电压:{{pumpData[count-1].vb}}V；B相电流:{{pumpData[count-1].ab}}A</p>
              <p>C相电压:{{pumpData[count-1].vc}}V；C相电流:{{pumpData[count-1].ac}}A</p>
          </card>
        </div>
      </div>
    </card>
    <Modal title="水位告警配置" v-bind:closable='false' width="45" v-model='alarmDialogModal.modal' closable
           @on-cancel="alarmDialogCancel"
           :mask-closable="alarmDialogModal.closable">

      <Form :model="alarmForm" ref="alarmForm" :label-width="90">
        <FormItem label="告警名称:" prop="alarmName">
          <Input v-model="alarmForm.alarmName" placeholder="请输入告警名称" style="width:400px"
                 :maxlength="64"/>
        </FormItem>
        <FormItem label="告警水位:" prop="alarmLine">
          <Input type="number" v-model="alarmForm.alarmLine" style="width: 70px"/>&nbsp;米
        </FormItem>
        <FormItem label="告警开关:" prop="status">
          <Switch size="large" v-model="alarmForm.status ==1 ?true :false" @on-change="switchChange">
            <template #open>
              <span>ON</span>
            </template>
            <template #close>
              <span>OFF</span>
            </template>
          </Switch>
        </FormItem>
        <FormItem label="告警频率:" prop="frequency">
          <Select v-model="alarmForm.frequency" style="width: 100px;">
            <Option v-for="(f,index) in frequencyList" :value="f" :key="index">{{f}}分钟/次</Option>
          </Select>
        </FormItem>
        <FormItem label="通知人:" prop="receivers">
          <Select v-model="alarmForm.receivers" multiple filterable style="width: 400px;" placeholder="请选择通知人">
            <Option v-for="item in receiverList" :value="item.openid" :key="item.id">{{item.nickName}}</Option>
          </Select>
        </FormItem>
      </Form>
      <div slot='footer'>
        <Button type='text' size='large' @click='alarmDialogCancel'>取消</Button>
        <Button type='primary' size='large' :loading="alarmDialogModal.loading" @click='alarmDialogSubmit'>保存
        </Button>
      </div>
    </Modal>
  </div>
</template>

<script>
  import $ from '../../../../common/lib/axios.js'
  import Qs from 'qs'

  export default {
    name: 'control',
    data: function () {
      return {
        selectStationId:1,
        selectStationName:'',
        setDrawer:false,
        isAlarm: false,
        insideVal: 0.01,
        foreVal: 0.01,
        outsideVal: 0.01,
        timer: '',
        receiverList: [],
        stationList:[],
        pumpData:[],
        pumpCount:4,
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
        this.getStationList()
        this.getWaterLine()
        this.getPumpData()
        this.timer = setInterval(() => {
          this.initData()
         }, 10000)
      },
      initData:function(){
        this.getWaterLine()
        this.getPumpData()
      },
      getPumpCount:function(){
        $.ajax.post('/control/getPumpCount?stationId='+this.selectStationId)
          .then(data => {
            if (null != data) {
              this.pumpCount = data
            }
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      getStationList:function(){
        $.ajax.post('/station/getStationData')
          .then(data => {
            if (null != data) {
              this.stationList = data.stationList
              this.selectStationId=data.defaultStation.id
              this.selectStationName=data.defaultStation.name
            }
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      stationChange:function(stationVal){
        this.selectStationId=stationVal.value
        this.selectStationName=stationVal.label
        this.alarmForm.stationId=stationVal.value
        this.initData()
      },
      drawerMove:function(){
        this.setDrawer=true
      },
      getWXReceivers: function () {
        $.ajax.post('/control/getWXReceivers')
          .then(data => {
            if (null != data) {
              this.receiverList = data
            }
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      getPumpData:function(){
        $.ajax.post('/control/getPumpData?stationId='+this.selectStationId)
          .then(data => {
            if (null != data) {
              // this.pumpCount=data.length
              this.pumpData=data
            }
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      getWaterLine: function () {
        $.ajax.post('/control/getWaterLine?stationId='+this.selectStationId)
          .then(data => {
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
        $.ajax.post('/control/alarmConfigSave', reqData)
          .then((resp) => {
            if (resp) {
              this.$Message.success('报警设置成功！')
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
        this.$Message.warning({
          content: content,
          duration: 3
        })
      },
      alarmMessage: function (alarmLine) {
        this.$Message.error({
          background: true,
          content: '当前内河水位已超' + alarmLine + '米的警戒水位，请及时处置！',
          top: 50,
          duration: 3
        })
      },
      switchChange: function (flag) {
        this.alarmForm.status = flag ? 1 : 0
      },
      initAlarmDialog: function () {
        this.getWXReceivers()
        $.ajax.post('/control/getAlarmConfig?stationId=' + this.alarmForm.stationId)
          .then((form) => {
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
      clearInterval(this.timer);
    },
  }
</script>


<style>
  .card-top {
    margin-top: 10px;
  }

  .card-content {
    margin: 2px;
    height: 105px;
    width: 85px;
    display: inline-block;
    background: #a5def3;
  }

  .card-building {
    margin: 2px;
    background: #fffff9;
    height:150px;
    width:270px
    /*text-align: center;*/
    /*align-content: center;*/
  }

  .warnFont {
    -webkit-text-fill-color: red;
  }

</style>
