<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加专著或教材
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px;">
      <el-table
          :data="monographsList"
          stripe
          border
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%;"
      >
        <el-table-column
            fixed
            prop="name"
            align="center"
            label="专著或教材名称"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="state"
            label="状态"
            min-width="10%"
            align="center"
        >
          <template slot-scope="scope">
            <span
                style="padding: 4px"
                :style="(scope.row.state=='tea_reject' || scope.row.state=='adm_reject') ? {'color':'red'}:{'color':'gray'}"
                size="mini"
            >
              {{scope.row.state=="commit"
                ? "已提交"
                :scope.row.state=="tea_pass"
                    ? "导师通过"
                    :scope.row.state=="tea_reject"
                        ? "导师驳回"
                        :scope.row.state=="adm_pass"
                            ? "管理员通过"
                            :"管理员驳回"}}
              </span>
          </template>
        </el-table-column>
        <el-table-column
            prop="publisher"
            label="出版社"
            align="center"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="isbn"
            label="ISBN"
            align="center"
            min-width="10%"
        >
        </el-table-column>
        <el-table-column
            prop="author"
            align="center"
            label="完成人"
            min-width="20%"
        >
        </el-table-column>
        <el-table-column
            prop="point"
            label="积分"
            align="center"
            min-width="8%"
        >
        </el-table-column>
        <el-table-column
            prop="operationList[0].remark"
            min-width="20%"
            align="center"
            label="备注"
        >
        </el-table-column>
        <el-table-column align="center" width="280px" label="操 作" min-width="20%">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row, scope.$index)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
                v-show="scope.row.state == 'commit' || scope.row.state == 'tea_reject' || scope.row.state == 'adm_reject'? true:false"
            >编辑</el-button
            >
            <el-button
                @click="showInfo(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情</el-button
            >
            <el-button
                @click="deleteEmp(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
                v-show="scope.row.state == 'tea_reject' || scope.row.state == 'commit'? true:false"
            >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 添加科研专著或教材对话框 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :model="currentMonographCopy"
          :rules="rules"
          ref="currentMonographCopy"
      >
        <el-form-item label="著作名称:" label-width="80px" style="margin-left: 20px;" prop="name">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentMonographCopy.name"
              placeholder="请输入专著或教材名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="完成年月:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-date-picker
              style="width: 80%"
              v-model="currentMonographCopy.date"
              type="month"
              value-format="yyyy-MM"
              placeholder="选择年月">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="完成人:" label-width="80px" style="margin-left: 20px;" prop="author">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="currentMonographCopy.author"
              @blur="judgeMember()"
              placeholder="请输入完成人,如有多个用分号分隔"
          ></el-input>
        </el-form-item>
        <el-form-item label="出版社:" label-width="80px" style="margin-left: 20px;" prop="publisher">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="currentMonographCopy.publisher"
              placeholder="请输入专著或教材出版社"
          ></el-input>
        </el-form-item>
        <el-form-item label="ISBN:" label-width="80px" style="margin-left: 20px;" prop="isbn">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width: 80%"
              prefix-icon="el-icon-edit"
              v-model="currentMonographCopy.isbn"
              placeholder="请输入专著或教材ISBN"
          ></el-input>
        </el-form-item>
        <el-form-item label="指标点:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-button ref="selectBtn" size="mini" type="text" @click="initTree()">{{ indicatorBtn }}</el-button>
        </el-form-item>

        <el-form-item label="证明材料:" prop="url" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-upload
              :file-list="files"
              action="#"
              :limit="1"
              :headers="headers"
              :on-remove="handleDelete"
              :auto-upload="false"
              :on-change="handleChangeFiles"
              :on-exceed="handleExceed"
          >
            <el-button type="primary" icon="el-icon-upload2"
                       slot="trigger"
            >选择文件</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
            <span style="color:gray;font-size:11px">只允许doc docx pdf jpg png jpe rar zip类型文件
                  &nbsp;&nbsp;大小不能超过10MB
                </span>
          </el-upload>
        </el-form-item>
      </el-form>
      <div style="margin-left: 20px;">
        <span style="color:gray;font-size:10px">将会获得：{{ monographPoint }}积分</span>
        <span style="color:gray;font-size:10px;margin-left: 8px">{{ zeroPointReason }}</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAdd">取 消</el-button>
        <el-button type="primary" @click="addMonograph" v-show="addButtonState">提 交</el-button>
      </span>
    </el-dialog>

    <!--查看详情-->
    <el-dialog
        class="showInfo_dialog"
        :title="title_show"
        :visible.sync="dialogVisible_showInfo"
        width="520px"
        center
    >
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="currentMonograph"
          style="margin-left: 20px">
        <el-form-item label="著作名称:">
          <span>{{ currentMonograph.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者列表:">
          <span>{{ currentMonograph.author }}</span
          >
        </el-form-item>
        <el-form-item label="作者人数:">
          <span>{{ currentMonograph.total }}</span
          >
        </el-form-item>
        <el-form-item label="作者排名:">
          <span>{{ currentMonograph.rank }}</span
          >
        </el-form-item>
        <el-form-item label="完成年份:">
          <span>{{ currentMonograph.date }}</span
          >
        </el-form-item>
        <el-form-item label="出版社:">
          <span>{{ currentMonograph.publisher }}</span
          >
        </el-form-item>
        <el-form-item label="ISBN:">
          <span>{{ currentMonograph.isbn }}</span
          >
        </el-form-item>
        <el-form-item label="成果积分:">
          <span>{{ currentMonograph.point }}</span
          >
        </el-form-item>
        <el-form-item label="证明材料:">
          <span v-if="currentMonograph.url == '' || currentMonograph.url == null ? true:false" >无证明材料</span>
          <div v-else>{{ currentMonograph.url | fileNameFilter }}</div>
        </el-form-item>
        <div v-show="currentMonograph.url == '' || currentMonograph.url == null ? false : true" style="margin-left: 80px">
          <div>
            <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
            <el-button @click="previewMethod('2')">下载</el-button>
          </div>
          <div style="margin-top: 5px">
            <el-image
                v-show="false"
                ref="previewImage"
                style="width: 100px; height: 100px"
                :src="previewUrl"
                :preview-src-list="previewImageSrcList">
            </el-image>
          </div>
        </div>
        <br />
        <div >
          <span>历史操作:</span>
          <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
              <div >
                <p>{{item.time | dataFormat}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operatorName}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operationName}}</p>
                <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{item.remark}}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible_showInfo = false"
        >关 闭</el-button
        >
      </span>
    </el-dialog>

    <el-dialog title="" center :visible.sync="showTreeDialog" width="60%">
      <div slot="title">
        <div>选择指标点分类</div>
        <div style="font-size: 14px;margin-top: 10px">以下仅显示本类型的指标点</div>
      </div>
      <span class="el-tree-node">
        <el-tree
            :data="data"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :expand-on-click-node="false"
            :highlight-current="true"
            node-key="id"
            :default-expanded-keys="defaultExpandedKeys"
        ></el-tree>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
      <template v-if="isPdf">
        <vue-office-pdf
            :src="previewUrl"
            style="height: 100vh;"
        />
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import {postRequest1} from "@/utils/api";
export default {
  name: "SalSearch",
  data() {
    return {
      isImage: false,
      isPdf: false,
      dialogPreviewPdfFile: false,
      previewImageSrcList: [],
      previewUrl: '',
      defaultExpandedKeys: [],
      zeroPointReason: '',
      currentSelectedIndicator: {},
      monoLimitRankN: '',
      isAuthorIncludeSelf: false,
      indicatorBtn: '选择指标点',
      defaultProps: {
        children: "children",
        label: "label",
      },
      data: [],
      showTreeDialog: false,
      monographPoint:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      addButtonState: false,//是否允许添加专著或教材
      operList:[],//每个专著或教材的历史操作记录
      options:[],//存储所有类型对象
      labelPosition: "left",
      title: "",
      title_show: "",
      monographsList: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '专著教材',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      currentMonographCopy: {},
      currentMonograph: {
        id: null,
        name: null,
        author:"",
        state: '',
        date: "",
        rank: "",
        total:"",
        point:"",
        url:'',
        remark:'',
        publisher: '',
        isbn: ''
      },
      rules: {
        name: [{ required: true, message: "请输入专著或教材名称", trigger: "blur" }],
        publisher: [{ required: true, message: "请输入专著或教材出版社", trigger: "blur" }],
        isbn: [{ required: true, message: "请输入专著或教材ISBN", trigger: "blur" }]
      },
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  watch: {
    //监听是否选择了否个指标树点 和作者列表隐形的互相监听是否发生改变
    currentSelectedIndicator: {
      deep: true,
      handler: function (newV, oldV) {
        this.judgeMember();
      }
    },
  },
  created() {},
  mounted() {
    this.currentMonographCopy = JSON.parse(JSON.stringify(this.currentMonograph));
    this.initMonographsList();
  },
  methods: {
    previewMethod(type) {
      if(type == '1') {
        this.previewFileMethod(this.currentMonograph).then(res => {
          this.previewUrl = res;
          if(this.isImage) {
            this.previewImageSrcList = [res];
            this.$refs.previewImage.showViewer = true;
          }
          if(this.isPdf) {
            this.dialogPreviewPdfFile = true;
          }
        });
      } else {
        this.downloadFileMethod(this.currentMonograph);
      }
    },

    handleNodeClick(data, node) {
      if (data.children.length == 0) {
        this.indicatorBtn = data.label;
        this.currentSelectedIndicator = data;
        this.currentMonographCopy.indicatorId = data.id;
        this.monoLimitRankN = data.rankN;
        this.showTreeDialog = false;
      }
    },
    initTree() {
      this.getRequest("/indicator/getAllByType?type=学术专著和教材").then( resp => {
        this.showTreeDialog = true;
        this.defaultExpandedKeys = [];
        if (resp) {
          this.data = resp.obj[1];
          if(this.data.length > 0)
            if(this.data[0].children.length > 0) {
              this.defaultExpandedKeys.push(this.data[0].children[0].id);
            } else this.defaultExpandedKeys.push(this.data[0].id);
        }
      });
    },
    cancelAdd() {
      this.dialogVisible = false;
    },
    handleDelete() {//删除选择的文件
      var file={
        filepath:this.urlFile
      }
      this.postRequest1("/monograph/basic/deleteFile",file).then(
        ()=>{
          this.files = [];
          this.urlFile = '';
        }
      )
    },
    handleExceed(){//超过限制数量
      this.$message.error(`只允许上传1个文件`);
    },
    handleChangeFiles(file,fileList){//文件列表数量改变
      this.files=[]
      var attachmentType = [
        "doc","docx","pdf","jpg","png","jpeg","rar","zip"]
      var type=file.name.split('.')
      if(file.size > 10*1024*1024){
        this.$message.error('上传文件大小不能超过10MB!');
        return
      }
      if(attachmentType.indexOf(type[type.length-1].toLowerCase()) === -1) {
        this.$message.error("不支持上传该类型的附件")
        return
      }
      var formData=new FormData();
      this.files.push(file);
      formData.append("file",this.files[0].raw)
      axios.post("/achievements/basic/upload",formData,{
        headers:{
          'token': localStorage.getItem('user') ? this.user.token : ''
        }
      }).then(
          (response)=>{
            this.$message({
              message:'上传成功！'
            })
            this.addButtonState = true
            //获取文件路径
            this.urlFile = response.data
          },()=>{}
      )
    },
    judgeMember(){//输入作者框 失去焦点触发事件
      var author = this.currentMonographCopy.author;
      if(!author || author === '') {
        this.isAuthorIncludeSelf = false;
        return;
      }
      var isalph = false//判断输入中是否有英文字母
      for(var i in author){
        var asc = author.charCodeAt(i)
        if(asc >= 65 && asc <= 90 || asc >= 97 && asc <= 122){
          isalph=true
          break
        }
      }
      var num = null
      var info = this.user;
      num = author.split(/[;；]/)
      num = num.map(item => {
        return item && item.replace(/\s*/g,"");
      }).filter(v => {
        return v
      })
      //判断自己在不在其中
      if(num.indexOf(info.name) == -1){//不在 并且没有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        this.isAuthorIncludeSelf = false;
        this.zeroPointReason = '参与人未包含自己'
        this.monographPoint = 0;
      } else {
        //作者列表的rank大于规定的rankN，积分为0
        this.judgeRankScore(num.indexOf(info.name) + 1)
        this.isAuthorIncludeSelf = true;
      }
      this.currentMonographCopy.total = num.length
      this.currentMonographCopy.rank = num.indexOf(info.name) + 1
    },
    //判断分数
    judgeRankScore(rank) {
      if(JSON.parse(JSON.stringify(this.currentSelectedIndicator)) === '{}') {
        this.monographPoint = 0; //输入作者，但未选择指标点
        this.zeroPointReason = '请选择指标点'
      }
      else { //指标点已选择，再次修改作者列表
        const indicatorRankN = this.currentSelectedIndicator.rankN;
        if(rank > indicatorRankN && indicatorRankN > 0) {
          this.monographPoint = 0;
          this.zeroPointReason = `著作人排名需在前${this.currentSelectedIndicator.rankN}名以内`
        } else {
          this.monographPoint = this.currentSelectedIndicator.score;
          this.zeroPointReason = ''
        }
      }
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    //编辑按钮
    showEditEmpView(data, idx) {
      this.title = "编辑专著或教材信息";
      this.currentMonographCopy = JSON.parse(JSON.stringify(data));
      this.dialogVisible = true;
      this.options = [];
      this.monoLimitRankN = data.indicator.rankN;
      this.monographPoint = data.point;
      this.zeroPointReason = '';
      this.isAuthorIncludeSelf = true;
      this.addButtonState = true;
      this.indicatorBtn = data.indicator.name;
      this.files = [
        {
          name: this.currentMonographCopy.url.split('/').reverse()[0],
          url: this.currentMonographCopy.url
        }
      ]
      this.urlFile = this.currentMonographCopy.url;
    },
    showInfo(data){
      this.loading = true;
      this.title_show = "显示详情";
      this.currentMonograph = data
      this.dialogVisible_showInfo = true
      this.isPdf = this.isImage = false; //初始化
      this.previewUrl = '';
      this.previewImageSrcList = [];
      if(data.url.includes('.pdf')) { //判断文件类型
        this.isPdf = true;
      } else if(data.url.includes('.jpg') || data.url.includes('.png') || data.url.includes('.jpe')) {
        this.isImage = true;
      }
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=专著教材').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteEmpMethod(data) {
      return new Promise((resolve, reject) => {
            this.deleteRequest("/monograph/basic/remove/" + data.id).then((resp) => {
              this.dialogVisible = false;
              resolve('success');
            })
          }
      )
    },
    deleteEmp(data) {
      this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
        Promise.all([this.deleteEmpMethod(data), this.deleteOperationList(data)]).then(res => {
          this.$message.success('删除成功!');
          this.initMonographsList();
        }).catch(() => {
          this.$message.error('删除失败!');
        })
      })
    },
    deleteOperationList(data) {
      const params = {}
      params.prodId = data.id;
      params.prodType = '专著教材'
      return new Promise((resolve, reject) => {
        this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
          resolve('success');
        })
      })
    },
    editMonograph(params) {
      params.studentId = this.user.id;
      this.$refs["currentMonographCopy"].validate((valid) => {
        if (valid) {
          params.id = this.currentMonographCopy.id;
          this.postRequest1("/monograph/basic/edit", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.$message.success('编辑成功！')
                  this.doAddOper("commit", this.currentMonographCopy.id);
                }
              }
          );
        }
      });
    },
    addMonograph() {//专著或教材提交确认
      const params = {};
      params.name = this.currentMonographCopy.name;
      params.url = this.urlFile;
      params.rank = this.currentMonographCopy.rank;
      params.total = this.currentMonographCopy.total;
      params.author = this.currentMonographCopy.author;
      params.indicatorId = this.currentMonographCopy.indicatorId;
      params.date = this.currentMonographCopy.date;
      params.publisher = this.currentMonographCopy.publisher;
      params.isbn = this.currentMonographCopy.isbn;
      params.point = this.monographPoint;
      params.state = "commit";
      params.studentId = this.user.id;
      if(params.url == '' || params.url == null){
        this.$message.error('请上传证明材料！')
        return
      }
      if(params.url.indexOf("\\") >= 0) {
        params.url = params.url.replaceAll("\\", "/")
      }
      if(!this.isAuthorIncludeSelf) {
        this.$message.error('请仔细检查作者列表！');
        return;
      }
      if (this.currentMonographCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editMonograph(params);
      } else {
        this.$refs["currentMonographCopy"].validate((valid) => {
          if (valid) {
            params.studentId = this.user.id
            this.postRequest1("/monograph/basic/add", params).then(
                (resp) => {
                  if (resp) {
                    this.$message.success('添加成功！')
                    this.dialogVisible = false;
                    this.doAddOper("commit", resp.data);
                  }
                }
            );
          }
        });
      }
    },
    async doAddOper(state,monographID) {
      this.oper.state = state
      this.oper.prodId = monographID
      this.oper.operationName = "提交著作"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper);
      await this.initMonographsList();
    },
    showAddEmpView() {//点击添加科研专著或教材按钮
      this.title = "添加著作";
      this.dialogVisible = true;
      this.urlFile = '';
      this.files = [];
      this.currentMonographCopy = {};
      this.addButtonState = false;
      this.isAuthorIncludeSelf = false;
      this.indicatorBtn = '选择指标点';
      this.monographPoint = '';
      this.zeroPointReason = '';
      this.monoLimitRankN = '';
      this.currentSelectedIndicator = {};
    },
    initMonographsList() {
      this.loading = true;
      let url = "/monograph/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.monographsList = resp.data;
        }
      });
    },
  },
};
</script>

<style>
.select_div_input{
  margin-left:3px;
  width:120px;
  height:32px;
  position:relative;
  display:inline-block
}
.select_div{
  border-radius: 3px;
  margin-top: 5px;
  font-size: 14px;
  position: absolute;
  background-color: #fff;
  z-index: 999;
  overflow: auto;
  width: 300px;
  cursor: pointer;
}
.showInfo_dialog .el-form-item{
  margin-bottom: 5px;
}
.isMust{
  position: absolute;
  color: #F56C6C;
  top: 2px;
  left: -100px;
}
.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

</style>
