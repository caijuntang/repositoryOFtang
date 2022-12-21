<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-select v-model="query.stationId" placeholder="全部站点" class="filter-item" :clearable="true">
          <el-option
            v-for="item in stations"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
        <el-input v-model="query.blurry" clearable size="small" placeholder="模糊搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="580px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="监控点" prop="videoName">
          <el-input v-model="form.videoName" placeholder="请填写监控点" style="width: 200px;" />
        </el-form-item>
        <el-form-item  label="泵站名称" prop="name">
          <el-select v-model="form.station" placeholder="请选择站点" style="width: 200px;">
            <el-option
              v-for="item in stations"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="序列号" prop="serialNo">
          <el-input v-model="form.serialNo" placeholder="请填写序列号" style="width: 120px;" />
        </el-form-item>
        <el-form-item label="通道" prop="channel">
          <el-input v-model="form.channel" placeholder="请填写通道" style="width: 120px;" />
        </el-form-item>
        <el-form-item label="主屏播放" prop="isDefault" style="width: 140px;">
          <el-switch v-model="form.isDefault"></el-switch>
        </el-form-item>
        <el-form-item label="是否接入" prop="status" style="width: 140px;">
          <el-switch v-model="form.status"></el-switch>
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
      <el-table-column prop="videoName" label="监控点" align="center" width="100px" />
      <el-table-column prop="stationName" label="站点" align="center" width="200px" />
      <el-table-column prop="serialNo" label="序列号" align="center" width="100px" />
      <el-table-column prop="channel" label="通道" align="center" width="100px" />
      <el-table-column prop="isLive" label="主屏播放" align="center" width="100px" >
        <template slot-scope="scope">
          <span v-if="scope.row.isLive">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="是否接入" align="center" width="100px" >
        <template slot-scope="scope">
          <span v-if="scope.row.status">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="isDefault" label="是否默认" align="center" width="100px" >
        <template slot-scope="scope">
          <span v-if="scope.row.isDefault">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建日期" align="center"  width="160px" />
      <el-table-column v-if="checkPer(['admin','station:edit','station:del'])" label="操作" width="130px" align="center" fixed="right">
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
  import crudVideo from '@/api/monitor/video'
  import StationJs from '@/api/monitor/station'
  import CRUD, { presenter, header, form, crud } from '@crud/crud'
  import rrOperation from '@crud/RR.operation'
  import crudOperation from '@crud/CRUD.operation'
  import udOperation from '@crud/UD.operation'
  import pagination from '@crud/Pagination'


  // crud交由presenter持有
  const defaultForm = { id: null, name: null, station: null, serialNo: null, channel: null, isDefault: null, status: false }
  export default {
    name: 'WaterLine',
    components: {crudOperation, rrOperation, udOperation,pagination},
    cruds() {
      return CRUD({ title: '视频', url: 'api/video/', crudMethod: { ...crudVideo },
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
        stations:[],
        permission: {
          add: ['admin', 'video:add'],
          edit: ['admin', 'video:edit'],
          del: ['admin', 'video:del']
        },
        rules: {
          name: [
            { required: true, message: '请输入监控点', trigger: 'blur' }
          ],
          serialNo: [
            { required: true, message: '请输入序列号', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      // 新增与编辑前做的操作
      [CRUD.HOOK.afterToCU](crud, form) {
        if (form.id != null) {
        } else {
        }
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
