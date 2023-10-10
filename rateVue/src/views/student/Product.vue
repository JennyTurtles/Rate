<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            添加产品
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          stripe
          border
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
      >
        <el-table-column
            fixed
            prop="name"
            align="center"
            label="产品名称"
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
            prop="author"
            align="center"
            label="完成人"
            min-width="10%"
        >
        </el-table-column>
        <el-table-column
            prop="indicator.name"
            label="产品类别"
            align="center"
            min-width="15%"
        >
        </el-table-column>
        <el-table-column
            prop="point"
            label="积分"
            align="center"
            min-width="10%"
        >
        </el-table-column>
        <el-table-column
            prop="operationList[0].remark"
            style="width:90px"
            align="center"
            label="备注"
            min-width="25%"
        >
        </el-table-column>
        <el-table-column align="center" width="275" label="操 作" min-width="20%">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
                v-show="scope.row.state == 'commit' || scope.row.state == 'tea_reject' || scope.row.state == 'adm_reject'? true : false"
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

    <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" center>
      <el-form
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="150px"
          :rules="rules"
          :model="currentProductCopy"
          ref="currentProductCopy"
      >
        <el-form-item label="产品名称:" prop="name" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentProductCopy.name"
              placeholder="请输入产品名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="完成年月:" prop="date" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-date-picker
              v-model="currentProductCopy.date"
              type="month"
              value-format="yyyy-MM"
              placeholder="完成年月">
          </el-date-picker>
        </el-form-item>
        <el-form-item  prop="author" label="完成人:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-input
              size="mini"
              style="width:80%"
              prefix-icon="el-icon-edit"
              v-model="currentProductCopy.author"
              @blur="judgeProductee()"
              placeholder="请输入完成人,如有多个用分号按顺位分隔"
          ></el-input>
        </el-form-item>
        <el-form-item label="指标点:" label-width="80px" style="margin-left: 20px;">
          <span class="isMust">*</span>
          <el-button ref="selectBtn" size="mini" type="text" @click="initTree()">{{indicatorBtn}}</el-button>
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
        <span style="color:gray;font-size:10px">将会获得：{{productPoint}}积分</span>
        <span style="color:gray;font-size:10px;margin-left: 8px">{{zeroPointReason}}</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAddProduct">取 消</el-button>
        <el-button type="primary" @click="addAward" v-show="addButtonState">提 交</el-button>
      </span>
    </el-dialog>

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
          :model="currentProduct"
          style="margin-left: 20px">

        <el-form-item label="产品名称:" prop="name">
          <span>{{ currentProduct.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="受理日期:" prop="date">
          <span>{{ currentProduct.date }}</span
          ><br />
        </el-form-item>
        <el-form-item label="完成人:" prop="author">
          <span>{{ currentProduct.author }}</span
          ><br />
        </el-form-item>
        <el-form-item label="总人数:" prop="total">
          <span>{{ currentProduct.total }}</span
          ><br />
        </el-form-item>
        <el-form-item label="排名:" prop="rank">
          <span>{{ currentProduct.rank }}</span
          ><br />
        </el-form-item>
        <el-form-item label="积分:" prop="point">
          <span>{{ currentProduct.point }}</span
          ><br />
        </el-form-item>
        <el-form-item label="状态:" prop="state">
          <span>{{ currentProduct.state=="commit"
              ? "已提交"
              :currentProduct.state=="tea_pass"
                  ? "导师通过"
                  :currentProduct.state=="tea_reject"
                      ? "导师驳回"
                      :currentProduct.state=="adm_pass"
                          ? "管理员通过"
                          :"管理员驳回" }}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentProduct.url == '' || currentProduct.url == null ? true:false" >无证明材料</span>
          <a v-else style="color:gray;font-size:11px;text-decoration:none;cursor:pointer"
             @click="download(currentProduct)"
             onmouseover="this.style.color = 'blue'"
             onmouseleave="this.style.color = 'gray'">
            {{currentProduct.url|fileNameFilter}}</a>
          <br />
        </el-form-item>
        <div >
          <span>历史操作:</span>
          <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;font-size:5px;margin-left:5px">
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
            :data="indicatorData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :expand-on-click-node="false"
            :highlight-current="true"
            node-key="id"
            :default-expanded-keys="defaultExpandedKeys"
        ></el-tree>
      </span>
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
      defaultExpandedKeys: [],
      zeroPointReason: '',
      isAuthorIncludeSelf: false,
      indicatorBtn: '选择指标点',
      defaultProps: {
        children: "children",
        label: "label",
      },
      showTreeDialog: false,
      currentSelectedIndicator: {},
      indicatorData: [],
      //在编辑状态时，做一个副本，用户点击取消可恢复原始状态
      currentProductCopy: {},
      productStatusList: ['受理', '初审', '公布', '实审', '授权', '转让'],
      productPoint:0,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      files:[],//选择上传的文件列表
      urlFile:'',//文件路径
      addButtonState:true,//是否允许添加项目
      operList:[],//每个项目的历史操作记录
      productee:'',//和输入的作者列表绑定
      labelPosition: "left",
      title: "",
      title_show: "",
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_show: false,
      dialogVisible_showInfo:false,
      oper:{
        operatorRole: "student",
        operatorId: JSON.parse(localStorage.getItem('user')).id,
        operatorName: JSON.parse(localStorage.getItem('user')).name,
        prodType: '产品应用',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      currentProduct: {
        id: '',
        name:'',
        studentId: '',
        date:"",
        author:"",
        rank:"",
        total:"",
        point:"",
        url:'',
        state:" ",
        remark: '',
        indicatorId:''
      },
      rules: {
        name: [{ required: true, message: "请输入产品名称", trigger: "blur" }],
        author: [{ required: true, message: "请输入产品作者", trigger: "blur" }],
        date: [{ required: true, message: "请选择完成时间", trigger: "blur" }]
      }
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    }
  },
  created() {},
  mounted() {
    this.currentProductCopy = JSON.parse(JSON.stringify(this.currentProduct));
    this.initEmps();
    // this.initTutor(this.user);
  },
  filters:{
    fileNameFilter:function(data){//将证明材料显示出来
      if(data == null || data == ''){
        return '无证明材料'
      }else{
        var arr= data.split('/')
        return  arr.reverse()[0]
      }
    }
  },
  methods: {
    handleNodeClick(data, node) {
      if (data.children.length == 0) {
        this.indicatorBtn = data.label;
        this.currentSelectedIndicator = data;
        this.currentProductCopy.indicatorId = data.id;
        if (!this.isAuthorIncludeSelf) {
          this.productPoint = 0;
          this.zeroPointReason = '参与人未包含自己';
        } else {
          this.productPoint = data.score;
          this.zeroPointReason = '';
        }
        this.showTreeDialog = false;
      }
    },
    initTree() {
      this.getRequest("/indicator/getAllByType?type=产品应用").then( resp => {
        this.showTreeDialog = true;
        this.defaultExpandedKeys = [];
        if (resp) {
          this.indicatorData = resp.obj[1];
          if(this.indicatorData.length > 0)
            if(this.indicatorData[0].children.length > 0) {
              this.defaultExpandedKeys.push(this.indicatorData[0].children[0].id);
            } else this.defaultExpandedKeys.push(this.indicatorData[0].id);
        }
      });
    },
    //添加 编辑框点击取消出发事件
    cancelAddProduct() {
      this.dialogVisible = false;
    },
    download(data){//下载证明材料
      var fileName = data.url.split('/').reverse()[0]
      var url = data.url
      axios({
        url: '/product/basic/downloadByUrl?url='+url,
        method: 'GET',
        responseType: 'blob'
      }).then(response => {
        const url = window.URL.createObjectURL(new Blob([response]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      });
    },
    handleDelete() {//删除选择的文件
      var file={
        filepath:this.urlFile
      }
      this.postRequest1("/product/basic/deleteFile",file).then(
          (response)=>{
            this.urlFile = '';
            this.files = [];
          },()=>{}
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
      axios.post("/product/basic/upload",formData,{
        headers:{
          'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
      }).then(
          (response)=>{
            this.$message({
              message:'上传成功！'
            })
            this.addButtonState = true
            //获取文件路径
            this.urlFile=response.data
          },()=>{}
      )
    },
    judgeProductee(){//输入作者框 失去焦点触发事件
      var val = this.currentProductCopy.author;
      if(!val || val === '') {
        this.isAuthorIncludeSelf = false;
        return;
      }
      var isalph = false//判断输入中是否有英文字母
      for(var i in val){
        var asc = val.charCodeAt(i)
        if(asc >= 65 && asc <= 90 || asc >= 97 && asc <= 122){
          isalph = true
          break
        }
      }
      var memberList = null
      var info = this.user;
      memberList = val.split(/[;；]/)
      memberList = memberList.map(item => {
        return item && item.replace(/\s*/g,"");
      }).filter(v => {
        return v
      })
      //不止一个作者 判断自己在不在其中
      if(memberList.indexOf(info.name) == -1){//不在 并且没有英文单词
        this.$message.error("您的姓名【 " + info.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + info.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        this.isAuthorIncludeSelf = false;
        this.zeroPointReason = '参与人未包含自己'
        this.productPoint = 0;
      } else { //自己在里面
        if(this.currentSelectedIndicator) {
          this.productPoint = this.currentSelectedIndicator.score;
          this.zeroPointReason = '';
        }else {
          this.productPoint = '';
          this.zeroPointReason = '';
        }
        this.isAuthorIncludeSelf = true;
      }
      this.currentProductCopy.total = memberList.length;
      this.currentProductCopy.rank = memberList.indexOf(info.name) + 1;
    },

    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    showEditEmpView(data) {
      this.currentSelectedIndicator = data.indicator;
      this.dialogVisible = true;
      this.title = "编辑产品信息";
      this.currentProductCopy = JSON.parse(JSON.stringify(data));
      this.files = [
        {
          name: this.currentProductCopy.url.split('/').reverse()[0],
          url: this.currentProductCopy.url
        }
      ];
      this.indicatorBtn = data.indicator.name;
      this.productPoint = data.point;
      this.zeroPointReason = '';
      this.urlFile = this.currentProductCopy.url;
      this.isAuthorIncludeSelf = true;
      this.addButtonState = true;
    },
    showInfo(data){
      this.title_show = "显示详情";
      this.currentProduct = data
      this.dialogVisible_showInfo = true
      this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=产品应用').then((resp) => {
        this.loading = false;
        if (resp) {
          this.operList = resp.obj
        }
      });
    },
    deleteEmpMethod(data) {
      return new Promise((resolve, reject) => {
            this.deleteRequest("/product/basic/remove/" + data.id).then((resp) => {
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
          this.initEmps();
        }).catch(() => {
          this.$message.error('删除失败!');
        })
      })
    },
    deleteOperationList(data) {
      const params = {}
      params.prodId = data.id;
      params.prodType = '产品应用'
      return new Promise((resolve, reject) => {
        this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
          resolve('success');
        })
      })
    },
    editAward(params) {
      params.studentId = this.user.id;
      this.$refs["currentProductCopy"].validate((valid) => {
        if (valid) {
          this.postRequest1("/product/basic/edit", params).then(
              (resp) => {
                if (resp) {
                  this.dialogVisible = false;
                  this.doAddOper("commit", this.currentProductCopy.id);
                  this.$message.success('编辑成功！')
                }
              }
          );
        }
      });
    },
    addAward() {//项目提交确认
      const params = {};
      params.id = this.currentProductCopy.id;
      params.name = this.currentProductCopy.name;
      params.url = this.urlFile;
      params.rank = this.currentProductCopy.rank;
      params.total = this.currentProductCopy.total;
      params.author = this.currentProductCopy.author;
      params.indicatorId = this.currentProductCopy.indicatorId;
      params.author = this.currentProductCopy.author;
      params.date = this.currentProductCopy.date;
      params.point = this.productPoint;
      params.state = "commit";
      params.studentId = this.user.id;
      if(params.url == '' || params.url == null){
        this.$message.error('请上传证明材料！')
        return
      }
      if(!params.indicatorId) {
        this.$message.error('请选择指标点！')
        return;
      }
      if(!this.isAuthorIncludeSelf) {
        this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【"  + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
        return;
      }
      if (this.currentProductCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
        this.editAward(params);
      } else {
        this.$refs["currentProductCopy"].validate((valid) => {
          if (valid) {
            params.studentId = this.user.id
            this.postRequest1("/product/basic/add", params).then(
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
    async doAddOper(state, productID) {
      this.oper.state = state
      this.oper.prodId = productID
      this.oper.operationName = "提交产品"
      this.oper.time = this.dateFormatFunc(new Date());
      await this.postRequest1("/oper/basic/add", this.oper)
      await this.initEmps();
    },
    showAddEmpView() {//点击添加科研项目按钮
      this.addButtonState = true;
      this.isAuthorIncludeSelf = false;
      this.title = "添加产品";
      this.dialogVisible = true;
      this.urlFile = '';
      this.files = [];
      this.productPoint = '';
      this.currentSelectedIndicator = {};
      this.zeroPointReason = '';
      this.indicatorBtn = '选择指标点';
      this.currentProductCopy = {};
    },
    initEmps() {
      this.loading = true;
      let url = "/product/basic/studentID?studentID=" + this.user.id
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
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
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all 0.8s ease;
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

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}
</style>