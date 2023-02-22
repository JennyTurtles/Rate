<template>
  <div class="Information_box">
    <div class="Information_title">
      {{dialogdata.name}}的详情信息
    </div>
    <div style="padding-top: 16px;
                margin-top:15px;
                backgroundColor:white;
                padding-bottom:20px">
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
                            || val.contentType.indexOf('jpg') >= 0)) && dialogdata['info' + val.name]"
                    style="color:gray;font-size:14px;text-decoration:none;cursor:pointer;"
                    onmouseover="this.style.color = 'blue'"
                    onmouseleave="this.style.color = 'gray'"
                    @click="downloadInfoItems(val)">
                {{dialogdata["info" + val.name].content | fileNameFilter}}
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
  overflow: scroll;
  width: 90%;
  height: 100%;
  margin: auto;
  margin-top: 20px;
  box-shadow: 8px 8px 5px #888888;
}
</style>