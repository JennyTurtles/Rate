<template>
  <div>
<!--    <el-row>-->
    <div style="margin-top: 10px">
      <span style="font-size: 15px">学生姓名:</span>
      <div class="select_div_input">
        <input
            autocomplete="off"
            style="
        line-height: 28px;
        width: 80px;
        height: 30px;
        border: 1px solid lightgray;
        padding: 0 10px 1px 15px;
        border-radius: 4px;
        color: gray;
      "
            placeholder="学生姓名"
            v-model="selectGraduatesName"
            @focus="inputSelectGraduatesNameFocus"
            @blur="isSelectShow = isSelectFlag"
        />
        <div
            class="select_div"
            v-show="isSelectShow && selectGraduatesName"
            :style="'height:' + menuHeight"
            @mouseover="isSelectFlag = true"
            @mouseleave="isSelectFlag = false"
        >
          <div
              class="select_div_div"
              v-for="val in select_graduates"
              :key="val"
              :value="val"
              @click="filter_graduates(val)"
          >
            {{ val }}
          </div>
        </div>
      </div>
      <span style="font-size: 15px; margin-left: 10px">入学年份:</span>
      <div class="select_div_input">
        <input
            autocomplete="off"
            style="
        line-height: 28px;
        width: 80px;
        height: 30px;
        border: 1px solid lightgray;
        padding: 0 10px 1px 15px;
        border-radius: 4px;
        color: gray;
      "
            placeholder="入学年份"
            v-model="selectYear"
            @focus="inputSelectYearFocus"
            @blur="isSelectYearShow = isSelectYearFlag"
        />
        <div
            class="select_div"
            v-show="isSelectYearShow"
            style="height: 100px; overflow: scroll"
            @mouseover="isSelectYearFlag = true"
            @mouseleave="isSelectYearFlag = false"
        >
          <div
              class="select_div_div"
              v-for="val in selectYearsList"
              :key="val"
              :value="val"
              @click="filter_year(val)"
          >
            {{ val }}
          </div>
        </div>
      </div>
      <span style="font-size: 15px; margin-left: 10px">专业类型:</span>
      <div class="select_div_input">
        <input
            autocomplete="off"
            style="
        line-height: 28px;
        width: 80px;
        height: 30px;
        border: 1px solid lightgray;
        padding: 0 10px 1px 15px;
        border-radius: 4px;
        color: gray;
      "
            placeholder="专业类型"
            v-model="selectType"
            @focus="inputSelectTypeFocus"
            @blur="isSelectTypeShow = isSelectTypeFlag"
        />
        <div
            class="select_div"
            v-show="isSelectTypeShow"
            style="height: 80px"
            @mouseover="isSelectTypeFlag = true"
            @mouseleave="isSelectTypeFlag = false"
        >
          <div
              class="select_div_div"
              v-for="val in selectTypeList"
              :key="val"
              :value="val"
              @click="filter_type(val)"
          >
            {{ val }}
          </div>
        </div>
      </div>

      <span style="font-size: 15px; margin-left: 10px">积分选择:</span>
      <div class="select_point_input">
        <input
            type="number"
            autocomplete="off"
            v-model.number="point1"
            :min="0"
            :max="50"
        />
      </div>
      <label>&nbsp;-&nbsp;</label>
      <div class="select_point_input">
        <input
            type="number"
            autocomplete="off"
            v-model.number="point2"
            :min="0"
            :max="50"
        />
      </div>
      <el-button
          icon="el-icon-search"
          @click="filterBtn"
          style="margin-left: 30px"
          type="primary"
      >搜索</el-button>
      <el-button
          icon="el-icon-search"
          @click="reinitGraduateStudents()"
          type="primary"
      >重置</el-button>
    </div>

    <!--    </el-row>-->
      <div style="margin-top: 20px">
        <el-table
          class="table-with-shadow"
          :data="graduateStudents"
          stripe="stripe"
          border="border"
          :header-cell-style="rowClass"
          style="width: 100%"
        >
          <el-table-column
            prop="stuNumber"
            label="学号"
            align="center"
            width="100px"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="姓名"
            align="center"
            width="100px"
          ></el-table-column>
          <el-table-column
            prop="year"
            label="入学年份"
            align="center"
            width="100px"
          ></el-table-column>
          <el-table-column
            prop="studentType"
            label="学生类别"
            align="center"
            width="100px"
          ></el-table-column>
          <el-table-column
            prop="telephone"
            label="电话"
            align="center"
            width="150px"
          ></el-table-column>
          <el-table-column
            prop="email"
            label="邮箱"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="point"
            label="积分"
            align="center"
            width="100px"
          ></el-table-column>
          <el-table-column
            prop="username"
            label="用户名"
            align="center"
            width="100px"
          ></el-table-column>
          <el-table-column label="操作" align="center" width="100px">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                plain="plain"
                @click="resetPasswordShow(scope.row)"
                style="padding: 4px"
                >重置密码</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    
    <el-dialog
      title="重置密码"
      :visible.sync="dialogResetPassword"
      center="center"
      width="400px"
    >
      <el-form>
        <el-form-item label="请输入新密码:">
          <el-input style="width: 60%" v-model="newPassword" ></el-input>
        </el-form-item>
        <div class="footer">
          <el-button @click="resetPassword" type="primary">确认</el-button>
          <el-button @click="closeDialogReset" type="primary">取消</el-button>
        </div>
      </el-form>
    </el-dialog>
    <div style="margin-top: 10px; text-align: right">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalCount"
        :page-sizes="pageSizes"
        background="background"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "GraduateList",
  data() {
    return {
      point1: 0,
      point2: 15,
      pageSizes: [10, 20, 30],
      totalCount: 0,
      currentPage: 1,
      pageSize: 20,

      isSelectFlag: false,
      isSelectShow: false, //搜索老师名字的搜索框
      select_graduates: [],
      selectGraduatesName: "",

      isSelectYearFlag: false,
      isSelectYearShow: false,
      selectYearsList: [],
      selectYear: "",

      isSelectTypeFlag: false,
      isSelectTypeShow: false,
      selectTypeList: ["专硕", "学硕"],
      selectType: "",

      database: [], //永久存储初始化页面获取的数据

      newPassword: "dhucst@11", //重置密码中的新密码
      dialogResetPassword: false,

      dialogEdit: false,
      yearTimer: null,
      timer: null,

      user: {},
      graduateStudents: [],

      currentGraduateStudentOfEdit: {
        id: null,
        name: "",
        year: null,
        studentType: "",
        telephone: null,
        email: "",
        point: null,
        username: "",
        password: "",
      },
    };
  },
  watch: {
    selectGraduatesName: {
      handler(val) {
        this.delayInputTimer(val);
      },
    },
  },
  computed: {
    menuHeight() {
      return this.selectGraduatesName.length * 50 > 150
        ? 150 + "px"
        : `${this.selectGraduatesName.length * 50}px`;
    },
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem("user"));
    this.tutorID = this.user.ID;
    this.initSelectYearsList();
    this.initGraduateStudents(this.currentPage, this.pageSize);
  },
  methods: {
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:13px;text-align:center";
    },

    closeDialogReset() {
      this.dialogResetPassword = false;
    },
    resetPasswordShow(data) {
      //重制密码
      this.currentGraduateStudentOfEdit = data;
      this.dialogResetPassword = true;
    },
    resetPassword() {
      //重制密码
      if (this.newPassword == "" || this.newPassword == null) {
        this.$message.warning("请输入密码！");
        return;
      }
      this.currentGraduateStudentOfEdit.password = this.newPassword;
      this.currentGraduateStudentOfEdit.studentID =
        this.currentGraduateStudentOfEdit.id;

      this.postRequest(
        "/graduatestudentM/basic/resetUnderPassword",
        this.currentGraduateStudentOfEdit
      ).then((response) => {
        if (response) {
          if (response.status == 200) {
            this.$message.success("重置成功，密码重置为"+this.newPassword);
            this.closeDialogReset();
          } else {
            this.$message.fail("重置失败");
          }
        }
      });
    },
    inputSelectGraduatesNameFocus() {
      //input获取焦点判断是否有下拉框，是否可输入
      this.isSelectShow = true; //控制下拉框是否显示
    },
    filter_graduates(val) {
      //点击某个筛选出来的名字
      this.selectGraduatesName = val;
      this.isSelectShow = false;
      this.isSelectFlag = false;
    },

    inputSelectYearFocus() {
      //年份输入框获得焦点
      this.isSelectYearShow = true;
    },
    filter_year(val) {
      //点击年份下拉框的某个选项
      this.selectYear = val;
      this.isSelectYearShow = false;
    },

    inputSelectTypeFocus() {
      //类型输入框获得焦点
      this.isSelectTypeShow = true;
    },
    filter_type(val) {
      //点击类型下拉框的某个选项
      this.selectType = val;
      this.isSelectTypeShow = false;
    },

    filterBtn() {
      //点击筛选按钮
      this.graduateStudents = this.database;

      // 筛选积分
      const { point1, point2 } = this;
      this.graduateStudents = this.graduateStudents.filter(
        (student) => student.point >= point1 && student.point <= point2
      );

      // 筛选毕业生
      const { selectYear, selectGraduatesName, selectType } = this;
      const filteredGraduateStudents = this.graduateStudents.filter(
        ({ year, name, studentType }) => {
          return (
            (selectYear === "" || year === selectYear) &&
            name
              .toLowerCase()
              .includes(selectGraduatesName.trim().toLowerCase()) &&
            studentType.toLowerCase().includes(selectType.trim().toLowerCase())
          );
        }
      );

      this.graduateStudents = filteredGraduateStudents;
      this.totalCount = this.graduateStudents.length;
    },

    delayInputTimer(val) {
      if (this.timer) {
        clearTimeout(this.timer);
      }
      if (!val) {
        return;
      }
      let that = this;
      this.timer = setTimeout(() => {
        that.select_graduates = [];
        that.select_graduates = Array.from(
          new Set(that.graduateStudents.map((student) => student.name))
        );
        // console.log(that.select_graduates);
      }, 300);
    },

    handleSizeChange(val) {
      // 改变每页显示的条数
      this.pageSize = val;
      // 注意：在改变每页显示的条数时，要将页码显示到第一页
      this.currentPage = 1;
      //没有筛选条件
      if (
        (this.selectYear == "" || this.selectYear == null) &&
        (this.selectGraduatesName == "" || this.selectGraduatesName == null) &&
        (this.selectType == "" || this.selectType == null)
      ) {
        this.initGraduateStudents(this.currentPage, this.pageSize);
      } else {
        //筛选条件不为空
        this.filterBtn();
      }
    },
    // 显示第几页
    handleCurrentChange(val) {
      // 改变默认的页数
      this.currentPage = val;
      if (
        (this.selectYear == "" || this.selectYear == null) &&
        (this.selectGraduatesName == "" || this.selectGraduatesName == null) &&
        (this.selectType == "" || this.selectType == null)
      ) {
        this.initGraduateStudents(this.currentPage, this.pageSize);
      } else {
        //筛选条件不为空
        this.filterBtn();
      }
    },
    initSelectYearsList() {
      //初始化筛选框中的年份数据
      const currentYear = new Date().getFullYear();
      for (let i = 0; i <= 5; i++) {
        this.selectYearsList.push(currentYear - i);
      }
    },
    reinitGraduateStudents() {
      this.selectGraduatesName = "";
      this.selectType = "";
      this.selectYear = "";
      this.point1 = 0;
      this.point2 = 15;
      this.graduateStudents = this.database;
      this.pageSize = 20;
      this.currentPage = 1;
    },
    initGraduateStudents(curr, pagesize) {
      const _this = this;
      this.teaID = JSON.parse(localStorage.getItem("user")).id;
      let url =
        "/graduatestudentM/basic/getGraduateListByTutorID?tutorID=" +
        this.teaID +
        "&pageNum=" +
        curr +
        "&pageSize=" +
        pagesize;
      this.getRequest(url).then((response) => {
        if (response.status == 200) {
          _this.graduateStudents = response.obj[0];
          _this.database = _this.graduateStudents;
          _this.totalCount = response.obj[1];
        }
      });
    },
  },
};
</script>

<style scoped="scoped">
.select_div_input {
  position: relative;
  display: inline-block;
}
.select_div {
  border: 0.5px solid lightgray;
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

.select_point_input {
  display: inline-block;
  vertical-align: middle;
}

.select_point_input input {
  line-height: 28px;
  width: 50px;
  height: 30px;
  border: 1px solid lightgray;
  padding: 0 10px 1px 15px;
  border-radius: 4px;
  color: gray;
}
</style>
