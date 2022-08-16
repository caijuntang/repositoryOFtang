import request from '@/utils/request'

export function getWaterLine() {
  return request({
    url: 'api/control/getWaterLine',
    method: 'get'
  })
}
