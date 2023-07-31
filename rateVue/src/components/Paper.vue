<template>
  <div>
    <div
        style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
              <label style="fontSize:10px">学生姓名：</label>
              <input type="text" 
                style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
                placeholder="学生姓名" autocomplete="off"
                id="select_stuname">
              <label style="fontSize:10px;margin-left:16px">论文名称：</label>
              <input type="text" 
                style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
                placeholder="论文名称"
                id="select_paperName">
              <label style="fontSize:10px;margin-left:16px">期刊：</label>
              <div class="select_div_input">
                <el-select
                    v-model="searchPaperPublicationName"
                    filterable
                    remote
                    clearable
                    reserve-keyword
                    @change="selectOption($event)"
                    :remote-method="searchPublicationMethod"
                    placeholder="期刊名称"
                    :loading="loading">
                  <el-option
                      v-for="item in select_pubName"
                      :key="item.index"
                      :label="item.value"
                      :value="item.value">
                  </el-option>
                </el-select>
                </div>
              <label style="fontSize:10px;margin-left:40px;">论文状态：</label>
              <el-select
                  v-model="tmp1"
                  style="margin-left:3px;width:120px"
                  prefix-icon="el-icon-edit"
                  clearable
                  filterable
                  placeholder="状态筛选"
                  @change="((val) => filter(val,'select_state'))"
                  id="select_state"
                  >
                    <el-option
                      v-for="val in option"
                      :key="val"
                      :value="val"
                    >
                    </el-option>
              </el-select>
              <label style="fontSize:10px;margin-left:16px">积分范围：</label>
              <el-select
                  v-model="tmp2"
                  style="margin-left:3px;width:60px"
                  prefix-icon="el-icon-edit"
                  clearable
                  filterable
                  placeholder="1"
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
                  v-model="tmp3"
                  style="margin-left:3px;width:60px"
                  prefix-icon="el-icon-edit"
                  clearable
                  filterable
                  placeholder="12"
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
              <el-button
                  icon="el-icon-search"
                  type="primary"
                  @click="searchEmps(1, 15)"
                  :disabled="showAdvanceSearchView"
                  style="margin-left:30px"
                  >
                  搜索
              </el-button>
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
          width="230"
        >
        </el-table-column>
        <!-- width="200" -->
        <el-table-column
          prop="state"
          label="状态"
          width="110"
          align="center"
        >
        <template slot-scope="scope">
            <span
              style="padding: 4px"
              size="mini"
              :model="emp.state"
              :style="(scope.row.state=='tea_reject' || scope.row.state=='adm_reject') ? {'color':'red'}:{'color':'gray'}"
              >
              {{scope.row.state=="commit" 
              ? "学生提交"
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
          width="240"
        >
        </el-table-column>
        <el-table-column
          prop="point"
          label="积分"
          align="center"
          width="80"
        >
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注"
          align="center"
          :formatter="checkScoreComent"
        >
        </el-table-column>
        <el-table-column
          width="130"
          align="center"
          label="详情"
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
      class="showInfo_dialog"
      :title="title_show"
      :visible.sync="dialogVisible_show"
      width="520px"
      center>
      <el-form
        :label-position="labelPosition"
        label-width="80px"
        :model="emp"
        ref="empForm"
        style="margin-left: 20px"
      >
        <el-form-item label="论文名:">
          <span>{{ emp.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="学生姓名:">
          <span>{{ emp.student.sname }}</span
          ><br />
        </el-form-item>

        <el-form-item label="期刊页码:">
          <span>{{ emp.pubPage}}</span
          ><br />
        </el-form-item>
        <el-form-item label="论文状态:">
          <span>{{emp.state=="commit"
              ? "学生提交"
              :emp.state=="tea_pass"
                  ? "导师通过"
                  :emp.state=="tea_reject"
                      ? "导师驳回"
                      :emp.state=="adm_pass"
                          ? "管理员通过"
                          :"管理员驳回"}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者人数:">
          <span>{{emp.total}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者列表:">
          <span>{{emp.author}}</span
          ><br />
        </el-form-item>
        <el-form-item label="排名:">
          <span>{{emp.rank}}</span
          ><br />
        </el-form-item>
        <el-form-item label="发表年份:">
          <span>{{emp.year}}</span
          ><br />
        </el-form-item>
        <el-form-item label="发表月份:">
          <span>{{emp.month}}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:">
          &nbsp;&nbsp;&nbsp;&nbsp;
          <span v-if="emp.url == '' || emp.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer" @click="download(emp)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{emp.url|fileNameFilter}}</a>
          <br />
        </el-form-item>
        <div >
          <span>历史操作:</span>
          <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;font-size:5px;margin-left:5px">
              <div style="font-size: 10px;">
                <p>{{item.time | dataFormat}}&nbsp;&nbsp;&nbsp;{{item.operatorName}}&nbsp;&nbsp;&nbsp;{{item.operationName}}</p>
                <p v-show="item.remark == '' ? false : true">驳回理由：{{item.remark}}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>
        <span slot="footer" class="dialog-footer" :model="emp">
            <el-button
                  id="but_pass"
                  v-show="((emp.state=='commit' && role == 'teacher') || (emp.state=='tea_pass' && role == 'admin')) ? true:false"
                  @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                  type="primary"
                  >审核通过</el-button>
            <el-button
                  id="but_reject"
                  v-show="((emp.state=='commit' && role == 'teacher') || (emp.state=='tea_pass' && role == 'admin')) ? true:false"
                  @click="isShowInfo = true"
                  type="primary"
                  >审核不通过</el-button>
            <el-button
                  id="but_reject"
                  v-show="(emp.state=='tea_reject' || emp.state=='adm_reject' || emp.state == 'adm_pass' || (emp.state=='tea_pass' && role == 8))? true:false"
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
          <el-button @click=" (()=>{
            if (role == 'teacher')
              auditing_commit('tea_reject')
            else if (role == 'admin')
              auditing_commit('adm_reject')
            isShowInfo = false
          })"
                     type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import { set } from 'vue';
import axios from "axios";
import {debounce} from "@/utils/debounce";
export default {
  name: "SalSearch",
  data() {
    return {
      searchPaperPublicationName: '',
      tmp1:'',tmp2:'',tmp3:'', //假装绑定了v-model，让控制台不报错
      ispubFlag:false,
      ispubShow:false,
      select_pub_option:'',
      operList:[],
      isShowInfo:false,
      select_stuName:["全部"],//筛选框
      select_paperName:["全部"],
      select_point:['全部',1,3,4,6,9,12,15],
      select_pubName:[],
      option:["全部","学生提交","导师通过","管理员通过","导师驳回","管理员驳回"],
      labelPosition: "left",
      title: "",
      title_show: "",
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      copyemps:[],
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_pass: false,
      dialogVisible_reject: false,
      dialogVisible_show: false,
      total: 0,
      page: 1,
      size: 10,
      positions: [],
      reason:"",
      oper:{
        operatorRole: "",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '学术论文',
        operationName:"",
        state:"",
        remark:"",
        time: null,
        prodId: null,
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
        comment: "",
        state:"",
        student:{},
        total:0,
        rank:0
        // reason:"",
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
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
    menuHeight() {
      return this.select_pubName.length * 50 > 150
        ? 150 + 'px'
        : '${this.select_pubName.length * 50}px'
    },
    role() {
      return JSON.parse(localStorage.getItem('user')).role.indexOf('8') >= 0 ||
          JSON.parse(localStorage.getItem('user')).role.indexOf('9') >= 0 ? 'teacher' : 'admin';
    }
  },
  created() {
    this.debounceSearch = debounce(this.searchPublicationMethod,400);
  },
  mounted() {
    // this.initEmps();
    this.searchEmps(1, 3);
  },
  filters:{
    fileNameFilter:function(data){//将证明材料显示出来
      if(data == null || data == ''){
        return '无证明材料'
      }else{
        var arr= data.split('/')
        return  arr.reverse()[0]
      }
    }
  },
  watch:{
  },
  methods: {
    searchPublicationMethod(val) {
      let url = "/publication/basic/listByName?publicationName=" + val
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.select_pubName=[]
          if(resp.obj != null){
            for(var i=0;i<resp.obj.length;i++){
              this.select_pubName.push(
                  {
                    index:resp.obj[i].id,
                    value:resp.obj[i].name,
                  }
              )
            }
          }
        }
      });
    },
    selectOption() {

    },
    download(data){//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      var url = data.url
      axios({
        url: '/paper/basic/downloadByUrl?url='+url,
        method: 'GET',
        responseType: 'blob'
      }).then(response => {
        const url = window.URL.createObjectURL(new Blob([response]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      });
    },
    filter(val,options){
      document.getElementById(options).value=val
    },
    filter_pub(val){//选择期刊
      this.select_pub_option=val
      this.ispubFlag=false
      this.ispubShow=false
    },
    //点击对话框中的确定按钮 触发事件
    auditing_commit(status){
      this.loading = true;
      let url = "/paper/basic/edit_state?state=" + status + "&ID="+this.emp.id;
      this.dialogVisible_show = false
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emp.state = status
          this.total = resp.total;
          this.emp.pubid = this.emp.publicationID;
          this.emp.pubName = this.emp.publication.name;
          this.$message({
            type: 'success',
            message: '操作成功'
          })
          this.doAddOper(status, this.reason, this.emp.id);
        }
      }).finally(()=>{
        this.initEmps();
      });
    },
    doAddOper(state, remark, paperID) {
      this.oper.state = state;
      this.oper.remark = remark;
      this.oper.prodId = paperID;
      this.oper.time = this.dateFormatFunc(new Date());
      this.oper.operatorRole = this.role;
      if(this.oper.state == "tea_pass"){
        this.oper.operationName = "审核通过"
      }else if (this.oper.state == 'adm_pass'){
        this.oper.operationName = "审核通过"
      } else if (this.oper.state =="tea_reject"){
        this.oper.operationName = "审核驳回"
      } else{
        this.oper.operationName = "审核驳回"
      }
      this.postRequest1("/oper/basic/add", this.oper).then(
        (resp) => {
          if (resp) {
            this.initEmps()
          }
        }
      );    
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
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
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=学术论文').then((resp) => {
          this.loading = false;
          if (resp) {
            this.isShowInfo = false;
            this.operList = resp.obj;
            this.operList.sort(function(a,b){
            return a.time > b.time ? -1 : 1
            })
          }
      });
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
      let url = "/paper/basic/List" ;
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.copyemps = this.emps
          this.total = resp.total;
          //什么意思？
          for(var i = 0; i < this.emps.length; i ++){
            var papername = this.emps[i].name
            if(this.select_paperName.indexOf(papername)==-1){
              this.select_paperName.push(papername)
            }
            var judge=this.emps[i].student.sname
            if(this.select_stuName.indexOf(judge)==-1){
              this.select_stuName.push(judge)
            }
          }
        }
      });
    },
    searchEmps(pageNum, pageSize) {//根据条件搜索论文
      const params = {};
      params.studentName = document.getElementById("select_stuname").value
      var state = document.getElementById("select_state").value
      if(state == '导师通过'){
        state = 'tea_pass'
      }else if(state == '导师驳回'){
        state = 'tea_reject'
      }else if(state == '学生提交'){
        state = 'commit'
      }else if (state == '管理员通过'){
        state = 'adm_pass'
      }else if (state == '管理员驳回') {
        state = 'adm_reject'
      }else state = '';
      params.state = state;
      var pointFront = document.getElementById("select_point1").value
      var pointBack = document.getElementById("select_point2").value
      if(pointFront == '全部') {
        params.pointFront = '';
      }else {
        params.pointFront = pointFront;
      }
      if(pointBack == '全部') {
        params.pointBack = '';
      }else {
        params.pointBack = pointBack;
      }
      params.name = document.getElementById("select_paperName").value
      params.pub = this.searchPaperPublicationName;
      params.pageNum = pageNum.toString();
      params.pageSize = pageSize.toString();
      this.postRequest('/paper/basic/searchPaperByConditions', params).then((response) => {
        if(response) {
          this.emps = response.extend.res[0];
          this.totalCount = response.extend.res[1];
        }
      })
    },
    checkScoreComent(row){
      if (row.state === "adm_pass" && row.point === 2 && row.have_score === 0)
      {
        return (row.remark === null ? "" : row.remark+";") + "本类别论文只计算一篇，本论文积分不计入总分"
      }
      return row.remark
    },
  },
};
</script>

<style>
.showInfo_dialog .el-form-item{
  margin-bottom: 5px;
}
  .select_div_input{
    margin-left:3px;
    width:120px;
    height:32px;
    position:relative;
    display:inline-block
  }
  .select_div{
    border-radius: 3px;
    margin-top: 5px;
    font-size: 14px;
    position: absolute;
    background-color: #fff;
    z-index: 999;
    overflow: auto;
    width: 300px;
    cursor: pointer;
  }
  input[type=text]::placeholder {
            color:lightgrey;
        }
  input:focus{
    border:1px solid lightblue;
  }
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
div::-webkit-scrollbar { 
	/* 隐藏默认的滚动条 */
	-webkit-appearance: none;
}
div::-webkit-scrollbar:vertical { 
	/* 设置垂直滚动条宽度 */
	width: 6px;
}


/* 这里不需要用到这个 */
/* div::-webkit-scrollbar:horizontal{ */
	/* 设置水平滚动条厚度 */
	/* height: 2px; */
/* } */

div::-webkit-scrollbar-thumb { 
	/* 滚动条的其他样式定制，注意，这个一定也要定制，否则就是一个透明的滚动条 */
	border-radius: 8px; 
	border: 3px solid rgba(255,255,255,.4); 
	background-color: rgba(0, 0, 0, .5);
}
</style>
