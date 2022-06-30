export default {
  getParamValue: function (key) {
    let location = decodeURI(window.location.href)
    let params = location.substring(location.indexOf('?') + 1).split('&')
    for (let i = 0; i < params.length; i++) {
      let kv = params[i].split('=')
      if (kv.length === 2 && kv[0] === key) {
        return kv[1]
      }
    }
    return ''
  },
  
  getParamStr: function () {
    let location = decodeURI(window.location.href)
    let index = location.indexOf('?');
    if(index==-1){
      return ''
    }
    let param= location.substring(index + 1)
    if(param){
      return param
    }
    return ''
  }
}
