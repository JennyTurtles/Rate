<template>
  <div class="button-container">
    <!-- 页面标题和提示 -->
    <el-row type="flex" justify="center" style="margin-bottom: 20px;">
      <el-col :span="24">
        <h2 style="text-align: center; margin: 0;">科研成果与项目申报</h2>
        <p style="text-align: center; color: #666; margin-top: 5px;">
          点击下方按钮可跳转到相应页面查看或提交相关信息。
        </p>
      </el-col>
    </el-row>

    <!-- 选项组 -->
    <el-row type="flex" justify="center">
      <el-col :span="20">
        <el-radio-group
            v-model="selectedOption"
            class="custom-radio-group"
        >
          <el-radio
              v-for="(path, key) in urlMap"
              :key="key"
              :label="key"
              class="custom-radio"
          >
            {{ key }}
          </el-radio>
        </el-radio-group>
      </el-col>
    </el-row>

    <!-- 操作按钮 -->
    <el-row type="flex" justify="center" style="margin-top: 20px;">
      <el-col :span="12" :lg="6" style="max-width: 200px;">
        <el-button type="primary" @click="submitDeclaration" style="width: 100%;">
          进入申报
        </el-button>
      </el-col>
      <el-col :span="12" :lg="6" style="max-width: 200px;">
        <el-button @click="close" style="width: 100%;">
          关闭
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "DeclareList",
  data() {
    return {
      urlMap: {
        '学术论文': '/student/Paper',
        '授权专利': '/student/Patent',
        '科研获奖': '/student/ResearchAward',
        '学术专著和教材': '/student/AcademicMonograph',
        '纵向科研项目': '/student/ResearchProject',
        '横向科研项目': '/student/HorizontalResearchProject',
        '学科竞赛': '/student/AcademicCompetition',
        '决策咨询': '/student/Decision',
        '产品应用': '/student/Product',
        '制定标准': '/student/Standard'
      },
      selectedOption: '学术论文' // 默认选中第一个选项
    };
  },
  methods: {
    navigateTo(name, url) {
      this.$confirm('是否跳转到' + name + '页面', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$router.push(url);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      });
    },
    submitDeclaration() {
      if (this.urlMap[this.selectedOption]) {
        this.navigateTo(this.selectedOption, this.urlMap[this.selectedOption]);
      }
    },
    close() {
      // 关闭操作逻辑可以在这里定义
    }
  }
}
</script>

<style scoped>
/* 新增自定义单选组样式 */
.custom-radio-group {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  width: 100%;
}

.custom-radio {
  display: flex !important;
  align-items: center;
  padding: 12px 15px;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
  transition: all 0.3s;
}

/* 调整单选按钮位置 */
.custom-radio::v-deep .el-radio__input {
  margin-right: 8px !important;
}

.custom-radio::v-deep .el-radio__label {
  flex: 1;
  text-align: left !important;
  padding-left: 0 !important;
}

/* 鼠标悬停效果 */
.custom-radio:hover {
  border-color: #409EFF;
  background-color: #f5f7fa;
}

/* 选中状态 */
.custom-radio.is-checked {
  border-color: #409EFF;
  background-color: #ecf5ff;
}

@media (max-width: 768px) {
  .custom-radio-group {
    grid-template-columns: 1fr;
  }
}
</style>