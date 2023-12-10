import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '@/stores/user-store';

export const constantRoutes = [
  {
    path: '/main',
    component: () => import('../components/main/Main'),
    children: [
      {
        path: '',
        component: () => import('@/components/common/Home')
      },
      {
        path: 'profile',
        component: () => import('@/components/user/UserInfor')
      },
      {
        path: 'access',
        component: () => import('@/components/admin/ManageAccess')
      },
      {
        path: 'account',
        component: () => import('@/components/admin/ManageAccount')
      }, {
        path: 'administration',
        component: () => import('@/components/administration/Administration')
      },
      {
        path: 'seedtype',
        component: () => import('@/components/seed/SeedType')
      },
      {
        path: 'seedfacility',
        component: () => import('@/components/seed/SeedFacility')
      },
      {
        path: 'woodtype',
        component: () => import('../components/wood/WoodType')
      },
      {
        path: 'woodfacility',
        component: () => import('@/components/wood/WoodFacility')
      },
      {
        path: 'animaltype',
        component: () => import('@/components/animal/AnimalType')
      },
      {
        path: 'animalfacility',
        component: () => import('@/components/animal/AnimalFacility')
      }
    ]
  },
  {
    path: '/',
    component: () => import('@/components/login/Login'),
    children: [
      {
        path: '',
        component: () => import('@/components/login/LoginForm')
      },
      {
        path: 'forgetpass',
        component: () => import('@/components/login/ForgetPass')
      },
      {
        path: 'authenticatecode',
        component: () => import('@/components/login/AuthenticateCode')
      },
      {
        path: 'changepass',
        component: () => import('@/components/login/ChangePass')
      }
    ]
  }
];


const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes
})

router.beforeEach(async (to, from) => {
  if (
    !$cookies.get('username') && to.path !== '/'
  ) {
    return { path: '/' }
  }
})

export default router