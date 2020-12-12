import Vue from 'vue'
import App from './App.vue'
import axios from "axios"
import router from './router'
import './plugins/element.js'
import  VueResource  from 'vue-resource'


// import ElementUI from 'element-ui'

Vue.config.productionTip = false;
// Vue.use(ElementUI)
Vue.prototype.$axios = axios
Vue.use(VueResource)


// 全局vue添加axios
Vue.prototype.axios = axios;
// 设置默认请求路径
//axios.defaults.baseURL = "wr3ck97y.xiaomy.net:30244/springbootdemo/";
axios.defaults.baseURL = "http://127.0.0.1:8088/springbootdemo/";
// // 拦截器要求axios返回的数据是res.data
// axios.interceptors.response.use(
//     // config => {
//     //     if (localStorage.getItem('Authorization')) {
//     //         config.headers.Authorization = localStorage.getItem('Authorization');
//     //     }
//     //
//     //     return config;
//     // },
//     // error => {
//     //     return Promise.reject(error);
//     // },
//     res => {
//         return res.data;
//     }
// );

new Vue({
    render: h => h(App),
    router
}).$mount('#app')
