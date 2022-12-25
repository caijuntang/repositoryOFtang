import request from '@/utils/request'

export function getStationData() {
  return request({
    url: 'api/station/getStationData',
    method: 'get'
  })
}


export function add(data) {
  return request({
    url: 'api/station',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/station',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/station',
    method: 'put',
    data
  })
}

export default { add, del, edit, getStationData }
