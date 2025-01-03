import { createRouter, createWebHashHistory } from 'vue-router'
import HomePage from "@/Pages/HomePage.vue";
import IndexPage from "@/Pages/IndexPage.vue";
import LoginPage from "@/Pages/LoginPage.vue";
import PostingPage from "@/Pages/PostingPage.vue";
import PostDetailPage from "@/Pages/PostDetailPage.vue";
import RegisterPage from "@/Pages/RegisterPage.vue";
import Cookies from "js-cookie";
const  routes=[
    {
        path:'/',
        redirect: '/index'
    },
    {
        path:'/homepage/:username',
        name:'homepage',
        component:HomePage,
        meta: {requiresAuth:true}
    },
    {
        path:'/index',
        name:'indexpage',
        component:IndexPage
    },
    {
        path:'/login',
        name:'loginpage',
        component:LoginPage
    },
    {
        path:'/register',
        name:'registerpage',
        component:RegisterPage
    },
    {
        path:'/postdetail/:postid',
        name:'postdetailpage',
        component:PostDetailPage,
        meta: {requiresAuth:true}
    },
    {
        path:'/posting',
        name:'postingpage',
        component:PostingPage,
        meta: {requiresAuth:true}
    },
]
const router = createRouter({
    history: createWebHashHistory(),
    routes
})
//添加全局导航守卫
router.beforeEach((to, from, next) => {
    const isLoggedIn = Cookies.get("isLogin") === 'true'
    if (to.matched.some(record => record.meta.requiresAuth)) {
        // 如果需要登录的页面
        if (!isLoggedIn) {
            // 未登录，强制跳转到登录页面
            next('/login')
        } else {
            // 已登录，允许访问
            next()
        }
    } else {
        // 不需要登录的页面，直接允许访问
        next()
    }
})
export default router