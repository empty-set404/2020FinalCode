<template>
  <div>
    <div class="Login">
      <div class="login-right">
        <div>
          <p class="small">Welcome to</p>
          <p class="big">Z z 聊天室</p>
        </div>
      </div>

      <div class="login-left">
        <div class="content">
          <div>
            <label for="username" class="iconfont icon-ziyuanxhdpi" >
              用户名</label
            >
            <input type="text" class="user" id="username" ref="inputUsername" v-model="form.username" style="margin-top:5px" />
          </div>
          <div style="margin-top:10px">
            <label for="password" class="iconfont icon-ziyuanxhdpi" >
              密码</label
            >
            <input type="password" class="user" id="password" ref="inputPassword"  v-model="form.password" style="margin-top:5px" />
          </div>

          <!--    选择头像      -->
          <div class="chooseAvatar">
            <label for="avatar" class="iconfont icon-icon26"> 选择头像</label>
            <ul class="avatarWrap">
              <li v-for="(item, index) in imgUrl" :key="item">
                <img
                    :src="require('../assets/avatar/' + item)"
                    alt
                    @click="clickImg(index, item)"
                    :class="{ active: currentIndex === index ? true : false }"
                />
              </li>
            </ul>
          </div>

          <button class="button" @click="register">注册</button>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Register",
  data() {
    return {
      imgUrl: [
        "one.jpg",
        "two.jpg",
        "four.jpg",
        "three.jpeg",
        "eight.jpg",
        "seven.jpg",
        "six.jpg",
        "five.jpg",
        "nine.jpg",
        "ten.jpeg",
      ],
      currentIndex: 0,
      currentImg: "one.jpg",
      form: {
        username: "",
        password: ""
      }
    }
  },
  methods: {
    register() {
      axios({
        method: "get",
        url: "http://localhost:9874/register",
        params: {
          ...this.form,
          avatar: this.currentImg
        }
      }).then(res => {
        const { data } = res
        if (data.code === 20000) {
          alert("注册成功")
          this.$router.push({name: "Login", params: {...this.form, avatar: this.currentImg}})
        }else {
          alert(data.data.data)
        }
      })
    },
    clickImg(index, item) {
      this.currentIndex = index;
      this.currentImg = item;
    },
  }
}
</script>

<style lang="less" scoped>
.Login {
  width: 600px;
  height: 360px;
  margin: 130px auto;
  display: flex;
  .login-right {
    width: 260px;
    height: 100%;
    background-color: rgba(66, 69, 120, 0.76);
    display: flex;
    justify-content: center;
    align-items: center;
    .small {
      color: #f1e9e9;
      font-size: 14px;
      font-family: sans-serif;
    }
    .big {
      font-size: 20px;
      font-weight: 600;
      margin-top: 5px;
      color: #f1e9e9;
      font-family: sans-serif;
    }
  }
  .login-left {
    width: 500px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #fff;
    .icon-ziyuanxhdpi,
    .icon-icon26 {
      color: #353c73;
    }
    label {
      color: #000;
      font-size: 14px;
    }
    .content {
      margin: 20px auto;
      width: 90%;
      .user {
        width: 95%;
        border: 1px solid #ccc;
        font-size: 14px;
        line-height: 30px;
        padding-left: 10px;
        display: block;
      }
      .chooseAvatar {
        margin-top: 15px;
      }

      .avatarWrap {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        align-items: center;
        border: 1px solid #ccc;
        li {
          cursor: pointer;
          width: 50px;
          height: 50px;
          padding: 7px;
          img {
            width: 50px;
            height: 50px;
          }
          .active {
            border: 3px solid #2980b9;
          }
        }
      }
    }
    .button {
      width: 100px;
      line-height: 30px;
      background-color: #705a76;
      color: #fff;
      border-radius: 10px;
      margin-left: 34%;
      margin-top: 30px;
    }
  }
}
</style>
