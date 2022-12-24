import request from '@/utils/request'

export function edit(data) {
  return request({
    url: 'api/alarmRecord',
    method: 'put',
    data
  })
}

export default {edit}
