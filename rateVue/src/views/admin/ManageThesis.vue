<template>
  <div>
    <div>
      <label>选择毕业设计：</label>
      <el-select v-model="selectDate" placeholder="请选择" @change="handleSelectSemesterChange">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
    </div>

    <div style="margin-top: 10px">
      <el-button icon="el-icon-plus" type="success" style="margin-right: 10px" @click="importStudents">导入学生
      </el-button>

      <el-button icon="el-icon-upload" type="primary" style="margin-right: 10px" @click="groupsForStudent">学生分组
      </el-button>

      <el-button icon="el-icon-plus" type="success" style="margin-right: 10px" @click="importTeachers">导入指导教师
      </el-button>

      <el-button icon="el-icon-search" type="info" style="margin-right: 10px" @click="search"
                 :disabled="selectDate === ''">查询
      </el-button>

    </div>

    <el-dialog :visible.sync="dialogTeacherVisible" width="30%">
      <template v-slot:title>
        <div style="text-align: center; font-size: 20px;">导入指导教师信息</div>
      </template>
      <div style="margin-left: 10px">第一步：
        <el-button icon="el-icon-upload" type="primary" style="margin-right: 10px" @click="downloadExcel('teacher')">
          下载模版
        </el-button>
      </div>

      <div style="margin-top: 10px;margin-left: 10px">第二步：
        <el-upload
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            style="display: inline-flex; margin-left: 1px"
            :action="`/undergraduateM/basic/importThesis?type=teacher&institutionID=${user.institutionID}&year=${startYear}&semester=${selectSemester}`"
        >
          <el-button icon="el-icon-plus" type="success" :disabled="selectDate==''">导入教师</el-button>
        </el-upload>
      </div>
      <template #footer>
        <div>
          <el-button @click="dialogTeacherVisible = false">关闭</el-button>
        </div>
      </template>

    </el-dialog>

    <el-dialog :visible.sync="dialogStudentVisible" width="30%">
      <template v-slot:title>
        <div style="text-align: center; font-size: 20px;">导入学生信息</div>
      </template>
      <div style="margin-left: 10px">第一步：
        <el-button icon="el-icon-upload" type="primary" style="margin-right: 10px" @click="downloadExcel('thesis')">下载模版
        </el-button>
      </div>

      <div style="margin-top: 10px;margin-left: 10px">第二步：
        <el-upload
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            style="display: inline-flex; margin-left: 1px"
            :action="`/undergraduateM/basic/importThesis?type=student&institutionID=${user.institutionID}&year=${startYear}&semester=${selectSemester}`"
        >
          <el-button icon="el-icon-plus" type="success" :disabled="selectDate==''">导入学生</el-button>
        </el-upload>
      </div>
      <template #footer>
        <div>
          <el-button @click="dialogStudentVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="查询学生信息" :visible.sync="isSearchShow" center width="30%">
      <template>
        <el-form label-width="70px" label-position="left">
          <el-form-item label="学号">
            <el-input v-model="query.stuNumber"></el-input>
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="query.name"></el-input>
          </el-form-item>
          <el-form-item label="专业">
            <el-input v-model="query.specialty"></el-input>
          </el-form-item>
          <el-form-item label="班级">
            <el-input v-model="query.className"></el-input>
          </el-form-item>
          <el-form-item label="入学年份">
            <el-input v-model="query.year"></el-input>
          </el-form-item>
          <el-form-item label="教师工号">
            <el-input v-model="query.tutorJobNumber"></el-input>
          </el-form-item>
          <el-form-item label="教师姓名">
            <el-input v-model="query.tutorName"></el-input>
          </el-form-item>
        </el-form>

        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="initUnderGraduateStudents(1, pageSize)">确定</el-button>
          <el-button @click="closeDialogEdit">关闭</el-button>
        </span>

      </template>
    </el-dialog>

    <div style="margin-top: 10px">
      <el-table
          :data="undergraduateStudents">
        <el-table-column prop="stuNumber" label="学号" align="center" width="80px"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center" width="80px"></el-table-column>
        <el-table-column prop="group" label="组别" align="center" width="80px"></el-table-column>
        <el-table-column prop="specialty" label="专业" align="center" width="150px"></el-table-column>
        <el-table-column prop="className" label="班级" align="center" width="100px"></el-table-column>
        <el-table-column prop="telephone" label="手机号" align="center" width="150px"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center" width="150px"></el-table-column>
        <el-table-column prop="year" label="入学年份" align="center" width="80px"></el-table-column>
        <el-table-column prop="tutorJobNumber" label="教师工号" align="center" width="100px"></el-table-column>
        <el-table-column prop="tutorName" label="教师姓名" align="center" width="80px"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" plain @click="editDialogShow(scope.row)" type="primary" style="padding: 4px">编辑
            </el-button>
            <el-button size="mini" type="danger" plain @click="deleteUnder(scope.row)" style="padding: 4px">删除
            </el-button>
            <el-button size="mini" type="primary" plain @click="resetPasswordShow(scope.row)" style="padding: 4px">
              重置密码
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="编辑信息" :visible.sync="dialogEdit" center width="500px" @close="closeDialogEdit">
      <template>
        <el-form :model="currentUnderStudentOfEdit" label-width="100px" label-position="left">
          <el-form-item label="导师信息">
            <el-select
                v-model="selectTeacherNameOrJobnumber"
                value-key="id"
                filterable
                remote
                clearable
                reserve-keyword
                @change="selectOption($event)"
                placeholder="请输入指导教师的姓名或者工号"
                loading-text="搜索中..."
                :remote-method="selectTeacherMethod"
                :loading="searchTeacherLoading">
              <el-option
                  v-for="teacher in select_teachers"
                  :key="teacher.name + '(' + teacher.jobnumber + ')'"
                  :label="teacher.name + '(' + teacher.jobnumber + ')'"
                  :value="teacher.name + '(' + teacher.jobnumber + ')'+teacher.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="学生姓名">
            <el-input style="width: 80%" v-model="currentUnderStudentOfEdit.name"></el-input>
          </el-form-item>
          <el-form-item label="学生专业">
            <el-input style="width: 80%" v-model="currentUnderStudentOfEdit.specialty"></el-input>
          </el-form-item>
          <el-form-item label="学生班级">
            <el-input style="width: 80%" v-model="currentUnderStudentOfEdit.className"></el-input>
          </el-form-item>
          <el-form-item label="学生手机号">
            <el-input style="width: 80%" v-model="currentUnderStudentOfEdit.telephone"></el-input>
          </el-form-item>
          <el-form-item label="学生邮箱">
            <el-input style="width: 80%" v-model="currentUnderStudentOfEdit.email"></el-input>
          </el-form-item>
          <el-form-item label="学生入学年份">
            <el-input style="width: 80%" v-model="currentUnderStudentOfEdit.year"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editUnder" type="primary">确定</el-button>
          <el-button @click="closeDialogEdit">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog title="重置密码" :visible.sync="dialogResetPassword" center width="400px" @close="closeDialogReset">
      <el-form>
        <el-form-item label="请输入新密码:">
          <el-input style="width: 60%" v-model="newPassword"></el-input>
        </el-form-item>

        <div class="footer">
          <el-button @click="resetPassword" type="primary">确认</el-button>
          <el-button @click="closeDialogReset" type="primary">取消</el-button>
        </div>
      </el-form>
    </el-dialog>
    <div style="margin-top: 10px;text-align:right">
      <el-pagination @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="currentPage"
                     :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                     :total="totalCount"
                     :page-sizes="pageSizes"
                     background
      >
      </el-pagination>
    </div>

    <el-dialog :visible.sync="dialogImportResult" width="40%" title="导入学生结果统计" :center="true">
      <p>
        共计<span :style="{ color: 'blue', fontWeight: 'bold' }">{{ totalRows }}</span>条记录，
        成功插入<span :style="{ color: 'green', fontWeight: 'bold' }">{{ successfulRowsCount }}</span>条记录，
        成功更新<span :style="{ color: 'orange', fontWeight: 'bold' }">{{ duplicateInsertRowsCount }}</span>条记录，
        有<span :style="{ color: 'red', fontWeight: 'bold' }">{{ failedRowsCount }}</span>条记录插入或更新失败。
      </p>

      <p v-show="failedRowsCount>0">以下是失败原因的统计：</p>
      <ul>
        <li v-for="(reason, code) in failureReasons" :key="code">第{{ code }}行: {{ reason }}</li>
      </ul>

      <template #footer>
        <div>
          <el-button @click="dialogImportResult = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog :visible.sync="dialogStudentGroup" width="81%" title="学生分组-根据学生绩点分组" center
               @close="closeDialogGroup">
      <div>
        <div>
          <div style="margin-top: 15px;width: 100%">
            <template>
              <el-radio v-model="groupWay" label="专业" >待分组学生专业</el-radio>
              <el-radio v-model="groupWay" label="班级" >待分组学生班级</el-radio>
            </template>
          </div>
          <div style="margin-top: 10px">
            <template v-if="groupWay=='专业'">
              <span>请选择专业：</span>
              <el-select
                  style="margin-right: 20px;width: 250px;"
                  v-model="selectedSubGroupInfo"
                  multiple
              >
                <el-option
                    v-for="item in groupSubOfSelectedSpecialty"
                    :key="item"
                    :label="item + '（' + groupSpecialtyNums[item].length +'）人'"
                    :value="item">
                </el-option>
              </el-select>
            </template>
            <template v-if="groupWay=='班级'">
              <span>请选择班级：</span>
              <el-select
                  style="margin-right: 20px;width: 250px;"
                  v-model="selectedSubGroupInfo"
                  multiple
              >
                <el-option
                    v-for="item in groupSubOfSelectedClass"
                    :key="item"
                    :label="item + '（' + groupClassNums[item].length +'）人'"
                    :value="item">
                </el-option>
              </el-select>
            </template>
            <template>
              <span>请选择分组个数：</span>
              <el-select
                  style="margin-right: 20px;width: 150px;"
                  v-model="selectedGroupNums"
              >
                <el-option
                    v-for="item in groupNums"
                    :key="item"
                    :label="item"
                    :value="item">
                </el-option>
              </el-select>
            </template>
          </div>
          <div style="margin-top: 15px;width: 100%">
            <template>
              <el-radio v-model="radio" label="1">均分每组人数</el-radio>
              <el-radio v-model="radio" label="2">指定每组人数</el-radio>
            </template>
            <el-button @click="creatGroup" type="primary">
              创建分组
            </el-button>
            <el-button @click="closeDialogGroup" type="primary">
              &nbsp;关&nbsp;&nbsp;闭&nbsp;
            </el-button>
          </div>
          <div id="tableOfGroupNums"
               class="inputOfGroupsBox"
               v-show="selectedGroupNums > 0"
          >
            <span v-for="item in groupNumsInput" style="margin: auto;width: 100%;padding-bottom: 2px">
              第{{ item.idx + 1 }}组：
              <input v-model="item.value" style="width: 60px" :disabled="item.disabled"></input>
            </span>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";
import {debounce} from "@/utils/debounce";


export default {
  name: "SalStudentM",
  data() {
    return {
      isSearchShow: false,
      query: {
        stuNumber: '',
        name: '',
        specialty: '',
        className: '',
        year: null,
        tutorJobNumber: '',
        tutorName: '',
        institutionID: null,
        startYear: null,
        month: null,
        pageNum: null,
        pageSize: null,
      },
      searchTeacherLoading: false,
      duplicateInsertRowsCount: 0,
      failedRowsCount: 0,
      successfulRowsCount: 0,
      totalRows: 0,
      failureReasons: [],
      dialogImportResult: false,
      dialogTeacherVisible: false,
      dialogStudentVisible: false,
      dialogStudentGroup: false,
      selectedGroupNums: 0,
      groupNumsInput: [],//用于存放选择分为几组后的div和input框
      groupNums: null,//分数个数列表
      NoGroupPar: [],//未分组学生
      filterNoGroupPar: [],//筛选得到的未分组学生
      groupSubOfSelectedClass: [],//用于选择的不同班级
      groupSubOfSelectedSpecialty: [],//用于选择的不同专业
      selectedSubGroupInfo: [],//选择的子信息项
      groupClassNums: {}, //班级计数
      groupSpecialtyNums:{}, //专业计数
      radio: '1',
      groupWay:'专业',
      options: [],
      selectDate: '',
      canImportStudents: false,
      selectSemester: '',
      startYear: null,
      selectTeacherNameOrJobnumber: '',//编辑框中导师搜索一栏的下拉框绑定数据
      newPassword: '',//重置密码中的新密码
      conNewPassword: '',//重置密码中的确认新密码
      dialogResetPassword: false,
      pageSizes: [10, 20, 50, 100],
      totalCount: 0,
      currentPage: 1,
      pageSize: 20,
      isSelectYearFlag: false,
      isSelectYearShow: false,
      selectYearsList: [],
      isSelectFlag: false,
      isSelectShow: false,//搜索老师名字的搜索框
      yearTimer: null,
      timer: null,
      select_teachers: [],
      selectTeacherName: '',
      selectYear: '',
      currentUnderStudentOfEdit: {
        ID: null,
        name: '',
        specialty: '',
        className: '',
        year: null,
        studentID: null,
        telephone: '',
        email: '',
        tutorName: '',
        tutorID: null,
        tutorJobNumber: '',
        institutionID: null,
        tutorNameAndJobNumber: '',
        thesis: {}
      },
      dialogEdit: false,
      user: {},
      undergraduateStudents: []
    }
  },
  created() {
    this.debounceSearch = debounce(this.debounceSearchType, 400);
  },
  watch: {

    selectedGroupNums: {//监听用户选择了分为几组
      handler(val) {
        this.groupNumsInput = []
        for (var i = 0; i < val; i++) {
          this.groupNumsInput.push({
            idx: i,
            value: '',
            disabled: true//默认是均分
          })
        }
        if (this.radio == '2') {//如果是指定人数
          for (var item of this.groupNumsInput) {
            item.disabled = false
          }
        }
        this.calculationGroupInputValue()
      }
    },
    radio: {
      handler(val) {
        if (this.radio == '1') {//均分
          for (var i = 0; i < this.selectedGroupNums; i++) {
            this.groupNumsInput[i].disabled = true
          }
        } else if (this.radio == '2') {//指定每组人数
          for (var i = 0; i < this.selectedGroupNums; i++) {
            this.groupNumsInput[i].disabled = false
          }
        }
        this.calculationGroupInputValue()
      }
    },
    selectedSubGroupInfo: {//第一个下拉框的变化
      handler(val) {
        if (this.groupWay == '专业')
           this.filterNoGroupPar = this.NoGroupPar.filter(item => this.selectedSubGroupInfo.includes(item.specialty))
        if (this.groupWay == '班级')
          this.filterNoGroupPar = this.NoGroupPar.filter(item => this.selectedSubGroupInfo.includes(item.className))
        this.calculationGroupInputValue()
      }
    },
    groupWay:{
      handler(val){
        this.selectedSubGroupInfo=[]
      }
    }
  },

  computed: {
    menuHeight() {
      return this.selectTeacherName.length * 50 > 150
          ? 150 + 'px'
          : `${this.selectTeacherName.length * 50}px`
    },
    labelWidth() {
      return `${8 * 17}px`
    }
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.fetchThesisExistDate()
  },
  methods: {

    // 打开搜索框
    search() {
      this.isSearchShow = true;
    },
    //选择下拉框的某个选项
    selectOption(data) {
      if (data) {
        // 从 tutorNameAndJobNumber 中提取导师姓名和工号
        const tutorNameAndJobNumber = this.selectTeacherNameOrJobnumber;
        const separatorIndexleft = tutorNameAndJobNumber.lastIndexOf('(');
        const separatorIndexright = tutorNameAndJobNumber.lastIndexOf(')');

        if (separatorIndexleft !== -1) {
          const tutorName = tutorNameAndJobNumber.substring(0, separatorIndexleft).trim();  // 提取导师姓名
          const tutorJobNumber = tutorNameAndJobNumber.substring(separatorIndexleft + 1, separatorIndexright).trim();  // 提取导师工号
          const tutorID = tutorNameAndJobNumber.substring(separatorIndexright + 1, tutorNameAndJobNumber.length).trim();  // 提取导师工号


          // 分别赋值给 currentUnderStudentOfEdit 对象的属性
          this.currentUnderStudentOfEdit.tutorName = tutorName;
          this.currentUnderStudentOfEdit.tutorJobNumber = tutorJobNumber;
          this.currentUnderStudentOfEdit.tutorID = tutorID;
        } else {
          // 如果找不到分隔符，您可以在这里处理错误情况
          console.log("无法提取导师姓名和工号");
        }

      }
    },
    debounceSearchType(data) {
      if (data != null && data != '') {
        const url = `/system/teacher/searchByNameOrJobNumber?nameOrJobNumber=${encodeURIComponent(data)}&institutionID=${this.user.institutionID}`;
        this.getRequest(url).then(response => {
          if (response) {
            this.searchTeacherLoading = false;
            this.select_teachers = response.obj;
          } else {
            this.select_teachers = [];
          }
        })
      }
    },
    selectTeacherMethod(data) {
      this.searchTeacherLoading = true;
      this.debounceSearch(data);
    },
    importStudents() {
      this.dialogStudentVisible = true;
    },
    importTeachers() {
      this.dialogTeacherVisible = true;
    },

    async fetchThesisExistDate() {
      try {
        const url = `/undergraduateM/basic/getThesisExistDate?institutionID=${this.user.institutionID}`;
        const response = await this.getRequest(url);
        if (response.status === 200) {
          this.options = this.transformOptions(response.obj);
        } else {
          throw new Error("请求失败!");
        }
      } catch (error) {
        throw new Error("请求出现异常!");
      }
    },

    transformOptions(options) {
      return options.map(option => {
        let year = option.substring(0, 4);
        let season = option.slice(-2) === '春季' ? 3 : 9;
        let optionValue = year + season

        return {value: optionValue, label: option};
      });
    },

    handleSelectSemesterChange() {
      this.startYear = parseInt(this.selectDate.substring(0, 4));
      this.selectSemester = parseInt(this.selectDate.charAt(4));
      this.initUnderGraduateStudents(1, 10);
    },

    // !编辑框中 搜索老师姓名之后点击下拉框的某个选项
    filterEditTeacher(val) {
      this.currentUnderStudentOfEdit.teachers.name = val.split(":")[1]
      this.currentUnderStudentOfEdit.teachers.jobnumber = val.split(":")[0]
      this.isSelectShow = false
      this.isSelectFlag = false
    },
    closeDialogReset() {
      this.dialogResetPassword = false
    },
    resetPassword() {//重制密码
      if (this.newPassword == '' || this.newPassword == null) {
        this.$message.warning('请输入密码！')
        return
      }
      this.currentUnderStudentOfEdit.password = this.newPassword
      this.postRequest('/undergraduateM/basic/resetUnderPassword', this.currentUnderStudentOfEdit).then((response) => {
        if (response) {
          if (response.status == 200) {
            this.$message.success("重置成功")
            this.closeDialogReset()
          } else {
            this.$message.fail("重置失败")
          }
        }
      })
    },
    resetPasswordShow(data) {//重制密码
      this.currentUnderStudentOfEdit = data
      this.dialogResetPassword = true
    },
    inputSelectYearFocus() {//年份输入框获得焦点
      this.isSelectYearShow = true
    },
    filter_year(val) {//点击年份下拉框的某个选项
      this.selectYear = val
      this.isSelectYearShow = false
    },
    filterBtn() {//点击筛选按钮
      let tempYear = this.selectYear
      if (this.selectYear == '') {
        tempYear = 0
      }
      let url = '/undergraduateM/basic/getUnderStudentsBySelect?year=' + parseInt(tempYear) + '&teaName=' + this.selectTeacherName +
          '&pageNum=' + this.currentPage + '&pageSize=' + this.pageSize
      this.getRequest(url).then((resp) => {
        if (resp) {
          if (resp.status == 200) {
            this.undergraduateStudents = resp.obj[0]
            this.totalCount = resp.obj[1]
          }
        }
      })
    },
    filter_teas(val) {//点击某个筛选出来的名字
      this.selectTeacherName = val
      this.isSelectShow = false
      this.isSelectFlag = false
    },
    inputSelectTeacerNameFocus() {//input获取焦点判断是否有下拉框，是否可输入
      this.isSelectShow = true//控制下拉框是否显示
    },
    closeDialogEdit() {//关闭对话框
      this.dialogEdit = false
      this.isSearchShow = false
    },
    editDialogShow(data) {//控制变量
      this.dialogEdit = true
      this.currentUnderStudentOfEdit = JSON.parse(JSON.stringify(data));
      this.selectTeacherNameOrJobnumber = this.currentUnderStudentOfEdit.tutorName == null ? '' : this.currentUnderStudentOfEdit.tutorName + '(' + this.currentUnderStudentOfEdit.tutorJobNumber + ')'
    },
    checkValue(value, message) {
      if (value === '' || value === null) {
        this.$message.warning(message);
        return false;
      }
      return true;
    },
    async editUnder() {
      try {
        const {tutorName, tutorJobNumber} = this.currentUnderStudentOfEdit;

        if (!this.checkValue(tutorName, '请填写老师姓名！') || !this.checkValue(tutorJobNumber, '请填写老师工号！')) {
          return;
        }

        const data = {
          ...this.currentUnderStudentOfEdit,
          thesis: {
            studentID: this.currentUnderStudentOfEdit.studentID,
            year: this.startYear,
            month: this.selectSemester,
            tutorID: this.currentUnderStudentOfEdit.tutorID
          }
        };

        const resp = await this.putRequest('/undergraduateM/basic/updateUndergraduateBaseOnTeacher', data);

        if (resp && resp.status === 200) {
          this.dialogEdit = false;
          this.$message.success("修改成功！");
          await this.initUnderGraduateStudents(this.currentPage, this.pageSize);
        } else {
          this.$message.error("修改失败！");
        }
      } catch (error) {
        console.error("编辑出错：", error);
        // 进一步处理错误，例如显示错误消息
      }
    },

    deleteUnder(data) {
      this.$confirm('确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const queryParams = {
          studentID: data.studentID,
          tutorID: data.tutorID || "none", // 处理可能为空的情况
          year: this.startYear,
          month: this.selectSemester
        };
        const queryParamsStr = Object.entries(queryParams)
            .map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
            .join('&');

        const url = `/undergraduateM/basic/deleteThesis?${queryParamsStr}`;

        this.postRequest(url).then(async (resp) => {
          if (resp.status === 200) {
            this.$message.success('删除成功');
            await this.initUnderGraduateStudents(this.currentPage, this.pageSize);
          } else {
            this.$message.warning('删除失败！');
          }
        });
      });
    },
    onSuccess(res) {
      const {status, msg, obj} = res;

      if (status === 200) {
        this.handleSuccessfulImport(obj);
      } else {
        this.$message.error(msg)
      }
    },
    handleSuccessfulImport(obj) {
      const {
        duplicateInsertRowsCount,
        failedRowsCount,
        failureReasons,
        successfulRowsCount,
        total,
      } = obj;

      this.$message.success("导入成功");
      this.dialogTeacherVisible = false
      this.dialogStudentVisible = false
      this.initUnderGraduateStudents(1, this.pageSize);
      this.duplicateInsertRowsCount = duplicateInsertRowsCount;
      this.failedRowsCount = failedRowsCount;
      this.failureReasons = failureReasons;
      this.successfulRowsCount = successfulRowsCount;
      this.totalRows = total;
      this.dialogImportResult = true;
    },
    beforeUpload() {//上传前
      this.$message.success("正在导入")
    },

    downloadExcel(val) {
      let url = `/undergraduateM/basic/exportUnderGraduate?type=` + val
      this.$message.success('正在下载')
      window.open(url, '_parent')
    },
    initSelectYearsList() {//初始化筛选框中的年份数据
      let timeDate = new Date()
      let temp1 = Array.from(Array(timeDate.getFullYear() - 20).keys(), n => n + 1)
      let temp2 = Array.from(Array(timeDate.getFullYear()).keys(), n => n + 1)
      this.selectYearsList = temp2.filter(item1 => !temp1.some(item2 => item2 === item1))//去掉两者相同的，留下不同的
      this.selectYearsList.sort((a, b) => {
        return b - a;
      })
    },
    handleSizeChange(val) {
      // 改变每页显示的条数
      this.pageSize = val
      // 注意：在改变每页显示的条数时，要将页码显示到第一页
      this.currentPage = 1
      if ((this.selectYear == '' || this.selectYear == null) && (this.selectTeacherName == '' || this.selectTeacherName == null)) {
        this.initUnderGraduateStudents(this.currentPage, this.pageSize)
      } else {//筛选条件不为空
        this.filterBtn()
      }
    },
    // 显示第几页
    handleCurrentChange(val) {
      // 改变默认的页数
      this.currentPage = val;
      if ((this.selectYear == '' || this.selectYear == null) && (this.selectTeacherName == '' || this.selectTeacherName == null)) {
        this.initUnderGraduateStudents(this.currentPage, this.pageSize)
      } else {//筛选条件不为空
        this.filterBtn()
      }
    },
    async initUnderGraduateStudents(pageNum, pageSize) {
      try {
        this.query.pageNum = pageNum;
        this.query.pageSize = pageSize;
        this.query.institutionID = this.user.institutionID;
        this.query.startYear = this.startYear;
        this.query.month = this.selectSemester;

        const url = `/undergraduateM/basic/getStudentsByConditions`
        const response = await this.postRequest(url, this.query);
        const {code, extend} = response;
        if (code === 200) {

          const [students, totalCount] = extend.res;
          this.undergraduateStudents = students;
          this.totalCount = totalCount;
          this.isSearchShow = false;
          if (this.totalCount == 0) {
            // this.$message.info("本学期没有导入毕业论文信息！")
            this.undergraduateStudents = []
          }
          this.isSearchShow = false
        } else {
          // 处理请求失败的情况
          this.undergraduateStudents = []
          this.$message.error("请求失败!")

        }
      } catch (error) {
        this.$message.error("请求出现异常！");
      }
    },
    calculationGroupInputValue() {//计算input框的值
      if (this.filterNoGroupPar.length == 0 || this.selectedGroupNums == 0) {//如果没有选择分组个数或没有待分组的选手直接返回
        return
      }
      if (this.selectedGroupNums > this.filterNoGroupPar.length) {
        this.$message.warning('分组数不能超过未分组人数！')
        return
      }
      var participantNums = this.filterNoGroupPar.length//选手人数
      var groupNums = this.selectedGroupNums//组数
      if (this.selectedGroupNums != null) {//均分
        var baseNums = Math.floor(participantNums / groupNums);
        for (var i = 0; i < groupNums; i++) {
          this.groupNumsInput[i].value = baseNums;
        }
        for (var i = 0; i < (participantNums - baseNums * groupNums); i++) {
          this.groupNumsInput[i].value++;
        }
      }
    },
    closeDialogGroup() {//选手分组对话框关闭,清空遗留数据
      this.dialogStudentGroup = false;
      this.selectedSubGroupInfo = []
      this.selectedGroupNums = 0
      this.filterNoGroupPar = []
      this.groupSubOfSelectedClass = []
      this.groupSubOfSelectedSpecialty = []
      this.groupWay='专业'
      this.groupNumsInput = []
    },
    creatGroup() {//创建分组
      var sum = 0
      if (this.radio == '2') {
        this.groupNumsInput.map(item => {
          sum += parseInt(item.value)
        })
        if (sum != this.filterNoGroupPar.length) {
          this.$message.error('待创建分组人数为' + this.filterNoGroupPar.length + '，分组总人数为' + sum + '，人数不相等！')
          return
        }
      }
      if (this.selectedGroupNums==0){
        this.$message.error('请选择分组数！')
        return
      }
      var url = '/undergraduateM/basic/createGroups'
      //每组人数
      var arr = this.groupNumsInput.map(item => {
        return parseInt(item.value)
      })
      var data = {
        'year': this.startYear,
        'month': this.selectSemester,
        'arr': arr,
        'exchangeNums': Math.ceil(this.selectedGroupNums / 2),
        'groupsNums': this.selectedGroupNums,
        'selectInfo': this.selectedSubGroupInfo,
        'groupWay': this.groupWay
      }
      console.log(data);
      for (var i = 0;i < this.filterNoGroupPar.length; i++){
        if (this.filterNoGroupPar[i].thesis.grade==null){
          this.$message.warning("选中的部分学生无绩点数据，按0进行分组")
          break;
        }
      }
      this.postRequest(url, data).then((resp) => {
          if (resp) {
            if (resp == "分组成功") {
              this.$message.success(resp)
              this.initUnderGraduateStudents(this.currentPage, this.pageSize);
            } else {
              this.$message.error(resp)
            }
            this.closeDialogGroup()
          }
      })
    },
    groupsForStudent() {
      if (this.undergraduateStudents == null || this.undergraduateStudents.length == 0) {
        this.$message.warning('请先导入学生！')
        return
      }
      this.groupSubOfSelectedSpecialty = []
      this.groupSubOfSelectedClass = []
      this.groupClassNums = {}
      this.groupSpecialtyNums = {}
      var url = '/undergraduateM/basic/getUngrouped/?year=' + this.startYear + "&month=" + this.selectSemester;
      this.getRequest(url).then((resp) => {
        if (resp.status == 200) {
          this.NoGroupPar = resp.obj;
          if (this.NoGroupPar.length === 0) {
            this.$message.warning('当前没有未分组的学生！')
            return
          }
          var data = resp.obj;
          for (var i = 0; i < data.length; i++) {
            if (!(data[i].className in this.groupClassNums)) {
              this.groupClassNums[data[i].className] = [];
              this.groupSubOfSelectedClass.push(data[i].className);
            }
            if (!(data[i].specialty in this.groupSpecialtyNums)) {
              this.groupSpecialtyNums[data[i].specialty] = [];
              this.groupSubOfSelectedSpecialty.push(data[i].specialty);
            }
            this.groupClassNums[data[i].className].push(data[i])
            this.groupSpecialtyNums[data[i].specialty].push(data[i])
          }
          if (!this.groupNums) {
            this.groupNums = Array.from(Array(10).keys(), n => n + 1)
          }
          this.dialogStudentGroup = true
          console.log(this.NoGroupPar);
        }
      })
    },
  }
}
</script>

<style scoped>


.footer {
  text-align: center;
}

.select_div_input {
  /*margin-left:3px;*/
  width: 20%;
  height: 32px;
  position: relative;
  display: inline-block
}

.select_div {
  border: .5px solid lightgray;
  border-radius: 3px;
  margin-top: 5px;
  font-size: 14px;
  position: absolute;
  background-color: #fff;
  z-index: 999;
  overflow: scroll;
  width: 90%;
  cursor: pointer;
}

.select_div_div:hover {
  background-color: lightgray;
}

.select_div_div {
  padding-bottom: 2px;
  /*padding-top: 7px;*/
  padding-left: 12px;
  width: 100%;
}
</style>
