<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.blurry" clearable size="small" placeholder="模糊搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="650px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="100px">
        <el-form-item  label="泵站名称" prop="name">
          <el-input v-model="form.name" style="width:180px" placeholder="请填写泵站名称" />
        </el-form-item>
        <el-form-item label="简称" prop="nameKey">
          <el-input v-model="form.nameKey" placeholder="请填写泵站简称" style="width: 180px;" />
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-select v-model="form.province" placeholder="请选择省份" @change="provinceChange" style="width: 180px">
            <el-option
              v-for="(item,index) in provinces"
              :key="index"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-select v-model="form.city" placeholder="请选择城市" style="width: 180px">
            <el-option
              v-for="(item,index) in cities"
              :key="index"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="泵机数" prop="pumpCount" style="display: inline-block;width: 280px">
          <el-input v-model="form.pumpCount" placeholder="请填写机组数量" style="width: 140px;" />
        </el-form-item>
        <el-form-item label="是否接入" prop="enable" style="display: inline-block;width: 280px">
          <el-switch v-model="form.enable"></el-switch>
        </el-form-item>
        <el-form-item label="是否默认" prop="isDefault" style="display: inline-block;width: 280px">
          <el-switch v-model="form.isDefault"></el-switch>
        </el-form-item>
        <el-form-item label="纬度" prop="attitude" style="display: inline-block;width: 280px">
          <el-input v-model="form.attitude" placeholder="请填写纬度信息" style="width: 140px;" />
        </el-form-item>
        <el-form-item  label="经度" prop="longitude" style="display: inline-block;width: 280px">
          <el-input v-model="form.longitude" placeholder="请填写经度信息" style="width: 140px;" />
        </el-form-item>
        <el-form-item  label="城市编码" prop="cityCode">
          <el-input v-model="form.cityCode" placeholder="请填写泵站所在城市编码" style="width: 180px;" />
        </el-form-item>
        <el-form-item  label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="要点备注" style="width: 300px;" />
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
      <el-table-column prop="name" label="名称" align="center" width="300px" />
      <el-table-column prop="pumpCount" label="泵机数" align="center" />
      <el-table-column prop="nameKey" label="简称" align="center"/>
      <el-table-column prop="province" label="省份" align="center"/>
      <el-table-column prop="city" label="城市" align="center"  />
      <el-table-column prop="attitude" label="纬度" align="center"/>
      <el-table-column prop="longitude" label="经度" align="center" />
      <el-table-column prop="cityCode" label="城市编码" align="center"/>
      <el-table-column prop="enable" label="是否接入" align="center" >
        <template slot-scope="scope">
          <span v-if="scope.row.enable">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="isDefault" label="默认展示" align="center" >
        <template slot-scope="scope">
          <span v-if="scope.row.isDefault">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建日期" align="center" width="300px" />
      <el-table-column v-if="checkPer(['admin','station:edit','station:del'])" label="操作" width="130px" align="center" fixed="right">
        <template slot-scope="scope">
          <udOperation
            :data="scope.row"
            :permission="permission"
            msg="确定删除吗，此操作不能撤销！"
          />
        </template>
      </el-table-column>
    </el-table>
    <pagination />
  </div>
</template>

<script>
  import crudStation from '@/api/monitor/station'
  import CRUD, { presenter, header, form, crud } from '@crud/crud'
  import cityMap from '@/utils/cityMap'
  import rrOperation from '@crud/RR.operation'
  import crudOperation from '@crud/CRUD.operation'
  import udOperation from '@crud/UD.operation'
  import pagination from '@crud/Pagination'

  // crud交由presenter持有
  const defaultForm = { id: null, name: null, nameKey: null, province: null, pumpCount:null,city: null, attitude: null, longitude: null, enable:false ,cityCode:null,isDefault:false}
  export default {
    name: 'Station',
    components: {crudOperation, rrOperation, udOperation , pagination},
    cruds() {
      return CRUD({ title: '泵站', url: 'api/station/', crudMethod: { ...crudStation },
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
        provinces:["安徽省"],
        cities:[],
        permission: {
          add: ['admin', 'station:add'],
          edit: ['admin', 'station:edit'],
          del: ['admin', 'station:del']
        },
        rules: {
          name: [
            { required: true, message: '请输入泵站名称', trigger: 'blur' }
          ],
          pumpCount: [
            { required: true, message: '请输入泵机数', trigger: 'blur' }
          ],
          longitude: [
            { required: true, message: '请输入经度', trigger: 'blur' }
          ],
          attitude: [
            { required: true, message: '请输入经度', trigger: 'blur' }
          ],
          cityCode: [
            { required: true, message: '请输入城市编码', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      // 新增与编辑前做的操作
      [CRUD.HOOK.afterToCU](crud, form) {
        if (form.id != null) {
          this.cities=cityMap[form.province]
        } else {
          this.cities=['宣城','合肥']
        }
      },
      provinceChange(item){
        this.cities=cityMap[item]
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
</style>
