<template>
  <div class="box">
    <h1>个人中心</h1>
    <el-form :model="superAdmin" label-width="160px" class="formbox">
      <el-form-item label="姓名">
        <el-input v-model="superAdmin.name" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="superAdmin.phone" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="mail.emailAddress"  @input="handleEmailInput"></el-input>
      </el-form-item>
      <el-form-item label="IMAP验证码">
        <el-input v-model="mail.imapverifyCode" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="接收(IMAP)邮件服务器">
        <el-input v-model="mail.imaphost" @input="infoChange"></el-input>
      </el-form-item>
      <el-form-item label="发送(SMTP)邮件服务器">
        <el-input v-model="mail.smtphost" @input="infoChange"></el-input>
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
      mail: {},
      pattern: /mail\.(.*?)\.edu/,
    };
  },
  mounted() {
    this.user = JSON.parse(localStorage.getItem("user"));
    this.init();
  },
  methods: {
    handleEmailInput() {
      this.infoChange()
      const email = this.mail.emailAddress
      if (email.endsWith('@163.com')) {
        this.mail.imaphost = 'imap.163.com'
        this.mail.smtphost = 'smtp.163.com'
      } else if (email.endsWith('@qq.com')) {
        this.mail.imaphost = 'imap.qq.com'
        this.mail.smtphost = 'smtp.qq.com'
      } else if (email.endsWith('@126.com')) {
        this.mail.imaphost = 'imap.126.com'
        this.mail.smtphost = 'smtp.126.com'
      } else if (email.endsWith('dhu.edu.cn')) {
        this.mail.imaphost = 'imap.dhu.edu.cn'
        this.mail.smtphost = 'smtp.dhu.edu.cn'
      }
    },
    infoChange() {
      this.infoIsChanged = true;
    },
    saveInfo() {
      this.superAdmin.email = this.mail.emailAddress;

      const url1 = '/system/admin/update';
      const adminData = this.superAdmin;

      const url2 = '/system/admin/updateMail';
      const mailData = this.mail;

      const that = this;

      let promise1 = new Promise((resolve, reject) => {
        this.postRequest(url1, adminData).then((response) => {
          if (response && response.status === 200) {
            resolve();
          } else {
            that.$message.fail('错误：更新管理员信息失败')
            reject(Error('错误：更新管理员信息失败'));
          }
        });
      });

      promise1.then(() => {
        this.postRequest(url2, mailData).then((response) => {
          if (response && response.status === 200) {
            that.$message.success(response.msg)
            that.infoIsChanged = false;
            that.user.name = this.superAdmin.name;
            sessionStorage.setItem('user', JSON.stringify(this.user));
            that.reload();
          } else {
            that.$message.fail('错误：更新邮箱地址信息失败');
          }
        });
      }).catch((error) => {
        that.$message.fail(error.message);
      });
    },
    async init() {
      try {
        const superAdminResp = await this.getRequest(`/system/admin/getSuperAdminInfo?id=${this.user.id}`);
        this.superAdmin = superAdminResp.extend.admin;

        // const mailResp = await this.getRequest("/info/mail");
        this.mail = superAdminResp.extend.mail;
      } catch (error) {
        // console.error(error);
        throw new Error("Error initializing the app");
      }
    }

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
