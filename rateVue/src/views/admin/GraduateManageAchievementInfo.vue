<template>
  <div v-loading.fullscreen="pageLoadingStatus">
    <el-tabs v-model="tabsActivateName" @tab-click="tabChange">
      <el-tab-pane v-for="item in dynamicTabs" :key="item.name" :value="item.name" :label="item.label"></el-tab-pane>
    </el-tabs>
    <el-table :data="tabsTableData" v-loading="tabsTableLoading" v-show="dynamicTabs.length > 0" :header-cell-style="rowClass"
    >
      <el-table-column
          key="1"
          prop="student.name"
          align="center"
          label="学生姓名"
      >
      </el-table-column>
      <el-table-column key="2" prop="name" label="成果名称" align="center"></el-table-column>
      <el-table-column key="3" prop="state" label="成果状态" align="center">
        <template slot-scope="scope">
            <span
                style="padding: 4px"
                :style="scope.row.state=='tea_reject' || scope.row.state=='adm_reject' ? {'color':'red'}:{'color':'gray'}"
                size="mini"
            >
              {{
                scope.row.state == "commit"
                    ? "已提交"
                    : scope.row.state == "tea_pass"
                        ? "导师通过"
                        : scope.row.state == "tea_reject"
                            ? "导师驳回"
                            : scope.row.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}
              </span>
        </template>
      </el-table-column>
      <el-table-column
          key="4"
          v-if="dynamicTabs.length > 0 && dynamicTabs[parseInt(tabsActivateName)].name == 'paper' ? true : false"
          prop="pubName"
          label="发表刊物"
          align="center"
      >
      </el-table-column>
      <el-table-column
          key="5"
          label="积分"
          prop="point"
          align="center"
      >
        <template slot-scope="scope">
          <span>{{scope.row.have_score == 1 ? scope.row.point : 0}}</span>
          <span>/</span>
          <span>{{scope.row.point}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" align="center" key="6">
      </el-table-column>
      <el-table-column
          key="7"
          align="center"
          label="详情"
      >
        <template slot-scope="scope">
          <el-button
              @click="showDetailInfo(scope.row)"
              style="padding: 4px"
              size="mini"
          >查看详情</el-button
          >
          <el-button v-show="scope.row.state == 'adm_pass' && role == 'admin' ? true : false" @click="changePointMethod(scope.row)" style="padding: 4px"
                     size="mini">
            {{scope.row.changePointButton}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="dynamicTabs.length == 0">
      <span>该学生没有学术成果！</span>
    </div>
    <!--  驳回对话框-->
    <el-dialog v-model="currentAchievementOfEdit" :visible.sync="dialogOfReject">
      <el-input
          type="textarea"
          :rows="4"
          v-model="rejectReason"
          placeholder="请输入论文驳回理由"
      >
      </el-input>
      <span slot="footer">
          <el-button @click='rejectDialogConfirm' type="primary">确定</el-button>
          <el-button @click="dialogOfReject = false">取消</el-button>
        </span>
    </el-dialog>
    <!--  查看详情-->
    <el-dialog
        title="查看详情"
        :visible.sync="dialogVisibleOfDetailInfo"
        width="520px"
        center>
      <el-form
          :model="currentAchievementOfEdit"
          style="margin-left: 20px"
      >
        <el-form-item label="成果名称:">
          <span>{{ currentAchievementOfEdit.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="学生姓名:">
          <span>{{ currentAchievementOfEdit.student.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="期刊页码:" v-show="dynamicTabs.length > 0 && tabActivateOfIndexName == 'paper'">
          <span>{{ currentAchievementOfEdit.pubPage}}</span
          ><br />
        </el-form-item>
        <el-form-item label="成果状态:">
          <span>{{currentAchievementOfEdit.state=="commit"
              ? "学生提交"
              :currentAchievementOfEdit.state=="tea_pass"
                  ? "导师通过"
                  :currentAchievementOfEdit.state=="tea_reject"
                      ? "导师驳回"
                      :currentAchievementOfEdit.state=="adm_pass"
                          ? "管理员通过"
                          :"管理员驳回"}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者人数:">
          <span>{{currentAchievementOfEdit.total}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者列表:">
          <span>{{currentAchievementOfEdit.author}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者排名:">
          <span>{{currentAchievementOfEdit.rank}}</span
          ><br />
        </el-form-item>
        <el-form-item label="发表年月:">
          <span v-show="dynamicTabs.length > 0 && tabActivateOfIndexName != 'paper'">{{currentAchievementOfEdit.date}}</span>
          <span v-show="dynamicTabs.length > 0 && tabActivateOfIndexName == 'paper'">{{currentAchievementOfEdit.year}}-{{currentAchievementOfEdit.month}}</span>
          <br />
        </el-form-item>
        <el-form-item label="证明材料:">
          &nbsp;&nbsp;&nbsp;&nbsp;
          <span v-if="currentAchievementOfEdit.url == '' || currentAchievementOfEdit.url == null ? true:false" >无证明材料</span>
          <span v-else>{{ currentAchievementOfEdit.url | fileNameFilter }}</span>
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
                <p v-show="item.remark == '' ? false : true">驳回理由：{{item.remark}}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer" :model="currentAchievementOfEdit">
            <el-button
                id="but_pass"
                v-show="(currentAchievementOfEdit.state == 'commit' || (currentAchievementOfEdit.state == 'tea_pass' && role == 'admin')) ? true : false"
                @click="(()=>{
                  if (role == 'teacher')
                   examineMethod('tea_pass')
                  else if (role == 'admin')
                   examineMethod('adm_pass')
                }) "
                type="primary"
            >审核通过</el-button>
            <el-button
                id="but_reject"
                v-show="(currentAchievementOfEdit.state == 'commit' || (currentAchievementOfEdit.state == 'tea_pass' && role == 'admin')) ? true : false"
                @click="dialogOfReject = true"
                type="primary"
            >审核不通过</el-button>
            <el-button
                id="but_reject"
                v-show="(currentAchievementOfEdit.state=='tea_reject' || currentAchievementOfEdit.state=='adm_reject' || currentAchievementOfEdit.state == 'adm_pass' || (currentAchievementOfEdit.state=='tea_pass' && role == 8))? true:false"
                @click="dialogVisibleOfDetailInfo = false"
                type="primary"
            >关闭</el-button>
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
export default {
  name: "GraduateManageAchievementInfo",
  data() {
    return {
      isImage: false,
      isPdf: false,
      dialogPreviewPdfFile: false,
      previewImageSrcList: [],
      previewUrl: '',
      oper:{
        operatorRole: "",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '',
        operationName:"",
        state:"",
        remark:"",
        time: null,
        prodId: null,
      },
      operList: [],
      rejectReason: '', //绑定驳回理由
      dialogOfReject: false, //审核驳回对话框
      pageLoadingStatus: true,
      tabsActivateName: '0',
      tabsTableLoading: false,
      dialogVisibleOfDetailInfo: false,//查看详情
      tabsTableData: [],
      currentAchievementOfEdit: {
        name: '',
        student: {},
        state: '',
        have_score: '',
        point: '',
      },
      staticTabs: [
        {label: '学术论文', name: 'paper'},
        {label: "授权专利", name: "patent"},
        {label: "科研获奖", name: "award"},
        {label: "专著教材", name: "monograph"},
        {label: "学科竞赛", name: "competition"},
        {label: "纵向科研项目", name: "project"},
        {label: "横向科研项目", name: "horizontalProject"},
        {label: "决策咨询", name: "decision"},
        {label: "产品应用", name: "product"},
        {label: "制定标准", name: "standard"}
      ],
      dynamicTabs: []
    }
  },
  mounted() {
    this.initAllTypeAchievement();
  },
  computed: {
    role() {
      return JSON.parse(localStorage.getItem('user')).roleName == 'expert' || JSON.parse(localStorage.getItem('user')).roleName == 'expert;' ?
          'expert' : JSON.parse(localStorage.getItem('user')).roleName.indexOf('teacher') >= 0 ?
              'teacher' : JSON.parse(localStorage.getItem('user')).roleName.indexOf('admin') >= 0 ? 'admin' : '';
    },
    tabActivateOfIndexName() {
      return this.dynamicTabs[this.tabsActivateName].name;
    },
    tabActivateOfIndexLabel() {
      return this.dynamicTabs[this.tabsActivateName].label;
    }
  },
  methods: {
    previewMethod(type) {
      if(type == '1') {
        this.previewFileMethod(this.currentAchievementOfEdit).then(res => {
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
        this.downloadFileMethod(this.currentAchievementOfEdit);
      }
    },
     //输入驳回理由对话框中的确定按钮
    rejectDialogConfirm(){
      if (this.role == 'teacher')
        this.examineMethod('tea_reject')
      else if (this.role == 'admin')
        this.examineMethod('adm_reject')
      this.dialogOfReject = false
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
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
      let url = ''
      if(this.tabActivateOfIndexName == 'horizontalProject') {
        url = `/project/basic/editPoint/${data.id}`;
      } else {
        url = `/${this.tabActivateOfIndexName}/basic/editPoint/${data.id}`;
      }
      return this.postRequest(url, params).then();
    },
    changeStudentPoint(data, point) {
      const params = {
        studentID: data.studentId,
        point: data.point //传递需要进行加法或减法的数值
      }
      if(this.tabActivateOfIndexName == 'paper') { //论文书写不一样 额外判断一些
        params.studentID = data.studentID
      }
      if(point == 0) { //减法
        return this.postRequest('/graduatestudentM/basic/updateScoreSub', params).then()
      } else { //加法
        return this.postRequest('/graduatestudentM/basic/updateScore', params).then()
      }
    },
    //点击对话框中的确定按钮 触发事件
    examineMethod(status){
      if(this.role == 'admin' && (status.indexOf('pass') >= 0 || status.indexOf('reject') >= 0) && this.currentAchievementOfEdit.state == 'commit') { //管理员通过 有提示
        this.$confirm('目前导师尚未审核，是否确认审核该成果？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.rolePass(status);
        }).catch(() => {
          console.log()
        });
      }else this.rolePass(status);
    },
    rolePass(status) {
      let url = ''
      if(this.tabActivateOfIndexName == 'horizontalProject') {
        url = `/project/basic/edit_state?state=${status}&ID=${this.currentAchievementOfEdit.id}`;
      } else {
        url = `/${this.tabActivateOfIndexName}/basic/edit_state?state=${status}&ID=${this.currentAchievementOfEdit.id}`;
      }
      this.dialogVisibleOfDetailInfo = false
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.currentAchievementOfEdit.state = status
          this.$message({
            type: 'success',
            message: '操作成功'
          })
          this.currentAchievementOfEdit.remark = this.rejectReason;
          this.doAddOper(status, this.rejectReason, this.currentAchievementOfEdit.id);
          let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
          this.$store.dispatch('changePendingMessageange', roleParam);
        }
      })
    },
    doAddOper(state, remark, paperID) {
      this.oper.state = state;
      this.oper.remark = remark;
      this.oper.prodId = paperID;
      this.oper.time = this.dateFormatFunc(new Date());
      this.oper.operatorRole = this.role;
      this.oper.prodType = this.tabActivateOfIndexLabel
      if(this.oper.state == "tea_pass"){
        this.oper.operationName = "审核通过"
      }else if (this.oper.state == 'adm_pass'){
        this.oper.operationName = "审核通过"
      } else if (this.oper.state =="tea_reject"){
        this.oper.operationName = "审核驳回"
      } else{
        this.oper.operationName = "审核驳回"
      }
      this.postRequest1("/oper/basic/add", this.oper).then();
    },
    initAllTypeAchievement() { //获取该学生所有类型的成果，形成动态tab
      this.pageLoadingStatus = true;
      this.getRequest(`/student/basic/getAllAchievement?studentId=${this.$route.query.studentId}`).then(response => {
        if(response.extend) {
          for(let i in response.extend) {
            for(let j in this.staticTabs) {
              if(response.extend[i].length > 0 && i == this.staticTabs[j].name) {
                this.dynamicTabs.push(this.staticTabs[j])
              }
            }
          }
          if(this.dynamicTabs.length > 0) {
            this.tabsActivateName = '0'
            this.initTabData(response.extend[this.dynamicTabs[0].name])
          }
          this.pageLoadingStatus = false;
        }
      })
    },
    tabChange(tab, event) {
      this.getTableDataMethod(this.dynamicTabs[tab.index].name);
    },
    getTableDataMethod(data) {
      let url = '';
      if(data != 'horizontalProject') {
        url = `/${data}/basic/studentID?studentID=${this.$route.query.studentId}`;
      } else {
        url = `/project/basic/studentID/horizontal?studentID=${this.$route.query.studentId}`;
      }
      this.tabsTableLoading = true;
      this.getRequest(url).then(response => {
        if(response) {
          this.initTabData(response.data)
          this.tabsTableLoading = false;
        }
      })
    },
    initTabData(data) {
      this.tabsTableData = data;
      this.tabsTableData.map(item => {
        if(item.have_score == 1) {
          this.$set(item, 'changePointButton', '取消积分')
        } else {
          this.$set(item, 'changePointButton', '计入积分')
        }
        item.remark = item.hasOwnProperty('operationList') == true ? item.operationList[0].remark : item.paperoperList[0].remark
      })
    },
    showDetailInfo(data) {
      this.currentAchievementOfEdit = data;
      this.dialogVisibleOfDetailInfo = true;
      this.isPdf = this.isImage = false;
      this.previewUrl = '';
      this.previewImageSrcList = [];
      if(data.url.includes('.pdf')) { //判断文件类型
        this.isPdf = true;
      } else if(data.url.includes('.jpg') || data.url.includes('.png') || data.url.includes('.jpe') || data.url.includes('.JPG') || data.url.includes('.PNG') || data.url.includes('.JPE')) {
        this.isImage = true;
      }
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=' + this.tabActivateOfIndexLabel).then((resp) => {
        if (resp) {
          this.dialogOfReject = false;
          this.operList = resp.obj;
          this.operList.sort(function(a,b){
            return a.time > b.time ? -1 : 1
          })
        }
      });
    },
  }
}
</script>

<style scoped>

</style>