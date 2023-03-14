<template>
  <div>
    {{ ACNAME }}活动 {{ keywords_name }} 专家名单
    <div style="display: flex; justify-content: left;margin-top:10px">
      <div>
        <span style="font-weight:600;">导入新数据</span> 第一步：
        <el-button
            type="primary"
            @click="exportMo"
            icon="el-icon-upload"
            style="margin-right: 8px"
        >
          下载模板
        </el-button>
        第二步：
        <el-upload
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            :on-error="onError"
            :disabled="importDataDisabled"
            style="display: inline-flex; margin-right: 8px"
            :action="UploadUrl()"
        >
          <el-button
              :disabled="importDataDisabled"
              type="primary"
              :icon="importDataBtnIcon"
          >
            {{ importDataBtnText }}
          </el-button>
        </el-upload>

      </div>
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">

          返回
        </el-button>
      </div>
      <!--      <div style="margin-left:auto;">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>-->
    </div>
    <div><br/>专家导入后的初始用户名为手机号，密码为身份证后六位<br/>单元格中内容双击后可编辑</div>
    <div style="margin-top: 10px">
      <el-table
          ref="multipleTable"
          :data="hrs"
          stripe
          border
          v-loading="loading"
          @cell-dblclick="tabClick"
          :row-class-name="tableRowClassName"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
      >
        <el-table-column prop="name" fixed label="专家姓名" min-width="3%">
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex &&
                  tabClickLabel === '专家姓名'
                "
                v-focus
                v-model.trim="scope.row.name"
                maxlength="20"
                size="mini"
                @focus="beforehandleEdit(scope.$index,scope.row,'name')"
                @change="handleEdit(scope.$index,scope.row,'name')"
                @blur="inputBlur"
            />

            <span class="spanscoped" v-else>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="idnumber"
            label="证件号码"
            align="center"
            min-width="4%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex &&
                  tabClickLabel === '证件号码'
                "
                v-focus
                v-model.trim="scope.row.idnumber"
                maxlength="18"
                size="mini"
                @focus="beforehandleEdit(scope.$index,scope.row,'idnumber')"
                @change="handleEdit(scope.$index,scope.row,'idnumber')"
                @blur="inputBlur"
            />

            <span class="spanscoped" v-else>{{ scope.row.idnumber }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="username"
            align="left"
            label="用户名"
            min-width="3%"
        >
          <template slot-scope="scope">
            <!--            <span v-if="scope.row.index === tabClickIndex && tabClickLabel === '专家姓名'">
              <el-input v-model="scope.row.username" size="mini" @blur="inputBlur"/>
            </span>
            <span v-else>{{ scope.row.username }}</span>-->
            <span>{{ scope.row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="phone"
            align="center"
            label="电话"
            min-width="3%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '电话'
                "
                v-focus
                v-model.trim="scope.row.phone"
                maxlength="15"
                size="mini"
                @focus="beforehandleEdit(scope.$index,scope.row,'phone')"
                @change="handleEdit(scope.$index,scope.row,'phone')"
                @blur="inputBlur"
            />
            <span class="spanscoped" v-else>{{ scope.row.phone }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" align="left" min-width="3%">
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '邮箱'
                "
                v-focus
                v-model.trim="scope.row.email"
                size="mini"
                maxlength="25"
                @focus="beforehandleEdit(scope.$index,scope.row,'email')"
                @change="handleEdit(scope.$index,scope.row,'email')"
                @blur="inputBlur"
            />
            <span class="spanscoped" v-else>{{ scope.row.email }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="byParticipant"
            label="设置为秘书"
            align="center"
            min-width="2%"
        >
          <template slot-scope="scope">
            <el-checkbox
                :true-label="1"
                :false-label="0"
                v-model.trim="scope.row.isSecretary"
                @change="UpdateCheckbox(scope.row)"
            ></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            min-width="3%"
            label="已提交评分"
            prop="finished"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.finished == 0" style="color:red">否</span>
            <span v-else>是</span>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="10%" label="操作">
          <template slot-scope="scope">
            <el-button
                @click="save(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-collection"
                type="primary"
                plain
            >保存
            </el-button
            >
            <el-button
                @click="reset_password(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-refresh-right"
                type="primary"
                plain
            >重置密码
            </el-button
            >
            <el-button
                @click="jumperInToS(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-refresh-right"
                type="primary"
                plain
            >查看专家评分
            </el-button>
            <el-button
                @click="changeFinished(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-refresh-right"
                type="primary"
                :disabled="scope.row.finished==0"
                plain
            >退回评分
            </el-button>
            <el-button
                @click="Delete_ExActivity(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0; display: flex; justify-content: left">
        <!-- <div>
          <el-button
            @click="toggleSelection()"
            type="primary"
            icon="el-icon-close"
            >取消选择</el-button
          >
        </div> -->
        <!--        <div style="margin-left: 8px">
          <el-button @click="handleAddDetails()" type="primary" icon="el-icon-plus"
          >新增</el-button
          >
        </div>-->
        <div style="margin-left: 8px">
          <!--          <el-button @click="save()" type="primary" icon="el-icon-check"
          >保存</el-button
          >-->
        </div>
        <div style="margin-left: auto">
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

      <el-dialog :title="err_title "  :visible.sync="err_dialogVisible_edit" width="40%" >
        <div style="background-color: bisque; padding-left: 5px;padding-top: 5px;font-size: 20px;" v-for="i in err_msg">{{ i }}<hr>
        </div>
        <span slot="footer" class="dialog-footer" >
          <el-button @click="err_dialogVisible_edit = false" type="warning">关闭</el-button>
        </span>
      </el-dialog>

      <!--弹窗-->
      <el-dialog :title="title" :visible.sync="dialogVisible_edit" width="40%">
        <div>
          已经存在于其他组，是否需要调换？
          <el-table
              ref="multipleTable"
              :data="unsureinfo"
              stripe
              border
              v-loading="loading"
              @cell-click="tabClick"
              :row-class-name="tableRowClassName"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.08)"
              style="width: 100%"
          >
            <el-table-column prop="name" fixed label="专家姓名" min-width="5%">
              <template slot-scope="scope">
                <span>{{ scope.row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column
                prop="idnumber"
                label="证件号码"
                align="left"
                min-width="5%"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.idnumber }}</span>
              </template>
            </el-table-column>
            <el-table-column
                prop="oldgroupname"
                align="left"
                label="已经存在组名"
                min-width="5%">
              <template slot-scope="scope">
                <span>{{ scope.row.oldgroupname }}</span>
              </template>
            </el-table-column>
            <!--            <el-table-column
                          prop="username"
                          align="left"
                          label="用户名"
                          min-width="5%"
                        >
                          <template slot-scope="scope">
                            <span>{{ scope.row.username }}</span>
                          </template>
                        </el-table-column>
                        <el-table-column
                          prop="phone"
                          align="left"
                          label="电话"
                          min-width="5%"
                        >
                          <template slot-scope="scope">
                            <span>{{ scope.row.phone }}</span>
                          </template>
                        </el-table-column>-->
            <el-table-column
                prop="email"
                label="邮箱"
                align="left"
                min-width="3%">
              <template slot-scope="scope">
                <span>{{ scope.row.email }}</span>
              </template>
            </el-table-column>
            <el-table-column
                align="left"
                min-width="15%"
                label="操作">

              <template slot-scope="scope">

                <el-button @click="update(scope.row)" style="padding: 4px" size="mini" icon="el-icon-check"
                           type="primary"
                           plain>调换到本组：{{ keywords_name }}
                </el-button>

              </template>
            </el-table-column>
          </el-table>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible_edit = false">关闭</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {Message} from 'element-ui'

export default {
  name: "SalSobCfg",
  data() {
    return {
      //当前焦点数据
      currentfocusdata: "",
      searchValue: {
        compnayName: null,
      },
      title: "",
      importDataBtnText: "导入专家",
      importDataBtnIcon: "el-icon-plus",
      importDataDisabled: false,
      page: 1,
      tabClickIndex: null, // 点击的单元格
      tabClickLabel: "", // 当前点击的列名
      keywords: "",
      activitydata: [],
      keywords_name: "",
      ACNAME:"",
      groupID: '',
      size: 10,
      total: 0,
      loading: false,
      hrs: [],
      unsureinfo: [],
      dialogVisible_edit: false,
      selectedRoles: [],
      allroles: [],
      hr_info: {
        id: null,
        compnayName: null,
        institutionID: null,
        name: "javaboy",
        phone: "18568128889",
        email: "123@126.com",
        enabled: 1,
        username: "test123",
        password: "123",
        role: 1,
        comment: null,
      },
      hr_info_new: {
        id: null,
        compnayName: null,
        institutionID: null,
        name: "javaboy",
        phone: "18568128889",
        email: "123@126.com",
        enabled: 1,
        username: "test123",
        password: "123",
        role: 1,
        comment: null,
      },
      rules: {
        compnayName: {
          required: true,
          message: "请输入单位名称",
          trigger: "blur",
        },
        institutionID: {
          required: true,
          message: "请输入单位编号",
          trigger: "blur",
        },
        name: {required: true, message: "请输入用户名", trigger: "blur"},
        phone: {required: true, message: "请输入电话", trigger: "blur"},
        role: {required: true, message: "请输入角色", trigger: "blur"},
        email: {required: true, message: "请输入邮箱", trigger: "blur"},
        enabled: {required: true, message: "请输入enable", trigger: "blur"},
        username: {
          required: true,
          message: "请输入username",
          trigger: "blur",
        },
        password: {required: true, message: "请输入密码", trigger: "blur"},
        comment: {required: false, message: "请输入备注", trigger: "blur"},
      },

      err_dialogVisible_edit: false,
      err_title: '',
      err_msg: ''
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  created() {
    //this.keywords = this.$route.query.keywords;
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.groupID = this.$route.query.groupID;
    this.ACNAME = this.$route.query.keywords_name;
    //console.log(this.keywords_name);
    // console.log("groupID", this.groupID);
    this.initHrs();
    // this.initData();
    //this.initAd();
  },
  methods: {

    Delete_ExActivity(si) {
      this.$confirm("此操作将永久删除【" + si.name + "】, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            si.institutionid=this.user.institutionID;
            this.postRequest("/systemM/Experts/delete?groupid=" + this.groupID + "&activityid=" + this.keywords, si).then(resp => {
              if (resp) {
                this.initHrs();
              }
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
    },
    enabledChange(hr) {
      //delete hr.roles;
      this.putRequest("/system/admin/", hr).then((resp) => {
        if (resp) {
          this.initHrs();
        }
      });
    },
    initAllRoles() {
      this.getRequest("/system/hr/roles").then((resp) => {
        if (resp) {
          this.allroles = resp;
        }
      });
    },
    initHrs() {
      this.getRequest(
          "/systemM/Experts/?keywords=" +
          this.groupID +
          "&page=" +
          this.page +
          "&size=" +
          this.size
      ).then((resp) => {
        if (resp) {
          this.hrs = resp;
          this.total = resp.length;
          //console.log(this.hrs);
        }
      });
    },
    jumperInToS(data){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/situation",
        query: {
          keywords: this.keywords,
          keyword_name: this.ACNAME,
          keywords_name:this.keywords_name,
          groupID: this.groupID,
          expertID:data.id,
          expertName:data.name,
          institutionID:this.user.institutionID
        },
      });
    },
    UpdateCheckbox(data){
      this.getRequest("/secretary/setSecretary?teacherID=" + data.id + "&activityID=" + this.keywords + "&groupID=" + this.groupID)
          .then((resp) => {
            if(resp)
            {Message.success("更新成功");}
            else
            {Message.error("更新失败");}
          });
    },
    advancedSearch() {
      this.getRequest(
          "/groups/basic/advanced/?keywords=" +
          this.keywords +
          "&keywords_name=" +
          this.keywords_name +
          "&page=" +
          this.page +
          "&size=" +
          this.size
      ).then((resp) => {
        if (resp) {
          this.hrs = resp.data;
          this.total = resp.total;
        }
      });
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initHrs();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initHrs("advanced");
    },
    //取消表格选择
    // toggleSelection(rows) {
    //   if (rows) {
    //     rows.forEach((row) => {
    //       this.$refs.multipleTable.toggleRowSelection(row);
    //     });
    //   } else {
    //     this.$refs.multipleTable.clearSelection();
    //   }
    // },
    handleAddDetails() {
      if (this.hrs === undefined) {
        this.hrs = new Array();
      }
      let obj = {};
      obj.activityID = this.keywords;
      obj.expertCount = 0;
      obj.participantCount = 0;
      this.hrs.push(obj);
    },
    back() {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/table",
        query: {
          keywords: this.keywords,
          keyword_name: this.ACNAME,
        },
      });
    },
    tableRowClassName({row, rowIndex}) {
      // 把每一行的索引放进row
      row.index = rowIndex;
    },
    // 添加明细原因 row 当前行 column 当前列
    tabClick(row, column, cell, event) {
      switch (column.label) {
        case "专家姓名":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "证件号码":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "邮箱":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "部门":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "电话":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        default:
          return;
      }
    },

    showParticipantsM(data) {
      const _this = this;
      _this.$router.push({
        path: "/participantsM",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    beforehandleEdit(index, row, label) {
      this.currentfocusdata = row[label]
    },
    handleEdit(index, row, label) {
      if (row[label] === '') {
        Message.warning('输入内容不能为空！')
        row[label] = this.currentfocusdata
      } else {
        this.save(row)
      }
    },
    // 失去焦点初始化
    inputBlur() {
      this.tabClickIndex = null;
      this.tabClickLabel = "";
      this.currentfocusdata = "";
    },
    save(si) {
      si.institutionid=this.user.institutionID;
      const _this = this;
      //console.log(si);
      //this.$router.push({name:'/scoreItem/basic/update',params:{scoreItem:_this.hrs,total:_this.total}})
      this.postRequest("/systemM/Experts/update", si).then((resp) => {
        Message.success(resp)
      });
    },
    update(si) {
      si.institutionid=this.user.institutionID;
      const _this = this
      //console.log(si);
      //this.$router.push({name:'/scoreItem/basic/update',params:{scoreItem:_this.hrs,total:_this.total}})
      this.postRequest("/systemM/Experts/updateGroupId?groupid=" + this.groupID + "&activityid=" + this.keywords, si).then(resp => {
      })
    },
    initData() {
      this.getRequest("/activities/basic/get_activity_info").then((resp) => {
        if (resp) {
          this.activitydata = resp;
        }
      });
    },
    onError(err, file, fileList) {
      this.importDataBtnText = "导入专家";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
    },
    onSuccess(res) {
      // console.log("res", res);
      if (res.msg === 'file error') {
        Message.error("文件内容或者格式有误，请不要修改表头，信息按格式填写！")
      } else if (res.msg === 'success') {
        Message.success('数据导入成功！')
      } else {
        this.err_dialogVisible_edit = true;
        this.err_msg = res.obj;
        this.err_title = "以下专家用户名重复：";
        //Message.error("上传失败！")
      }
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
      this.initHrs();
    },
    beforeUpload() {
      this.importDataBtnText = "正在导入";
      this.importDataBtnIcon = "el-icon-loading";
      this.importDataDisabled = true;
    },
    UploadUrl() {
      let url = "/systemM/Experts/import?groupid=" + this.groupID + "&activityid=" + this.keywords+"&insititutionID="+this.user.institutionID;
      return url;
    },
    exportMo() {
      Message.success("正在下载模板");
      window.open("/participants/basic/exportMo", "_parent");
    },
    reset_password(row) {
      row.institutionid=this.user.institutionID;
      this.putRequest("/systemM/Experts/pass", row).then((resp) => {
        if (resp) {
          //弹窗
          if(resp==='已将密码设置为证件号码后6位!')
          {
            Message.success(resp);
          }
          else
          {
            Message.error(resp);
          }
        }
      });
    },
    changeFinished(row){
      this.$confirm("是否确定撤销打分", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            row.institutionid=this.user.institutionID;
            this.postRequest("/systemM/Experts/withdraw?activityID=" + this.keywords + "&groupID=" + this.groupID , row).then(resp => {
              if (resp) {
                this.initHrs();
                this.$message({
                  type: 'success',
                  message: '撤销成功!'
                });
              }
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消撤销",
            });
          });
    }
  },
};
</script>

<style>
.userinfo-container div {
  font-size: 12px;
  color: #409eff;
}

.userinfo-container {
  margin-top: 20px;
}

.img-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.userface-img {
  width: 72px;
  height: 72px;
  border-radius: 72px;
}

.hr-container {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}

.hr-card {
  width: 350px;
  margin-bottom: 20px;
}

.tb-edit.el-input {
  display: none;
}

.tb-edit.current-row.el-input {
  display: block;
}

.tb-edit.current-row.el-input + span {
  display: none;
}

.spanscoped {
  width: 100%;
  height: 100%;
  display: inline-block;
}
</style>
