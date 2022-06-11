'use strict'
const path = require('path')
const utils = require('./utils')
const config = require('../config')
const webpack = require('webpack')
const vueLoaderConfig = require('./vue-loader.conf')
const HtmlWebpackPlugin = require('html-webpack-plugin')
var WriteFilePlugin = require('write-file-webpack-plugin')
var jsEntryMapData = require('../config/js-entry-map')
const CopyWebpackPlugin = require('copy-webpack-plugin')

const pathResolve = utils.pathResolve
const entries = {}
jsEntryMapData.forEach(item => {
  entries[item.name] = [item.jsPath]
})

const htmlWebpackPlugins = utils.getHtmlAndJspMapPath().map(function (mapEntry) {
  const conf = {
    filename: mapEntry.dist, // 生成的html存放路径，相对于path
    template: mapEntry.src, // html模板路径
    inject: false,
    cache: false
  }
  const indexPage = jsEntryMapData.filter(item => {
    return item.entryPath === mapEntry.dist
  })[0]
  if (indexPage) {
    conf.inject = 'body'
    conf.chunks = ['polyfill', 'vendor', 'manifest', indexPage.name]
    conf.hash = true
  }
  return new HtmlWebpackPlugin(conf)
})
module.exports = {
  context: pathResolve.root(),// webpack目录
  entry: Object.assign({
    polyfill: [pathResolve.src('polyfills/index.js')]
  }, entries),
  output: {
    path: config.build.assetsRoot,
    filename: utils.assetsPath('js/[name].js'),
    chunkFilename: utils.assetsPath('js/[id].chunk.js?[chunkhash]'),
    publicPath: process.env.NODE_ENV === 'production'
      ? config.build.assetsPublicPath
      : config.dev.assetsPublicPath,
  },
  resolve: {
    extensions: ['.js', '.vue', '.json', '.css'],
    alias: {
      vue: 'vue/dist/vue.js',
      '@': pathResolve.src()
    }
  },
  externals: {
    jquery:'window.$'
  },
  module: {
    rules: [
      // ...(config.dev.useEslint ? [{
      //   test: /\.(js|vue)$/,
      //   loader: 'eslint-loader',
      //   enforce: 'pre',
      //   include: [pathResolve.src()],
      //   options: {
      //     formatter: require('eslint-friendly-formatter'),
      //     emitWarning: !config.dev.showEslintErrorsInOverlay
      //   }
      // }] : []),
      {
        test: /\.vue$/,
        use: [
          {
            loader: 'vue-loader',
            options: vueLoaderConfig
          },
          {
            loader: 'iview-loader',
            options: {
              prefix: true
            }
          }
        ]
      },
      {
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: /node_modules/,
        include: [
          pathResolve.src()
        ]
      },
      // {
      //   test: /\.html$/,
      //   loader: 'html?-minimize' // 避免压缩html,https://github.com/webpack/html-loader/issues/50
      // },
      {
        test: /\.jsp$/,
        loader: 'raw-loader'
      },
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('img/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('media/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
        }
      }
    ]
  },
  plugins: [
    new CopyWebpackPlugin([
      {
        from: pathResolve.src('resources'),
        to: pathResolve.dist('resources'),
        ignore: ['README.md']
      }
    ]),
    ...htmlWebpackPlugins,
    // copy custom static assets
    new WriteFilePlugin({
      test: /^((?!\.hot-update).)*$/,
    })
  ]
}
