<template>
  <div class="box">
    <h1>个人中心</h1>
    <el-form :model="superAdmin" label-width="100px" class="formbox">
      <el-form-item label="姓名">
        <el-input v-model="superAdmin.name" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="superAdmin.phone" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="superAdmin.email" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="IMAP验证码">
        <el-input v-model="superAdmin.comment" @input="infoChange"></el-input>
      </el-form-item>

      <div class="footer">
        <el-button @click="saveInfo" type="primary" :disabled="!infoIsChanged"
          >保存</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "superAdminInfo",
  inject: ["reload"],
  data() {
    return {
      infoIsChanged: false,
      user: {},
      superAdmin: {},
    };
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem("user"));
    this.initSuperAdmin();
  },
  methods: {
    infoChange() {
      this.infoIsChanged = true;
    },
    saveInfo() {
      let url = "/system/admin/update";
      this.postRequest(url, this.superAdmin).then((response) => {
        if (response.status == 200) {
          this.$message.success(response.msg);
          this.infoIsChanged = false;
          this.user.name = this.superAdmin.name;
          localStorage.setItem("user", JSON.stringify(this.user));
          this.reload();
        } else {
          this.$message.fail("更新失败");
        }
      });
    },
    initSuperAdmin() {
      this.getRequest("/info/admin?id=" + this.user.id).then((resp) => {
        if (resp) {
          if (resp.status == 200) this.superAdmin = resp.obj;
        }
      });
    },
  },
};
</script>

<style scoped>
.formbox {
  margin: 30px auto;
  width: 60%;
}
.box {
  overflow: scroll;
  text-align: center;
  width: 100%;
  height: 100%;
  background-color: white;
}
</style>
