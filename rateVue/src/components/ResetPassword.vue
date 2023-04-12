<template>
  <div>
    <el-form class="resetPassContainer" :label-width="labelWidth">
      <el-form-item label="请输入密保问题:">
        <el-input style="width: 60%"  ></el-input>
      </el-form-item>
      <el-form-item label="请输入密保答案:">
        <el-input style="width: 60%" ></el-input>
      </el-form-item>
      <el-form-item label="请输入新密码:">
        <el-input style="width: 60%" ></el-input>
      </el-form-item>
      <el-form-item label="请确认新密码:">
        <el-input style="width: 60%" ></el-input>
      </el-form-item>
      <div class="footer">
        <el-button @click="register" type="primary">确认</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "ResetPassword",
  data(){
    return{

    }
  },
  computed:{
    labelWidth(){
      return `${10 * 17}px`
    }
  },
}
</script>

<style scoped>
.footer{
  text-align: center;
}
.resetPassContainer {
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