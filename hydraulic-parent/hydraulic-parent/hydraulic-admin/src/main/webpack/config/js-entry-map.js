'use strict'
const utils = require('../build/utils')

const pathResolve = utils.pathResolve

function jsPathResolve(path) {
  return pathResolve.src('pages', path)
}
function htmlPathResolve(path) {
  return pathResolve.dist('WEB-INF/html', path)
}

const jsEntryMap = [
  { name: 'channel', jsPath: jsPathResolve('admin/channel/channel.js'), entryPath: htmlPathResolve('admin/channel/channel.html')},
  { name: 'control', jsPath: jsPathResolve('admin/control/control.js'), entryPath: htmlPathResolve('admin/control/control.html')},
  { name: 'video', jsPath: jsPathResolve('admin/video/video.js'), entryPath: htmlPathResolve('admin/video/video.html')}
]

module.exports = jsEntryMap
