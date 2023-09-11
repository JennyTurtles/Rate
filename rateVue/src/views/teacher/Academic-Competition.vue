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
               v-model="searchStudentName"
               placeholder="学生姓名"
               autocomplete="off">
        <label style="fontSize:10px;margin-left:16px">竞赛名称：</label>
        <input type="text"
               style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
               placeholder="竞赛名称"
               v-model="searchCompetitionName"
               id="select_paperName">
<!--        <label style="fontSize:10px;margin-left:16px">竞赛类别：</label>-->
<!--        <div class="select_div_input">-->
<!--          <el-select-->
<!--              v-model="searchCompetitionTypeName"-->
<!--              filterable-->
<!--              remote-->
<!--              clearable-->
<!--              reserve-keyword-->
<!--              @change="selectOption($event)"-->
<!--              :remote-method="searchPublicationMethod"-->
<!--              placeholder="期刊名称"-->
<!--              :loading="loading">-->
<!--            <el-option-->
<!--                v-for="item in select_pubName"-->
<!--                :key="item.index"-->
<!--                :label="item.value"-->
<!--                :value="item.value">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </div>-->
        <label style="fontSize:10px;margin-left:40px;">成果状态：</label>
        <el-select
            v-model="searchStatus"
            style="margin-left:3px;width:120px"
            prefix-icon="el-icon-edit"
            clearable
            filterable
            placeholder="状态筛选"
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
            v-model="searchPointFront"
            style="margin-left:3px;width:60px"
            prefix-icon="el-icon-edit"
            clearable
            filterable
            placeholder="1"
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
            v-model="searchPointBack"
            style="margin-left:3px;width:60px"
            prefix-icon="el-icon-edit"
            clearable
            filterable
            placeholder="12"
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
            @click="searchCompetition(1, 10)"
            style="margin-left:30px"
        >
          搜索
        </el-button>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="competitionList"
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
            fixed
            prop="author"
            align="center"
            label="获奖人"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            fixed
            prop="name"
            align="center"
            label="竞赛名称"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="state"
            label="状态"
            min-width="10%"
            align="center"
        >
          <template slot-scope="scope">
            <span
                style="padding: 4px"
                size="mini"
                :model="currentCompetition.state"
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
            prop="competitionType.name"
            label="竞赛类别"
            align="center"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="point"
            label="积分"
            align="center"
            min-width="8%"
        >
        </el-table-column>
        <el-table-column
            prop="operationList[0].remark"
            label="备注"
            min-width="15%"
            align="center"
        >
        </el-table-column>
        <el-table-column
            width="130"
            align="center"
            label="详情"
            min-width="15%"
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
            :current-page="currentPage"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="totalCount"
            :page-sizes="pageSizes"
            :page-size="pageSize"
        >
        </el-pagination>
      </div>
    </div>

    <!-- 对话框 老师审核通过竞赛 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisiblePass" width="30%" center>
      <!-- 确定审核通过该学生竞赛？ -->
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="currentCompetition"
          ref="empForm"
          style="margin-left: 60px"
      >
        <el-form-item label="竞赛ID:" prop="id">
          <span>{{ currentCompetition.id }}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="dialogVisiblePass = false">取 消</el-button> -->
        <el-button type="primary" @click="auditing_commit('tea_pass')">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 对话框 老师驳回该学生竞赛 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisibleReject" width="30%" center>

      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="currentCompetition"
          ref="empForm"
          style="margin-left: 40px"
      >
        <el-form-item label="竞赛ID:" prop="id">
          <span>{{ currentCompetition.id }}</span>
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
        <!-- <el-button @click="dialogVisiblePass = false">取 消</el-button> -->
        <el-button type="primary" @click="auditing_commit('tea_reject')">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 查看详情按钮 -->
    <el-dialog
        class="showInfo_dialog"
        :title="titleName"
        :visible.sync="dialogVisible_show"
        width="520px"
        center>
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="currentCompetition"
          ref="empForm"
          style="margin-left: 20px"
      >
        <el-form-item label="竞赛名称:">
          <span>{{ currentCompetition.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="获奖人:">
          <span>{{ currentCompetition.author }}</span
          ><br />
        </el-form-item>
        <el-form-item label="成果状态:">
          <span>{{currentCompetition.state == "commit"
              ? "已提交"
              : currentCompetition.state == "tea_pass"
                  ? "导师通过"
                  : currentCompetition.state == "tea_reject"
                      ? "导师驳回"
                      : currentCompetition.state == "adm_pass"
                          ? "管理员通过"
                          : "管理员驳回"}}</span
          ><br />
        </el-form-item>
        <el-form-item label="获奖年月:">
          <span>{{currentCompetition.date}}</span
          ><br />
        </el-form-item>
        <el-form-item label="竞赛类别:">
          <span>{{currentCompetition.competitionType.name}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者人数:">
          <span>{{currentCompetition.total}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者排名:">
          <span>{{currentCompetition.rank}}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          &nbsp;&nbsp;&nbsp;&nbsp;
          <span v-if="currentCompetition.url == '' || currentCompetition.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer" @click="download(currentCompetition)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{currentCompetition.url | fileNameFilter}}</a>
          <br />
        </el-form-item>
        <div >
          <span>历史操作:</span>
          <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;font-size:5px;margin-left:5px">
              <div style="font-size: 10px;">
                <p>{{item.time | dataFormat}}&nbsp;&nbsp;&nbsp;{{item.operatorName}}&nbsp;&nbsp;&nbsp;{{item.operationName}}</p>
                <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{item.remark}}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer" :model="currentCompetition">
            <el-button
                id="but_pass"
                v-show="currentCompetition.state == 'commit' ? true : false"
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
                v-show="currentCompetition.state == 'commit' ? true : false"
                @click="rejectDialog"
                type="primary"
            >审核不通过</el-button>
            <el-button
                id="but_reject"
                v-show="(currentCompetition.state=='tea_reject' || currentCompetition.state=='adm_reject' || currentCompetition.state == 'adm_pass' || (currentCompetition.state=='tea_pass' && role == 8))? true:false"
                @click="dialogVisible_show = false"
                type="primary"
            >关闭</el-button>
        </span>
    </el-dialog>
    <el-dialog v-model="currentCompetition" :visible.sync="isShowInfo">
      <el-input
          type="textarea"
          :rows="4"
          v-model="reason"
          placeholder="请输入驳回理由"
      >
      </el-input>
      <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import { set } from 'vue';
import axios from "axios";
export default {
  name: "SalSearch",
  data() {
    return {
      searchCompetitionTypeName: '',
      searchPointFront: '',
      searchPointBack: '',
      searchCompetitionName: '',
      searchStatus: '导师通过',
      searchStudentName: '',
      pageSizes:[10, 20, 50, 100],
      totalCount:0,
      currentPage: 1,
      pageSize: 10,
      operList:[],
      isShowInfo: false,
      select_stuName:["全部"],//筛选框
      select_paperName: ["全部"],
      select_point: ['全部',1,3,4,6,9,12,15],
      option: ["全部","学生提交","导师通过","管理员通过","导师驳回","管理员驳回"],
      labelPosition: "left",
      title: "",
      titleName: "",
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisiblePass: false,
      dialogVisibleReject: false,
      dialogVisible_show: false,
      reason: "",
      oper:{
        operatorRole: "",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '学科竞赛',
        operationName:"",
        state:"",
        remark:"",
        time: null,
        prodId: null,
      },
      currentCompetition: {
        id: null,
        institutionID: null,
        name: null,
        scoreItemCount: "0",
        score: "",
        remark: "",
        state: "",
        student: {},
        total: '',
        rank: '',
        startDate: '',
        endDate: '',
        competitionType: {}
      },
      competitionList: []
    };
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
    role() {
      return JSON.parse(localStorage.getItem('user')).roleName.indexOf('teacher') >= 0 ||
      JSON.parse(localStorage.getItem('user')).roleName.indexOf('expert') >= 0 ? 'teacher' : 'admin';
    }
  },
  created() {},
  mounted() {
    this.searchCompetition(1,10);
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
  methods: {
    rejectDialog() {
      if(this.role == 'admin' && this.currentCompetition.state == 'commit') { //管理员驳回 有提示
        this.$confirm('目前导师尚未审核，是否确认审核驳回？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.isShowInfo = true;
        }).catch(() => {});
      }else this.isShowInfo = false;
    },
    rejectDialogConfirm(){
      if (this.role == 'teacher')
        this.auditing_commit('tea_reject')
      else if (this.role == 'admin')
        this.auditing_commit('adm_reject')
      this.isShowInfo = false
    },
    download(data){//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      var url = data.url
      axios({
        url: '/competition/basic/downloadByUrl?url='+url,
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
    //点击对话框中的确定按钮 触发事件
    auditing_commit(state){
      this.loading = true;
      if(this.role == 'admin' && state.indexOf('pass') >= 0) { //管理员通过 有提示
        this.$confirm('目前导师尚未审核，是否确认审核通过？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.rolePass(state);
        }).catch(() => {});
      }else this.rolePass(state);
    },
    rolePass(state) {
      let url = "/competition/basic/edit_state?state=" + state + "&ID=" + this.currentCompetition.id;
      this.dialogVisible_show = false
      if(state.indexOf('reject') >= 0){
        this.currentCompetition.operationList[0].remark = this.reason;
      }
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.currentCompetition.state = state
          this.$message({
            type: 'success',
            message: '操作成功'
          })
          this.doAddOper(state, this.reason, this.currentCompetition.id);
        }
      })
    },
    doAddOper(state,remark,competitionID) {
      this.oper.state = state;
      this.oper.remark = remark;
      this.oper.prodId = competitionID;
      this.oper.time = this.dateFormatFunc(new Date());
      this.oper.operatorRole = this.role;
      if(this.oper.state == "tea_pass" || this.oper.state == 'adm_pass'){
        this.oper.operationName = "审核通过"
      } else if (this.oper.state =="tea_reject" || this.oper.state == 'adm_reject'){
        this.oper.operationName = "审核驳回"
      }
      this.postRequest1("/oper/basic/add", this.oper);
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    showEditEmpView_show(data) {
      this.loading = true;
      this.titleName = "显示详情";
      this.currentCompetition = data;
      this.dialogVisible_show = true;
      this.getOperationListOfCompetition(data);
    },
    //获取改专著的操作列表
    getOperationListOfCompetition(data) {
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=学科竞赛').then((resp) => {
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
    //应该要分是否有无筛选条件
    sizeChange(currentSize) {
      this.pageSize = currentSize;
      this.searchCompetition(this.currentPage,currentSize);
    },
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.searchCompetition(currentPage,this.pageSize);
    },
    searchCompetition(pageNum, pageSize) {//根据条件搜索
      this.loading = true;
      const params = {};
      params.studentName = this.searchStudentName;
      var state = this.searchStatus;
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
      if(this.searchPointFront == '全部') {
        params.pointFront = '';
      }else {
        params.pointFront = this.searchPointFront;
      }
      if(this.searchPointBack == '全部') {
        params.pointBack = '';
      }else {
        params.pointBack = this.searchPointBack;
      }
      params.state = state;
      params.name = this.searchCompetitionName;
      params.pageNum = pageNum.toString();
      params.pageSize = pageSize.toString();
      this.postRequest('/competition/basic/searchCompetitionByConditions', params).then((response) => {
        if(response) {
          this.competitionList = response.extend.res[0];
          this.totalCount = response.extend.res[1];
          this.loading = false;
        }else this.competitionList = [];
      })
    }
  }
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

div::-webkit-scrollbar-thumb {
  /* 滚动条的其他样式定制，注意，这个一定也要定制，否则就是一个透明的滚动条 */
  border-radius: 8px;
  border: 3px solid rgba(255,255,255,.4);
  background-color: rgba(0, 0, 0, .5);
}
</style>
