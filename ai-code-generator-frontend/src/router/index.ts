import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import userLoginPage from '@/pages/user/UserLoginPage.vue'
import userManagePage from '@/pages/admin/UserManagePage.vue'
import userRegisterPage from '@/pages/user/UserRegisterPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
    },
    {
      path: '/user/login',
      name: '用户登录',
      component: userLoginPage,
    },
    {
      path: '/user/register',
      name: '用户注册',
      component: userRegisterPage,
    },
    {
      path: '/admin/userManage',
      name: '用户管理',
      component: userManagePage,
    },

  ],

})

export default router
