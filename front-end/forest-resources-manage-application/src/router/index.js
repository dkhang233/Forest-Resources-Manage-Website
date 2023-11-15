import { createRouter, createWebHashHistory } from 'vue-router'

export const constantRoutes = [
    {
      path: '/', 
      component: () => import('../components/admin/index')
      // children: [
      //   {
      //     path: '', 
      //     component: () => import('@/components/common/hello')
      //   }
      // ]
    }
];


const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes
})

export default router