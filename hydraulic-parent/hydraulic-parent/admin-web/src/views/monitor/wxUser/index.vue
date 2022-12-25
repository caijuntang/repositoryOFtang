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
      <el-button
        v-permission="permission.add"
        class="filter-item"
        size="mini"
        type="primary"
        icon="el-icon-plus"
        :loading="syncLoad"
        @click="syncWxUser"
      >
        公众号用户同步
      </el-button>
    </div>
    <!--表单渲染-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="650px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="100px">
        <el-form-item  label="昵称" prop="nickName">
          <el-input v-model="form.nickName" style="width:180px" />
        </el-form-item>
        <el-form-item label="openid" prop="openid">
          <el-input v-model="form.openid" style="width: 180px;"  :disabled=true />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName" style="display: inline-block;width: 280px">
          <el-input v-model="form.realName" placeholder="请填写真实姓名" />
        </el-form-item>
        <el-form-item  label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="备注信息" style="width: 300px;" />
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
      <el-table-column prop="nickName" label="昵称" align="center" />
      <el-table-column prop="openid" label="openid" align="center" width="340px" />
      <el-table-column prop="realName" label="真实姓名" align="center" />
      <el-table-column prop="remark" label="备注" align="center" width="300px" />
      <el-table-column v-if="checkPer(['admin','wxUser:edit','wxUser:del'])" label="操作" width="130px" align="center" fixed="right">
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
  import crudWxUser from '@/api/monitor/wxUser'
  import CRUD, { presenter, header, form, crud } from '@crud/crud'
  import rrOperation from '@crud/RR.operation'
  import crudOperation from '@crud/CRUD.operation'
  import udOperation from '@crud/UD.operation'
  import pagination from '@crud/Pagination'

  // crud交由presenter持有
  const defaultForm = { id: null, nickName: null, realName: null, openid: null,remark:null}
  export default {
    name: 'WxUser',
    components: {crudOperation, rrOperation, udOperation , pagination},
    cruds() {
      return CRUD({ title: '消息接受人', url: 'api/wxReceiver/', crudMethod: { ...crudWxUser },
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
        permission: {
          add: ['admin', 'wxUser:add'],
          edit: ['admin', 'wxUser:edit'],
          del: ['admin', 'wxUser:del']
        },
        syncLoad:false,
        rules: {
          nickName: [
            { required: true, message: '请输入昵称', trigger: 'blur' }
          ],
          openid: [
            { required: true, message: '请输入openid', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      syncWxUser(){
        const _this=this
        _this.syncLoad=true
        crudWxUser.syncWxUser().then(() => {
          _this.syncSuccessNotify()
          _this.crud.toQuery()
          _this.syncLoad=false
        }).catch((e) => {
          console.log(e)
        })
      },
      syncSuccessNotify() {
        this.$notify({
          title: '数据同步成功',
          type: 'success',
          duration: 2500
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
</style>
