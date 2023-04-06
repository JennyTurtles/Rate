<template>
  <div>
    <div>
      <el-button type="primary" @click="downloadExcel">下载模版</el-button>
      <el-upload
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-success="onSuccess"
          style="display: inline-flex; margin-left: 8px"
          :action="UploadUrl()"
      >
        <el-button type="primary">上传学生excel</el-button>
      </el-upload>
    </div>
    <div style="margin-top: 10px">
      <span>
        请选择条件进行搜索：
      </span>
      <div class="select_div_input">
        <input
            autocomplete="off"
            style="width:95%;line-height:28px;
                              border:1px solid lightgrey;padding:0 10px 1px 15px;
                              border-radius:4px;color:gray"
            placeholder="请输入老师姓名"
            v-model="selectTeacerName"
            @focus="inputSelectTeacerNameFocus"
            @blur="isSelectShow = isSelectFlag"/>
        <div class="select_div"
             v-show="isSelectShow && selectTeacerName ? true:false"
             :style="'height:${menuHeight}'"
             @mouseover="isSelectFlag = true"
             @mouseleave="isSelectFlag = false"
        >
          <div
              class="select_div_div"
              v-for="val in select_teachers"
              :key="val"
              :value="val"
              @click="filter_teas(val)"
          >
            {{ val }}
          </div>
        </div>
      </div>
      <div class="select_div_input" style="margin-left: 30px">
        <input
            autocomplete="off"
            style="width:95%;line-height:28px;
                              border:1px solid lightgrey;padding:0 10px 1px 15px;
                              border-radius:4px;color:gray"
            placeholder="请输入入学年份"
            v-model="selectYear"
            @focus="inputSelectYearFocus"
            @blur="isSelectYearShow = isSelectYearFlag"/>
        <div class="select_div"
             v-show="isSelectYearShow"
             style="height:200px;overflow: scroll"
             @mouseover="isSelectYearFlag = true"
             @mouseleave="isSelectYearFlag = false"
        >
          <div
              class="select_div_div"
              v-for="val in selectYearsList"
              :key="val"
              :value="val"
              @click="filter_year(val)"
          >
            {{ val }}
          </div>
        </div>
      </div>
      <el-button @click="filterBtn" style="margin-left: 30px;" type="primary">筛选</el-button>
    </div>

    <div style="margin-top: 10px">
      <el-table
        :data="undergraduateStudents">
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="stuNumber" label="学号" align="center"></el-table-column>
        <el-table-column prop="telephone" label="电话" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
        <el-table-column prop="year" label="入学年份" align="center"></el-table-column>
        <el-table-column prop="idnumber" label="身份证号" align="center"></el-table-column>
        <el-table-column prop="teachers.name" label="导师姓名" align="center"></el-table-column>
        <el-table-column  label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" plain @click="editDialogShow(scope.row)" type="primary">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="deleteUnder(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="编辑信息" :visible.sync="dialogEdit" center width="500px">
      <template>
        <el-form :model="currentUnderStudentOfEdit">
          <el-form-item label="导师姓名">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.teachers.name"></el-input>
          </el-form-item>
          <el-form-item label="导师工号">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.teachers.jobnumber"></el-input>
          </el-form-item>
          <el-form-item label="学生姓名">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.name"></el-input>
          </el-form-item>
          <el-form-item label="学生手机号">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.telephone"></el-input>
          </el-form-item>
          <el-form-item label="学生邮箱">
            <el-input style="width: 50%" v-model="currentUnderStudentOfEdit.email"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editUnder" type="primary">确定</el-button>
          <el-button @click="closeDialogEdit">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    <div style="margin-top: 10px;text-align:right">
      <el-pagination @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="currentPage"
                     :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                     :total="totalCount"
                     :page-sizes="pageSizes"
                     background
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {Message} from "element-ui";
export default {
  name: "SalStudentM",
  data(){
    return {
      pageSizes:[20,20,20,20,30],
      totalCount:0,
      currentPage:1,
      pageSize:20,
      isSelectYearFlag:false,
      isSelectYearShow:false,
      selectYearsList:[],
      isSelectFlag:false,
      isSelectShow:false,//搜索老师名字的搜索框
      yearTimer:null,
      timer:null,
      select_teachers:[],
      selectTeacerName:'',
      selectYear:'',
      currentUnderStudentOfEdit:{
        ID:null,
        name:'',
        teachers:{
          name:'',
          jobnumber:''
        },
        studentID:null,
        tutorID:null,
        telephone:'',
        email:''
      },
      dialogEdit:false,
      user:{},
      undergraduateStudents:[]
    }
  },
  watch:{
    selectTeacerName:{
      handler(val){
        this.delayInputTimer(val)
      }
    }
  },
  computed:{
    menuHeight() {
      return this.selectTeacerName.length * 50 > 150
          ? 150 + 'px'
          : `${this.selectTeacerName.length * 50}px`
    },
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem('user'))
    this.initSelectYearsList()
    this.initUnderGraduateStudents()
  },
  methods:{
    inputSelectYearFocus(){//年份输入框获得焦点
      this.isSelectYearShow = true
    },
    filter_year(val){//点击年份下拉框的某个选项
      this.selectYear = val
      this.isSelectYearShow = false
    },
    filterBtn(){//点击筛选按钮
      let tempYear = this.selectYear
      if(this.selectYear == ''){
        tempYear = 0
      }
      let url = '/undergraduateM/basic/getUnderStudentsBySelect?year=' + parseInt(tempYear) + '&teaName=' + this.selectTeacerName
      this.getRequest(url).then((resp)=>{
        if(resp){
          if(resp.status == 200){
            this.undergraduateStudents = resp.obj
          }
        }
      })
    },
    filter_teas(val){//点击某个筛选出来的名字
      this.selectTeacerName = val
      this.isSelectShow=false
      this.isSelectFlag=false
    },
    delayInputTimer(val){//防抖
      if(this.timer){
        clearTimeout(this.timer)
      }
      if(!val){
        return
      }
      if(this.selectYear == ''){
        this.selectYear = 0
      }
      let that = this
      this.timer = setTimeout(()=>{
        let url = '/undergraduateM/basic/getTeaNamesBySelect?teaName=' + this.selectTeacerName
        that.getRequest(url).then((resp)=>{
          that.select_teachers = []
          if(resp){
            if(resp.status == 200){
              for(var i=0;i<resp.obj.length;i++){
                that.select_teachers.push(resp.obj[i])
              }
              that.select_teachers = Array.from(new Set(that.select_teachers));
            }
          }
        })
      },300);
    },
    inputSelectTeacerNameFocus(){//input获取焦点判断是否有下拉框，是否可输入
      this.isSelectShow = true//控制下拉框是否显示
    },
    closeDialogEdit(){//关闭对话框
      this.dialogEdit = false
      this.currentUnderStudentOfEdit = {}
    },
    editDialogShow(data){//控制变量
      this.dialogEdit = true
      this.currentUnderStudentOfEdit = data
    },
    editUnder(){//点击编辑中的确定按钮
      if(this.currentUnderStudentOfEdit.teachers.name == '' || this.currentUnderStudentOfEdit.teachers.jobnumber == ''){
        this.$message.warning('请填写老师姓名和工号！')
        return
      }
      let data = this.currentUnderStudentOfEdit
      this.postRequest('/undergraduateM/basic/editUnderGraduateStudent',data).then((resp)=>{
        if(resp){
          if(resp.status == 200){
            this.dialogEdit = false
            this.$message.success(resp.msg)
            this.initUnderGraduateStudents()
          }else {
            this.$message.error(resp)
          }
        }
      })
    },
    deleteUnder(data){//删除本科生
      this.$confirm('确定删除吗？','提示',{
        confirmButtonText:'确定',
        cancelButtonText:'取消',
        type:"warning"
      }).then(()=> {
        this.postRequest('/undergraduateM/basic/deleteUnderGraduateStudent', data).then((resp) => {
          if (resp.code == 200) {
            this.$message.success('删除成功')
            this.initUnderGraduateStudents(1,this.pageSize)
          }else {
            this.$message.warning('删除失败！')
          }
        })
      })
    },
    onSuccess(res){//上传excel成功的回调
      if(res.status == 200){
        this.$message.success("导入成功")
        this.initUnderGraduateStudents(1,this.pageSize);
      }else {
        this.$message.error("导入失败")
      }
    },
    beforeUpload() {//上传前
      this.$message.success("正在导入")
    },
    UploadUrl(){
      let url = '/undergraduateM/basic/importUnderGraduate?institutionID=' + this.user.institutionID
      return url;
    },
    downloadExcel(){
      let url = '/undergraduateM/basic/exportUnderGraduate'
      this.$message.success('正在下载')
      window.open(url,'_parent')
    },
    initSelectYearsList(){//初始化筛选框中的年份数据
      let timeDate = new Date()
      let temp1 = Array.from(Array(timeDate.getFullYear() - 20).keys(), n=>n+1)
      let temp2 = Array.from(Array(timeDate.getFullYear()).keys(), n=>n+1)
      this.selectYearsList = temp2.filter(item1 => !temp1.some(item2 => item2 === item1))//去掉两者相同的，留下不同的
      this.selectYearsList.sort((a,b)=>{
        return b - a;
      })
    },
    handleSizeChange(val) {
      // 改变每页显示的条数
      this.pageSize=val
      // 注意：在改变每页显示的条数时，要将页码显示到第一页
      this.currentPage=1
      this.initUnderGraduateStudents(this.currentPage,this.pageSize)
    },
    // 显示第几页
    handleCurrentChange(val) {
      // 改变默认的页数
      this.currentPage=val;
      this.initUnderGraduateStudents(this.currentPage,this.pageSize)
    },
    initUnderGraduateStudents(curr,pagesize){//初始化本科生
      //因为很多不同情况下都要初始化数据，所以不能只依靠data中都两个参数
      this.getRequest('/undergraduateM/basic/getUnderGraduateStudents?pageNum=' + curr + '&pageSize=' + pagesize).then((response)=>{
        if(response.code == 200){
          this.undergraduateStudents = response.extend.res[0]
          this.totalCount = response.extend.res[1]
        }
      })
    }
  }
}
</script>

<style scoped>
.select_div_input{
  /*margin-left:3px;*/
  width:20%;
  height:32px;
  position:relative;
  display:inline-block
}
.select_div{
  border: .5px solid lightgray;
  border-radius: 3px;
  margin-top: 5px;
  font-size: 14px;
  position: absolute;
  background-color: #fff;
  z-index: 999;
  overflow: scroll;
  width: 90%;
  cursor: pointer;
}
.select_div_div:hover{
  background-color: lightgray;
}
.select_div_div{
  padding-bottom: 2px;
  /*padding-top: 7px;*/
  padding-left: 12px;
  width: 100%;
}
</style>