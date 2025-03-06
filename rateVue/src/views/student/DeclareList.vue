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
            @change="resetDialogs"
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
      <el-col :span="12" :lg="6" style="max-width: 250px;">
        <el-button type="primary" @click="submitDeclaration" style="width: 100%;font-size: 16px;">
          进入申报
        </el-button>
      </el-col>
    </el-row>
    <!-- 添加论文对话框 -->
    <Paper v-if="showAddPaperDialog" :dialogVisible.sync="showAddPaperDialog" @add="addPaper" />
    <!-- 添加专利对话框 -->
    <Patent v-if="showAddPatentDialog" :dialogVisible.sync="showAddPatentDialog" @add="addPatent"/>
    <!-- 添加获奖对话框 -->
    <ResearchAward v-if="showAddResearchAwardDialog" :dialogVisible.sync="showAddResearchAwardDialog" @add="addResearchAward"/>
    <!-- 添加著作对话框 -->
    <AcademicMonograph v-if="showAddAcademicMonographDialog" :dialogVisible.sync="showAddAcademicMonographDialog" @add="addAcademicMonograph"/>
    <!-- 添加标准对话框 -->
    <ResearchProject v-if="showAddResearchProjectDialog" :dialogVisible.sync="showAddResearchProjectDialog" @add="addResearchProject"/>
    <!-- 添加标准对话框 -->
    <Standard v-if="showAddStandardDialog" :dialogVisible.sync="showAddStandardDialog" @close="handleDialogClose" @add="addStandard" />
    <!-- 添加横向科研项目对话框 -->
    <AcademicCompetition v-if="showAddAcademicCompetitionDialog" :dialogVisible.sync="showAddAcademicCompetitionDialog" @add="addAcademicCompetition" />
    <!-- 添加决策咨询对话框 -->
    <Decision v-if="showAddDecisionDialog" :dialogVisible.sync="showAddDecisionDialog" @add="addDecision"/>
    <!-- 添加产品对话框 -->
    <Product v-if="showAddProductDialog" :dialogVisible.sync="showAddProductDialog" @add="addProduct" />
    <!-- 添加产品对话框 -->
    <HorizontalResearchProject v-if="showAddHorizontalResearchProjectDialog" :dialogVisible.sync="showAddHorizontalResearchProjectDialog" @add="addHorizontalProject"/>
  </div>
</template>

<script>
import Paper from '@/views/student/Paper.vue';
import Patent from '@/views/student/Patent.vue';
import ResearchAward from '@/views/student/Research-Award.vue';
import AcademicMonograph from "@/views/student/Academic-Monograph";
import Decision from "./Decision";
import Product from "./Product";
import HorizontalResearchProject from "./Horizontal-Research-Project";
import Standard from "./Standard";
import ResearchProject from "./Research-Project";
import AcademicCompetition from "./Academic-Competition";
export default {
  name: "DeclareList",
  components: {
    AcademicCompetition,
    Paper,
    Patent,
    ResearchAward,
    AcademicMonograph,
    Decision,
    Product,
    HorizontalResearchProject,
    Standard,
    ResearchProject,
  },
  created() {
    // 初始化逻辑可以在这里定义
  },
  mounted() {
    // 组件挂载后执行的逻辑可以在这里定义
  },

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
      selectedOption: '学术论文', // 默认选中第一个选项
      showAddPaperDialog: false, // 控制添加论文对话框显示
      showAddPatentDialog: false, // 控制添加专利对话框显示
      showAddResearchAwardDialog: false,
      showAddAcademicMonographDialog: false,
      showAddAcademicCompetitionDialog: false,
      showAddDecisionDialog: false,
      showAddProductDialog: false,
      showAddStandardDialog: false,
      showAddResearchProjectDialog: false,
      showAddHorizontalResearchProjectDialog: false
    };
  },
  methods: {
    submitDeclaration() {
      this.close();

      switch (this.selectedOption) {
        case '学术论文':
          this.showAddPaperDialog = true;
          break;
        case '授权专利':
          this.showAddPatentDialog = true;
          break;
        case '科研获奖':
          this.showAddResearchAwardDialog = true;
          break;
        case '学术专著和教材':
          this.showAddAcademicMonographDialog = true;
          break;
        case '纵向科研项目':
          this.showAddResearchProjectDialog = true;
          break;
        case '横向科研项目':
          this.showAddHorizontalResearchProjectDialog = true;
          break;
        case '学科竞赛':
          this.showAddAcademicCompetitionDialog = true;
          break;
        case '决策咨询':
          this.showAddDecisionDialog = true;
          break;
        case '产品应用':
          this.showAddProductDialog = true;
          break;
        case '制定标准':
          this.showAddStandardDialog = true;
          break;
        default:
          this.$message.error("未选择有效选项");
      }

    },
    close() {
      this.showAddPaperDialog = false;
      this.showAddPatentDialog = false;
      this.showAddResearchAwardDialog = false;
      this.showAddAcademicMonographDialog = false;
      this.showAddAcademicCompetitionDialog = false;
      this.showAddDecisionDialog = false;
      this.showAddProductDialog = false;
      this.showAddStandardDialog = false;
      this.showAddResearchProjectDialog = false;
      this.showAddHorizontalResearchProjectDialog = false;
    },
    resetDialogs() {
      this.close(); // 重置所有对话框状态
    },
    handleDialogClose() {
      // 关闭所有对话框
      this.close();
      // 重置 selectedOption 为默认值
      this.selectedOption = '学术论文';
    },
    addPaper() {
      // 添加论文的逻辑
      // 假设添加论文成功
      this.$router.push('/student/Project');
      this.close();
    },
    addPatent() {
      // 添加专利的逻辑
      // 假设添加专利成功
      this.$router.push('/student/Project');
      this.close();
    },
    addResearchAward() {
      // 添加科研获奖的逻辑
      // 假设添加科研获奖成功
      this.$router.push('/student/Project');
      this.close();
    },
    addAcademicMonograph() {
      // 添加学术专著和教材的逻辑
      // 假设添加学术专著和教材成功
      this.$router.push('/student/Project');
      this.close();
    },
    addResearchProject() {
      // 添加纵向科研项目的逻辑
      // 假设添加纵向科研项目成功
      this.$router.push('/student/Project');
      this.close();
    },
    addHorizontalProject() {
      // 添加横向科研项目的逻辑
      // 假设添加横向科研项目成功
      this.$router.push('/student/Project');
      this.close();
    },
    addAcademicCompetition() {
      // 添加学科竞赛的逻辑
      // 假设添加学科竞赛成功
      this.$router.push('/student/Project');
      this.close();
    },
    addDecision() {
      // 添加决策咨询的逻辑
      // 假设添加决策咨询成功
      this.$router.push('/student/Project');
      this.close();
    },
    addProduct() {
      // 添加产品应用的逻辑
      // 假设添加产品应用成功
      this.$router.push('/student/Project');
      this.close();
    },
    addStandard() {
      // 添加制定标准的逻辑
      // 假设添加制定标准成功
      this.$router.push('/student/Project');
      this.close();
    }
  },

};

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