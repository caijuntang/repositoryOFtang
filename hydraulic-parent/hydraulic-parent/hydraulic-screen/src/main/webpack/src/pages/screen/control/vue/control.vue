<template>
  <!--search-->
  <div style="margin:10px">
      内河水位：<Input v-model="insideVal"  style="width: 100px" disabled />
      前池水位：<Input v-model="foreVal" style="width: 100px" disabled />
      外河水位：<Input v-model="outsideVal"  style="width: 100px" disabled />

  </div>
</template>

<script>
  import $ from '../../../../resources/lib/axios.js'
  import Date from '../../../../resources/lib/date.js'

  import Qs from 'qs'

  export default {
    name: 'video',
    data: function () {
      return {
        insideVal: 0.01,
        foreVal: 0.01,
        outsideVal:0.01,
        timer:''
      }
    },
    methods: {
      init: function () {
        this.getWaterLine()
        this.timer = setInterval(() => {
          this.getWaterLine()
        },10000)
      },
      getWaterLine :function(){
        $.ajax.post('/index/getWaterLine')
          .then(data => {
            let code = data.code;
           if("200"!==code){
             return
           }
           this.insideVal=data.insideVal
           this.outsideVal=data.outsideVal
           this.foreVal=data.foreVal
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

  .warnfont {
    color: #FF0000;
  }

</style>
