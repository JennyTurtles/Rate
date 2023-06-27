<template>
  <div>
    <div >
      <div style="display: flex; justify-content: left">
        <div style="width: 100%;text-align: center">选手管理</div>
        <div style="margin-left: auto">
          <el-button icon="el-icon-back" type="primary" @click="change">
            切换到专家管理
          </el-button>
        </div>
      </div>
      <div v-show="mode === 'secretary'">{{ keywords_name }}活动 选手名单<br/><br/></div>
      <a>
      选手添加有三种模式：手动添加、从本单位添加、批量导入。<br/><br/>
      </a>
      <div style="display: flex;justify-content: space-between;">
        <!-- <div>
          <el-input placeholder="请输入单位名进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="searchEmps"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="searchEmps" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="searchEmps" :disabled="showAdvanceSearchView">
            搜索
          </el-button>
        </div> -->
        <div>
          <el-button type="success" @click="showMethod">
            添加选手
          </el-button>
        </div>
        <div>
          <el-button type="primary" @click="exportTG" icon="el-icon-download">
            导出专家打分
          </el-button>
          <el-button
              icon="el-icon-refresh"
              type="primary"
              @click="refreshact()">刷新</el-button>
          <el-button icon="el-icon-back" type="primary" @click="back">
            返回
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          :model="emps"
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
            prop="displaySequence"
            fixed
            align="left"
            label="序号"
            min-width="25%"
            v-if="mode === 'admin'"
        >
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.displaySequence" :min="1" :max="total"
                             controls-position="right"
                             size="mini"
                             min-width="3%"
            ></el-input-number>
            <el-button icon="" type="primary" @click="alterDisplay(scope.row)" size="mini"
                       style="padding: 4px"
                       plain >更改
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
            prop="displaySequence"
            fixed
            align="center"
            label="序号"
            min-width="3%"
            v-if="mode !== 'admin'"
            >
        </el-table-column>
        <el-table-column
            prop="code"
            align="left"
            label="编号"
            min-width="10%">
        </el-table-column>
        <el-table-column
            prop="idnumber"
            align="left"
            label="身份证号码"
            min-width="10%">
        </el-table-column>
        <el-table-column
            prop="name"
            align="left"
            label="姓名"
            min-width="10%">
        </el-table-column>
        <el-table-column
            sortable
            prop="score"
            label="活动得分"
            align="center"
            min-width="10%">
        </el-table-column>
        <el-table-column align="left" label="操作" min-width="30%">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-collection"
                type="primary"
                plain
            >编辑</el-button
            >
            <el-button
                @click="Delete(scope.row)"
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
<!--      <div v-show="mode==='admin'" style="display: flex;justify-content: flex-end;margin:10px 0">-->
<!--        <el-pagination-->
<!--            background-->
<!--            @current-change="currentChange"-->
<!--            @size-change="sizeChange"-->
<!--            layout="sizes, prev, pager, next, jumper, ->, total, slot"-->
<!--            :total="total">-->
<!--        </el-pagination>-->
<!--      </div>-->
    </div>


    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" center>
      <el-form
          :label-position="labelPosition"
          label-width="100px"
          :model="emp"
          :rules="rules"
          ref="empForm"

      >
        <el-form-item label="显示顺序:" prop="name">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.displaySequence"
              disabled
              placeholder="显示顺序"
          ></el-input>
        </el-form-item>
        <el-form-item label="编号:" prop="code">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.code"
              placeholder="请输入编号"
          ></el-input>
        </el-form-item>
        <el-form-item label="选手姓名:" prop="name">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.name"
              placeholder="请输入选手姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="证件号码:" prop="idnumber">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.idnumber"
              disabled
              placeholder="证件号码"
          ></el-input>
        </el-form-item>
        <el-form-item
            v-for="(value,idx) in emp.scoremap"
            :key="idx"
            :label="value.name"
            width="180px"
        >
            <el-input
                placeholder="请打分"
                v-model.trim="value.score"
                clearable
            >
            </el-input>
        </el-form-item>
        <el-form-item
            v-for="(value,idx) in emp.infomap"
            :label="value.name"
            width="180px"
        >
          <el-input
              placeholder="请打分"
              v-model.trim="value.content"
              clearable
          >
          </el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
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

              <el-button @click="update(scope.row)" style="padding: 4px" size="mini" icon="el-icon-check" type="primary"
                         plain>调换到本组：{{keywords_name}}</el-button>

            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible_edit = false">关闭</el-button>
        </span>
    </el-dialog>
    <!--弹窗-->
    <el-dialog :title="title" :visible.sync="dialogVisible_checkbox" width="90%" center>
<!--      <div style="font-size: 17px;">-->
<!--        导入模板中必须包含身份证号和姓名，以下勾选的列将包含在导入模板中。模板中不包含的列，则导入时将保持数据库中已有信息不变。-->
<!--        <br/>-->
<!--        首次导入请注意！如果student表中已有该选手的记录，则手机号、邮箱、属于本单位三列可为空，“属于本单位”列填是或否。用户名密码可以不填写，若不填写第一次导入将默认为编号和手机号，其余必须填写。-->
<!--          <br/>模版内的列顺序为点击顺序。-->
<!--      </div><br/>-->
        <div style="font-size: 17px;">
            导入模板中必须包含姓名和身份证号，以下勾选的列将包含在导入模板中。模板中不包含的列，则导入时将保持数据库中已有信息不变。
            <br/>
        </div><br/>
      <div style="font-size: 16px;margin-left: 15%">基本信息：<br/>
        <el-checkbox label="姓名" key="姓名" v-model="dymatic_list" disabled style="width: 150px">姓名</el-checkbox>
        <el-checkbox label="身份证号码" v-model="dymatic_list" disabled style="width: 150px">身份证号码</el-checkbox>
        <el-checkbox label="编号" v-model="dymatic_list"  style="width: 150px">编号</el-checkbox>
        <el-checkbox label="序号" v-model="dymatic_list"  style="width: 150px">序号</el-checkbox>
        <el-checkbox label="手机号" v-model="dymatic_list"  style="width: 150px">手机号</el-checkbox>
        <el-checkbox label="邮箱" v-model="dymatic_list"  style="width: 150px">邮箱</el-checkbox>
<!--        <el-checkbox label="属于本单位" v-model="dymatic_list"  style="width: 150px">属于本单位</el-checkbox>-->
<!--        <el-checkbox label="用户名" v-model="dymatic_list"  style="width: 150px">用户名</el-checkbox>-->
<!--        <el-checkbox label="密码" v-model="dymatic_list"  style="width: 150px">密码</el-checkbox>-->
      </div><br/>
      <div style="font-size: 16px;margin-left: 15%">信息项：<br/>
        <el-checkbox v-for="item in infoitem_from_back" :key="item.name" :label="item.name" v-model="infoitem" style="width: 450px">
        </el-checkbox>
      </div><br/>
      <div style="font-size: 16px;margin-left: 15%">评分项：<br/>
        <el-checkbox v-for="item in scoreitem_from_back" :key="item.name" :label="item.name" v-model="scoreitem" style="width: 450px">
        </el-checkbox>
      </div>
        <div style="font-size: 16px;margin-left: 15%;margin-top: 15px">模版中的列排列顺序如下：<br/></div>
        <div style="font-size: 16px;margin-left: 15%">{{preview(dymatic_list,scoreitem,infoitem)}}</div>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportMo">下载</el-button>
          <el-button @click="dialogVisible_checkbox = false">关闭</el-button>
        </span>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="dialogVisible_method" width="55%" center>
      <el-tabs type="border-card">
        <el-tab-pane label="手动添加">

        </el-tab-pane>
        <el-tab-pane label="从本单位添加">
          <el-table
              ref="multipleTable"
              :data="currentParticipants"
              tooltip-effect="dark"
              style="width: 100%"
              @selection-change="handleSelectionChange">
            <el-table-column
                type="selection"
                width="40px">
            </el-table-column>
            <el-table-column
                prop="name"
                label="姓名">
            </el-table-column>
            <el-table-column
                prop="idnumber"
                label="证件号码"
                show-overflow-tooltip>
            </el-table-column>
          </el-table>
          <div class="block" style="padding-top: 10px">
            <el-pagination
                @current-change="currentChange"
                @size-change="sizeChange"
                layout="sizes, prev, pager, next, jumper, ->, total, slot"
                :total="total">
            </el-pagination>
          </div>
          <el-button type="primary" @click="add" style="float: right">
            添加
          </el-button>
        </el-tab-pane>
        <el-tab-pane label="批量导入">
          <a>
            选手第一次导入时，可先不分组。此时可以将导入表格中的“分组名称”留空，进行导入操作。待分组后，再导入一次，从而实现分组。
            选手的信息项以及评分项，也可在选手第一次导入时留空，待第二次、第三次（或之后）导入时填入那些信息。<br/><br/>
          </a>
          <span style="font-weight:600;">导入新数据</span> 第一步：
          <!--<a href="/participants/basic/exportMoPar?activityid=15">Test ResponseEntity</a>-->
          <el-button type="primary" @click="exportCheckbox" icon="el-icon-upload" style="margin-right: 8px">
            下载模板
          </el-button>
            第二步：
          <el-upload
              :show-file-list="false"
              :before-upload="beforeUpload"
              :on-success="onSuccess"
              :on-error="onError"
              :disabled="importDataDisabled"
              style="display: inline-flex;margin-right: 8px"
              action="#"
              :http-request="handleChange">
            <el-button :disabled="importDataDisabled" type="primary" :icon="importDataBtnIcon">
              {{importDataBtnText}}
            </el-button>
          </el-upload>
        </el-tab-pane>
        <el-tab-pane label="从大组添加" v-if="mode==='secretarySub'">
          <el-table
              ref="multipleTable"
              :data="parentGroup"
              tooltip-effect="dark"
              style="width: 100%"
              @selection-change="handleSelectionChange">
            <el-table-column
                type="selection"
                width="40px">
            </el-table-column>
            <el-table-column
                prop="name"
                label="姓名"
                >
            </el-table-column>
            <el-table-column
                prop="idnumber"
                label="编号"
                show-overflow-tooltip>
            </el-table-column>
          </el-table>
          <el-button type="primary" @click="add">
            添加
          </el-button>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import {Message} from "element-ui";
export default {
  name: "SalPar",
  data() {
    return{
      title: '',
      labelPosition: "left",
      importDataBtnText: '导入选手',
      importDataBtnIcon: 'el-icon-plus',
      importDataDisabled: false,
      showAdvanceSearchView: false,
      keywords_name: "",
      datalist:'',
      allDeps: [],
      emps: [],
      unsureinfo: [],
      dymatic_list:["姓名","身份证号码"],
      scoreitem:[],
      infoitem:[],
      scoreitem_from_back:[],
      infoitem_from_back:[],
      dialogVisible_edit: false,
      dialogVisible_checkbox: false,
      loading: false,
      dialogVisible: false,
      dialogVisible_method:false,
      total: 0,
      page: 1,
      keywords: '',
      keyword: '',
      activityID: '',
      groupID: '',
      mode:'',
      size: 10,
      positions: [],
      participants: [],
      currentParticipants:[],
      multipleSelection: [],
      parentGroup:[],
      activityIDParent:0,
      groupIDParent:0,
      emp: {
        id:null,
        institutionID:null,
        name: null/*,*/
        /*startDate: '2022/02/02',
        scoreItemCount: '0',
        score: '100',
        groupCount: '0',
        expertCount: '0',
        participantCount: '0',
        comment: "javaboy",*/
      },
      defaultProps: {
        children: 'children',
        label: 'name',
      },
      rules: {
        name: [{required: true, message: '请输入活动名', trigger: 'blur'}],
        startDate: [{required: true, message: '请输入活动时间', trigger: 'blur'}],
        scoreItemCount: [{required: true, type: 'number', message: '请输入正确数据', trigger: 'blur', transform: (value) => Number(value)}],
        comment: [{required: true, message: '请输入备注', trigger: 'blur'}],
      }
    }
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  created() {
  },
  mounted() {
    //this.init();//先获得评分项
    this.groupID = this.$route.query.groupID;
    this.activityID = this.$route.query.activityID;
    this.mode = this.$route.query.mode
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.ACNAME=this.$route.query.keywords_name;
    this.activityIDParent=this.$route.query.activityIDParent;
    this.groupIDParent=this.$route.query.groupIDParent;
    this.initEmps();
  },
  methods: {
      preview(dymatic_list,infoitem,scoreitem){
          // 拼接3个list，然后转换为不带有引号的字符串
          var list = dymatic_list.concat(scoreitem).concat(infoitem);
          var str = list.join()
          return str
      },
    /** 查询角色列表 */
    onError(err, file, fileList) {
      this.importDataBtnText = '导入选手';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
    },
    onSuccess(res) {
      //this.$message(res.msg);
      this.importDataBtnText = '导入选手';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
      this.initEmps();
    },
    beforeUpload() {
      /*this.importDataBtnText = '正在导入';
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataDisabled = true;*/
    },
    UploadUrl() {
      let url = "/participants/basic/import?groupid="+this.groupID+"&activityid="+this.activityID+"&insititutionID="+this.user.institutionID;
      return  url;
    },
    handleChange(file) {
      this.show = true;
      let fd = new FormData();
      let fileName = file.file.name + new Date().getTime();
      fd.append("file", file.file);
      fd.append("key", fileName);
      let url = "/participants/basic/check?groupid="+this.groupID;
      axios.post(url, fd, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
          .then((res1) => {
            if(res1.length===0)
            {
              url = "/participants/basic/import?groupid="+this.groupID+"&activityid="+this.activityID+"&insititutionID="+this.user.institutionID;
              axios.post(url, fd, {
                headers: {
                  "Content-Type": "multipart/form-data",
                },
              }).then((res) => {
                this.refreshact()
                this.$message(res.msg);
              })
            }
            else{
              let newD=[],h=this.$createElement;
              newD.push(h('span',null,'导入数据中'));
              for(const i in res1)
              {
                newD.push(h('span',null,res1[i]))
              }
              newD.push(h('span',null,'，以上列数据会被置空，是否确认继续?'));
              this.$confirm(h('div',null,newD), '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                url = "/participants/basic/import?groupid="+this.groupID+"&activityid="+this.activityID+"&insititutionID="+this.user.institutionID;
                axios.post(url, fd, {
                  headers: {
                    "Content-Type": "multipart/form-data",
                  },
                }).then((res) => {
                  this.onSuccess(res);
                  this.initEmps();
                  this.$message(res.msg);
                })
              })
            }
          })
          .catch((err) => {
            // console.log(err);
          });

    },
/*    exportData() {
      let url = "/participants/basic/export?activityID="+this.activityID;
      //console.log(url)
      window.open(url, '_parent');
    },*/
    exportTG() {
      Message.success("正在导出");
      let url = "/participants/basic/exportTG?groupID="+this.groupID;
      window.open(url, '_parent');
    },
    exportCheckbox() {
      let url = "/participants/basic/getItem?activityid="+this.activityID;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.scoreitem_from_back=resp.extend.ScoreItem;
          this.infoitem_from_back=resp.extend.InfoItem;
        }
      });
      this.title = "选择导入模板的数据列";
      this.dialogVisible_checkbox=true;
    },
    exportMo() {
      this.dialogVisible_checkbox=false;
      Message.success("正在下载模板");
      let url = "/participants/basic/exportMoPar_Group?activityid="+this.activityID+"&dymatic_list="+this.dymatic_list+"&scoreitem="+this.scoreitem+"&infoitem="+this.infoitem;
      window.open(url, '_parent');
    },
    showEditEmpView(data) {
      this.title = "编辑选手信息";
      this.emp = data;
      this.dialogVisible = true;
    },
    Delete(data) {
      //console.log(data)
      data.institutionid= this.user.institutionID;
      this.$confirm('此操作将永久删除【' + data.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.postRequest("/participants/basic/delete?groupID="+this.groupID,data).then(resp => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        })
      })
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.getCurrentParticipants();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.getCurrentParticipants('advanced');
    },
    getCurrentParticipants(){
      let begin = (this.page - 1) * this.size;
      let end = this.page * this.size;
      this.currentParticipants = this.participants.slice(begin, end);
    },
    initEmps() {
      this.loading = true;
      let url = '/participants/basic/?page=' + 1 + '&size=' + 1000+ '&groupID=' + this.groupID+ '&activitiesID=' + this.activityID;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          //console.log("aha",resp);
          this.emps = resp.data;
          console.log(this.emps);
        }
      });
      this.initParticipants();
      if (this.mode==='secretarySub')
        this.initParentGroup();
    },
    initParticipants(){
      this.loading = true;
      let url = '/participants/basic/getByInstitutionID/?institutionID=' + this.user.institutionID;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.participants = resp.obj;
          this.total = this.participants.length;
          console.log(this.participants);
        }
      });
    },
    initParentGroup(){
      this.loading = true;
      let url = '/participants/basic/?page=' + 1 + '&size=' + 1000+ '&groupID=' + this.groupIDParent+ '&activitiesID=' + this.activityID;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.parentGroup = resp.data;
          console.log(this.parentGroup);
        }
      });
    },
    back(){
      const _this = this;
      var url;
      if (this.mode==='admin')
        url="/ActivitM/table"
      else if (this.mode==='secretary')
        url="/secretary/ActManage"
      _this.$router.push({
        path: url,
        query: {
          keywords: this.activityID,
          keyword_name: this.ACNAME,
          groupID:this.groupID,
          mode:this.mode,
        },
      });
    },
    alterDisplay(row){
      row.institutionid= this.user.institutionID;
      const _this = this;
      //console.log(row);
      this.postRequest("/participants/basic/alterDisplay?total="+this.total+"&groupID="+this.groupID,row).then((resp) => {
        this.refreshact();
      });
    },
    refreshact() {
      this.initEmps();
      /*this.reload();
      this.clear();*/
    },
    save() {
      this.emp.institutionid= this.user.institutionID;
      this.dialogVisible = false;
      const _this = this;
      // console.log("-----------",_this.hrs);
      //this.$router.push({name:'/scoreItem/basic/update',params:{scoreItem:_this.hrs,total:_this.total}})
      this.postRequest("/participants/basic/update", _this.emp).then((resp) => {
      });
    },
    showGroupmanagement(data){
      const _this = this
      _this.$router.push({
        path: '/sal/table',
        query:{
          keywords: data.id,
          keyword_name: data.name
        }
      })
    },
    showScoreItem(data){
      const _this = this
      _this.$router.push({
        path: '/sal/month',
        query:{
          keywords: data.id,
          keyword_name: data.name
        }
      })
    },
    change(){
      const _this = this
      _this.$router.push({
        path: '/ActivitM/sobcfg',
        query:{
          keywords: this.keywords,
          keyword_name: this.keyword_name,
          groupID:this.groupID,
          ACNAME:this.keyword_name,
          mode:this.mode,
        }
      })
    },
    showMethod(){
      this.title = "添加选手";
      this.dialogVisible_method=true;
      this.getCurrentParticipants();
    },
    handleSelectionChange(val){
      this.multipleSelection=val;
    },
    add(){
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择选手!'
        });
        return;
      }
      this.dialogVisible_method = false;
      const _this = this;
      this.postRequest("/participants/basic/addPars?activityID="+this.keywords + "&groupID=" + this.groupID,_this.multipleSelection).then((resp) => {
        if (resp) {
          this.initEmps();
          this.$message({
            type: 'success',
            message: '添加成功!'
          });
        }
      });
    }
    // searchEmps() {
    //   this.loading = true;
    //   console.log(this.keyword);
    //   const _this = this
    //   //let url =
    //   this.getRequest("/activities/basic/search?company="+this.keyword+"&page=" + this.page + '&size=' + this.size).then(resp => {
    //     this.loading = false;
    //     if (resp) {
    //       this.emps = resp.data;
    //       this.total = resp.total;
    //     }
    //   });

    // }
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

.el-pagination {
  text-align: center;
}

</style>

