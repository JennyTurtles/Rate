<template>
  <div>
    <div
        style="display: flex; justify-content: space-between; margin: 15px 0"
    >
      <div>
        <label>学生姓名：</label>
        <input type="text"
               style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
               v-model="searchStudentName"
               placeholder="学生姓名"
               autocomplete="off">
        <label style="margin-left:16px">项目名称：</label>
        <input type="text"
               style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
               placeholder="项目名称"
               v-model="searchProjectName"
               id="select_paperName">
        <label style="margin-left:40px;">项目状态：</label>
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
        <label style="margin-left:16px">积分范围：</label>
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
            @click="searchProject(1, 10)"
            style="margin-left:30px"
        >
          搜索
        </el-button>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="projectList"
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
            label="作者列表"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            fixed
            prop="name"
            align="center"
            label="项目名称"
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
                :model="currentProject.state"
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
            prop="projectType.name"
            label="项目类别"
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
          <template slot-scope="scope">
            <span>{{scope.row.have_score == 1 ? scope.row.point : 0}}</span>
            <span>/</span>
            <span>{{scope.row.point}}</span>
          </template>
        </el-table-column>
        <el-table-column
            min-width="15%"
            prop="operationList[0].remark"
            label="备注"
            align="center"
        >
        </el-table-column>
        <el-table-column
            min-width="15%"
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
            <el-button v-show="scope.row.state == 'adm_pass' ? true : false" @click="changePointMethod(scope.row)" style="padding: 4px"
                       size="mini">
              {{scope.row.changePointButton}}
            </el-button>
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

    <!-- 对话框 老师审核通过项目 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisiblePass" width="30%" center>
      <!-- 确定审核通过该学生项目？ -->
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="currentProject"
          ref="empForm"
          style="margin-left: 60px"
      >
        <el-form-item label="项目ID:" prop="id">
          <span>{{ currentProject.id }}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="dialogVisiblePass = false">取 消</el-button> -->
        <el-button type="primary" @click="auditing_commit('tea_pass')">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 对话框 老师驳回该学生项目 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisibleReject" width="30%" center>

      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="currentProject"
          ref="empForm"
          style="margin-left: 40px"
      >
        <el-form-item label="项目ID:" prop="id">
          <span>{{ currentProject.id }}</span>
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
          :model="currentProject"
          ref="empForm"
          style="margin-left: 20px"
      >
        <el-form-item label="项目名称:">
          <span>{{ currentProject.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="学生姓名:">
          <span>{{ currentProject.student.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="项目状态:">
          <span>{{currentProject.state=="commit"
              ? "学生提交"
              :currentProject.state=="tea_pass"
                  ? "导师通过"
                  :currentProject.state=="tea_reject"
                      ? "导师驳回"
                      :currentProject.state=="adm_pass"
                          ? "管理员通过"
                          :"管理员驳回"}}</span
          ><br />
        </el-form-item>
        <el-form-item label="立项年月:">
          <span>{{currentProject.startDate}}</span
          ><br />
        </el-form-item>
        <el-form-item label="结项年月:">
          <span>{{currentProject.endDate}}</span
          ><br />
        </el-form-item>
        <el-form-item label="项目类别:">
          <span>{{currentProject.projectType.name}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者列表:">
          <span>{{ currentProject.author }}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者人数:">
          <span>{{currentProject.total}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者排名:">
          <span>{{currentProject.rank}}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          &nbsp;&nbsp;&nbsp;&nbsp;
          <span v-if="currentProject.url == '' || currentProject.url == null ? true:false" >无证明材料</span>
          <span v-else>{{ currentProject.url | fileNameFilter }}</span>
        </el-form-item>
        <div v-show="currentProject.url == '' || currentProject.url == null ? false : true" style="margin-left: 80px">
          <div>
            <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
            <el-button @click="previewMethod('2')">下载</el-button>
          </div>
          <div style="margin-top: 5px">
            <el-image
                v-show="false"
                ref="previewImage"
                style="width: 100px; height: 100px"
                :src="previewUrl"
                :preview-src-list="previewImageSrcList">
            </el-image>
          </div>
          <br />
        </div>
        <div >
          <span>历史操作:</span>
          <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
              <div>
                <p>{{item.time | dataFormat}}&nbsp;&nbsp;&nbsp;{{item.operatorName}}&nbsp;&nbsp;&nbsp;{{item.operationName}}</p>
                <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{item.remark}}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer" :model="currentProject">
            <el-button
                id="but_pass"
                v-show="(currentProject.state == 'commit' || (currentProject.state == 'tea_pass' && role == 'admin')) ? true : false"
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
                v-show="(currentProject.state == 'commit' || (currentProject.state == 'tea_pass' && role == 'admin')) ? true : false"
                @click="rejectDialog"
                type="primary"
            >审核不通过</el-button>
            <el-button
                id="but_reject"
                v-show="(currentProject.state=='tea_reject' || currentProject.state=='adm_reject' || currentProject.state == 'adm_pass' || (currentProject.state=='tea_pass' && role == 8))? true:false"
                @click="dialogVisible_show = false"
                type="primary"
            >关闭</el-button>
        </span>
    </el-dialog>
    <el-dialog v-model="currentProject" :visible.sync="isShowInfo">
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
    <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
      <template v-if="isPdf">
        <vue-office-pdf
            :src="previewUrl"
            style="height: 100vh;"
        />
      </template>

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
      isImage: false,
      isPdf: false,
      dialogPreviewPdfFile: false,
      previewImageSrcList: [],
      previewUrl: '',
      searchPointFront: '',
      searchPointBack: '',
      searchProjectName: '',
      searchStatus: '',
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
        prodType: '纵向科研项目',
        operationName:"",
        state:"",
        remark:"",
        time: null,
        prodId: null,
      },
      currentProject: {
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
        projectType: {}
      },
      projectList: []
    };
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
    role() {
      // return JSON.parse(localStorage.getItem('user')).roleName.indexOf('teacher') >= 0 ||
      // JSON.parse(localStorage.getItem('user')).roleName.indexOf('expert') >= 0 ? 'teacher' : 'admin';
      return JSON.parse(localStorage.getItem('user')).roleName == 'expert' || JSON.parse(localStorage.getItem('user')).roleName == 'expert;' ?
          'expert' : JSON.parse(localStorage.getItem('user')).roleName.indexOf('teacher') >= 0 ?
              'teacher' : JSON.parse(localStorage.getItem('user')).roleName.indexOf('admin') >= 0 ? 'admin' : '';
    }
  },
  created() {},
  mounted() {
    if(this.role == 'teacher') this.searchStatus = '学生提交';
    else if(this.role == 'admin') this.searchStatus = '导师通过';
    this.searchProject(1,10);
  },
  methods: {
    previewMethod(type) {
      if(type == '1') {
        this.previewFileMethod(this.emp).then(res => {
          this.previewUrl = res;
          if(this.isImage) {
            this.previewImageSrcList = [res];
            this.$refs.previewImage.showViewer = true;
          }
          if(this.isPdf) {
            this.dialogPreviewPdfFile = true;
          }
        });
      } else {
        this.downloadFileMethod(this.emp);
      }
    },
    changePointMethod(data) { //修改积分按钮
      var have_score = data.have_score
      var point = data.point
      var score = have_score == 1 ? 0 : point
      this.$confirm(`确定将积分修改为${score}分?`,'提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        this.changePaperPoint(data, score).then(() => {
          this.changeStudentPoint(data, score).then(() => {
            this.$message.success('修改成功！')
            data.have_score = 1 - data.have_score;
            if(data.have_score == 1) data.changePointButton = '取消积分'
            else if(data.have_score == 0) data.changePointButton = '计入积分'
          });
        })
      }).catch(() => {
        data.have_score = have_score;
      })
    },
    changePaperPoint(data, point) {
      const params = {
        point: point,
        have_score: 1 - data.have_score
      }
      return this.postRequest(`/project/basic/editPoint/${data.id}`, params).then()
    },
    changeStudentPoint(data, point) {
      const params = {
        studentID: data.studentId,
        point: data.point //传递需要进行加法或减法的数值
      }
      if(point == 0) { //减法
        return this.postRequest('/graduatestudentM/basic/updateScoreSub', params).then()
      } else { //加法
        return this.postRequest('/graduatestudentM/basic/updateScore', params).then()
      }
    },
    rejectDialog() {
      if(this.role == 'admin' && this.currentProject.state == 'commit') { //管理员驳回 有提示
        this.$confirm('目前导师尚未审核，是否确认审核驳回？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.isShowInfo = true;
        }).catch(() => {});
      }else this.isShowInfo = true;
    },
    rejectDialogConfirm(){
      if (this.role == 'teacher')
        this.auditing_commit('tea_reject')
      else if (this.role == 'admin')
        this.auditing_commit('adm_reject')
      this.isShowInfo = false
    },
    //点击对话框中的确定按钮 触发事件
    auditing_commit(state){
      this.loading = true;
      if(this.role == 'admin' && (state.indexOf('pass') >= 0 || state.indexOf('reject') >= 0) && this.currentProject.state == 'commit') { //管理员通过 有提示
        this.$confirm('目前导师尚未审核，是否确认审核该成果？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.rolePass(state);
        }).catch(() => {
          this.loading = false;
        });
      }else this.rolePass(state);
    },
    rolePass(state) {
      let url = "/project/basic/edit_state?state=" + state + "&ID=" + this.currentProject.id;
      this.dialogVisible_show=false
      if(state.indexOf('reject') >= 0){
        this.currentProject.remark = this.reason;
      }
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.currentProject.state = state
          this.$message({
            type: 'success',
            message: '操作成功'
          })
          this.doAddOper(state, this.reason, this.currentProject.id);
          let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
          this.$store.dispatch('changePendingMessageange', roleParam);
        }
      })
    },
    async doAddOper(state,remark,projectID) {
      this.oper.state = state;
      this.oper.remark = remark;
      this.oper.prodId = projectID;
      this.oper.time = this.dateFormatFunc(new Date());
      this.oper.operatorRole = this.role;
      if(this.oper.state == "tea_pass" || this.oper.state == 'adm_pass'){
        this.oper.operationName = "审核通过"
      } else if (this.oper.state =="tea_reject" || this.oper.state == 'adm_reject'){
        this.oper.operationName = "审核驳回"
      }
      await this.postRequest1("/oper/basic/add", this.oper);
      await this.searchProject(this.currentPage, this.pageSize);
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    showEditEmpView_show(data) {
      this.loading = true;
      this.titleName = "显示详情";
      this.currentProject = data;
      this.dialogVisible_show = true;
      this.isPdf = this.isImage = false; //初始化
      this.previewUrl = '';
      this.previewImageSrcList = [];
      if(data.url.includes('.pdf')) { //判断文件类型
        this.isPdf = true;
      } else if(data.url.includes('.jpg') || data.url.includes('.png') || data.url.includes('.jpe') || data.url.includes('.JPG') || data.url.includes('.PNG') || data.url.includes('.JPE')) {
        this.isImage = true;
      }
      this.getOperationListOfProject(data);
    },
    //获取改专著的操作列表
    getOperationListOfProject(data) {
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=纵向科研项目').then((resp) => {
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
      this.searchProject(this.currentPage,currentSize);
    },
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.searchProject(currentPage,this.pageSize);
    },
    //先做个备份，可以删除
    setDataRemark(data) {
      //初始化页面需要根据学生提交时间做降序
      //页面的table的备注列需要展示驳回时间最晚的一条记录，两者操作无法合并
      let dataRejectList;
      let dataCommitList;
      data.map(item => {
        dataRejectList = [];
        dataCommitList = [];
        item.operationList.map(operation => {
          //将每个项目的提交和驳回单独提取
          if(operation.state === 'commit') dataCommitList.push(operation);
          if(operation.state === 'tea_reject' || operation.state === 'adm_reject') dataRejectList.push(operation);
        })
        //找出最晚驳回理由
        if(dataRejectList.length) {
          dataRejectList.sort((a,b) => {
            return b.time - a.time;
          })
          item.remark = dataRejectList[0].remark;
        }
      })
    },
    test(){
      this.getRequest('/project/basic/List').then((response) => {
        if(response) {
          this.projectList = response.extend.res;
          // this.totalCount = response.extend.res[1];
        }
      })
    },
    searchProject(pageNum, pageSize) {//根据条件搜索
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
      params.name = this.searchProjectName;
      params.pageNum = pageNum.toString();
      params.pageSize = pageSize.toString();
      this.postRequest('/project/basic/searchProjectByConditions', params).then((response) => {
        if(response) {
          this.projectList = response.extend.res[0];
          this.projectList.map(item => {
            if(item.have_score == 1) {
              this.$set(item, 'changePointButton', '取消积分')
            } else {
              this.$set(item, 'changePointButton', '计入积分')
            }
          })
          this.totalCount = response.extend.res[1];
        }else this.projectList = [];
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
