<template>
  <div style="margin: 20px">
    <Card style="margin: 20px auto">
      <Select v-model="head.logType" placeholder='选择日志类型' style="width: 240px; margin-right: 10px" @on-change='logTypeChanage' :clearable='true' filterable>
        <Option v-for="item in head.logTypeList" :value="item" :key="item">{{ item }}</Option>
      </Select>
      <Select v-model="head.target" placeholder='选择通道类型' style="width: 240px; margin-right: 10px" @on-change='targetChanage' :clearable='true' filterable>
        <Option v-for="item in head.targetList" :value="item.id" :key="item.id">{{ item.id }} - {{item.name}}</Option>
      </Select>
      <Select v-model="head.status" style="width: 240px; margin-right: 10px" @on-change='statusChanage' :clearable='true'>
        <Option value="1">有效</Option>
        <Option value="0">无效</Option>
      </Select>
      <div style="float: right">
        <Button type='primary' @click="addLogType">新增日志</Button>
      </div>
    </Card>

    <div>
      <Table stripe :columns="table.columns" :data="table.datas" :loading="table.loading"></Table>
      <div style='margin-top: 15px'>
        <Page :total='table.iTotalRecords'
              :current='table.iPageNum'
              :page-size='table.iPageSize'
              @on-change='turnPage'
              @on-page-size-change='changePageSize'
              show-sizer show-total></Page>
      </div>
    </div>

    <Modal title='新增日志' width='450' v-model='addDialog.model' :mask-closable='false' class-name='vertical-center-modal'>
      <div style="margin: auto 20px">
        <Form ref="addDialogForm" :model="addDialog.formLogType" :rules="addDialog.ruleLogType" label-position="left" :label-width="80" inline>
          <FormItem label="日志通道"  prop="target">
            <Select v-model="addDialog.formLogType.target" style="width: 240px; margin-right: 10px" filterable>
              <Option v-for="item in head.targetList" :value="item.id" :key="item.id">{{ item.id }} - {{item.name}}</Option>
            </Select>
          </FormItem>
          <FormItem label="日志类型" prop="logType">
            <Input v-model='addDialog.formLogType.logType' :maxlength='32' placeholder='' style='width: 240px'/>
          </FormItem>
        </Form>
      </div>

      <div slot='footer'>
        <Button type='text' @click='addDialog.model = false'>取消</Button>
        <Button type='primary' :loading="addDialog.loading" v-on:click='saveLogType'>确定</Button>
      </div>
    </Modal>
  </div>

</template>

<script>
  import $ from '../../../axios.js'

  export default {
    name: "channel",

    data () {
      const validatorTarget = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('日志通道不能为空'))
        } else {
          callback()
        }
      }
      const validatorLogType = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('日志类型不能为空'))
        } else {
          let params = new URLSearchParams()
          params.append('target', this.addDialog.formLogType.target)
          params.append('logType', this.addDialog.formLogType.logType)
          $.ajax.post('/logChannel/existLogType', params)
            .then(data => {
              if (!data) {
                callback()
              } else {
                callback(new Error('通道日志已存在'))
              }
            })
            .catch(function (reason) {
              console.log(reason)
            })
        }
      }

      return {
        head: {
          logType: '',
          logTypeList: [],

          target: '',
          targetList: [],

          status: '1',
        },

        table: {
          iTotalRecords: 0,
          iPageSize: 10,
          iPageNum: 1,
          loading: false,

          columns: [
            {
              title: '序号',
              key: 'id',
              width: 80,
              align: 'center'
            },
            {
              title: '日志类型',
              key: 'logType',
              align: 'center'
            },
            {
              title: '通道类型',
              key: 'target',
              align: 'center'
            },
            {
              title: '操作人',
              key: 'author',
              align: 'center'
            },
            {
              title: '状态',
              align: 'center',
              render: (h, params) => {
                let data = this.table.datas[params.index]
                return h('p', [
                  h('i-switch', {
                    props: {
                      type: 'primary',
                      value: data.status,
                      disabled: data.disabled
                    },
                    on: {
                      'on-change': () => {
                        this.changeStatus(data.id, data.status)
                      }
                    }
                  }, [
                    h('Icon', {
                      slot: 'open',
                      props: {
                        type: 'checkmark'
                      }
                    }),
                    h('Icon', {
                      slot: 'close',
                      props: {
                        type: 'close'
                      }
                    })
                  ])
                ])
              }
            },
            {
              title: '更新时间',
              key: 'updateTime',
              width: 180,
              align: 'center'
            },
            {
              title: '创建时间',
              key: 'createTime',
              width: 180,
              align: 'center'
            }
          ],
          datas: []
        },

        addDialog: {
          model: false,
          loading: false,
          formLogType: {
            target: '1',
            logType: ''
          },
          ruleLogType: {
            target: [{required: true, validator: validatorTarget, trigger: 'blur'}],
            logType: [{required: true, validator: validatorLogType, trigger: 'blur'}],
          }
        }
      }
    },

    created () {
      this.initHead()
      this.initTable()
    },

    methods: {
      initHead: function () {
        $.ajax.post('/logChannel/getAllLogType')
          .then(data => {
            this.head.logTypeList = data
          })
          .catch(function (reason) {
            console.log(reason)
          })

        $.ajax.post('/logChannel/getLogChannelType')
          .then(data => {
            this.head.targetList = data
          })
          .catch(function (reason) {
            console.log(reason)
          })
      },
      initTable: function () {
        let params = new URLSearchParams()
        params.append('iDisplayStart', (this.table.iPageNum - 1) * this.table.iPageSize)
        params.append('iDisplayLength', this.table.iPageSize)
        params.append('sEcho', '1')
        params.append('logType', this.head.logType === undefined ? '' : this.head.logType)
        params.append('target', this.head.target === undefined ? '' : this.head.target)
        params.append('status', this.head.status === undefined ? '' : this.head.status)
        this.table.loading = true
        $.ajax.post('/logChannel/query', params)
          .then(data => {
            if ((data.iTotalRecords - (this.table.iPageNum - 1) * this.table.iPageSize) === 0 && this.table.iPageNum > 1) {
              this.table.iPageNum = this.table.iPageNum - 1
              this.initTable()
            } else {
              this.table.datas = []
              this.table.iTotalRecords = data.iTotalRecords
              for (let i = 0; i < data.aaData.length; i++) {
                let secretkey = (data.aaData[i][3] === undefined || data.aaData[i][3] === '') ? '--' : data.aaData[i][3]
                this.table.datas.push({
                  id: data.aaData[i][0],
                  logType: data.aaData[i][1],
                  target: data.aaData[i][2],
                  author: secretkey,
                  status: data.aaData[i][4],
                  updateTime: data.aaData[i][5],
                  createTime: data.aaData[i][6],
                  disabled: data.aaData[i][7]
                })
              }
            }
            this.table.loading = false
          }).catch(function (reason) {
            console.log(reason)
          })
      },
      logTypeChanage: function () {
        this.initTable()
      },
      targetChanage: function () {
        this.initTable()
      },
      statusChanage: function () {
        this.initTable()
      },
      addLogType: function () {
        this.addDialog.model = true
        this.$refs.addDialogForm.resetFields()
      },
      saveLogType: function () {
        this.$refs.addDialogForm.validate((valid) => {
          if (valid) {
            let params = new URLSearchParams()
            params.append('target', this.addDialog.formLogType.target)
            params.append('logType', this.addDialog.formLogType.logType)
            $.ajax.post('/logChannel/addLogType', params)
              .then(data => {
                if (data) {
                  this.addDialog.model = false
                  this.addDialog.loading = false
                  this.initHead()
                  this.initTable()
                }
              })
              .catch(function (reason) {
                console.log(reason)
              })
          }
        })
      },
      turnPage: function (index) {
        this.table.iPageNum = index
        this.initTable()
      },
      changePageSize: function (size) {
        this.table.iPageSize = size
        this.table.iPageNum = 1
        this.initTable()
      },
      changeStatus: function (id, status) {
        let message = ''
        if (status) {
          message = '状态置为：无效'
        } else {
          message = '状态置为：有效'
        }
        this.$Modal.confirm({
          title: '提示',
          content: message,
          onOk: () => {
            let params = new URLSearchParams()
            params.append('id', id)
            $.ajax.post('/logChannel/changeStatus', params)
              .then(data => {
                if (data) {
                  this.initTable()
                }
              }).catch(function (reason) {
                console.log(reason)
              })
          },
          onCancel: () => {

          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
