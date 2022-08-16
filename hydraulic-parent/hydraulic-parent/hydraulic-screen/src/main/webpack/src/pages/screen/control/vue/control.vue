<template>
  <!--search-->
  <div style="margin:10px">
    <Button type="primary" @click="initAlarmDialog" style="margin-left: 22px">水位报警设置</Button>
    <card dis-hover class="card-top">
      <p slot="title" style="text-align: center">宣城市宣州区敬亭圩排涝站水位监测</p>
      <card class="card-content">
        外河水位：<Input v-model="outsideVal" style="width: 70px;" readonly/>&nbsp;米
      </card>
      <card class="card-building">
        泵站机房
      </card>
      <card class="card-content">
        前池水位：<Input v-model="foreVal" style="width: 70px;" readonly/>&nbsp;米
      </card>
      <card class="card-content">
        内河水位：<Input v-model="insideVal" style="width: 70px;" readonly/>&nbsp;米

      </card>
    </card>
    <Modal title="水位报警配置" v-bind:closable='false' width="60" v-model='alarmDialogModal.modal' closable
           @on-cancel="alarmDialogCancel"
           :mask-closable="alarmDialogModal.closable">

      <Form :model="alarmForm" ref="alarmForm" :label-width="90">
        <FormItem label="报警名称:" prop="alarmName">
          <Input v-model="alarmForm.alarmName" placeholder="请输入报警名称" style="width:200px"
                 :maxlength="64"/>
        </FormItem>
        <FormItem label="报警水位:" prop="alarmLine">
          <Input type="number" v-model="alarmForm.alarmLine" style="width: 70px"/>&nbsp;米
        </FormItem>
        <FormItem label="报警开关:" prop="status">
          <Switch size="large" v-model="alarmForm.status ==1 ?true :false" @on-change="switchChange">
            <template #open>
              <span>ON</span>
            </template>
            <template #close>
              <span>OFF</span>
            </template>
          </Switch>
        </FormItem>
        <FormItem label="通知人:" prop="receivers">
          <Input type="textarea" v-model="alarmForm.receivers" :autosize='true' placeholder="请输入通知人，多个通知人用“,”分隔"
                 style="width:300px"/>
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
        insideVal: 0.01,
        foreVal: 0.01,
        outsideVal: 0.01,
        timer: '',
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
          receivers: "",
          status: 0
        }
      }
    },
    methods: {
      init: function () {
        this.getWaterLine()
        this.timer = setInterval(() => {
          this.getWaterLine()
        }, 10000)
      },
      getWaterLine: function () {
        $.ajax.post('/control/getWaterLine?stationId=1')
          .then(data => {
            let code = data.code;
            if ("200" !== code) {
              return
            }
            const alarmLine = data.alarmLine
            this.insideVal = data.insideVal
            this.outsideVal = data.outsideVal
            this.foreVal = data.foreVal
            if (alarmLine!==0&&this.insideVal >= alarmLine) {
              this.alarmMessage(alarmLine)
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
        if (this.alarmForm.receivers == '') {
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
          content: '当前内河水位已超' + alarmLine + '米的警戒水位！',
          top: 50,
          duration: 3
        })
      },
      switchChange: function (flag) {
        this.alarmForm.status = flag ? 1 : 0
        // $.ajax.post('/control/updateAlarmStatus?id=' + this.alarmForm.id)
        //   .then(() => {
        //   }).catch(function (reason) {
        //   console.log(reason)
        // })
      },
      initAlarmDialog: function () {
        $.ajax.post('/control/getAlarmConfig?stationId='+this.alarmForm.stationId)
          .then((form) => {
            if (form) {
              this.alarmForm.id = form.id
              this.alarmForm.alarmName = form.name
              this.alarmForm.alarmLine = form.alarmLine
              this.alarmForm.receivers = form.receivers
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
        this.alarmForm.alarmName = ""
        this.alarmForm.alarmLine = 0.00
        this.alarmForm.receivers=""
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
    margin: 10px;
    height: 30%;
    text-align: center;
    align-content: center;
    background: #57a3f3;
  }

  .card-building {
    margin: 10px;
    background: coral;
    height: 50%;
    text-align: center;
    align-content: center;
    font-size: 100px
  }

  .warnfont {
    color: #FF0000;
  }

</style>
