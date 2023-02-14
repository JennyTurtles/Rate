<template>
  <div>
    <div>
      <div
        style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <div>
              <label style="fontSize:10px">学生姓名：</label>
              <el-select
                style="margin-left:5px;width:100px"
                prefix-icon="el-icon-edit"
                clearable
                filterable
                placeholder="学生姓名"
                @change="((val) => filter(val,'select_stuname'))"
                id="select_stuname"
                >
                  <el-option
                    style="width:90px"
                    v-for="val in select_stuName"
                    :key="val"
                    :value="val"
                  >
                  </el-option>
              </el-select>

              <label style="fontSize:10px;margin-left:20px">论文名称：</label>
              <el-select
                  style="margin-left:5px;width:130px"
                  prefix-icon="el-icon-edit"
                  clearable
                  filterable
                  placeholder="论文名称"
                  @change="((val) => filter(val,'select_paperName'))"
                  id="select_paperName"
                  >
                    <el-option
                      style=""
                      v-for="val in select_paperName"
                      :key="val"
                      :value="val"
                    >
                    </el-option>
              </el-select>
              <label style="fontSize:10px;margin-left:20px">期刊：</label>
              <el-select
                  style="margin-left:5px;width:140px"
                  prefix-icon="el-icon-edit"
                  clearable
                  filterable
                  placeholder="期刊"
                  @change="((val) => filter(val,'select_pub'))"
                  id="select_pub"
                  >
                    <el-option
                      style=""
                      v-for="val in select_pubName"
                      :key="val"
                      :value="val"
                    >
                    </el-option>
              </el-select>
              <label style="fontSize:10px;margin-left:20px">论文状态：</label>
              <el-select
                  style="margin-left:5px;width:100px"
                  prefix-icon="el-icon-edit"
                  clearable
                  filterable
                  placeholder="状态筛选"
                  @change="((val) => filter(val,'select_state'))"
                  id="select_state"
                  >
                    <el-option
                      style="width:90px"
                      v-for="val in option"
                      :key="val"
                      :value="val"
                    >
                    </el-option>
              </el-select>
              <label style="fontSize:10px;margin-left:20px">积分范围：</label>
              <el-select
                  style="margin-left:5px;width:70px"
                  prefix-icon="el-icon-edit"
                  clearable
                  filterable
                  placeholder="积分"
                  @change="((val) => filter(val,'select_point1'))"
                  id="select_point1"
                  >
                    <el-option
                      style=""
                      v-for="val in select_point"
                      :key="val"
                      :value="val"
                    >
                    </el-option>
              </el-select>
              <label >&nbsp; - &nbsp;</label>
              <el-select
                  style="margin-left:5px;width:70px"
                  prefix-icon="el-icon-edit"
                  clearable
                  filterable
                  placeholder="积分"
                  @change="((val) => filter(val,'select_point2'))"
                  id="select_point2"
                  >
                    <el-option
                      style=""
                      v-for="val in select_point"
                      :key="val"
                      :value="val"
                    >
                    </el-option>
              </el-select>
          </div>
          <div style="margin-top:20px">
              <el-button
              icon="el-icon-search"
              type="primary"
              @click="searchEmps"
              :disabled="showAdvanceSearchView"
              style="margin-left:5px"
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
          </div>
        </div>
      </div>
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
        <el-table-column
          prop="student.sname"
          align="center"
          label="学生姓名"
          width="75"
        >
        </el-table-column>
        <el-table-column
          prop="name"
          align="center"
          label="论文名称"
        >
        </el-table-column>
        <!-- width="200" -->
        <el-table-column
          prop="state"
          label="状态"
          width="100"
          align="center"
        >
        <template slot-scope="scope">
            <span
              style="padding: 4px"
              size="mini"
              :model="emp.state"
              :style="scope.row.state=='tea_reject' ||  scope.row.state=='adm_reject'? {'color':'red'}:{'color':'gray'}"
              >
              {{scope.row.state=="commit" 
              ? "待审核"
              :scope.row.state=="tea_pass"
              ? "导师通过"
              :scope.row.state=="tea_reject"
              ? "导师驳回"
              :scope.row.state=="adm_pass"
              ? "管理员通过"
              :"管理员驳回"}}
              </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="publication.name"
          label="发表刊物"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="point"
          label="积分"
          align="center"
          width="60"
        >
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注"
          align="center"
          width="300"
        >
        </el-table-column>
        <el-table-column
          width="130"
          align="center"
          label="审核"
        >
          <template slot-scope="scope">
            <el-button
              @click="showEditEmpView_show(scope.row)"
              style="padding: 4px"
              size="mini"
              >查看详情</el-button
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

    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" center>
      <el-form
        :label-position="labelPosition"
        label-width="100px"
        :model="emp"
        :rules="rules"
        ref="empForm"      
      >
        <el-form-item label="论文名称:" prop="name">
          <el-input
            size="mini"
            style="width: 200px"
            prefix-icon="el-icon-edit"
            v-model="emp.name"
            placeholder="请输入单位名称"
          ></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp">确 定</el-button>
      </span>
    </el-dialog>

<!-- 对话框 老师审核通过论文 -->
    <el-dialog :title="title" 
    :visible.sync="dialogVisible_pass" width="30%" center>
        <!-- 确定审核通过该学生论文？ -->
        <el-form
        :label-position="labelPosition"
        label-width="80px"
        :model="emp"
        :rules="rules"
        ref="empForm"
        style="margin-left: 60px"
      >
        <el-form-item label="论文ID:" prop="id">
          <span>{{ emp.id }}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="dialogVisible_pass = false">取 消</el-button> -->
        <el-button type="primary" @click="auditing_commit('tea_pass')">确 定</el-button>
      </span>
    </el-dialog>
<!-- 对话框 老师驳回该学生论文 -->
    <el-dialog :title="title" 
    :visible.sync="dialogVisible_reject" width="30%" center>
        
        <el-form
        :label-position="labelPosition"
        label-width="80px"
        :model="emp"
        :rules="rules"
        ref="empForm"
        style="margin-left: 40px"
      >
        <el-form-item label="论文ID:" prop="id">
          <span>{{ emp.id }}</span>
        </el-form-item>
        <el-form-item label="驳回理由:">
          <el-input
            type="textarea"
            :rows="4"
            v-model="reason"
            placeholder="驳回理由"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="dialogVisible_pass = false">取 消</el-button> -->
        <el-button type="primary" @click="auditing_commit('tea_reject')">确 定</el-button>
      </span>
    </el-dialog>

<!-- 查看详情按钮 -->
    <el-dialog
      :title="title_show"
      :visible.sync="dialogVisible_show"
      width="520px"
      center>
      <el-form
        :label-position="labelPosition"
        label-width="80px"
        :model="emp"
        :rules="rules"
        ref="empForm"
        style="margin-left: 20px"
      >
        <el-form-item label="论文名:" prop="name">
          <span>{{ emp.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="学生姓名:" prop="student">
          <span>{{ emp.student.sname }}</span
          ><br />
        </el-form-item>
        <!-- <el-form-item label="发表期刊:" prop="publication">
          <span>{{ emp.publication }}</span
          ><br />
        </el-form-item> -->
        <el-form-item label="期刊页码:" prop="pubPage">
          <span>{{ emp.pubPage}}</span
          ><br />
        </el-form-item>
        <el-form-item label="论文状态:" prop="state">
          <span>{{emp.state}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者人数:" prop="total">
          <span>{{emp.total}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者列表:" prop="author">
          <span>{{emp.author}}</span
          ><br />
        </el-form-item>
        <el-form-item label="排名:" prop="rank">
          <span>{{emp.rank}}</span
          ><br />
        </el-form-item>
        <el-form-item label="发表年份:" prop="year">
          <span>{{emp.year}}</span
          ><br />
        </el-form-item>
        <el-form-item label="发表月份:" prop="month">
          <span>{{emp.month}}</span
          ><br />
        </el-form-item>
        <div >
          <span>历史操作:</span>
          <el-form-item  v-model="operList" v-for="item in operList" :key="item.time" style="margin-bottom:8px;">
            <div style="color:gray;font-size:5px;line-height:18px">
              <label >操作角色：{{item.operatorRole}}</label>&nbsp;&nbsp;&nbsp;&nbsp;
              <label >操作人员：{{item.operatorName}}</label><br/>
              <span >操作类型：{{item.operation}}</span>&nbsp;&nbsp;&nbsp;
              <span >操作时间：{{item.time|dataFormat}}</span><br/>
              <span >历史驳回：{{item.remark}}</span><br/>
            </div>
          <!-- <br/> -->
          </el-form-item>
        </div> 
      </el-form>
        <span slot="footer" class="dialog-footer" :model="emp">
            <el-button
                  id="but_pass"
                  v-show="emp.state=='commit' || emp.state=='tea_reject'? true:false"
                  @click="auditing_commit('tea_pass')"
                  type="primary"
                  >审核通过</el-button>
            <el-button
                  id="but_reject"
                  v-show="emp.state=='commit' ? true:false"
                  @click="isShowInfo = true"
                  type="primary"
                  >审核不通过</el-button>
            <el-button
                  id="but_reject"
                  v-show="emp.state=='tea_pass' ? true:false"
                  @click="dialogVisible_show = false"
                  type="primary"
                  >关闭</el-button>
        </span>     
    </el-dialog>
    <el-dialog v-model="emp" :visible.sync="isShowInfo">
        <el-input
            type="textarea"
            :rows="4"
            v-model="reason"
            placeholder="请输入论文驳回理由"
          >
        </el-input>
        <span slot="footer">
          <el-button @click="auditing_commit('tea_reject')"
                type="primary">
                确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import { set } from 'vue';
export default {
  name: "SalSearch",
  data() {
    return {
      operList:[],
      isShowInfo:false,
      select_stuName:["全部"],//筛选框
      select_paperName:["全部"],
      select_point:['全部',1,3,4,6,9,12,15],
      select_pubName:["全部"],
      option:["全部","通过","待审核","驳回"],
      labelPosition: "left",
      title: "",
      title_show: "",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      copyemps:[],
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_pass: false,
      dialogVisible_reject: false,
      dialogVisible_show: false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      positions: [],
      reason:"",
      oper:{
        operatorRole:"teacher",
        operatorID:JSON.parse(sessionStorage.getItem('user')).id,
        operatorName:JSON.parse(sessionStorage.getItem('user')).name,
        paperID:null,
        paperName:null,
        pubID:null,
        pubName:null,
        operation:"",
        state:"",
        remark:""
      },
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
        state:"",
        student:{},
        total:0,
        rank:0
        // reason:"",
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        name: [{ required: true, message: "请输入论文名", trigger: "blur" }],
        startDate: [
          { required: true, message: "请输入论文时间", trigger: "blur" },
        ],
        scoreItemCount: [
          {
            required: true,
            type: "number",
            message: "请输入正确数据",
            trigger: "blur",
            transform: (value) => Number(value),
          },
        ],
        comment: [{ required: true, message: "请输入备注", trigger: "blur" }],
      },
    };
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
  },
  created() {},
  mounted() {
    this.initEmps();
    this.initPositions();
  },
  methods: {
    filter(val,options){
      document.getElementById(options).value=val
    },
    //点击对话框中的确定按钮 触发事件
    auditing_commit(num){
      // this.emp=data      
      this.loading = true;
      let url;
      const _this=this
      this.dialogVisible_show=false
      url= "/paper/basic/edit_state?state="+num
      +"&ID="+this.emp.id
      if(num == "tea_reject"){
        this.isShowInfo=true
      }
      this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            console.log(resp);
            this.emp.state=num
            this.total = resp.total;
            this.isShowInfo=false
            this.doAddOper(num,this.reason,
                  this.emp.id,this.emp.name,
                  this.emp.pubName,this.emp.pubid)
            }
      });
               
    },
    doAddOper(state,reamrk,paperID,paperName,pubName,pubID) {
      this.oper.state=state
      this.oper.remark=reamrk,
      this.oper.paperID=paperID,
      this.oper.paperName=paperName,
      this.oper.pubName=pubName,
      this.oper.pubID=pubID
      if(this.oper.state=="tea_pass"){
        this.oper.operation="审核通过"
      }else{
        this.oper.operation="审核驳回"
      }
      this.postRequest1("/paperoper/basic/add", this.oper).then(
        (resp) => {
          if (resp) {
            console.log(resp)
            this.initEmps()
          }
        }
      );    
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    /** 查询角色列表 */
    onError(err, file, fileList) {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
    },
    onSuccess(response, file, fileList) {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
      this.initEmps();
    },
    beforeUpload() {
      this.importDataBtnText = "正在导入";
      this.importDataBtnIcon = "el-icon-loading";
      this.importDataDisabled = true;
    },
    emptyEmp() {
      this.emp = {
        id: null,
        startDate: null,
        name: "",
        scoreItemCount: "0",
        comment: "论文备注example：关于xxx的论文",
      };
    },
    showEditEmpView(data) {//修改论文
      console.log(data.id)
      this.initPositions();
      this.title = "编辑单位信息";
      this.emp = data;
      this.dialogVisible = true;
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
      this.getRequest("/paperoper/basic/List?ID="+data.id).then((resp) => {
          this.loading = false;
          if (resp) {
            console.log("/paperoper/basic/List?ID=");
            console.log(resp);
            this.isShowInfo=false
            this.operList=resp.data
          }
      });
      // if(this.emp.state == 'tea_pass'){
      //   this.isShowInfo=true
      // }else{
      //   this.isShowInfo=false
      // }
    },
    deleteEmp(data) {
      console.log(data);
      this.$confirm(
        "此操作将永久删除【" + data.name + "】, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",  
        }
      ).then(() => {
        this.postRequest("/paper/basic/remove", {ID:data.id}).then((resp) => {
          if (resp) {
            console.log(resp)
            this.dialogVisible = false;
            this.initEmps();
          }
        });
      });
    },
    doAddEmp() {
      if (this.emp.id) {
        console.log(this.emp);
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            const _this = this;
            this.postRequest("/activities/basic/update", _this.emp).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.initEmps();
                }
              }
            );
          }
        });
      } else {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.institutionID = this.user.id;
            console.log(this.emp);
            console.log(this.user.id);
            const _this = this;
            this.postRequest("/activities/basic/insert", _this.emp).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.initEmps();
                }
              }
            );
          }
        });
      }
    },
    initPositions() {
      /*this.getRequest('/employee/basic/positions').then(resp => {
        if (resp) {
          this.positions = resp;
        }
      })*/
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    initEmps() {
      this.loading = true;
      let url = "/paper/basic/List";
      console.log(url);
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          console.log(resp);
          this.emps = resp.data;
          this.copyemps=this.emps
          this.total = resp.total;
          for(var i=0;i<this.emps.length;i++){
            var papername=this.emps[i].name
            if(this.select_paperName.indexOf(papername)==-1){
              this.select_paperName.push(papername)
            }
            var judge=this.emps[i].student.sname
            if(this.select_stuName.indexOf(judge)==-1){
              this.select_stuName.push(judge)
            }
            var pub=this.emps[i].publication.name
            if(this.select_pubName.indexOf(pub)==-1){
              this.select_pubName.push(pub)
            }
          }
          this.emps.sort(function(a,b){
            return a.time > b.time ? -1 : 1
          })
        }
      });
    },
    showGroupmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/table",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showInsertmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/group",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showteachermanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showScoreItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/month",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    searchEmps() {//根据条件搜索论文
      var newemps=new Set()
      // var copyemps=this.emps
      var stuname=document.getElementById("select_stuname").value
      var state=document.getElementById("select_state").value
      if(state == '通过'){
        state = 'tea_pass'
      }else if(state == '驳回'){
        state = 'tea_reject'
      }if(state == '待审核'){
        state = 'commit'
      }
      var paper=document.getElementById("select_paperName").value
      var pub=document.getElementById("select_pub").value
      var point1=document.getElementById("select_point1").value
      var point2=document.getElementById("select_point2").value
      for(var i=0;i<this.copyemps.length;i++){
        if((((this.copyemps[i].student.sname == stuname))||(stuname == '全部' || stuname == ''))&&
        (((this.copyemps[i].state == state))||(state == '全部' || state == ''))&&
        (((this.copyemps[i].name == paper) )||(paper == '全部' || paper == ''))&&
        (((this.copyemps[i].point <= point2 &&this.copyemps[i].point >= point1))||(point1 == '全部' || point1 == '' || point2 == '全部' || point2 == ''))&&
        (((this.copyemps[i].publication.name == pub) )||(pub == '全部' || pub == ''))
        ){
            newemps.add(this.copyemps[i])
        }
      }
      this.emps=Array.from(newemps)
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
  /* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}
</style>