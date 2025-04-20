<template>
  <el-card>
    <el-skeleton :loading="JSON.stringify($store.state.pendingMessageTypeObject) == '{}'" animated :rows="6">
      <template #template></template>
      <template>
        <div>
          <div
              style="padding-left: 40px; cursor: pointer; text-decoration: underline;"
              @click="goTeacherMain()"
          >
            共有{{ $store.state.pendingMessageTotal }}条待办消息！
          </div>
        </div>
      </template>
    </el-skeleton>
  </el-card>
</template>


<script>
export default {
  name: "PendingMessage",
  data() {
    return {
      typeMap: {
        'MAIN': {
          name: '待审核',
          path: '/teacher/tProject1'
        },
        'adminMain': {
          name: '待审核',
          path: '/admin/Examine'
        },
        'paper': {
          name: '学术论文',
          path: '/teacher/tPaper'
        },
        'patent': {
          name: '授权专利',
          path: '/teacher/tPatent'
        },
        'award': {
          name: '科研获奖',
          path: '/teacher/tResearchAward'
        },
        'monograph': {
          name: '学术专著和教材',
          path: '/teacher/tAcademicMonograph'
        },
        'project': {
          name: '纵向科研项目',
          path: '/teacher/tResearchProject'
        },
        'horizontalProject': {
          name: '横向科研项目',
          path: '/teacher/tHorizontalResearchProject'
        },
        'competition': {
          name: '学科竞赛',
          path: '/teacher/AcademicCompetition'
        },
        'decision': {
          name: '决策咨询',
          path: '/teacher/Decision'
        },
        'product': {
          name: '产品应用',
          path: '/teacher/Product'
        },
        'standard': {
          name: '制定标准',
          path: '/teacher/Standard'
        }
      }
    }
  },
  computed: {
    list() {
      for(let item in this.$store.state.pendingMessageTypeObject) {
        if(this.$store.state.pendingMessageTypeObject[item] == 0) {
          delete this.$store.state.pendingMessageTypeObject[item]
        }
      }
      return this.$store.state.pendingMessageTypeObject;
    },
    // 添加计算属性获取当前角色
    currentRole() {
       // const user = JSON.parse(localStorage.getItem("user"));
      const user = JSON.parse(localStorage.getItem('user'));
      return user;
    }
  },
  mounted() {
    // if(JSON.stringify(this.$store.state.pendingMessageTypeObject) == '{}') {
    //   this.$store.dispatch('changePendingMessageange');
    // }
  },
  methods: {
    goLink(key, value) {
      this.$router.push({
        path: this.typeMap[value].path})
    },
    goTeacherMain() {
   const user =  this.currentRole;
      const roleName =  user.roleName ;
      if (roleName.indexOf('admin') < 0 ){
        this.$router.push({
          path:this.typeMap["MAIN"].path})
      }else {
        this.$router.push({
          path:this.typeMap["adminMain"].path})
      }

    }
  }
}
</script>

<style scoped>

</style>