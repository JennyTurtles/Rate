<template>
  <div class="Information_box">
    <div class="Information_title">
      {{name}}的详情信息
    </div>
    <div style="padding-top: 16px;
                margin-top:15px;
                backgroundColor:white;
                padding-bottom:20px;">
      <el-form
          :label-position="labelPosition"
          label-width="auto"
          ref="dialogdataForm"
          style="margin-left: 20px"
      >
          <el-form-item
              v-for="(val, idx) in datalist"
              :key="idx"
              :label="val.name + ':'"
              :label-width="labelWidth"
          >
            <template v-if="val.contentType!='' && val.contentType!=',' && val.contentType!=null" style="width: 100%">
              <span
                  v-if="((val.contentType.indexOf('pdf') >= 0 || val.contentType.indexOf('zip') >= 0
                            || val.contentType.indexOf('jpg') >= 0))"
              >
                <a  style="color:gray;font-size:11px;text-decoration:none;cursor:pointer" @click="download(val)"
                    onmouseover="this.style.color = 'blue'"
                    onmouseleave="this.style.color = 'gray'">
                    {{val.content | fileNameFilter}}</a>
            </span>
              <span v-else v-html="val.content" style="line-height: 14px;font-size: 14px;width: 100%"></span>
            </template>
            <template v-else>
              <span v-html="val.content"></span>
            </template>
          </el-form-item>
      </el-form>
    </div>
  </div>

</template>

<script>
import axios from "axios";

export default {
  name: "InformationDetails",
  data(){
    return {
      labelWidth:'',
      labelPosition:"left",
      participantID:'',
      activityID:null,
      datalist:{},
      dialogdata:{},
      name:''
    }
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
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  created() {
  },
  mounted() {
    this.urlAddress()
  },
  methods:{
    urlAddress(){
      var con = 'http://localhost:8080/#/teacher/tperact/InformationDetails?activityID=26&IDNumber=310111111111111117'
      var str=location.href; //取得整个地址栏
      var num=str.indexOf("?")
      str=str.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]
      var arr=str.split("&"); //各个参数放到数组里
      if(arr[0].indexOf("activity") == 0){
        num=arr[0].indexOf("=");
        this.activityID = arr[0].substr(num+1);
      }
      if(arr[1].indexOf("participantID") == 0){
        num=arr[1].indexOf("=");
        this.participantID = arr[1].substr(num+1);
      }
      this.getInfosDetails(this.participantID,this.activityID)
    },
    getInfosDetails(participantID,activityID){
      var url = '/participants/basic/getParticipantIDByIdNumber?activityID=' + activityID +'&participantID=' + participantID
      this.getRequest(url).then((res)=>{
        if(res.code == 200){
          this.datalist = res.extend.infoitems
          this.name = res.extend.name
          document.title = this.name
          var arr = this.datalist.map(item=>{
            return item.name.length
          })
          this.labelWidth = Math.max.apply(null,arr) * 18 + 'px';
        }
      })
    },
    download(data){//下载证明材料
      var fileName = data.content.split('/').reverse()[0]
      console.log(data)
      var url = '/paper/basic/downloadByUrl?infoItemID=' + data.id + '&participantID=' + this.participantID + '&activityID=' + data.activityID
      axios({
        url: url,
        method: 'get',
        responseType: 'blob',
        headers: {
          'token': this.user.token ? this.user.token : ''
        }
      }).then(response => {
        //根据传来的参数，创建一个指向该参数对象的url
        //生命周期存在于被创建的文档里，指向file文件或blob
        const url = window.URL.createObjectURL(new Blob([response],  { type: 'application/pdf' }));
        const link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);//释放url对象
      }).catch((error) => {
        console.error('下载材料失败:', error);
      });
    },
  }
}
</script>

<style scoped>
.el-form-item__label-wrap{ float: left; }
.el-form-item__label-wrap {
  margin-left: 0px !important;
}
*{
  margin: 0;
  padding: 0;
}

.el-form-item{
  margin-bottom: 0px;
}
.textarea_{
  width: 450px;
  /*height: 50px;*/
  line-height: 20px;
  overflow: scroll;
  white-space: pre-wrap;
  max-height: 90px;
  border: 0.5px solid #333333;
  border-radius: 3px;
}
.Information_title{
  width: 100%;
  font-size: 30px;
  text-align: center;
}
.Information_box{
  border: .5px solid #888888;
  overflow: scroll;
  width: 80%;
  height: 100%;
  margin: auto;
  margin-top: 20px;
  border-radius: 5px;
}
/*div::-webkit-scrollbar {*/
/*  !* 隐藏默认的滚动条 *!*/
/*  -webkit-appearance: none;*/
/*}*/
/*div::-webkit-scrollbar:vertical {*/
/*  !* 设置垂直滚动条宽度 *!*/
/*  width: 6px;*/
/*}*/
/*div::-webkit-scrollbar-thumb {*/
/*  !* 滚动条的其他样式定制，注意，这个一定也要定制，否则就是一个透明的滚动条 *!*/
/*  border-radius: 6px;*/
/*  border: 3px solid rgba(255,255,255,.4);*/
/*  background-color: rgba(0, 0, 0, .5);*/
/*}*/

</style>
