<template>
  <div>
    <div>
      {{keywords_name}}活动<br/><br/>
      选手导入可以分多次进行。<br/>
      选手第一次导入时，可先不分组。此时可以将导入表格中的“分组名称”留空，进行导入操作。待分组后，再导入一次，从而实现分组。
      选手的信息项以及评分项，也可在选手第一次导入时留空，待第二次、第三次（或之后）导入时填入那些信息。<br/>
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
          <span style="font-weight:600;">导入新数据</span> 第一步：
          <el-button type="primary" @click="exportCheckbox" icon="el-icon-upload" style="margin-right: 8px">
            下载模板
          </el-button>
          第二步：
<!--          导入选手按钮-->
          <el-upload
              :show-file-list="false"
              :before-upload="beforeUpload"
              :on-success="onSuccess"
              :on-error="onError"
              :disabled="importDataDisabled"
              style="display: inline-flex;"
              action="#"
              :http-request="handleChange">
            <el-button :disabled="importDataDisabled" type="primary" :icon="importDataBtnIcon">
              {{importDataBtnText}}
            </el-button>
          </el-upload>

          <el-button type="primary" @click="groupsForParticipant" icon="el-icon-upload" style="margin-left: 15px">
            选手分组
          </el-button>
          <el-button type="danger" icon="el-icon-delete" @click="deleteGroupsOfParticipant"  style="margin-left: 15px">
            删除分组
          </el-button>
        </div>
        <div>
          <el-button type="primary" @click="exportData" :loading="loading" icon="el-icon-download">
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
            prop="oldgroupname"
            fixed
            align="left"
            label="组名"
            width="100px">
        </el-table-column>
        <el-table-column
            prop="displaySequence"
            fixed
            align="left"
            label="序号"
            width="50px">
        </el-table-column>
        <el-table-column
            prop="code"
            align="left"
            label="编号"
            width="200px">
        </el-table-column>
        <el-table-column
            prop="idnumber"
            align="left"
            label="身份证号码"
            width="200px">
        </el-table-column>
        <el-table-column
            prop="name"
            align="left"
            label="姓名"
            width="100px">
        </el-table-column>
        <el-table-column
            prop="score"
            label="得分"
            align="center"
            width="80px">
        </el-table-column>
        <el-table-column align="left" min-width="10%" label="操作">
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
      <div style="display: flex;justify-content: flex-end;margin:10px 0">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total">
        </el-pagination>
      </div>
    </div>


    <el-dialog title="选手分组" :visible.sync="dialogPartipicantGroups" width="70%" center
               @close='closeDialogGroupOfParticipant'>
      <div>
        <div>
          <span>请选择进行分组的信息项：</span>
          <el-select
              style="margin-right: 20px;width: 120px;"
              v-model="selectedGroupInfo"
          >
            <el-option
                v-for="(item,key,index) in groupInfoNums"
                :key="key"
                :label="key"
                :value="key">
            </el-option>
          </el-select>
          <template>
            <span>请选择分组个数：</span>
            <el-select
                style="margin-right: 20px;width: 150px;"
                v-model="selectedGroupNums"
            >
              <el-option
                  v-for="item in groupNums"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </template>
          <template v-if="groupSubOfSelectedInfos.length != 0">
            <span>请对{{selectedGroupInfo}}选择细分：</span>
            <el-select
                style="margin-right: 20px;width: 150px;"
                v-model="selectedSubGroupInfo"
                multiple
            >
              <el-option
                  v-for="item in groupSubOfSelectedInfos"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </template>
          <br>

          <div style="margin-top: 8px">
            <template>
              <el-radio v-model="radio" label="1">均分每组人数</el-radio>
              <el-radio v-model="radio" label="2">指定每组人数</el-radio>
            </template>
          </div>

          <div id="tableOfGroupNums"
               v-show="(this.selectedGroupInfo != '' && this.selectedSubGroupInfo.length > 0) ||
                        (this.groupSubOfSelectedInfos.length == 0 && this.selectedGroupInfo != '') ? true:false"
               style="margin: auto;width: 100%;position: relative;text-align: center;margin-top: 8px">
            <div v-for="item in groupNumsInput" style="margin: auto;margin-top: 6px;width: 100%">
              第{{item.idx + 1}}组：
              <input v-model="item.value" ></input>
            </div>
          </div>

          <div style="margin-top: 10px;text-align: center;width: 100%">
            <el-button @click="creatGroup">
              创建分组
            </el-button>
          </div>

        </div>
<!--        对话框里显示没有分组的学生-->
<!--        <div style="margin-top: 10px">-->
<!--          <el-table-->
<!--              :data="filterNoGroupPar"-->
<!--          >-->
<!--            <el-table-column-->
<!--                label="学生ID"-->
<!--                prop="studentID">-->

<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--                label="活动ID"-->
<!--                prop="activityID">-->

<!--            </el-table-column><el-table-column-->
<!--              label="选手ID"-->
<!--              prop="id">-->

<!--          </el-table-column><el-table-column-->
<!--              label="code"-->
<!--              prop="code">-->

<!--          </el-table-column>-->
<!--          </el-table>-->
<!--        </div>-->
      </div>

    </el-dialog>


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
            :key="Math.random()"
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
        <el-form-item label="编号:" prop="code">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.code"
              placeholder="请输入编号"
          ></el-input>
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
    <el-dialog :title="title" :visible.sync="dialogVisible_checkbox" width="60%" center>
      <div style="font-size: 17px;">
        导入模板中必须包含身份证号和姓名，以下勾选的列将包含在导入模板中。模板中不包含的列，则导入时将保持数据库中已有信息不变。
        <br/>
        首次导入请注意！如果student表中已有该选手的记录，则手机号、邮箱、属于本单位三列可为空，“属于本单位”列填是或否。用户名密码可以不填写，若不填写第一次导入将默认为编号和手机号，其余必须填写。
      </div><br/>
      <div style="font-size: 16px;margin-left: 15%">基本信息：<br/>
      <el-checkbox label="姓名" key="姓名" v-model="dymatic_list" disabled style="width: 150px">姓名</el-checkbox>
      <el-checkbox label="身份证号码" v-model="dymatic_list" disabled style="width: 150px">身份证号码</el-checkbox>
      <el-checkbox label="编号" v-model="dymatic_list"  style="width: 150px">编号</el-checkbox>
      <el-checkbox label="序号" v-model="dymatic_list"  style="width: 150px">序号</el-checkbox>
      <el-checkbox label="手机号" v-model="dymatic_list"  style="width: 150px">手机号</el-checkbox>
      <el-checkbox label="邮箱" v-model="dymatic_list"  style="width: 150px">邮箱</el-checkbox>
      <el-checkbox label="属于本单位" v-model="dymatic_list"  style="width: 150px">属于本单位</el-checkbox>
      <el-checkbox label="组名" v-model="dymatic_list"  style="width: 150px">组名</el-checkbox>
      <el-checkbox label="用户名" v-model="dymatic_list"  style="width: 150px">用户名</el-checkbox>
      <el-checkbox label="密码" v-model="dymatic_list"  style="width: 150px">密码</el-checkbox>
      </div><br/>
      <div style="font-size: 16px;margin-left: 15%">信息项：<br/>
        <el-checkbox v-for="item in infoitem_from_back" :key="item.name" :label="item.name" v-model="infoitem" style="width: 150px">
        </el-checkbox>
      </div><br/>
      <div style="font-size: 16px;margin-left: 15%">评分项：<br/>
        <el-checkbox v-for="item in scoreitem_from_back" :key="item.name" :label="item.name" v-model="scoreitem" style="width: 150px">
        </el-checkbox>
      </div>
<!--      <div>基本信息：{{dymatic_list}}<br/>信息项：{{scoreitem}}<br/>评分项：{{infoitem}}</div>-->
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportMo">下载</el-button>
          <el-button @click="dialogVisible_checkbox = false">关闭</el-button>
        </span>
    </el-dialog>

  </div>
</template>

<script>
import {Message} from "element-ui";
import axios from 'axios'
export default {
  name: "SalGroup",
  data() {
    return{
      groupNumsInput:[],//用于存放选择分为几组后的div和input框
      radio:'1',
      filterNoGroupPar:[],//对话框中的展示table数据
      groupSubOfSelectedInfos:[],//对子信息项再进行选择
      selectedSubGroupInfo:[],//选择的子信息项
      selectedGroupNums:null,//选择的分组数量
      groupNums:null,//分数个数列表
      selectedGroupInfo:'',//被选择的信息项
      groupInfoNums: {},//所有信息项 选项
      dialogPartipicantGroups:false,//选手分组的对话框
      title: '',
      labelPosition: "left",
      importDataBtnText: '导入选手',
      importDataBtnIcon: 'el-icon-plus',
      importDataDisabled: false,
      showAdvanceSearchView: false,
      show:false,
      percentage: '',
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
      total: 0,
      page: 1,
      keywords: '',
      keyword: '',
      activityID: '',
      groupID: '',
      size: 10,
      positions: [],
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
      return JSON.parse(localStorage.getItem('user'));//object信息
    }
  },
  watch:{
    selectedGroupNums:{//监听用户选择了分为几组
      handler(val){
        this.groupNumsInput = []
        for(var i = 0;i < val;i ++){
          this.groupNumsInput.push({
            idx:i,
            value:''
          })
        }
        //信息项和子信息项都被选择，或者信息项被选择并且没有子信息项
        // if((this.selectedGroupInfo != '' && this.selectedSubGroupInfo.length > 0) ||
        //     (this.groupSubOfSelectedInfos.length == 0 && this.selectedGroupInfo != '')){
        //   this.calculationGroupInputValue()
        // }
      }
    },
    radio:{
      handler(val){
        if(this.radio == '1'){//均分

        }else//指定每组人数
        {

        }
      }
    },
    selectedGroupInfo:{//监听第一个下拉框的变化 信息项
      handler(val){
        if(this.selectedSubGroupInfo.length!=0){//信息项和信息项的子选项都被选择了
          this.filterNoGroupParticipants()
        }else {
          this.groupSubOfSelectedInfos = []
          //取出选择的信息项的所有内容content
          if(this.groupInfoNums[val].length!=0) {
            for (var i = 0; i < this.groupInfoNums[val].length; i++) {
              if (this.groupSubOfSelectedInfos.indexOf(this.groupInfoNums[val][i].content) == -1 &&
                  this.groupInfoNums[val][i].content != '') {
                this.groupSubOfSelectedInfos.push(this.groupInfoNums[val][i].content)
              }
            }
          }
        }
      }
    },
    selectedSubGroupInfo:{//第二个下拉框的变化 信息项的细分
      handler(val){
        if(this.selectedGroupInfo != ''){//信息项和信息项的子选项都被选择了
          this.filterNoGroupParticipants()
        }
      }
    }
  },
  created() {
  },
  mounted() {
    //this.init();//先获得评分项
    this.activityID = this.$route.query.activityID;
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.ACNAME=this.$route.query.keywords_name;
    this.initEmps();
  },
  methods: {
    deleteGroupsOfParticipant(){//删除分组
      // console.log(this.emps)
      var flag = false//判断当前的选手列表中是否已经分过组
      for(var item of this.emps){
        if(item.groupID != null && item.groupID != 0){
          flag = true
          break;
        }
      }
      if(flag){
        this.$confirm('确定删除分组？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          var url = '/participants/basic/deleteGroups?activityID=' + this.activityID
          this.getRequest(url).then(()=>{
            if(resp){
              this.$message.success('删除成功')
            }
          })
        })
        .catch(()=>{

        })
      }else {
        this.$message.warning('该活动还未分组！无法删除')
      }
    },
    calculationGroupInputValue(){//计算选择组数和单选按钮后input框的赋值
      console.log(this.filterNoGroupPar)
      var participantNums = this.filterNoGroupPar.length//选手人数
      var groupNums = this.selectedGroupNums//组数
      if(this.radio == '1' && this.selectedGroupNums != null){//均分
        var baseNums = Math.floor(participantNums / groupNums);
        for(var i = 0;i<groupNums;i++){
          this.groupNumsInput[i].value = baseNums;
        }
        for(var i = 0;i<(participantNums - baseNums * groupNums);i ++){
          this.groupNumsInput[i].value ++;
        }
      }else if(this.radio == '2'){//指定人数 groupNumsInput保存每组的指定人数

      }

    },
    closeDialogGroupOfParticipant(){//选手分组对话框关闭,清空遗留数据
      this.selectedGroupInfo = ''
      this.selectedSubGroupInfo = []
      this.selectedGroupNums = 0
      this.filterNoGroupPar = []
    },
    filterNoGroupParticipants(){//信息项和分为几组已经选择
      //通过2个id找infos的选手id， 通过活动id，选手id找没有分组的选手，返回信息
      if(this.selectedSubGroupInfo.length == 0 || this.selectedGroupInfo == ''){
        return
      }
      var data = {
        'activityID':[this.keywords.toString()],
        'infoItemID':[this.groupInfoNums[this.selectedGroupInfo][0].id.toString()],
        'infoContent':this.selectedSubGroupInfo
      }
      var url = '/info/basic/getPartipicantIDByInfos'
      this.postRequest(url,data).then((res)=>{
        if(res){
          console.log('res:')
          console.log(res.obj)
          this.filterNoGroupPar = res.obj//存放没有分好组的选手，但是不显示在页面上
          if(this.filterNoGroupPar.length == 0){
            this.$message.warning('该信息项下已分组!')
          }
          this.calculationGroupInputValue()
        }
      })

    },
    groupsForParticipant(){//选手分组,点击按钮对话框弹出 //获得该活动下的所有Infoitem，其中包括content
      var flag = false//判断当前的选手列表中是否已经分过组
      if(this.emps != null && this.emps.length != 0){
        for(var item of this.emps){
          if(item.groupID != null && item.groupID != 0){
            flag = true
            break;
          }
        }
      }else {
        this.$message.warning('请先导入选手！')
        return
      }
      if(!flag){
        this.groupSubOfSelectedInfos = []
        var url = '/infoItem/basic/getAll/' + this.keywords
        this.getRequest(url).then((resp)=>{
          if(resp){
            for(var i = 0;i < resp.obj.length;i ++){
              if(!(resp.obj[i].name in this.groupInfoNums)){
                this.groupInfoNums[resp.obj[i].name]=[]
              }
              this.groupInfoNums[resp.obj[i].name].push(resp.obj[i])//将每一个信息项改为对象形式
            }
            // console.log(this.groupInfoNums)
            if(!this.groupNums){
              this.groupNums = Array.from(Array(10).keys(),n=>n+1)
            }
            this.dialogPartipicantGroups = true
          }
        })
      }else {
        this.$message.warning('请先删除分组！')
      }

    },
    creatGroup(){//创建分组
      //传递activityID和选手id，分为几组和每组人数

    },
    /** 查询角色列表 */
    onError(err, file, fileList) {
      this.importDataBtnText = '导入选手';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
    },
    onSuccess(res) {
      // console.log(res);
      this.$message(res.msg);
      this.importDataBtnText = '导入选手';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
      this.initEmps();
    },
    beforeUpload() {
      let url = "/participants/basic/check?groupid=0";
      //let url = "/participants/basic/import?groupid=0"+"&activityid="+this.keywords+"&insititutionID="+this.user.institutionID;
      return  url;
      this.importDataBtnText = '正在导入';
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataDisabled = true;
    },
    handleChange(file) {
      this.show = true;
      var that = this
      let fd = new FormData();
      let fileName = file.file.name + new Date().getTime();
      fd.append("file", file.file);
      fd.append("key", fileName);
      let url = "/participants/basic/check?groupid=0";
      this.postRequest(url, fd, {
            headers: {
              "Content-Type": "multipart/form-data",
              'token':this.user.token
            },
          })
          .then((res1) => {
            if(res1.length===0)
            {
              url = "/participants/basic/import?groupid=0"+"&activityid="+this.keywords+"&insititutionID="+this.user.institutionID;
              axios.post(url, fd, {
                headers: {
                  "Content-Type": "multipart/form-data",
                  'token':that.user.token
                },
              }).then((res) => {
                this.$message(res.msg);
              })
            }
            else{
              let newD=[],h=this.$createElement;
              newD.push(h('p',null,'导入数据中'));
              for(const i in res1)
              {
                newD.push(h('p',null,res1[i]))
              }
              newD.push(h('p',null,'为空，以上列数据会被置空，是否确认继续?'));
              this.$confirm(h('div',null,newD), '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                url = "/participants/basic/import?groupid=0"+"&activityid="+this.keywords+"&insititutionID="+this.user.institutionID;
                axios.post(url, fd, {
                  headers: {
                    "Content-Type": "multipart/form-data",
                    'token':that.user.token
                  },
                }).then((res) => {
                  this.initEmps();
                  this.$message(res.msg);
                })
              })
            }
          })
          .catch((err) => {
            console.log(err);
          });

    },

    exportData() {
      this.loading=true;
      Message.success("正在导出");
      let url = "/participants/basic/export?activityID="+this.keywords;
      window.open(url, '_parent');
      this.loading=false;
    },
    exportCheckbox() {
      let url = "/participants/basic/getItem?activityid="+this.keywords;
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
      let url = "/participants/basic/exportMoPar_Group?activityid="+this.keywords+"&dymatic_list="+this.dymatic_list+"&scoreitem="+this.scoreitem+"&infoitem="+this.infoitem;
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
        this.postRequest("/participants/basic/delete?groupID="+data.groupID,data).then(resp => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        })
      })
    },
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
      let url = '/participants/basic/?page=' + this.page + '&size=' + this.size+ '&groupID=0'+ '&activitiesID='+this.keywords;
      //console.log(url);
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          //console.log("aha",resp);
          this.emps = resp.data;
          this.total = resp.total;
          //console.log("total",this.total);
        }
      });
    },
    back(){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/search",
      });
    },
    refreshact() {
      this.initEmps();
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

