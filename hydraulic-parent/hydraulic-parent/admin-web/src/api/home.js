import request from '@/utils/request'

export function getWeatherNow(stationId) {
  return request({
    url: 'api/weather/getWeatherNow?stationId=' + stationId,
    method: 'get'
  })
}

export function getWeatherWeek(stationId) {
  return request({
    url: 'api/weather/getWeatherWeek?stationId=' + stationId,
    method: 'get'
  })
}

export default { getWeatherNow, getWeatherWeek }
