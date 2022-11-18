'use strict'
const utils = require('../build/utils')

const pathResolve = utils.pathResolve

function jsPathResolve(path) {
  return pathResolve.src('pages', path)
}
function htmlPathResolve(path) {
  return pathResolve.dist('templates/html', path)
}

const jsEntryMap = [
  { name: 'control', jsPath: jsPathResolve('screen/control/control.js'), entryPath: htmlPathResolve('screen/control/control.html')},
  { name: 'index', jsPath: jsPathResolve('screen/index/index.js'), entryPath: htmlPathResolve('screen/index/index.html')}
]

module.exports = jsEntryMap
