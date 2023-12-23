import { createApp } from 'vue'
import App from './App.vue'
import { VueCookies } from 'vue-cookies'

import { createPinia } from 'pinia'
import router from './router'
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

//import vue-leaflet css and component 
import { LMap, LTileLayer, LMarker } from "@vue-leaflet/vue-leaflet";
import "leaflet/dist/leaflet.css";

// import echarts

//import tailwind css
import '@/assets/tailwind.css'

//Vue-datepicker
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

const app = createApp(App)

app.use(VueCookies)


//pinia
const pinia = createPinia()
app.use(pinia)

//router
app.use(router)

//element-plus
app.use(ElementPlus)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// add icons to the library
library.add(fas, fab , far)

//add component
app.component("l-map", LMap);
app.component("l-tile-layer", LTileLayer);
app.component("l-marker", LMarker);
app.component('font-awesome-icon', FontAwesomeIcon)
app.component('VueDatePicker', VueDatePicker);
//mount 
app.mount('#app')
