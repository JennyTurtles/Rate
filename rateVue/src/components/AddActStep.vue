<template>
  <div>
   <div style="text-align: center;font-size: 30px" v-if="mode === 'admin'">
    添加活动
   </div>
   <div style="text-align: center;font-size: 30px" v-if="mode === 'adminSub'">
    添加子活动{{this.$route.query.subActNo}}
   </div>
   <el-steps style="margin-bottom: 10px;" :active="active" finish-status="success">
    <el-step title="基本信息"></el-step>
    <el-step title="信息项"></el-step>
    <el-step title="评分项"></el-step>
    <el-step title="成绩查看设置"></el-step>
    <el-step title="分组管理" v-if="mode !== 'adminSub'"></el-step>
    <el-step title="人员管理" v-if="mode !== 'adminSub'"></el-step>
   </el-steps>
   <el-button style="margin-top: 12px;margin-bottom: 10px; float: right" type="success" @click="goAct" v-if="active===5 || active === 3 && mode === 'adminSub' ">完成</el-button>
   <el-button style="margin-top: 12px;margin-bottom: 10px; float: right" type="success" @click="next" v-else>下一步</el-button>
   <el-button style="margin-top: 12px;margin-bottom: 10px;float: right;margin-right: 10px" type="primary" @click="back" v-if="active == '0'">返回</el-button>
   <el-button style="margin-top: 12px;margin-bottom: 10px;float: right;margin-right: 10px" type="primary" @click="back" v-else>上一步</el-button>
  </div>
</template>

<script>
export default {
  name: "AddActStep",
  props:["active","actID","actName","groupNum"],
  data () {
    return {
     mode: 'admin',
     haveSub: 0,
     requireGroup: '',
    }
  },
 mounted() {
  this.mode = this.$route.query.mode
  this.requireGroup = this.$route.query.requireGroup
 },
 methods: {
   async next() {
    switch (this.active){
     case 0:
      var act = null;
      if (typeof this.actID === 'undefined'){
       act = (await this.$parent.add()).obj;
       this.haveSub = act.haveSub
       this.requireGroup = act.requireGroup
       this.goInfoItem(act.id,act.name,true)
      }
      else{
       act = await this.$parent.save(this.actID)
       this.haveSub = act.haveSub
       this.requireGroup = act.requireGroup
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
       if (this.$route.query.requireGroup && this.$route.query.requireGroup === '0' && this.mode === 'adminSub'){
        this.active += 1
        this.goPeople(this.actID,this.actName,true)
       }
       else
        this.goGroup(this.actID,this.actName,true)
       break
     case 4:
      if (this.groupNum === 0){
       this.goPeople(this.actID,this.actName,true)
      }else
       this.goGroup(this.actID,this.actName,true)
      break
    }
   },
  back(){
   switch (this.active){
    case 0:
     if (this.$route.query.mode === 'adminSub'){
      this.$router.push({
       path: "/ActivitM/SubActManage",
       query:{id:this.$route.query.parentID}
      });
     }else{
      this.$router.push({
       path: "/ActivitM/search",
      });
     }
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
     if (this.$route.query.requireGroup && this.$route.query.requireGroup === '0' && this.mode === 'adminSub'){
      this.active -= 1
      this.goDisplayItem(this.actID,this.actName,false)
     }else
      this.goGroup(this.actID,this.actName,false)
     break
   }
  },
  getQuery(actID,actName){
    return {
    keywords: actID,
    keyword_name: actName,
    mode:this.$route.query.mode,
    addActive:this.active+1, // 标记步骤
    haveSub: this.active !== 0 ? this.$route.query.haveSub : this.haveSub,
    parentID:this.$route.query.parentID,
    requireGroup: this.active !== 0 ? this.$route.query.requireGroup : this.requireGroup,
    groupNums: this.groupNum,
    subActNo: this.$route.query.subActNo
   }
  },
  getQueryBack(actID,actName){
   return {
    keywords: actID,
    keyword_name: actName,
    mode:this.$route.query.mode,
    addActive:this.active-1, // 标记步骤
    haveSub: typeof this.$route.query.haveSub !== 'undefined' ? this.$route.query.haveSub : this.haveSub,
    parentID:this.$route.query.parentID,
    requireGroup: this.$route.query.requireGroup,
    subActNo: this.$route.query.subActNo
   }
  },
  getQuerySub(actID){
   return {
    parentID: this.$route.query.mode === 'adminSub' ? this.$route.query.parentID : actID, // 从子活动到子活动和从主活动到子活动
    mode:'adminSub',
    addActive:0,
    subActNo: this.$route.query.subActNo ? parseInt(this.$route.query.subActNo) + 1: 1,
   }},
  goAct(){
   // 弹出一个对话框，提示是否要添加子活动
   const _this = this;
   if (this.$route.query.subActNo || this.$route.query.haveSub == 1){
    _this.$confirm(this.$route.query.haveSub == 1 ? '是否要添加子活动？' : '是否要继续添加子活动？', '提示', {
     confirmButtonText: '是',
     cancelButtonText: '否',
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
   }else if (this.$route.query.mode === 'adminSub'){ // 在子活动里面添加返回子活动管理界面
    this.$router.push({
     path: "/ActivitM/SubActManage",
     query: {
      id: this.$route.query.parentID,
     },
    })
   }
   else {
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
   if (typeof this.$route.query.groupID !== 'undefined' && !flag){ // 处理从组内人员管理返回，此时addActive不应该减1
    _this.$router.push({
     path: "/ActivitM/table",
     query: {
      keywords: actID,
      keyword_name: actName,
      mode:this.mode,
      addActive:this.active,
      haveSub: typeof this.$route.query.haveSub !== 'undefined' ? this.$route.query.haveSub : this.haveSub
     }
    });
    return
   }
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
