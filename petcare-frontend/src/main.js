import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import axios from 'axios'

// Axios config
axios.defaults.baseURL = 'http://localhost:8080/api'
axios.interceptors.request.use(c => {
  const t = localStorage.getItem('token')
  if (t) c.headers.token = t
  return c
})
axios.interceptors.response.use(r => r, e => {
  if (e.response && e.response.status === 401) {
    localStorage.removeItem('token')
    location.reload()
  }
  return Promise.reject(e)
})

const app = createApp(App)
app.config.globalProperties.$axios = axios
app.use(ElementPlus)
app.mount('#app')