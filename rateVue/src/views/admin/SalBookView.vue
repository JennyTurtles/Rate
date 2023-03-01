<template>
<!--    <el-header class="homeHeader">研究生毕业指标点管理-->
<!--      <el-button @click="$refs.search.searchAllItem()" style="float: right; margin-right: 5px" icon="el-icon-search" >-->
<!--        搜索-->
<!--      </el-button>-->
<!--    </el-header>-->
    <div>
      <el-container class="homeMain">
        <el-aside width="440px">
        <BookViewTree @getLabelInfo="getLabelInfo"></BookViewTree>  <!--引入树形组件-->
        </el-aside>
        <el-main>
        <BookViewSearch ref="search" :category="nodeData.label" :score="nodeData.score" :type="nodeData.type" :p1="nodeData.p1" :p2="nodeData.p2"></BookViewSearch>
        </el-main>
      </el-container>
    </div>
</template>

<script>
import tree from "@/components/tree";
import search from "@/components/search";
export default {
  name:'bookview',
  components:{
    'BookViewTree':tree,
    'BookViewSearch':search,
  },
  data(){
    return {
      nodeData:""
    }
  },
  methods:{
    getLabelInfo(nodeData){
      this.nodeData = nodeData
      this.$refs.search.getData(nodeData.id,nodeData.type)
    }
  }
}
</script>

<style>
.el-container{
  height:100vh
}

.el-tree{
  background-color: #ecf5ff;
}

.el-tree-node.is-current > .el-tree-node__content {
  /*color: #409eff;*/
  background-color: white !important;
}


.el-aside {
  background-color: #ecf5ff;
  color: #333;
  /* /* margin-left: 0.5%; */
  border-radius: 10px;
  margin-right: 0.25%;
  box-shadow: 15px 0 15px -15px rgb(117, 119, 119),
  -15px 0 15px -15px rgb(119, 117, 117);
}
.el-main {
  padding: 0px 0px;
  background-color: #f4f4f5;
  color: #333;
  border-radius: 0px;
  margin-right: 0%;
  box-shadow: 10px 0 10px -10px rgb(119, 117, 117)
}
</style>
