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
               placeholder="学生姓名"
               autocomplete="off"
               v-model="searchStudentName"
               id="select_stuname">
        <label style="fontSize:10px;margin-left:16px">标准名称：</label>
        <input type="text"
               style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
               placeholder="标准名称"
               v-model="searchStandardName"
               id="select_paperName">

        <label style="fontSize:10px;margin-left:40px;">标准状态：</label>
        <el-select
            v-model="searchStandardState"
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
            v-model="pointFront"
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
            v-model="pointBack"
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
            @click="searchStandardListByCondicitions(1, 10)"
            :disabled="showAdvanceSearchView"
            style="margin-left:30px"
        >
          搜索
        </el-button>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="standards"
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
            prop="student.name"
            align="center"
            label="学生姓名"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="name"
            align="center"
            label="标准名称"
            min-width="15%"
        >
        </el-table-column>
        <!-- width="200" -->
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
            prop="indicator.name"
            label="标准类别"
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

    <!-- 对话框 老师审核通过标准 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisible_pass" width="30%" center>
      <!-- 确定审核通过该学生标准？ -->
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          ref="empForm"
          style="margin-left: 60px"
      >
        <el-form-item label="标准ID:" prop="id">
          <span>{{ emp.id }}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="dialogVisible_pass = false">取 消</el-button> -->
        <el-button type="primary" @click="auditing_commit('tea_pass')">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 对话框 老师驳回该学生标准 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisible_reject" width="30%" center>

      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          ref="empForm"
          style="margin-left: 40px"
      >
        <el-form-item label="标准ID:" prop="id">
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
        <el-form-item label="标准名称:">
          <span>{{ emp.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="学生姓名:">
          <span>{{ emp.student.name }}</span
          ><br />
        </el-form-item>


        <el-form-item label="标准状态:">
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
        <el-form-item label="排名:">
          <span>{{emp.rank}}</span
          ><br />
        </el-form-item>
        <el-form-item label="制定年月:">
          <span>{{emp.date}}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          &nbsp;&nbsp;&nbsp;&nbsp;
          <span v-if="emp.url == '' || emp.url == null ? true:false" >无证明材料</span>
          <span v-else>{{ emp.url | fileNameFilter }}</span>
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
      <span slot="footer" class="dialog-footer" :model="emp">
            <el-button
                v-show="(emp.state == 'commit' || (emp.state == 'tea_pass' && role == 'admin')) ? true : false"
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
                v-show="(emp.state == 'commit' || (emp.state == 'tea_pass' && role == 'admin')) ? true : false"
                @click="rejectDialog"
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
          placeholder="请输入标准驳回理由"
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
      pointBack: '',
      pointFront: '',
      searchStandardState: '',
      searchStandardName: '',
      searchStudentName: '',
      pageSizes:[10, 20, 50, 100],
      totalCount:0,
      currentPage:1,
      pageSize:10,
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
      standards: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_pass: false,
      dialogVisible_reject: false,
      dialogVisible_show: false,
      positions: [],
      reason:"",
      oper:{
        operatorRole: "",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '制定标准',
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
        state:"",
        student:{},
        total:0,
        rank:0
      },
    };
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
    menuHeight() {
      return this.select_pubName.length * 50 > 150
          ? 150 + 'px'
          : '${this.select_pubName.length * 50}px'
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
    if(this.role == 'teacher') this.searchStandardState = '学生提交';
    else if(this.role == 'admin') this.searchStandardState = '导师通过';
    this.searchStandardListByCondicitions(1, 10);
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
      return this.postRequest(`/standard/basic/editPoint/${data.id}`, params).then()
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
      if(this.role == 'admin' && this.emp.state == 'commit') { //管理员驳回 有提示
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
    download(data){//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      var url = data.url
      axios({
        url: '/standard/basic/downloadByUrl?url='+url,
        method: 'GET',
        responseType: 'blob',
        headers: {
          'token': this.user.token
        }
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
      if(this.role == 'admin' && (state.indexOf('pass') >= 0 || state.indexOf('reject') >= 0) && this.emp.state == 'commit') { //管理员通过 有提示
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
      let url = "/standard/basic/edit_state?state=" + state + "&ID="+this.emp.id;
      this.dialogVisible_show = false
      if(state.indexOf('reject') >= 0){
        this.emp.operationList[0].remark = this.reason;
      }
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emp.state = state
          this.$message({
            type: 'success',
            message: '操作成功'
          })
          this.doAddOper(state, this.reason, this.emp.id);
          let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
          this.$store.dispatch('changePendingMessageange', roleParam);
        }
      })
    },
    async doAddOper(state,remark,standardID) {
      this.oper.state = state;
      this.oper.remark = remark;
      this.oper.prodId = standardID;
      this.oper.time = this.dateFormatFunc(new Date());
      this.oper.operatorRole = this.role;
      if(this.oper.state == "tea_pass" || this.oper.state == 'adm_pass'){
        this.oper.operationName = "审核通过"
      } else if (this.oper.state =="tea_reject" || this.oper.state == 'adm_reject'){
        this.oper.operationName = "审核驳回"
      }
      await this.postRequest1("/oper/basic/add", this.oper);
      await this.searchStandardListByCondicitions(this.currentPage, this.pageSize)
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    showEditEmpView(data) {//修改论文
      this.title = "编辑单位信息";
      this.emp = data;
      this.dialogVisible = true;
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
      this.isPdf = this.isImage = false; //初始化
      this.previewUrl = '';
      this.previewImageSrcList = [];
      if(data.url.includes('.pdf')) { //判断文件类型
        this.isPdf = true;
      } else if(data.url.includes('.jpg') || data.url.includes('.png') || data.url.includes('.jpe')) {
        this.isImage = true;
      }
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=制定标准').then((resp) => {
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
      this.searchStandardListByCondicitions(this.currentPage, this.pageSize)
    },
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.searchStandardListByCondicitions(this.currentPage, this.pageSize)
    },
    searchStandardListByCondicitions(pageNum, pageSize) {//根据条件搜索论文
      const params = {};
      params.studentName = this.searchStudentName;
      var state = this.searchStandardState;
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
      if(this.pointFront == '全部') {
        params.pointFront = '';
      }else {
        params.pointFront = this.pointFront;
      }
      if(this.pointBack == '全部') {
        params.pointBack = '';
      }else {
        params.pointBack = this.pointBack;
      }
      params.state = state;
      params.name = this.searchStandardName;
      params.pageNum = pageNum.toString();
      params.pageSize = pageSize.toString();
      this.postRequest('/standard/basic/searchStandardByConditions', params).then((response) => {
        if(response) {
          this.standards = response.extend.res[0];
          this.standards.map(item => {
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
