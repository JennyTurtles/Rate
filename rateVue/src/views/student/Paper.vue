<template>
  <div>
    <div>
      <div
        style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <!-- <el-input
            placeholder="请输入论文名称进行搜索，可以直接回车搜索..."
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
          </el-button> -->
          <!-- <el-button
            icon="el-icon-refresh"
            type="primary"
            @click="initEmps"
            :disabled="showAdvanceSearchView"
          >
            重置
          </el-button> -->
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加论文
          </el-button>
        </div>
        <!-- <div>
          <el-upload
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            :on-error="onError"
            :disabled="importDataDisabled"
            style="display: inline-flex; margin-right: 8px"
            action="/employee/basic/import"
          >
            <el-button
              :disabled="importDataDisabled"
              type="primary"
              :icon="importDataBtnIcon"
            >
              {{ importDataBtnText }}
            </el-button>
          </el-upload> -->
          <!--<el-button type="primary" @click="exportData" icon="el-icon-download">
            导出数据
          </el-button>-->
        <!-- </div> -->
      </div>
    </div>
    <div style="margin-top: 10px;">
      <el-table
        :data="emps"
        stripe
        border
        v-loading="loading"
        :header-cell-style="rowClass"
        element-loading-text="正在加载..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.12)"
        style="width: 100%;"
      >
        <el-table-column type="selection" width="35px"> </el-table-column>
        <el-table-column
          prop="name"
          align="center"
          label="论文名称"
          width="200px"
        >
        </el-table-column>
        <!-- width="200" -->
        <!-- <el-table-column
          prop="studentID"
          fixed
          align="center"
          label="所属人学号"
          width="200"
        >
        </el-table-column> -->
        <el-table-column
          prop="state"
          label="状态"
          width="70px"
          align="center"
        >
        <template slot-scope="scope">
            <span
              style="padding: 4px"
              :style="scope.row.state=='tea_reject' ? {'color':'red'}:{'color':'gray'}"
              size="mini"
              >
              {{scope.row.state=="commit" 
              ? "已提交"
              :scope.row.state=="tea_pass"
              ? "导师通过"
              :scope.row.state=="tea_reject"
              ? "导师驳回"
              :scope.row.state=="adm_pass"
              ? "管理员通过"
              :"管理员驳回"}}
              </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="publication.name"
          label="发表刊物"
          align="center"
          width="250px"
        >
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注"
          align="center"
          style="width:220px"
        >
        <!-- <span v-for="(item,index) in remarksort" :key="index"
        style="white-space: pre-wrap;">
          {{item}}
        </span> -->
        <!-- <template slot-scope="scope">
            <el-tooltip placement="top" :content="scope.row.remark">
              <p v-for="(item,index) in scope.row.remark" :key="index"
                style="overflow:hidden;text-overflow:ellipsis;
                work-break:break-all;display: -webkit-box;
                -webkit-line-clamp: 3;
                -webkit-box-orient: vertical;"
              >{{item}}</p>
            </el-tooltip>
        </template> -->
        <!-- 驳回理由 -->
        </el-table-column>
        <el-table-column
          prop="point"
          label="积分"
          align="center"
          width="80px"
        >
        </el-table-column>
        <el-table-column align="center" width="280px" label="操 作">
          <template slot-scope="scope">
            <el-button
              @click="showEditEmpView(scope.row)"
              style="padding: 4px"
              size="mini"
              icon="el-icon-edit"
              type="primary"
              plain
              v-show="scope.row.state == 'commit' || scope.row.state == 'tea_reject'? true:false"
              >编辑</el-button
            >
            
             <!-- <el-button
              @click="tijiao(scope.row)"
              style="padding: 4px"
              size="mini"
              icon="el-icon-edit"
              type="primary"
              plain
              v-show="scope.row.state == 'tea_reject' ? true:false"
              >提交审核</el-button
            > -->
            <!-- <el-button
              @click="showScoreItem(scope.row)"
              style="padding: 4px"
              size="mini"
              icon="el-icon-tickets"
              type="primary"
              plain
              >评分项设置</el-button
            > -->
            <!-- <el-button
              @click="showGroupmanagement(scope.row)"
              style="padding: 4px"
              size="mini"
              icon="el-icon-s-operation"
              type="primary"
              plain
              >分组管理</el-button
            > -->
            <!-- <el-button
              @click="showInsertmanagement(scope.row)"
              style="padding: 4px"
              size="mini"
              icon="el-icon-plus"
              type="primary"
              plain
              >导入选手</el-button
            > -->
            <!-- <el-button
                @click="showteachermanagement(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >导入专家</el-button
            > -->
            <!-- <el-button
              @click="deleteEmp(scope.row)"
              style="padding: 4px"
              size="mini"
              type="danger"
              icon="el-icon-circle-close"
              plain
              >结束论文</el-button
            > -->
            <el-button
              @click="deleteEmp(scope.row)"
              style="padding: 4px"
              size="mini"
              type="danger"
              icon="el-icon-delete"
              plain
              v-show="scope.row.state == 'tea_reject' || scope.row.state == 'commit'? true:false"
              >删除</el-button
            >
            <el-button
              @click="showInfo(scope.row)"
              style="padding: 4px"
              size="mini"
              >查看详情</el-button
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

<!-- 添加论文对话框 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
        :hide-required-asterisk="true"
        :label-position="labelPosition"
        label-width="150px"
        :model="emp"
        :rules="rules"
        ref="empForm"        
      >
        <el-form-item label="论文名称:" prop="name" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
            size="mini"
            style="width:80%"
            prefix-icon="el-icon-edit"
            v-model="emp.name"
            placeholder="请输入论文名称"
          ></el-input>
        </el-form-item>
        <el-form-item  prop="year" label="出版年月:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-date-picker
              style="width:100%"
              v-model="data_picker"
              type="month"
              placeholder="请选择出版年"
              @change="timechange"
          ></el-date-picker>
        </el-form-item>
        <el-form-item  label="所属期刊:" prop="name" label-width="80px" style="margin-left: 20px;">
            <span class="isMust">*</span>
            <div>
              <el-select
              :disabled="data_picker === ''"
              id="input_publicationName"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="publicationName"
              clearable
              filterable
              @change="select_pub"
              placeholder="请输入论文所属期刊"
              >
                <el-option
                  style="width:100%"
                  v-for="val in options"
                  :key="val.index"
                  :value="JSON.stringify({'name':val.value,'point':val.point,'pubId':val.index})"
                  :label="val.value"
                >
                </el-option>
              </el-select>
            </div>
            
        </el-form-item>
       
        <el-form-item  prop="total" label="作者列表:" label-width="80px" style="margin-left: 20px;">
            <el-input
            id="input_writer"
            size="mini"
            style="width:80%"
            prefix-icon="el-icon-edit"
            v-model="writer"
            @blur="judgeWriter()"
            placeholder="请输入作者,如有多个用分号分隔"
            ></el-input> 
        </el-form-item>

        <el-form-item  prop="content" label="页码:" label-width="80px" style="margin-left: 20px;">
          <el-input
            size="mini"
            style="width: 32%"
            prefix-icon="el-icon-edit"
            v-model="startPage"
            placeholder="开始页码"
          ></el-input>  &nbsp;-&nbsp;  <el-input
            size="mini"
            style="width: 32%"
            prefix-icon="el-icon-edit"
            v-model="endPage"
            placeholder="结尾页码"
          ></el-input>
        </el-form-item>      
        <el-form-item label="证明材料:" prop="url" label-width="80px" style="margin-left: 20px;">
          <!-- <el-input
            id="url_input"
            size="mini"
            style="width: 420px"
            prefix-icon="el-icon-edit"
            v-model="urlFile"
            placeholder="请选择文件"
          ></el-input> -->
           <!-- <input type="file" v-on:change="up" ref="inputer" name="file" multiple required="required"
           style="margin-left:10px" /> -->
           <el-upload
                :file-list="files"
                action="#" 
                :limit="1"
                :headers="headers"
                :on-remove="handleDelete"
                :auto-upload="false"
                :on-change="handleChangeFiles"
                :on-exceed="handleExceed"
              >
                <el-button type="primary" icon="el-icon-upload2"
                    slot="trigger"
                >选择文件</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
                <span style="color:gray;font-size:11px">只允许doc docx pdf jpg png jpe rar zip类型文件
                  &nbsp;&nbsp;大小不能超过10MB
                </span>
                <!-- <el-button type="primary" icon="el-icon-upload2"
                    @click="up()" :disabled="files.length < 1"
                  >上传文件
                </el-button> -->
              </el-upload>
              <div>
                
              </div>
              
        </el-form-item>
      </el-form>
      <div style="margin-left: 20px;">
          <span style="color:gray;font-size:10px">将会获得：{{view_point}}积分</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp()" v-show="addButtonState">提 交</el-button>
      </span>
    </el-dialog>

    <el-dialog
      :title="title_show"
      :visible.sync="dialogVisible_show"
      width="25%"
      center>
      <el-form
        :label-position="labelPosition"
        label-width="120px"
        :model="emp"
        :rules="rules"
        ref="empForm"
        style="margin-left: 60px">

        <el-form-item label="论文名:" prop="name">
          <span>{{ emp.name }}</span
          ><br />
        </el-form-item>
        <!-- <el-form-item label="所属人id:" prop="studentID">
          <span>{{ emp.studentID }}</span
          ><br />
        </el-form-item> -->
        <el-form-item label="出版年:">
          <span>{{ emp.year }}</span>
        </el-form-item>
        <el-form-item label="出版月:">
          <span>{{ emp.month }}</span>
        </el-form-item>
        <el-form-item label="第一作者:">
          <span>{{ emp.total }}</span>
        </el-form-item>
        <el-form-item label="积分:">
          <span>{{ emp.point }}</span>
        </el-form-item>
        <el-form-item label="路径">
          <span>{{ emp.url }}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible_show = false"
          >关 闭</el-button
        >
      </span>
    </el-dialog>

    <!-- 查看详情按钮 -->
     <el-dialog
      class="showInfo_dialog"
      :title="title_show"
      :visible.sync="dialogVisible_showInfo"
      width="520px"
      center>
      <el-form
        :label-position="labelPosition"
        label-width="80px"
        :model="emp"
        :rules="rules"
        ref="empForm"
        style="margin-left: 20px"
      >
         <el-form-item label="论文名:" prop="name">
          <span>{{ emp.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="积分:" prop="point">
          <span>{{emp.point}}</span
          ><br />
        </el-form-item>
        <el-form-item label="论文状态:" prop="state">
          <span>{{emp.state}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者人数:" prop="total">
          <span>{{emp.total}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者列表:" prop="author">
          <span>{{emp.author}}</span
          ><br />
        </el-form-item>
        <el-form-item label="排名:" prop="rank">
          <span>{{emp.rank}}</span
          ><br />
        </el-form-item>
        <el-form-item label="发表年份:" prop="year">
          <span>{{emp.year}}</span
          ><br />
        </el-form-item>
        <el-form-item label="发表月份:" prop="month">
          <span>{{emp.month}}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <!-- <el-button @click="download(emp)" type="primary" v-show="emp.url == '' || emp.url == null ? false:true">下载材料</el-button> -->
          &nbsp;&nbsp;&nbsp;&nbsp;
          <span v-if="emp.url == '' || emp.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer" @click="download(emp)">{{emp.url|fileNameFilter}}</a>
          <br />
        </el-form-item>
        <div >
          <span>历史操作:</span>
          <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;font-size:5px;margin-left:5px">
          <!-- <el-form-item  v-model="operList" v-for="item in operList" :key="item.time" style="margin-bottom:8px;"> -->
              <div >
                <p>{{item.time|dataFormat}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operatorName}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operation}}</p>
                <p v-show="item.remark == '' ? false : true">驳回理由：{{item.remark}}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form> 
      <span slot="footer" class="dialog-footer">
        <el-button
              id="but_reject"
              @click="dialogVisible_showInfo= false"
              type="primary"
              >关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
// import { delete } from 'vue/types/umd';
import axios from "axios";
import {postRequest1} from "@/utils/api";
export default {
  name: "SalSearch",
  data() {
    return {
      empPaperName:'',//添加论文中的论文名称
      view_point:0,
      headers: {
					'Content-Type': 'multipart/form-data'
			},
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      addButtonState:true,//是否允许添加论文
      operList:[],//每个论文的历史操作记录
      remarksort:[],//对显示的驳回理由做排序
      writer:'',//和输入的作者列表绑定
      options:[],//存储所有刊物对象
      data_picker:"",//选择时间
      ulList:false,
      labelPosition: "left",
      title: "",
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
      dialogVisible_showInfo: false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      positions: [],
      publicationName:"",//所属期刊
      publicationID:-1,
      startPage:'',
      endPage:'',
      // tutorName:JSON.parse(sessionStorage.getItem('user')).teachers.name,//导师名字
      oper:{
        operatorRole:"student",
        operatorID:JSON.parse(localStorage.getItem('user')).id,
        operatorName:JSON.parse(localStorage.getItem('user')).name,
        paperID:null,
        paperName:null,
        pubID:null,
        pubName:null,
        operation:"",
        state:"",
        remark:""
      },
      emp: {
        id: null,
        institutionID: null,
        name: null,//论文名称
        year: "",
        month: "",
        rank: "",//排名
        total: "",//总人数
        content: "",
        url:'',
        state:'',
        pubPage:'',
        publicationID:null,
        
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        name: [{ required: true, message: "请输入论文名", trigger: "blur" }],
        year: [
          { required: true, message: "请输入出版年份", trigger: "blur" },
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
        comment: [{ required: true, message: "请输入备注", trigger: "blur" }],
      },
    };
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
  },
  created() {},
  mounted() {
    this.initEmps();
    this.initPositions();
  },
  filters:{
    fileNameFilter:function(data){//将证明材料显示出来
      if(data == null || data == ''){
        return '无证明材料'
      }else{
        var arr= data.split('/')
        return  arr.reverse()[0]
      }
    }
  },
  methods: {
    getPublicationByYear(year){
      console.log("year")
      console.log(year.year)
    },
    download(data){//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      console.log(fileName);
      if(localStorage.getItem("user")){
        var url="/paper/basic/download?fileUrl=" + data.url + "&fileName=" + fileName
        window.location.href = encodeURI(url);
      }else{
        this.$message.error("请重新登录！");
      }
      
    },
    handleDelete() {//删除选择的文件
      var file={
        filepath:this.urlFile
      }
      this.postRequest1("/paper/basic/deleteFile",file).then(
        (response)=>{
          console.log(response)
        },()=>{}
      )
    },
    handleExceed(){//超过限制数量
      this.$message.error(`只允许上传1个文件`);
    },
    handleChangeFiles(file,fileList){//文件列表数量改变
      this.files=[]
      var attachmentType = [
          "doc","docx","pdf","jpg","png","jpeg","rar","zip"]
      var type=file.name.split('.')
      if(file.size > 10*1024*1024){
        this.$message.error('上传文件大小不能超过10MB!');
        return
      }
      if(attachmentType.indexOf(type[type.length-1].toLowerCase()) === -1) {
        this.$message.error("不支持上传该类型的附件")
        return
      }
      var formData=new FormData();
      this.files.push(file);
      formData.append("file",this.files[0].raw)
      axios.post("/paper/basic/upload",formData,{
        headers:{
          // 'token': window.sessionStorage.getItem('user') ? JSON.parse(window.sessionStorage.getItem('user')).token : ''
          'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
      }).then(
        (response)=>{
          this.$message({
            message:'上传成功！'
          })
          this.addButtonState = true
          //获取文件路径
          this.urlFile=response.data
          // console.log("response:");
          // console.log(this.urlFile);
        },()=>{}
      )
    },
    select_pub(val){//所属期刊的输入框 触发的事件 点击某个选项
      var val = JSON.parse(val)
      this.publicationName=val.name
      this.view_point = val.point
      this.publicationID = val.pubId
    },
    timechange(picker){//选择日历调用的方法
      var data=new Date(picker)
      this.emp.year=data.getFullYear()
      this.emp.month=data.getMonth()+1
      this.options=[]
      this.publicationName=""
      // 日期选中后获取期刊列表
      this.getRequest("/publication/basic/list/"+this.emp.year).then((resp) => {
        if (resp) {
          for(var i=0;i<resp.data.length;i++){
            this.options.push( //修改
                {
                  index:resp.data[i].id,
                  value:resp.data[i].name,
                  point:resp.data[i].score
                }
            )
          }
        }
      })
    },
    judgeWriter(){//输入作者框 失去焦点触发事件
      // for(var i=0;i<this.options.length;i++){
      //   if(this.options[i].value==this.publicationName){
      //     this.emp.publicationID=this.options[i].index
      //     // this.emp.point = this.options[i].point
      //     this.view_point=this.emp.point
      //       break;
      //   }
      // }
      var val=this.writer;
      var isalph = false//判断输入中是否有英文字母
      for(var i in val){
        var asc = val.charCodeAt(i)
        if(asc >= 65 && asc <= 90 || asc >= 97 && asc <= 122){
            isalph=true
            break
        }
      }
      var num=null
      // var info=JSON.parse(window.sessionStorage.getItem("user"))
      var info=JSON.parse(localStorage.getItem("user"))
      if(val.indexOf("；")>-1 && val.indexOf(";") == -1){//中文
        num=val.split('；')          
      }else if(val.indexOf(";")>-1 && val.indexOf("；") == -1){//英文
        num=val.split(';')       
      }else if(val.indexOf("；")>-1 && val.indexOf(";")>-1){//中英都有
        this.$message.error();('输入不合法请重新输入！')
      }else if(val.indexOf("；") == -1 && val.indexOf(";") == -1){//只有一个人
        if(this.writer != info.name && isalph){
          this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。");
          this.addButtonState=false
        }else{
          this.addButtonState=true
          this.emp.author=this.writer
          this.emp.rank=1
          this.emp.total=1
        }
        return
      }
      
      //判断自己在不在其中
      if(num.indexOf(info.name) == -1 && !isalph){//不在 并且没有英文单词
          this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】");
          this.addButtonState=false
      }else if(num.indexOf(info.name) == -1 && isalph){//不在 里面有英文单词
          this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。");
          this.addButtonState=false
      }else{//自己在里面 自己是一作不用做任何判断 导师无所谓
        if(num.indexOf(info.name) > 0){//自己不是一作 
            if(num.indexOf(info.teachers.name) > 0 || num.indexOf(info.teachers.name) == -1){//导师在作者列表中,不是一作
              this.$confirm(
                "第一作者不是导师【 " + info.teachers.name + " 】！积分将为【0】分",
                "提示",
                {
                  confirmButtonText: "确定",
                  // cancelButtonText: "取消",
                  type: "warning",  
                }
              ).then(() => {//点击确定
                // num=[info.teachers.name,...num]
                // this.writer = num.join(';')
              },()=>{});
                this.emp.point=0
                this.view_point=this.emp.point
            }
        }
        this.addButtonState=true
      }
      this.emp.total=num.length
      if(num.indexOf(info.teachers.name)>-1){
        num.splice(num.indexOf(info.teachers.name),1)
      }
      this.emp.rank=num.indexOf(info.name)+1
      this.emp.author=this.writer 
      console.log(this.writer);
      console.log(this.emp.point);
    },
    // input_wri(){//填入作者 触发的事件
    //   var val=this.writer;
    //   var num=null
    //   if(val.indexOf("；")>-1 && val.indexOf(";") == -1){
    //     num=val.split('；')          
    //   }else if(val.indexOf(";")>-1 && val.indexOf("；") == -1){
    //     num=val.split(';')       
    //   }else if(val.indexOf("；")>-1 && val.indexOf(";")>-1){
    //     this.$message.success('输入不合法请重新输入！')
    //   }else if(val.indexOf("；") == -1 && val.indexOf(";") == -1){
    //     this.emp.rank=1
    //     this.emp.total=1
    //     return
    //   }
    //   var info=JSON.parse(window.sessionStorage.getItem("user"))
    //   this.emp.total=num.length
    //   if(num.indexOf(info.teachers.name)>-1){
    //     num.splice(num.indexOf(info.teachers.name),1)
    //   }
    //   console.log(num)
    //   this.emp.rank=num.indexOf(info.name)+1
    // },
    rowClass(){
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
    exportData() {
      window.open("/employee/basic/export", "_parent");
    },
    emptyEmp() {
      this.emp = {
        // ID: 6003,
        // startDate: null,
        name: "",
        year: "",
        month: "",
        rank: "",
        total: "",
        content: "",
        url:'',
        state:'',
        // reason:"",
        pubPage:'',
        publicationID:''
        // scoreItemCount: "0",
        // comment: "论文备注example：关于xxx的论文",
      };
    },
     mail(e){
      console.log(1);
      this.postRequest('/test/mail/mail?content='+e).then((resp) => {
        console.log(resp)
        // this.loading = false;
        console.log(1)
       
        
      });
    },
    //编辑按钮
    showEditEmpView(data) {
      // this.initPositions();
      this.title = "编辑论文信息";
      this.emp = data;
      this.view_point = data.point
      this.dialogVisible = true;
      this.publicationName=this.emp.publication.name
      this.data_picker= new Date(this.emp.year,this.emp.month)
      this.writer=this.emp.author
      var page = this.emp.pubPage.split('-')
      this.startPage = page[0]
      this.endPage = page[1]
      this.options = []
      this.getRequest("/publication/basic/list/"+data.year).then((resp) => {
        if (resp) {
          for(var i=0;i<resp.data.length;i++){
            this.options.push( //修改
                {
                  index:resp.data[i].id,
                  value:resp.data[i].name,
                  point:resp.data[i].score
                }
            )
          }
        }
      })
      // let url = "/publication/basic/List"
      // this.getRequest(url).then((resp) => {
      //   this.loading = false;
      //   if (resp) {
      //     this.options=[]
      //     for(var i=0;i<resp.data[0].length;i++){ // 待修改
      //       // console.log(resp.data)
      //       this.options.push({index:resp.data[0][i].id,value:resp.data[0][i].name,point:resp.data[1][i]})
      //     }
      //   }
      // });
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
    },
    deleteEmp(data) {//点击删除按钮
      if(confirm(
        "此操作将永久删除【" + data.name + "】, 是否继续?",)){
        axios.delete("/paper/basic/remove/" + data.id).then((resp) => {
          if (resp) {
            console.log(resp)
            this.dialogVisible = false;
            this.initEmps();
          }
        })
      }
    },
    // up(){//上传文件
    //   var formData=new FormData();
    //   this.files.forEach(
    //     (val,index)=>{
    //       formData.append("file",val.raw)
    //     }
    //   )
      
    //   axios.post("/paper/basic/upload",formData).then(
    //     (response)=>{
    //       console.log(response);
    //       this.$message({
    //         message:'上传成功！'
    //       })
    //     },()=>{}
    //   )
    // },
    // doAddEmpConfirm(){
    //   var postData = {
    //     name:this.publicationName,
    //     year:this.emp.yearrate_test.sql
    //   }
    //   axios.post("/paper/basic/score",postData).then((resp) => {
    //     if (resp.data == null)
    //       alert("请检查年份，当前无法获得积分")
    //     else{
    //       this.emp.publicationID = resp.data.id
    //       this.emp.point = resp.data.score
    //       if(confirm("您将获得"+this.emp.point+"分，是否提交？")){
    //         this.doAddEmp()
    //       }
    //     }
    //   });
    // },
    doAddEmp() {//确定添加论文
      if (this.emp.id) {//emptyEmp中没有将id设置为空 所以可以判断
        // if(confirm('确定要提交吗？')){
          var empdata=this.emp
          this.emptyEmp()
          this.$refs["empForm"].validate((valid) => {
              if (valid) {
                this.emp.name=empdata.name
                this.emp.ID=empdata.id
                // this.emp.publication=empdata.publication
                this.emp.publicationID=empdata.publicationID
                this.emp.point = empdata.point
                this.emp.year=empdata.year
                this.emp.month = empdata.month
                this.emp.author=empdata.author
                this.emp.total = empdata.total
                this.emp.rank=empdata.rank
                // this.publicationName=document.getElementById("input_publicationName").value
                this.emp.pubPage=this.startPage+"-"+this.endPage
                this.emp.url = this.urlFile;
                this.emp.state="commit"
                this.emp.studentID = JSON.parse(localStorage.getItem('user')).id
                const _this = this;
                if(this.emp.url == '' ||this.emp.url == null){
                  this.$message({
                    message:'请上传证明材料！'
                  })
                  return
                }
                this.postRequest1("/paper/basic/edit", _this.emp).then(
                  (resp) => {
                    if (resp) {
                      this.dialogVisible = false;
                      this.initEmps();
                      }
                    }
                  );
                }
          });
      } else {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.publicationName=document.getElementById("input_publicationName").value
            console.log("writer:"+this.emp.author);
            this.emp.pubPage=this.startPage+"-"+this.endPage
            this.emp.url = this.urlFile;
            this.emp.state="commit"
            this.emp.point = this.view_point
            this.emp.publicationID = this.publicationID
            // this.emp.name=this.empPaperName
            this.emp.studentID = JSON.parse(localStorage.getItem('user')).id
            const _this = this;
            if(this.emp.url == '' ||this.emp.url == null){
              this.$message.error('请上传证明材料！')
              return
            }
            console.log("emp")
            console.log(_this.emp)
            this.postRequest1("/paper/basic/add",_this.emp).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.doAddOper("commit",this.emp.name,this.publicationName,
                      this.emp.publicationID,resp.data)
                }
              }
            );
          }
        });
      }
    },
    doAddOper(state,paperName,pubName,pubID,paperID) {
      this.oper.state=state
      this.oper.paperName=paperName,
      this.oper.pubName=pubName,
      this.oper.pubID=pubID
      this.oper.operation="提交论文"
      this.oper.paperID=paperID
      console.log("/paperoper/basic/add");
      this.postRequest1("/paperoper/basic/add", this.oper).then(
        (resp) => {
          if (resp) {
            this.initEmps();
          }
        }
      );    
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    showAddEmpView() {//点击添加论文按钮
      this.addButtonState=true
      this.options=[]
      this.view_point = 0
      this.emptyEmp();//532
      this.writer=''
      this.data_picker=''
      this.publicationName=''
      this.empPaperName=''
      this.startPage=''
      this.endPage=''
      this.urlFile=''
      this.title = "添加论文";
      this.dialogVisible = true;//440
      // this.loading = true;
      // let url = "/publication/basic/List"
      // this.getRequest(url).then((resp) => {
      //   this.loading = false;
      //   if (resp) {
      //     for(var i=0;i<resp.data[0].length;i++){
      //       console.log(resp.data)
      //       this.options.push( //修改
      //         {
      //           index:resp.data[0][i].id,
      //           value:resp.data[0][i].name,
      //           point:resp.data[1][i]
      //         }
      //       )
      //     }
      //   }
      // });
    },
    initEmps() {
      this.loading = true;
      let url = "/paper/basic/studentID?studentID="+JSON.parse(localStorage.getItem('user')).id
          // +"&page=" +
          // this.page +
          // "&size=" +
          // this.size;
      this.getRequest(url).then((resp) => {
        console.log(resp)
        this.loading = false;
        if (resp) {
          console.log(resp);
          this.emps = resp.data;
          this.total = 11;
          // this.remarksort=this.emps.remark
        }
      });
    },
    showGroupmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/table",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showInsertmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/group",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showteachermanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showScoreItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/month",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    searchEmps() {
      this.loading = true;
      console.log('---------',this.keyword);
      const _this = this;
      //let url =
      this.getRequest(
        "/paper/basic/byName?name=" +
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
    //查看详情
    showInfo(data){
      this.title_show = "显示详情";
      this.emp=data
      // console.log(data);
      this.dialogVisible_showInfo=true
      this.getRequest("/paperoper/basic/List?ID="+data.id).then((resp) => {
          this.loading = false;
          if (resp) {
            console.log("/paperoper/basic/List?ID=");
            console.log(resp);
            // this.isShowInfo=false
            this.operList=resp.data
            this.operList.sort(function(a,b){
            return a.time > b.time ? -1 : 1
            })
          }
      });
    }
  },
};
</script>

<style>
.showInfo_dialog .el-form-item{
  margin-bottom: 5px;
}
.isMust{
  position: absolute;
    color: #F56C6C;
    top: 2px;
    left: -100px;
}

.el-form-item label{
  text-align: justify;
}
#selectItem{
  display: 'none';
  border: 1px solid #eee;
  width: 200px;
  /* height:100px; */
  position: absolute;
  /* background-color: royalblue; */
}
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
#selectItem ul{
  list-style: none;
  /* height: 100px; */
  width: 200px;
  /* background: red; */
  /* border: 1px solid #eee; */
  /* visibility: hidden; */
}
#selectItem ul li{
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

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */ {
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
	border: 3px solid rgba(255,255,255,.4); 
	background-color: rgba(0, 0, 0, .5);
}
</style>
