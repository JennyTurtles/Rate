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
        {//管理员下的
          path: '/bookview',
          name: 'SalBookView',
          component: () => import('./views/admin/SalBookView'),
          meta: { title: 'BookView' },
          children: [
            {
              path: 'search',
              name: 'Search',
              component: () => import('./components/search'),
              meta: { title: 'Search' },
            },
          ],
        },
        {//管理员下的
          path: '/SalLog',
          name: 'SalLog',
          component: () => import('./views/admin/SalLog'),
          meta: { title: 'SalLog' },

        }, {//管理员下的
          path: '/ActivitM/score',
          name: 'SalScore',
          component: () => import('./views/admin/SalScore'),
          meta: { title: 'SalScore' },

        }, {
          path: '/ActivitM/detail',
          name: 'SalDetail',
          component: () => import('./views/admin/SalDetail'),
          meta: { title: 'SalDetail' },
        },
        {//管理员下的
          path: '/ActivitM/SalPartipicantGroup',
          name: 'SalPartipicantGroup',
          component: () => import('./views/admin/SalPartipicantGroup'),
          meta: { title: 'SalPartipicantGroup' },

        },{//管理员下的
          path: '/ActivitM/search',
          name: 'SalSearch',
          component: () => import('./views/admin/SalSearch'),
          meta: { title: 'SalSearch' },
        },{//管理员下的
          path: '/ActivitM/situation',
          name: 'SalSituation',
          component: () => import('./views/admin/SalSituation'),
          meta: {title: 'SalSituation' },
        },{//管理员下的
          path: '/ActivitM/expertScore',
          name: 'expertScore',
          component: () => import('./views/admin/ExpertScore.vue'),
          meta: {title: 'expertScore'},
        },
        {
          path: '/ActivitM/subActivity',
          name: 'SalSubActivity',
          component: () => import('./views/admin/SalSubActivity'),
          meta: { title: 'SalSubActivity' },
        },
        {//管理员下的
          path: '/ActivitM/month',
          name: 'Salmonth',
          component: () => import('./views/admin/SalMonth'),
          meta: { title: 'Salmonth' },

        },
        {//管理员下的
          path: '/ActivitM/table',
          name: 'SalTable',
          component: () => import('./views/admin/SalTable'),
          meta: { title: 'SalTable' },

        },{//管理员下的
          path: '/ActivitM/sobcfg',
          name: 'SalSobCfg',
          component: () => import('./views/admin/SalSobCfg'),
          meta: { title: 'SalSobCfg' },

        },
        {//管理员下的
          path: '/admin/SalExamine',
          name: 'SalExamine',
          component: () => import('./views/admin/Examine'),
          meta: { title: 'SalExamine' },

        },{//管理员下的
          path: '/ActivitM/group',
          name: 'SalGroup',
          component: () => import('./views/admin/SalGroup'),
          meta: { title: 'SalGroup' },

        },
        {//管理员下的
          path: '/ActivitM/infos',
          name: 'SalInfos',
          component: () => import('./views/admin/SalInfos'),
          meta: { title: 'SalInfos' },

        },
        {//管理员下的
          path: '/participantsM',
          name: 'SalPar',
          component: () => import('./views/admin/SalPar'),
          meta: { title: 'SalPar' },

        },
        {
          path: '/student/Product',
          name: '制造或设计的产品1',
          hidden: true,
          component: () => import('./views/student/Product'),
          meta: {
            title: '制造或设计的产品1',
          },
        },
        {
          path: '/student/AcademicMonograph',
          name: '学术专著和教材1',
          hidden: true,
          component: () => import('./views/student/Academic-Monograph'),
          meta: {
            title: '学术专著和教材1',
          },
        },
        {
          path: '/student/ResearchProject',
          name: '科研项目1',
          hidden: true,
          component: () => import('./views/student/Research-Project'),
          meta: {
            title: '科研项目1',
          },
        },
        {
          path: '/student/ResearchAward',
          name: '科研获奖1',
          hidden: true,
          component: () => import('./views/student/Research-Award'),
          meta: {
            title: '科研获奖1',
          },
        },
        {
          path: '/student/Patent',
          name: '专利1',
          hidden: true,
          component: () => import('./views/student/Patent'),
          meta: {
            title: '专利1',
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
          path: '/student/PaperComment',
          name: '毕业论文评审',
          hidden: true,
          component: () => import('./views/student/PaperComment'),
          meta: {
            title: '毕业论文评审',
          },
        },
        {
          path:'/student/search',
          name:'学生活动列表',
          hidden:true,
          component:()=>import('./views/student/Stusearch'),
          meta: {
            title: '学生活动列表',
          },
        },
        {
          path: '/student/Achievements',
          name: '成果列表1',
          hidden: true,
          component: () => import('./views/student/Achievements'),
          meta: {
            title: '成果列表1',
          },
        },
        {
          path: '/teacher/tProduct',
          name: '制造或设计的产品2',
          hidden: true,
          component: () => import('./views/teacher/Product'),
          meta: {
            title: '制造或设计的产品2',
          },
        },
        {
          path: '/teacher/tAcademicMonograph',
          name: '学术专著和教材2',
          hidden: true,
          component: () => import('./views/teacher/Academic-Monograph'),
          meta: {
            title: '学术专著和教材2',
          },
        },
        {
          path: '/teacher/tperact/actList',
          name: '教师活动列表',
          hidden: true,
          component: () => import('./views/teacher/ActList'),
          meta: {
            title: '教师活动列表',
          },
        },
        {
          path: '/secretary/tperact/actList',
          name: '秘书活动列表',
          hidden: true,
          component: () => import('./views/secretary/ActList'),
          meta: {
            title: '秘书活动列表',
          },
        },
        {
          path: '/teacher/tResearchProject',
          name: '科研项目2',
          hidden: true,
          component: () => import('./views/teacher/Research-Project'),
          meta: {
            title: '科研项目2',
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
      //新打开一个页面
    {
      path: '/admin/InformationDetails',
      name: '详情信息',
      hidden: false,
      component: () => import('./views/admin/InformationDetails'),
      meta: {
        title: '详情信息',
      },
    },
    {
      path: '/teacher/tperact/InformationDetails',
      name: 'score详情信息',
      hidden: false,
      component: () => import('./views/teacher/InformationDetails'),
      meta: {
        title: 'score详情信息',
      },
    },

    {
      path:'/teacher/tperact/score',
      name:'score',
      hidden:false,
      component:()=>import('./views/teacher/Score'),
      meta:{
        title:'分数'
      }
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
      name: '活动列表2',
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
          name: '专家活动列表',
          component: () => import('./views/expert/EactList'),
          meta: { title: '专家活动列表2' },
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
    //   path: '/test',
    //   name: 'test',
    //   component: () => import('./views/test'),
    //   meta: { title: 'test' },
    // },
  ],
})