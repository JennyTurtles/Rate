<template>
  <div>
    <div
        style="display: flex; justify-content: space-between; margin: 15px 0"
    >
      <div>
        <label style="fontSize:10px">学生姓名：</label>
        <input type="text"
               style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
               placeholder="学生姓名"
               id="select_stuname">
        <label style="fontSize:10px;margin-left:16px">论文名称：</label>
        <input type="text"
               style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
               placeholder="论文名称"
               id="select_paperName">
        <label style="fontSize:10px;margin-left:16px">期刊：</label>
        <div class="select_div_input">
          <input
              style="margin-left:5px;width:120px;line-height:28px;
                            border:1px solid lightgrey;padding:0 10px 1px 15px;
                            border-radius:4px;color:gray;margin-right: 20px"
              placeholder="期刊"
              v-model="select_pub_option"
              @focus="ispubShow=true"
              @blur="ispubShow=ispubFlag"
              id="select_pub"/>
          <div class="select_div"
               v-show="ispubShow && select_pub_option != '' ? true:false"
               style="margin-right: 20px"
               :style="'height:${menuHeight}'"
               @mouseover="ispubFlag = true"
               @mouseleave="ispubFlag = false">
            <div
                style="margin:12px 0px 3px 12px;width:90%"
                v-for="val in select_pubName"
                :key="val.index"
                :value="val.value"
                @click="filter_pub(val.value)"
            >
              {{ val.value }}
            </div>
          </div>
        </div>

<!--修改点3-->
        <label v-show="role == 1" style="fontSize:10px;margin-left:40px;">论文状态：</label>
        <el-select
            v-show="role == 1"
            v-model="tmp1"
            style="margin-left:3px;width:100px"
            prefix-icon="el-icon-edit"
            clearable
            filterable
            placeholder="状态筛选"
            @change="((val) => filter(val,'select_state'))"
            id="select_state"
        >
          <el-option
              style="width:120px"
              v-for="val in option"
              :key="val"
              :value="val"
          >
          </el-option>
        </el-select>

        <label v-show="role == '8'" style="fontSize:10px;margin-left:50px">积分范围：</label>
        <label v-show="role == '1'"  style="fontSize:10px;margin-left:15px">积分范围：</label>
        <el-select
            v-model="tmp2"
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
            v-model="tmp3"
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
            style="margin-left:10px"
        >
          搜索
        </el-button>
        <el-button type="primary" v-show="role == '1'"
                   icon="el-icon-check" @click="auditing_commit_selected()">
          批量通过
        </el-button>
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
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="35px"> </el-table-column>
        <el-table-column
          prop="student.type"
          align="center"
          label="类别"
          width="75"
        >
        </el-table-column>
        <el-table-column
          prop="student.sname"
          align="center"
          label="学生姓名"
          width="75"
        >
        </el-table-column>
        <el-table-column
          prop="name"
          align="center"
          label="论文名称"
          width="230"
        >
        </el-table-column>
        <!-- width="200" -->
        <el-table-column
          prop="state"
          label="状态"
          width="100"
          align="center"
        >
        <template slot-scope="scope">
            <span
              style="padding: 4px"
              size="mini"
              :model="emp.state">
              {{scope.row.state=="commit"
              ? "学生提交"
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
<!--        需要修改！！！-->
        <el-table-column
          prop="publication.name"
          label="发表刊物"
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
        >
        </el-table-column>
        <el-table-column
          prop="comment"
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
        <el-form-item label="论文名:" prop="name">
          <span>{{ emp.name }}</span
          ><br />
        </el-form-item>
        <el-form-item label="学生姓名:" prop="student">
          <span>{{ emp.student.sname }}</span
          ><br />
        </el-form-item>
        <el-form-item label="期刊页码:" prop="pubPage">
          <span>{{ emp.pubPage}}</span
          ><br />
        </el-form-item>
        <el-form-item label="论文状态:" prop="state">
          <span>{{emp.state}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者人数:" prop="total">
          <span>{{emp.total}}</span
          ><br />
        </el-form-item>
        <el-form-item label="作者列表:" prop="author">
          <span>{{emp.author}}</span
          ><br />
        </el-form-item>
        <el-form-item label="排名:" prop="rank">
          <span>{{emp.rank}}</span
          ><br />
        </el-form-item>
        <el-form-item label="发表年份:" prop="year">
          <span>{{emp.year}}</span
          ><br />
        </el-form-item>
        <el-form-item label="发表月份:" prop="month">
          <span>{{emp.month}}</span
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
              <div style="font-size: 10px;">
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
                v-show="emp.state=='commit' || emp.state=='tea_pass' ? true:false"
                @click="(()=>{
                  if (this.role == 8)
                   auditing_commit('tea_pass')
                  else if (this.role == 1)
                   auditing_commit('adm_pass')
                }) "
                type="primary"
            >审核通过</el-button>
            <el-button
                id="but_reject"
                v-show="emp.state=='commit' || emp.state=='tea_pass' ? true:false"
                @click="isShowInfo = true"
                type="primary"
            >审核不通过</el-button>
            <el-button
                id="but_reject"
                v-show="emp.state=='tea_reject' ? true:false"
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
          placeholder="请输入论文驳回理由"
      >
      </el-input>
      <span slot="footer">
          <el-button @click=" (()=>{
            if (this.role == 8)
              auditing_commit('tea_reject')
            else if (this.role == 1)
              auditing_commit('adm_reject')
            isShowInfo = false
          })"
           type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SalSearch",
  data() {
    return {
      role:-1,//教师：8，管理员1
      tmp1:'',tmp2:'',tmp3:'', //假装绑定了v-model，让控制台不报错
      multipleSelection: [],
      ispubFlag:false,
      ispubShow:false,
      select_pub_option:'',
      operList:[],
      isShowInfo:false,
      select_stuName:["全部"],//筛选框
      select_paperName:["全部"],
      select_point:['全部',1,3,4,6,9,12,15],
      // select_state:[],
      select_pubName:[""],
      select_pubName2:[],
      option:["全部","学生提交","导师通过"],
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
      dialogVisible_show: false,
      dialogVisible_pass: false,
      dialogVisible_reject: false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      positions: [],
      reason:"",
      oper:{
        operatorRole:"",
        operatorID:JSON.parse(localStorage.getItem('user')).id,
        operatorName:JSON.parse(localStorage.getItem('user')).name,
        paperID:null,
        paperName:null,
        pubID:null,
        pubName:null,
        operation:"",
        state:"",
        remark:""
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
        publication:{}
        // reason:"",
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        name: [{ required: true, message: "请输入论文名", trigger: "blur" }],
        startDate: [
          { required: true, message: "请输入论文时间", trigger: "blur" },
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
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            this.select_pubName=[]
            if(resp.data != null){
              for(var i=0;i<resp.data.length;i++){
                if (this.select_pubName2.indexOf(resp.data[i].name) === -1)
                  continue
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
    auditing_commit_selected(){
      //操作记录待修改
      let url;
      const _this=this
      for (let i = 0; i < this.multipleSelection.length; i++)
      {
        this.loading = true;
        url= "/paper/basic/edit_state?state=adm_pass&ID="+this.multipleSelection[i].id
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            this.emp.state='adm_pass'
            this.total = resp.total;
            this.doAddOper('adm_pass',this.reason,
                this.multipleSelection[i].id,this.multipleSelection[i].name,
                this.multipleSelection[i].publication.name,this.multipleSelection[i].publicationID)
            _this.initEmps()
          }

        });
      }
      this.$message({
        type: 'success',
        message: `操作成功`
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //点击对话框中的确定按钮 触发事件
    auditing_commit(num){
      this.loading = true;
      let url;
      const _this=this
      this.dialogVisible_show=false
      url= "/paper/basic/edit_state?state="+num
      +"&ID="+this.emp.id
      if(false){
        this.loading = false;
        this.$message.success('论文已通过，无法驳回')
      }else{
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
          this.emp.state=num
          this.total = resp.total;
          this.emp.pubid = this.emp.publicationID;
          this.emp.pubName = this.emp.publication.name;
            this.$message({
              type: 'success',
              message: '操作成功'
            })
          this.doAddOper(num,this.reason,
                this.emp.id,this.emp.name,
                this.emp.pubName,this.emp.pubid);
          }
        }).finally(()=>{

          this.initEmps();
        });
      }          
    },
    doAddOper(state,reamrk,paperID,paperName,pubName,pubID) {
      this.oper.state=state
      this.oper.remark=reamrk,
      this.oper.paperID=paperID,
      this.oper.paperName=paperName,
      this.oper.pubName=pubName,
      this.oper.pubID=pubID
      if(this.oper.state=="tea_pass"){
        this.oper.operation="教师审核通过"
      }else if (this.oper.state == 'adm_pass')
        this.oper.operation="管理员审核通过"
      else if (this.oper.state =="tea_reject")
        this.oper.operation="教师驳回"
      else{
        this.oper.operation="管理员驳回"
      }
      console.log(this.oper)
      this.postRequest1("/paperoper/basic/add", this.oper).then(
        (resp) => {
          if (resp) {

          }
        }
      );    
    },
    rowClass(){
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    /** 查询角色列表 */
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
    initPositions() {
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    showAddEmpView() {
      this.emptyEmp();
      this.title = "添加论文";
      this.dialogVisible = true;
    },
    initEmps() {
      this.role = JSON.parse(localStorage.getItem('user')).role
      if (this.role == 1)
        this.oper.operatorRole = 'admin'
      else if (this.role == 8)
        this.oper.operatorRole = 'teacher'
      this.loading = true;
      let url = "/paper/basic/List";
      this.getRequest(url).then((resp) => {
        this.role = JSON.parse(localStorage.getItem('user')).role
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
          var newemps=[]//筛选待审核的
          //修改点1 ok
          var authority=[]
          if (this.role == 8)
            authority = ['commit']
          else if (this.role == 1)
            authority = ['commit','tea_pass']
          for(var i=0;i<this.emps.length;i++){
            if(authority.indexOf(this.emps[i].state) !== -1){
              newemps.push(this.emps[i])
            }
          }
          this.emps=newemps
          this.copyemps=this.emps
          //将学生姓名等存起来做筛选
          for(var i=0;i<this.emps.length;i++){
            this.select_pubName2.push(this.emps[i].publication.name)
            var judge=this.emps[i].student.sname
            this.emps[i].student.type='论文'
            if(this.select_stuName.indexOf(judge)>-1){
              continue;
            }else{
              this.select_stuName.push(judge)
            }
          }
          //按提交时间降序排序
          this.emps.sort(function(a,b){
            return a.time > b.time ? -1 : 1
          })
        }
      });
    },
    showGroupmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/table",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showInsertmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/group",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showteachermanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    showScoreItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/month",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    // 修改点2
    searchEmps() {//根据条件搜索论文
      var newemps=new Set()
      // var copyemps=this.emps
      var stuname=document.getElementById("select_stuname").value
      var state=''
      if (this.role == 1)
        state=document.getElementById("select_state").value
      else if (this.role == 8)
        state='学生提交'
      if(state === '学生提交'){
        state = 'commit'
      }else if (state === '导师通过'){
        state = 'tea_pass'
      }
      var paper=document.getElementById("select_paperName").value
      var pub=document.getElementById("select_pub").value
      var point1=document.getElementById("select_point1").value
      var point2=document.getElementById("select_point2").value
      for(var i=0;i<this.copyemps.length;i++){
        if(((this.copyemps[i].student.sname.indexOf(stuname) >= 0 )||(stuname == '全部' || stuname == ''))&&
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
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
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

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}

</style>
