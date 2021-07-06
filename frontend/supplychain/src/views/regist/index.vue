<template>
  <div class="register-container">
    <header class="top">
      <div class="content">
        <div class="logo">基于区块链的供应链管理结算平台</div>
        <router-link to="/">返回首页</router-link>
      </div>
    </header>
    <div class="brand">欢迎注册帐号</div>
    <div class="register-form">
      <div class="login-link">
        已有账号？<router-link to="/login">去登录</router-link>
      </div>
      <el-form
        ref="registerForm"
        :model="registerForm"
        :rules="registerRules"
        class="login-form"
        auto-complete="on"
        label-position="left"
      >
        <el-form-item prop="username">
          <el-input
            ref="username"
            v-model="registerForm.username"
            placeholder="用户名"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>
        <el-form-item prop="orgName">
          <el-input
            ref="orgName"
            v-model="registerForm.orgName"
            placeholder="组织名称"
            name="orgName"
            type="text"
            tabindex="1"
          />
        </el-form-item>
        <el-form-item prop="orgDescription">
          <el-input
            ref="orgDescription"
            v-model="registerForm.orgDescription"
            placeholder="组织描述"
            name="orgDescription"
            type="text"
            tabindex="1"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            :key="passwordType"
            ref="password"
            v-model="registerForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            auto-complete="on"
          />
        </el-form-item>

        <el-button
          :loading="loading"
          type="primary"
          style="width:100%;margin-bottom:30px;"
          @click.native.prevent="handleRegister('registerForm')"
          >注册</el-button
        >
      </el-form>
    </div>
    <div class="copyright">
      CopyRight © 2021 上海久誉软件系统有限公司
    </div>
  </div>
</template>

<script>
import { register } from "@/api/user";

export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error("密码至少6位"));
      } else {
        callback();
      }
    };
    return {
      registerForm: {
        username: "",
        password: "",
        orgName: "",
        orgDescription: ""
      },
      loading: false,
      passwordType: "password",
      registerRules: {
        username: [{ required: true, trigger: "blur" }],
        password: [
          { required: true, trigger: "blur", validator: validatePassword }
        ]
      }
    };
  },
  methods: {
    handleRegister(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          const registerParams = this.registerForm;
          register(registerParams)
            .then(res => {
              if (res.code === 0) {
                this.$confirm("注册成功", "注册成功", {
                  confirmButtonText: "去登录",
                  type: "success",
                  showCancelButton: false,
                  closeOnClickModal: false
                }).then(() => {
                  this.$router.push({ path: "/login" });
                });
              }
            })
            .catch(function(error) {
              console.log(error);
            });
        }
      });
    },
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.register-container {
  .login-link {
    padding: 0 35px 35px;
    text-align: right;
    font-size: 12px;
    color: #666;
    a {
      color: #09aaff;
    }
  }
  .top {
    position: absolute;
    display: block;
    top: 0;
    left: 0;
    width: 100%;
    height: 60px;
    background-color: rgba(104, 104, 104, 0.15);
    z-index: 1000;
    color: #fff;
    .content {
      width: 980px;
      margin: 0 auto;
      padding-top: 12px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
  }
  .register-form {
    position: relative;
    z-index: 2000;
    width: 705px;
    font-size: 14px;
    margin: 0 auto;
    top: -90px;
    padding: 65px 20px 20px;
    background: #fff;
    box-shadow: 0 4px 5px 0 rgb(0 0 0 / 9%);
    .el-form {
      width: 420px;
      margin: 0 auto;
    }
  }
  .brand {
    width: 100%;
    height: 400px;
    line-height: 400px;
    color: #fff;
    font-size: 32px;
    text-align: center;
    background: url("../../assets/images/regist-bg.jpg") no-repeat top center;
    background-size: cover;
  }
}
.copyright {
  padding: 20px 0;
  border-top: 1px solid #efefef;
  font-size: 12px;
  text-align: center;
  color: #454a5b;
}
</style>
