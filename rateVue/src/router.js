import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  // mode:'hash',
  // mode:'history',
  // base: '/',
  routes: [
    {
      path: '/Admin/Login', //管理员路由跳转路径
      name: 'Login', //路由名称
      hidden: true,
      component: () => import('./views/admin/Login.vue'),
      meta: { title: 'Login' },
    },
    {
      path: '/home',
      name: 'Home',
      hidden: true,
      component: () => import('./views/Home.vue'),
      meta: {
        roles: ['admin', 'user'],
        title: 'Home',
      },
      children: [
        {
          path: '/hrinfo',
          name: '个人中心1',
          hidden: true,
          component: () => import('./views/HrInfo.vue'),
          meta: {
            title: '个人中心',
          },
        },
        {
          path: '/UserInfo',
          name: '个人中心2',
          hidden: true,
          component: () => import('./views/expert/userInfo.vue'),
          meta: {
            title: '个人中心',
          },
        },
        {
          path: '/student/Product',
          name: '制造或设计的产品',
          hidden: true,
          component: () => import('./views/student/Product'),
          meta: {
            title: '制造或设计的产品',
          },
        },
        {
          path: '/student/AcademicMonograph',
          name: '学术专著和教材',
          hidden: true,
          component: () => import('./views/student/Academic-Monograph'),
          meta: {
            title: '学术专著和教材',
          },
        },
        {
          path: '/student/ResearchProject',
          name: '科研项目',
          hidden: true,
          component: () => import('./views/student/Research-Project'),
          meta: {
            title: '科研项目',
          },
        },
        {
          path: '/student/ResearchAward',
          name: '科研获奖',
          hidden: true,
          component: () => import('./views/student/Research-Award'),
          meta: {
            title: '科研获奖',
          },
        },
        {
          path: '/student/Patent',
          name: '专利',
          hidden: true,
          component: () => import('./views/student/Patent'),
          meta: {
            title: '专利',
          },
        },
        {
          path: '/student/Paper',
          name: '论文1',
          hidden: true,
          component: () => import('./views/student/Paper'),
          meta: {
            title: '论文',
          },
        },
        {
          path: '/student/Infos',
          name: '信息填写',
          hidden: true,
          component: () => import('./views/student/Infos'),
          meta: {
            title: '信息填写',
          },
        },
        {
          path: '/student/PaperCommit',
          name: '毕业论文评审',
          hidden: true,
          component: () => import('./views/student/PaperCommit'),
          meta: {
            title: '毕业论文评审',
          },
        },
        {
          path: '/student/Achievements',
          name: '成果列表',
          hidden: true,
          component: () => import('./views/student/Achievements'),
          meta: {
            title: '成果列表',
          },
        },
        {
          path: '/teacher/tProduct',
          name: '制造或设计的产品2',
          hidden: true,
          component: () => import('./views/teacher/Product'),
          meta: {
            title: '制造或设计的产品',
          },
        },
        {
          path: '/teacher/tAcademicMonograph',
          name: '学术专著和教材2',
          hidden: true,
          component: () => import('./views/teacher/Academic-Monograph'),
          meta: {
            title: '学术专著和教材',
          },
        },
        {
          path: '/teacher/tResearchProject',
          name: '科研项目2',
          hidden: true,
          component: () => import('./views/teacher/Research-Project'),
          meta: {
            title: '科研项目',
          },
        },
        {
          path: '/teacher/tResearchAward',
          name: '科研获奖2',
          hidden: true,
          component: () => import('./views/teacher/Research-Award'),
          meta: {
            title: '科研获奖',
          },
        },
        {
          path: '/teacher/tPatent',
          name: '专利2',
          hidden: true,
          component: () => import('./views/teacher/Patent'),
          meta: {
            title: '专利',
          },
        },
        {
          path: '/teacher/tPaper',
          name: '论文2',
          hidden: true,
          component: () => import('./views/teacher/Paper'),
          meta: {
            title: '论文',
          },
        },
        {
          path: '/admin/SalPaper',
          name: '论文3',
          hidden: true,
          component: () => import('./views/admin/Paper'),
          meta: {
            title: '论文',
          },
        },
        {
          path: '/teacher/tAchievements',
          name: '学生所有成果',
          hidden: true,
          component: () => import('./views/teacher/Achievements'),
          meta: {
            title: '学生所有成果',
          },
        },
        {
          path: '/teacher/tExamine',
          name: '待审核列表2',
          hidden: true,
          component: () => import('./views/teacher/Examine'),
          meta: {
            title: '待审核列表',
          },
        },
        {
          path: '/admin/SalAchievements',
          name: '所有成果',
          hidden: true,
          component: () => import('./views/admin/Achievements'),
          meta: {
            title: '学生所有成果',
          },
        },
        {
          path: '/admin/SalExamine',
          name: '待审核列表',
          hidden: true,
          component: () => import('./views/admin/Examine'),
          meta: {
            title: '待审核列表',
          },
        },
      ],
    },
    {
      path: '*',
      redirect: '/',
    },
    {
      path: '/p/ec',
      name: 'PerEc',
      component: () => import('./views/admin/PerEc'),
      meta: { title: '管理员' },
    },
    {
      path: '/UserInfo',
      name: '个人中心3',
      hidden: true,
      component: () => import('./views/expert/userInfo.vue'),
      meta: { title: '个人中心' },
    },
    {
      path: '/Student',
      redirect: '/',
    },
    {
      path: '/Student/Login',
      name: '学生登录',
      hidden: true,
      component: () => import('./views/student/Login'),
      meta: { title: '学生登录' },
      alias: '/',
    },
    {
      //教师登录页
      path: '/Teacher/Login', //路由跳转路径
      name: '教师登录', //路由名称
      hidden: true,
      component: () => import('./views/teacher/Login'),
      meta: { title: '教师登录' },
    },
    {
      //专家登录页
      path: '/Expert/Login', //路由跳转路径
      name: '专家登录', //路由名称
      hidden: true,
      component: () => import('./views/expert/ELogin'),
      meta: { title: '专家登录' },
    },
    {
      path: '/Expert/peract',
      name: '专家活动列表',
      component: () => import('./views/expert/Eperact'),
      meta: { title: 'peract' },
      children: [
        {
          path: 'warn',
          name: '警告',
          component: () => import('./views/expert/Ewarn'),
          meta: { title: '警告' },
        },
        {
          path: 'actList',
          name: '活动列表',
          component: () => import('./views/expert/EactList'),
          meta: { title: '活动列表' },
        },
        {
          path: 'score',
          name: '评分',
          component: () => import('./views/expert/Escore'),
          meta: { title: '评分列表' },
        },
      ],
    },
    // {
    //   path: '/bookview',
    //   name: 'SalBookView',
    //   component: () => import('./views/admin/SalBookView'),
    //   meta: { title: 'BookView' },
    //   children: [
    //     {
    //       path: 'search',
    //       name: 'Search',
    //       component: () => import('./components/search'),
    //       meta: { title: 'Search' },
    //     },
    //   ],
    // },
    // {
    //   path: '/test',
    //   name: 'test',
    //   component: () => import('./views/test'),
    //   meta: { title: 'test' },
    // },
  ],
})