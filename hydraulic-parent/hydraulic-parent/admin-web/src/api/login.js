import request from '@/utils/request'

export function login(un, pwd, uid) {
  return request({
    url: 'auth/login',
    method: 'post',
    data: {
      username:un,
      password:pwd,
      uuid:uid
    }
  })
}

export function getInfo() {
  return request({
    url: 'auth/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: 'auth/logout',
    method: 'delete'
  })
}
