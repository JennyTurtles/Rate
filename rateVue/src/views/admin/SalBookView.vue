<template>
  <div>
    <el-container class="homeMain">
      <transition name="router-slid">
        <el-aside v-show="!visible" :style="{ width: width + 'px' }">
          <BookViewTree @getLabelInfo="getLabelInfo"></BookViewTree>
        </el-aside>
      </transition>
      <div class="xHandle">
        <XHandle
            :showTooltip="false"
            :isCanMove="true"
            @widthChange="widthChange"
            @visibleChange="visibleChange"
        />
      </div>
      <el-main :style="divStyle">
        <BookViewSearch
            ref="search"
            :category="nodeData.label"
            :score="nodeData.score"
            :type="nodeData.type"
            :p1="nodeData.p1"
            :p2="nodeData.p2"
        ></BookViewSearch>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import tree from "@/components/tree";
import search from "@/components/search";
import XHandle from "@/components/Xhandle";

export default {
  name: "bookview",
  components: {
    BookViewTree: tree,
    BookViewSearch: search,
    XHandle,
  },
  data() {
    return {
      nodeData: {},
      width: 500,
      visible: false,
    };
  },
  computed: {
    //右侧界面宽度的动态改变
    divStyle: function () {
      return {
        width: "calc(100% - " + this.width + "px)",
        height: "100%",
      };
    },
  },
  methods: {
    getLabelInfo(nodeData) {
      // console.log(nodeData);
      this.nodeData = nodeData;
      this.$refs.search.getData(nodeData.id, nodeData.type);
    },
    visibleChange() {
      this.visible = !this.visible;
      if (this.visible) {
        this.width = 0;
      } else {
        this.width = 500;
      }
    },
    widthChange(movement) {
      this.visible = false;
      this.width -= movement; //右边收起时，this.width += movement  左边收起时this.width -= movement
      if (this.width < 0) {
        //这个值的作用为：限制左侧界面能允许接受的最小宽度，例：设为50时，左侧的界面不会被缩小到50px以下
        this.width = 0;
      }
      if (this.width > 1200) {
        //设置为data 中width变量的宽度一致
        this.width = 1200;
      }
    },
  },
};
</script>

<style>
.xHandle {
  display: flex;
}

.router-slid-enter-active,
.router-slid-leave-active {
  transition: all 0.1s;
}

.router-slid-enter,
.router-slid-leave-active {
  transform: translate3d(-3rem, 0, 0);
  opacity: 0;
}

.el-container {
  height: 100vh;
}

.el-tree {
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
  box-shadow: 10px 0 10px -10px rgb(119, 117, 117);
}
</style>
