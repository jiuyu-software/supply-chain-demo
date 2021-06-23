import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

/* Layout */
import Layout from "@/layout";

export const constantRoutes = [
  {
    path: "/",
    component: () => import("@/views/index/index"),
    hidden: true,
  },
  {
    path: "/login",
    component: () => import("@/views/login/index"),
    hidden: true,
  },
  {
    path: "/regist",
    component: () => import("@/views/regist/index"),
    hidden: true,
  },
  {
    path: "/setting",
    component: () => import("@/views/regist/index"),
  },
  {
    path: "/404",
    component: () => import("@/views/404"),
    hidden: true,
  },

  {
    path: "/list",
    component: Layout,
    redirect: "/list/table",
    children: [
      {
        path: "table",
        name: "Dashboard",
        component: () => import("@/views/dashboard/index"),
        meta: { title: "自建链列表", icon: "dashboard" },
      },
      {
        path: "create",
        name: "Create",
        component: () => import("@/views/create/index"),
        meta: { title: "新建", icon: "Create" },
        hidden: true,
      },
    ],
  },
  {
    path: "/join",
    component: Layout,
    redirect: "/join/list",
    children: [
      {
        path: "list",
        name: "jonList",
        component: () => import("@/views/join/index"),
        meta: { title: "参与链列表", icon: "example" },
      },
    ],
  },
  {
    path: "/sign",
    component: Layout,
    children: [
      {
        path: "index",
        name: "sign",
        component: () => import("@/views/sign/index"),
        meta: { title: "极速签名", icon: "form" },
      },
    ],
  },
  {
    path: "/pay",
    component: Layout,
    children: [
      {
        path: "index",
        name: "pay",
        component: () => import("@/views/pay/index"),
        meta: { title: "模拟支付", icon: "nested" },
      },
    ],
  },
  {
    path: "/user",
    component: Layout,
    children: [
      {
        path: "index",
        name: "pay",
        component: () => import("@/views/user/index"),
        meta: { title: "个人中心", icon: "user" },
      },
    ],
  },

  // 404 page must be placed at the end !!!
  { path: "*", redirect: "/404", hidden: true },
];

const createRouter = () =>
  new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes,
  });

const router = createRouter();

export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
