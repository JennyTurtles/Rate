<template>
  <div>

    <div>
      <div style="display: flex;justify-content: space-between;;margin: 15px 0 ;">
        <div>
          <el-input
              placeholder="请输入获奖人姓名进行搜索，可以直接回车搜索..."
              prefix-icon="el-icon-search"
              clearable
              @clear="searchEmps"
              style="width: 350px; margin-right: 10px"
              v-model="keyword"
              @keydown.enter.native="searchEmps"
              :disabled="showAdvanceSearchView"
          ></el-input>
          <el-button icon="el-icon-search" type="primary" @click="searchEmps" :disabled="showAdvanceSearchView">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" type="primary" @click="initEmps" :disabled="showAdvanceSearchView">
            重置
          </el-button>
        </div>

        </div>
      </div>

    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          stripe
          border
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%">
        <el-table-column
            type="selection"
            min-width="3%">
        </el-table-column>
        <el-table-column
            prop="id"
            fixed
            align="left"
            label="编号"
            width="75">
        </el-table-column>
        <el-table-column
            prop="name"
            fixed
            align="left"
            label="获奖人"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="awardname"
            fixed
            align="left"
            label="获奖名称"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="year"
            fixed
            align="left"
            label="年份"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="unit"
            fixed
            align="left"
            label="所属单位"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="rank"
            fixed
            align="left"
            label="排名"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="state"
            fixed
            align="left"
            label="审核状态"
            min-width="75">
        </el-table-column>

        <el-table-column align="center" min-width="120" label="操 作">
          <template slot-scope="scope">

            <el-button
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
            >审核通过</el-button>

            <el-button
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >打回</el-button>

            <el-button
                @click="showAddView(scope.row)"
                style="padding: 4px"
                size="mini"
                type="primary"
                icon="el-icon-edit"
                plain
            >留言</el-button>

            <el-button
                @click="showAll(scope.row)"
                style="padding: 4px"
                size="mini"
                type="primary"
                icon="el-icon-edit"
                plain
            >查看具体信息</el-button>

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

    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" center>
      <el-form
          :label-position="labelPosition"
          label-width="100px"
          :model="emp"
          :rules="rules"
          ref="empForm">

        <el-form-item label="审核意见:" prop="studentnumber">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.awardname"
              placeholder="请输入审核意见"
          ></el-input>
        </el-form-item>
        <el-form-item label="留言:" prop="name">
          <el-input
              type="textarea"
              prefix-icon="el-icon-edit"
              v-model="emp.name"
              placeholder="请输入留言"
          ></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
             <el-button @click="dialogVisible = false">取 消</el-button>
             <el-button type="primary" @click="doAddEmp">确 定</el-button>
          </span>
    </el-dialog>

    <!-- Table -->
    <el-dialog title="具体成果信息" :visible.sync="dialogTableVisible">
      <el-table
          :data="empss"
          stripe
          border
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%">
        <el-table-column
            prop="name"
            fixed
            align="left"
            label="获奖人"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="awardname"
            fixed
            align="left"
            label="获奖名称"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="year"
            fixed
            align="left"
            label="年份"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="unit"
            fixed
            align="left"
            label="所属单位"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="rank"
            fixed
            align="left"
            label="排名"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="total"
            fixed
            align="left"
            label="总人数"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="member"
            fixed
            align="left"
            label="成员"
            min-width="75">
        </el-table-column>
        <el-table-column
            prop="state"
            fixed
            align="left"
            label="审核状态"
            min-width="75">
        </el-table-column>
      </el-table>
    </el-dialog>

  </div>

</template>

<script>
export default {
  name: "StuView",
  data() {
    return{
      title: '',
      importDataBtnText: '导入数据',
      importDataBtnIcon: 'el-icon-upload2',
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      emps: [],
      empss:[],
      loading: false,
      dialogVisible: false,
      dialogTableVisible:false,
      total: 0,
      page: 1,
      keywords: '',
      keyword: '',
      keyword_name: '',
      size: 10,
      positions: [],

      emp: {
        awardname:null,
        name: null,
        year:null,
        member:null,
        unit:null,
        total:null,
        rank:null,
        awardID:null,
        state:null,
      },
      rules: {
        studentnumber: [{required: true, message: '请输入意见', trigger: 'blur'}],
        name: [{required: true, message: '请输入留言', trigger: 'blur'}],
      },
      defaultProps: {
        children: 'children',
        label: 'name',
      },
    }
  },
  computed: {
    user() {
      return this.$store.state.currentHr;//object信息
    }
  },

  created() {
  },

  mounted() {
    this.initEmps();
    this.keywords = this.$route.query.keywords;
    this.keyword_name = this.$route.query.keyword_name;
  },

  methods: {
    /** 查询角色列表 */

    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },

    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps('advanced');
    },

    initEmps() {
      this.loading = true;
      let url = "/award/basic/?page=" + this.page + "&size=" + this.size;
      console.log(url);
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          console.log(resp);
          this.emps = resp.data;
          this.total = resp.total;
        }
      });
    },

    searchEmps() {
      this.loading = true;
      console.log('---------',this.keyword);
      const _this = this;
      //let url =
      this.getRequest(
          "/student/basic/search?name=" +
          this.keyword +
          "&page=" +
          this.page +
          "&size=" +
          this.size
      ).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
        }
      });
    },

    showAddView() {
      this.emptyEmp();
      this.title = "留言";
      this.dialogVisible = true;
    },

    showEditView(data) {
      this.initPositions();
      this.title = "查看留言";
      this.emp = data;
      this.dialogVisible = true;
    },

    showAll(data){
      this.initEmpss(),
      this.title = "成果具体信息";
      this.emp = data;
      this.dialogTableVisible = true;
    },

    initEmpss() {
      this.loading = true;
      let url = "/award/basic/get?page=" + this.page + "&size=" + this.size;
      console.log(url);
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          console.log(resp);
          this.empss = resp.data;
          this.total = resp.total;
        }
      });
    },

    initPositions(){
    },

    emptyEmp() {
      this.emp = {
        studentnumber:null,
        name: "",
        telephone:null,
        email: ""
      }
    },

    doAddEmp() {
      if (this.emp.id) {
        console.log(this.emp);
        this.$refs['empForm'].validate(valid => {
          if (valid) {
            const _this = this
            this.postRequest("/student/basic/update",_this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        });
      } else {
        this.$refs['empForm'].validate(valid => {
          if (valid) {
            this.emp.institutionID=this.user.id
            console.log(this.emp);
            console.log(this.user.id);
            const _this = this
            this.postRequest("/student/basic/insert", _this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        });
      }
    },

    deleteEmp(data) {
      console.log(data)
      this.$confirm('此操作将永久删除【' + data.company + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.postRequest("/student/basic/delete",data).then(resp => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        })
      })
    },

    showComment(){

    },



  }
}
</script>

<style scoped>
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>
