<template>
  <div>
   <el-steps style="margin-bottom: 10px" :active="active" finish-status="success">
    <el-step title="基本信息"></el-step>
    <el-step title="信息项"></el-step>
    <el-step title="评分项"></el-step>
    <el-step title="成绩查看设置"></el-step>
    <el-step title="分组管理"></el-step>
    <el-step title="人员管理"></el-step>
   </el-steps>
   <el-button style="margin-top: 12px;margin-bottom: 10px; float: right" type="success" @click="next" >下一步</el-button>
  </div>
</template>

<script>
import store from '../store'
import router from '../router.js'
import sha1 from "sha1";
import { initMenu } from "../utils/menus.js";
import this_ from "@/main";
export default {
  name: "AddActStep",
  props:["active","actID","actName"], // 接受父组的loginForm，通过role判断角色
  data () {
    return {
     mode: 'admin',
    }
  },
 methods: {
   async next() {
    switch (this.active){
     case 0:
       const act = (await this.$parent.save()).obj;
       this.goInfoItem(act.id,act.name)
       break
     case 1:
       this.goScoreItem(this.actID,this.actName)
       break
     case 2:
       this.goDisplayItem(this.actID,this.actName)
       break
     case 3:
       this.goGroup(this.actID,this.actName)
       break
     case 4:
       this.goPeople(this.actID,this.actName)
       break
    }
   },
   saveAct(){

   },
  getQuery(actID,actName){
    return {
    keywords: actID,
    keyword_name: actName,
    mode:this.mode,
    addActive:this.active, // 标记步骤
   }
  },
   goInfoItem(actID,actName){
    const _this = this;
    _this.$router.push({
     path: "/ActivitM/infos",
     query: _this.getQuery(actID,actName),
    });
   },
   goScoreItem(actID,actName){
    const _this = this;
    _this.$router.push({
     path: "/ActivitM/month",
     query: _this.getQuery(actID,actName),
    });
   },
   goDisplayItem(actID,actName){
    const _this = this;
    _this.$router.push({
     path: "/ActivitM/total",
     query: this.getQuery(actID,actName),
    });
   },
  goGroup(actID,actName){
    const _this = this;
    var query = this.getQuery(actID,actName)
    query.haveSub = false
   _this.$router.push({
    path: "/ActivitM/table",
    query: query
   });
   },
  goPeople(actID,actName){
    const _this = this;
    // _this.$router.push({
    //  path: "/ActivitM/people",
    //  query: _this.getQuery(actID,actName),
    // });
   },
  },

}
</script>

<style>
.footer{
  margin-top: 8px;
  text-align: center;
}
.setPassword span:hover{
  color: #4b8ffe;
}
.setPassword{
  cursor: pointer;
  margin-top: -10px;
  margin-bottom: 5px;
  font-size: 12px;
  color: gray;
  text-decoration: none;
}
.loginContainer {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 15px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.loginTitle {
  margin: 15px auto 20px auto;
  text-align: center;
  color: #505458;
}

.loginRemember {
  text-align: left;
  margin: 0px 0px 15px 0px;
}
.el-form-item__content {
  display: flex;
  align-items: center;
}
</style>
