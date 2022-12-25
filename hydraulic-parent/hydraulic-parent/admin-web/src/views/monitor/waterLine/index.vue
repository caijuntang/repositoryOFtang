<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-select v-model="query.stationId" placeholder="全部站点" class="filter-item" :clearable=true>
          <el-option
            v-for="item in stations"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
        <el-input v-model="query.blurry" :clearable=true size="small" placeholder="模糊搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="660px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="告警名称" prop="alarmName">
          <el-input v-model="form.name" placeholder="请填写告警名称" style="width: 200px;"  :clearable=true />
        </el-form-item>
        <el-form-item  label="泵站名称" prop="name">
          <el-select v-model="form.stationId" :clearable=true placeholder="请选择站点" style="width: 200px;">
            <el-option
              v-for="item in stations"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="消息模版" prop="templateId" >
          <el-input v-model="form.templateId" placeholder="请输入告消息模版ID" style="width: 500px;" :clearable=true />
        </el-form-item>
        <el-form-item label="告警水位" prop="alarmLine" style="width: 300px;">
          <el-input v-model="form.alarmLine" placeholder="请设置告警水位" style="width: 100px;"/> 米
        </el-form-item>
        <el-form-item label="是否接入" prop="status" style="width: 280px;">
          <el-switch v-model="form.status"></el-switch>
        </el-form-item>
        <el-form-item  label="告警频次" prop="frequency" style="width: 280px;">
          <el-select v-model="form.frequency" placeholder="请选择告警频次" style="width: 100px;" >
            <el-option
              v-for="item in frequencyList"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select> 分钟
        </el-form-item>
        <el-form-item  label="通知人" prop="receivers" >
          <el-select v-model="form.receivers" :multiple=true placeholder="请选择通知人" style="width: 500px;">
            <el-option
              v-for="item in receiverList"
              :key="item.id"
              :label="item.nickName"
              :value="item.openid">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      :data="crud.data"
      row-key="id">
      <el-table-column prop="name" label="名称" align="center" width="240px" />
      <el-table-column prop="stationName" label="站点" align="center" width="300px" />
      <el-table-column prop="alarmLine" label="告警水位" align="center">
        <template slot-scope="scope">
          <span>{{scope.row.alarmLine}} 米</span>
        </template>
      </el-table-column>
      <el-table-column prop="frequency" label="告警频次" align="center" >
        <template slot-scope="scope">
          <span>{{scope.row.frequency}} 分钟/次</span>
        </template>
      </el-table-column>
      <el-table-column prop="templateId" label="模版Id" align="center" width="400px" />
      <el-table-column prop="status" label="是否开启" align="center" >
        <template slot-scope="scope">
          <span v-if="scope.row.status">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" align="center" label="创建日期" width="200px" />
      <el-table-column v-if="checkPer(['admin','alarm:edit','alarm:del'])" label="操作" width="150px" align="center" fixed="right">
        <template slot-scope="scope">
          <udOperation
            :data="scope.row"
            :permission="permission"
            msg="确定删除吗,此操作不能撤销！"
          />
        </template>
      </el-table-column>
    </el-table>
    <pagination />
  </div>
</template>

<script>
  import crudAlarm from '@/api/monitor/alarm'
  import StationJs from '@/api/monitor/station'
  import controlJs from '@/api/monitor/centerControl'
  import CRUD, { presenter, header, form, crud } from '@crud/crud'
  import rrOperation from '@crud/RR.operation'
  import crudOperation from '@crud/CRUD.operation'
  import udOperation from '@crud/UD.operation'
  import pagination from '@crud/Pagination'


  // crud交由presenter持有
  const defaultForm = { id: null, name: null, stationId: 1, alarmLine:null,receivers:[],templateId:'',frequency:60,status:false}
  export default {
    name: 'WaterLine',
    components: {crudOperation, rrOperation, udOperation,pagination},
    cruds() {
      return CRUD({ title: '水位告警', url: 'api/alarm/', crudMethod: { ...crudAlarm },
        optShow: {
          add: true,
          edit: false,
          del: false
        }
      })
    },
    mixins: [presenter(), header(), form(defaultForm), crud()],
    data() {
      return {
        receiverList:[],
        stations:[],
        frequencyList:[15,30,60],
        permission: {
          add: ['admin', 'alarm:add'],
          edit: ['admin', 'alarm:edit'],
          del: ['admin', 'alarm:del']
        },
        rules: {
          name: [
            { required: true, message: '请输入标题', trigger: 'blur' }
          ],
          alarmLine: [
            { required: true, message: '请设置告警水位', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      // 新增与编辑前做的操作
      // 初始化编辑时候的角色与岗位
      // [CRUD.HOOK.beforeToEdit](crud, form) {
      //
      // },
      // 提交前做的操作
      // [CRUD.HOOK.afterValidateCU](crud) {
      //   return true
      // },
      getStationAll(){
        StationJs.getStationData().then(res=>{
          this.stations=res['all']
        })
      },
      getWXReceivers(){
        controlJs.getWXReceivers().then(res=>{
          this.receiverList=res
        })
      }
    },
    mounted() {
      this.getStationAll()
      this.getWXReceivers()
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
