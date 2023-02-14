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
      >
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column
            prop="name"
            align="center"
            label="名称"
            width="280px"
        >
        </el-table-column>
        <el-table-column
            prop="shuZuType"
            label="内容"
            align="center"
            min-width="10%"
        >
          <template slot-scope="scope">
            <template v-if="!scope.row.byParticipant">
                <span >此项不可修改填写！</span>
            </template>
            <template v-else-if="scope.row.contentType == 'textbox'">
                <div>
                    <el-input v-if="scope.row.byParticipant" style="width:65%"
                    @blur="saveTextbox(scope.row)"
                    @change="judgeTextboxChange()"
                    v-model="infoTextboxContent">
                    </el-input>
                    
                </div>
            </template>
            <template v-else-if="scope.row.contentType == 'textarea'">
                    <el-button @click="editSaveTextarea(scope.row)" type="primary" size="mini"
                        style="width:97px;height:28px" icon="el-icon-edit">
                        编&nbsp;&nbsp;辑
                    </el-button>
            </template>
            <template v-else-if="(scope.row.contentType.indexOf('pdf') >= 0 || scope.row.contentType.indexOf('zip') >= 0
                            || scope.row.contentType.indexOf('jpg') >= 0)"
                            style="height:100%">
                <el-upload
                    :file-list="files"
                    action="#" 
                    ref="upload"
                    :limit="1"
                    :headers="headers"
                    :on-remove="()=>{handleDelete(scope.row)}"
                    :auto-upload="false"
                    :on-exceed="handleExceed"
                    :on-change="(file,fileList)=>{handleChangeFiles(file,fileList,scope.row)}"
                >
                    <el-button type="primary" icon="el-icon-upload2"
                        slot="trigger"  size="mini"
                    >选择文件</el-button>
                </el-upload>
            </template>
          </template>
        </el-table-column>
        <el-table-column
            prop="sizelimit"
            label="历史信息"
            align="center"
            min-width="5%"
        >
          <template slot-scope="scope">
            <span v-if="(scope.row.contentType.indexOf('pdf')>=0 || scope.row.contentType.indexOf('zip')>=0
                        || scope.row.contentType.indexOf('jpg')>=0)">
                {{urlFile|fileNameFilter}}
            </span>
            
            <span v-else
                style="width: 100%; height: 100%; display: inline-block"
            >{{ scope.row.content }}</span
            >
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" min-width="5%" label="操作"> -->
          <!-- <template slot-scope="scope">
            <el-upload
                v-if="(scope.row.contentType.indexOf('pdf')>0 || scope.row.contentType.indexOf('zip')>0
                        || scope.row.contentType.indexOf('jpg')>0)"
                :file-list="files"
                action="#" 
                ref="upload"
                :limit="1"
                :headers="headers"
                :on-remove="handleDelete"
                :auto-upload="false"
                :on-exceed="handleExceed"
                :on-change="(file,fileList)=>{handleChangeFiles(file,fileList,scope.row)}"
              >
                <el-button type="primary" icon="el-icon-upload2"
                    slot="trigger"  size="mini"
                >选择文件</el-button>
              </el-upload> -->
            <!-- <el-button
                v-else
                @click="UpdateOrNew(scope.row)"
                style="padding: 4px;width:97px;height:28px"
                icon="el-icon-collection"
                type="primary"
                plain
            >保&nbsp;&nbsp;存
            </el-button
            > -->
          <!-- </template> -->
        <!-- </el-table-column> -->
      </el-table>
      <div style="margin: 20px 0; display: flex; justify-content: left">
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
    <el-dialog :visible.sync="isShowSaveTextarea">
        <el-input
            v-model="infoTextareaContent"
            type="textarea"
            :rows="8"
            placeholder="请输入内容"
        >
        </el-input>
        <span slot="footer">
          <el-button @click="saveTextarea()" type="primary">
            保存
            </el-button>
            <el-button @click="isShowSaveTextarea = false" type="primary">取消</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from 'element-ui'
import axios from "axios";

export default {
  name: "SalInfos",
  data() {
    return {
      infoTextboxContent:"",//textbox的内容
      isChangeTextbox:false,//判断textbox框的值是否改变
      isShowSaveTextarea:false,        
      currentfocusdata: "",//当前焦点数据
      searchValue: {
        compnayName: null,
      },
      info:{
        id:null,
        activityid:null,
        participantid:null,
        infoitemid:null,
        content:""
      },
      infoTextareaContent:"",//textarea数据的内容
      files:[],//上传的文件
      urlFile:'',//文件路径
      headers: {
		'Content-Type': 'multipart/form-data'
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
        {name:"textbox",famname:"字"},
        {name:"textarea",famname:"字"},
        {name:"pdf",famname:"字节"},
        {name:"jpg",famname:"字节"},
        {name:"zip",famname:"字节"}
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
      return this.$store.state.currentHr; //object信息
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
  filters:{
    fileNameFilter:function(data){//将上传的材料显示出来
      if(data == null || data == ''){
        return '未选择材料'
      }else{
        var arr= data.split('/')
        return  arr.reverse()[0]
      }
    }
  },
  methods: {
    judgeTextboxChange(){//判断textbox内容是否变化
        this.isChangeTextbox = true
    },
    editSaveTextarea(data){//点击编辑按钮
        this.isShowSaveTextarea = true
        this.info = data
    },
    judgeSize(value){//判断输入内容是否超过限制大小，分中文和英文
      var pattern = new RegExp("[A-Za-z]")
      let length = 0;
      for(var i=0;i<value.length;i++){
        if(value.charCodeAt(i)>255){
          length += 2;
        }else if(pattern.test(value[i])){
          length ++;
        }
      }
      return length
    },
    saveTextUrl(edit){
        var url = "/info/basic/savetextarea"
        this.postRequest1(url,edit).then((response)=>{
            if(response.status == 200){
                this.$message.success("保存成功！")
                this.isShowSaveTextarea = false//保存框关闭
                this.isChangeTextbox = false//判断输入框是否变化的变量
                this.reset()
            }
        },(error)=>{})
    },
    saveTextbox(data){
        var edit = {
            infoItemID:data.id,
            content:"",
            activityID:data.activityID,
            studentID:JSON.parse(localStorage.getItem("user")).id
        }
        if(this.isChangeTextbox){
            edit.content = this.infoTextboxContent
        }//内容发生了改变
        else{
            return           
        }
        var length = this.judgeSize(this.infoTextboxContent)
        if(length > data.sizelimit){
            this.$message.warning("大小不超过" + data.sizelimit + "!")
        }else{
            this.saveTextUrl(edit)
        }
        
    },
    saveTextarea(){//textarea和textbox失去焦点保存内容
        var edit = {
            infoItemID:this.info.id,
            content:this.infoTextareaContent,
            activityID:this.info.activityID,
            studentID:JSON.parse(localStorage.getItem("user")).id
        }
        if(this.infoTextareaContent == null){
            this.$message.warning("不能提交空白数据！")
            return
        }
        var length = this.judgeSize(this.infoTextareaContent)
        console.log(length);
        if(length > this.info.sizelimit){
            this.$message.warning("大小不超过" + this.info.sizelimit + "!")
        }else{
            this.saveTextUrl(edit)
        }
    },
    handleDelete(data) {//删除选择的文件
      var file={
        filepath:this.urlFile,
        activityID:data.activityID,
        studentID:JSON.parse(localStorage.getItem("user")).id,
        infoItemID:data.id
      }
      this.postRequest1("/infoItem/basic/deleteFile",file).then(
        (response)=>{
          console.log(response)
          this.reset()
        },()=>{}
      )
    },
    handleExceed(){//超过限制数量
      this.$message.error(`只允许上传1个文件`);
    },

    handleChangeFiles(file,fileList,data){//文件列表数量改变
        console.log(this.files);
        console.log(data);
      this.files=[]
      var attachmentType = data.contentType.split(",")
      var type=file.name.split('.')
      if(file.size > data.sizelimit*1024){
        this.$message.error('上传文件大小不能超过'+ data.sizelimit +'大小!');
        return
      }
      if(attachmentType.indexOf(type[type.length-1].toLowerCase()) === -1) {
        this.$message.error("不支持上传该类型的附件")
        return
      }
      var formData=new FormData();
      this.files.push(file);
      formData.append("file",this.files[0].raw)
      formData.append("activityID",data.activityID)
      formData.append("studentID",JSON.parse(localStorage.getItem("user")).id)
      formData.append("infoItemID",data.id)
      axios.post("/infoItem/basic/upload",formData,{
        headers:{
          'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
      }).then(
        (response)=>{
          this.$message({
            message:'上传成功！'
          })
            //获取文件存储的路径
          this.urlFile=response.data
          this.reset()
        },()=>{}
      )
    },
    initAllRoles() {
      this.getRequest("/system/hr/roles").then((resp) => {
        if (resp) {
          this.allroles = resp;
        }
      });
    },
    initHrs() {//获取该活动下的所有信息项
      this.loading = true;
      this.getRequest(
          "/infoItem/basic/stu/?keywords=26" +
        //   this.keywords +
          "&page=" +
          this.page +
          "&size=" +
          this.size +
          "&studentID=" +
          JSON.parse(localStorage.getItem("user")).id
      ).then((resp) => {
        // console.log("resp",resp);
        if (resp) {
          this.loading = false;
          var hrs = []
          resp.data.forEach(element => {//是否允许显示给学生
            if(element.display){
                hrs.push(element)
            }
          });
          this.hrs = hrs
          this.total = hrs.length
          console.log(resp.data);
        }
      });
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
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initHrs();
    },
    reset(){
      this.initHrs();
    },
    initData() {//获取所有活动 
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
