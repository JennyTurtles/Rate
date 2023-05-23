<template>
  <div class="x-handle" :style="divStyle" @mousedown="mouseDown">
    <el-header
      style="
        height: 21px;
        border-bottom: 1px solid rgb(235, 238, 245);
        padding: 0px;
        text-align: center;
        position: absolute;
        top: 50%;
        transform: translate(-0%, -0%);
      "
    >
      <div v-if="shouldCloseWindow == 'left'">
        <el-tooltip
          v-if="showTooltip"
          class="item"
          effect="dark"
          content="点击展开/隐藏"
          placement="bottom-end"
        >
          <el-button
            type="text"
            style="
              margin-left: -2px;
              float: right;
              font-size: 5px;
              padding: 0;
              color: #030c14;
            "
            v-on:click="visibleChange"
            class="btn-collapse"
          >
            <i class="el-icon-arrow-right" v-if="visible"></i>
            <i class="el-icon-arrow-left" v-else></i>
          </el-button>
        </el-tooltip>
        <el-button
          v-if="!showTooltip"
          type="text"
          style="
            margin-left: -2px;
            float: right;
            font-size: 5px;
            padding: 0;
            color: #030c14;
          "
          v-on:click="visibleChange"
          class="btn-collapse"
        >
          <i class="el-icon-arrow-left" v-if="visible"></i>
          <i class="el-icon-arrow-right" v-else></i>
        </el-button>
      </div>
      <div v-if="shouldCloseWindow == 'right'">
        <el-tooltip
          v-if="showTooltip"
          class="item"
          effect="dark"
          content="点击展开/隐藏"
          placement="bottom-end"
        >
          <el-button
            type="text"
            style="
              margin-left: -2px;
              float: right;
              font-size: 5px;
              padding: 0;
              color: #030c14;
            "
            v-on:click="visibleChange"
            class="btn-collapse"
          >
            <i class="el-icon-arrow-left" v-if="visible"></i>
            <i class="el-icon-arrow-right" v-else></i>
          </el-button>
        </el-tooltip>
        <el-button
          v-if="!showTooltip"
          type="text"
          style="
            margin-left: -2px;
            float: right;
            font-size: 5px;
            padding: 0;
            color: #030c14;
          "
          v-on:click="visibleChange"
          class="btn-collapse"
        >
          <i class="el-icon-arrow-right" v-if="visible"></i>
          <i class="el-icon-arrow-left" v-else></i>
        </el-button>
      </div>
    </el-header>
  </div>
</template>

<script>
export default {
  name: "XHandle",
  props: ["isShowTooltip", "isCanMove", "closeWindow"],
  data() {
    return {
      lastX: "",
      visible: false,
      canMove: false,
      showTooltip: false,
      shouldCloseWindow: "right",
    };
  },

  created() {
    document.addEventListener("mouseup", this.mouseUp);
    if (this.isShowTooltip != undefined && this.isShowTooltip != "undefined")
      this.showTooltip = this.isShowTooltip;
    if (this.isCanMove != undefined && this.isCanMove != "undefined")
      this.canMove = this.isCanMove;
    if (this.closeWindow != undefined && this.closeWindow != "undefined")
      this.shouldCloseWindow = this.closeWindow;
  },

  destroyed() {
    document.removeEventListener("mouseup", this.mouseUp);
  },
  computed: {
    divStyle: function () {
      if (this.canMove)
        return {
          cursor: "w-resize",
        };
    },
  },
  methods: {
    visibleChange() {
      this.visible = !this.visible;
      this.$emit("visibleChange");
    },
    mouseDown(event) {
      document.addEventListener("mousemove", this.mouseMove);
      this.lastX = event.screenX;
    },
    mouseMove(event) {
      if (this.canMove) {
        this.$emit("widthChange", this.lastX - event.screenX);
        this.lastX = event.screenX;
      }
    },
    mouseUp() {
      this.lastX = "";
      document.removeEventListener("mousemove", this.mouseMove);
    },
  },
};
</script>
<style lang="less">
.x-handle {
  width: 7px;
  z-index: 10;
  background: #ccc;
}
</style>
