<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-input
              placeholder="请输入活动名称进行搜索，可以直接回车搜索..."
              prefix-icon="el-icon-search"
              clearable
              @clear="searchEmps"
              style="width: 350px; margin-right: 10px"
              v-model="keyword"
              @keydown.enter.native="searchEmps"
              :disabled="showAdvanceSearchView"
          ></el-input>
          <el-button
              icon="el-icon-search"
              type="primary"
              @click="searchEmps"
              :disabled="showAdvanceSearchView"
          >
            搜索
          </el-button>
          <el-button
              icon="el-icon-refresh"
              type="primary"
              @click="initEmps"
              :disabled="showAdvanceSearchView"
          >
            重置
          </el-button>
          <el-button type="primary" icon="el-icon-plus" @click='showAddEmpView' v-show="mode === 'admin' || mode === 'adminSub'">
            添加活动
          </el-button>

          <span style="margin-left: 20px" v-show="mode === 'secretarySub'">当前管理的是： {{actName}} {{groupName}} </span>
        </div>
        <div style="margin-left: auto">
          <el-button
              v-show="mode === 'adminSub' || mode === 'secretarySub'"
              type="primary"
              icon="el-icon-arrow-left"
              @click="back">
            返回
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
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
      >
        <el-table-column type="selection" width="35px"></el-table-column>

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
            v-if="mode==='admin'"
        >
        </el-table-column>
        <!-- width="70" -->
        <el-table-column
            prop="score"
            label="总分"
            align="center"
            width="75"
        >
        </el-table-column>
        <el-table-column
            prop="groupName"
            label="组名"
            align="center"
            width="75"
            v-if="mode==='secretary'"
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
                @click="showEditEmpView(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
            >编辑
            </el-button
            >
            <el-button
                @click="showScoreItem(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >评分项设置
            </el-button
            >
            <el-button
                @click="showScoreItem(scope.row)"
                v-show="mode==='secretary' || mode==='secretarySub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >评分项查看
            </el-button
            >
            <el-button
                @click="showInfoItem(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >信息项设置
            </el-button
            >
            <el-button
                @click="showInfoItem(scope.row)"
                v-show="mode==='secretary' || mode==='secretarySub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >信息项查看
            </el-button
            >
            <el-button
                @click="showTotalItem(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >成绩查看设置
            </el-button
            >
            <el-button
                @click="showGroupmanagement(scope.row)"
                v-show="mode !== 'secretary' && mode !== 'secretarySub' || scope.row.requireGroup === true"
                style="padding: 4px"
                size="mini"
                icon="el-icon-s-operation"
                type="primary"
                plain
            >分组管理
            </el-button
            >
            <el-button
                @click="showInsertmanagement(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >选手管理
            </el-button
            >
             <el-button
                      @click="showGroups(scope.row)"
                      v-show="mode === 'admin' || mode === 'adminSub' "
                      style="padding: 4px"
                      size="mini"
                      icon="el-icon-tickets"
                      type="primary"
                      plain
              >专家管理
              </el-button
              >
            <el-button
                v-show="mode === 'secretary'"
                @click="showParticipants(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >选手管理
            </el-button
            >
            <el-button
                @click="showScore(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >分数统计
            </el-button
            >
              <el-button
                      @click="assignPE(scope.row)"
                      v-show="mode === 'secretarySub' &&scope.row.requireGroup === false"
                      style="padding: 4px"
                      size="mini"
                      icon="el-icon-tickets"
                      type="primary"
                      plain
              >分配选手和专家
              </el-button
              >
            <el-button
                @click="exportEx(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >{{text}}导出专家打分
            </el-button
            >
            <el-button
                @click="showFinalScore(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >查看选手成绩
            </el-button
            >
              <el-button
                      @click="showSubActivity(scope.row)"
                      style="padding: 4px"
                      size="mini"
                      icon="el-icon-plus"
                      type="primary"
                      plain
                      v-show="mode !== 'secretarySub' && mode !== 'adminSub'"
              >子活动管理
              </el-button
              >
            <el-button
                @click="endEmp(scope.row)"
                v-show="mode==='admin'|| mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-circle-close"
                plain
                :disabled="scope.row.status=='close'"
            >{{ scope.row.status == 'open' ? '结束活动' : '已结束' }}
            </el-button
            >
            <el-button
                @click="deleteEmp(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >删除
            </el-button
            >
          </template>
        </el-table-column>
        <el-table-column
            v-if="mode === 'adminSub'"
            prop="group"
            label="是否分组"
            align="center"
            width="75"
        >
            <template slot-scope="scope">
                <el-checkbox v-model="scope.row.requireGroup"
                             @change="changeCheckGroup(scope.row)"></el-checkbox>
            </template>
        </el-table-column>
<!--          <el-table-column-->
<!--                  v-if="mode === 'secretarySub'"-->
<!--                  prop="group"-->
<!--                  label="是否分组"-->
<!--                  align="center"-->
<!--                  width="75"-->
<!--          >-->
<!--              <template slot-scope="scope">-->
<!--                  <el-checkbox v-model="scope.row.requireGroup"-->
<!--                               disabled-->
<!--                               @change="changeCheckGroup(scope.row)"></el-checkbox>-->
<!--              </template>-->
<!--          </el-table-column>-->
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
          ref="empForm"
      >
        <el-form-item label="活动名称:" prop="name">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.name"
              placeholder="请输入单位名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="开始时间:" prop="startDate">
          <div class="block">
            <el-date-picker
                v-model="emp.startDate"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期和时间">
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="备 注: " prop="comment">
          <!-- <textarea v-model="emp.comment" placeholder="备注" style="height:100px;width: 350px"></textarea> -->
          <el-input
              type="textarea"
              :rows="2"
              v-model="emp.comment"
              placeholder="备注"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="存在子活动: " prop="comment">
            <el-checkbox v-model="haveSub"></el-checkbox>
        </el-form-item>

        <!-- <el-form-item label="评分项数:" prop="scoreItemCount">
                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit" v-model="emp.scoreItemCount"
                          placeholder="评分项数"></el-input>
              </el-form-item> -->
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp">确 定</el-button>
      </span>
    </el-dialog>

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
        <el-form-item label="分组数:" v-show="mode === 'admin'" prop="groupCount">
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
import da from "element-ui/src/locale/lang/da";
import fa from "element-ui/src/locale/lang/fa";

export default {
  name: "ActManage",
  props:["mode","activityID","actName","groupName","groupID"], // 四个地方复用组件
  data() {
    return {
      haveSub:false,
      startDate: '',
      experts:'',
      participates:'',
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
      dialogVisible: false,
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
        startDate: "",
        scoreItemCount: "0",
        score: "100",
        groupCount: "0",
        expertCount: "0",
        participantCount: "0",
        comment: "javaboy",
          parentId: null,
          requireGroup: true,
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
      fa() {
          return fa
      },
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
      assignPE(data) {
          console.log(data)
          const _this = this;
          _this.$router.push({
              path: "/Expert/EassignPE",
              query: {
                  activityIDParent: this.$route.query.id,
                  activityID: data.id,
                  groupIDParent: this.$route.query.groupID, // 这里有问题
                  groupID: data.groupID,
                  mode:this.mode
              }
          })
      },
    changeCheckGroup(row){
        this.postRequest("/activities/basic/changeRequireGroup?activityID="+row.id+"&requireGroup="+(row.requireGroup?1:0)).then(res=>{
            if(res.status === 200){
                this.$message({
                    type: 'success',
                    message: '修改成功!'
                });
            }else{
                this.$message({
                    type: 'error',
                    message: '修改失败!'
                });
            }
        })
    },
    rowClass() {
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    /** 查询角色列表 */
    onError(err, file, fileList) {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
    },
    onSuccess(response, file, fileList) {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
      this.initEmps();
    },
    beforeUpload() {
      this.importDataBtnText = "正在导入";
      this.importDataBtnIcon = "el-icon-loading";
      this.importDataDisabled = true;
    },
    // exportData() {
    //   window.open("/employee/basic/export", "_parent");
    // },
    emptyEmp() {
      this.emp = {
        id: null,
        startDate: null,
        name: "",
        scoreItemCount: "0",
        comment: "活动备注example：关于xxx的活动",
      };
    },
    exportEx(data){
      this.loading=true;
      this.text='正在';
      Message.success("正在导出");
      let url = '/participants/basic/export?activityID=' + data.id;
      window.open(url, "_parent");
      this.loading=false;
      this.text='';
    },
    showEditEmpView(data) {
      this.title = "编辑单位信息";
      this.emp = data;
      this.haveSub = data.haveSub === 1;
      this.dialogVisible = true;
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
    },
    // showEditEmpView_show(row) {
    //   let routeUrl = this.$router.resolve({
    //     path:"/teacher/tperact/InformationDetails",
    //     query: {
    //       activityID: this.activityID,
    //       IDNumber: row.student.idnumber,
    //     },
    //   })
    //   window.open(routeUrl.href)
    // },
    showScore(data){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/score",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          mode:this.mode,
          backID:this.activityID,
        },
      });
    },
    deleteEmp(data) {
      data.institutionID = this.user.institutionID;
      this.getRequest("/activities/basic/check?id=" + data.id).then(res => {
        if (res == true) {
          this.$confirm(
              "此操作将永久删除【" + data.name + "】, 是否继续?",
              "提示",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }
          ).then(() => {
            this.postRequest("/activities/basic/delete", data).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          });
        } else {
          this.$confirm(
              "尚存在与活动" + data.name + "相关的信息，所以放入回收站，如要永久删除，请先删除分组和评分项等相关信息，是否放入回收站?",
              "提示",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }
          ).then(() => {
            this.postRequest("/activities/basic/predelete", data).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          });
        }
      })
    },
    endEmp(data) {
      data.institutionID = this.user.institutionID;
      this.$confirm(
          "此操作将永久停止活动【" + data.name + "】, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      ).then(() => {
        this.postRequest("/activities/basic/end", data).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        });
      });
    },
    doAddEmp() {
        if(this.mode === 'adminSub')
            this.emp.parentID = this.activityID;
        this.emp.haveSub = this.haveSub ? 1 : 0
      if (this.emp.id) {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.institutionID = this.user.institutionID;
            const _this = this;
            this.postRequest("/activities/basic/update", _this.emp).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible = false;
                      this.$message({
                          type: 'success',
                          message: '修改成功!'
                      });
                    this.initEmps();
                  }
                }
            );
          }
        });
      } else { //添加活动
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.institutionID = this.user.institutionID;
            const _this = this;
            this.postRequest("/activities/basic/insert", _this.emp).then(
                (resp) => {
                  if (resp) {
                      this.$message({
                          type: 'success',
                          message: '添加成功!'
                      });
                    this.dialogVisible = false;
                    this.initEmps();
                  }
                }
            );
          }
        });
      }
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    showAddEmpView() {
      this.emptyEmp();
      this.title = "添加活动";
      this.dialogVisible = true;
    },
    initEmps() { // 在此适配不同的组件
      this.loading = true;
      if (this.mode === "admin"){ // 管理员活动管理
        let url = "/activities/basic/?page=" + this.page + "&size=" + this.size + "&institutionID=" + this.user.institutionID;
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            this.emps = resp.data;
            this.total = resp.total;
          }
        });
      }else if (this.mode === "secretary"){ // 秘书活动管理
        const id = JSON.parse(localStorage.getItem("user")).id
        this.getRequest("/secretary/getAct?teacherID="+id).then((resp)=>{
          this.loading = false;
          if (resp) { // 后续再包装成函数
            this.emps = resp.obj;
            this.total = this.emps.length;
          }
        })
      }else if (this.mode === "adminSub"){ // 管理员子活动管理
        this.getRequest("/activities/basic/sub?activityID="+this.activityID).then((resp)=>{
          this.loading = false;
          this.emps = resp.obj;
          for (let i = 0; i < this.emps.length; i++) {
              this.emps[i].requireGroup = this.emps[i].requireGroup === 1
          }
          this.total = this.emps.length
        })
      }else if (this.mode === "secretarySub"){ // 秘书子活动管理
        this.loading = false;
        // 获取当前组内的专家和学生，待修改
        this.getRequest("/secretary/getMember?activityID="+this.activityID+"&groupID="+this.groupID).then((resp)=>{
          this.experts = resp.obj[1]
          this.participates = resp.obj[0]
          // 获取所有的子活动
          this.getRequest("/activities/basic/sub?activityID="+this.activityID).then((resp)=>{
            this.loading = false;
            this.emps = resp.obj;
              for (let i = 0; i < this.emps.length; i++) {
                  this.emps[i].requireGroup = this.emps[i].requireGroup === 1
              }
            for (let i = 0; i < this.emps.length; i++) {
              this.emps[i].participantCount =  this.participates.length
              this.emps[i].expertCount = this.experts.length
            }
            this.total = this.emps.length
          })
        })
      }

    },
    showGroupmanagement(data) {
      const _this = this;
      if (this.mode === "admin"){
        _this.$router.push({
          path: "/ActivitM/table",
          query: {
            keywords: data.id,
            keyword_name: data.name,
            mode:this.mode,
          },
        });
      }else if (this.mode === "secretary"){
        _this.$router.push({
          path: "/ActivitM/table",
          query: {
            keywords: data.id,
            keyword_name: data.name,
            groupID:data.groupID,
            mode:this.mode,
          },
        });
      }else if (this.mode === "adminSub") {
          _this.$router.push({
              path: "/ActivitM/table",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  groupID: this.groupID,
                  mode: this.mode,
                  backID: this.activityID,
              },
          });
      }else if (this.mode === "secretarySub") {
          _this.$router.push({
              path: "/ActivitM/table",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  groupID: this.groupID, // 用于读取主活动的组内的选手
                  groupName: this.groupName,
                  mode: this.mode,
                  backID: this.activityID,
                  backActName: this.actName,
              },
          });
      }

      // console.log(data)
      // if (this.mode === "admin"){
      //   _this.$router.push({
      //     path: "/ActivitM/table",
      //     query: {
      //       keywords: data.id,
      //       keyword_name: data.name,
      //     },
      //   });
      // }
    },
    showInsertmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/group",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          groupID:this.groupID,
          mode:this.mode,
          backID:this.activityID,
        },
      });
    },
    showSubActivity(data) {
      const _this = this;
      if (this.mode === "admin")
        _this.$router.push({query :{id:data.id}, path: "/ActivitM/SubActManage",});
      else if (this.mode === "secretary" || this.mode === "secretarySub"){
        _this.$router.push({
          query :{id:data.id,actName:data.name,groupName:data.groupName,groupID:data.groupID},
          path: "/secretary/SubActManage",});
      }

    },
    showteachermanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          mode:this.mode,
          backID:this.activityID,
        },
      });
    },
    showFinalScore(data){
      const _this = this;
        console.log(data)
      if (this.mode === "admin" || this.mode === "adminSub"){
          _this.$router.push({
              path: "/ActivitM/final",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  mode:this.mode,
                  backID:this.activityID,
              },
          });
      }else if (this.mode === "secretary"){
          _this.$router.push({
              path: "/ActivitM/final",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  mode:this.mode,
                  groupName:data.groupName,
                  groupID:data.groupID,
              },
          });
      }else if (this.mode === "secretarySub") {
          _this.$router.push({
              path: "/ActivitM/final",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  mode:this.mode,
                  backGroupID:this.groupID,
                  backActID:this.activityID,
                  backActName:this.actName,
              },
          });
      }

    },
    showScoreItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/month",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          mode:this.mode,
          backID:this.activityID
        },
      });
    },
    showInfoItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/infos",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          mode:this.mode,
          backID:this.activityID,
        },
      });
    },
    showTotalItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/total",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          backID:this.activityID,
          mode:this.mode,
        },
      });
    },
    searchEmps() {
      this.loading = true;
      const _this = this;
      //let url =
      this.getRequest(
          "/activities/basic/search?company=" +
          this.keyword +
          "&page=" +
          this.page +
          "&size=" +
          this.size +
          "&institutionID=" + this.user.institutionID
      ).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
        }
      });
    },
      back(){
          const _this = this;
          var url = ""
          var query = ""
          if (this.mode === 'adminSub'){
            {
                url = "/ActivitM/search"
            }
          }else if (this.mode === "secretarySub"){
              url = "/secretary/ActManage"
          }
          _this.$router.push({
              path: url,
          });
      },
      showGroups(data) {
          const _this = this;
          _this.$router.push({
              path: "/ActivitM/sobcfg",
              query: {
                  activityID: data.id,
                  keywords: data.id,
                  keyword_name: data.name,
                  keywords_name:this.keywords_name,
                  groupID: data.groupID,
                  backID: this.activityID,
                  mode:this.mode
              }
          })
      },
     showParticipants(data){
         const _this = this;
         _this.$router.push({
             path: "/participantsM",
             query: {
                 activityID: data.id,
                 keyword_name: data.name,
                 keywords_name:this.keywords_name,
                 groupID: data.groupID,
                 mode:this.mode
         }
       })
     }
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
