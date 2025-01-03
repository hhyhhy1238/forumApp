import { createApp } from 'vue'
import App from './App.vue'
import router from './router/router.js'
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
//import locale from 'element-plus/lib/locale/lang/zh-cn'
app.use(router).use(ElementPlus).mount('#app')
