import { createRouter, createWebHashHistory } from 'vue-router'

export const constantRoutes = [
  {
    path: '/',
    component: () => import('../components/main/Main'),
    children: [
      {
        path: 'access',
        component: () => import('../components/admin/ManageAccess')
      },
      {
        path: 'account',
        component: () => import('../components/admin/ManageAccount')
      },{
        path: 'administrative',
        component: () => import('../components/administrative/Administrative')
      },
      {
        path: 'seedtype',
        component: () => import('../components/seed/SeedType')
      },
      {
        path: 'seedfacility',
        component: () => import('../components/seed/SeedFacility')
      },
      {
        path: 'woodtype',
        component: () => import('../components/wood/WoodType')
      },
      {
        path: 'woodfacility',
        component: () => import('../components/wood/WoodFacility')
      },
      {
        path: 'animaltype',
        component: () => import('../components/animal/AnimalType')
      },
      {
        path: 'animalfacility',
        component: () => import('../components/animal/AnimalFacility')
      }
    ]
  }
];


const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes
})

export default router