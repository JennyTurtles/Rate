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
              <el-input v-show="item.content != '' || item.content != null" :disabled="true" style="width: 20%;" v-model="item.content"></el-input>
<!--              <span style="color:gray;margin-left: 9px">此项不可修改填写！</span>-->
            </template>
            <template v-else-if="item.contentType.indexOf('textbox')>=0 && item.byParticipant">
              <div>
                <el-input style="width:80%"
                          :maxlength="item.sizelimit"
                          show-word-limit
                          @blur="saveTextbox(item,'textbox',index)"
                          @change="judgeTextboxChange()"
                          v-model="infoTextboxContent[index]"
                          placeholder="请输入内容"
                >
                </el-input>
              </div>
            </template>
            <template v-else-if="item.contentType.indexOf('textarea') >= 0 && item.byParticipant">
              <el-input
                  :maxlength="item.sizelimit"
                  show-word-limit
                  @blur="saveTextbox(item,'textarea',index)"
                  @change="judgeTextareaChange()"
                  v-model="infoTextareaContent[index]"
                  type="textarea"
                  :rows="8"
                  placeholder="请输入内容"
                  style="width:80%"
              >
              </el-input>
            </template>
            <template v-else-if="(item.contentType.indexOf('pdf') >= 0 || item.contentType.indexOf('zip') >= 0
                            || item.contentType.indexOf('jpg') >= 0) && item.byParticipant"
                      style="height:100%">
              <el-upload
                  class="myupload"
                  :file-list="files"
                  :data="item"
                  action="string"
                  ref="upload"
                  :limit="1"
                  :on-exceed="handleExceed(item)"
                  :headers="headers"
                  :disabled="!uploadListElement[index].isShow"
                  :show-file-list="false"
                  :on-change="(file,fileList)=>{handleChangeFiles(file,fileList,item,index)}"
                  style="width:90%;margin-left:20px;height:50%"
                  :auto-upload="false"
                  :http-request="upload(item,index)"
              >
                <div
                    style="
                    width: 100%; height: 100%; display: inline-block;
                    color:lightgray;margin-left: 12px"
                    v-show="uploadListElement[index].isShow">
                <el-button type="primary" icon="el-icon-upload2"
                           slot="trigger"  size="mini" ref="uploadButton"
                           :disabled="!uploadListElement[index].isShow"
                >选择文件</el-button>
                    <span style="color:gray;font-size:11px;margin-left: 5px">只允许{{item.contentType}}类型文件&nbsp;&nbsp;不能超过{{item.sizelimit}}
                    </span>
                </div>

                <span style="font-size:11px;" v-show="uploadListElement[index].uploadState">
                  正在上传...
                </span>

                <div v-if="fileDownloadList[item.id] && fileDownloadList[item.id].length" class="fileList">
                    <a :id="item.id" @click="downloadFileALink(item)"></a>
                    <span @click="downloadFileALink(item)">
                      {{fileDownloadList[item.id][0].fileUrl | fileNameFilter}}
                    </span>
                    <a class="deleteFileStyle" @click="handleDelete(item,index)"></a>
                </div>
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
      uploadButtonDisabled:false,
      uploadListElement:{},//所有upload组件，用于记录index
      uploadInfoid:0,
      fileDownloadList:{},//保存所有上传的文件，用索引标识
      fileUploadList:[],
      formData:{},
      infoTextboxContent:[],//textbox的内容
      isChangeTextbox:false,//判断textbox框的值是否改变
      isChangeTextarea:false,//判断textarea框的值是否改变
      currentfocusdata: "",//当前焦点数据
      info:{
        id:null,
        activityid:null,
        participantid:null,
        infoitemid:null,
        content:""
      },
      infoTextareaContent:[],//textarea数据的内容
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
        return  arr.reverse()[0]
      }
    }
  },
  methods: {
    handleExceed(data,index){
      if(data.id == this.uploadInfoid && this.fileDownloadList[data.id] && this.fileDownloadList[data.id].length > 1){//超过数量限制
        this.$message.error(`只允许上传1个文件`);
        return;
      }
    },
    renderFileIcon(fileName,data) {//渲染上传文件的图标
      this.$nextTick(() => {
        // let fileElementList = document.getElementsByClassName('el-upload-list__item-name');
        // if (fileElementList && fileElementList.length > 0) {
        //   for (let ele of fileElementList) {
        //     let fileName = ele.innerText;
            //获取文件名后缀
            let fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            let iconElement = document.getElementById(data.id);
            if (['png','jpg','jpeg',".gif",'PNG','JPG','JPEG',"GIF"].indexOf(fileType) != -1) {
              iconElement.className = "imgicon-img" // 图⽚，动图
            }
            // else if (['mp4','3gp','avi',"flv",'MP4','3GP','AVI',"FLV"].indexOf(fileType) != -1) {
            //   iconElement.className = 'imgicon-video' // 视频
            // }
            else if (['doc','docx','DOC','DOCX'].indexOf(fileType) != -1) {
              iconElement.className = 'imgicon-docx' // 文档
            }
            // else if (['xls','xlsx','XLS','XLSX'].indexOf(fileType) != -1) {
            //   iconElement.className = 'imgicon-xlsx' // 表格
            // } else if (['ppt','pptx','PPT','PPTX'].indexOf(fileType) != -1) {
            //   iconElement.className = 'imgicon-pptx' // PPT
            // }
            else if (['zip','ZIP'].indexOf(fileType) != -1) {
              iconElement.className = 'imgicon-zip' // 压缩包
            } else if (['pdf','PDF'].indexOf(fileType) != -1) {
              iconElement.className = 'imgicon-pdf' // PDF
            } else {
              iconElement.className = 'imgicon-default' //默认图标
            }
          // }
        // }
      })
    },
    downloadFileALink(data){
      var fileUrl = this.fileDownloadList[data.id][0].fileUrl
      var fileName = fileUrl.split('/').reverse()[0]
      var url = fileUrl
      axios({
        url: '/paper/basic/downloadByUrl?url='+url,
        method: 'GET',
        responseType: 'blob'
      }).then(response => {
        const url = window.URL.createObjectURL(new Blob([response]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      });
    },//通过点击a标签或者图标下载文件
    downloadFile(e){//通过el-upload的on-preview进行下载
      // 参考：https://blog.csdn.net/qq_44603011/article/details/124941102
      var a = document.createElement("a");
      var event = new MouseEvent("click");
      a.target = "_blank";
      a.download = e.name;
      if (!e.url)
          e.url = URL.createObjectURL(e.raw);
      a.href = e.url;//路径前拼上前缀，完整路径
      a.dispatchEvent(event);
    },
    upload(data,index){
      if(data.id != this.uploadInfoid){//渲染了多个upload，用每一个infoitem的id做标识，判断是哪个upload被点击了
        return ;
      }
      if(this.fileUploadList.length == 0){//没有上传文件
        return ;
      }
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
          this.fileDownloadList[this.uploadInfoid] = []//保存每个upload上传的文件列表，用index做标识，每个index是一个数组
          this.fileDownloadList[this.uploadInfoid].push({
            fileUrl:response.data,
            file:this.fileUploadList[0].file.raw
          })
          this.urlFile=response.data//获取文件存储的路径
          this.fileUploadList = []//每上传一次就设置为空，所以每个upload都只有一个文件
          this.renderFileIcon(response.data,data)//渲染图标
          this.uploadListElement[index].isShow = false
          this.uploadListElement[index].uploadState = false
          // this.reset()
        },()=>{}
      )
    },
    judgeTextboxChange(){//判断textbox内容是否变化
      this.isChangeTextbox = true
    },
    judgeTextareaChange(){//判断textarea内容是否变化
      this.isChangeTextarea = true
    },
    // judgeSize(value){//判断输入内容是否超过限制大小，分中文和英文
    //   var pattern = new RegExp("[A-Za-z]")
    //   let length = 0;
    //   for(var i=0;i<value.length;i++){
    //     if(value.charCodeAt(i)>255){
    //       length += 2;
    //     }else if(pattern.test(value[i])){
    //       length ++;
    //     }
    //   }
    //   return length
    // },
    saveTextUrl(edit,type){//发送请求保存数据
      var url = "/info/basic/savetextarea"
      this.postRequest1(url,edit).then((response)=>{
        if(response.status == 200){
          this.$message.success("保存成功！")
          if(type == "textarea"){
            this.isChangeTextarea = false
          }else{
            this.isChangeTextbox = false//判断输入框是否变化的变量
          }
          // this.reset()
        }
      },(error)=>{})
    },
    saveTextbox(data,type,idx){//textbox失去焦点保存内容
      var edit = {
        infoItemID:data.id,
        content:"",
        activityID:data.activityID,
        studentID:JSON.parse(localStorage.getItem("user")).id
      }
      // var length
      if(type == "textbox"){
        if(this.isChangeTextbox){
          edit.content = this.infoTextboxContent[idx]
        }//内容发生了改变
        else{
          return
        }
        // length = this.judgeSize(this.infoTextboxContent)
      }else if(type == "textarea"){
        if(this.isChangeTextarea){
          edit.content = this.infoTextareaContent[idx]
        }else{
          return
        }
        // length = this.judgeSize(this.infoTextareaContent)
      }
      //保留空格和换行符号
      edit.content = edit.content.replace(/\n/g,"<br>").replace(/\s/g," ");
      // if(length > data.sizelimit){
      //   this.$message.warning("大小不超过" + data.sizelimit + "!")
      // }else{
        this.saveTextUrl(edit,type)
      // }
    },
    handleDelete(data,index) {//删除选择的文件
      if(!this.fileDownloadList[data.id])
        return;
      var file={
        filepath:this.fileDownloadList[data.id][0].fileUrl,
        activityID:data.activityID,
        studentID:JSON.parse(localStorage.getItem("user")).id,
        infoItemID:data.id
      }
      this.postRequest1("/infoItem/basic/deleteFile",file).then(
          (response)=>{
            // this.reset()
            this.urlFile = ""
            this.files = []
            this.fileDownloadList[data.id] = null//删除这个index
            //删除这个在refs中记录的文件，防止影响别的upload
            this.$refs.upload[this.uploadListElement[index].elementIndex].uploadFiles = []
            this.uploadListElement[index].isShow = true
          },()=>{}
      )

    },
    handleChangeFiles(file,fileList,data,index){//文件列表数量改变
      this.fileUploadList=[]
      this.files=[]
      var attachmentType = data.contentType.split(",")
      var type=file.name.split('.')
      var sizelim = data.sizelimit.substr(0,data.sizelimit.length-1)
      if(file.size > sizelim*1024*1024){
        this.$message.error('上传文件大小不能超过'+ data.sizelimit +'大小!');
        return false
      }
      if(attachmentType.indexOf(type[type.length-1].toLowerCase()) === -1) {
        this.$message.error("不支持上传该类型的附件")
        return false
      }

      this.uploadInfoid = data.id
      this.fileUploadList.push({
        index:index,
        name:data.name,
        file:file
      })
      this.files = null
      this.uploadListElement[index].uploadState = true
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
        if (resp) {
          this.loading = false;
          var hrs = []
          var idx = 0;
          this.infoTextareaContent = []
          this.infoTextboxContent = []
          resp.data.forEach((element,index) => {
            if(element.contentType.indexOf('pdf') >= 0 || element.contentType.indexOf('jpg') >= 0 || element.contentType.indexOf('zip')>=0){
                this.uploadListElement[index] = {//在所有infoitem中的索引值
                      element,
                      elementIndex:idx,//用于记录当前的upload对于所有的upload处于第几个位置,上传文件会用到
                      isShow:true,
                      uploadState:false
                }
                idx ++;
            }
            this.infoTextareaContent.push("")
            this.infoTextboxContent.push("")
          });
          hrs = resp.data
          this.hrs = hrs
          this.total = hrs.length
          this.form_hrs.hrs = this.hrs
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
.myupload .deleteFileStyle{
  margin-left: 50px;
  background-image: url("../../assets/images/删除.png") !important;
  width: 15px;
  height: 15px;
  display: inline-block;
  background-size: 100% 100%;
  margin-bottom: -3px;
}
.fileList{
  display: inline-block;
  width: 100%;
  margin-top: 5px;
  height: 100%;
  color: #333333
}
.fileList:hover{
  background-color: lightgray;
  cursor: pointer;
}
.myupload  .imgicon-img {
  display: inline-block;
  width: 20px;
  margin-bottom: -3px;
  height: 20px;
  background-size: 100% 100%;
  margin-right: 10px;
  background-image: url("../../assets/images/fileicon/Jpg.png") !important;
}
.myupload .imgicon-pdf {
  display: inline-block;
  width: 30px;
  margin-bottom: -3px;
  height: 30px;
  background-size: 100% 100%;
  margin-right: 10px;
  background-image: url("../../assets/images/fileicon/PDF.png") !important;
}
.myupload .imgicon-zip {
  display: inline-block;
  width: 20px;
  margin-bottom: -3px;
  height: 20px;
  background-size: 100% 100%;
  margin-right: 10px;
  background-image: url("../../assets/images/fileicon/zip.png") !important;
}
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
