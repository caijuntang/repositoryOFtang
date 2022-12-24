import request from '@/utils/request'

export function getVideoData(stationId) {
  return request({
    url: 'api/video/getVideoMap?stationId=' + stationId,
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

export function add(data) {
  return request({
    url: 'api/video',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/video',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/video',
    method: 'put',
    data
  })
}

export default { add, edit, del, getVideoData, saveVideoChannel }
