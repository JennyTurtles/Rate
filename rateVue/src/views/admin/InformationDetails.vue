<template>
  <div class="Information_box">
    <div class="Information_title">
      {{dialogdata.name}}的详情信息
    </div>
    <div style="padding-top: 16px;
                margin-top:15px;
                backgroundColor:white;
                padding-bottom:20px;">
      <el-form
          label-position="left"
          label-width="100px"
          :model="dialogdata"
          ref="dialogdataForm"
          style="margin-left: 20px"
      >
        <template slot-scope="scope">
          <el-form-item label="姓 名 :">
            <span>{{ dialogdata.name }}</span>
          </el-form-item>
          <el-form-item label="编 号 :">
            <span>{{ dialogdata.idCode }}</span>
          </el-form-item>
          <el-form-item
              v-for="(val, idx) in datalist.infoItems"
              :key="idx"
              :label="val.name + ' :'"
              v-if="dialogdata['info' + val.name] && val.name == '报考专业'"
          >
            <span>{{ dialogdata["info" + val.name].content }}</span>
          </el-form-item>
          <el-form-item
              v-for="(val, idx) in datalist.infoItems"
              :key="idx"
              :label="val.name + ' :'"
              v-if="dialogdata['info' + val.name] && val.name == '毕业单位'"
          >
            <span>{{ dialogdata["info" + val.name].content }}</span>
          </el-form-item>
          <el-form-item
              v-for="(val, idx) in datalist.infoItems"
              :key="idx"
              :label="val.name + ' :'"
              width="130px"
              v-if="
                  val.name != '报考专业' &&
                  val.name != '毕业单位'"
          >
              <span v-if="((val.contentType.indexOf('pdf') >= 0 || val.contentType.indexOf('zip') >= 0
                            || val.contentType.indexOf('jpg') >= 0)) && dialogdata['info' + val.name]">
                <a  style="color:gray;font-size:11px;text-decoration:none;cursor:pointer" @click="download(val)"
                    onmouseover="this.style.color = 'blue'"
                    onmouseleave="this.style.color = 'gray'">
                    {{dialogdata["info" + val.name].content | fileNameFilter}}</a>
              </span>
            <span v-else-if="(val.contentType.indexOf('textarea') >= 0 || val.contentType.indexOf('textbox') >= 0) && dialogdata['info' + val.name]">
                {{ dialogdata["info" + val.name].content }}
              </span>
          </el-form-item>
        </template>
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
  mounted() {
    this.datalist = JSON.parse(this.$route.query.datalist).datalist
    this.dialogdata = JSON.parse(this.$route.query.dialogdata)
  },
  methods:{
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