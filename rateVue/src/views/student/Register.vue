<template>
  <div class="box">
      <el-form class="registerContainer" :label-width="labelWidth" :rules="rules">
        <el-form-item label="姓名:">
          <el-input style="width: 60%" v-model="user.name" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="单位:">
          <el-autocomplete
              class="inline-input"
              v-model="currentInstitution"
              :fetch-suggestions="querySearch"
              :trigger-on-focus="false"
              @select="handleSelect"
              value-key="company"
          >
<!--            <template v-if="user.institutionID===0" slot-scope="{ item }">-->
<!--              <div class="default">{{ item.default }}</div>-->
<!--            </template>-->
          </el-autocomplete>
        </el-form-item>
        <el-form-item label="电话:">
          <el-input style="width: 60%" v-model="user.telephone" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="邮箱:">
          <el-input style="width: 60%" v-model="user.email" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="注册的学生类型:" v-show="user.institutionID!==''">
<!--          <span class="selectTitle"></span>-->
          <el-select v-model="selectStuType" clearable>
            <el-option
                v-for="val in stuType"
                :value="val"
                :label="val"
                :key="val">
            </el-option>
          </el-select>
        </el-form-item>
<!--        <div v-show="selectStuType == '本科生' || selectStuType == '研究生'">-->
        <div v-show="selectStuType !== '没有本校学号' && selectStuType !== null && selectStuType !== '' && selectStuType !== '不是大学生'">
          <el-form-item label="学号:">
            <el-input style="width: 60%" v-model="user.studentnumber" ></el-input>
          </el-form-item>
          <el-form-item label="入学年份:">
            <el-input style="width: 60%" v-model="user.year" :placeholder="defaultYear" ></el-input>
          </el-form-item>
        </div>
        <div v-show="selectStuType === '研究生' && user.institutionID!==''">
          <el-form-item label="研究生类型:">
            <el-select v-model="user.gradType">
              <el-option v-for="val in ['专硕','学硕']"
                         :value="val"
                         :label="val"
                         :key="val">
              </el-option>
            </el-select>
          </el-form-item>
        </div>
        <div v-show="selectStuType === '博士生' && user.institutionID!==''">
          <el-form-item label="博士生类型:">
            <el-select v-model="user.gradType">
              <el-option v-for="val in ['专博','学博']"
                         :value="val"
                         :label="val"
                         :key="val">
              </el-option>
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="用户名:">
          <el-input style="width: 60%" v-model="user.username" :disabled="usernameAndPwdIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="密码:">
          <el-input style="width: 60%" v-model="user.password" type="password" :disabled="usernameAndPwdIsDisabled"　 @blur="checkPassword"></el-input>
        </el-form-item>
        <el-form-item label="确认密码:" prop="confirmPassword">
          <el-input style="width: 60%" v-model="confirmPassword" type="password" :disabled="usernameAndPwdIsDisabled" ></el-input>
        </el-form-item>
        <el-form-item label="密保问题:">
          <el-input style="width: 60%" v-model="user.registerQuestion" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <el-form-item label="密保答案:">
          <el-input style="width: 60%" v-model="user.registerAnswer" :disabled="userInfoIsDisabled"></el-input>
        </el-form-item>
        <div class="footer">
          <el-button @click="register" type="primary">注册</el-button>
        </div>
      </el-form>
  </div>
</template>

<script>
import {debounce} from "@/utils/debounce";
import {getRequest,postRequest} from "@/utils/api";
export default {
  name: "StuRegister",
  data(){
    return{
      debounceCheckPwd:null,
      usernameAndPwdIsDisabled:false,
      userInfoIsDisabled:false,
      defaultYear:2022,
      selectStuType:'',
      confirmPassword:'',
      checkPwdState:false,
      currentInstitution:'',
      stuType:['本科生','研究生','博士生','不是大学生'],
      user:{
        name:'',
        telephone:'',
        email:'',
        idnumber:'',
        studentnumber:'',
        year:'',
        username:'',
        password:'',
        gradType:'',//专硕/学硕
        registerQuestion:'',
        registerAnswer:'',
        institutionID:'',
      },
      rules:{
        confirmPassword: [
          {
            trigger:['blur','change'],
            required:true,
            // message:'输入密码不一致!',
            validator:(rule,value,callback)=>{
              if(!this.checkPwdState) callback(new Error("输入密码不一致!"));
            }
          }
        ]
      },
      item:{
        activityCount:'',
        company:'',
        id:'',
        comment:'',
        uplimit:'',
      }
    }
  },
  mounted() {
    // this.debounceCheckPwd = debounce(this.checkPwd(),300)
  },
  computed:{
    labelWidth(){
      return `${10 * 17}px`
    }
  },
  watch:{
    confirmPassword:{
      handler(){
        //因为不想输入一个判断一次，所以只在长度一样的时候判断一次
        if(this.confirmPassword.length == this.user.password.length)this.checkPwd()
        else this.checkPwdState = false
        // this.debounceCheckPwd()
      }
    },
  },
  methods:{
    checkPassword() {
      const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[^a-zA-Z\d])[\S]{8,20}$/;

      if (!passwordRegex.test(this.user.password)) {
        this.checkPwdState = false;
        this.$message.error('密码必须是8-20位，包含至少一个英文字符，一个数字和一个特殊字符');
      } else {
        this.checkPwdState = true;
      }
    },

    //因为form自带的表单验证有问题，所以自定义验证方法
    checkPwd(){
      if(this.confirmPassword !== this.user.password) this.checkPwdState = false
      else this.checkPwdState = true
    },

    register(){
      if (this.currentInstitution == ''){
        this.$message.warning('请输入单位！')
        return
      }
      if((this.selectStuType == null || this.selectStuType == '') && this.currentInstitution !== '其他'){
        this.$message.warning('请选择注册的学生身份！')
        return
      }
      if(this.user.registerQuestion == null || this.user.registerQuestion == ''){
        this.$message.warning('请输入密保问题！')
        return
      }
      if(this.user.registerAnswer == null || this.user.registerAnswer == ''){
        this.$message.warning('请输入密保答案！')
        return
      }
      this.user.stuType = this.selectStuType
      postRequest('/registerUser/stu',this.user).then((response)=>{
        if(response){
          this.$message.success('注册成功！')
          this.$router.replace('/')
        }
      })
    },
    clearStuUser(){
      this.user = {
        name:'',
        telephone:'',
        email:'',
        idnumber:'',
        stuNumber:'',
        year:'',
        username:'',
        password:'',
        studentType:'',
        registerQuestion:'',
        registerAnswer:'',
        institutionID:''
      }
    },
    querySearch(queryString, callback) {
      let results = []
      if (queryString.length == 0){
        results.push(this.item);
        callback(results);
      }
      this.getRequest('/institution/basic/searchByName?name='+queryString)
          .then((resp) => {
            if (resp) {
              results = resp.obj;
              if (results.length === 0 ) {
                this.item.company = '其他';
                results.push(this.item);
              }// 调用 callback 返回建议列表的数据
              callback(results);
            }
          })
    },
    handleSelect(item) {
      this.user.institutionID = item.id;
      this.currentInstitution = item.company;
    }
  },
}
</script>

<style scoped>
  .footer{
    text-align: center;
  }
  .registerContainer {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 50px auto;
    width: 500px;
    padding: 15px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
</style>
