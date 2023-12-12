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

      <el-button icon="el-icon-download" type="primary" style="margin-right: 10px" @click="exportGroupsResult">导出分组
      </el-button>

      <el-button icon="el-icon-plus" type="success" style="margin-right: 10px" @click="importTeachers">导入指导教师
      </el-button>

      <el-button icon="el-icon-search" type="info" style="margin-right: 10px" @click="search"
                 :disabled="selectDate === ''">高级查询
      </el-button>

    </div>

    <div style="margin-top: 10px">
      <label for="stuNumber">学号 </label>
      <input type="text" id="stuNumber" v-model="query.stuNumber" autocomplete="off"
             style="width:15%;line-height:28px;
                              border:1px solid lightgrey;padding:0 10px 1px 15px;
                              border-radius:4px;color:gray">

      <label for="name"> 姓名 </label>
      <input type="text" id="name" v-model="query.name" autocomplete="off"
             style="width:15%;line-height:28px;
                              border:1px solid lightgrey;padding:0 10px 1px 15px;
                              border-radius:4px;color:gray">

      <label for="tutorName"> 教师姓名 </label>
      <input type="text" id="tutorName" v-model="query.tutorName" autocomplete="off"
             style="width:15%;line-height:28px;
                              border:1px solid lightgrey;padding:0 10px 1px 15px;
                              border-radius:4px;color:gray">

      <el-button icon="el-icon-search" type="info"

                 style="margin-right: 10px;margin-left: 10px"
                 @click="initUnderGraduateStudents(1, pageSize)"
                 :disabled="selectDate === ''"> 查询
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
            :headers="{'token':user.token}"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            style="display: inline-flex; margin-left: 1px"
            :action="`/undergraduateM/basic/importThesis?type=teacher&institutionID=${user.institutionID}&startThesisID=${selectThesis}`"
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
            :headers="{'token':user.token}"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            style="display: inline-flex; margin-left: 1px"
            :action="`/undergraduateM/basic/importThesis?type=student&institutionID=${user.institutionID}&startThesisID=${selectThesis}`"
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
    <el-dialog :visible.sync="dialogStudentGroup" width="85%" title="学生分组-根据学生绩点分组" center
               @close="closeDialogGroup">
      <div>
          <div style="margin-top: 15px;width: 100%">
            分组对象：
            <template>
              <el-radio v-model="groupWay" label="专业">待分组学生专业</el-radio>
              <el-radio v-model="groupWay" label="班级">待分组学生班级</el-radio>
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
          <div class="table" v-show="selectedGroupNums > 0" >
            <el-table
                :data="groupNumsInput"
                :key="bomCheckKey"
                style="width: 100%">
              <el-table-column
                  type="index"
                  label="组号"
                  min-width="3%"
                  show-overflow-tooltip>
              </el-table-column>
              <el-table-column
                  :label=" '人数 ' + currentAllSelect + '/' + filterNoGroupPar.length"
                  min-width="10%">
                <template slot-scope="scope">
                  <input v-model="scope.row.value" style="width: 60px; margin-bottom: 10px" :disabled="scope.row.disabled || scope.row.order"
                         @blur="currentInputChange()"/>
                </template>
              </el-table-column>
              <el-table-column
                  label="指定分配人数"
                  min-width="10%"
                  v-if="radio === '2'">
                <template slot-scope="scope">
                  <span>{{scope.row.orderSum}}</span>
                  <el-button
                      type="text"
                      icon="el-icon-edit"
                      size="medium"
                      style="padding-left: 5px"
                      @click="showStuTable(scope.row)">
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                  prop="className"
                  min-width="10%"
                  v-if="radio === '2'"
                  label="是否指定分配专业/班级">
                <template slot-scope="scope">
                  <el-checkbox
                      v-show="radio === '2'"
                      v-model="scope.row.order"
                      style="padding-left: 5px">
                  </el-checkbox>
                </template>
              </el-table-column>
              <el-table-column
                  v-for="(info) in this.selectedSubGroupInfo"
                  :label="info +' ' + currentSubSelect[info] + '/' + (groupWay === '专业' ? groupSpecialtyNums[info].length : groupClassNums[info].length)"
                  min-width="10%"
                  v-if="radio === '2'">
                <template slot-scope="scope">
                  <input v-model="scope.row.selectGroupInfo[info]" style="width: 60px; margin-bottom: 10px" :disabled="!scope.row.order"
                         @focus="getInfoBefore(scope.row.selectGroupInfo[info])"
                         @blur="juegeInput(scope.row, info)"/>
                </template>
              </el-table-column>
            </el-table>
          </div>
      </div>
    </el-dialog>
    <el-dialog :title="title_searchStu" :visible.sync="showStudents" width="55%" center @close="closeDialogSearch">
      <el-input
          v-model="searchText"
          placeholder="请输入学号或姓名进行搜索"
          @keyup.enter.native="searchStu"
          @input="searchStu"
      >
      </el-input>
      <span style="color: #4b8ffe">已选学生({{selectStu.length}})人：</span>
      <span v-for="item in selectStu" style="padding-left: 5px; color: #4b8ffe">{{item.name}}</span>
      <el-table
          ref="multipleTable"
          :data="searchStudent"
          :row-key="getRowKeys"
          tooltip-effect="dark"
          style="width: 100%"
          max-height="400px"
          @selection-change="handleSelectionChange">
        <el-table-column
            :reserve-selection="true"
            type="selection"
            width="40px">
        </el-table-column>
        <el-table-column
            prop="stuNumber"
            label="学号"
            show-overflow-tooltip>
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名">
        </el-table-column>
        <el-table-column
            prop="thesis.grade"
            label="绩点">
        </el-table-column>
        <el-table-column
            prop="className"
            v-if="groupWay === '班级'"
            label="班级">
        </el-table-column>
        <el-table-column
            prop="specialty"
            v-if="groupWay === '专业'"
            label="专业">
        </el-table-column>
      </el-table>
      <div style="color: #4b8ffe ;float: right;">
        <el-button type="primary" @click="order" style="padding-left: 10px; padding-top: 5px">
          确认分配
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";
import {debounce} from "@/utils/debounce";
import PinYinMatch from "pinyin-match";


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
        startThesisID: null,
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
      groupSpecialtyNums: {}, //专业计数
      currentAllSelect: 0, //当前选择的所有人数
      currentSubSelect:[], //当前指定的不同子信息项人数
      currentInputItem:'',
      showStudents:false, //用于展示指定分配的table
      inputBefore: null,
      bomCheckKey: 0,
      selectStu:[],
      title_searchStu:'',
      radio: '1',
      groupWay: '专业',
      searchText: '',
      searchStudent:[],
      options: [],
      selectDate: '',
      canImportStudents: false,
      selectSemester: '',
      startYear: null,
      selectThesis: null,
      selectTeacherNameOrJobnumber: '',//编辑框中导师搜索一栏的下拉框绑定数据
      newPassword: 'dhucst',//重置密码中的新密码
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
        this.showStudents = false
        for (var i = 0; i < val; i++) {
          this.groupNumsInput.push({
            idx: i,
            value: '',
            disabled: true, //默认是均分
            selectGroupInfo:{},
            orderSum: 0, //指定分配人数
            orderNum: {},
            order: false
          });
        }
        if (this.radio === '2') {//如果是指定人数
          for (var item of this.groupNumsInput) {
            item.disabled = false
          }
        }
        for (var item of this.groupNumsInput) {
          for (var info of this.selectedSubGroupInfo){
            item.selectGroupInfo[info] = 0
            item.orderNum[info] = []
          }
        }
        this.calculationGroupInputValue()
      }
    },
    radio: {
      handler(val) {
        this.showStudents = false
        if (this.radio === '1') {//均分
          for (var i = 0; i < this.selectedGroupNums; i++) {
            this.groupNumsInput[i].disabled = true
          }
        } else {//指定每组人数
          for (var i = 0; i < this.selectedGroupNums; i++) {
            this.groupNumsInput[i].disabled = false
          }
        }
        this.calculationGroupInputValue()
      }
    },
    selectedSubGroupInfo: {//第一个下拉框的变化
      handler(val) {
        this.showStudents = false
        if (this.groupWay === '专业')
          this.filterNoGroupPar = this.NoGroupPar.filter(item => this.selectedSubGroupInfo.includes(item.specialty))
        if (this.groupWay === '班级')
          this.filterNoGroupPar = this.NoGroupPar.filter(item => this.selectedSubGroupInfo.includes(item.className))
        this.searchStudent = this.filterNoGroupPar
        for (var item of this.groupNumsInput) {
          for (var info of this.selectedSubGroupInfo){
            item.orderSum = 0
            item.orderNum[info] = []
          }
        }
        this.calculationGroupInputValue()
      }
    },
    groupWay: {
      handler(val) {
        this.selectedSubGroupInfo = []
        this.radio = '1'
        this.selectedGroupNums = null
        this.currentAllSelect = 0
        for (var item of this.groupNumsInput) {
          for (var info of this.selectedSubGroupInfo){
            item.orderSum = 0
            item.orderNum[info] = []
          }
        }
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
    emptyQuery() {
      this.query = {
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
        startThesisID: null,
        pageNum: null,
        pageSize: null,
      };
    },


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
        const url = `/undergraduateM/basic/getThesisExistDate?institutionID=${this.user.institutionID}&adminID=${this.user.id}`;
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
        let message = option.split('.')[1];
        let year = message.substring(0, 4);
        let season = message.slice(-2) === '春季' ? 3 : 9;
        let optionID = option.split('.')[0];
        let optionValue = optionID + '.' + year + season

        return {value: optionValue, label: message};
      });
    },

    handleSelectSemesterChange() {
      let message = this.selectDate.split('.')[1];
      this.startYear = parseInt(message.substring(0, 4));
      this.selectSemester = parseInt(message.charAt(4));
      this.selectThesis = parseInt(this.selectDate.split('.')[0]);
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
      const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[^a-zA-Z\d])[\S]{8,20}$/;
      if (this.newPassword != "dhucst" && !passwordRegex.test(this.newPassword)) {
        this.$message.error('密码必须是8-20位，包含至少一个英文字符，一个数字和一个特殊字符(@$!%*?&)');
        return
      }
      this.postRequest('/undergraduateM/basic/resetUnderPassword', this.currentUnderStudentOfEdit).then((response) => {
        if (response) {
          if (response.status == 200) {
            this.$message.success("重置成功，密码重置为" + this.newPassword);
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
          // 将搜索框中的内容制空
          this.emptyQuery()
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
          startThesisID: this.selectThesis
        };
        const queryParamsStr = Object.entries(queryParams)
            .map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
            .join('&');

        const url = `/undergraduateM/basic/deleteThesis?${queryParamsStr}`;

        this.postRequest(url).then(async (resp) => {
          if (resp.status === 200) {
            this.$message.success('删除成功');
            this.emptyQuery()
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
        this.query.startThesisID = this.selectThesis;
        this.query.startYear = this.startYear;
        this.query.month = this.selectSemester;

        console.log(this.query)
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
    getRowKeys(row) {
      return row.id;
    },
    calculationGroupInputValue() {//计算input框的值
      if (this.filterNoGroupPar.length === 0) {//如果没有选择分组个数或没有待分组的选手直接返回
        for (var i = 0; i < this.selectedGroupNums; i++)
          this.groupNumsInput[i].value = null;
        this.currentAllSelect = 0;
        return
      }
      if (this.selectedGroupNums > this.filterNoGroupPar.length) {
        this.$message.warning('分组数不能超过未分组人数！')
        return
      }
      var participantNums = this.filterNoGroupPar.length //学生人数
      var groupNums = this.selectedGroupNums //组数
      if (this.selectedGroupNums != null && this.radio ==='1') {//均分
        var baseNums = Math.floor(participantNums / groupNums);
        for (var i = 0; i < groupNums; i++) {
          this.groupNumsInput[i].value = baseNums;
        }
        for (var i = 0; i < (participantNums - baseNums * groupNums); i++) {
          this.groupNumsInput[i].value++;
        }
      }
      else if (this.radio === '2'){
        for (var i = 0; i < groupNums; i++) {
          this.groupNumsInput[i].value = 0;
        }
        for (var info of this.selectedSubGroupInfo){
          var infoSum = this.groupWay === '专业' ? this.groupSpecialtyNums[info].length : this.groupClassNums[info].length;
          var baseNums = Math.floor(infoSum / groupNums);
          for (var i = 0; i < groupNums; i++) {
            this.groupNumsInput[i].value += baseNums;
            this.groupNumsInput[i].selectGroupInfo[info] = baseNums;
          }
          for (var i = 0; i < (infoSum - baseNums * groupNums); i++) {
            this.groupNumsInput[i].selectGroupInfo[info]++;
            this.groupNumsInput[i].value++;
          }
        }
      }
      this.currentInputChange('总人数');
    },
    calculationCurrentInput(){
      if (this.radio === '2'){
        this.currentSubSelect = {}
        for (let item of this.groupNumsInput){
          item.value = 0
          for (let info of this.selectedSubGroupInfo){
            if (!(info in this.currentSubSelect))
              this.currentSubSelect[info] = 0
            this.currentSubSelect[info] += item.selectGroupInfo[info] === null ? 0 : parseInt(item.selectGroupInfo[info])
            item.value += item.selectGroupInfo[info] === null ? 0 : parseInt(item.selectGroupInfo[info])
          }
        }
      }
      let sum = 0;
      this.groupNumsInput.map(item => {
        sum += parseInt(item.value)
      })
      this.currentAllSelect = sum;
    },
    currentInputChange(){
      let un_ordered = [], sumNums = {}, itemSum= {}  //sumNums:各专业的已指定人数和总人数记录 itemSum:每个未指定分配的item已自动分配的人数
      for (let info of this.selectedSubGroupInfo){
        sumNums[info] = []
        sumNums[info].push(0)  //0:指定的人数 1:总人数
        let num = this.groupWay === '专业' ? this.groupSpecialtyNums[info].length : this.groupClassNums[info].length
        sumNums[info].push(num)
      }
      if (this.radio === '2'){
        for (let item of this.groupNumsInput) { //取出未指定分配的组，进行自动分配
          if (item.order === true) {
            item.value = 0
            for (let info of this.selectedSubGroupInfo) {
              sumNums[info][0] += parseInt(item.selectGroupInfo[info])
              item.value += parseInt(item.selectGroupInfo[info]);
            }
          } else {
            item.value = parseInt(item.value)
            un_ordered.push(item)
            itemSum[item.idx] = 0
            for (let info of this.selectedSubGroupInfo) {
              item.selectGroupInfo[info] = 0
              if (item.orderNum[info].length > 0) {
                item.selectGroupInfo[info] = item.orderNum[info].length
                itemSum[item.idx] += item.orderNum[info].length
                sumNums[info][0] += parseInt(item.orderNum[info].length)
              }
            }
          }
        }
        let temp_selectInfo = []
        for (let i = 0; i < this.selectedSubGroupInfo.length; i++)
          temp_selectInfo[i] = this.selectedSubGroupInfo[i]
        for (let k = 0; k < temp_selectInfo.length - 1; k++) {  //对选择的信息项按照人数进行排序从小到大，使自动分配更平均
          for (let i=0; i < temp_selectInfo.length - 1 - k ; i++) {
            let num1 = this.groupWay === '专业' ? this.groupSpecialtyNums[temp_selectInfo[i]] : this.groupClassNums[temp_selectInfo[i]]
            let num2 = this.groupWay === '专业' ? this.groupSpecialtyNums[temp_selectInfo[i + 1]] : this.groupClassNums[temp_selectInfo[i + 1]]
            if (num1 > num2) {
              let temp = temp_selectInfo[i];
              temp_selectInfo[i] = temp_selectInfo[i + 1];
              temp_selectInfo[i + 1] = temp;
            }
          }
        }
        let max = []  //max：已满组
        for (let info of temp_selectInfo){  //对每组进行分配
          let participantNums = sumNums[info][1] - sumNums[info][0]  //学生人数
          let i = 1
          while (i <= participantNums && max.length < un_ordered.length){
            for (let item of un_ordered){
              if (itemSum[item.idx] === item.value || i/un_ordered.length < item.orderNum[info].length){
                if (!(max.includes(item.idx)))
                  max.push(item.idx)
                continue
              }
              item.selectGroupInfo[info]++
              itemSum[item.idx]++
              i++
              if (i > participantNums || max.length === un_ordered.length) break
            }
          }
        }
        for (let item of this.groupNumsInput){   // 当输入的人数大于总人数时对人数不足的组补齐
          let sum = 0
          for (let info of this.selectedSubGroupInfo)
            sum += item.selectGroupInfo[info]
          var flag = true
          while (sum < item.value){
            for (let info of this.selectedSubGroupInfo){
              item.selectGroupInfo[info]++
              sum++
              if (sum === item.value){
                flag = false
                break
              }
            }
            if (!flag) break
          }
        }
      }
      this.calculationCurrentInput()
    },
    getInfoBefore(value){
      this.inputBefore = value;
    },
    juegeInput(row, info){
      if (row.orderNum[info].length > row.selectGroupInfo[info]){
        this.$alert('当前输入的人数小于该组' + info + '分配的人数，请重新输入', '提示', {
          confirmButtonText: '确定',
          type: 'warning',
          callback: action => {
            row.selectGroupInfo[info] = this.inputBefore
            this.bomCheckKey++
          }
        });
      }
      else
        this.calculationCurrentInput()
    },
    handleSelectionChange(val){
      this.selectStu = val;
    },
    closeDialogSearch(){
      this.showStudents = false;
      this.$refs.multipleTable.clearSelection();
      this.searchText = '';
    },
    order(){
      var count = {}, sum = 0;
      for (var info of this.selectedSubGroupInfo){
        if (!(info in count)){
          count[info] = 0;
        }
        this.selectStu.forEach(item => {
          if (this.groupWay === '专业' && item.specialty === info) {
            count[info]++;
            sum++;
          }
          if (this.groupWay === '班级' && item.className === info) {
            count[info]++;
            sum++;
          }
        })
        if (count[info] > this.currentInputItem.selectGroupInfo[info] && this.currentInputItem.order === true){
          this.$message.error('分配的' + info + '人数大于该组' + info + '总人数，请重新选择！')
          return
        }
        if (sum > this.currentInputItem.value){
          this.$message.error('分配的人数大于该组总人数，请重新选择！')
          return
        }
      }
      for (var info of this.selectedSubGroupInfo){
        this.currentInputItem.orderNum[info] = []
      }
      this.currentInputItem.orderSum = sum
      for (var i = 0; i < this.selectStu.length; i++ ){
        if (this.groupWay === '专业') {
          this.currentInputItem.orderNum[this.selectStu[i].specialty].push(this.selectStu[i]);
        }
        else
          this.currentInputItem.orderNum[this.selectStu[i].className].push(this.selectStu[i]);
      }
      this.currentInputChange();
      this.showStudents = false;
    },
    showStuTable(inputItem){
      this.currentInputItem = inputItem
      let index = inputItem.idx+1
      this.title_searchStu = "第" + index + "组"
      var curr_ids = [], other_ids = []
      for (let i = 0; i < this.filterNoGroupPar.length; i++)
        this.searchStudent[i] = this.filterNoGroupPar[i]
      for (var info of this.selectedSubGroupInfo){
        for (var item of this.groupNumsInput){
          if (item.orderNum[info].length > 0){
            if (item === inputItem)
              item.orderNum[info].forEach(e => { curr_ids.push(e.id) })
            else
              item.orderNum[info].forEach(e => { other_ids.push(e.id) })
          }
        }
      }
      this.searchStudent = this.searchStudent.filter((e) => !other_ids.includes(e.id))
      this.showStudents = true
      this.$nextTick(() => {
        this.searchStudent.forEach(item => {
          if (curr_ids.includes(item.id))
            this.$refs.multipleTable.toggleRowSelection(item, true)
        })
      })
    },
    searchStu() {
      this.showStuTable(this.currentInputItem)
      var filter_new = []
      for (var i = 0; i < this.searchStudent.length; i++)
        filter_new[i] = this.searchStudent[i]
      if (this.searchText === ''){
        this.searchStudent = filter_new
      }else if (/^\d+$/.test(this.searchText)){ // 纯数字，按工号搜索
        this.searchStudent = filter_new.filter(item => item.stuNumber != null &&  item.stuNumber.includes(this.searchText))
      }else if (/^[a-zA-Z]*$/.test(this.searchText)){ //纯英文，考虑首字母
        this.searchStudent = filter_new.filter(item => PinYinMatch.match(item.name,this.searchText))
      } else { // 非纯数字，按姓名搜索
        this.searchStudent = filter_new.filter(item => item.name.includes(this.searchText))
      }
    },
    closeDialogGroup() {//选手分组对话框关闭,清空遗留数据
      this.dialogStudentGroup = false;
      this.selectedSubGroupInfo = []
      this.selectedGroupNums = 0
      this.filterNoGroupPar = []
      this.groupSubOfSelectedClass = []
      this.groupSubOfSelectedSpecialty = []
      this.groupWay = '专业'
      this.groupNumsInput = []
      this.radio = '1'
    },
    creatGroup() {//创建分组
      for (let item of this.groupNumsInput) { // 遍历this.groupNumsInput，如果value为0，报错
        if (item.value === 0) {
          this.$message.error('第' + (item.idx + 1) + '组人数为0，请重新输入！')
          return
        }
      }
      if (this.radio === '2') {
        if (this.currentAllSelect !== this.filterNoGroupPar.length) {
          this.$message.error('待创建分组人数为' + this.filterNoGroupPar.length + '，分组总人数为' + this.currentAllSelect + '，人数不相等！')
          return
        }
      }
      if (this.selectedGroupNums === 0) {
        this.$message.error('请选择分组数！')
        return
      }
      var url = '/undergraduateM/basic/createGroups'
      //每组人数
      var arr = this.groupNumsInput.map(item => {
        return parseInt(item.value)
      })
      var orderNums = {}, arrSub = {}
      for (var info of this.selectedSubGroupInfo){
        if (!(info in arrSub)){
          arrSub[info] = []
        }
        if (!(info in orderNums)){
          orderNums[info] = {}
        }
        for (var item of this.groupNumsInput){
          if (!(item.idx in orderNums[info])){
            orderNums[info][item.idx] = []
          }
          arrSub[info].push(parseInt(item.selectGroupInfo[info]))
          item.orderNum[info].forEach(per => {
            orderNums[info][item.idx].push(per.thesis.id)
          })
        }
      }
      arrSub = this.radio === '1' ? null : arrSub
      var data = {
        'startThesisID': this.selectThesis,
        'arr': arr,
        'exchangeNums': Math.ceil(this.selectedGroupNums / 2),
        'groupsNums': this.selectedGroupNums,
        'selectInfo': this.selectedSubGroupInfo,
        'groupWay': this.groupWay,
        'orderNums': orderNums,
        'arrSub': arrSub
      }
      console.log(data)
      for (var i = 0; i < this.filterNoGroupPar.length; i++) {
        if (this.filterNoGroupPar[i].thesis.grade == null) {
          this.$message.warning("选中的部分学生无绩点数据，将按0进行分组")
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
    async exportGroupsResult() {
      if (this.selectDate == "") {
        this.$message.warning('请在上方选择导出毕业设计的批次！')
        return
      }
      if (this.undergraduateStudents == null || this.undergraduateStudents.length == 0) {
        this.$message.warning('请先导入学生！')
        return
      }
      if (this.undergraduateStudents[0].group == null||this.undergraduateStudents[0].group == "") {
        this.$message.warning('请先分组学生！')
        return
      }
      const url = '/undergraduateM/basic/exportGroupsResult';
      console.log(this.undergraduateStudents);
      fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.undergraduateStudents),
      })
          .then(response => response.blob())
          .then(blob => {
            const url = window.URL.createObjectURL(new Blob([blob]));
            const a = document.createElement('a');
            a.href = url;
            a.download = '本科生毕业设计分组结果.xls'; // 设置下载文件的名称
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
          })
          .catch(error => console.error('Error:', error));
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
      var url = '/undergraduateM/basic/getUngrouped/?startThesisID=' + this.selectThesis;
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

<style lang="less">
.table {
  //避免单元格之间出现白色边框
  .el-table__row > td {
    border: none;
  }
  //修改表格边框颜色
  .el-table {
    --el-table-border-color: #ffffff;
  }
  .el-table::before { height: 0px; }
}
</style>
