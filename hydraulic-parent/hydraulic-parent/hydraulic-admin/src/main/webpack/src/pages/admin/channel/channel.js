import Vue from 'vue'
import iView from 'iview'
import 'iview/dist/styles/iview.css'

import channel from './vue/channel'

Vue.use(iView)

new Vue({
  el: '#channel',
  components: {
    'layout-component': channel
  }
})
