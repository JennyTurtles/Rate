let proxyObj = {};
const CompressionPlugin = require("compression-webpack-plugin");
proxyObj['/ws'] = {
    ws: true,
    target: "ws://localhost:8081"
};

proxyObj['/'] = {
        ws: false,
        target: 'http://localhost:8081',
        // target: 'http://106.15.36.190:8081',
        // target: 'http://219.228.76.150:8081',
        changeOrigin: true,
        pathRewrite: {
            '^/api': ''
       },
}
module.exports = {
	productionSourceMap: false,
    publicPath:'./',
    devServer: {
        // host: '0.0.0.0',
        host: 'localhost',
        port: 8080,
        proxy: proxyObj,
		disableHostCheck: true,
    },

    configureWebpack: config => {
        if (process.env.NODE_ENV === 'production') {
            return {
                plugins: [
                    new CompressionPlugin({
                        test: /\.js$|\.html$|\.css/,
                        threshold: 1024,
                        deleteOriginalAssets: false,
						algorithm: 'gzip'
                    })
                ]
            }
        }
    }
}

