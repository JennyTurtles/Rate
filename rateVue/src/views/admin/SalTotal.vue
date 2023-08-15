<template>
  <div>
   <AddActStep v-show="typeof $route.query.addActive !== 'undefined'" :active="parseInt($route.query.addActive)" :actID="keywords" :act-name="keywords_name"></AddActStep>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center" v-show="!$route.query.addActive">{{ keywords_name }}成绩查看设置</div>
      <div style="margin-left: auto" v-show="typeof $route.query.addActive === 'undefined'">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div><br/>点击选择成绩展示方式：若选择默认方式则展示所有的评分项，若选择定制成绩查看界面则可进行自行定制</div>
    <div><br/>
      <el-radio-group v-model="setBySelf" style="padding-top: 5px" center @change="setChange">
        <el-radio :label=0 border>默认展示方式</el-radio>
        <el-radio :label=1 border>定制成绩查看界面</el-radio>
      </el-radio-group>
    </div>
    <div v-show="setBySelf === 1"><br/>双击单元格可编辑显示名称与及格分数</div>
    <div style="margin-top: 10px" v-show="setBySelf === 1">
      <el-table
          ref="multipleTable"
          :data="hrs"
          stripe
          border
          v-loading="loading"
          @cell-dblclick="tabClick"
          :row-class-name="tableRowClassName"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.08)"
          style="width: 100%"
      >
        <el-table-column
            prop="displaySequence"
            fixed
            align="center"
            label="序号"
            min-width="10%"
        >
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.displaySequence" :min="1" :max=total+1
                             controls-position="right"
                             size="mini"
                             min-width="3%"
            ></el-input-number>
            <el-button icon="" type="primary" @click="alterDisplay(scope.row)" size="mini"
                       style="padding: 4px"
                       plain >更改
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="10%" label="操作">
          <template slot-scope="scope">
            <el-button
                @click="UpdateOrNew(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-collection"
                type="primary"
                plain
                :disabled="scope.row.sourceName==='编号' || scope.row.sourceName==='姓名'"
            >保存
            </el-button
            >
            <el-button
                @click="deleteItem(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-delete"
                type="danger"
                plain
                center
                :disabled="scope.row.sourceName==='编号' || scope.row.sourceName==='姓名'"
            >删除</el-button
            >
          </template>
        </el-table-column>
        <el-table-column
            prop="sourceName"
            fixed
            align="center"
            label="数据来源"
            min-width="10%">
          <template slot-scope="scope">
            <span>{{scope.row.sourceName}}</span>
            <el-button v-if="scope.row.source!==null"
                       type="text"
                       icon="el-icon-edit"
                       size="medium"
                       style="padding-left: 5px"
                       v-show="scope.row.sourceName!=='编号' && scope.row.sourceName!=='姓名'"
                       @click="showEditView(scope.row)">
            </el-button>
            <el-button v-if="scope.row.source===null"
                       @click="showEditView(scope.row)"
                       style="padding: 4px"
                       size="mini"
                       icon="el-icon-plus"
                       type="primary"
                       plain
            >设置</el-button
            >
          </template>
        </el-table-column>
        <el-table-column
            prop="name"
            fixed
            align="center"
            label="显示名称"
            min-width="5%">
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '显示名称'
                "
                v-focus
                v-model.trim="scope.row.name"
                size="mini"
                maxlength="80"
                @focus="beforehandleEdit(scope.$index,scope.row,'name')"
                @change="handleEdit(scope.$index,scope.row,'name')"
                @blur="inputBlur"
            />
            <span
                style="width: 100%; height: 100%; display: inline-block"
                v-else
            >{{ scope.row.name }}</span
            >
          </template>
        </el-table-column>
        <el-table-column
            prop="passScore"
            fixed
            align="center"
            label="及格分数"
            min-width="5%">
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '及格分数'
                "
                v-focus
                v-model="scope.row.passScore"
                size="mini"
                maxlength="80"
                @focus="beforehandleEdit(scope.$index,scope.row,'score')"
                @change="handleEdit(scope.$index,scope.row,'score')"
                @blur="inputBlur"
            />
            <span
                style="width: 100%; height: 100%; display: inline-block"
                v-else
            >{{ scope.row.passScore }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0; display: flex; justify-content: left">
        <div style="margin-left: 8px">
          <el-button
              @click="newItem()"
              type="primary"
              icon="el-icon-plus"
          >新增
          </el-button
          >
        </div>
      </div>
    </div>
    <el-dialog :title="title" :visible.sync="dialogVisible" center :before-close="quit">
      <el-radio-group v-model="radio" style="padding-top: 5px" center>
        <el-radio :label="1" >使用已有数据</el-radio>
        <el-radio :label="2" >用公式计算</el-radio>
      </el-radio-group>
      <el-form v-if="radio===1" style="padding-top: 10px" :inline="true">
        <el-form-item label="选择活动" :label-width="formLabelWidth" center :model="item" v-if="subActivities.length>1">
          <el-select v-model="currentActivity" placeholder="请选择" @change="activityChange($event)">
            <el-option
                v-for="x in subActivities"
                :key="x.name"
                :label="x.name"
                :value="x.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="已有信息选择" :label-width="formLabelWidth" center :model="item" v-if="currentActivity!==null">
          <el-select v-model="currentfirst" placeholder="请选择" @change="firstChange($event)">
            <el-option
                v-for="x in subFirst"
                :key="x.sourceName"
                :label="x.sourceName"
                :value="x.source">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-form v-if="radio===2"
               ref="form"
               style="padding-top: 10px"
               :inline="true"
               :model="form"
               class="demo-form-inline">
        <!-- 动态增加项目 -->
        <!-- 不止一个项目，用div包裹起来 -->
        <div v-if="radio===2" v-for="(i, index) in dynamicItem" :key="index">
          <el-form-item label="选择活动" label-width="80px" center :model="item" v-if="subActivities.length>1" >
            <el-select v-model="dynamicItem[index].activity" placeholder="请选择" @change="dynamicActivity($event,i,index)" style="width: 150px">
              <el-option
                  v-for="x in subActivities"
                  :key="x.name"
                  :label="x.name"
                  :value="x.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="项名" label-width="80px">
            <el-select v-model="dynamicItem[index].source" placeholder="请选择" @change="dynamicName($event,i,index)" style="width: 150px">
              <el-option
                  v-for="x in dynamicItem[index].info"
                  :key="x.sourceName"
                  :label="x.sourceName"
                  :value="x.source">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="系数" label-width="80px">
            <el-input  v-model="dynamicItem[index].times" placeholder="请输入系数" @change="dynamicTimes($event,i,index)" style="width: 100px"></el-input>
          </el-form-item>
          <el-form-item>
            <i class="el-icon-delete" @click="deleteSecond(i, index)" v-show="index>0"></i>
          </el-form-item>
        </div>
        <!-- 动态增加项目 -->
      </el-form>
      <div v-if="radio===2" style="margin: 20px 0; display: flex; justify-content: left">
        <div style="margin-left: 8px">
          <el-button
              @click="newSecond()"
              type="primary"
              icon="el-icon-plus"
          >新增
          </el-button
          >
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="quit">取 消</el-button>
        <el-button type="primary" @click="doAdd">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from 'element-ui'
import AddActStep from "@/components/AddActStep.vue";

export default {
  name: "SalTotal",
 components: {AddActStep},
  data() {
    return {
      title: "",
      keywords: "",
      keywords_name: "",
      loading: false,
      labelPosition: "left",
      hrs:[],
      first:[],
      second:[],
      subActivities:[],
      dialogVisible: false,
      item:{
        id: null,
        activityID: null,
        name: null,
        content: null,
        passScore: null,
        source: null,
        sourceName: null,
      },
      dynamicItem: [{
        activity:'',
        info:[],
        source:'',
        times:'',
      }],
      radio: 1,
      formLabelWidth:'120px',
      activity:[], //展示动态项的活动
      times: [],     //展示动态项的系数
      source:[], //展示动态项的source
      tabClickIndex: null, // 点击的单元格
      tabClickLabel: "", // 当前点击的列名
      currentfocusdata: "",
      currentfirst:"", //展示第一类source
      currentActivity:null,//当前选择的活动
      subFirst:[],
      mode:'',
      setBySelf:0, //设置方式
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.mode = this.$route.query.mode;
    this.initMethod();
    this.initHrs();
  },
  methods: {
    initMethod(){
      this.loading = true;
      this.getRequest(
          "/displayItem/getSetMethod?activityID=" +
          this.keywords
      ).then((resp) => {
        this.setBySelf = resp;
      });
    },
    initHrs() {
      this.loading = true;
      this.getRequest(
          "/displayItem/all?activityID=" +
          this.keywords
      ).then((resp) => {
        if (resp) {
          this.loading = false;
          this.hrs = resp.data;
          this.total = resp.total;
        }
      });
      this.initFirst();
      this.initSecond();
    },
    initFirst(){
      this.loading = true;
      this.getRequest(
          "/displayItem/subFirst?activityID=" +
          this.keywords
      ).then((resp) => {
        if (resp) {
          // console.log(resp);
          this.loading = false;
          this.subActivities = resp.obj[0];
          this.first=resp.obj[1].dmap;
        }
      });
    },
    initSecond(){
      this.loading = true;
      this.getRequest(
          "/displayItem/subSecond?activityID=" +
          this.keywords
      ).then((resp) => {
        if (resp) {
          this.loading = false;
          this.second = resp.obj.dmap;
        }
      });
    },
    back() {
      const _this = this;
      if (this.mode === "admin"){
        _this.$router.push({
          path: "/ActivitM/search",
        });
      }else if (this.mode === "adminSub") {
        // _this.$router.push({
        //   path: "/ActivitM/SubActManage",
        //   query: {
        //     id: this.$route.query.backID,
        //   }
        // });
       this.$router.go(-1);
      }
    },
    tabClick(row, column, cell, event) {
      if (row.sourceName!=='姓名'&&row.sourceName!=='编号'){
        switch (column.label) {
          case "显示名称":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "及格分数":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          default:
            return;
        }
      }
    },
    tableRowClassName({row, rowIndex}) {
      // 把每一行的索引放进row
      row.index = rowIndex;
    },
    beforehandleEdit(index, row, label) {
      if (row.sourceName!=='姓名'&&row.sourceName!=='编号'){
        if (label === 'name') {
          this.currentfocusdata = row.name
        } else if (label === 'score') {
          this.currentfocusdata = row.passScore
        }
        this.currentfocusdata = row[label]
      }
    },
    handleEdit(index, row, label) {
      if (row[label] === ''&&label !== 'score') {
        Message.warning('输入内容不能为空!')
        if (label === 'name') {
          row.name = this.currentfocusdata
        } else if (label === 'score') {
          row.passScore = this.currentfocusdata
        }
        Message.warning('输入内容不能为空！')
        row[label] = this.currentfocusdata
      } else {
        this.UpdateOrNew(row)
      }
    },
    UpdateOrNew(displayItem) {
      // console.log(displayItem);
      const _this = this;
      _this
          .postRequest("/displayItem/save",displayItem)
          .then((resp) => {
            if(resp)
            {Message.success('更新成功！');
              this.reset();}
            else
            {Message.error('更新失败');}
          });
    },
    reset(){
      this.initHrs();
    },
    inputBlur() {
      this.tabClickIndex = null;
      this.tabClickLabel = "";
      this.currentfocusdata = ""
    },
    alterDisplay(row){
      const _this = this;
      //console.log(row);
      this.postRequest("/displayItem/alterDisplay?total="+this.total+"&activityID="+this.keywords,row).then((resp) => {
        this.reset();
      });
    },
    findSourceID(activityID,source){
      var id;
      var single=this.second[activityID];
      for(var i in single){
        var value=single[i];
        if (value.source===source) {
          id=value.id;
        }
      }
      return id;
    },
    findSource(source){
      var result;
      if (source.includes("displayitem")){
        var all=source.split(".");
        var id=all[1];
        var single = this.second[this.findActivityID(source,'second')];
        for(var i in single){
          var value=single[i];
          if (value.id==id) {
            result=value.source;
            break;
          }
        }
      }
      else{
        result=source;
      }
      return result;
    },
    findActivityID(source,kind){
      var id;
      if (kind==='first'){
        for(var i in this.first){
          var value = this.first[i];
          for (var j in value){
            if (source==value[j].source){
              id=i;
              break;
            }
          }
        }
      }
      else if (kind==='second'){
        if (source.includes("displayitem")){
          var items=source.split(".");
          for(var i in this.second){
            var value = this.second[i];
            for (var j in value){
              if (items[1]==value[j].id){
                id=i;
                break;
              }
            }
          }
        }
        else {
          for(var i in this.second){
            var value = this.second[i];
            for (var j in value){
              if (source==value[j].source){
                id=i;
                break;
              }
            }
          }
        }
      }
      return id;
    },
    findInfos(activityID){
      var value = this.second[activityID];
      var subSecond=[];
      for (var i in value){
        subSecond.push(value[i]);
      }
      return subSecond;
    },
    activityChange(event){
      this.currentActivity=event;
      var value = this.first[event];
      this.subFirst.splice(0);
      for (var i in value){
        this.subFirst.push(value[i]);
      }
      this.currentfirst="";
      for (var i = 0;i<this.subFirst.length;i++){
        if (this.subFirst[i].source===this.item.source){
          this.currentfirst=this.item.source;
        }
      }
    },
    firstChange(event){
       this.currentfirst=event;
    },
    dynamicActivity(event,item,index){
       this.dynamicItem[index].activity=event;
       this.dynamicItem[index].info=this.findInfos(event);
       this.activity.push(event);
    },
    dynamicName(event,item,index){
      this.dynamicItem[index].source=event;
    },

    dynamicTimes(event,item,index){
      this.dynamicItem[index].times=event;
    },
    newSecond(){
      if(this.subActivities.length>1){
        this.dynamicItem.push({
          activity:'',
          info: [],
          source: '',
          times: '',
        })
      }
      else {
        this.dynamicItem.push({
          activity:this.keywords,
          info: this.findInfos(this.keywords),
          source: '',
          times: '',
        })
      }
    },
    deleteSecond (item, index) {
      this.dynamicItem.splice(index, 1);
    },
    deleteItem(row){
      this.$confirm("是否删除此展示项?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.deleteRequest("/displayItem/?ID="+row.id+"&activityID="+this.keywords+"&total="+this.total).then((resp) => {
              if (resp) {
                if(resp.msg==='success')
                {Message.success('删除成功')}
                else
                {Message.error('删除失败')}
                this.initHrs();
                this.total=this.total-1;
              }
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
    },
    showEditView(row){
      this.title = "设置数据来源";
      this.item = row;
      if (row.source!==null){
        if (row.source.includes("*")){ //来源于公式计算
          this.radio=2;
          var s=row.source;
          var items=s.split("+"); //根据“+”分割，获得每一项的系数*项名
          for (let i=0;i<items.length;i++){
            var all=items[i].split("*"); //分割每一项的系数与项名
            if (i===0){ //添加到默认有的第一项中
              this.dynamicItem[0].activity=this.findActivityID(all[1],'second')*1;
              this.dynamicItem[0].info=this.findInfos(this.findActivityID(all[1],'second')*1);
              this.dynamicItem[0].times=all[0];
              this.dynamicItem[0].source=this.findSource(all[1]);
            }
            else{ //插入数组中
              this.dynamicItem.push({
                activity: this.findActivityID(all[1],'second')*1,
                info: this.findInfos(this.findActivityID(all[1],'second')*1),
                source: this.findSource(all[1]),
                times: all[0],
              })
            }
          }
        }
        else{ //来源于已有数据选择
          this.radio=1;
          this.currentfirst=row.source;
          this.currentActivity=this.findActivityID(this.currentfirst,'first')*1;
          if (this.subActivities.length===1){
            this.dynamicItem[0].info=this.findInfos(this.keywords);
          }
          var value = this.first[this.currentActivity];
          for (var i in value){
            this.subFirst.push(value[i]);
          }
        }
      }
      else {
          this.radio=1;
          this.currentActivity=this.keywords*1;
          var value = this.first[this.currentActivity];
          if (this.subActivities.length===1){
            this.dynamicItem[0].info=this.findInfos(this.keywords);
          }
          for (var i in value){
            this.subFirst.push(value[i]);
          }
      }
      this.dialogVisible = true;
    },
    doAdd(){
      if (this.radio===1){
        if (this.currentfirst===""){
          Message.warning('输入内容不能为空!')
        }
        else{
          if (this.currentActivity!==null){
            for(var i in this.subFirst){
              var value=this.subFirst[i];
              if (value.source===this.currentfirst)
                this.dispalyname=value.sourceName;
            }
          }
          else{
            for(var i in this.first){
              var value=this.first[i];
              if (value.source===this.currentfirst)
                this.dispalyname=value.sourceName;
            }
          }
          this.item.source=this.currentfirst;
          this.item.name=this.dispalyname;
          this.UpdateOrNew(this.item);
          this.quit();
        }
      }
      else if (this.radio===2){
        var flag=true; //判断输入是否合法
        for (let i=0;i<this.dynamicItem.length;i++){
          if (this.dynamicItem[i].source===''||this.dynamicItem[i].times===''){
            Message.warning('输入内容不能为空!')
            flag=false;
            break;
          }
        }
        if (flag) {
          this.item.source=null;
          for(let i=0; i<this.dynamicItem.length; i++){
            if (this.dynamicItem[i].source.includes("*")){
              var name="displayitem."+this.findSourceID(this.dynamicItem[i].activity*1,this.dynamicItem[i].source);
              if (this.item.source===null)
                this.item.source=this.dynamicItem[i].times+"*"+name;
              else
                this.item.source+="+"+this.dynamicItem[i].times+"*"+name;
            }
            else{
              if (this.item.source===null)
                this.item.source=this.dynamicItem[i].times+"*"+this.dynamicItem[i].source;
              else
                this.item.source+="+"+this.dynamicItem[i].times+"*"+this.dynamicItem[i].source;
            }
          }
          this.item.name="请输入显示名称";
          this.item.passScore=null;
          this.UpdateOrNew(this.item);
          this.quit();
        }
      }
    },
    quit(){
      this.dialogVisible=false;
      this.currentfirst="";
      this.radio=1;
      this.currentActivity=null;
      this.source.splice(0);
      this.times.splice(0);
      this.dynamicItem.splice(0);
      this.newSecond();
      this.subFirst.splice(0);
    },
    newItem() {
      let obj = {};
      obj.activityID = this.keywords;
      obj.id = null;
      obj.displaySequence = this.total+1;
      obj.name = '请输入显示名称';
      obj.source= null;
      obj.passScore = null;
      this.hrs.push(obj);
      this.UpdateOrNew(obj);
      this.initHrs();
    },
    setChange(){
      this.postRequest("/displayItem/changeMethod?activityID="+this.keywords+"&setByself="+this.setBySelf).then((resp) => {
        if (resp.msg==='success'){
          {Message.success('更改成功')}
        }
        else
          {Message.success('更改失败')}
      });
    }
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
