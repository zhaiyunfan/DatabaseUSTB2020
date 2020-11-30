<!--<template>-->
<!--  <div>-->
<!--    -->
<!--    <input type="text" v-model="loginForm.username" placeholder="用户名"/>-->
<!--    <input type="text" v-model="loginForm.password" placeholder="密码"/>-->
<!--    <button @click="login">登录</button>-->
<!--  </div>-->
<!--</template>-->
<template>
  <div>
    <!--flex弹性盒子模型，justify-content：主轴 -->
    <div style="display: flex;justify-content: center;margin-top: 150px">
      <el-card style="width: 400px">
        <div slot="header" class="clearfix">
          <span>登录</span>
        </div>
        <table>
          <tr>
            <td>用户名</td>
            <td>
              <el-input v-model="loginForm.username" placeholder="若是新用户名，则自动注册"></el-input>
            </td>
          </tr>
          <tr>
            <td>密码</td>
            <td>
              <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"
                        @keydown.enter.native="login"></el-input>
              <!-- @keydown.enter.native="login"当按下enter键的时候也会执行login方法-->
            </td>
          </tr>
          <tr>
            <!-- 占两行-->
            <td colspan="2">
              <!-- 点击事件的两种不同的写法v-on:click和 @click-->
              <!--<el-button style="width: 300px" type="primary" v-on:click="login">登录</el-button>-->
              <el-button style="width: 300px" type="primary" @click="login">登录</el-button>
            </td>
          </tr>
        </table>
      </el-card>
    </div>
  </div>
</template>

<script>
import {mapMutations} from 'vuex';

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    };
  },

  methods: {
    ...mapMutations(['changeLogin']),
    login() {
      let _this = this;
      if (this.loginForm.username === '' || this.loginForm.password === '') {
        alert('账号或密码不能为空');
      } else {
        this.axios({
          method: 'post',
          url: '/user/login',
          data: _this.loginForm
        }).then(res => {
          console.log(res.data);
          _this.userToken = 'Bearer ' + res.data.token;
          console.log(_this.userToken);
          // 将用户token保存到vuex中
          _this.changeLogin({Authorization: _this.userToken});
          _this.$router.push('/home');
          alert('登陆成功');
        }).catch(error => {
          alert('账号或密码错误');
          console.log(error);
        });
      }
    }
  }
};
</script>