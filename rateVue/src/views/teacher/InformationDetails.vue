<template>
  <div class="Information_box">
    <div class="Information_title">
      {{IDNumber}}的详情信息
    </div>
    <div style="padding-top: 16px;
                margin-top:15px;
                backgroundColor:white;
                padding-bottom:20px;">
      <el-form
          label-position="left"
          label-width="100px"
          ref="dialogdataForm"
          style="margin-left: 20px"
      >

          <el-form-item
              v-for="(val, idx) in datalist"
              :key="idx"
              :label="val.name + ' :'"
          >
            <template v-if="val.contentType!='' && val.contentType!=',' && val.contentType!=null">
              <span
                  v-if="((val.contentType.indexOf('pdf') >= 0 || val.contentType.indexOf('zip') >= 0
                            || val.contentType.indexOf('jpg') >= 0))"
              >
                <a  style="color:gray;font-size:11px;text-decoration:none;cursor:pointer" @click="download(val)"
                    onmouseover="this.style.color = 'blue'"
                    onmouseleave="this.style.color = 'gray'">
                    {{val.content | fileNameFilter}}</a>
            </span>
              <span v-else>{{ val.content }}</span>
            </template>
            <template v-else>
              <span>{{ val.content }}</span>
            </template>
          </el-form-item>
      </el-form>
    </div>
<!--    <div class="Information_title">-->
<!--      {{dialogdata.name}}的详情信息-->
<!--    </div>-->
<!--    <div style="padding-top: 16px;-->
<!--                margin-top:15px;-->
<!--                backgroundColor:white;-->
<!--                padding-bottom:20px;">-->
<!--      <el-form-->
<!--          label-position="left"-->
<!--          label-width="100px"-->
<!--          :model="dialogdata"-->
<!--          ref="dialogdataForm"-->
<!--          style="margin-left: 20px"-->
<!--      >-->
<!--        <template slot-scope="scope">-->
<!--          <el-form-item label="姓 名 :">-->
<!--            <span>{{ dialogdata.name }}</span>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="编 号 :">-->
<!--            <span>{{ dialogdata.idCode }}</span>-->
<!--          </el-form-item>-->
<!--          <el-form-item-->
<!--              v-for="(val, idx) in datalist.infoItems"-->
<!--              :key="idx"-->
<!--              :label="val.name + ' :'"-->
<!--              v-if="dialogdata['info' + val.name] && val.name == '报考专业'"-->
<!--          >-->
<!--            <span>{{ dialogdata["info" + val.name].content }}</span>-->
<!--          </el-form-item>-->
<!--          <el-form-item-->
<!--              v-for="(val, idx) in datalist.infoItems"-->
<!--              :key="idx"-->
<!--              :label="val.name + ' :'"-->
<!--              v-if="dialogdata['info' + val.name] && val.name == '毕业单位'"-->
<!--          >-->
<!--            <span>{{ dialogdata["info" + val.name].content }}</span>-->
<!--          </el-form-item>-->
<!--          <el-form-item-->
<!--              v-for="(val, idx) in datalist.infoItems"-->
<!--              :key="idx"-->
<!--              :label="val.name + ' :'"-->
<!--              width="130px"-->
<!--              v-if="-->
<!--                  val.name != '报考专业' &&-->
<!--                  val.name != '毕业单位'"-->
<!--          >-->
<!--              <span v-if="((val.contentType.indexOf('pdf') >= 0 || val.contentType.indexOf('zip') >= 0-->
<!--                            || val.contentType.indexOf('jpg') >= 0)) && dialogdata['info' + val.name]">-->
<!--                <a  style="color:gray;font-size:11px;text-decoration:none;cursor:pointer" @click="download(val)"-->
<!--                   onmouseover="this.style.color = 'blue'"-->
<!--                   onmouseleave="this.style.color = 'gray'">-->
<!--                    {{dialogdata["info" + val.name].content | fileNameFilter}}</a>-->
<!--              </span>-->
<!--            <span v-else-if="(val.contentType.indexOf('textarea') >= 0 || val.contentType.indexOf('textbox') >= 0) && dialogdata['info' + val.name]">-->
<!--                {{ dialogdata["info" + val.name].content }}-->
<!--              </span>-->
<!--          </el-form-item>-->
<!--        </template>-->
<!--      </el-form>-->
<!--    </div>-->
  </div>

</template>

<script>
import axios from "axios";

export default {
  name: "InformationDetails",
  data(){
    return {
      IDNumber:'',
      activityID:null,
      datalist:{},
      dialogdata:{}
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
  created() {
  },
  mounted() {

    this.urlAddress()
    // this.datalist = JSON.parse(this.$route.query.datalist).datalist
    // this.dialogdata = JSON.parse(this.$route.query.dialogdata)
    // console.log(this.datalist)
    // console.log(this.dialogdata)
  },
  methods:{
    urlAddress(){
      var con = 'http://localhost:8080/#/teacher/tperact/InformationDetails?activityID=26&IDNumber=310111111111111117'
      // var con = 'http://localhost:8080/#/teacher/tperact/InformationDetails/26/310111111111111117'
      var str=location.href; //取得整个地址栏
      var num=str.indexOf("?")
      str=str.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]

      var arr=str.split("&"); //各个参数放到数组里
      if(arr[0].indexOf("activity") == 0){
        num=arr[0].indexOf("=");
        this.activityID = arr[0].substr(num+1);
      }
      if(arr[1].indexOf("IDNumber") == 0){
        num=arr[1].indexOf("=");
        this.IDNumber = arr[1].substr(num+1);
      }
      this.getInfosDetails(this.IDNumber,this.activityID)
    },
    getInfosDetails(IDNumber,activityID){
      var url = '/participants/basic/getParticipantIDByIdNumber?activityID=' + activityID +'&IDNumber=' + IDNumber.toString()
      this.getRequest(url).then((res)=>{
        if(res){
          this.datalist = res
          console.log(this.datalist)
        }
      })
    },
    download(data){//下载证明材料
      var fileName = data.content.split('/').reverse()[0]
      console.log(data)
      var url = data.content
      axios({
        url: '/paper/basic/downloadByUrl?url='+url,
        method: 'GET',
        responseType: 'blob'
      }).then(response => {
        //根据传来的参数，创建一个指向该参数对象的url
        //生命周期存在于被创建的文档里，指向file文件或blob
        const url = window.URL.createObjectURL(new Blob([response]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);//释放url对象
      });
    },
  }
}
</script>

<style scoped>
.Information_title{
  width: 100%;
  font-size: 30px;
  text-align: center;
}
.Information_box{
  border: .5px solid #888888;
  overflow: scroll;
  width: 90%;
  height: 100%;
  margin: auto;
  margin-top: 20px;
  border-radius: 5px;
}
</style>