module.exports = {
  publicPath: "./",
  devServer: {
    port: 3100,
    historyApiFallback: true,
    proxy: {
      "/ide": {
        target: "http://127.0.0.1:3001",
        changeOrigin: true,
      },
      "/auth": {
        target: "http://127.0.0.1:3001",
        changeOrigin: true,
      },
    },
  },
};
