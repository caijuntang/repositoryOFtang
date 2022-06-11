<template>
  <!--search-->
  <div style="margin:10px">
    <Button type='primary' @click="init">直播</Button>
    <div class="hello-ezuikit-js">
      <div id="video-container" style="width:600px;height:400px"></div>
      <div>
        <Button  shape="circle"   @mousedown="movePtz('1','0')" @mouseup="movePtz('0','0')">
          <Icon type="arrow-up-b"  size="50px"></Icon>
        </Button>
        <Button  shape="circle" @mousedown="movePtz('1','1')" @mouseup="movePtz('0','1')">
          <Icon type="arrow-down-b" size="50px"></Icon>
        </Button>
        <Button  shape="circle"  @mousedown="movePtz('1','2')" @mouseup="movePtz('0','2')">
          <Icon type="arrow-right-b" size="50px"></Icon>
       </Button>
        <Button  shape="circle"  @mousedown="movePtz('1','3')" @mouseup="movePtz('0','3')">
          <Icon type="arrow-left-b" size="50px"></Icon>
        </Button>
        <Button @click="capturePicture()">截图</Button>
        <Button @click="ezopenStartTalk()">开始对讲</Button>
        <Button @click="ezopenStopTalk()">结束对讲</Button>
        <Button @click="fullScreen()">全屏</Button>
      </div>
    </div>
  </div>
</template>

<script>
  import $ from '../../../../resources/lib/axios.js'
  import Date from '../../../../resources/lib/date.js'
  import EZUIKit from "ezuikit-js"

  import Qs from 'qs'

  export default {
    name: 'video',
    data: function () {
      return {
        player: null,
        urlHost: 'https://open.ys7.com',
        ptzStart:"/api/lapp/device/ptz/start",
        ptzStop:"/api/lapp/device/ptz/stop",
        token: "at.c3wpzrlqb77tr7q6b5zq5qo19yk3vppx-77cl79pc4s-1tu9bia-ttgrzh5od",
        deviceNo: "C39292726",
        channel:'6'
      }
    },
    methods: {
      init: function () {
        this.player = new EZUIKit.EZUIKitPlayer({
          id: 'video-container', // 视频容器ID
          accessToken: this.token,
          url: 'ezopen://open.ys7.com/C39292726/6.hd.live',
          template: 'simple', // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版; theme-可配置主题；
          plugin: ['talk'],                       // 加载插件，talk-对讲
          width: 600,
          height: 400,
        });
        this.player.play()
        this.player.closeSound();
      },
      capturePicture: function () {
        let capturePicturePromise = this.player.capturePicture();
        capturePicturePromise.then((data) => {
          console.log("promise 获取 数据", data)
        })
      },
      openSound: function () {
        let openSoundPromise = this.player.openSound();
        openSoundPromise.then((data) => {
          console.log("promise 获取 数据", data)
        })
      },
      closeSound: function () {
        let closeSoundPromise = this.player.closeSound();
        closeSoundPromise.then((data) => {
          console.log("promise 获取 数据", data)
        })
      },
      movePtz: function (index,direction) {
        let params = new URLSearchParams()
        let ptzUrl=this.ptzStop
        if(1===index){
          ptzUrl=this.ptzStart
        }
        params.append("accessToken",this.token)
        params.append("deviceSerial",this.deviceNo)
        params.append("channelNo",'1')
        params.append("direction",direction)
        $.ajax.post(this.urlHost+ptzUrl,params).then(resp => {
          if("200"!==resp.code){
            console.log(resp.msg)
          }

        }).catch(function (reason) {
          console.log(reason)
        })

      },
      ezopenStartTalk: function () {
        this.player.startTalk();
      },
      ezopenStopTalk: function () {
        this.player.stopTalk();
      },
      fullScreen:function() {
        var playPromise = this.player.fullScreen();
        playPromise.then((data) => {
          console.log("promise 获取 数据", data)
        })
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
      this.init()
    }
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
