<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-select v-model="query.stationId" placeholder="全部泵站" class="filter-item" :clearable=true>
          <el-option
            v-for="item in stations"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
        <el-select v-model="query.type" placeholder="告警类型" class="filter-item" style="width: 100px" :clearable=true >
          <el-option
            v-for="(item,index) in alarmTypeList"
            :key="index"
            :label="item.name"
            :value="item.value">
          </el-option>
        </el-select>
        <el-select v-model="query.status" placeholder="是否解除" class="filter-item" style="width: 100px" :clearable=true >
          <el-option
            v-for="(item,index) in statusList"
            :key="index"
            :label="item.name"
            :value="item.value">
          </el-option>
        </el-select>
        <el-input v-model="query.blurry" :clearable=true size="small" placeholder="模糊搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker v-model="query.createTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="泵站名称" prop="stationName">
          <el-input v-model="form.stationName"  style="width: 200px;" :disabled=true />
        </el-form-item>
        <el-form-item label="告警类型" prop="alarmTypeName">
          <el-input v-model="form.alarmTypeName" style="width: 200px;" :disabled=true />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input  type="textarea" v-model="form.content"  style="width: 400px;" :disabled=true />
        </el-form-item>
        <el-form-item label="告警时间" prop="createTime" >
          <el-input v-model="form.createTime" :disabled=true style="width: 200px;"/>
        </el-form-item>
        <el-form-item label="解除时间" prop="fixTime" >
          <el-input v-model="form.fixTime==null?'--':form.fixTime" :disabled=true style="width: 200px;"/>
        </el-form-item>
        <el-form-item label="是否解除" prop="status" style="width: 200px;">
          <el-switch v-model="form.status==0" :disabled=true ></el-switch>
        </el-form-item>
        <el-form-item label="备注" prop="remark" >
          <el-input type="textarea" v-model="form.remark" :disabled="form.status==0"style="width: 400px;" ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU" v-if="form.status==1">解除</el-button>
      </div>
    </el-dialog>
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      :data="crud.data"
      row-key="id">
      <el-table-column prop="alarmTypeName" label="告警类型" align="center" width="300px" />
      <el-table-column prop="stationName" label="泵站" align="center" width="300px" />
      <el-table-column prop="content" label="内容" align="center"/>
      <el-table-column prop="createTime" label="告警时间" align="center" width="240px" />
      <el-table-column prop="status" label="是否解除" align="center" >
        <template slot-scope="scope">
          <span v-if="scope.row.status==1">否</span>
          <span v-else>是</span>
        </template>
      </el-table-column>
      <el-table-column prop="fixTime" label="解除时间" align="center" width="260px" >
        <template slot-scope="scope">
          <span v-if="scope.row.fixTime==null">{{"--"}}</span>
          <span v-else>{{scope.row.fixTime}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="checkPer(['admin','alarmRecord:edit'])" label="操作" width="130px" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button v-permission="permission.edit" :loading="crud.status.cu === 2"  size="mini" type="primary" @click="crud.toEdit(scope.row)">{{scope.row.status==0? '详情' : '解除'}}</el-button>
          <!--          <udOperation-->
<!--            :data="scope.row"-->
<!--            :permission="permission"-->
<!--            msg="确定删除吗,此操作不能撤销！"-->
<!--          />-->
        </template>
      </el-table-column>
    </el-table>
    <pagination />
  </div>
</template>

<script>
  import crudAlarmRecord from '@/api/monitor/alarmRecord'
  import StationJs from '@/api/monitor/station'
  import CRUD, { presenter, header, form, crud } from '@crud/crud'
  import rrOperation from '@crud/RR.operation'
  import crudOperation from '@crud/CRUD.operation'
  import udOperation from '@crud/UD.operation'
  import pagination from '@crud/Pagination'
  import DateRangePicker from '@/components/DateRangePicker'

  // crud交由presenter持有
  const defaultForm = { id: null, alarmType: null, stationName: null, content: null,  createTime: null, status: 0 ,fixTime:null,remark:null}
  export default {
    name: 'AlarmRecord',
    components: {crudOperation, rrOperation, udOperation,pagination,DateRangePicker},
    cruds() {
      return CRUD({ title: '告警记录', url: 'api/alarmRecord/', crudMethod: { ...crudAlarmRecord },
        optShow: {
          add: false,
          edit: false,
          del: false
        }
      })
    },
    mixins: [presenter(), header(), form(defaultForm), crud()],
    data() {
      return {
        alarmTypeList:[{name:'水位告警',value:0},{name:'泵机告警',value:1}],
        statusList:[{name:'未解除',value:1},{name:'解除',value:0}],
        stations:[],
        permission: {
          edit: ['admin', 'alarmRecord:edit'],
        },
        rules: {
          remark: [
            {required: true, message: '请输入标题', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      // 新增与编辑前做的操作
      // [CRUD.HOOK.afterToCU](crud, form) {
      //
      // },
      getStationAll(){
        StationJs.getStationData().then(res=>{
          this.stations=res['all']
        })
      }
    },
    mounted() {
      this.getStationAll()
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
