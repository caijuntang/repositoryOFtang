import request from '@/utils/request'

export function getVideoData(stationId) {
  return request({
    url: 'api/video/getVideoMap?stationId='+stationId,
    method: 'get'
  })
}

export default { getVideoData}
