<template>
  <div>
   <el-steps style="margin-bottom: 10px;" :active="active" finish-status="success">
    <el-step title="基本信息"></el-step>
    <el-step title="信息项"></el-step>
    <el-step title="评分项"></el-step>
    <el-step title="成绩查看设置"></el-step>
    <el-step title="分组管理"></el-step>
    <el-step title="人员管理"></el-step>
   </el-steps>
   <el-button style="margin-top: 12px;margin-bottom: 10px; float: right" type="success" @click="goAct" v-if="active===5">完成</el-button>
   <el-button style="margin-top: 12px;margin-bottom: 10px; float: right" type="success" @click="next" v-else>下一步</el-button>
   <el-button style="margin-top: 12px;margin-bottom: 10px;float: right;margin-right: 10px" type="primary" @click="back" >返回</el-button>
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
  props:["active","actID","actName"],
  data () {
    return {
     mode: 'admin',
     haveSub: 0,
    }
  },
 methods: {
   async next() {
    switch (this.active){
     case 0:
      var act = null;
      if (typeof this.actID === 'undefined'){
       act = (await this.$parent.add()).obj;
       this.haveSub = act.haveSub
       this.goInfoItem(act.id,act.name,true)
      }
      else{
       this.$parent.save(this.actID)
       this.goInfoItem(this.actID,this.actName,true)
      }
       break
     case 1:
       this.goScoreItem(this.actID,this.actName,true)
       break
     case 2:
       this.goDisplayItem(this.actID,this.actName,true)
       break
     case 3:
       this.goGroup(this.actID,this.actName,true)
       break
     case 4:
       this.goPeople(this.actID,this.actName,true)
       break
    }
   },
  back(){
   switch (this.active){
    case 0:
     this.goAct()
     break
    case 1:
     this.goAddAct(this.actID,this.actName,false)
     break
    case 2:
     this.goInfoItem(this.actID,this.actName,false)
     break
    case 3:
     this.goScoreItem(this.actID,this.actName,false)
     break
    case 4:
     this.goDisplayItem(this.actID,this.actName,false)
     break
    case 5:
     this.goGroup(this.actID,this.actName,false)
     break
   }
  },
  getQuery(actID,actName){
    return {
    keywords: actID,
    keyword_name: actName,
    mode:this.mode,
    addActive:this.active+1, // 标记步骤
    haveSub: this.active !== 0 ? this.$route.query.haveSub : this.haveSub
   }
  },
  getQueryBack(actID,actName){
   return {
    keywords: actID,
    keyword_name: actName,
    mode:this.mode,
    addActive:this.active-1, // 标记步骤
    haveSub: typeof this.$route.query.haveSub !== 'undefined' ? this.$route.query.haveSub : this.haveSub
   }
  },
  getQuerySub(actID){
   return {
    parentID: actID,
    mode:'adminSub',
    addActive:0,
   }},
  goAct(){
   // 弹出一个对话框，提示是否要添加子活动
   const _this = this;
   if (this.mode == 'adminSub' || this.$route.query.haveSub == 1){
    _this.$confirm('是否要添加子活动？', '提示', {
     confirmButtonText: '确定',
     cancelButtonText: '取消',
     type: 'warning'
    }).then(() => {
     _this.$router.push({
      path: "/Admin/addAct",
      query: this.getQuerySub(_this.actID)
     });
    }).catch(() => {
     _this.$router.push({
      path: "/ActivitM/search",
     });
    });
   }else {
    _this.$router.push({
     path: "/ActivitM/search",
    });
   }

  },
  goAddAct(actID,actName,flag){
   const _this = this;
   _this.$router.push({
    path: "/Admin/addAct",
    query: flag ? _this.getQuery(actID,actName) : _this.getQueryBack(actID,actName)
   });
  },
   goInfoItem(actID,actName,flag){ // flag为true表示下一步，false表示返回
    const _this = this;
    _this.$router.push({
     path: "/ActivitM/infos",
     query: flag ? _this.getQuery(actID,actName) : _this.getQueryBack(actID,actName)
    });
   },
   goScoreItem(actID,actName,flag){
    const _this = this;
    _this.$router.push({
     path: "/ActivitM/month",
     query: flag ? _this.getQuery(actID,actName) : _this.getQueryBack(actID,actName)
    });
   },
   goDisplayItem(actID,actName,flag){
    const _this = this;
    _this.$router.push({
     path: "/ActivitM/total",
     query: flag ? _this.getQuery(actID,actName) : _this.getQueryBack(actID,actName)
    });
   },
  goGroup(actID,actName,flag){
    const _this = this;
    var query = flag ? _this.getQuery(actID,actName) : _this.getQueryBack(actID,actName)
    // query.haveSub = false
   _this.$router.push({
    path: "/ActivitM/table",
    query: query
   });
   },
  goPeople(actID,actName,flag){
    const _this = this;
    _this.$router.push({
     path: "/ActivitM/group",
     query: flag ? _this.getQuery(actID,actName) : _this.getQueryBack(actID,actName)
    });
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
