<template>
  <!--评分项设置-->
  <div>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center">{{ keywords_name }}信息项设置</div>
    </div>
    <div style="padding-top: 16px;
                margin-top:15px;
                backgroundColor:white;
                padding-bottom:20px">
      <el-form :model="form_hrs" ref="form"
               element-loading-text="正在加载..."
               element-loading-spinner="el-icon-loading"
               element-loading-background="rgba(0, 0, 0, 0.08)">
        <el-form-item v-for="(item,index) in form_hrs.hrs" :key="item.id"
                      :label="item.name+':'">
          <template >
            <template v-if="!item.byParticipant">
              <span style="color:gray">此项不可修改填写！</span>
            </template>
            <template v-else-if="item.contentType == 'textbox'">
              <div>
                <el-input style="width:80%"
                          @blur="saveTextbox(item,'textbox')"
                          @change="judgeTextboxChange()"
                          v-model="infoTextboxContent"
                          placeholder="请输入内容"
                >
                </el-input>

              </div>
            </template>
            <template v-else-if="item.contentType == 'textarea'">
              <el-input
                  @blur="saveTextbox(item,'textarea')"
                  @change="judgeTextareaChange()"
                  v-model="infoTextareaContent"
                  type="textarea"
                  :rows="8"
                  placeholder="请输入内容"
                  style="width:80%"
              >
              </el-input>
            </template>
            <template v-else-if="(item.contentType.indexOf('pdf') >= 0 || item.contentType.indexOf('zip') >= 0
                            || item.contentType.indexOf('jpg') >= 0)"
                      style="height:100%">
              <el-upload
                  :file-list="files"
                  :data="item"
                  action="string"
                  ref="upload"
                  :limit="1"
                  :headers="headers"
                  :on-change="(file,fileList)=>{handleChangeFiles(file,fileList,item,index)}"
                  style="width:70%;margin-left:20px;height:50%"
                  :on-remove="()=>{handleDelete(item)}"
                  :auto-upload="false"
                  :on-exceed="handleExceed"
                  :http-request="upload(item)"
              >
                <el-button type="primary" icon="el-icon-upload2"
                           slot="trigger"  size="mini"
                >选择文件</el-button>

                <template
                    style="
                    width: 100%; height: 100%; display: inline-block;
                    margin-left:12px;color:lightgray">
                    <span style="color:gray;font-size:11px;margin-left:12px;">只允许{{item.contentType}}类型文件&nbsp;&nbsp;不能超过{{item.sizelimit}}
                    </span>
                  <el-button type="primary" icon="el-icon-download"
                             slot="trigger"  size="mini" @click="downloadInfosFile(item)"
                  >下载文件</el-button>
                </template>
              </el-upload>
            </template>
          </template>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {Message} from 'element-ui'
import axios from "axios";

export default {
  name: "SalInfos",
  data() {
    return {
      uploadInfoid:0,
      fileDownloadList:{},//保存所有上传的文件，用索引标识
      fileUploadList:[],
      formData:{},
      infoTextboxContent:"",//textbox的内容
      isChangeTextbox:false,//判断textbox框的值是否改变
      isChangeTextarea:false,//判断textarea框的值是否改变
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
      form_hrs:{
        hrs:[]
      },
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
        return ''
      }else{
        var arr= data.split('/')
        return  arr.reverse()[0] + '  x'
      }
    }
  },
  methods: {
    downloadInfosFile(data){//下载上传的该文件
      const fileName = this.fileDownloadList[data.id]
      // axios({
      //   url: 'http://www.xxx.com/download',
      //   method: 'get',
      //   responseType: 'blob',
      // }).then(res => {
      //   fileName = res.headers.content-disposition.split(';')[1].split('filename=')[1];
      //   const filestream = res.data;  // 返回的文件流
      //   // {type: 'application/vnd.ms-excel'}指定对应文件类型为.XLS (.XLS的缩写就为application/vnd.ms-excel)
      //   const blob = new Blob([filestream], {type: 'application/vnd.ms-excel'});
      //   const a = document.createElement('a');
      //   const href = window.URL.createObjectURL(blob); // 创建下载连接
      //   a.href = href;
      //   a.download = decodeURL(fileName );
      //   document.body.appendChild(a);
      //   a.click();
      //   document.body.removeChild(a); // 下载完移除元素
      //   window.URL.revokeObjectURL(href); // 释放掉blob对象
      // })
    },
    upload(data){
      if(data.id != this.uploadInfoid){//渲染了多了upload，用每一个infoitem的id做标识，判断是哪个upload被点击了
        return;
      }
      if(this.fileUploadList.length == 0){
        return;
      }
      console.log(this.fileDownloadList)
      var formData=new FormData();
      formData.append("file",this.fileUploadList[0].file.raw)
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
          this.fileDownloadList['index'] = {
            index:this.uploadInfoid,
            fileUrl:response.data,
            file:this.fileUploadList[0].file.raw
          }
            //获取文件存储的路径
          this.urlFile=response.data
          this.reset()
          this.fileUploadList = []
        },()=>{}
      )
    },
    judgeTextboxChange(){//判断textbox内容是否变化
      this.isChangeTextbox = true
    },
    judgeTextareaChange(){//判断textarea内容是否变化
      this.isChangeTextarea = true
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
    saveTextUrl(edit,type){
      var url = "/info/basic/savetextarea"
      this.postRequest1(url,edit).then((response)=>{
        if(response.status == 200){
          this.$message.success("保存成功！")
          if(type == "textarea"){
            this.isChangeTextarea = false
          }else{
            this.isChangeTextbox = false//判断输入框是否变化的变量
          }
          this.reset()
        }
      },(error)=>{})
    },
    saveTextbox(data,type){//textbox失去焦点保存内容
      var edit = {
        infoItemID:data.id,
        content:"",
        activityID:data.activityID,
        studentID:JSON.parse(localStorage.getItem("user")).id
      }
      var length
      if(type == "textbox"){
        if(this.isChangeTextbox){
          edit.content = this.infoTextboxContent
        }//内容发生了改变
        else{
          // this.$message.warning("不能提交空白数据！")
          return
        }
        length = this.judgeSize(this.infoTextboxContent)
      }else if(type == "textarea"){
        if(this.isChangeTextarea){
          edit.content = this.infoTextareaContent
        }else{
          // this.$message.warning("不能提交空白数据！")
          return
        }
        length = this.judgeSize(this.infoTextareaContent)
      }

      if(length > data.sizelimit){
        this.$message.warning("大小不超过" + data.sizelimit + "!")
      }else{
        this.saveTextUrl(edit,type)
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
            this.urlFile = ""
            this.files = ''
          },()=>{}
      )
    },
    handleExceed(){//超过限制数量
      this.$message.error(`只允许上传1个文件`);
    },
    handleChangeFiles(file,fileList,data,index){//文件列表数量改变
      this.fileUploadList=[]
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
      this.uploadInfoid = data.id
      this.fileUploadList.push({
        index:index,
        name:data.name,
        file:file
      })
      this.files = null
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
          "/infoItem/basic/stu/?keywords=" +
          this.keywords +
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
          // resp.data.forEach(element => {//是否允许显示给学生
          //   if(element.display){
          //       hrs.push(element)
          //   }
          // });
          hrs = resp.data
          this.hrs = hrs
          this.total = hrs.length
          this.form_hrs.hrs = this.hrs
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

.el-form-item__label {
  /* padding-left: 12px; */
  margin-left: 20px ;
}
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
