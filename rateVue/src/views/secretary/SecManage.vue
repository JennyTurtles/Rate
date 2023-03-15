<!--<template>-->
<!--  <div style="height:600px">-->
<!--    <div style="display: flex; justify-content: left">-->
<!--      <div style="width: 100%;text-align: center">{{ actName }} {{groupName}} 分组管理</div>-->
<!--      <div style="margin-left: auto">-->
<!--        <el-button icon="el-icon-back" type="primary" @click="back">-->
<!--          返回-->
<!--        </el-button>-->
<!--      </div>-->
<!--    </div>-->
<!--    组内学生-->
<!--    <div style="height: 55%;width: 30%">-->
<!--      <el-table-->
<!--          height="100%"-->
<!--          stripe-->
<!--          ref="multipleTable"-->
<!--          :data="participates"-->
<!--          tooltip-effect="dark"-->
<!--          @selection-change="handleSelectionChange">-->
<!--        <el-table-column-->
<!--            type="selection"-->
<!--            width="55">-->
<!--        </el-table-column>-->
<!--        <el-table-column-->
<!--            prop="name"-->
<!--            label="姓名"-->
<!--            width="120">-->
<!--        </el-table-column>-->
<!--        <el-table-column-->
<!--            prop="code"-->
<!--            label="学号"-->
<!--            show-overflow-tooltip>-->
<!--        </el-table-column>-->
<!--      </el-table>-->
<!--    </div>-->
<!--    组内老师-->
<!--    <div style="height: 55%;width: 30%">-->
<!--      <el-table-->
<!--          stripe-->
<!--          ref="multipleTable"-->
<!--          :data="experts"-->
<!--          tooltip-effect="dark"-->
<!--          height="100%"-->
<!--          style=""-->
<!--          @selection-change="handleSelectionChange">-->
<!--        <el-table-column-->
<!--            type="selection"-->
<!--            width="55">-->
<!--        </el-table-column>-->
<!--        <el-table-column-->
<!--            prop="name"-->
<!--            label="姓名"-->
<!--            width="120">-->
<!--        </el-table-column>-->
<!--        <el-table-column-->
<!--            prop="jobNumber"-->
<!--            label="工号"-->
<!--            show-overflow-tooltip>-->
<!--        </el-table-column>-->
<!--      </el-table>-->
<!--    </div>-->

<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import {Message} from 'element-ui'-->

<!--export default {-->
<!--  name: "SecGroup",-->
<!--  data() {-->
<!--    return {-->
<!--      actName: "",-->
<!--      groupName: "",-->
<!--      activityID:-1,-->
<!--      groupID:-1,-->
<!--      participates:[],-->
<!--      experts:[],-->
<!--      multipleSelection: []-->
<!--    }-->
<!--  },-->
<!--  computed: {-->
<!--    user() {-->
<!--      return this.$store.state.currentHr; //object信息-->
<!--    },-->
<!--  },-->
<!--  mounted() {-->
<!--    console.log(this.$route.query)-->
<!--    this.actName = this.$route.query.actName;-->
<!--    this.groupName = this.$route.query.groupName;-->
<!--    this.activityID = this.$route.query.activityID;-->
<!--    this.groupID = this.$route.query.groupID;-->
<!--    this.initData();-->
<!--  },-->
<!--  methods: {-->
<!--    // rowClass() {-->
<!--    //   return 'background:#b3d8ff;color:black;font-size:13px'-->
<!--    // },-->
<!--    initData(){-->
<!--      this.getRequest("/secretary/getMember?activityID="+this.activityID+"&groupID="+this.groupID).then((resp)=>{-->
<!--        this.experts = resp.obj[1]-->
<!--        this.participates = resp.obj[0]-->
<!--        console.log(this.experts)-->
<!--      })-->
<!--    },-->
<!--    back() {-->
<!--      const _this = this;-->
<!--      _this.$router.push({-->
<!--        path: "/secretary/ActManage",-->
<!--      });-->
<!--    },-->
<!--    toggleSelection(rows) {-->
<!--      if (rows) {-->
<!--        rows.forEach(row => {-->
<!--          this.$refs.multipleTable.toggleRowSelection(row);-->
<!--        });-->
<!--      } else {-->
<!--        this.$refs.multipleTable.clearSelection();-->
<!--      }-->
<!--    },-->
<!--    handleSelectionChange(val) {-->
<!--      this.multipleSelection = val;-->
<!--      console.log(val)-->
<!--    }-->
<!--  },-->
<!--};-->
<!--</script>-->

<!--<style>-->
<!--.userinfo-container div {-->
<!--  font-size: 12px;-->
<!--  color: #409eff;-->
<!--}-->

<!--.userinfo-container {-->
<!--  margin-top: 20px;-->
<!--}-->

<!--.img-container {-->
<!--  width: 100%;-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--}-->

<!--.userface-img {-->
<!--  width: 72px;-->
<!--  height: 72px;-->
<!--  border-radius: 72px;-->
<!--}-->

<!--.hr-container {-->
<!--  margin-top: 10px;-->
<!--  display: flex;-->
<!--  flex-wrap: wrap;-->
<!--  justify-content: space-around;-->
<!--}-->

<!--.hr-card {-->
<!--  width: 350px;-->
<!--  margin-bottom: 20px;-->
<!--}-->

<!--.tb-edit.el-input {-->
<!--  display: none;-->
<!--}-->

<!--.tb-edit.current-row.el-input {-->
<!--  display: block;-->
<!--}-->

<!--.tb-edit.current-row.el-input + span {-->
<!--  display: none;-->
<!--}-->

<!--</style>-->
