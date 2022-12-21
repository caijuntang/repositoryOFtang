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

export function getRecordList(stationId) {
  return request({
    url: 'api/control/getAlarmRecord?stationId=' + stationId,
    method: 'get'
  })
}

export function getWXReceivers() {
  return request({
    url: 'api/control/getWXReceivers',
    method: 'get'
  })
}

export default { getWaterLine, getPumpCount, getPumpData, getWXReceivers,getRecordList}
