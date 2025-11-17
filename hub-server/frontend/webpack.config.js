const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const { VueLoaderPlugin } = require("vue-loader");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
  entry: "./src/main.js",
  output: {
    path: path.resolve(__dirname, "dist"),
    filename: "bundle.js",
  },
  resolve: {
    extensions: [".js", ".vue", ".json"],
    alias: {
      "@": path.resolve(__dirname, "src"),
    },
  },
  devServer: {
    contentBase: path.join(__dirname, "dist"),
    host: "0.0.0.0",
    port: 8090,
    proxy: {
      "/ide": {
        target: "http://localhost:8081",
        changeOrigin: true,
      },
    },
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: "vue-loader",
      },
      {
        test: /\.js$/,
        loader: "babel-loader",
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        use: [MiniCssExtractPlugin.loader, "css-loader"],
      },
      {
        test: /\.(png|jpe?g|gif|svg|woff2?|eot|ttf|otf)$/i,
        loader: "file-loader",
        options: {
          name: "assets/[name].[hash:8].[ext]",
        },
      },
    ],
  },
  plugins: [
    new VueLoaderPlugin(),
    new HtmlWebpackPlugin({
      title: "Continue Hub 配置控制台",
      templateContent: ({ htmlWebpackPlugin }) => `<!DOCTYPE html>
        <html lang="zh-CN">
          <head>
            <meta charset="UTF-8" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>${htmlWebpackPlugin.options.title}</title>
          </head>
          <body>
            <div id="app"></div>
          </body>
        </html>`,
      inject: true,
    }),
    new MiniCssExtractPlugin({
      filename: "styles.[contenthash:8].css",
    }),
  ],
};
