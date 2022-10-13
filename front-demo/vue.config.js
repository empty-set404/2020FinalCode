module.exports = {
  outputDir:__dirname + "/../service/chatroom",
  publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
    lintOnSave: false,
    configureWebpack: (config) => {
        if (process.env.NODE_ENV === 'production') {// 为生产环境修改配置...
            config.mode = 'production';
            config["performance"] = {//打包文件大小配置
                "maxEntrypointSize": 10000000,
                "maxAssetSize": 30000000
            }
        }
    }
}
// module.exports = {
//     publicPath: './', // 基本路径
//     outputDir: 'dist', // 输出文件目录
//     assetsDir: "static", //放置生成的静态文件目录（js css img）
//     baseUrl: "./",
//     productionSourceMap: false, // 生产环境是否生成 sourceMap 文件
//     lintOnSave: false,
//
//     configureWebpack: (config) => {
//         if (process.env.NODE_ENV === 'production') {// 为生产环境修改配置...
//             config.mode = 'production';
//             config["performance"] = {//打包文件大小配置
//                 "maxEntrypointSize": 10000000,
//                 "maxAssetSize": 30000000
//             }
//         }
//     }
// }
