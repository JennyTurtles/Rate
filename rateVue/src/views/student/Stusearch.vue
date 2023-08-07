<template>
  <div>
   <div>
    <el-button  type="primary" @click="addVisible=true">添加活动</el-button>
   </div>
   <div>
    <el-dialog
        title="添加活动"
        :visible.sync="addVisible"
        width="50%"
        :before-close="handleClose">
     <el-form :model="addActForm">
      <el-form-item label="活动名称">
       <el-autocomplete
           style="width: 90%"
           v-model="addActForm.activityName"
           :fetch-suggestions="querySearchAsync"
           placeholder="请输入活动名称"
           @change="allowSubmit=false"
           @select="handleSelect">
       </el-autocomplete>
      </el-form-item>
      <el-form-item label="选手编号">
       <el-input style="width: 90%" v-model="addActForm.code" placeholder="请输入编号"></el-input>
      </el-form-item>
     </el-form>
     <span slot="footer" class="dialog-footer">
      <el-button @click="addVisible = false">取 消</el-button>
     <el-tooltip style="margin-left: 10px" class="item" effect="dark" content="请先从下拉框选择一个活动后再提交" placement="top-start" :disabled='allowSubmit'>
      <span>
       <el-button type="primary" @click="addActSelf" :disabled="!allowSubmit">确 定</el-button>
      </span>
      </el-tooltip>
    </span>
    </el-dialog>
   </div>
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
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
            prop="id"
            fixed
            align="center"
            label="编号"
            width="50"
        >
        </el-table-column>
        <el-table-column
            prop="name"
            fixed
            align="left"
            label="活动名称"
        >
        </el-table-column>
        <el-table-column
            prop="startDate"
            label="开始时间"
            align="center"
            width="130"
        >
        </el-table-column>
        <el-table-column
            prop="scoreItemCount"
            label="评分项数"
            align="center"
            width="80"
        >
        </el-table-column>
        <el-table-column
            prop="score"
            label="总分"
            align="center"
            width="75"
        >
        </el-table-column>
        <el-table-column align="center" width="650" label="操 作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView_show(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情
            </el-button
            >
            <el-button
                @click="showInfoItem(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >信息项设置
            </el-button
            >
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

    <el-dialog
        :title="title_show"
        :visible.sync="dialogVisible_show"
        width="25%"
        center
    >
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          :rules="rules"
          ref="empForm"
          style="margin-left: 60px"
      >
        <el-form-item label="分组数:" prop="groupCount">
          <span>{{ emp.groupCount }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="专家数:" prop="expertCount">
          <span>{{ emp.expertCount }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="选手数:" prop="participantCount">
          <span>{{ emp.participantCount }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="备 注:">
          <span>{{ emp.comment }}</span>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible_show = false"
        >关 闭</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";

export default {
  name: "SalSearch",
  data() {
    return {
     allowSubmit: false,
     addActForm: {
      activityName: '',
      activityID: '',
      code: ''
     },
      actName: "",
      addVisible: false,
      labelPosition: "left",
      title: "",
      text: "",
      title_show: "",
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      emps: [],
      loading: false,
      dialogVisible_show: false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      positions: [],
      emp: {
        id: null,
        institutionID: null,
        name: null,
        startDate: "2022/02/02",
        scoreItemCount: "0",
        score: "100",
        groupCount: "0",
        expertCount: "0",
        participantCount: "0",
        comment: "javaboy",
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        name: [{required: true, message: "请输入活动名", trigger: "blur"}],
        startDate: [
          {required: true, message: "请输入活动时间", trigger: "blur"},
        ],
        scoreItemCount: [
          {
            required: true,
            type: "number",
            message: "请输入正确数据",
            trigger: "blur",
            transform: (value) => Number(value),
          },
        ],
        comment: [{required: true, message: "请输入备注", trigger: "blur"}],
      },
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  created() {
  },
  mounted() {
    this.initEmps();
  },
  methods: {
   addActSelf() {
    this.postRequest1('/participants/basic/addActSelf?studentID='+this.user.id+'&activityID='+this.addActForm.activityID+'&code='+this.addActForm.code)
        .then(response => {
         if (response.status === 200) {
          this.$message({
           message: response.msg,
           type: 'success'
          });
          this.addVisible = false;
          this.allowSubmit = false;
          this.addActForm = {}
          this.initEmps();
         }
        });
   },
   querySearchAsync(queryString, cb) {
    if (queryString.length < 2) {
     return cb([]);
    }
    this.getRequest('/activities/basic/searchByName?name='+queryString)
        .then(response => {
         let results = response.obj;
         // 遍历result，把里面的name赋值给value
         results.forEach(result => {
          result.value = result.name;
         });
         // 调用回调函数返回搜索建议
         cb(results);
        })
        .catch(error => {
         console.error(error);
        });
   },
   handleSelect(item) {
    // 当用户选择一个活动时触发
    this.allowSubmit = true
    this.addActForm.activityName = item.name;
    this.addActForm.activityID = item.id;
   },
   handleClose(done) {
    // 在 dialog 关闭之前的回调，关闭 dialog 之前可以进行一些状态的清理
    this.addVisible = false
    this.addActForm.activityName = '';
    this.addActForm.code = '';
    this.addActForm.activityID = '';
    done();
   },
    rowClass() {
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    initEmps() {
      this.loading = true;
      let url = "/activities/basic/getActivityOfStudent?page=" + this.page + "&size=" + this.size + "&studentID=" + this.user.id;
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          for(var i = 0;i < resp.length; i++){
            var time = new Date(resp[i].startDate)
            var year = time.getFullYear()
            var month = time.getMonth() + 1
            var date = time.getDate()
            if(month < 10){
              month = "0" + month
            }
            if(date < 10){
              date = "0" + date
            }
            resp[i].startDate = year + "-" + month + "-" + date
          }
          this.emps = resp;
          this.total = resp.length;
        }
      });
    },

    showInfoItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/student/infos",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
  },
};
</script>

<style>
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>
