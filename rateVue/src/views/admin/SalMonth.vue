<template>
<!--评分项设置-->
  <div>
   <AddActStep v-show="typeof $route.query.addActive !== 'undefined'" :active="parseInt($route.query.addActive)" :actID="keywords" :act-name="keywords_name"></AddActStep>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center" v-if="(mode==='admin' || mode==='adminSub') && !$route.query.addActive ">{{ keywords_name }}评分项设置</div>
      <div style="width: 100%;text-align: center" v-if="mode==='secretary' || mode==='secretarySub'">{{ keywords_name }}评分项查看</div>
      <div style="margin-left: auto" v-show="typeof $route.query.addActive === 'undefined' ">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div v-if="mode === 'secretary' || mode==='secretarySub'"><br/>单元格内容只可查看不可编辑</div>
    <div style="margin-top: 10px">
      <el-table
          ref="multipleTable"
          :data="hrs"
          stripe
          border
          v-loading="loading"
          :row-class-name="tableRowClassName"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.08)"
          style="width: 100%"
          @cell-mouse-enter="handleCellMouseEnter"
          @cell-mouse-leave="()=>{
            if(this.editing === false){
              this.tabClickIndex = -1;
              this.tabClickLabel = '';
            }
          }"
      >
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column
            prop="name"
            fixed
            align="name"
            label="名称"
            min-width="10%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '名称'
                "
                v-model.trim="scope.row.name"
                size="mini"
                maxlength="500"
                @input="editing = true"
                @focus="beforehandleEdit(scope.$index,scope.row,'name')"
                @change="handleEdit(scope.$index,scope.row,'name')"
                @blur="inputBlur"
            />
            <span
                style="width: 100%; height: 100%; display: inline-block"
                v-else
            >{{ scope.row.name }}</span
            >
          </template>
        </el-table-column>
        <el-table-column
            prop="score"
            label="分数"
            align="center"
            min-width="10%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '分数'
                "
                maxlength="5"
                v-model="scope.row.score"
                size="mini"
                @input="editing = true"
                @focus="beforehandleEdit(scope.$index,scope.row,'score')"
                @change="handleEdit(scope.$index,scope.row,'score')"
                @blur="inputBlur"
            />
            <span
                style="width: 100%; height: 100%; display: inline-block"
                v-else
            >{{ scope.row.score }}</span
            >
          </template>
          <!-- </div>   -->
        </el-table-column>
        <el-table-column
            prop="coef"
            label="折算系数"
            align="center"
            min-width="10%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex &&
                  tabClickLabel === '折算系数'
                "
                v-model="scope.row.coef"
                maxlength="5"
                placeholder="请输入折算系数"
                size="mini"
                @input="editing = true"
                @focus="beforehandleEdit(scope.$index,scope.row,'coef')"
                @change="handleEdit(scope.$index,scope.row,'coef')"
                @blur="inputBlur"
            />
            <span
                style="width: 100%; height: 100%; display: inline-block"
                v-else
            >{{ scope.row.coef }}</span
            >
          </template>
        </el-table-column>
        <el-table-column
            prop="byexpert"
            label="是否需要专家打分"
            align="center"
            min-width="10%"
        >
          <template slot-scope="scope" >
              <span v-if="mode!=='secretary' && mode!=='secretarySub'">
                 <el-checkbox
                    :true-label="1"
                    :false-label="0"
                    v-model.trim="scope.row.byexpert"
                    @change="UpdateCheckbox(scope.row)"
                 ></el-checkbox>
            专家打分
              </span>
              <span v-else-if="scope.row.byexpert">是</span>
              <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="comment"
            label="详细描述"
            align="center"
            min-width="10%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex &&
                  tabClickLabel === '详细描述'
                "
                @input="editing = true"
                v-model="scope.row.comment"
                maxlength="2000"
                placeholder="请输入详细描述"
                size="mini"
                @focus="beforehandleEdit(scope.$index,scope.row,'comment')"
                @change="handleEdit(scope.$index,scope.row,'comment')"
                @blur="inputBlur"
            />
            <span
                style="width: 100%; height: 100%; display: inline-block"
                v-else
            >{{ scope.row.comment }}</span
            >
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="20%" label="操作" v-if="mode!=='secretary' && this.mode!=='secretarySub'">
          <template slot-scope="scope">
            <el-button
                @click="UpdateOrNew(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-collection"
                type="primary"
                plain
            >保存
            </el-button
            >
            <el-button
                @click="Delete_Score_Item(scope.row)"
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
      <div style="margin: 20px 0; display: flex; justify-content: left" v-show="mode!=='secretary' &&mode!=='secretarySub'">
        <div style="margin-left: 8px">
          <el-button
              @click="newScoring()"
              type="primary"
              icon="el-icon-plus"
              v-if="$route.query.mode != 'admin' || !$route.query.addActive || $route.query.haveSub != 1"
          >新增
          </el-button>
         <el-tooltip class="item" effect="dark" content="当前活动存在子活动，无法添加评分项。" placement="top-start" v-else :disabled='false'>
       <span>
        <el-button @click="newScoring()" type="primary" icon="el-icon-plus" :disabled="true">新增</el-button>
       </span>
         </el-tooltip>

        </div>
<!--        <div style="margin-left: auto">-->
<!--          <el-pagination-->
<!--              background-->
<!--              @current-change="currentChange"-->
<!--              @size-change="sizeChange"-->
<!--              layout="sizes, prev, pager, next, jumper, ->, total, slot"-->
<!--              :total="total"-->
<!--          >-->
<!--          </el-pagination>-->
<!--        </div>-->
      </div>
    </div>
  </div>
</template>

<script>
import {Message} from 'element-ui'
import AddActStep from "@/components/AddActStep.vue";

export default {
  name: "SalMonth",
 components: {AddActStep},
  data() {
    return {
      editing: false,
      //当前焦点数据
      currentfocusdata: "",
      searchValue: {
        compnayName: null,
      },
      title: "",
      page: 1,
      tabClickIndex: null, // 点击的单元格
      tabClickLabel: "", // 当前点击的列名
      keywords: "",
      activitydata: [],
      keywords_name: "",
      size: 10,
      total: 0,
      loading: false,
      hrs: [],
      mode:"",
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
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem("user")); //object信息
    },
  },
  created() {
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.mode = this.$route.query.mode;
    this.initHrs();
    this.initData();
  },
  methods: {
    handleCellMouseEnter(row, column, cell, event) {
      if (this.editing === true)
        return;
      if (this.mode!=="secretary" && this.mode!=='secretarySub'){
        switch (column.label) {
          case "折算系数":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "分数":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "名称":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "详细描述":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          default:
            return;
        }
      }
    },
    Delete_Score_Item(si) {
      this.$confirm("此操作将永久删除【" + si.name + "】, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
            this.postRequest("/scoreItem/basic/delete?institutionID="+this.user.institutionID, si).then((resp) => {
              if (resp) {
                this.$message({
                      type: "info",
                      message: "删除成功",
                    });
                this.initHrs();
              }
            });
          })
    },
    initHrs() {
      this.loading = true;
      this.getRequest(
          "/scoreItem/basic/?keywords=" +
          this.keywords +
          "&page=" +
          1 +
          "&size=" +
          1000
      ).then((resp) => {
        if (resp) {
          this.loading = false;
          this.hrs = resp.data;
         console.log(this.hrs)
          for (var i = 0; i < this.hrs.length; i++){
            if (this.hrs[i].byexpert)
              this.hrs[i].byexpert = 1
            else
              this.hrs[i].byexpert = 0
          }
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
    handleAddDetails() {
      if (this.hrs == undefined) {
        this.hrs = new Array();
      }
      let obj = {};
      this.getRequest("/scoreItem/basic/latestId").then(res=>{
        obj.id = res.obj;
      })
      obj.activityid = this.keywords;
      obj.score = 100;
      obj.coef = 1;
      obj.byexpert = 1;
      this.hrs.push(obj);
    },
    back() {
      this.$router.go(-1);
      // const _this = this;
      // if (this.mode === "admin"){
      //     _this.$router.push({
      //         path: "/ActivitM/search",
      //     });
      // }else if (this.mode === "adminSub"){
      //     _this.$router.push({
      //         path: "/ActivitM/SubActManage",
      //         query:{
      //             id: this.$route.query.backID,
      //         }
      //     });
      // }else if (this.mode==="secretary"){
      //   _this.$router.push({
      //     path: "/secretary/ActManage",
      //     query:{
      //       id: this.$route.query.backID,
      //     }
      //   });
      // }else if (this.mode==="secretarySub"){
      //   _this.$router.push({
      //     path: "/secretary/SubActManage",
      //     query:{
      //       id: this.$route.query.backID,
      //       groupID :this.$route.query.groupID,
      //     }
      //   });
      // }
    },
    tableRowClassName({row, rowIndex}) {
      // 把每一行的索引放进row
      row.index = rowIndex;
    },
    // 添加明细原因 row 当前行 column 当前列
    tabClick(row, column, cell, event) {
      if (this.mode!=="secretary" && this.mode!=='secretarySub'){
        switch (column.label) {
          case "折算系数":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "分数":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "名称":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "详细描述":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          default:
            return;
        }
      }
    },
    beforehandleEdit(index, row, label) {
      if (this.mode!=="secretary"&& this.mode!=='secretarySub'){
        if (label === 'name') {
          this.currentfocusdata = row.name
        } else if (label === 'score') {
          // console.log('222222222222222222')
          this.currentfocusdata = row.score
        } else if (label === 'coef') {
          this.currentfocusdata = row.coef
        } else if (label === 'comment') {
          this.currentfocusdata = row.comment
        }
        this.currentfocusdata = row[label]
      }
    },
    handleEdit(index, row, label) {
      if (this.mode!=="secretary"&& this.mode!=='secretarySub'){
        if (row[label] == ''&&label !== 'comment') {
          Message.warning('输入内容不能为空!')
          if (label === 'name') {
            row.name = this.currentfocusdata
          } else if (label === 'score') {
            row.score = this.currentfocusdata
          } else if (label === 'coef') {
            row.coef = this.currentfocusdata
          }else if (label === 'comment') {
            row.comment = this.currentfocusdata
          }
          Message.warning('输入内容不能为空！')
          row[label] = this.currentfocusdata
        } else {
          this.UpdateOrNew(row)
          this.editing = false
          // this.newScoring(row)
        }
      }
    },
    // 失去焦点初始化
    inputBlur() {
      this.tabClickIndex = null;
      this.tabClickLabel = "";
      this.currentfocusdata = ""
    },
    UpdateOrNew(scoreItem) {
      const _this = this;
      _this
          .postRequest("/scoreItem/basic/UpdateOrNew?institutionID="+this.user.institutionID, scoreItem)
          .then((resp) => {
            if(resp==='更新成功!')
            {Message.success(resp);
              this.reset();}
            else if(resp==='新增成功!')
            {Message.success(resp);
              this.reset();}
            else
            {Message.error(resp);}
          });
    },
    UpdateCheckbox(scoreItem) {
      console.log(scoreItem)
      const _this = this;
      _this
          .postRequest("/scoreItem/basic/UpdateOrNew?institutionID="+this.user.institutionID, scoreItem)
          .then((resp) => {
            if(resp==='更新成功!')
            {Message.success(resp);
              }
            else if(resp==='新增成功!')
            {Message.success(resp);
              }
            else
            {Message.error(resp);}
          });
    },
    reset(){
      this.initHrs();
    },
    newScoring() {
      let obj = {};
      obj.activityid = this.keywords;
      obj.score = 100;
      obj.name = '请输入评分项名称';
      obj.coef = 1;
      obj.byexpert = 1;
      this.hrs.push(obj);
    },
    initData() {
      this.getRequest("/activities/basic/get_activity_info").then((resp) => {
        if (resp) {
          this.activitydata = resp;
        }
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
</style>
