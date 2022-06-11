<template>
  <!--search-->
  <div style="margin:10px">
    <div>
      <Card>
        <Row>
          <div style="display: inline-block;">
            <Select v-model="logSearch.productLine" clearable filterable style="width: 200px;" placeholder="请选择产品线"
                    @on-change="productLineChange">
              <Option v-for="item in productLineList" :value="item.id" :key="item.id">{{item.name}}-{{item.id}}</Option>
            </Select>
          </div>
          <div style="display: inline-block;">
            <Select v-model="logSearch.product" clearable filterable placeholder="请选择产品" style="width: 200px">
              <Option v-for="pl in productList" :value="pl.id" :key="pl.id">{{pl.cnName }}-{{ pl.id }}</Option>
            </Select>
          </div>
          <div style="display: inline-block;">
            <Select v-model="logSearch.logType" clearable filterable style="width: 200px" placeholder="请选择日志类型"
                    @on-change="logTypeChange">
              <Option v-for="logType in logTypeList" :value="logType.logTypeName" :key="logType.id">{{logType.logTypeName}}</Option>
            </Select>
          </div>
          <div style="display: inline-block;">
            <DatePicker :value="dateInterval" :clearable=false type="datetimerange"
                        @on-change="getDateRang"
                        format="yyyy-MM-dd HH:mm:ss"
                        placeholder="请选择时间范围"
                        style="width: 280px"></DatePicker>
          </div>
          <div style="display: inline-block;">
            <Button type="text" @click="seniorSearch">高级搜索</Button>
          </div>
          <div style="display: inline-block; float: right;">
            <Button type="primary" @click="queryTable" :disabled=" table_load">查询</Button>
            <Button type="warning" @click="dataExport">导出</Button>
          </div>
        </Row>
        <Row style="margin-top:10px " v-if="searchInputShow">
          <div style="display: inline-block;">
            <Select v-model="logSearch.tagName" style="width: 200px" placeholder="请选择标签" clearable>
              <Option v-for="tagName in tagList" :value="tagName" :key="tagName">按{{tagName}}查询</Option>
            </Select>
            <Input v-model="logSearch.tagVal" style="width: 200px " clearable/>
          </div>
          <div style="display: inline-block;">
            <Select v-model="logSearch.signalName" style="width: 200px" placeholder="请选择用户标示" clearable>
              <Option v-for="signal in singalList" :value="signal" :key="signal">按{{signal}}检索</Option>
            </Select>
            <Input v-model="logSearch.signalVal" style="width: 280px" clearable/>
          </div>

        </Row>
        <Row style="margin-top:10px" v-if="searchInputShow">
          <div style="display: inline-block;">
            <Input v-model="logSearch.params" placeholder="自定义查询条件" style="width: 892px" clearable/>
          </div>
          <a>
            <Icon type="md-help-circle" size="24" @click="conditionTips"/>
          </a>
        </Row>
      </Card>
    </div>
    <div>
      <div class="card-top">
        <Tabs size="small" @on-click="tabChange" style="margin-top: 51px" v-if="chartShow">
          <TabPane label="按时统计" name="hour"></TabPane>
          <TabPane label="按天统计" name="day"></TabPane>
        </Tabs>
        <Collapse @on-change="initChart" :style="chartShow ? 'margin-top: -90px':''">
          <Panel>
            日志统计
            <div slot="content" id="logChart" style="margin-top: 15px;height: 450px;width: 100%">
            </div>
          </Panel>
        </Collapse>
        <Table style="margin-top: 10px" size="small" border :columns='table_columns' :data='table_data'
               :highlight-row=true :loading="table_load"></Table>
        <div style='margin-top: 15px'>
          <div style='float:left; display: inline-block'>
            <Page :total='logSearch.iTotalRecords'
                  :current='logSearch.iPageNum'
                  :page-size='logSearch.iPageSize'
                  @on-change='turnPage'
                  @on-page-size-change='changePageSize'
                  show-sizer show-total>共{{logSearch.iTotalDisplayRecords}}条
            </Page>
          </div>
          <div style="float:left; display: inline-block;margin-top: 5px">
            <Poptip trigger="hover" word-wrap width="550" content="提示：表格分页中展示的为当前条件下的总数据量，当查询结果数据量超过第10000后将不展示。">
              <a>
                <Icon type="md-help-circle" size="24"/>
              </a>
            </Poptip>
          </div>
        </div>
      </div>
    </div>

    <Modal title="日志详情" v-bind:closable='false' width="55" v-model='logDialogModal.modal' closable
           @on-cancel="logDialogCancel"
           :mask-closable="logDialogModal.closable">
      <Table size="small" :columns='log_detail_columns' :data='log_detail_data'></Table>
      <Divider type="horizontal" dashed v-if="log_detail_text.length>0"></Divider>
      <Row v-for="text in log_detail_text" :value="text.value" :key="text.key">
        <label>{{text.key}}：</label>
        <Input v-model="text.value" type="textarea" :autosize="true" :readonly="true"/>
      </Row>
      <div slot='footer'>
        <Button type='text' size='large' @click='logDialogCancel'>关闭</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
  import $ from '../../../../resources/lib/axios.js'
  import Date from '../../../../resources/lib/date.js'
  import Qs from 'qs'
  import eCharts from 'echarts'

  export default {
    name: 'control',
    data: function () {
      return {
        productLineList: [],
        productList: [],
        logTypeList: [],
        productsObj: {},
        initTags: ['cpname', 'status', 'localip'],
        tagList: [],
        singalList: ['uid', 'deviceid', 'caller', 'imsi'],
        dateInterval: [],
        chart: null,
        chartShow: false,
        searchInputShow: false,
        logDialogModal: {
          modal: false,
          closable: false,
        },
        logSearchOld:{
          productLine: '',
          logType: '',
        },
        logSearch: {
          productLine: '',
          productLineName: '',
          product: '',
          productName: '',
          logType: '',
          tagName: '',
          tagVal: '',
          signalName: '',
          signalVal: '',
          startTime: '',
          endTime: '',
          params: '',
          iTotalRecords: 0,
          iDisplayStart: 0,
          iTotalDisplayRecords: 0,
          iPageSize: 10,
          iPageNum: 1,
          chartType: "hour",
          sEcho: '1'
        },
        table_load: false,
        table_columns_start: [
          {
            title: '序号',
            key: 'index',
            align: 'center',
            width: 70
          },
          {
            title: '产品',
            key: 'bizid',
            align: 'center'
          }],
        table_columns_middle: [],
        table_columns_end: [
          {
            title: '创建时间',
            key: 'createTime',
            align: 'center'
          }, {
            title: '操作',
            key: 'action',
            align: 'center',
            width: 140,
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '10px'
                  },
                  on: {
                    click: () => {
                      this.detail(params.row.detail)
                    }
                  }
                }, '详情'),
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.totrace(params.row.traceid)
                    }
                  }
                }, '调用链')
              ])
            }
          }],
        table_columns: [],
        table_data: [],
        log_detail_columns: [
          {
            title: '字段名',
            key: 'key',
            align: 'center',
            sortable: true,
            sortType: 'asc',
            width: 150
          },
          {
            title: '内容',
            key: 'value',
            align: 'center'
          }
        ],
        log_detail_text: [],
        log_detail_textfield: [],
        log_detail_data: []
      }
    },
    methods: {
      init: function () {
        this.table_columns = this.table_columns_start.concat(this.table_columns_end)
        $.ajax.post('/anon/getUserContext')
          .then(data => {
            var pls = data.productLines;
            let len = pls.length;
            for (var i = 0; i < len; i++) {
              if (pls[i].products.length > 0) {
                this.productLineList.push(pls[i])
              }
            }
            if (this.productLineList.length>0) {
              if(this.logSearchOld.productLine){
                this.logSearch.productLine = this.logSearchOld.productLine
              }else{
                this.logSearch.productLine = this.productLineList[0].id
              }
              this.productLineChange()
            }
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      getQueryParam:function(){
        $.ajax.post('/logQuery/getQueryParam')
          .then(data => {
            if(data) {
              this.logSearchOld.productLine = data.productLine
              this.logSearchOld.logType = data.logType
              var startTime = data.startTime;
              if(startTime){
                this.logSearch.startTime = startTime
              }
              var endTime = data.endTime;
              if(endTime){
                this.logSearch.endTime = endTime
              }
            }
            this.init()
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      saveQueryParam:function(reqData){
        $.ajax.post('/logQuery/saveQueryParam',reqData)
          .then(data => {
            console.log(data)
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      productLineChange: function () {
        var productLineId = this.logSearch.productLine
        this.productList = []
        if (!productLineId) {
          return
        }
        var plLength = this.productLineList.length;
        for (var i = 0; i < plLength; i++) {
          var obj = this.productLineList[i]
          if (obj.id == productLineId) {
            this.productList = obj.products;
            this.productsObj = {}
            var productList = this.productList;
            for (var i in productList) {
              var p = productList[i];
              this.productsObj[p.id] = p.cnName
            }
            break;
          }
        }
        this.logSearch.iPageNum = 1
        this.getLoyTypeList();
      },
      getLoyTypeList: function () {
        if(!this.logSearch.productLine){
          return
        }
        $.ajax.post('/logQuery/getLogTypeByProductLine?productLine=' + this.logSearch.productLine)
          .then((resp) => {
            this.logTypeList = []
            this.logSearch.logType = ''
            if (resp) {
              var length = resp.length;
              if (length > 0) {
                for (var i = 0; i < length; i++) {
                  this.logTypeList.push(resp[i])
                }
              }
              if(this.logSearchOld.logType){
                this.logSearch.logType=this.logSearchOld.logType
                this.logTypeChange()
              }
            }
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      logTypeChange: function () {
        if (!this.logSearch.productLine) {
          return
        }
        this.logSearch.tagName = ''
        this.logSearch.tagVal = ''
        this.tagList = [].concat(this.initTags)
        this.log_detail_textfield = []
        let reqData = Qs.stringify(this.logSearch, {allowDots: true})
        this.table_columns_middle = []
        $.ajax.post('/logQuery/getCommonInfo', reqData)
          .then((resp) => {
            if (resp && resp.result) {
              //文本展示字段
              var textField = resp.textField;
              if (textField != null) {
                this.log_detail_textfield = textField
              }
              //列表标题
              var tags = resp.tags;
              if (tags != null) {
                var taglength = tags.length;
                for (var a = 0; a < taglength; a++) {
                  if (this.tagList.indexOf(tags[a]) == -1) {
                    this.tagList.push(tags[a])
                  }
                }
              }
              var rows = resp.rows;
              if (rows != null) {
                var rowlength = rows.length;
                for (var b = 0; b < rowlength; b++) {
                  this.table_columns_middle.push({
                    title: rows[b][1],
                    key: rows[b][0],
                    align: 'center'
                  })
                }
              }
            }
            this.logSearch.iPageNum = 1
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      getDateRang: function (dateRange) {
        this.logSearch.startTime = dateRange[0]
        this.logSearch.endTime = dateRange[1]
      },
      seniorSearch: function () {
        this.searchInputShow = !this.searchInputShow
      },
      tabChange: function (tabVal) {
        this.logSearch.chartType = tabVal
        this.queryChart()
      },
      turnPage: function (index) {
        this.logSearch.iPageNum = index
        this.initTable()
      }
      ,
      conditionTips: function () {
        this.$Modal.info({
          title: "提示",
          content: '<p><span>多条件查询分割符: </span>,</p>' +
          '<p><span>字段值完全匹配: </span>cpname:=searchservice</p>' +
          '<p><span>字段值模糊匹配: </span>uid:=?593656*332?122</p>' +
          '<p><span>字段不匹配: </span>cpname:!*componentname*</p>' +
          '<p><span>字段数值范围: </span>utm:>140 或 utm:<100</p>'+
          '<p class="warnfont"><span>注意: </span>respkg、reqpkg、inner字段内容模</p>'+
          '<p class="warnfont">糊搜索采用完全匹配格式</p>',
          okText: '关闭'
        });
      }
      ,
      changePageSize: function (size) {
        this.logSearch.iPageSize = size
        this.initTable()
      }
      ,
      detail: function (logMap) {
        this.log_detail_data = []
        this.log_detail_text = []
        for (var k in logMap) {
          var val = logMap[k];
          if ('string' != typeof(val)) {
            val = val.toString();
          }
          if (this.log_detail_textfield.indexOf(k) == -1) {
            this.log_detail_data.push({
              key: k,
              value: val
            })
          }
        }
        //按管理平台填入顺序展示文本域
        for (var l in this.log_detail_textfield) {
          var field = this.log_detail_textfield[l];
          var element = logMap[field];
          if (element != null || element != undefined) {
            this.log_detail_text.push({
              key: field,
              value: element
            })
          }
        }

        this.logDialogModal.modal = true
      },
      totrace: function (traceId) {
        var href = $.ROOT + '/logQuery/toTrace?traceId=' + traceId + "&productLine=" + this.logSearch.productLine + "&logType="
          + this.logSearch.logType + "&startTime=" + this.logSearch.startTime + "&endTime=" + this.logSearch.endTime;
        window.open(href, '_blank');
      },
      queryChart: function () {
        this.fillProductLineName();
        let reqData = Qs.stringify(this.logSearch, {allowDots: true})
        $.ajax.post('/logQuery/chartQuery', reqData)
          .then((result) => {
            var dataList = result.datas;
            var categoryList = result.category;
            const chartOption = {
              tooltip: {
                trigger: 'axis'
              },
              grid: {
                left: '3%',
                right: '3%',
                bottom: '3%',
                containLabel: true
              },
              toolbox: {
                feature: {
                  dataZoom: {
                    yAxisIndex: 'none'
                  },
                  restore: {},
                  saveAsImage: {show: true}
                }
              },
              xAxis: {
                type: 'category',
                boundaryGap: true,
                data: categoryList
              },
              yAxis: {
                type: 'value',
                name: '数量',
                position: 'left'
              },
              series: {
                type: 'line',
                smooth: true,
                data: dataList
              }
            }
            var logchartDiv = document.getElementById("logChart");
            this.chart = eCharts.init(logchartDiv);
            var childDiv = logchartDiv.firstChild;
            var widNum = logchartDiv.style.width - 8;
            childDiv.style.width = widNum
            this.chart.setOption(chartOption)
            this.chart.resize()
          }).catch(function (reason) {
          console.log(reason)
        })
      },
      initChart: function () {
        if (this.chartShow) {
          this.chartShow = false
          return
        } else {
          this.chartShow = true
        }
        this.queryChart()
      },
      logDialogCancel: function () {
        this.logDialogModal.modal = false
      },
      dataExport: function () {
        if (this.logSearch.logType == '' || this.logSearch.logType == undefined) {
          this.$Modal.warning({title: "提示", content: "请选择日志类型！"})
          return
        }
        if (this.logSearch.iTotalDisplayRecords > 65535) {
          this.$Modal.warning({title: "提示", content: "日志量过大，无法导出！"})
          return
        }
        this.fillProductLineName();
        let reqData = Qs.stringify(this.logSearch)
        var paramString = reqData.toString();
        location.href = $.ROOT + '/logQuery/dataExport?' + paramString;
      },
      queryTable: function () {
        if (this.logSearch.productLine == '' || this.logSearch.productLine == undefined) {
          this.$Modal.warning({title: "提示", content: "请选择产品线！"})
          return
        }
        if (this.logSearch.startTime == '' || this.logSearch.endTime == '') {
          this.$Modal.warning({title: "提示", content: "请选择时间范围！"})
          return
        }
        if (this.logSearch.logType == '' || this.logSearch.logType == undefined) {
          this.$Modal.warning({title: "提示", content: "请选择日志类型！"})
          return
        }
        this.initTable()
      },
      //根据选择的产品线id和产品id,为logsearch请求参数填充对应的中文名称字段,用于后端打印查询日志.
      fillProductLineName: function () {
        this.logSearch.productLineName = this.logSearch.productLine ?
          this.productLineList.find(x => x.id === this.logSearch.productLine).name : ''
        this.logSearch.productName = this.logSearch.product ?
          this.productList.find(x => x.id === this.logSearch.product).cnName : ''
      },
      initTable: function () {
        this.table_load = true
        this.table_data = []
        this.logSearch.iDisplayStart = (this.logSearch.iPageNum - 1) * this.logSearch.iPageSize
        this.logSearch.iDisplayLength = this.logSearch.iPageSize
        this.logSearch.sEcho = '1'
        this.fillProductLineName();
        let reqData = Qs.stringify(this.logSearch, {allowDots: true})
        this.table_columns = this.table_columns_start.concat(this.table_columns_middle)
        this.table_columns = this.table_columns.concat(this.table_columns_end)
        $.ajax.post('/logQuery/tableQuery', reqData)
          .then((result) => {
            if (result) {
              if ((result.iTotalRecords - (this.logSearch.iPageNum - 1) * this.logSearch.iPageSize) <= 0 && this.logSearch.iPageNum > 1) {
                this.logSearch.iPageNum = this.logSearch.iPageNum - 1
                this.initTable()
              } else {
                this.table_data = []
                this.logSearch.iTotalRecords = result.iTotalRecords
                this.logSearch.iTotalDisplayRecords = result.iTotalDisplayRecords
                var data = result.aaData;
                if (data) {
                  var length = data.length;
                  if (length > 0) {
                    var keys = result.keyList;
                    if (data) {
                      var dataLen = data.length
                      var keyLen = keys.length;
                      for (var i = 0; i < dataLen; i++) {
                        var row = {}
                        for (var j = 0; j < keyLen; j++) {
                          if (j == 1) {
                            var p = this.productsObj[data[i][j]];
                            row[keys[j]] = p == null ? data[i][j] : p + "-" + data[i][j]
                          } else {
                            row[keys[j]] = data[i][j]
                          }
                        }
                        this.table_data.push(row)
                      }
                    }
                  }
                }
              }
            }
            this.table_load = false
          }).catch(function (reason) {
          this.table_load = false
          console.log(reason)
        })
        if (this.chartShow) {
          this.queryChart()
        }
        this.saveQueryParam(reqData)
      },
      confirmAlert: function (message) {
        var content = '<p>' + message + '</p>'
        this.$Modal.confirm({
          title: '提示',
          content: content,
        })
      }
    },
    created() {
      this.getQueryParam()
      var today = Date.today();
      var startTime = today.toString("yyyy-MM-dd 00:00:00")
      var tomorrow = today.addDays(1);
      var endTime = tomorrow.toString("yyyy-MM-dd 00:00:00")
      this.dateInterval = [startTime, endTime]
      this.logSearch.startTime = startTime
      this.logSearch.endTime = endTime
    }
  }
</script>


<style>
  .card-top {
    margin-top: 10px;
  }
  .warnfont{
    color: #FF0000;
  }

  #detailCell .ivu-cell-footer span {
    width: 430px;
    word-break: break-all;
    display: inline-block;
    white-space: pre-wrap;
    word-wrap: break-word;
    overflow: auto;
    padding-top: 5px;
  }

</style>
