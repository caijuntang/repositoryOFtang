<template>
  <!--search-->
  <div class="app-container">
    <div style="margin:10px">
      <el-card :shadow="never">
        <p slot="title" style="text-align: center">宣城市宣州区敬亭圩排涝站水位监测</p>
        <el-card class="card-content">
          外河水位：<el-input v-model="outsideVal" style="width: 70px;" :readonly="true" />米
        </el-card>
        <el-card class="card-building">
          泵站机房
        </el-card>
        <el-card class="card-content">
          前池水位：<el-input v-model="foreVal" style="width: 70px;" :readonly="true" />米
        </el-card>
        <el-card class="card-content">
          内河水位：<el-input v-model="insideVal" style="width: 70px;" :readonly="true" />米
        </el-card>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getWaterLine } from '@/api/monitor/centerControl'

export default {
  name: 'CenterControl',
  data() {
    return {
      insideVal: 0.01,
      foreVal: 0.01,
      outsideVal: 0.01,
      timer: ''
    }
  },
  created() {
    this.init()
  },
  beforeDestroy() {
    clearInterval(this.timer)
  },
  methods: {
    init() {
      this.moniterWaterLine()
      this.timer = setInterval(() => {
        this.moniterWaterLine()
      }, 10000)
    },
    moniterWaterLine() {
      getWaterLine().then(data => {
        const code = data.code
        if (code !== '200') {
          return
        }
        this.insideVal = data.insideVal
        this.outsideVal = data.outsideVal
        this.foreVal = data.foreVal
      }).catch(function(reason) {
        console.log(reason)
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
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
</style>
