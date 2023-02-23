<template>
  <div>
    <div>
      <span>请选择进行分组的信息项：</span>
      <el-select
          style="margin-right: 20px;width: 120px;"
          v-model="selectedGroupInfo"
      >
        <el-option
            v-for="item in groupInfoNums"
            :key="item.id"
            :label="item.name"
            :value="item.name">
        </el-option>
      </el-select>
      <span>请选择分组个数：</span>
      <el-select
          style="margin-right: 20px;width: 120px;"
          v-model="selectedGroupNums"
      >
        <el-option
            v-for="item in groupNums"
            :key="item"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <el-button @click="creatGroup">
        创建分组
      </el-button>
    </div>
  </div>
</template>

<script>
import {getRequest,postRequest} from "@/utils/api";

export default {
  name: "SalPartipicantGroup",
  data(){
    return{
      keywords:'',
      keyword_name:'',
      selectedGroupNums:1,
      groupNums:[],
      selectedGroupInfo:'',
      groupInfoNums:[]
    }
  },
  mounted(){
    this.keywords = this.$route.query.keywords
    this.keyword_name = this.$route.query.keyword_name
    this.groupNums = Array.from(Array(20).keys(),n=>n+1)
    // const List = len => [...new Array(len).keys()+1]
    // this.groupNums = List(20)
    this.initData()
  },
  methods:{
      initData(){
        var url = '/infoItem/basic/getAll/' + this.keywords
        this.getRequest(url).then((resp)=>{
          if(resp)
            // console.log(resp)
            this.groupInfoNums = resp.obj
        })
    },
    creatGroup(){

    }
  }
}
</script>

<style scoped>

</style>