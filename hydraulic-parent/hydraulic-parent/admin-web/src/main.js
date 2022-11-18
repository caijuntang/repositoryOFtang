import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css'
import 'element-ui/lib/theme-chalk/index.css'
import Element from 'element-ui'
// 数据字典
// import dict from './components/Dict'
// 权限指令
import checkPer from '@/utils/permission'
import permission from './components/Permission'
import './assets/styles/element-variables.scss'
// global css
import './assets/styles/index.scss'
import App from './App'
import screen from './views/screen'
import store from './store'
import VueBaiduMap from 'vue2-baidu-map'
import router from './router/routers'
import './assets/icons' // icon
import './router/index' // permission control
import dataV from '@jiaminghi/data-view'

Vue.use(checkPer)
Vue.use(permission)
Vue.use(VueBaiduMap, {
  ak: 'Mg8b5K05LplE6Cz5IQuoar7tW19kIuMP'
})
// Vue.use(dict)
Vue.use(dataV)
Vue.use(Element, {
  size: Cookies.get('size') || 'small' // set element-ui default size
})

Vue.config.productionTip = false
new Vue({
  el: '#app',
  router,
  screen,
  store,
  render: h => h(App)
})
