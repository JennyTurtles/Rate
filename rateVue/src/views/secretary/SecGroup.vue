<template>
  <div>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center">{{ actName }} {{groupName}} 分组管理</div>
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {Message} from 'element-ui'

export default {
  name: "SecGroup",
  data() {
    return {
      actName: "",
      groupName: "",
      activityID:-1,
      groupID:-1,
      participates:"",
      experts:"",
    }
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
  },
  mounted() {
    console.log(this.$route.query)
    this.actName = this.$route.query.actName;
    this.groupName = this.$route.query.groupName;
    this.activityID = this.$route.query.activityID;
    this.groupID = this.$route.query.groupID;
    this.initData();
  },
  methods: {
    initData(){
      this.getRequest("/secretary/getMember?activityID="+this.activityID+"&groupID="+this.groupID).then((resp)=>{
        console.log(resp)
      })
    },
    back() {
      const _this = this;
      _this.$router.push({
        path: "/secretary/actGroup",
      });
    },
  },
};
</script>

<style>
.userinfo-container div {
  font-size: 12px;
  color: #409eff;
}

.userinfo-container {
  margin-top: 20px;
}

.img-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.userface-img {
  width: 72px;
  height: 72px;
  border-radius: 72px;
}

.hr-container {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}

.hr-card {
  width: 350px;
  margin-bottom: 20px;
}

.tb-edit.el-input {
  display: none;
}

.tb-edit.current-row.el-input {
  display: block;
}

.tb-edit.current-row.el-input + span {
  display: none;
}
</style>
