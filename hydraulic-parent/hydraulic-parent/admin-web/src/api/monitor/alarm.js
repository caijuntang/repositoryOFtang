import request from '@/utils/request'
import {getPumpCount, getPumpData, getWaterLine, getWXReceivers} from "./centerControl";

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

export function getAlarmReport(stationId) {
  return request({
    url: 'api/alarm/getAlarmReport?stationId=' + stationId,
    method: 'get'
  })
}
export default { alarmConfigSave, getAlarmConfig,getAlarmReport}
