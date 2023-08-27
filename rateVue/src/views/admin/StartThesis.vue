<template>
  <div>
    <div>
      <label>开启毕业设计：</label>
      <el-input-number v-model="startYear" :min="1999" :max="new Date().getFullYear()"
                       style="width: 120px"
                       @change="handleSelectSemesterChange"></el-input-number>
      <span style="margin-left: 10px">学期：</span>
      <el-select v-model="selectSemester" style="width: 100px; height: 30px;" @change="handleSelectSemesterChange">
        <el-option value="春季">春季</el-option>
        <el-option value="秋季">秋季</el-option>
      </el-select>
      <el-button @click="startThesis" type="primary" style="margin-left: 10px">开启</el-button>
    </div>


    <div style="margin-top: 10px">
      导入参加的学生第一步：
      <el-button icon="el-icon-upload" type="primary" style="margin-right: 10px" @click="downloadExcel">下载模版
      </el-button>
      第二步：
      <el-upload
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-success="onSuccess"
          style="display: inline-flex; margin-left: 1px"
          :action="`/undergraduateM/basic/importThesis?type=student&institutionID=${this.user.institutionID}&year=${this.startYear}&semester=${encodeURIComponent(this.selectSemester)}`"
      >
        <el-button icon="el-icon-plus" type="success" :disabled=havingStart>导入学生</el-button>
      </el-upload>

    </div>


    <div style="margin-top: 10px">
      <el-table
          :data="undergraduateStudents">
        <el-table-column prop="stuNumber" label="学号" align="center"></el-table-column>
        <el-table-column prop="name" label="姓名" align="center" width="80px"></el-table-column>
        <el-table-column prop="specialty" label="专业" align="center"></el-table-column>
        <el-table-column prop="className" label="班级" align="center"></el-table-column>
        <el-table-column prop="telephone" label="电话" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
        <el-table-column prop="year" label="入学年份" align="center"></el-table-column>
      </el-table>
    </div>
    <el-dialog title="编辑信息" :visible.sync="dialogEdit" center width="500px" @close="closeDialogEdit">
      <el-form :model="currentUnderStudentOfEdit" label-width="100px" style="margin-left: 20px" label-position="left">
        <!--          <el-form-item label="导师">-->
        <!--            <div class="select_div_input" style="width: 70%">-->
        <!--              <input-->
        <!--                  autocomplete="off"-->
        <!--                  style="width:95%;line-height:28px;-->
        <!--                              border:1px solid lightgrey;padding:0 10px 1px 15px;-->
        <!--                              border-radius:4px;color:gray"-->
        <!--                  placeholder="请输入老师姓名"-->
        <!--                  v-model="currentUnderStudentOfEdit.teachers.name"-->
        <!--                  @focus="inputSelectTeacerNameFocus"-->
        <!--                  @blur="isSelectShow = isSelectFlag"/>-->
        <!--              <div class="select_div"-->
        <!--                   v-show="isSelectShow && currentUnderStudentOfEdit.teachers.name ? true:false"-->
        <!--                   :style="'height:${menuHeight}'"-->
        <!--                   @mouseover="isSelectFlag = true"-->
        <!--                   @mouseleave="isSelectFlag = false"-->
        <!--              >-->
        <!--                <div-->
        <!--                    class="select_div_div"-->
        <!--                    v-for="val in selectTeaNameAndJobnumber"-->
        <!--                    :key="val"-->
        <!--                    :value="val"-->
        <!--                    @click="filterEditTeacher(val)"-->
        <!--                >-->
        <!--                  {{ val }}-->
        <!--                </div>-->
        <!--              </div>-->
        <!--            </div>-->
        <!--                  </el-form-item>-->
        <el-form-item label="学生姓名">
          <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.name"></el-input>
        </el-form-item>
        <el-form-item label="学生专业">
          <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.specialty"></el-input>
        </el-form-item>
        <el-form-item label="学生班级">
          <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.className"></el-input>
        </el-form-item>
        <el-form-item label="学生手机号">
          <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.telephone"></el-input>
        </el-form-item>
        <el-form-item label="学生邮箱">
          <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.email"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="editUnder" type="primary">确定</el-button>
          <el-button @click="closeDialogEdit">关闭</el-button>
        </span>
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

  </div>
</template>

<script>
import {Message} from "element-ui";
import {debounce} from "@/utils/debounce";

export default {
  name: "SalStudentM",
  data() {
    return {
      duplicateInsertRowsCount: 0,
      failedRowsCount: 0,
      successfulRowsCount: 0,
      totalRows: 0,
      failureReasons: [],
      dialogImportResult: false,
      havingStart: true,  // 默认没有开启
      canImportStudents: false,
      selectSemester: '',
      startYear: new Date().getFullYear(),
      selectTeaNameAndJobnumber: [],//编辑框中导师搜索一栏的下拉框绑定数据
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
      selectTeacerName: '',
      selectYear: '',
      currentUnderStudentOfEdit: {
        ID: null,
        name: '',
        specialty: '',
        className: '',
        teachers: {
          name: '',
          jobnumber: ''
        },
        studentID: null,
        tutorID: null,
        telephone: '',
        email: ''
      },
      dialogEdit: false,
      user: {},
      undergraduateStudents: []
    }
  },
  created() {
    //初始化防抖
    this.debounceSearch = debounce(this.delayInputTimer, 500)
  },
  watch: {
    selectTeacerName: {
      handler(val) {
        if (val) {
          this.debounceSearch()
        }
      }
    },
    'currentUnderStudentOfEdit.teachers.name': {
      handler(val) {
        if (val) {
          this.debounceSearch()
        }
      }
    }
  },
  computed: {
    menuHeight() {
      return this.selectTeacerName.length * 50 > 150
          ? 150 + 'px'
          : `${this.selectTeacerName.length * 50}px`
    },
    labelWidth() {
      return `${8 * 17}px`
    }
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    // this.initSelectYearsList()
    // this.initUnderGraduateStudents(this.currentPage, this.pageSize)
  },
  methods: {
    async startThesis() {
      const url = `/undergraduateM/basic/startThesis?institutionID=${this.user.institutionID}&year=${this.startYear}&semester=${encodeURIComponent(this.selectSemester)}`;
      try {
        const resp = await this.postRequest1(url);
        if (resp.status == 200) {
          this.$message.success("开启毕业设计成功！");
          await this.initUnderGraduateStudents(1, 10);
        } else {
          this.$message.error("开启毕业设计失败！");
        }

      } catch (error) {
        console.log(error);
      }
    },
    async handleSelectSemesterChange() {
      if (this.selectSemester == '') {
        return
      }
      await this.initUnderGraduateStudents(1, 10);
    },

    //编辑框中 搜索老师姓名之后点击下拉框的某个选项
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
      let url = '/undergraduateM/basic/getUnderStudentsBySelect?year=' + parseInt(tempYear) + '&teaName=' + this.selectTeacerName +
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
      this.selectTeacerName = val
      this.isSelectShow = false
      this.isSelectFlag = false
    },
    delayInputTimer() {//防抖
      let url
      if (this.dialogEdit) {
        url = '/undergraduateM/basic/getTeaNamesBySelect?teaName=' + this.currentUnderStudentOfEdit.teachers.name
      } else {
        url = '/undergraduateM/basic/getTeaNamesBySelect?teaName=' + this.selectTeacerName
      }
      this.getRequest(url).then((resp) => {
        this.select_teachers = []
        this.selectTeaNameAndJobnumber = []
        if (resp) {
          if (resp.status == 200) {
            for (var i = 0; i < resp.obj.length; i++) {
              this.select_teachers.push(resp.obj[i].name)
              this.selectTeaNameAndJobnumber.push(resp.obj[i].jobnumber + ":" + resp.obj[i].name)
            }
            this.select_teachers = Array.from(new Set(this.select_teachers));
            this.selectTeaNameAndJobnumber = Array.from(new Set(this.selectTeaNameAndJobnumber));
          }
        }
      })
    },
    inputSelectTeacerNameFocus() {//input获取焦点判断是否有下拉框，是否可输入
      this.isSelectShow = true//控制下拉框是否显示
    },
    closeDialogEdit() {//关闭对话框
      this.dialogEdit = false
    },
    editDialogShow(data) {//控制变量
      console.log(data);
      this.dialogEdit = true
      this.currentUnderStudentOfEdit = data
    },
    editUnder() {//点击编辑中的确定按钮
      // if (this.currentUnderStudentOfEdit.teachers.name == '' || this.currentUnderStudentOfEdit.teachers.name == null) {
      //   // this.currentUnderStudentOfEdit.teachers.jobnumber = ''
      //   this.$message.warning('请填写老师姓名！')
      //   return
      // }
      // if (this.currentUnderStudentOfEdit.teachers.jobnumber == '' || this.currentUnderStudentOfEdit.teachers.jobnumber == null) {
      //   // this.currentUnderStudentOfEdit.teachers.name = ''
      //   this.$message.warning('请填写老师工号！')
      //   return
      // }
      let data = this.currentUnderStudentOfEdit
      this.postRequest('/undergraduateM/basic/editUnderGraduateStudent', data).then((resp) => {
        if (resp) {
          if (resp.status == 200) {
            this.dialogEdit = false
            this.$message.success(resp.msg)
            this.initUnderGraduateStudents(this.currentPage, this.pageSize)
          } else {
            this.$message.error(resp)
          }
        }
      })
    },
    deleteUnder(data) {//删除本科生
      this.$confirm('确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: "warning"
      }).then(() => {
        this.postRequest('/undergraduateM/basic/deleteUnderGraduateStudent', data).then((resp) => {
          if (resp.code == 200) {
            this.$message.success('删除成功')
            this.initUnderGraduateStudents(this.currentPage, this.pageSize)
          } else {
            this.$message.warning('删除失败！')
          }
        })
      })
    },
    onSuccess(res) {
      const { status, msg, obj } = res;

      if (status === 200) {
        this.handleSuccessfulImport(obj);
      } else {
        this.$message.error(msg);
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
    downloadExcel() {
      let url = `/undergraduateM/basic/exportUnderGraduate?type=thesis`
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
      if ((this.selectYear == '' || this.selectYear == null) && (this.selectTeacerName == '' || this.selectTeacerName == null)) {
        this.initUnderGraduateStudents(this.currentPage, this.pageSize)
      } else {//筛选条件不为空
        this.filterBtn()
      }
    },
    // 显示第几页
    handleCurrentChange(val) {
      // 改变默认的页数
      this.currentPage = val;
      if ((this.selectYear == '' || this.selectYear == null) && (this.selectTeacerName == '' || this.selectTeacerName == null)) {
        this.initUnderGraduateStudents(this.currentPage, this.pageSize)
      } else {//筛选条件不为空
        this.filterBtn()
      }
    },
    async initUnderGraduateStudents(curr, pageSize) {
      try {
        const url = `/undergraduateM/basic/getStudents?institutionID=${this.user.institutionID}&year=${this.startYear}&semester=${this.selectSemester}&pageNum=${curr}&pageSize=${pageSize}`;

        const response = await this.getRequest(url);
        const {code, extend} = response;
        if (code === 200) {
          const [students, totalCount] = extend.res;
          this.undergraduateStudents = students;
          this.totalCount = totalCount;
          this.havingStart = !extend.havingStart;
          if (extend.havingStart == true) {
            this.$message.success("本学期已经开启毕业设计活动！");

            setTimeout(() => {
              if (this.totalCount == 0) {
                this.$message.info("本学期没有导入毕业论文信息！");
              }
            }, 1000);
          } else {
            this.$message.info("本学期没有开启毕业设计活动！");
          }

        } else {
          // 处理请求失败的情况
          this.$message.error("请求失败!")
        }
      } catch (error) {
        this.$message.error("请求出现异常！");
      }
    }

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
