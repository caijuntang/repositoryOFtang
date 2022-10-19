<template>
<!--  <dv-full-screen-container>-->
  <div id="index" ref="appRef">
    <div class="bg">
      <dv-loading v-show="loading">Loading...</dv-loading>
      <div class="host-body">
        <div>
          <!-- 顶部title部分 -->
          <el-row>
            <el-col :span="6">
              <dv-decoration-8 class="title_right" :color="['#008CFF', '#00ADDD']"/>
            </el-col>
            <el-col :span="12">
              <dv-decoration-11 class="title_center"  :color="['#008CFF', '#00ADDD']">
              <div class="title_text">宣城市宣州区敬亭圩排涝站</div>
              </dv-decoration-11>
            </el-col>
            <el-col :span="6">
              <div class="title_time">{{ dateTime + " ("+dateWeek+")" }}</div>
              <dv-decoration-8 :reverse="true" class="title_left" :color="['#008CFF', '#00ADDD']"/>
            </el-col>
          </el-row>
          <!-- 主体部分 -->
          <el-row>
            <!-- 第一列 -->
            <el-col :span="6">
              <div class="left_box1">
                <dv-border-box->
                  <!-- 泵站地图-->
                  <div id="china-map">

                  </div>
                </dv-border-box->
              </div>
              <!-- 轮播表格部分 -->
              <div class="left_box2">
                <dv-border-box-11 style="padding-top: 10px" title="泵机电流数据">
                  <dv-scroll-board :config="dataA_info" class="carousel_list"  oddRowBGC="#fff"/>
                </dv-border-box-11>
              </div>
              <!-- 轮播表格部分 -->
              <div class="left_box3">
                <dv-border-box-11 style="padding-top: 10px" title="泵机电压数据">
                    <dv-scroll-board :config="dataV_info" class="carousel_list"  oddRowBGC="#fff"/>
                </dv-border-box-11>
              </div>
            </el-col>
            <!-- 第二列 -->
            <el-col :span="12">
              <!-- 视频部分 -->
              <div id="video" style="text-align: center">
                <dv-border-box-7 style="width:45%;height:45%;display:inline-block">

                </dv-border-box-7>
                <dv-border-box-7 style="width:45%;height:45%;display:inline-block">
                </dv-border-box-7>
                <dv-border-box-7 style="width:45%;height:45%;display:inline-block">
              </dv-border-box-7>
                <dv-border-box-7 style="width:45%;height:45%;display:inline-block">
                </dv-border-box-7>


              </div>
              <!-- 视频走马灯 -->
              <div class="line_center">
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
            </el-col>
            <!-- 第三列 -->
            <el-col :span="6">
              <!-- 轮播排行榜部分 -->
              <div class="right_box1">
                <dv-border-box-11 title="水位详情" style="text-align: center;padding-top:80px">
                    <dv-water-level-pond  :config="insideLine" style="width:150px;height:180px;display: inline-block;white-space: pre-wrap;" />
                    <dv-water-level-pond  :config="outsideLine" style="width:150px;height:180px;display: inline-block;white-space: pre-wrap;" />
                </dv-border-box-11>
              </div>
              <!-- 虚线柱状图部分 -->
              <div class="right_box2">
                <dv-border-box-11 title="当地天气">
                </dv-border-box-11>
              </div>
              <!-- 部分 -->
              <div class="right_box3" >
                <dv-border-box-11  title="消息通知">
                </dv-border-box-11>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
  </div>
<!--  </dv-full-screen-container>-->

</template>

<script>
  import drawMixin from "../utils/drawMixin"; //自适应缩放
  import {parseTime} from "../utils/index.js"; //日期格式转换
  import * as echarts from "echarts";
  import geoJson from '@/utils/china.json'

  export default {
    mixins: [drawMixin],
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
        //周几
        weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],

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
        }
      }
    },

    mounted() {
      echarts.registerMap('china', geoJson);
      //获取实时时间
      this.timeFn();
      //加载loading图
      this.cancelLoading();
      //中国地图
      this.china_map();
      //左侧玫瑰饼图
      // this.Rose_diagram();
      //左侧柱状图
      this.columnar();
      //中间折线图
      this.line_center_diagram();
      //虚线柱状图
      this.dotter_bar();

    },
    beforeDestroy() {
      //离开时删除计时器
      clearInterval(this.timing);
    },
    methods: {
      //右上角当前日期时间显示：每一秒更新一次最新时间
      timeFn() {
        this.timing = setInterval(() => {
          //获取当前时分秒
          this.dateTime = parseTime(new Date(), "YYYY-MM-DD HH:mm:ss");
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
      china_map() {
        let mapChart = echarts.init(document.getElementById("china-map")); //图表初始化，china-map是绑定的元素
        window.onresize = mapChart.resize; //如果容器变大小，自适应从新构图
        let series = []; //存放循环配置项
        let res = []; //存放射线的起始点和结束点位置
        let province = []; //存放有射线的省的名字，以此来拿到对应省份的坐标
        //提前存好的所有省份坐标，用于后面根据名字拿到对应的左边
        let chinaGeoCoordMap = {
          安徽: [117.2461, 31.0361],
        };
        //后台给的数据模拟
        let lineData = [];
        let index_data = new Set(province); //把省的名字去重
        let data_res = []; //定义一个新的变量存放省的坐标

        //注意这里一定要用name和value的形式。不是这个格式的散点图显示不出来
        index_data.forEach((item) => {
          data_res.push({
            name: item, //每个省的名字
            value: chinaGeoCoordMap[item] //每个省的坐标
          });
        });
        //循环往series内添加配置项
        series.push(
          {
            type: "map",
            mapType: "china",
            zlevel: 1,
            roam: true,
            geoIndex: 0,
            tooltip: {
              show: true,
            },
            itemStyle: {
              areaColor: "#00196d",
              borderColor: "#0a53e9",
            },
            emphasis: {
              show: true,
              label: {
                normal: {
                  show: true, // 是否显示对应地名
                  textStyle: {
                    color: "#fff"
                  },
                },
              },
              itemStyle: {
                areaColor: "#00196d",
                borderColor: "#0a53e9"
              },
            },
          }
        );
        //配置
        let option = {
          //title可要可不要

          legend: {
            show: true,
            selected: {},
            x: "left",
            orient: "vertical",
            textStyle: {
              color: "white"
            },
            data: [],
          },
          //鼠标移上去的弹框
          tooltip: {
            trigger: "item",
            show: true,
          },
          //geo：这是重点
          geo: {
            map: "china", //中国地图
            zoom: 1.2, //缩放倍数
            roam: false, //是否允许缩放和拖拽地图
            label: {
              normal: {
                show: true, // 是否显示省份名字，现在是隐藏的状态，因为和散点图的名字重叠了。如果需要就true
                textStyle: {
                  //名字的样式
                  color: "#fff"
                },
              },
              emphasis: {
                show: true
              },
            },
            //地图样式
            itemStyle: {
              normal: {
                borderColor: "#293171", //地图边框颜色
                borderWidth: "2", //地图边框粗细
                areaColor: "#0A0F33" //地图背景色
              },
              //省份地图阴影
              emphasis: {
                areaColor: null,
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowBlur: 20,
                borderWidth: 0,
                shadowColor: "#fff"
              },
            },
          },
          series: series, //图表配置
        };
        mapChart.setOption(option); //生成图表
      },
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
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height:100%;
    margin:0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #d2dbff;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
    height: 90%;
  }
  //页面样式部分！！！！
  #index {
    color: #d3d6dd;
    width: 1980px;
    height: 1040px;
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
      height: 50px;
    }

    //顶部左边装饰效果
    .title_right {
      width: 100%;
      height: 50px;
      margin-top: 18px;
    }

    //顶部中间装饰效果
    .title_center {
      width: 100%;
      height: 50px;
    }

    //顶部中间文字数据可视化系统
    .title_text {
      text-align: center;
      font-size: 22px;
      font-weight: bold;
      margin-top: 8px;
      color: #ffffff;
    }

    //时间日期
    .title_time {
      text-align: center;
    }

    //中国地图
    #china-map {
      height: 70%;
      width: 55%;
    }
    #video {
      height: 660px;
      width: 100%;
    }

    //中间折线图
    .line_center {
      width: 100%;
      height: 288px;
    }

    //左1模块
    .left_box1 {
      height: 310px;
      width: 100%;
      margin-bottom: 10px;
      position: relative;
    }

    //左2模块
    .left_box2 {
      height: 310px;
      width: 100%;
      margin-bottom: 10px;
    }

    //左3模块
    .left_box3 {
      height: 310px;
      width: 100%;
    }

    //右1模块
    .right_box1 {
      height: 310px;
      width: 100%;
      margin-bottom: 10px;
    }

    //右2模块
    .right_box2 {
      height: 310px;
      width: 100%;
      margin-bottom: 10px;
    }

    //右3模块
    .right_box3 {
      height: 310px;
      width: 100%;
    }

    //左1模块-玫瑰饼图
    /*#Rose_diagram {*/
    /*  height: 70%;*/
    /*  width: 55%;*/
    /*}*/

    //左1模块-圆环图
    .left_box1_rose_right {
      height: 85%;
      width: 55%;
      position: absolute;
      right: 0;
      top: 0;
    }

    //左1模块-文字部分
    .rose_text {
      display: inline-block;
      margin-top: 4%;
      margin-left: 4%;
    }

    // 左1模块-￥符号样式
    .coin {
      font-size: 20px;
      color: #ffc107;
    }

    //左1模块-（件）样式
    .colorYellow {
      color: yellowgreen;
    }

    //左1模块-数字样式
    .rose_text_nmb {
      font-size: 20px;
      color: #00b891;
    }

    //左2模块 柱状图
    #columnar {
      height: 97%;
      width: 95%;
      margin-left: 3%;
      margin-top: 5px;
    }

    //折线图
    #line_center_diagram {
      height: 100%;
      width: 100%;
    }

    //轮播表格
    .carousel_list {
      width: 95%;
      height: 95%;
      margin: 50px 10px 10px 10px;
    }

    //虚线柱状图
    #dotter_bar {
      width: 100%;
      height: 100%;
    }

    //锥形图
    .cone_box {
      width: 95%;
      height: 97%;
      margin-left: 3%;
    }
  }
</style>


