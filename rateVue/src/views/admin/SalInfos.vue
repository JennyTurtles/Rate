<template>
<!--评分项设置-->
  <div>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center" v-if="mode==='admin' || mode==='adminSub'">{{ keywords_name }}信息项设置</div>
      <div style="width: 100%;text-align: center" v-if="mode==='secretary' || mode==='secretarySub'">{{ keywords_name }}信息项查看</div>
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div v-if="mode === 'secretary' || mode==='secretarySub'"><br/>单元格内容只可查看不可编辑</div>
    <div><br/>名称不能为基本信息名，包括：姓名，身份证号，编号，序号，手机号，邮箱，组名</div>
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
            width="200px"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '名称'
                "
                v-model.trim="scope.row.name"
                size="mini"
                maxlength="80"
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
            prop="byParticipant"
            label="是否需要选手填写"
            align="center"
            width="120px"
        >
          <template slot-scope="scope" >
              <span v-if="mode!=='secretary'" >
            <el-checkbox
                v-model.trim="scope.row.byParticipant"
                @change="UpdateCheckbox(scope.row,'byParticipan')"
            ></el-checkbox>
            选手填写
              </span>
              <span v-else-if="scope.row.byParticipant">是</span>
              <span v-else>否</span>
<!--          </template>-->
<!--          <template slot-scope="scope" v-if="mode==='secretary' || mode==='secretarySub'">-->
<!--            <span v-if="scope.row.byParticipant">是</span>-->
<!--            <span v-else>否</span>-->
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
                       v-if="scope.row.byParticipant === true"
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
            <span v-else>/</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="sizelimit"
            label="大小限制"
            align="center"
            min-width="5%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="scope.row.index === tabClickIndex && tabClickLabel === '大小限制' && scope.row.byParticipant === true "
                v-model="scope.row.sizelimit"
                placeholder="请输入sizelimit"
                size="mini"
                @input="editing = true"
                @focus="beforehandleEdit(scope.$index,scope.row,'sizelimit')"
                @change="handleEdit(scope.$index,scope.row,'sizelimit')"
                @blur="inputBlur"
            />
            <span
                style="width: 100%; height: 100%; display: inline-block"
                v-else-if="scope.row.byParticipant === true"
            >{{ scope.row.sizelimit }}{{JSON.stringify(scope.row.shuZuType).indexOf('textbox') !== -1
            || JSON.stringify(scope.row.shuZuType).indexOf('textarea') !== -1 ? '字' : 'MB'}}</span
            >
            <span v-else>/</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="comment"
            label="是否显示在打分表中"
            align="center"
            min-width="3%"
        >
          <template slot-scope="scope">
              <span  v-if="mode!=='secretary'">
            <el-checkbox
                v-model.trim="scope.row.display"
                @change="UpdateCheckbox(scope.row,'display') "
            ></el-checkbox>
            display
              </span>
              <span v-else-if="scope.row.display">是</span>
              <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="5%" label="操作" v-if="this.mode!=='secretary' && this.mode!=='secretarySub'">
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
              v-show="mode!=='secretary' &&mode!=='secretarySub'"
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

export default {
  name: "SalInfos",
  data() {
    return {
      basicNameList: ['姓名','身份证号','编号','序号','手机号','邮箱','组名'],
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
      emp: {
        id:null,
        institutionID:null,
        name: null/*,*/
      },
      shuju:
          [
        {name:"textbox",famname:"字",disabled:false},
        {name:"textarea",famname:"字",disabled:false},
        {name:"pdf",famname:"字节",disabled:false},
        {name:"jpg",famname:"字节",disabled:false},
        {name:"zip",famname:"字节",disabled:false},
      ],
      activitydata: [],
      keywords_name: "",
      groupID:"",
      groupName:"",
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
        mode:'',
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
    this.mode = this.$route.query.mode;
    this.groupID = this.$route.query.groupID;
    this.groupName = this.$route.query.groupName;
    console.log(this.groupID);
    this.initHrs();
    this.initData();
  },
  methods: {
    handleCellMouseEnter(row, column, cell, event) {
      if (this.editing === true)
        return;
      if (this.mode!=="secretary" && this.mode!=='secretarySub'){
        switch (column.label) {
          case "contentType":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "大小限制":
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
      }
    },
    // 点击下拉框对显示的选项进行筛选。已选中文本类型则不允许选其他所有类型；已选中文件类型则不允许选本文类型。
    handleVisible(row){
      if (this.mode==='secretary'){
        for (let i = 0; i < this.shuju.length; i++) {
          this.shuju[i].disabled = true
        }
      }
      else{
        if (row.shuZuType === null){ // 处理为空的情况
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
            // this.shuju[5].disabled = true
          }
        }
        if (row.byParticipant === true){ // 选手填写打了个勾，label类型就要被禁止
          // this.shuju[5].disabled = true
        }
        else{
          for (let i = 0; i < this.shuju.length - 1; i++) {
            this.shuju[i].disabled = true
          }
        }
        // this.shuju[5].disabled = row.byParticipant === true;
      }
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
          1 +
          "&size=" +
          1000
      ).then((resp) => {
        // console.log("resp",resp);
        if (resp) {
          this.loading = false;
          this.hrs = resp.data;
          console.log(this.hrs);
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
      this.$router.go(-1);
      // const _this = this;
      // var url;
      // if (this.mode === "admin"){
      //   _this.$router.push({
      //     path: "/ActivitM/search",
      //     query: {
      //       id: this.$route.query.backID,
      //     },
      //   });
      // }
      // else if (this.mode === "adminSub"){
      //   _this.$router.push({
      //     path: "/ActivitM/SubActManage",
      //     query: {
      //       id: this.$route.query.backID,
      //     },
      //   });
      // }
      // else if (this.mode === "secretary"){
      //   _this.$router.push({
      //     path: "/secretary/ActManage",
      //     query: {
      //       id: this.$route.query.backID,
      //     },
      //   });
      // }
      // else if (this.mode === "secretarySub"){
      //   _this.$router.push({
      //     path: "/secretary/SubActManage",
      //     query: {
      //       id: this.$route.query.backID,
      //       groupID :this.$route.query.groupID,
      //     },
      //   });
      // }
    },
    tableRowClassName({row, rowIndex}) {
      // 把每一行的索引放进row
      row.index = rowIndex;
    },
    // 添加明细原因 row 当前行 column 当前列
    tabClick(row, column, cell, event) {
      if (this.mode!== 'secretary' && this.mode!=='secretarySub'){
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
      }
    },
    beforehandleEdit(index, row, label) {
      if (this.mode!== 'secretary' && this.mode!=='secretarySub'){
        if (label === 'name' || label === 'type') {
          this.currentfocusdata = row.name
        } else if (label === 'sizelimit') {
          this.currentfocusdata = row.sizelimit
        }
        this.currentfocusdata = row[label]
      }
    },
    handleEdit(index, row, label) {
      if (this.mode!== 'secretary' && this.mode!=='secretarySub'){
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
              // this.shuju[5].disabled = true
            }
            // if (row.byParticipant === true)
            //   this.shuju[5].disabled = true
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
          if (label === 'sizelimit' && (parseFloat(row.sizelimit).toString() === 'NaN' || row.sizelimit < 0 || row.sizelimit > 1000)){
            Message.warning('更新失败！大小限制需要为0到1000的数字！')
          }else if (label === 'name' && this.basicNameList.indexOf(row.name) !== -1){
            Message.warning('更新失败！名称不能为基本信息名！')
          }
          else
            this.UpdateOrNew(row)
          this.editing = false
          // this.newScoring(row)
        }
      }
    },
    // 失去焦点初始化
    inputBlur() {
      if (this.mode!=='secretary'){
        this.tabClickIndex = null;
        this.tabClickLabel = "";
        this.currentfocusdata = ""
      }
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
      console.log(infoItem)
      if (mode === 'byParticipan')
      {
        if (infoItem.byParticipant === false){
          infoItem.shuZuType = ['label']
          infoItem.contentType = 'label'
          infoItem.sizelimit = ''
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
      obj.sizelimit='';
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
