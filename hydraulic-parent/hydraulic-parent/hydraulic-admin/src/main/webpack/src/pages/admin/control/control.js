import Vue from 'vue'
import iView from 'iview'
import VueBus from 'vue-bus'
import 'iview/dist/styles/iview.css'

/** 自定义全局样式 **/
import '../../../resources/css/common.css'

import controlComponent from './vue/control'

Vue.use(iView)
Vue.use(VueBus)

new Vue({
  el: '#control',
  components: {'control-component': controlComponent}
})
