import request from '@/utils/request'

export function alarmConfigSave(alarmConfig) {
  return request({
    url: 'api/control/alarmConfigSave',
    method: 'post',
    data: alarmConfig
  })
}
export function getWaterLineAlarm() {
  return request({
    url: 'api/alarm/getWaterLineAlarm',
    method: 'get',
  })
}

export function getAlarmConfig(stationId) {
  return request({
    url: 'api/control/getAlarmConfig?stationId=' + stationId,
    method: 'get'
  })
}



export function getAlarmReport(stationId) {
  return request({
    url: 'api/alarm/getAlarmReport?stationId=' + stationId,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'api/alarm',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/alarm',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/alarm',
    method: 'put',
    data
  })
}

export default {add,del, edit, alarmConfigSave, getAlarmConfig,getAlarmReport}
