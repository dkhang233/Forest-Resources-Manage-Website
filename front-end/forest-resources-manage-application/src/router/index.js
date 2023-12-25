import { createRouter, createWebHashHistory  } from 'vue-router'
import { useUserStore } from '@/stores/user-store';

export const constantRoutes = [
  {
    path: '/main',
    component: () => import('../components/common/Main'),
    children: [
      {
        path: '',
        component: () => import('@/components/home/Home')
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
        component: () => import('@/components/wood/WoodType') 
      },
      {
        path : 'operation-form',
        component: () => import('@/components/wood/WoodOperationForm')
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
      },
      {
        path : 'map',
        component : () => import('@/components/map/MapView')
      },
      {
        path : 'change-pass',
        component : () => import('@/components/login/ChangePassWithCurrentPass')
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
        path: '/register',
        component: () => import('@/components/login/RegisterForm')
      },
      {
        path: 'forget-pass',
        component: () => import('@/components/login/ForgetPass')
      },
      {
        path: 'authenticate-code',
        component: () => import('@/components/login/AuthenticateCode')
      },
      {
        path: 'change-pass',
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
  const userStore = useUserStore()
  if (
    !$cookies.get('username') && to.path !== '/' && to.path !== '/forget-pass' && to.path !== '/authenticate-code' && to.path !== '/change-pass'
  ) {
    return { path: '/' }
  }
})

router.beforeEach(async (to, from) => {
  if (
    to.path === '/main/access' || to.path === '/main/account'
  ) {
    if ($cookies.get('role') !== 'admin') {
      return false
    }
  }
  return true
})

export default router