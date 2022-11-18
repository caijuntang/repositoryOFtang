import Vue from 'vue'
import iView from 'iview'
import VueBus from 'vue-bus'
import 'iview/dist/styles/iview.css'

/** 自定义全局样式 **/
import '../../../common/css/common.css'

import indexComponent from './vue/index'
import dataV from '@jiaminghi/data-view'


Vue.use(iView)
Vue.use(VueBus)
Vue.use(dataV)

new Vue({
  el: '#index',
  components: {'index-component': indexComponent}
})
