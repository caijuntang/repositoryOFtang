import axios from 'axios'

const AjaxUtil = {
  ROOT: '/newcommonlog'
}

AjaxUtil.ajax = axios.create({
  baseURL: AjaxUtil.ROOT,
  timeout: 120000,
  responseType: 'json'
})

AjaxUtil.ajax.interceptors.response.use(
  function (response) {
    return response.data
  },
  function (error) {
    return Promise.reject(error)
  }
)

export default AjaxUtil
