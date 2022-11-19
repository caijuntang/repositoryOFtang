import request from '@/utils/request'

export function getWeatherPre(stationId) {
  return request({
    url: 'api/weather/getWeatherPre?stationId=' + stationId,
    method: 'get'
  })
}

export function getWeatherWeek(stationId) {
  return request({
    url: 'api/api/weather/getWeatherWeek?stationId=' + stationId,
    method: 'get'
  })
}

export function getVideoData(stationId) {
  return request({
    url: 'api/video/getVideoData?stationId='+stationId,
    method: 'get'
  })
}

export function getStationData() {
  return request({
    url: 'api/station/getStationData',
    method: 'get'
  })
}

export function getAlarmReport(stationId) {
  return request({
    url: 'api/alarm/getAlarmReport?stationId=' + stationId,
    method: 'get'
  })
}
export default { getWeatherPre, getWeatherWeek, getVideoData, getStationData, getAlarmReport }
