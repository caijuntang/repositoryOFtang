import request from '@/utils/request'

export function getVideoData(stationId) {
  return request({
    url: 'api/video/getVideoMap?stationId='+stationId,
    method: 'get'
  })
}
export function saveVideoChannel(data) {
  return request({
    url: 'api/video/saveVideoChannel',
    method: 'post',
    data
  })
}

export default { getVideoData,saveVideoChannel}
