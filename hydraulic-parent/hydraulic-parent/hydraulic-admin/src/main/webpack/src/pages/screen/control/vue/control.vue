<template>
  <!--search-->
  <div style="margin:10px">
    <card :bordered="false" dis-hover>
      <p slot="title" style="text-align: center">宣城市宣州区敬亭圩排涝站水位监测</p>
      <card class="card-content">
        外河水位：<Input v-model="outsideVal" style="width: 70px;" readonly/>米
      </card>
      <card class="card-building">
        泵站机房
      </card>
      <card class="card-content">
        前池水位：<Input v-model="foreVal" style="width: 70px;" readonly/>米
      </card>
      <card class="card-content">
        内河水位：<Input v-model="insideVal" style="width: 70px;" readonly/>米
      </card>
    </card>
  </div>
</template>

<script>
  import $ from '../../../../common/lib/axios.js'
  import Date from '../../../../common/lib/date.js'

  import Qs from 'qs'

  export default {
    name: 'control',
    data: function () {
      return {
        insideVal: 0.01,
        foreVal: 0.01,
        outsideVal: 0.01,
        timer: ''
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
        $.ajax.post('/control/getWaterLine')
          .then(data => {
            let code = data.code;
            if ("200" !== code) {
              return
            }
            this.insideVal = data.insideVal
            this.outsideVal = data.outsideVal
            this.foreVal = data.foreVal
          }).catch(function (reason) {
          console.log(reason)
        })
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
