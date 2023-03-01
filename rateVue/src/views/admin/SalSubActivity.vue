<template>
  <div>
    <span>子活动</span>
    <div style="margin-top: 10px">
      <el-input
          placeholder="请输入活动名称进行搜索，可以直接回车搜索..."
          prefix-icon="el-icon-search"
          clearable
          @clear="searchEmps"
          style="width: 350px; margin-right: 10px"
          v-model="keyword"
          @keydown.enter.native="searchEmps"
          :disabled="showAdvanceSearchView"
      ></el-input>
      <el-button
          icon="el-icon-search"
          type="primary"
          @click="searchEmps"
          :disabled="showAdvanceSearchView"
      >
        搜索
      </el-button>
      <el-button
          icon="el-icon-refresh"
          type="primary"
          @click="initEmps"
          :disabled="showAdvanceSearchView"
      >
        重置
      </el-button>
      <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
        添加活动
      </el-button>
      <el-button style="float: right" icon="el-icon-back" type="primary" @click="back">
        返回
      </el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          stripe
          border
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
      >
        <el-table-column type="selection" width="35px"></el-table-column>

        <el-table-column
            prop="id"
            fixed
            align="center"
            label="编号"
            width="50"
        >
        </el-table-column>
        <!-- width="70" -->
        <el-table-column
            prop="name"
            fixed
            align="left"
            label="活动名称"
            width="200"
        >
        </el-table-column>
        <!-- width="200" -->
        <!-- width="200" -->
        <el-table-column
            prop="startDate"
            label="开始时间"
            align="center"
            width="130"
        >
        </el-table-column>
        <!-- width="100" -->
        <el-table-column
            prop="scoreItemCount"
            label="评分项数"
            align="center"
            width="80"
        >
        </el-table-column>
        <!-- width="70" -->
        <el-table-column
            prop="score"
            label="总分"
            align="center"
            width="75"
        >
        </el-table-column>
        <!-- width="60" -->
        <!-- width="80" -->
        <!-- width="550" -->
        <el-table-column align="center" width="650" label="操 作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView_show(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情
            </el-button
            >
            <el-button
                @click="showEditEmpView(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
            >编辑
            </el-button
            >
            <el-button
                @click="showScoreItem(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >评分项设置
            </el-button
            >
            <el-button
                @click="showInfoItem(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >信息项设置
            </el-button
            >
            <el-button
                @click="showGroupmanagement(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-s-operation"
                type="primary"
                plain
            >分组管理
            </el-button
            >
            <el-button
                @click="showGroupOfPartipicant(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-s-operation"
                type="primary"
                plain
            >选手分组
            </el-button
            >
            <el-button
                @click="showInsertmanagement(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >选手管理
            </el-button
            >

            <el-button
                @click="exportEx(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >{{text}}导出专家打分
            </el-button
            >
            <el-button
                @click="exportAc(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >导出选手分数
            </el-button
            >

            <el-button
                @click="endEmp(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-circle-close"
                plain
                :disabled="scope.row.status=='close'"
            >{{ scope.row.status == 'open' ? '结束活动' : '已结束' }}
            </el-button
            >
            <el-button
                @click="deleteEmp(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >删除
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <div style="display: flex; justify-content: flex-end; margin: 10px 0">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>
<script>
import {Message} from "element-ui";
export default {
  name: "SalSubActivity",
  data()  {
    return {
      emp: {
        id: null,
        institutionID: null,
        name: null,
        startDate: "2022/02/02",
        scoreItemCount: "0",
        score: "100",
        groupCount: "0",
        expertCount: "0",
        participantCount: "0",
        comment: "javaboy",
      },
      emps: [{
        id: 1,
        institutionID: null,
        name: "开题答辩",
        startDate: "2022/02/02",
        scoreItemCount: "0",
        score: "100",
        groupCount: "0",
        expertCount: "0",
        participantCount: "0",
        comment: "javaboy",
      },{
        id: 2,
        institutionID: null,
        name: "中期检查",
        startDate: "2022/03/02",
        scoreItemCount: "0",
        score: "100",
        groupCount: "0",
        expertCount: "0",
        participantCount: "0",
        comment: "javaboy",
      }],
    }
  },
  methods: {
    back(){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/search",
      });
    },
  },
};
</script>


<style>
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>