import request from '@/utils/request'

export function syncWxUser() {
  return request({
    url: 'api/wxReceiver/syncWxuser',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'api/wxReceiver',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/wxReceiver',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/wxReceiver',
    method: 'put',
    data
  })
}

export default { add, del, edit, syncWxUser }
