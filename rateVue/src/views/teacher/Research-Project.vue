<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <div>
            <label style="fontSize:10px">学生姓名：</label>
            <input type="text"
                   style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
                   placeholder="学生姓名"

                   id="select_stuname">
            <!-- <el-select
              style="margin-left:5px;width:100px"
              prefix-icon="el-icon-edit"
              clearable
              filterable
              placeholder="学生姓名"
              @change="((val) => filter(val,'select_stuname'))"
              id="select_stuname"
              >
                <el-option
                  style="width:90px"
                  v-for="val in select_stuName"
                  :key="val"
                  :value="val"
                >
                </el-option>
            </el-select> -->

            <label style="fontSize:10px;margin-left:16px">项目名称：</label>
            <input type="text"
                   style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
                   placeholder="项目名称"
                   id="select_paperName">
            <!-- <el-select
                style="margin-left:3px;width:120px"
                prefix-icon="el-icon-edit"
                clearable
                filterable
                placeholder="论文名称"
                @change="((val) => filter(val,'select_paperName'))"
                id="select_paperName"
                >
                  <el-option
                    style=""
                    v-for="val in select_paperName"
                    :key="val"
                    :value="val"
                  >
                  </el-option>
            </el-select> -->

            <label style="fontSize:10px;margin-left:40px;">项目状态：</label>
            <el-select
                style="margin-left:3px;width:100px"
                prefix-icon="el-icon-edit"
                clearable
                filterable
                placeholder="状态筛选"
                @change="((val) => filter(val,'select_state'))"
                id="select_state"
            >
              <el-option
                  style="width:90px"
                  v-for="val in option"
                  :key="val"
                  :value="val"
              >
              </el-option>
            </el-select>
            <label style="fontSize:10px;margin-left:16px">积分范围：</label>
            <el-select
                style="margin-left:3px;width:60px"
                prefix-icon="el-icon-edit"
                clearable
                filterable
                placeholder="1"
                @change="((val) => filter(val,'select_point1'))"
                id="select_point1"
            >
              <el-option
                  style=""
                  v-for="val in select_point"
                  :key="val"
                  :value="val"
              >
              </el-option>
            </el-select>
            <label >&nbsp; - &nbsp;</label>
            <el-select
                style="margin-left:3px;width:60px"
                prefix-icon="el-icon-edit"
                clearable
                filterable
                placeholder="12"
                @change="((val) => filter(val,'select_point2'))"
                id="select_point2"
            >
              <el-option
                  style=""
                  v-for="val in select_point"
                  :key="val"
                  :value="val"
              >
              </el-option>
            </el-select>
            <el-button
                icon="el-icon-search"
                type="primary"
                @click="searchEmps"
                :disabled="showAdvanceSearchView"
                style="margin-left:30px"
            >
              搜索
            </el-button>
          </div>
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
        <el-table-column type="selection" width="35px"> </el-table-column>
        <el-table-column
            prop="student.sname"
            align="center"
            label="学生姓名"
            width="75"
        >
        </el-table-column>
        <el-table-column
            prop="program.name"
            align="center"
            label="项目名称"
            width="230"
        >
        </el-table-column>
        <!-- width="200" -->
        <el-table-column
            prop="state"
            label="状态"
            width="90"
            align="center"
        >
          <template slot-scope="scope">
            <span
                style="padding: 4px"
                size="mini"
                :model="emp.state"
                :style="scope.row.state=='tea_reject' ? {'color':'red'}:{'color':'gray'}"
            >
              {{scope.row.state=="commit"
                ? "待审核"
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
            prop="indicator.type"
            label="项目类别"
            align="center"
            width="240"
        >
        </el-table-column>
        <el-table-column
            prop="point"
            label="积分"
            align="center"
            width="80"
        >
        </el-table-column>
        <el-table-column
            prop="remark"
            label="备注"
            align="center"
            width="300"
        >
        </el-table-column>
        <el-table-column
            width="130"
            align="center"
            label="审核"
        >
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView_show(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex; justify-content: flex-end; margin: 10px 0">
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
    <!-- 对话框 老师审核通过项目 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisible_pass" width="30%" center>
      <!-- 确定审核通过该学生项目？ -->
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          :rules="rules"
          ref="empForm"
          style="margin-left: 60px"
      >
        <el-form-item label="科研项目ID:" prop="id">
          <span>{{ emp.id }}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="dialogVisible_pass = false">取 消</el-button> -->
        <el-button type="primary" @click="auditing_commit('tea_pass')">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 对话框 老师驳回该学生科研项目 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisible_reject" width="30%" center>

      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          :rules="rules"
          ref="empForm"
          style="margin-left: 40px"
      >
        <el-form-item label="科研项目ID:" prop="id">
          <span>{{ emp.id }}</span>
        </el-form-item>
        <el-form-item label="驳回理由:">
          <el-input
              type="textarea"
              :rows="4"
              v-model="reason"
              placeholder="驳回理由"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="dialogVisible_pass = false">取 消</el-button> -->
        <el-button type="primary" @click="auditing_commit('tea_reject')">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 查看详情按钮 -->
    <el-dialog
        class="showInfo_dialog"
        :title="title_show"
        :visible.sync="dialogVisible_show"
        width="520px"
        center>
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          :rules="rules"
          ref="empForm"
          style="margin-left: 20px"
      >
        <el-form-item label="项目名:" prop="name">
          <span>{{ emp.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="学生姓名:" prop="student">
          <span>{{ emp.student.sname }}</span
          ><br />
        </el-form-item>
        <!-- <el-form-item label="发表期刊:" prop="publication">
          <span>{{ emp.publication }}</span
          ><br />
        </el-form-item> -->

        <el-form-item label="项目状态:" prop="state">
          <span>{{emp.state}}</span
          ><br />
        </el-form-item>
        <el-form-item label="参加人数:" prop="total">
          <span>{{emp.total}}</span
          ><br />
        </el-form-item>
        <el-form-item label="列表:" prop="author">
          <span>{{emp.author}}</span
          ><br />
        </el-form-item>
        <el-form-item label="排名:" prop="rank">
          <span>{{emp.rank}}</span
          ><br />
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <el-button @click="download(emp)" type="primary">下载材料</el-button>
          &nbsp;&nbsp;&nbsp;&nbsp;
          <span style="color:gray;font-size:11px">该学生证明材料为：xxx（文件名称包括后缀）</span>
          <br />
        </el-form-item>
        <div >
          <span>历史操作:</span>
          <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;font-size:5px;margin-left:5px">
              <div >
                <p>{{item.time|dataFormat}}&nbsp;&nbsp;&nbsp;{{item.operatorName}}&nbsp;&nbsp;&nbsp;{{item.operation}}</p>
                <p v-show="item.remark == '' ? false : true">驳回理由：{{item.remark}}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer" :model="emp">
            <el-button
                id="but_pass"
                v-show="emp.state=='commit' ? true:false"
                @click="auditing_commit('tea_pass')"
                type="primary"
            >审核通过</el-button>
            <el-button
                id="but_reject"
                v-show="emp.state=='commit' ? true:false"
                @click="isShowInfo = true"
                type="primary"
            >审核不通过</el-button>
            <el-button
                id="but_reject"
                v-show="emp.state=='tea_pass' || emp.state=='tea_reject' ? true:false"
                @click="dialogVisible_show = false"
                type="primary"
            >关闭</el-button>
        </span>
    </el-dialog>
    <el-dialog v-model="emp" :visible.sync="isShowInfo">
      <el-input
          type="textarea"
          :rows="4"
          v-model="reason"
          placeholder="请输入项目驳回理由"
      >
      </el-input>
      <span slot="footer">
          <el-button @click="auditing_commit('tea_reject')"
                     type="primary">
                确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import { set } from 'vue';
export default {
  name: "SalSearch",
  data() {
    return {
      ispubFlag:false,
      ispubShow:false,
      select_pub_option:'',
      operList:[],
      isShowInfo:false,
      select_stuName:["全部"],//筛选框
      select_paperName:["全部"],
      select_point:['全部',1,3,4,6,9,12,15],
      select_pubName:[],
      option:["全部","通过","待审核","驳回"],
      labelPosition: "left",
      title: "",
      title_show: "",
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      copyemps:[],
      emps: [],
      loading: false,
      dialogVisible: false,
      dialogVisible_pass: false,
      dialogVisible_reject: false,
      dialogVisible_show: false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      positions: [],
      reason:"",
      oper:{
        operatorRole: "teacher",
        operatorID: null,
        prodType: 'project',
        operationName: '',
        state: '',
        remark: '',
        prodId: null,
        time: null
      },
      emp: {
        id: null,
        institutionID: null,
        name: null,
        startDate: "2022/02/02",
        scoreItemCount: "0",
        score: "100",
        groupCount: "0",
        expertCount: "0",
        participantCount: "0",
        comment: "javaboy",
        state:"",
        student:{},
        total:0,
        rank:0
        // reason:"",
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        name: [{ required: true, message: "请输入项目名", trigger: "blur" }],
        startDate: [
          { required: true, message: "请输入项目时间", trigger: "blur" },
        ],
        scoreItemCount: [
          {
            required: true,
            type: "number",
            message: "请输入正确数据",
            trigger: "blur",
            transform: (value) => Number(value),
          },
        ],
        comment: [{ required: true, message: "请输入备注", trigger: "blur" }],
      },
    };
  },
  computed: {
    user() {
      return this.$store.state.currentHr; //object信息
    },
    menuHeight() {
      return this.select_pubName.length * 50 > 150
          ? 150 + 'px'
          : '${this.select_pubName.length * 50}px'
    },
  },
  created() {},
  mounted() {
    this.initEmps();
    this.initPositions();
  },
  watch:{
    //期刊输入框变化
    select_pub_option:{
      handler(val){
        let url = "/publication/basic/listByName?publicationName=" + val
        console.log(url);
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            this.select_pubName=[]
            if(resp.data != null){
              for(var i=0;i<resp.data.length;i++){
                this.select_pubName.push(
                    {
                      index:resp.data[i].id,
                      value:resp.data[i].name,
                      point:resp.data[i].indicator.score
                    }
                )
              }
            }else{
              this.$message.error(`请检查期刊名称的拼写`);
            }

            // console.log("xuanzel:");
            // console.log(resp);
            // console.log(this.select_pubName);
            // document.getElementById("select_pub").value=val
          }
        });
      }

    }
  },
  methods: {
    filter(val,options){
      document.getElementById(options).value=val
    },
    filter_pub(val){//选择期刊
      this.select_pub_option=val
      this.ispubFlag=false
      this.ispubShow=false
    },
    //点击对话框中的确定按钮 触发事件
    auditing_commit(num){
      // this.emp=data
      this.loading = true;
      let url;
      const _this=this
      this.dialogVisible_show=false
      url= "/paper/basic/edit_state?state="+num
          +"&ID="+this.emp.id
      if(num == "tea_reject"){
        this.isShowInfo=true
      }
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emp.state=num
          this.total = resp.total;
          this.isShowInfo=false
          this.doAddOper(num, this.reason, this.emp.id)
        }
      });

    },
    doAddOper(state,remark, projectID) {
      this.oper.state=state;
      this.oper.operatorID = this.user.id;
      this.oper.remark = remark;
      this.oper.prodId = projectID;
      this.oper.time = this.dateFormatFunc(new Date());
      if(this.oper.state=="tea_pass"){
        this.oper.operation="审核通过"
      }else{
        this.oper.operation="审核驳回"
      }
      this.postRequest1("/oper/basic/add", this.oper).then(
          (resp) => {
            if (resp) {
              this.initEmps()
            }
          }
      );
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    emptyEmp() {
      this.emp = {
        id: null,
        startDate: null,
        name: "",
        scoreItemCount: "0",
        comment: "论文备注example：关于xxx的论文",
      };
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
      this.getRequest("/paperoper/basic/List?ID="+data.id).then((resp) => {
        this.loading = false;
        if (resp) {
          this.isShowInfo=false
          this.operList=resp.data
          this.operList.sort(function(a,b){
            return a.time > b.time ? -1 : 1
          })
        }
      });
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    initEmps() {
      this.loading = true;
      let url = "/paper/basic/List";
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.copyemps=this.emps
          this.total = resp.total;
          for(var i=0;i<this.emps.length;i++){
            var papername=this.emps[i].name
            if(this.select_paperName.indexOf(papername)==-1){
              this.select_paperName.push(papername)
            }
            var judge=this.emps[i].student.sname
            if(this.select_stuName.indexOf(judge)==-1){
              this.select_stuName.push(judge)
            }
            var pub=this.emps[i].publication.name
            if(this.select_pubName.indexOf(pub)==-1){
              this.select_pubName.push(pub)
            }
          }
          this.emps.sort(function(a,b){
            return a.time > b.time ? -1 : 1
          })
        }
      });
    },
    searchEmps() {//根据条件搜索论文
      var newemps=new Set()
      // var copyemps=this.emps
      var stuname=document.getElementById("select_stuname").value
      var state=document.getElementById("select_state").value
      if(state == '通过'){
        state = 'tea_pass'
      }else if(state == '驳回'){
        state = 'tea_reject'
      }if(state == '待审核'){
        state = 'commit'
      }
      var paper=document.getElementById("select_paperName").value
      var pub=document.getElementById("select_pub").value
      var point1=document.getElementById("select_point1").value
      var point2=document.getElementById("select_point2").value
      for(var i=0;i<this.copyemps.length;i++){
        if((((this.copyemps[i].student.sname.indexOf(stuname) >= 0 ))||(stuname == '全部' || stuname == ''))&&
            (((this.copyemps[i].state == state))||(state == '全部' || state == ''))&&
            (((this.copyemps[i].name.indexOf(paper) >= 0) )||(paper == '全部' || paper == ''))&&
            (((this.copyemps[i].point <= point2 &&this.copyemps[i].point >= point1))||(point1 == '全部' || point1 == '' || point2 == '全部' || point2 == ''))&&
            (((this.copyemps[i].publication.name == pub) )||(pub == '全部' || pub == ''))
        ){
          newemps.add(this.copyemps[i])
        }
      }
      this.emps=Array.from(newemps)
    },
  },
};
</script>

<style>
.showInfo_dialog .el-form-item{
  margin-bottom: 5px;
}
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
input[type=text]::placeholder {
  color:lightgrey;
}
input:focus{
  border:1px solid lightblue;
}
.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}
div::-webkit-scrollbar {
  /* 隐藏默认的滚动条 */
  -webkit-appearance: none;
}
div::-webkit-scrollbar:vertical {
  /* 设置垂直滚动条宽度 */
  width: 6px;
}
div::-webkit-scrollbar-thumb {
  /* 滚动条的其他样式定制，注意，这个一定也要定制，否则就是一个透明的滚动条 */
  border-radius: 8px;
  border: 3px solid rgba(255,255,255,.4);
  background-color: rgba(0, 0, 0, .5);
}
</style>
