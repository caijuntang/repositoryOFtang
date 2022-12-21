<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-select v-model="query.stationId" placeholder="全部泵站" class="filter-item" :clearable="true">
          <el-option
            v-for="item in stations"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
        <el-select v-model="query.type" placeholder="告警类型" class="filter-item" :clearable="true">
          <el-option
            v-for="(item,index) in alarmTypeList"
            :key="index"
            :label="item.name"
            :value="item.value">
          </el-option>
        </el-select>
        <el-input v-model="query.blurry" clearable size="small" placeholder="模糊搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker v-model="query.createTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="580px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="泵站名称" prop="stationName">
          <el-input v-model="form.stationName"  style="width: 200px;" :disabled="true"/>
        </el-form-item>
        <el-form-item label="告警类型" prop="alarmType">
          <el-input v-model="form.alarmType"  style="width: 120px;" :disabled="true"/>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content"  style="width: 120px;" :disabled="true" />
        </el-form-item>
        <el-form-item label="告警时间" prop="createTime" style="width: 140px;">
          <el-input v-model="form.createTime" :disabled="true"/>
        </el-form-item>
        <el-form-item label="解除时间" prop="fixTime" style="width: 140px;">
          <el-input v-model="form.fixTime" :disabled="true"/>
        </el-form-item>
        <el-form-item label="是否解除" prop="status" style="width: 140px;">
          <el-switch v-model="form.status" :disabled="true"></el-switch>
        </el-form-item>
        <el-form-item label="备注" prop="remark" style="width: 140px;">
          <el-switch v-model="form.remark" :disabled="form.status==0"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU" v-if="form.status==0">解除</el-button>
      </div>
    </el-dialog>
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      :data="crud.data"
      row-key="id">
      <el-table-column prop="alramType" label="告警类型" align="center" width="100px" />
      <el-table-column prop="stationName" label="泵站" align="center" width="200px" />
      <el-table-column prop="content" label="内容" align="center" width="100px" />
      <el-table-column prop="createTime" label="告警时间" align="center" width="200px" />
      <el-table-column prop="status" label="是否解除" align="center" width="100px" >
        <template slot-scope="scope">
          <span v-if="scope.row.status==1">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="fixTime" label="解除时间" align="center" width="200px" />
      <el-table-column v-if="checkPer(['admin','alarmRecord:edit'])" label="操作" width="130px" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button v-permission="permission.edit" :loading="crud.status.cu === 2" :disabled="true" size="mini" type="primary" @click="crud.toEdit(data)">{{scope.row.status==1? '详情' : '解除'}}</el-button>
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


  // crud交由presenter持有
  const defaultForm = { id: null, alarmType: null, station: null, content: null,  createTime: null, status: 0 ,fixTime:null,remark:null}
  export default {
    name: 'AlarmRecord',
    components: {crudOperation, rrOperation, udOperation,pagination},
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
        alarmTypeList:[{name:'水位告警',value:'waterLine'},{name:'泵机告警',value:'pump'}],
        stations:[],
        permission: {
          edit: ['admin', 'alarmRecord:edit'],
        }
      }
    },
    methods: {
      // 新增与编辑前做的操作
      [CRUD.HOOK.afterToCU](crud, form) {

      },
      getStationAll(){
        StationJs.getStationData().then(res=>{
          this.stations=res['all']
        })
      },
      // 选中图标
      selected(name) {
        this.form.icon = name
      }
    },
    mounted() {
      this.getStationAll()
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
