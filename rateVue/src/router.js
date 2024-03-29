import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
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
          path: '/achievement/GraduateManageAchievementInfo',
          name: '研究生成果详情',
          component: () => import('./views/admin/GraduateManageAchievementInfo'),
          meta: {
            title: '研究生成果详情',
          },
        },
        {
          path: '/achievement/DoctorManageAchievementInfo',
          name: '博士生成果详情',
          component: () => import('./views/admin/DoctorManageAchievementInfo'),
          meta: {
            title: '博士生成果详情',
          },
        },
        {
          path: '/pending/message',
          name: '待办消息',
          component: () => import('./views/teacher/PendingMessage.vue')
        },
        {
          path: '/Expert/EassignPE', //路由跳转路径
          name: '分配人员', //路由名称
          hidden: true,
          component: () => import('./views/expert/EassignPE.vue'),
          meta: { title: '分配人员' },
        },
        {
          path: '/Admin/AssignPE',
          name: '分配人员(主活动)',
          hidden: true,
          component: () => import('./views/admin/AssignPE.vue'),
          meta: { title: '分配人员(主活动)' },
        },
        {
          path: '/Admin/ExceptionLog',
          name: '异常信息查看',
          hidden: true,
          component: () => import('./views/admin/ExceptionLog.vue'),
          meta: { title: '异常信息查看' },
        },
        {
          path: '/Admin/ExceptionDetails',
          name: '异常信息详情查看',
          hidden: true,
          component: () => import('./views/admin/ExceptionDetails.vue'),
          meta: { title: '异常信息详情查看' },
        },
        {
          path: '/admin/PersonalCenter',
          name: '管理员个人中心',
          hidden: true,
          component: () => import('./views/admin/HrInfo.vue'),
          meta: {
            title: '管理员个人中心',
          },
        },
        {
          path:'/superAdmin/PersonalCenter',
          name: '超级管理员个人中心',
          hidden: true,
          component: () => import('./views/admin/SuperInfo.vue'),
          meta: {
            title: '超级管理员个人中心',
          },
        },
        {
          path: '/student/PersonalCenter',
          name: '学生个人中心',
          hidden: true,
          component: () => import('./views/student/HrInfo.vue'),
          meta: {
            title: '学生个人中心',
          },
        },
        {
          path: '/teacher/PersonalCenter',
          name: '老师个人中心',
          hidden: true,
          component: () => import('./views/HrInfo.vue'),
          meta: {
            title: '老师个人中心',
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
        {
          path: '/per/emp',
          name: '查看所有单位',
          component: () => import('./views/admin/PerEmp'),
          meta: { title: '查看所有单位' },
        },
        {
          path: '/per/ec',
          name: '查看各个单位管理员列表',
          component: () => import('./views/admin/PerEc'),
          meta: { title: '查看各个单位管理员列表' },
        },
        {
          path: '/Admin/addAct',
          name: '添加活动',
          component: () => import('./views/admin/AddAct'),
          meta: { title: '添加活动' },
        },
        {
          path: '/ActivitM/total',
          name: 'SalTotal',
          component: () => import('./views/admin/SalTotal'),
          meta: { title: 'SalTotal' },
        },{
          path: '/ActivitM/final',
          name: 'SalFinalScore',
          component: () => import('./views/admin/SalFinalScore'),
          meta: { title: 'SalFinalScore' },
        }, {//管理员下的
          path: '/ActivitM/search',
          name: 'ActManage',
          component: () => import('./views/admin/ActManage.vue'),
          meta: { title: 'ActManage' },
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
        { // 管理员子活动
          path: '/ActivitM/SubActManage',
          name: 'SubActManage',
          component: () => import('./views/admin/SubActManage.vue'),
          meta: { title: 'SubActManage' },
        },
        { // 秘书子活动
          path: '/secretary/SubActManage',
          name: 'SecSubActManage',
          component: () => import('./views/secretary/SubActManage.vue'),
          meta: { title: 'SecSubActManage' },
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
          name: '纵向科研项目',
          hidden: true,
          component: () => import('./views/student/Research-Project'),
          meta: {
            title: '纵向科研项目',
          },
        },
        {
          path: '/student/HorizontalResearchProject',
          name: '横向科研项目',
          hidden: true,
          component: () => import('./views/student/Horizontal-Research-Project'),
          meta: {
            title: '横向科研项目',
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
          path: '/student/AcademicCompetition',
          name: '学科竞赛',
          hidden: true,
          component: () => import('./views/student/Academic-Competition'),
          meta: {
            title: '学科竞赛',
          },
        },
        {
          path: '/student/Standard',
          name: '制定标准',
          hidden: true,
          component: () => import('./views/student/Standard'),
          meta: {
            title: '制定标准',
          },
        },
        {
          path: '/student/infos',
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
          path: '/student/addPublication',
          name: '添加期刊',
          hidden: true,
          component: () => import('./views/student/AddPublication'),
          meta: {
            title: '添加期刊',
          },
        },
        {
          path: '/student/CheckProgress',
          name: '查询审核进度',
          hidden: true,
          component: () => import('./views/student/CheckProgress'),
          meta: {
            title: '查询审核进度',
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
          path: '/student/Product',
          name: '产品应用',
          hidden: true,
          component: () => import('./views/student/Product'),
          meta: {
            title: '产品应用',
          },
        },
        {
          path:'/student/Decision',
          name:'决策咨询',
          hidden:true,
          component:()=>import('./views/student/Decision'),
          meta: {
            title: '决策咨询',
          },
        },
        {
          path: '/teacher/AcademicCompetition',
          name: '学科竞赛',
          hidden: true,
          component: () => import('./views/teacher/Academic-Competition'),
          meta: {
            title: '学科竞赛',
          },
        },
        {
          path: '/teacher/Decision',
          name: '决策咨询',
          hidden: true,
          component: () => import('./views/teacher/Decision'),
          meta: {
            title: '决策咨询',
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
          path: '/teacher/tResearchProject',
          name: '纵向科研项目',
          hidden: true,
          component: () => import('./views/teacher/Research-Project'),
          meta: {
            title: '纵向科研项目',
          },
        },
        {
          path: '/teacher/tHorizontalResearchProject',
          name: '横向科研项目',
          hidden: true,
          component: () => import('./views/teacher/Horizontal-Research-Project'),
          meta: {
            title: '横向科研项目',
          },
        },
        {
          path: '/teacher/Standard',
          name: '制定标准',
          hidden: true,
          component: () => import('./views/teacher/Standard'),
          meta: {
            title: '制定标准',
          },
        },
        {
          path: '/teacher/Product',
          name: '产品应用',
          hidden: true,
          component: () => import('./views/teacher/Product'),
          meta: {
            title: '产品应用',
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
          path: 'ActManage',
          name: 'ActManage',
          hidden: true,
          component: () => import('./components/ActManage.vue'),
          meta: { title: 'ActManage' },
        },
        {
          path: 'AddActStep',
          name: 'AddActStep',
          hidden: true,
          component: () => import('./components/AddActStep.vue'),
          meta: { title: 'AddActStep' },
        },
        {
          path: '/secretary/ActManage',
          name: '秘书管理',
          hidden: true,
          component: () => import('./views/secretary/ActManage.vue'),
          meta: {
            title: '秘书管理',
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
          path: '/admin/Check',
          name: '审核期刊',
          hidden: true,
          component: () => import('./views/admin/Check'),
          meta: {
            title: '审核期刊',
          },
        },
        {
          path: '/teacher/stulist',
          name: '研究生导师的研究生列表',
          hidden: true,
          component: () => import('./views/teacher/StuList'),
          meta: {
            title: '研究生导师的研究生列表',
          },
        },
        {
          path: '/teacher/DoctorList',
          name: '研究生导师的博士生列表',
          hidden: true,
          component: () => import('./views/teacher/DoctorStudentList'),
          meta: {
            title: '研究生导师的博士生列表',
          },
        },
        {
          path: '/teacher/tPaperComment',
          name: '教师毕业论文评审记录',
          hidden: true,
          component: () => import('./views/teacher/PaperComment'),
          meta: {
            title: '教师毕业论文评审记录',
          },
        },
        {
          path: '/teacher/gradeForm',
          name: '成绩评定表导出',
          hidden: true,
          component: () => import('./views/teacher/GradeForm.vue'),
          meta: {
            title: '成绩评定表导出',
          },
        },
        {
          path: '/teacher/stuPaperComment',
          name: '教师毕业论文评审记录详情界面',
          hidden: true,
          component: () => import('./views/teacher/stuPaperComment'),
          meta: {
            title: '教师毕业论文评审记录',
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
        {
          path: '/admin/TeacherM',
          name: '教师管理',
          hidden: true,
          component: () => import('./views/admin/SalTeacherM'),
          meta: {
            title: '教师管理',
          },
        },
        {
          path: '/admin/GraduateM',
          name: '研究生列表',
          hidden: true,
          component: () => import('./views/admin/SalGraduateM'),
          meta: {
            title: '研究生列表',
          },
        },
        {
          path: '/admin/DoctorM',
          name: '研究生列表',
          hidden: true,
          component: () => import('./views/admin/SalDoctorM'),
          meta: {
            title: '研究生列表',
          },
        },
        {
          path: '/admin/UnderGraduateM',
          name: '学生管理',
          hidden: true,
          component: () => import('./views/admin/SalUnderGraduateM'),
          meta: {
            title: '学生管理',
          },
        },
        {
          path: '/admin/StartThesis',
          name: '开启毕业论文',
          hidden: true,
          component: () => import('./views/admin/StartThesis'),
          meta: {
            title: '开启毕业论文',
          },
        },
        {
          path: '/admin/ManageThesis',
          name: '管理毕业论文',
          hidden: true,
          component: () => import('./views/admin/ManageThesis'),
          meta: {
            title: '管理毕业论文',
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
      path: '/Student/Register',
      name: 'StuRegister',
      hidden: true,
      component: () => import('./views/student/Register'),
      meta: { title: '学生注册' },
    },
    {
      path: '/ResetPassword',
      name: 'ResetPassword',
      hidden: true,
      component: () => import('./components/ResetPassword'),
      meta: { title: '找回密码' },
    },
    {
      path: '/Teacher/Register',
      name: 'TeaRegister',
      hidden: true,
      component: () => import('./views/teacher/Register'),
      meta: { title: '教师注册' },
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
    {
      path: '/EassignE',
      name: '分配专家',
      component: () => import('./views/expert/EassignE'),
      meta: { title: '分配专家' },
    },
    {
      path: '/AssignE',
      name: '分配专家(主活动)',
      component: () => import('./views/admin/AssignE'),
      meta: { title: '分配专家(主活动)' },
    },
  ],
})
