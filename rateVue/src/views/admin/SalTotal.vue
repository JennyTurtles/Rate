<template>
  <!--评分项设置-->
  <div>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center">{{ keywords_name }}活动 成绩查看设置</div>
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>

    <div><br/>双击单元格可编辑显示名称与及格分数</div>
    <div style="margin-top: 10px">
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
        <el-form-item label="已有信息选择" :label-width="formLabelWidth" center :model="item">
          <el-select v-model="currentfirst" placeholder="请选择" @change="firstChange($event)">
            <el-option
                v-for="x in first"
                :key="x.sourceName"
                :label="x.sourceName"
                :value="x.source"
                :disabled="x.disabled">
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
<!--        固定项-->
        <el-form-item label="项名" :label-width="formLabelWidth">
          <el-select v-model="currentsecond" placeholder="请选择" @change="sourceChange($event)">
            <el-option
                v-for="x in second"
                :key="x.sourceName"
                :label="x.sourceName"
                :value="x.source"
                :disabled="x.disabled">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="系数" :label-width="formLabelWidth">
          <el-input  v-model="currenttimes" placeholder="请输入系数" @change="timesChange($event)"></el-input>
        </el-form-item>
<!--        固定项-->

        <!-- 动态增加项目 -->
        <!-- 不止一个项目，用div包裹起来 -->
        <div v-if="radio===2" v-for="(i, index) in form.dynamicItem" :key="index">
          <el-form-item label="项名" :label-width="formLabelWidth">
            <el-select v-model="sourcename[index]" placeholder="请选择" @change="dynamicName($event,i,index)">
              <el-option
                  v-for="x in second"
                  :key="x.sourceName"
                  :label="x.sourceName"
                  :value="x.source">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="系数" :label-width="formLabelWidth">
            <el-input  v-model="xishu[index]" placeholder="请输入系数" @change="dynamicTimes($event,i,index)"></el-input>
          </el-form-item>
          <el-form-item>
            <i class="el-icon-delete" @click="deleteSecond(i, index)"></i>
          </el-form-item>
        </div>
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

export default {
  name: "SalTotal",
  data() {
    return {
      title: "",
      page: 1,
      keywords: "",
      keywords_name: "",
      size: 10,
      total: 0,
      loading: false,
      labelPosition: "left",
      hrs:[],
      first:[],
      second:[],
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
      form: {
        source: '',
        times: '',
        dynamicItem: [],
      },
      formRules: {
        source: [{required: true, message: '请选择项名', trigger: 'blur'}],
        times: [
          {required: true, message: '请输入系数', trigger: 'blur'},
        ]
      },
      radio: 1,
      formLabelWidth:'120px',
      xishu: [],
      sourcename:[],
      tabClickIndex: null, // 点击的单元格
      tabClickLabel: "", // 当前点击的列名
      currentfocusdata: "",
      currentfirst:"",
      currentsecond:"",
      currenttimes:"",
      dispalyname:"",
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  created() {
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.initHrs();
    this.initFirst();
    this.initSecond();
  },
  methods: {
    initHrs() {
      this.loading = true;
      this.getRequest(
          "/displayItem/all?activityID=" +
          this.keywords
      ).then((resp) => {
        console.log("resp",resp);
        if (resp) {
          this.loading = false;
          this.hrs = resp.data;
          this.total = resp.total;
          console.log(this.hrs);
        }
      });
    },
    initFirst(){
      this.loading = true;
      this.getRequest(
          "/displayItem/first?activityID=" +
          this.keywords
      ).then((resp) => {
        console.log("resp",resp);
        if (resp) {
          this.loading = false;
          this.first = resp.obj;
          console.log(this.first);
        }
      });
    },
    initSecond(){
      this.loading = true;
      this.getRequest(
          "/displayItem/second?activityID=" +
          this.keywords
      ).then((resp) => {
        console.log("resp",resp);
        if (resp) {
          this.loading = false;
          this.second = resp.obj;
          console.log(this.second);
        }
      });
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initHrs();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initHrs("advanced");
    },
    back() {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/search",
      });
    },
    tabClick(row, column, cell, event) {
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
    },
    tableRowClassName({row, rowIndex}) {
      // 把每一行的索引放进row
      row.index = rowIndex;
    },
    beforehandleEdit(index, row, label) {
      if (label === 'name') {
        this.currentfocusdata = row.name
      } else if (label === 'score') {
        this.currentfocusdata = row.passScore
      }
      this.currentfocusdata = row[label]
    },
    handleEdit(index, row, label) {
      //console.log(row);
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
        // this.newScoring(row)
      }
    },
    UpdateOrNew(displayItem) {
      console.log(displayItem);
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
      this.initFirst();
      this.initSecond();
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
    firstChange(event){
       this.currentfirst=event;
    },
    sourceChange(event){
      this.currentsecond=event;
    },
    timesChange(event){
      this.currenttimes=event;
    },
    dynamicName(event,item,index){
      this.form.dynamicItem[index].source=event;
      this.sourcename[index]=event;
    },
    dynamicTimes(event,item,index){
      this.form.dynamicItem[index].times=event;
      this.xishu[index]=event;
    },
    newSecond(){
      this.form.dynamicItem.push({
        source: '',
        times: '',
      })
      this.sourcename.push("");
      this.xishu.push("");
      console.log(this.form);
    },
    deleteSecond (item, index) {
      this.form.dynamicItem.splice(index, 1);
      this.xishu.splice(index,1);
      this.sourcename.splice(index,1);
    },
    deleteItem(row){
      this.$confirm("是否删除此展示项?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.deleteRequest("/displayItem/?ID="+row.id).then((resp) => {
              console.log(resp);
              if (resp) {
                if(resp.msg==='success')
                {Message.success('删除成功')}
                else
                {Message.error('删除失败')}
                this.initHrs();
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
        if (row.source.includes("*")){
          this.radio=2;
          var s=row.source;
          var items=s.split("+");
          for (let i=0;i<items.length;i++){
            if (i===0){
              var all=items[i].split("*");
              this.currenttimes=all[0];
              this.currentsecond=all[1];
              // for(var j in this.second){
              //   var value=this.second[j];
              //   if (value.source===all[1]) {
              //     this.currentsecond=value.sourceName;
              //   }
              // }
            }
            else{
              var all=items[i].split("*");
              this.form.dynamicItem.push({
                source: all[1],
                times: all[0],
              })
              this.xishu.push(all[0]);
              this.sourcename.push(all[1]);
              // for(var j in this.second){
              //   var value=this.second[j];
              //   if (value.source===all[1]) {
              //     this.sourcename.push(value.sourceName);
              //   }
              // }
            }
          }
        }
        else{
          this.radio=1;
          this.currentfirst=row.source;
        }
      }
      this.dialogVisible = true;
      console.log(this.radio);
    },
    doAdd(){
      if (this.radio===1){
        if (this.currentfirst===""){
          Message.warning('输入内容不能为空!')
        }
        else{
          for(var i in this.first){
            var value=this.first[i];
            if (value.source===this.currentfirst)
              this.dispalyname=value.sourceName;
          }
          this.item.source=this.currentfirst;
          this.item.name=this.dispalyname;
          this.UpdateOrNew(this.item);
          this.quit();
        }
      }
      else if (this.radio===2){
        if (this.currentsecond===""||this.currenttimes==="")
          Message.warning('输入内容不能为空!')
        else {
          this.item.source=this.currenttimes+"*"+this.currentsecond;
          if (this.form.dynamicItem.length>0){
            for(let i=0; i<this.form.dynamicItem.length; i++){
              this.item.source+="+"+this.form.dynamicItem[i].times+"*"+this.form.dynamicItem[i].source;
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
      this.currenttimes="";
      this.currentsecond="";
      this.radio=1;
      this.dispalyname="";
      let len=this.form.dynamicItem.length;
      this.sourcename.splice(0,len);
      this.xishu.splice(0,len);
      this.form.dynamicItem.splice(0,len);
    },
    newItem() {
      //console.log("creating")
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
      /*this.postRequest("/scoreItem/basic/insert", obj)
          .then((resp) => {
            this.initHrs();
          });*/
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
