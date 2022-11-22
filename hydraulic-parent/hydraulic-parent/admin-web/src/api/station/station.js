import request from '@/utils/request'

export function getStationData() {
  return request({
    url: 'api/station/getStationData',
    method: 'get'
  })
}

export default { getStationData }
