<template>
  <el-card>
    <el-skeleton :loading="JSON.stringify($store.state.pendingMessageTypeObject) == '{}'" animated :rows="6">
      <template #template></template>
      <template>
        <div>
          <div style="padding-left: 40px">共有{{ $store.state.pendingMessageTotal }}条待办消息！</div>
          <ul>
            <li v-for="(key, value) in list" @click="goLink(key, value)" style="margin-top: 8px">
              <a href="#" style="text-decoration: none; color: #303133">
                【 {{ typeMap[value].name }} 】有
                <span :style="key > 0 ? {'color' : 'red'} : {'color' : '#303133'}">
                  {{ key }}
                </span>
                <span>
                  条待办消息
                </span>
              </a>
            </li>
          </ul>
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
        'patent': {
          name: '授权专利',
          path: '/teacher/tPatent'
        },
        'paper': {
          name: '学术论文',
          path: '/teacher/tPaper'
        },
        'monograph': {
          name: '专著教材',
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
        'product': {
          name: '产品应用',
          path: '/teacher/Product'
        },
        'competition': {
          name: '学科竞赛',
          path: '/teacher/AcademicCompetition'
        },
        'award': {
          name: '科研获奖',
          path: '/teacher/tResearchAward'
        },
        'standard': {
          name: '制定标准',
          path: '/teacher/Standard'
        },
        'decision': {
          name: '决策咨询',
          path: '/teacher/Decision'
        }
      }
    }
  },
  computed: {
    list() {
      return this.$store.state.pendingMessageTypeObject;
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
    }
  }
}
</script>

<style scoped>

</style>