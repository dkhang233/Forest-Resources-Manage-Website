import { createApp } from 'vue'
import App from './App.vue'
import { VueCookies } from 'vue-cookies'

import router from './router'
import { createPinia } from 'pinia'

//import element-plus
import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// import the fontawesome core 
import { library } from '@fortawesome/fontawesome-svg-core'
// import font awesome icon component 
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
// import specific icons 
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'


const app = createApp(App)

app.use(VueCookies)

//router
app.use(router)

//pinia
const pinia = createPinia()
app.use(pinia)

//element-plus
app.use(ElementPlus)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// add icons to the library
library.add(fas, fab , far)

//add component
app.component('font-awesome-icon', FontAwesomeIcon)

//mount 
app.mount('#app')
