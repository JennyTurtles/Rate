<template>
<!--评分项设置-->
  <div>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center">{{ keywords_name }}信息项设置</div>
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div><br/>单元格中内容双击后可编辑</div>
    <div><br/>是否展示：该信息项是否在展示给专家打分。大小限制：对文件大小或输入内容字数的限制</div>
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
          element-loading-background="rgba(0, 0, 0, 0.08)"
          style="width: 100%"
      >
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column
            prop="name"
            fixed
            align="name"
            label="名称"
            width="100px"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '名称'
                "
                v-focus
                v-model.trim="scope.row.name"
                size="mini"
                maxlength="80"
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
            prop="byParticipant"
            label="是否需要选手填写"
            align="center"
            width="120px"
        >
          <template slot-scope="scope">
            <el-checkbox
                v-model.trim="scope.row.byParticipant"
                @change="UpdateCheckbox(scope.row,'byParticipan')"
            ></el-checkbox>
            选手填写
          </template>
        </el-table-column>
        <el-table-column
            prop="shuZuType"
            label="类型"
            align="center"
            min-width="10%"
        >
          <template slot-scope="scope">
            <el-select v-model="scope.row.shuZuType" placeholder="请输入类型"
                       multiple
                       min-width="10%"
                       v-focus
                       @focus="beforehandleEdit(scope.$index,scope.row,'type')"
                       @change="handleEdit(scope.$index,scope.row,'type')"
                       @blur="inputBlur"
                       @visible-change="handleVisible(scope.row)"
                       prefix-icon="el-icon-edit"

            >
              <el-option
                  v-for="x in shuju"
                  :key="x.name"
                  :label="x.name"
                  :value="x.name"
                  :disabled="x.disabled">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
            prop="sizelimit"
            label="大小限制(字节默认为M，字为字数)"
            align="center"
            min-width="5%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="scope.row.index === tabClickIndex && tabClickLabel === '大小限制(字节默认为M，字为字数)' && scope.row.byParticipant === true "
                v-focus
                v-model="scope.row.sizelimit"
                placeholder="请输入sizelimit"
                size="mini"
                @focus="beforehandleEdit(scope.$index,scope.row,'sizelimit')"
                @change="handleEdit(scope.$index,scope.row,'sizelimit')"
                @blur="inputBlur"
            />
            <span
                style="width: 100%; height: 100%; display: inline-block"
                v-else
            >{{ scope.row.sizelimit }}</span
            >
          </template>
        </el-table-column>
        <el-table-column
            prop="comment"
            label="是否展示"
            align="center"
            min-width="3%"
        >
          <template slot-scope="scope">
            <el-checkbox
                v-model.trim="scope.row.display"
                @change="UpdateCheckbox(scope.row,'display') "
            ></el-checkbox>
            display
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="5%" label="操作">
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
                @click="Delete_Info_Item(scope.row)"
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
        <div style="margin-left: 8px">
          <el-button
              @click="newScoring()"
              type="primary"
              icon="el-icon-plus"
          >新增
          </el-button
          >
        </div>
        <!--        <div style="margin-left: 8px">-->
        <!--          <el-button @click="save()" type="primary" icon="el-icon-collection"-->
        <!--          >保存-->
        <!--          </el-button-->
        <!--          >-->
        <!--        </div>-->
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
    </div>

  </div>
</template>

<script>
import {Message} from 'element-ui'
import ro from "element-ui/src/locale/lang/ro";

export default {
  name: "SalInfos",
  data() {
    return {
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
      emp: {
        id:null,
        institutionID:null,
        name: null/*,*/
      },
      shuju:[
        {name:"textbox",famname:"字",disabled:false},
        {name:"textarea",famname:"字",disabled:false},
        {name:"pdf",famname:"字节",disabled:false},
        {name:"jpg",famname:"字节",disabled:false},
        {name:"zip",famname:"字节",disabled:false},
        {name:"label",famname:"",disabled:false}
      ],
      activitydata: [],
      keywords_name: "",
      size: 10,
      total: 0,
      loading: false,
      hrs: [],
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
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  created() {
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.initHrs();
    this.initData();
  },
  methods: {
    // 点击下拉框对显示的选项进行筛选。已选中文本类型则不允许选其他所有类型；已选中文件类型则不允许选本文类型。
    handleVisible(row){
      if (row.shuZuType === null){
        for (let i = 0; i < this.shuju.length; i++) {
          if (row.byParticipant === true && i === 5)
            this.shuju[i].disabled = true
          else
            this.shuju[i].disabled = false
        }
        return;
      }
      if (row.shuZuType.indexOf("textbox") !== -1 || row.shuZuType.indexOf("textarea") !== -1 || row.shuZuType.indexOf("label") !== -1){ // 有文本类型
        for (let i = 0; i < this.shuju.length; i++) {
          this.shuju[i].disabled = true
        }
      }else{
        for (let i = 0; i < this.shuju.length; i++) {
          this.shuju[i].disabled = false
        }
        if (row.shuZuType.length !== 0){ // 有文件类型
          this.shuju[0].disabled = true
          this.shuju[1].disabled = true
          this.shuju[5].disabled = true
        }
      }
      if (row.byParticipant === true){
        console.log(111)
        this.shuju[5].disabled = true
      }
      else{
        for (let i = 0; i < this.shuju.length - 1; i++) {
          this.shuju[i].disabled = true
        }
      }
      // this.shuju[5].disabled = row.byParticipant === true; // 选手填写打了个勾，label类型就要被禁止
    },
    Delete_Info_Item(si) {
      this.$confirm("此操作将永久删除【" + si.name + "】, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            if (typeof si.id === "undefined")
            {
              this.hrs.splice(si.index, 1)
              return
            }
            this.postRequest("/infoItem/basic/delete?institutionID="+this.user.institutionID, si).then((resp) => {
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
      this.loading = true;
      this.getRequest(
          "/infoItem/basic/?keywords=" +
          this.keywords +
          "&page=" +
          this.page +
          "&size=" +
          this.size
      ).then((resp) => {
        // console.log("resp",resp);
        if (resp) {
          this.loading = false;
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
    back() {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/search",
      });
    },
    tableRowClassName({row, rowIndex}) {
      // 把每一行的索引放进row
      row.index = rowIndex;
    },
    // 添加明细原因 row 当前行 column 当前列
    tabClick(row, column, cell, event) {
      switch (column.label) {
        case "contentType":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "大小限制(字节默认为M，字为字数)":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "名称":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        default:
          return;
      }
    },
    beforehandleEdit(index, row, label) {
      if (label === 'name' || label === 'type') {
        this.currentfocusdata = row.name
      } else if (label === 'sizelimit') {
        this.currentfocusdata = row.sizelimit
      }
      this.currentfocusdata = row[label]
    },
    handleEdit(index, row, label) {
      console.log(row)
      if (label === 'type'){
        if (row.shuZuType.indexOf("textbox") !== -1 || row.shuZuType.indexOf("textarea") !== -1 || row.shuZuType.indexOf("label") !== -1){ // 有文本类型
          for (let i = 0; i < this.shuju.length; i++) {
            this.shuju[i].disabled = true
          }
        }else{
          for (let i = 0; i < this.shuju.length; i++) {
            this.shuju[i].disabled = false
          }
          if (row.shuZuType.length !== 0){ // 有文件类型
            this.shuju[0].disabled = true
            this.shuju[1].disabled = true
            this.shuju[5].disabled = true
          }
        }
      }
      if (row[label] == ''&&label !== 'sizelimit'&&label !== 'contentType') {
        Message.warning('输入内容不能为空!')
        if (label === 'name' || label === 'type') {
          row.name = this.currentfocusdata
        } else if (label === 'sizelimit') {
          row.sizelimit = this.currentfocusdata
        }
        Message.warning('输入内容不能为空！')
        row[label] = this.currentfocusdata
      } else {
        this.UpdateOrNew(row)
        // this.newScoring(row)
      }
    },
    // 失去焦点初始化
    inputBlur() {
      //console.log(this.hrs);
      // if (this.currentfocusdata == null) {
      //   Message.error('输入内容不能为空！操作未保存！')
      // }
      this.tabClickIndex = null;
      this.tabClickLabel = "";
      this.currentfocusdata = ""
    },
    save() {
      const _this = this;
      // console.log("-----------",_this.hrs);
      //this.$router.push({name:'/scoreItem/basic/update',params:{scoreItem:_this.hrs,total:_this.total}})
      this.postRequest("/scoreItem/basic/update", _this.hrs).then((resp) => {
        if(resp==='更新成功!')
        {Message.success(resp)}
        else
        {Message.error(resp)}
      });
    },
    UpdateOrNew(infoItem) {
      const _this = this;
      _this
          .postRequest("/infoItem/basic/UpdateOrNew?institutionID="+this.user.institutionID, infoItem)
          .then((resp) => {
            if(resp.msg==='更新成功!')
            {Message.success(resp.msg);
              this.reset();}
            else if(resp.msg==='新增成功!')
            {Message.success(resp.msg);
              this.reset();}
            else
            {Message.error(resp.msg);
            }
          });
    },
    UpdateCheckbox(infoItem,mode) {
      const _this = this;
      if (mode === 'byParticipan')
      {
        if (infoItem.byParticipant === false){
          infoItem.shuZuType = ['label']
          infoItem.contentType = 'label'
        }else
        {
          infoItem.shuZuType = []
          infoItem.contentType = ''
        }
      }
      this.loading = true
      _this
          .postRequest("/infoItem/basic/UpdateOrNew?institutionID="+this.user.institutionID, infoItem)
          .then((resp) => {
            this.loading = false
            if(resp.msg==='更新成功!')
            {
              Message.success(resp.msg);
              }
            else if(resp.msg==='新增成功!')
            {Message.success(resp.msg);
              this.reset();
              }
            else
            {Message.error(resp.msg);}
          });
    },
    reset(){
      this.initHrs();
      // console.log('reload');
    },
    newScoring() {
      //console.log("creating")
      let obj = {};
      obj.activityID = this.keywords;
      obj.contentType = 'label';
      obj.shuZuType = ['label']
      obj.name = '请输入信息项名称';
      obj.sizelimit='500';
      obj.display = true;
      obj.byParticipant = false;
      this.hrs.push(obj);
      /*this.postRequest("/scoreItem/basic/insert", obj)
          .then((resp) => {
            this.initHrs();
          });*/
    },
    // shiftScoring(scoreItem) {
    //   console.log("modifying")
    //   console.log(scoreItem);
    //   // console.log(need);
    //   // need:1->需要专家打分
    //   this.currentfocusdata = scoreItem.name;
    //   if (this.currentfocusdata == null) {
    //     Message.error('输入内容不能为空！操作未保存！')
    //     return
    //   }
    //   this.postRequest("/scoreItem/basic/modify", scoreItem)
    //       .then(res => {
    //         if (res.msg != "修改成功！")
    //           scoreItem.name = this.currentfocusdata;
    //       })
    // }, //
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
