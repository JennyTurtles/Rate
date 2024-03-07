<template>
  <div>
    <div style="display: flex; align-items: center;">
      <label style="margin-right: 10px;">填写信息搜索：</label>
      <el-input v-model="searchNumber" placeholder="请输入学号" style="margin-right: 10px; width: 150px" prefix-icon="el-icon-search"></el-input>
      <el-input v-model="searchName" placeholder="请输入学生姓名" style="margin-right: 10px; width: 150px" prefix-icon="el-icon-search"></el-input>
      <el-autocomplete
          style="margin-right: 10px; width: 150px;" prefix-icon="el-icon-search"
          placeholder="请输入导师姓名"
          v-model="searchTutorName"
          :fetch-suggestions="querySearchAsync"
          @select="handleSelectTutor">
      </el-autocomplete>
      <el-select v-model="searchSpecialty" placeholder="请选择专业" style="width: 150px; margin-right: 10px;" prefix-icon="el-icon-search">
        <el-option
            v-for="item in Specialties"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-select v-model="searchStuType" placeholder="请选择学生类型" style="width: 150px; margin-right: 10px;" prefix-icon="el-icon-search">
        <el-option
            v-for="item in StuTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-select v-model="searchYears" multiple placeholder="请选择年份" style="margin-right: 10px; width: 220px" prefix-icon="el-icon-search">
        <el-option
            v-for="year in years"
            :key="year"
            :label="year"
            :value="year">
        </el-option>
      </el-select>
    </div>
    <div style="display: flex; align-items: center;">
      <label style="margin-right: 10px;">填写工作时长范围搜索：</label>
      <el-input v-model="minWorkHours" placeholder="请输入最小工作时长" style="margin-right: 10px; width: 200px" prefix-icon="el-icon-search"></el-input>
      <p>-</p>
      <el-input v-model="maxWorkHours" placeholder="请输入最大工作时长" style="margin-right: 10px; margin-left: 10px; width: 200px" prefix-icon="el-icon-search"></el-input>
      <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
      <el-button type="success" icon="el-icon-refresh-right" @click="refresh">重置</el-button>
    </div>
    <div style="margin-top: 20px">
      <el-table
          class="table-with-shadow"
          :data="emps"
          stripe="stripe"
          border="border"
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
          v-loading.fullscreen.lock="loading"
      >
        <el-table-column label="" type="index" width="50"></el-table-column>
        <el-table-column
            prop="stuNumber"
            align="center"
            label="学号"
            width="100px"
            :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
            prop="name"
            align="center"
            label="姓名"
            width="100px"
            :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
            prop="teachers.name"
            align="center"
            label="导师"
            width="100px"
            :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
            prop="year"
            align="center"
            label="入学年份"
            width="100px"
            :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
            prop="specialty"
            align="center"
            label="专业"
            width="100px"
            :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
            prop="studentType"
            align="center"
            label="研究生类型"
            width="120px"
            :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
            prop="workHoursTotal"
            align="center"
            width="120px"
            label="已工作时长"
            sortable
        ></el-table-column>

        <el-table-column
            prop="programRecordTotal"
            align="center"
            width="100px"
            label="已提交"
        ></el-table-column>

        <el-table-column align="center" label="待审核" width="100px">
          <template slot-scope="scope">
            {{
              scope.row.programRecordTotal - scope.row.programRecordDeny - scope.row.programRecordPass
            }}
          </template>
        </el-table-column>

        <el-table-column align="center" width="150px" label="是否可以补填">
          <template v-slot:header='scope'>
            <span>
              	  <el-tooltip
                      :aa="scope"
                      class="item"
                      effect="dark"
                      placement="top-start"
                  >
               <i class="el-icon-info" style="color: #4b8ffe"> </i>
               <div style="width: 200px" slot="content">
                若可以补填，则学生对上一次结束日期之后的日期进行补填，否则只能填写最近两周
               </div>
	                </el-tooltip>
              {{scope.column.label}}
	          </span>
          </template>
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.fillMiss === 1" @change="fillMissChange(scope.row)"></el-checkbox>
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button
                @click="showInfoItem(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain="plain"
            >进入
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {postRequest} from "@/utils/api";

export default {
  name: "adminProgramRecord",
  data() {
    return {
      user: {},
      disabledInput: true,
      searchNumber: '',
      searchName: '',
      searchSpecialty: '',
      searchStuType: '',
      searchYears: [],
      searchTeacherID: '',
      searchTutorName: '',
      minWorkHours: '',
      maxWorkHours: '',
      headers: {
        "Content-Type": "multipart/form-data",
      },
      data_picker: "", //选择时间
      labelPosition: "left",
      title: "",
      title_show: "",
      emps: [],

      loading: false,

      total: 0, // 现在显示的数据个数
      prePlan: "", // 上期安排
      curIndex: 0,
      showTooltip: true,
      pageMonth: new Date().getMonth(),

      keyword: "",

      defaultProps: {
        children: "children",
        label: "name",
      },
      Specialties: [{
        value: '软件工程',
        label: '软件工程'
      }, {
        value: '计算机科学与技术',
        label: '计算机科学与技术'
      }],
      StuTypes: [{
        value: '专硕',
        label: '专硕'
      }, {
        value: '学硕',
        label: '学硕'
      }],
    };
  },
  watch: {
    publicationName: {
      handler(val) {
        //函数抖动
        this.delayInputTimer(val);
      },
    },
    data_picker: {
      //清除时间input设置为不可输入
      handler(val) {
        if (!val) {
          this.disabledInput = true;
        }
      },
    },
  },
  computed: {
    menuHeight() {
      return this.publicationName.length * 50 > 150
          ? 150 + "px"
          : "${this.publicationName.length * 50}px";
    },
    years() {
      const currentYear = new Date().getFullYear();
      const currentMonth = new Date().getMonth();
      return currentMonth < 8 ? [currentYear - 1, currentYear - 2, currentYear - 3] : [currentYear, currentYear - 1, currentYear - 2];
    },
  },
  created() {
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.searchYears = [...this.years]; // 默认选中最近三个年份
    this.search();
  },
  filters: {},
  methods: {
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:13px;text-align:center";
    },
    refresh(){
      this.searchNumber =  '';
      this.searchName = '';
      this.searchSpecialty = '';
      this.searchStuType = '';
      this.searchYears = [...this.years];
      this.minWorkHours = '';
      this.maxWorkHours = '';
      this.searchTeacherID = '';
      this.searchTutorName = '';
      this.search();
    },
    querySearchAsync(queryString, cb) {
      if (queryString.length < 1) {
        return cb([]);
      }
      this.getRequest('/system/teacher/searchByName?name=' + queryString)
          .then(response => {
            let results = response.obj;
            // 遍历result，把里面的name赋值给value
            results.forEach(result => {
              result.value = result.name;
            });
            // 调用回调函数返回搜索建议
            cb(results);
          })
    },
    handleSelectTutor(item) {
      this.searchTeacherID = item.id;
    },
    search(){
      this.loading = true;
      const params = {
        searchNumber: this.searchNumber,
        searchName: this.searchName,
        searchSpecialty: this.searchSpecialty,
        searchStuType: this.searchStuType,
        searchYears: this.searchYears,
        minWorkHours: this.minWorkHours,
        maxWorkHours: this.maxWorkHours,
        tutorID: this.searchTeacherID,
      };
      const queryParams = new URLSearchParams(params).toString();
      const url = '/programRecord/basic/getStuByFilter?' + queryParams;
      console.log(url)
      this.getRequest(url)
          .then((resp) => {
            this.loading = false;
            if (resp.msg === 'success') {
              this.emps = resp.obj;
              console.log(this.emps)
            } else {
              this.$message.error(resp.msg);
            }
          })
          .catch((error) => {
            console.error(error);
            this.loading = false;
          });
    },
    fillMissChange(stu){
      stu.fillMiss = 1 - stu.fillMiss;
      this.postRequest('/programRecord/basic/fillMissChange?studentID=' + stu.id + '&fillMiss=' + stu.fillMiss)
          .then((resp) => {
            if (resp) {
              this.search();
            }
          })
          .catch((error) => {
            console.error(error);
            this.loading = false;
          });
    },
    // initEmps() {
    //   this.loading = true;
    //   const url = `/programRecord/basic/getStuByTea?tutorID=${this.user.id}`;
    //   this.getRequest(url)
    //       .then((resp) => {
    //         this.loading = false;
    //         if (resp.msg === 'success') {
    //           this.emps = resp.obj;
    //           console.log(this.emps)
    //         } else {
    //           this.$message.error(resp.msg);
    //         }
    //       })
    //       .catch((error) => {
    //         console.error(error);
    //         this.loading = false;
    //       });
    // },

    //查看详情
    showInfoItem(data) {
      const {id, name, stuNumber} = data;

      this.$router.push({
        path: `/teacher/stuProgramRecord`,
        query: {keyword: id, keyname: name, studentNumber: stuNumber},
      });
    },

  },
};
</script>

<style>
.upload-demo {
  display: inline-block;
  margin-right: 10px;
}

.el-loading-spinner {
  font-size: 20px;
  /*font-weight: bold;*/
}

.el-loading-spinner .el-loading-text {
  font-size: 18px;
}

.el-tooltip__popper {
  max-width: 750px;
}

.table-with-shadow {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.el-date-table td.is-disabled {
  visibility: hidden;
}

.red-cell {
  color: red;
}

/*选择日期，年份的隐藏 */
.el-date-editor {
  border: none;
  box-shadow: none;
}

.select_div_input {
  /*margin-left:3px;*/
  width: 80%;
  height: 32px;
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
  overflow: hidden;
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

.showInfo_dialog .el-form-item {
  margin-bottom: 5px;
}

.isMust {
  position: absolute;
  color: #f56c6c;
  top: 2px;
  left: -100px;
}

.el-form-item label {
  text-align: justify;
}

/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
#selectItem ul {
  list-style: none;
  /* height: 100px; */
  width: 200px;
  /* background: red; */
  /* border: 1px solid #eee; */
  /* visibility: hidden; */
}

#selectItem ul li {
  height: 28px;
  line-height: 28px;
  padding-left: 10px;
  cursor: pointer;
}

.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

/* .slide-fade-leave-active for below version 2.1.8 */
.slide-fade-enter,
.slide-fade-leave-to {
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
  border: 3px solid rgba(255, 255, 255, 0.4);
  background-color: rgba(0, 0, 0, 0.5);
}

.custom-confirm{
  width: 500px;
}
</style>
