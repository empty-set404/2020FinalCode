import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/Login'
import Register from '@/views/Register'

Vue.use(VueRouter)

 // 路由配置
  const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
    {
      path: '/register',
      name: 'Register',
      component: Register
    }

]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
