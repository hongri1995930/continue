module.exports = {
  root: true,
  env: {
    browser: true,
    es6: true,
  },
  extends: ["plugin:vue/recommended", "eslint:recommended"],
  parserOptions: {
    parser: "babel-eslint",
  },
  rules: {
    "vue/max-attributes-per-line": "off",
    "vue/html-self-closing": "off",
  },
};
