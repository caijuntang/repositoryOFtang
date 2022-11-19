<template>
  <dv-full-screen-container>
    <div id="index" ref="appRef">
      <div class="bg">
        <dv-loading v-show="loading">Loading...</dv-loading>
        <div class="host-body">
          <div>
            <!-- 顶部title部分 -->
            <el-row>
              <el-col :span="6">
                <div class="title_time">{{ dateTime + " ("+dateWeek+")" }}</div>
                <dv-decoration-8 class="title_right" :color="['#008CFF', '#00ADDD']"/>
              </el-col>
              <el-col :span="12">
                <dv-decoration-11 class="title_center"  :color="['#008CFF', '#00ADDD']">
                  <div class="title_text">宣城市宣州区敬亭圩排涝站</div>
                </dv-decoration-11>
              </el-col>
              <el-col :span="6">
                <screenfull id="screenfull" class="right-menu-item hover-effect" />
                <dv-decoration-8 :reverse="true" class="title_left" :color="['#008CFF', '#00ADDD']"/>
              </el-col>
            </el-row>
            <!-- 主体部分 -->
            <el-row>
              <!-- 第一列 -->
              <el-col :span="6">
                <div class="left_box1">
                  <dv-border-box-8>
                    <!-- 泵站地图-->
                    <baidu-map class="map" :dragging="false" :center="location" :zoom="zoom" @ready="handler" >
                      <bm-marker :position="{lng: locationInfo.lng,lat: locationInfo.lat}" :icon="iconStyle">
                        <bm-label :content="locationInfo.siteName" :labelStyle="labelStyleObj" :offset="{width: -20, height: -20}"/>
                      </bm-marker>
                    </baidu-map>
                  </dv-border-box-8>
                </div>
                <!-- 水位部分 -->
                <div class="left_box2">
                  <dv-border-box-11 title="水位详情" :titleWidth=140 style="text-align: center;padding-top:80px">
                    <dv-water-level-pond  :config="insideLine" style="width:130px;height:150px;display: inline-block;white-space: pre-wrap;" />
                    <dv-water-level-pond  :config="outsideLine" style="width:130px;height:150px;display: inline-block;white-space: pre-wrap;" />
                  </dv-border-box-11>
                </div>
                <!-- 泵机电压数据 -->
                <div class="left_box3">
                  <dv-border-box-11 style="padding-top: 2px" :titleWidth=140 title="泵机电压">
                    <dv-scroll-board :config="dataV_info" class="carousel_list"  oddRowBGC="#fff"/>
                  </dv-border-box-11>
                </div>
              </el-col>
              <!-- 第二列 -->
              <el-col :span="12" style="text-align: center">
                <!-- 视频部分 -->
                <div id="video" style="text-align: center">
                  <dv-border-box-7 style="width:45%;height:46%;display:inline-block">
                    <div id="live1" class="videoDiv"></div>
                  </dv-border-box-7>
                  <dv-border-box-7 style="width:45%;height:46%;display:inline-block;margin-left:20px">
                    <div id="live2" class="videoDiv"></div>
                  </dv-border-box-7>
                  <dv-border-box-7 style="width:45%;height:46%;display:inline-block;margin-top:20px">
                    <div id="live3" class="videoDiv"></div>
                  </dv-border-box-7>
                  <dv-border-box-7 style="width:45%;height:46%;display:inline-block;margin-left:20px;margin-top:20px">
                    <div id="live4" class="videoDiv"></div>
                  </dv-border-box-7>
                </div>
                <!-- 预警提示 -->
                <div class="line_center"  >
                    <dv-border-box-10>
                      <dv-scroll-board :config="report_info" class="carousel_list_line"  oddRowBGC="#fff"/>
                    </dv-border-box-10>
                </div>
              </el-col>
              <!-- 第三列 -->
              <el-col :span="6">
                <!-- 轮播排行榜部分 -->
                <div class="right_box1" >
                  <dv-border-box-10>
                    <div id="">
                      <el-carousel :interval="10000" type="card" >
                        <el-carousel-item v-for="item in 6" :key="item">
                          <h3 class="medium">{{ item }}</h3>
                        </el-carousel-item>
                      </el-carousel>
                    </div>
                  </dv-border-box-10>

                </div>
                <!-- 天气 -->
                <div class="right_box2">
                  <dv-border-box-11  title="当地天气" :titleWidth=140>
                  </dv-border-box-11>
                </div>
                <!-- 泵机电流数据 -->
                <div class="right_box3" >
                  <dv-border-box-11 style="padding-top: 2px" title="泵机电流" :titleWidth=140>
                    <dv-scroll-board :config="dataA_info" class="carousel_list"  oddRowBGC="#fff"/>
                  </dv-border-box-11>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </div>
  </dv-full-screen-container>
</template>

<script>
  import {parseTime} from "../utils/index.js"; //日期格式转换
  import Screenfull from '@/components/Screenfull';
  import locateIcon from "@/assets/images/locateIcon.png";
  import  EZUIKit from 'ezuikit-js';
  import {getWaterLine, getWXReceivers, getPumpData, alarmConfigSave, getAlarmConfig} from '@/api/monitor/centerControl'

  export default {
    components: {
      Screenfull
    },
    name:'screen',
    // mixins: [drawMixin],
    data() {
      return {
        //定时器
        timing: null,
        //loading图
        loading: true,
        //时分秒
        dateTime: null,
        //周几
        dateWeek: null,
        player1:null,
        player2:null,
        player3:null,
        player4:null,
        //周几
        weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
        infoWindowShow:false,
        //左侧轮播表格配置项
        dataV_info: {
          header: ["机组", "A相位", "B相位","C相位"],
          data: [
            ["1", "4505.9", "4589.6","5045.4"],
            ["2", "4677.3", "4505.9","4986.2"],
            ["3", "5045.5", "4589.6","4776.8"],
            ["4", "4986.2", "4505.9","5432.3"]
          ],
          evenRowBGC:"#382B47",
          oddRowBGC: "#1B0840",
          headerBGC: "#020308"
        },
        dataA_info: {
          header: ["机组", "A相位", "B相位","C相位"],
          data: [
            ["1", "0.00", "0.00","0.00"],
            ["2", "0.00", "0.00","0.00"],
            ["3", "0.00", "0.00","0.00"],
            ["4", "0.00", "0.00","0.00"]
          ],
          evenRowBGC:"#382B47",
          oddRowBGC: "#1B0840",
          headerBGC: "#020308"
        },
        report_info: {
          header: ["消息类型", "内容", "时间"],
          data: [
            ["水位告警", "内河水位超警戒线（7米），请及时处置！", "2022-12-3 15:30:12"],
            ["泵机维护", "2号泵机累计运行400个小时请及时维护", "2022-12-3 15:30:12"],
            ["人员入侵", "高压电房有人员入侵，请及时关注！", "2022-12-3 15:30:12"],
            ["水位告警", "内河水位超警戒线（7米），请及时处置！","2022-11-5 15:30:12"],
            ["异常天气", "今日12时有暴雨天气，请及时关注水位变化！","2022-11-20 15:30:12"],
            ["异常天气", "今日12时有暴雨天气，请及时关注水位变化！","2022-11-20 15:30:12"]
          ],
          columnWidth:[100,500,200],
          evenRowBGC:"#382B47",
          oddRowBGC: "#1B0840",
          headerBGC: "#020308"
        },
        insideLine:{
          data:[7.6],
          shape:'roundRect',
          waveHeight:20,
          waveNum:2,
          formatter:'内河:{value}米'
        },
        outsideLine:{
          data:[8.4],
          shape:'roundRect',
          waveHeight:20,
          waveNum:2,
          formatter:'外河:{value}米'
        },
        BMap: null,
        // 地图显示的中心坐标
        location: {
          lng: '118.3176',
          lat: '31.2604',
          siteName:'龙窝湖泵站'
        },
        // 点击后的当前坐标点位的信息
        locationInfo: {
          lng: '118.7573',
          lat: '30.9879',
          siteName: '敬亭圩泵站'
        },
        // 缩放，15基本上就可以看附近的一些街道了
        zoom: 8.5,
        iconStyle:{
          url:locateIcon,
          size:{
            width:'1px',
            height:'3px'
          }
        },
        labelStyleObj:{
          // fontColor:'#bfddf9',
          // fontColor:'white',
          // fontSize:'3px',
          border:'0px',
          backgroundColor:'#5076ed',
          opacity: 0.6,
        }
      }
    },

    mounted() {
      //获取实时时间
      this.timeFn();
      //加载loading图
      this.cancelLoading();
      //百度地图
      // this.initVideo()
    },
    beforeDestroy() {
      //离开时删除计时器
      clearInterval(this.timing);
      this.player1.stop()
      this.player2.stop()
      this.player3.stop()
      this.player4.stop()
    },
    methods: {
      //右上角当前日期时间显示：每一秒更新一次最新时间
      timeFn() {
        this.timing = setInterval(() => {
          //获取当前时分秒
          this.dateTime = parseTime(new Date(), "{y}-{m}-{d} {h}:{i}:{s}");
          //获取当前周几
          this.dateWeek = this.weekday[new Date().getDay()];
        }, 1000);
      },
      //loading图
      cancelLoading() {
        setTimeout(() => {
          this.loading = false;
        }, 500);
      },
      //中国地图
      initBMap() {
        this.infoWindowShow = true
        // 创建地址解析器的实例
        // const geocoder = new BMap.getGeoCoord()
        // geocoder.getLocation(this.location, rs => {
        //   详细地址信息如下
        // this.locationInfo.siteName = rs.address
        // })
      },
      initVideo(){
        this.player1 = new EZUIKit.EZUIKitPlayer({
          id: 'live1', // 视频容器ID
          accessToken: 'at.5d7x50n21082t0bi22glgmrl4h90m86t-8oix5zj9pz-0q569ha-ygwvf1rjn',
          url: 'ezopen://open.ys7.com/J59438351/1.live',
          audio:0,
          height:230
        })
        // player1.play()
        this.player2 = new EZUIKit.EZUIKitPlayer({
          id: 'live2', // 视频容器ID
          accessToken: 'at.5d7x50n21082t0bi22glgmrl4h90m86t-8oix5zj9pz-0q569ha-ygwvf1rjn',
          url: 'ezopen://open.ys7.com/J59438499/1.live',
          audio:0,
          height:230
        })
        // player2.play()

        this.player3 = new EZUIKit.EZUIKitPlayer({
          id: 'live3', // 视频容器ID
          accessToken: 'at.5d7x50n21082t0bi22glgmrl4h90m86t-8oix5zj9pz-0q569ha-ygwvf1rjn',
          url: 'ezopen://open.ys7.com/J59438550/1.live',
          audio:0,
          height:230
        })
        // player3.play()

        this.player4 = new EZUIKit.EZUIKitPlayer({
          id: 'live4', // 视频容器ID
          accessToken: 'at.5d7x50n21082t0bi22glgmrl4h90m86t-8oix5zj9pz-0q569ha-ygwvf1rjn',
          url: 'ezopen://open.ys7.com/J59438687/1.live',
          audio:0,
          height:230
        })
        // player4.play()

      },
      handler({ BMap, map }) {
        this.BMap = BMap
        this.infoWindowShow = true
        map.setMapStyleV2({styleId:'823c414bc80ff3e812451f0f271e6ded'})
      },
      getLocationPoint(e) {
        // 点击地图后显示信息窗体
        this.infoWindowShow = true
        this.locationInfo.lng = e.point.lng
        this.locationInfo.lat = e.point.lat
        // 创建地址解析器的实例
        // const geocoder = new BMap.getGeoCoord()
        // geocoder.getLocation(e.point, rs => {
        // 详细地址信息如下
        // this.locationInfo.siteName = rs.address
        // })
      },
      infoWindowClose() {
        // 关闭信息窗体
        this.infoWindowShow = false
      },
      infoWindowOpen() {
        this.infoWindowShow = true
      }
    }
  }
</script>

<style lang="scss">
  //全局样式部分！！！！
  * {
    margin: 0;
    padding: 0;
    list-style-type: none;
    outline: none;
    box-sizing: border-box;
  }

  html {
    margin: 0;
    padding: 0;
  }

  body {
    font-family: Arial, Helvetica, sans-serif;
    line-height: 1.2em;
    background-color: #f1f1f1;
    margin: 0;
    padding: 0;
  }

  a {
    color: #343440;
    text-decoration: none;
  }

  //--------------------------------------------
  //页面样式部分！！！！
  #index {
    color: #d3d6dd;
    width:100%;
    height:100%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    transform-origin: left top;
    overflow: hidden;

    .bg {
      //整体页面背景
      width: 100%;
      height: 100%;
      padding: 16px 16px 0 16px;
      /*background-image: url("../assets/pageBg.png"); //背景图*/
      background-color: midnightblue;
      background-size: cover; //背景尺寸
      background-position: center center; //背景位置
    }

    //顶部右边装饰效果
    .title_left {
      width: 100%;
      height: 30px;
    }

    //顶部左边装饰效果
    .title_right {
      width: 100%;
      height: 30px;
    }

    //顶部中间装饰效果
    .title_center {
      width: 100%;
      height: 50px;
    }

    //顶部中间文字数据可视化系统
    .title_text {
      text-align: center;
      font-size: 16px;
      font-weight: bold;
      margin-top: 3px;
      color: #ffffff;
    }

    //时间日期
    .title_time {
      display: inline-block;
      float:left;
      margin-left: 50px;
      font-size: 12px;
    }

    #video {
      height: 530px;
      width: 100%;
    }

    .videoDiv{
      vert-align: middle;
      /*margin-bottom: 50px;*/
    }

    //告警提示区域
    .line_center {
      width: 95%;
      height: 270px;
      margin-top: 10px;
      margin-left: 20px;
    }

    //左1模块
    .left_box1 {
      height: 260px;
      width: 100%;
      margin-bottom: 10px;
      /*position: relative;*/
    }

    //左2模块
    .left_box2 {
      height: 250px;
      width: 100%;
      margin-bottom: 10px;
    }

    //左3模块
    .left_box3 {
      height: 280px;
      width: 100%;
    }

    //右1模块
    .right_box1 {
      height: 260px;
      width: 100%;
      /*margin-bottom: 10px;*/
    }

    //轮播图
    .el-carousel__item h3 {
      color: #475669;
      font-size: 14px;
      opacity: 0.75;
      line-height:100%;
      margin:0;
    }

    .el-carousel__item:nth-child(2n) {
      background-color: #d2dbff;
      width: 70%;
      height: 95%;
    }

    .el-carousel__item:nth-child(2n+1) {
      background-color: #d3dce6;
      width: 70%;
      height: 95%;
    }

    .el-carousel__container {
      position: relative;
      height: 240px;
    }

    //右2模块
    .right_box2 {
      height: 250px;
      width: 100%;
      margin-bottom: 10px;
    }

    //右3模块
    .right_box3 {
      height: 280px;
      width: 100%;
    }

    //轮播表格
    .carousel_list {
      width: 94%;
      height: 94%;
      padding: 5px;
    }
    //轮播表格
    .carousel_list_line {
      width: 100%;
      height: 96%;
      padding: 5px;
    }

    .map {
      width: 100%;
      height: 100%;
      padding: 5px;
    }

    .BMap_cpyCtrl {
      display: none;
    }

    .anchorBL {
      display: none;
    }
    .right-menu-item {
      float:right;
      display: inline-block;
      margin-right: 50px;
      font-size: 14px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }
  }
</style>
