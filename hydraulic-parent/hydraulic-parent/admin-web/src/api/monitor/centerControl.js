import request from '@/utils/request'

export function getWaterLine(stationId) {
  return request({
    url: 'api/control/getWaterLine?stationId=' + stationId,
    method: 'get'
  })
}

export function getPumpCount(stationId) {
  return request({
    url: 'api/control/getPumpCount?stationId=' + stationId,
    method: 'get'
  })
}

export function getPumpData(stationId) {
  return request({
    url: 'api/control/getPumpData?stationId=' + stationId,
    method: 'get'
  })
}

export function getWXReceivers() {
  return request({
    url: 'api/control/getWXReceivers',
    method: 'get'
  })
}

export function alarmConfigSave(alarmConfig) {
  return request({
    url: 'api/control/alarmConfigSave',
    method: 'post',
    data: alarmConfig
  })
}

export function getAlarmConfig(stationId) {
  return request({
    url: 'api/control/getAlarmConfig?stationId=' + stationId,
    method: 'get'
  })
}
