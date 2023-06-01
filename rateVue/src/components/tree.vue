<template>
  <div>
    <div>
      <div
        style="
          text-align: center;
          line-height: 35px;
          font-size: 20px;
          margin-left: 45px;
        "
      >
        指标点分类
        <el-button
          icon="el-icon-plus"
          @click="
            ruleForm = { name: '', score: '' };
            dialogVisibleRoot = true;
          "
          style="float: right; padding: 6px; margin: 4px"
        ></el-button>
      </div>
    </div>
    <el-tooltip
      class="item"
      effect="dark"
      content="拖动节点以交换位置"
      placement="right"
    >
      <el-tree
        :data="data"
        :props="defaultProps"
        @node-click="handleNodeClick"
        @node-drag-start="handleDragStart"
        @node-drop="handleDrop"
        :render-content="renderContent"
        :expand-on-click-node="false"
        draggable
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        :highlight-current="true"
        default-expand-all
      ></el-tree>
    </el-tooltip>
    <!--添加到非一二级目录-->
    <el-dialog :visible.sync="dialogVisible" width="35%">
      <span slot="title" style="float: left; font-size: 25px"
        >请输入需要添加的指标点名称</span
      >
      <p>
        添加位置：<b>{{ data1.label }}</b>
      </p>
      <el-input
        v-model="label"
        placeholder="指标点名称"
        size="normal"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="
            append(data1);
            dialogVisible = false;
          "
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <!--添加到一级目录-->
    <el-dialog :visible.sync="dialogVisibleRoot" width="35%">
      <span slot="title" style="float: left; font-size: 25px"
        >请输入分类名和积分值</span
      >
      <el-form
        :rules="rules"
        :model="ruleForm"
        label-width="60px"
        :label-position="left"
      >
        <el-form-item style="margin-top: 15px" prop="name" label="类型">
          <el-input v-model="ruleForm.name" placeholder="请输入分类名" />
        </el-form-item>
        <el-form-item prop="score" label="分值">
          <el-input-number
            v-model="ruleForm.score"
            :min="0"
            :max="50"
            @input="onScoreInput"
            inline
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleRoot = false">取 消</el-button>
        <el-button type="primary" @click="submitFormAppend()">确 定</el-button>
      </span>
    </el-dialog>
    <!--添加到二级目录-->
    <el-dialog :visible.sync="dialogVisibleType" width="35%">
      <span slot="title" style="float: left; font-size: 25px"
        >请选择需要添加的分类</span
      >
      <p>
        添加位置：<b>{{ data1.label }}</b>
      </p>
      <el-form label-width="auto">
        <el-form-item label="类型">
          <el-select
            v-model="type"
            placeholder="请从下拉菜单中选择"
            style="width: 100%"
          >
            <el-option
              v-for="item in TypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类名">
          <el-input v-model="label"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleType = false">取 消</el-button>
        <el-button
          type="primary"
          @click="
            dialogVisibleType = false;
            append(data1);
          "
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <!--修改非一二级目录的名字-->
    <el-dialog :visible.sync="dialogVisibleUpdate" width="35%">
      <span slot="title" style="float: left; font-size: 20px"
        >请输入需要修改的名字</span
      >
      <el-input
        v-model="labelUpdate"
        placeholder="标签名"
        size="normal"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdate = false">取 消</el-button>
        <el-button
          type="primary"
          @click="
            update(data1);
            dialogVisibleUpdate = false;
          "
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <!--修改一级目录的名字-->
    <el-dialog :visible.sync="dialogVisibleUpdateRoot" width="35%">
      <span slot="title" style="float: left; font-size: 25px"
        >请输入需要修改的分类名和积分值</span
      >
      <el-form :rules="rules" :model="ruleForm" label-width="60px">
        <el-form-item style="margin-top: 15px" prop="name" label="类型">
          <el-input
            @input="$forceUpdate()"
            v-model="ruleForm.name"
            placeholder="请输入要修改的分类名"
          />
        </el-form-item>
        <el-form-item prop="score" label="分值">
          <el-input-number
            @input="$forceUpdate()"
            v-model="ruleForm.score"
            :min="0"
            :max="50"
          />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdateRoot = false">取 消</el-button>
        <el-button type="primary" @click="submitFormUpdate(data1)"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <!--修改二级目录的名字-->
    <el-dialog :visible.sync="dialogVisibleUpdateType" width="35%">
      <span slot="title" style="float: left; font-size: 25px"
        >请输入需要修改的分类名</span
      >
      <el-form label-width="auto">
        <el-form-item label="类型">
          <el-select
            v-model="type"
            placeholder="请从下拉菜单中选择"
            disabled
            style="width: 100%"
          >
            <el-option
              v-for="item in TypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类名">
          <el-input v-model="labelUpdate"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdateType = false">取 消</el-button>
        <el-button
          type="primary"
          @click="
            update(data1);
            dialogVisibleUpdateType = false;
          "
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "tree",
  created() {
    var that = this;
    axios.get("/indicator").then(function (resp) {
      //此处可以让父组件向子组件传递url,提高复用性
      that.id = resp.obj[0];
      that.data = resp.obj[1];
    });
  },

  data() {
    return {
      label: "",
      scoreValid: true, // 是否输入合法
      scoreError: "", // 错误提示信息
      scores: [
        { value: 2, label: 2 },
        { value: 4, label: 4 },
        { value: 6, label: 6 },
        { value: 9, label: 9 },
        { value: 12, label: 12 },
      ],
      score: "",
      type: "",
      labelUpdate: "",
      scoreUpdate: -1,
      data1: {}, //点击添加后，通过renderContent将标签名存放到data1里面，在输入框中点击确定，将data1传递给append
      dialogVisible: false, //添加到其他目录
      dialogVisibleRoot: false, //添加到一级目录
      dialogVisibleType: false, //添加到二级目录
      dialogVisibleUpdate: false,
      dialogVisibleUpdateRoot: false,
      dialogVisibleUpdateType: false,
      data: [],
      ruleForm: {
        name: "",
        score: "",
      },
      startNode: null, //
      defaultProps: {
        children: "children",
        label: "label",
      },
      id: 0,
      TypeOptions: [
        {
          value: "学术论文",
          label: "学术论文",
          disabled: false,
        },
        {
          value: "纵向科研项目",
          label: "纵向科研项目",
          disabled: false,
        },
        {
          value: "科技奖",
          label: "科技奖",
          disabled: false,
        },
        {
          value: "决策咨询成果",
          label: "决策咨询成果",
          disabled: false,
        },
        {
          value: "教材",
          label: "教材",
          disabled: false,
        },
        {
          value: "竞赛获奖",
          label: "竞赛获奖",
          disabled: false,
        },
        {
          value: "软著专利",
          label: "软著专利",
          disabled: false,
        },
        {
          value: "其他",
          label: "其他",
          disabled: false,
        },
      ],
      rules: {
        name: [{ required: true, message: "请输入分类名称", trigger: "blur" }],
        score: [{ required: true, message: "请输入分值", trigger: "blur" }],
      },
    };
  },

  methods: {
    onScoreInput(value) {
      const pattern = /^[0-9]*$/;
      if (!pattern.test(value)) {
        // 非数字字符
        this.ruleForm.score = "";
        this.scoreValid = false;
        this.scoreError = "请输入数字字符";
      } else if (value > 50) {
        // 超出范围
        this.ruleForm.score = 50;
        this.scoreValid = false;
        this.scoreError = "积分值不能大于50";
      } else if (value < 0) {
        // 超出范围
        this.ruleForm.score = 0;
        this.scoreValid = false;
        this.scoreError = "积分值不能小于0";
      } else {
        // 合法输入
        this.scoreValid = true;
        this.scoreError = "";
      }
    },
    //点击标签传数据，发送请求
    renderContent(h, { node, data, store }) {
      return (
        <span class="custom-tree-node">
          <span class="tree-node-text" title={node.label}>
            {node.label}
          </span>
          <span class="inline-tree-node">
            <el-button
              class="plus-button"
              circle
              // 添加
              size="mini"
              // type="text"
              icon="el-icon-circle-plus-outline"
              on-click={() => {
                this.data1 = data;
                if (node.level > 1) this.dialogVisible = true;
                else this.dialogVisibleType = true;
              }}
            ></el-button>
            <el-popconfirm
              title="此操作将删除此标签及其所有子标签"
              icon-color="red"
              icon="el-icon-info"
              on-confirm={() => this.remove(node, data)}
            >
              <el-button
                class="plus-button"
                circle //删除
                slot="reference"
                size="mini"
                // type="text"
                icon="el-icon-delete"
              ></el-button>
            </el-popconfirm>
            <el-button
              class="plus-button"
              circle //修改
              size="mini"
              // type="text"
              icon="el-icon-edit"
              on-click={() => {
                this.data1 = data;
                this.labelUpdate = data.label.substring(data.order.length + 1);
                if (node.level > 2) this.dialogVisibleUpdate = true;
                else if (node.level === 1) {
                  this.dialogVisibleUpdateRoot = true;
                  this.scoreUpdate = data.score;
                  this.ruleForm.name = this.labelUpdate;
                  this.ruleForm.score = this.scoreUpdate;
                } else if (node.level === 2) {
                  this.type = data.type;
                  this.dialogVisibleUpdateType = true;
                }
              }}
            ></el-button>
          </span>
        </span>
      );
    },
    submitFormAppend() {
      if (this.ruleForm.name === "" || this.ruleForm.score === "")
        alert("请填入所有必填项");
      else {
        this.label = this.ruleForm.name;
        this.score = this.ruleForm.score;
        this.append();
        this.dialogVisibleRoot = false;
        this.ruleForm = {};
      }
    },
    //包含了新增根节点和普通节点的操作
    append(data) {
      if (this.label === "") {
        alert("名称不能为空");
        return;
      }
      var newChild, postData;
      var that = this;
      if (!this.dialogVisibleRoot) {
        //添加的不是根节点
        var t;
        if (data.type == null) {
          //type为指标点类型，如果标签添加在第二层，则将标签名设置为label
          t = this.type;
        } else t = data.type;
        newChild = {
          id: this.id,
          label: this.label,
          type: t,
          order: data.order + "." + (data.children.length + 1),
          score: data.score,
          children: [],
        }; //order需要修改
        postData = {
          id: this.id,
          name: this.label,
          type: t,
          father: data.id,
          order: newChild.order,
          score: data.score,
        };
        console.log(postData);
        if (!data.children) {
          this.$set(data, "children", []);
        }
        data.children.push(newChild);
      } //添加的是根节点
      else {
        newChild = {
          id: this.id,
          label: this.label,
          type: null,
          order: this.data.length + 1,
          score: this.score,
          children: [],
        };
        postData = {
          id: this.id,
          name: this.label,
          type: null,
          father: null,
          order: newChild.order,
          score: this.score,
        };
        this.data.push(newChild);
      }
      axios.post("/indicator", postData).then(function (resp) {
        if (resp.status != 200) alert("添加失败！");
        else {
          that.$message({
            type: "success",
            message: "添加成功",
          });
        }
      });
      this.id++;
      this.label = "";
      this.score = "";
    },
    remove(node, data) {
      var that = this;
      axios.delete("/indicator/" + node.data.id).then(function (resp) {
        if (resp.status != 200) alert("删除失败！");
        else {
          that.$message({
            type: "success",
            message: "删除成功",
          });
          axios.get("/indicator").then(function (resp) {
            //此处可以让父组件向子组件传递url,提高复用性
            that.id = resp.obj[0];
            that.data = resp.obj[1];
          });
        }
      });
    },
    update(data) {
      data.label = this.labelUpdate;
      var postData = {
        id: data.id,
        name: this.labelUpdate,
        score: this.scoreUpdate,
        order: data.order,
      };
      var that = this;
      axios.put("/indicator", postData).then(function (resp) {
        if (resp.status != 200) alert("修改失败！");
        else {
          that.$message({
            type: "success",
            message: "修改成功",
          });
          axios.get("/indicator").then(function (resp) {
            //此处可以让父组件向子组件传递url,提高复用性
            that.id = resp.obj[0];
            that.data = resp.obj[1];
          });
        }
      });
      this.scoreUpdate = -1;
    },
    submitFormUpdate(data) {
      if (this.ruleForm.name === "" || this.ruleForm.score === "")
        alert("请填入所有必填项");
      else {
        this.labelUpdate = this.ruleForm.name;
        this.scoreUpdate = this.ruleForm.score;
        this.update(this.data1);
        this.dialogVisibleUpdateRoot = false;
      }
    },
    insert(draggingNode, dropNode, dropType, newScore) {
      //newScore用于非一级节点插入到一级目录
      var postData = {
        id: this.startNode.data.id,
        name: this.startNode.data.label,
        type: dropNode.data.type,
        father: dropNode.parent.data.id,
        order: null,
        score: dropNode.data.score,
      };
      var dropNodeData = dropNode.data;
      //需要处理同级节点从前往后拖的特殊情况
      if (dropType === "before") {
        //在同层级内替换时要注意：从前往后拖动，order为drop节点的order-1；从后往前拖动，为drop节点
        //处理type
        if (dropNode.level === 2) postData.type = this.startNode.data.label;
        //处理score
        if (dropNode.level === 1) {
          if (this.startNode.level !== 1) postData.score = newScore;
          else postData.score = this.startNode.data.score;
        }
        //处理order
        if (dropNode.level === 1)
          //处理都在一级目录的情况
          postData.order = dropNodeData.order * 1 - 1;
        else {
          var startOrder = this.startNode.data.order;
          var dropOrder = dropNode.data.order;
          var dotIndex = dropOrder.lastIndexOf(".");
          var dropOrderLast =
            dropOrder.substring(dotIndex + 1, dropOrder.size) * 1;
          if (
            startOrder.substring(0, dotIndex) ===
              dropOrder.substring(0, dotIndex) &&
            startOrder.substring(dotIndex + 1, dropOrder.size) * 1 <
              dropOrderLast
          )
            //父节点一样则比较先后顺序
            postData.order =
              dropOrder.substring(0, dotIndex + 1) + (dropOrderLast - 1);
          else postData.order = dropNodeData.order;
        }
      } else if (dropType === "after") {
        //在同层级内替换时要注意：从前往后拖动，order为drop节点的order；从后往前拖动，为drop节点+1
        //处理type
        if (dropNode.level === 2) postData.type = this.startNode.data.label;
        //处理score
        if (dropNode.level === 1) {
          if (this.startNode.level !== 1) postData.score = newScore;
          else postData.score = this.startNode.data.score;
        }
        //处理order
        if (dropNode.level === 1)
          //处理都在一级目录的情况
          postData.order = dropNodeData.order * 1 + 1;
        else {
          var startOrder = this.startNode.data.order;
          var dropOrder = dropNode.data.order;
          var dotIndex = dropOrder.lastIndexOf(".");
          var dropOrderLast =
            dropOrder.substring(dotIndex + 1, dropOrder.size) * 1;
          if (
            startOrder.substring(0, dotIndex) ===
              dropOrder.substring(0, dotIndex) &&
            startOrder.substring(dotIndex + 1, dropOrder.size) * 1 <
              dropOrderLast
          )
            //父节点一样则比较先后顺序
            postData.order = dropNodeData.order;
          else
            postData.order =
              dropOrder.substring(0, dotIndex + 1) + (dropOrderLast + 1);
        }
      } else if (dropType === "inner") {
        if (dropNode.level === 1) postData.type = this.startNode.data.label;
        postData.father = dropNodeData.id;
        postData.order =
          dropNodeData.order + "." + dropNodeData.children.length;
      }
      var that = this;
      axios.post("/indicator/insert", postData).then(function (resp) {
        axios.get("/indicator").then(function (resp) {
          that.id = resp.obj[0];
          that.data = resp.obj[1];
        });
        if (resp.status != 200)
          that.$message({
            type: "error",
            message: "移动失败!",
          });
        else
          that.$message({
            type: "success",
            message: "移动成功!",
          });
      });
    },
    updateAll() {
      var that = this;
      axios.get("/indicator").then(function (resp) {
        //此处可以让父组件向子组件传递url,提高复用性
        that.id = resp.obj[0];
        that.data = resp.obj[1];
      });
    },

    handleDragStart(node, ev) {
      this.startNode = node;
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      var location;
      var that = this;
      if (dropType === "before") location = "前面";
      else if (dropType === "after") location = "后面";
      else if (dropType === "inner") location = "内部";
      if (
        (draggingNode.level === 3 &&
          dropNode.level === 2 &&
          dropType === "inner" &&
          draggingNode.data.type === dropNode.data.type) ||
        (draggingNode.level === 2 &&
          dropNode.level === 1 &&
          dropType === "inner") ||
        (draggingNode.level === 3 &&
          dropNode.level === 3 &&
          dropType !== "inner" &&
          draggingNode.data.type === dropNode.data.type) ||
        (draggingNode.level === 2 &&
          dropNode.level === 2 &&
          dropType !== "inner")
      ) {
        //只允许同类节点拖拽
        this.$confirm(
          '此标签及其子标签将被移动至："\n' +
            dropNode.data.label +
            '"' +
            location,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(() => {
            that.insert(draggingNode, dropNode, dropType, 0);
          })
          .catch(() => {
            this.updateAll();
            this.$message({
              type: "info",
              message: "已取消移动",
            });
          });
      } else {
        this.$message({
          type: "error",
          message: "只允许在同类节点间移动",
        });
        this.updateAll();
      }
    },
    handleNodeClick(data, node) {
      if (data.children.length == 0) {
        // data.append(node.parent.data.label)
        data["p2"] = node.parent.data.label;
        data["p1"] = node.parent.parent.data.label;
        this.$emit("getLabelInfo", data);
      }
    },

    allowDrop(draggingNode, dropNode, type) {
      return true;
    },
    allowDrag(draggingNode) {
      return true;
    },
  },
};
</script>

<style>
.homeRouterView {
  margin-top: 10px;
}

.homeWelcome {
  text-align: center;
  font-size: 30px;
  font-family: 华文行楷;
  color: #409eff;
  padding-top: 200px;
  /* padding-bottom: 600px; */
}

.homeHeader {
  /* background-color:#409eff; */
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0px 15px;
  box-sizing: border-box;
  background-image: url(../assets/Navigation_bar.png);
  font-size: 30px;
  font-family: 华文行楷;
  color: #ffffff;
}

.homeHeader .userInfo {
  cursor: pointer;
}

.homeContainer .aside {
  background-color: #ecf5ff;
}

.homeContainer .aside .menu {
  background-color: #ecf5ff;
}

.homeContainer .aside .menu .submenu {
  background-color: #ecf5ff;
}

.el-dropdown-link img {
  width: 48px;
  height: 48px;
  border-radius: 24px;
  margin-left: 8px;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  color: white;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 15px;
  padding-right: 0px;
}
.inline-tree-node {
  position: absolute;
  left: 0px;
  display: none;
}
.custom-tree-node:hover .inline-tree-node {
  display: inline-block;
}

body {
  margin: 0;
  padding: 0;
  height: 100%;
}

/*节点高度*/
.el-tree-node__content {
  height: 30px;
  position: relative;
}

.plus-button {
  background-color: #1e90ff;
  color: white;
  border-radius: 4px;
  padding: 8px 16px;
  font-size: 16px;
  border: none;
  cursor: pointer;
}

.plus-button:hover {
  background-color: #add8e6;
}

.plus-button:active {
  background-color: #f0a020;
}

.tree-node-text {
  display: inline-block;
  max-width: 1000px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
