<template>
  <div>
    <div class="Login" v-if="isShow">
      <!--  左边侧边栏欢迎界面  -->
      <div class="login-right">
        <div>
          <p class="small">Welcome to</p>
          <p class="big">Yy 聊天室</p>
        </div>
      </div>

      <!--   右边主体内容   -->
      <div class="login-left">
        <div class="content">
          <!--  用户名密码输入框  -->
          <div>
            <div>
              <label for="username" class="iconfont icon-ziyuanxhdpi">
                用户名</label
              >
              <input type="text" class="user" id="username" ref="inputUsername" v-model="form.username" style="margin-top:10px"  />
            </div>
            <div style="margin-top:10px">
              <label for="password" class="iconfont icon-ziyuanxhdpi" >
                密码</label
              >
              <input type="password" class="user" id="password" ref="inputPassword" v-model="form.password" style="margin-top:10px" />
            </div>
          </div>

           <!-- 底部登录注册按钮 -->
          <div>
            <button class="button" @click="login">登录</button>
            <a href="#" @click = "toRegister">点击注册</a>
          </div>
        </div>
      </div>
    </div>

    <!--  聊天室组件  -->
    <room
      v-else
      :user="user"
      :userList="userList"
      ref="chatroom"
      @sendServer="sendServer"
      @sendUser="sendUser"
      @webrtc="webrtc"
      @addLocalStream="addLocalStream"
      :message="message"
      :oneMessage="oneMessage"
      :remoteVideoStream="remoteVideoStream"
      @handleFile="handleFile"
    />
  </div>
</template>

<script>
import Room from "./Room"
import axios from "axios"
export default {
  name: "Login",
  components: { Room },
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
      socket: null,
      isShow: true,
      user: {},
      userList: [],
      message: {},
      oneMessage: {},
      ws: {},
      pc: [],
      localStream: {},
      remoteVideoStream: {},
      form: {
        username: "",
        password: ""
      }
    };
  },

  created() {
    if (this.$route.params.username && this.$route.params.password) {
      this.form.username = this.$route.params.username
      this.form.password = this.$route.params.password
    }
  },

  methods: {
    // login
    login() {
      axios({
        method: "get",
        url: "http://localhost:8090/login",
        params: this.form
      }).then(res => {
        const { data } = res
        console.log(data)
        if (data.code === 20000) {
          this.init()
        }else {
          alert(data.data.data)
        }
      })
    },

    init() {
      // this.ws = new WebSocket("ws://127.0.0.1:7886");
      this.ws = new WebSocket("ws://127.0.0.1:8888");
      this.ws.onopen = () => {
        console.log("连接成功")
        this.loginRoom()
      };

      this.ws.onerror = function (err) {
        console.log(err);
        console.log("连接错误");
      };

      // 接收到消息
      this.ws.onmessage = (event) => {
        const data = event.data;
        if (typeof data !== "string") return;
        let body = JSON.parse(data);

        // LOGIN_SUCCEED
        if (body.type === 11) {
          alert("登录成功")
          this.isShow = false;
          this.user = JSON.parse(body.content);
        }

        // ADD_USER
        if (body.type === 22) {
          this.$store.commit("setJoinRoom", JSON.parse(body.content));
        }

        // USER_LIST
        if (body.type === 21) {
          this.userList = JSON.parse(body.content);
          console.log("用户列表： ", this.userList);
        }

        // sendMessage
        if (body.type === 5) {
          this.message = JSON.parse(body.content);
        }

        // 私聊信息
        if (body.type === 4) {
          console.log("接收到私聊信息： ", body);
          this.oneMessage = body;
        }

        // sendImage
        if (body.type === 8) {
          this.$refs.chatroom.handleImage(JSON.parse(body.content));
        }

        // liverRoom
        if (body.type === 23) {
          console.log("用户离开： ", JSON.parse(body.content));
          this.userList = this.userList.filter(
            (value) => value.username !== JSON.parse(body.content).username
          );
          this.$store.commit("setLeaveRoom", JSON.parse(body.content));
          this.$refs.chatroom ? this.$refs.chatroom.haveOneleaveRoom() : null;
        }

        // sdp
        if (body.type === 24) {
          body.content = JSON.parse(body.content);
          body.from = JSON.parse(body.from);
          body.to = JSON.parse(body.to);

          if (body.content && body.content.type === "offer") {
            this.StartCall(body.from, false);

            const desc = new RTCSessionDescription(body.content);
            this.pc[body.from].setRemoteDescription(desc);

            let myanswer;
            this.pc[body.from]
              .createAnswer()
              .then((answer) => {
                myanswer = answer;
                this.pc[body.from].setLocalDescription(answer);
              })
              .then(() => {
                const message = JSON.stringify({
                  type: 24,
                  content: JSON.stringify(myanswer),
                  from: JSON.stringify(this.user),
                  to: JSON.stringify(body.from),
                });
                this.ws.send(message);
              });
            console.log("收到offer");
          } else if (body.content && body.content.type === "answer") {
            const answer = new RTCSessionDescription(body.content);
            console.log("setRemoteDescription: ", answer);
            this.pc[body.from].setRemoteDescription(answer);
          }
        }

        // ice
        if (body.type === 25) {
          body.from = JSON.parse(body.from);
          body.content = JSON.parse(body.content);
          console.log("body.from: ", body.from);
          console.log("this.pc[body.from]: ", this.pc[body.from]);

          if (body.content) {
            console.log("ice,,,", body.content);
            const candidate = new RTCIceCandidate(body.content);
            this.pc[body.from].addIceCandidate(candidate);
          }
        }
      };
    },

    // 视频通话
    webrtc(to) {
      console.log("获取到的localStream: ", this.localStream);
      this.StartCall(to, true);
    },

    // addLocalStream
    addLocalStream(stream) {
      this.localStream = stream;
      console.log(this.localStream, ".....");
    },

    StartCall(parterName, createOffer) {
      this.pc[parterName] = new RTCPeerConnection();

      if (this.localStream) {
        this.localStream.getTracks().forEach((track) => {
          this.pc[parterName].addTrack(track, this.localStream); // should trigger negotiationneeded event
        });
      }

      if (createOffer) {
        let myoffer;
        this.pc[parterName]
          .createOffer()
          .then((offer) => {
            console.log("this.pc[parterName]: ", this.pc[parterName]);
            myoffer = offer;
            console.log("offer", myoffer);
            this.pc[parterName].setLocalDescription(offer);
          })
          .then(() => {
            const messgae = JSON.stringify({
              type: 24,
              content: JSON.stringify(myoffer),
              from: JSON.stringify(this.user),
              to: JSON.stringify(parterName),
            });
            console.log("发送offer.....", myoffer);
            this.ws.send(messgae);
          });
      }

      this.pc[parterName].onicecandidate = ({ candidate }) => {
        const message = JSON.stringify({
          type: 25,
          content: JSON.stringify(candidate),
          from: JSON.stringify(this.user),
          to: JSON.stringify(parterName),
        });
        console.log("发送ice.....", candidate);
        this.ws.send(message);
      };

      this.pc[parterName].ontrack = (ev) => {
        let str = ev.streams[0];
        // this.$refs.remoteVideo.srcObject = str
        // this.remoteVideoStream = str
        console.log("success！！！！", str);
        // this.$refs.chatroom.$refs.remoteVideo.srcObject = str
        this.$refs.chatroom.addRemoteVideoStream(str);
      };
    },

    handleFile(file) {
      // this.socket.emit('sendImage', { ...this.user, file })
      const data = {
        type: 8,
        content: {
          username: this.user.username,
          avatar: this.user.avatar,
          file,
        },
      };
      console.log(file);
      this.ws.send(JSON.stringify(data));
    },

    clickImg(index, item) {
      this.currentIndex = index;
      this.currentImg = item;
    },

    loginRoom() {
      // 1.获取用户名
      const username = this.$refs.inputUsername.value;
      if (!username.trim()) {
        alert("请输入用户名");
        return;
      }
      const data = {
        type: 1,
        content: { username, avatar: this.currentImg },
      };
      const json = JSON.stringify(data);
      this.ws.send(json);
    },

    sendServer(content) {
      const { username, avatar } = this.user;
      const data = {
        type: 5,
        content: {
          content,
          username,
          avatar,
        },
      };
      console.log("发送消息: ", data);
      this.ws.send(JSON.stringify(data));
    },

    // 私聊  from: user{} to: user{}
    sendUser(to, content) {
      const data = {
        type: 4,
        from: this.user,
        to,
        content,
      };
      console.log("发送私聊消息: ", data);
      this.ws.send(JSON.stringify(data));
    },

    toRegister() {
      this.$router.push({ name: "Register" })
    }

  },
};
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
    width: 400px;
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
      margin-left: 23%;
      margin-top: 30px;
      margin-right: 10px;
    }
  }
}
</style>
