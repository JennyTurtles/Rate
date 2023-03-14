<template>
  <!--评分项设置-->
  <div>
    <div style="display: flex; justify-content: left">
      <div style="width: 100%;text-align: center">{{ keywords_name }}活动 总分计算方式设置</div>
      <div style="margin-left: auto">
        <el-button icon="el-icon-back" type="primary" @click="back">
          返回
        </el-button>
      </div>
    </div>
    <div><br/>点击下拉框选择加入总分计算的信息项或评分项</div>

    <div><br/>总分计算方式：信息项分数+评分项分数</div>
    <div style="margin-top: 10px">
      <el-table
          ref="multipleTable"
          :data="hrs"
          stripe
          border
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.08)"
          style="width: 100%"
      >
        <el-table-column
            prop="infoSumIDs"
            label="信息项设置"
            align="center"
            min-width="10%"
        >
          <template slot-scope="scope">
            <el-select v-model="scope.row.infos" placeholder="请选择信息项"
                       multiple
                       width="200px"
                       v-focus
                       @focus="beforehandleEdit(scope.$index,scope.row,'name')"
                       @change="handleEdit(scope.$index,scope.row,'name')"
                       @blur="inputBlur"
                       prefix-icon="el-icon-edit"

            >
              <el-option
                  v-for="x in infoitem"
                  :key="x.name"
                  :label="x.name"
                  :value="x.name">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
            prop="scoreSumIDs"
            label="评分项设置"
            align="center"
            min-width="10%"
        >
          <template slot-scope="scope">
            <el-select v-model="scope.row.scores" placeholder="请选择评分项"
                       multiple
                       width="200px"
                       v-focus
                       @focus="beforehandleEdit(scope.$index,scope.row,'name')"
                       @change="handleEdit(scope.$index,scope.row,'name')"
                       @blur="inputBlur"
                       prefix-icon="el-icon-edit"

            >
              <el-option
                  v-for="x in scoreItem"
                  :key="x.name"
                  :label="x.name"
                  :value="x.name">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="5%" label="操作">
          <template slot-scope="scope">
            <el-button
                @click="UpdateOrNew(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-collection"
                type="primary"
                plain
            >保存
            </el-button
            >
            <el-button
                @click="Delete_Info_Item(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0; display: flex; justify-content: left">
        <div style="margin-left: 8px">
          <el-button
              @click="newScoring()"
              type="primary"
              icon="el-icon-plus"
              :disabled="this.total===1"
          >新增
          </el-button
          >
        </div>
        <!--        <div style="margin-left: 8px">-->
        <!--          <el-button @click="save()" type="primary" icon="el-icon-collection"-->
        <!--          >保存-->
        <!--          </el-button-->
        <!--          >-->
        <!--        </div>-->
        <div style="margin-left: auto">
          <el-pagination
              background
              @current-change="currentChange"
              @size-change="sizeChange"
              layout="sizes, prev, pager, next, jumper, ->, total, slot"
              :total="total"
          >
          </el-pagination>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import {Message} from 'element-ui'

export default {
  name: "SalTotal",
  data() {
    return {
      title: "",
      page: 1,
      keywords: "",
      keywords_name: "",
      size: 10,
      total: 0,
      loading: false,
      infoitem:[],
      scoreItem:[],
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  created() {
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.initHrs();
    this.initInfo();
    this.initScoreItem();
    // this.initData();
  },
  methods: {
    initHrs() {
      this.loading = true;
      this.getRequest(
          "/totalItem/basic/?activityID=" +
          this.keywords
      ).then((resp) => {
        console.log("resp",resp);
        if (resp) {
          this.loading = false;
          this.hrs = resp.data;
          this.total = resp.total;
          console.log(this.hrs);
        }
      });
    },
    initInfo(){
      this.loading = true;
      this.getRequest(
          "/infoItem/basic/all?activityID=" +
          this.keywords
      ).then((resp) => {
        //console.log("resp",resp);
        if (resp) {
          this.loading = false;
          this.infoitem = resp.obj;
          //console.log(this.infoitem);
        }
      });
    },
    initScoreItem(){
      this.loading = true;
      this.getRequest(
          "/scoreItem/basic/getall?activityID=" +
          this.keywords
      ).then((resp) => {
        // console.log("resp",resp);
        if (resp) {
          this.loading = false;
          this.scoreItem = resp.obj;
          //console.log(this.scoreItem);
        }
      });
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initHrs();
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
  },
};
</script>

<style>
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
