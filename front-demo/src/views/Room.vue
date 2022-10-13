<template>
  <div class="Room" ref="room">

    <!--  webrtc  -->
    <div id="videos" class="videos" v-if="webrtcFlag">
      <div class="remote-main">
        <video ref="remoteVideo" id="remoteVideo" class="remote-video" controls autoplay></video>
      </div>
      <div class="local-main">
        <!-- muted静音   playsinline-->
        <video muted class="video local" ref="localVideo" autoplay playsinline webkit-playsinline
               x5-video-player-type="h5" x5-video-player-fullscreen="true" x5-video-orientation="portraint"></video>
      </div>
    </div>

    <div class="room-left" v-show="user">
      <img :src="require('@/assets/avatar/' + user.avatar)" alt />
      <p
        v-for="(item, index) in iconList"
        :key="item"
        :class="[item, { active: iconCurrentIndex === index }]"
        class="iconfont"
        @click="tabsChange(index)"
      ></p>
    </div>

    <div class="room-center">
      <div class="center-h">
        <p>当前用户列表</p>
        <p />
      </div>
      <div class="center-b">
        <ul>
          <li class="user-item" v-for="item in userList" :key="item.username" @click = "toUser(item)">
            <img :src="require('@/assets/avatar/' + item.avatar)" alt />
            <span>{{ item.username }}</span>
          </li>
        </ul>
      </div>
    </div>

    <!--  群聊  -->
    <div class="room-right" v-if="iconCurrentIndex === 0">
      <p class="name">聊天室({{ userListLength }})</p>
      <div class="chatcontent">
        <ul class="join" ref="joinUs">
          <li v-for="item in joinRoom" :key="item.username">
            欢迎{{ item.username }}加入群聊
          </li>
          <li
            v-for="(item1, index) in messageContent"
            :key="index"
            :class="{
              'my-message': item1.type === 1 ? true : false,
              'other-message': item1.type === 2 ? true : false,
            }"
          >
            <div v-if="item1.type === 3">{{ item1.username }}离开了群聊</div>
            <div v-if="item1.type === 1">
              <img
                :src="item1.file"
                alt
                class="file"
                v-if="item1.file"
                @load="loadOverImg"
                preview="1"
              />
              <span v-else>{{ item1.content }}</span>
              <img
                :src="require('@/assets/avatar/' + item1.avatar)"
                class="avatar"
              />
            </div>

            <!-- 2 表示别人发的信息  -->
            <div v-if="item1.type === 2">
              <img
                :src="require('@/assets/avatar/' + item1.avatar)"
                alt
                class="avatar"
              />
              <p class="username">{{ item1.username }}</p>

              <!--  如果发送的时图片就是展示图片，否则以文本的形式显示  -->
              <img
                :src="item1.file"
                alt
                class="file"
                v-if="item1.file"
                @load="loadOverImg"
                preview="1"
              />
              <p class="content" v-else>{{ item1.content }}</p>
            </div>
          </li>
        </ul>
      </div>
      <div class="sendMessage">
        <div class="icon">
          <span
            class="iconfont icon-smile"
            @click="emojiShow = !emojiShow"
          ></span>
          <div
            class="emoji"
            tabindex="1"
            v-show="emojiShow"
            @blur="handleEmojiBlur"
          >
            <span
              v-for="item in emojiList"
              :key="item.codes"
              @click="handleEmoji(item)"
              >{{ item.char }}</span
            >
          </div>
          <label class="iconfont icon-wenjianjia" for="file"></label>
          <input
            type="file"
            style="display: none"
            id="file"
            @change="handleFile"
          />
          <span class="iconfont icon-jietu" @click="handleCanvas"></span>
        </div>
        <textarea
          cols="80"
          rows="5"
          ref="textarea"
          @keyup.enter="handlePress"
        ></textarea>
        <button class="sendMessage" @click="sendContentToServe">发送</button>
        <img :src="imgUrl" alt />
      </div>
    </div>

    <!--  单聊  -->
    <div class="room-right" v-else>
      <p class="name">{{ currentUser.username }}</p>
      <div class="chatcontent">
        <ul class="join" ref="joinUs">
          <li
              v-for="(item1, index) in currentMessages"
              :key="index"
              :class="{
              'my-message': item1.type === 1 ? true : false,
              'other-message': item1.type === 2 ? true : false,
            }"
          >
            <div v-if="item1.type === 1">
              <img
                  :src="item1.file"
                  alt
                  class="file"
                  v-if="item1.file"
                  @load="loadOverImg"
                  preview="1"
              />
              <span v-else>{{ item1.content }}</span>
              <img
                  :src="require('@/assets/avatar/' + item1.avatar)"
                  class="avatar"
              />
            </div>
            <div v-if="item1.type === 2">
              <img
                  :src="require('@/assets/avatar/' + item1.avatar)"
                  alt
                  class="avatar"
              />
              <p class="username">{{ item1.username }}</p>
              <img
                  :src="item1.file"
                  alt
                  class="file"
                  v-if="item1.file"
                  @load="loadOverImg"
                  preview="1"
              />
              <p class="content" v-else>{{ item1.content }}</p>
            </div>
          </li>
        </ul>
      </div>
      <div class="sendMessage">
        <div class="icon">
          <span
              class="iconfont icon-smile"
              @click="emojiShow = !emojiShow"
          ></span>
          <div
              class="emoji"
              tabindex="1"
              v-show="emojiShow"
              @blur="handleEmojiBlur"
          >
            <span
                v-for="item in emojiList"
                :key="item.codes"
                @click="handleEmoji(item)"
            >{{ item.char }}</span
            >
          </div>
          <label class="iconfont icon-wenjianjia" for="file"></label>
          <input
              type="file"
              style="display: none"
              id="file"
              @change="handleFile"
          />
          <span class="iconfont icon-jietu" @click="handleCanvas2"></span>
          <span
              class="iconfont icon-jietu"
              @click="webrtc"
          ></span>
        </div>
        <textarea
            cols="80"
            rows="5"
            ref="textarea"
            @keyup.enter="handlePress2"
        ></textarea>
        <button class="sendMessage" @click="sendContentToUser">发送(单聊)</button>
        <img :src="imgUrl" alt />
      </div>
    </div>

  </div>
</template>

<script>
import html2canvas from "html2canvas";
import emoji from "../assets/emoji.json";
export default {
  name: "Room",
  props: {
    user: Object,
    userList: Array,
    message: Object,
    oneMessage: Object,
    remoteVideoStream: Object
  },
  computed: {
    userListLength() {
      return this.userList.length;
    },
  },
  data() {
    return {
      currentUser: {},
      iconList: ["icon-liaotianqingqiu", "icon-yonghu"],
      iconCurrentIndex: 0,
      joinRoom: [],
      messageContent: [], //0不渲染，1表示自己发出的信息，2表示别人发出的信息
      emojiList: [],
      content: "",
      emojiShow: false,
      imgUrl: "",
      imgAllUrl: [],
      messageList: [],  //0不渲染，1表示自己发出的信息，2表示别人发出的信息
      currentMessages: [],
      localStream: {},
      webrtcFlag: false
    };
  },
  methods: {
    // 视频通话
    webrtc() {
      this.webrtcFlag = true
      this.$emit("webrtc", this.currentUser);
    },

    addRemoteVideoStream(str) {
      console.log("str~!@@@", str)
      this.$refs.remoteVideo.srcObject = str
    },

    getUserMedia(constrains, success, error) {
      if (navigator.mediaDevices.getUserMedia) {
        //最新标准API
        navigator.mediaDevices.getUserMedia(constrains).then(success).catch(error);
      } else if (navigator.webkitGetUserMedia) {
        //webkit内核浏览器
        navigator.webkitGetUserMedia(constrains).then(success).catch(error);
      } else if (navigator.mozGetUserMedia) {
        //Firefox浏览器
        navigator.mozGetUserMedia(constrains).then(success).catch(error);
      } else if (navigator.getUserMedia) {
        //旧版API
        navigator.getUserMedia(constrains).then(success).catch(error);
      }
    },

    canGetUserMediaUse() {
      return !!(navigator.mediaDevices.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia);
    },

    InitCamera() {
      if (this.canGetUserMediaUse()) {
        this.getUserMedia({
          video: true,
          audio: false
        }, (stream) => {
          this.localStream = stream
          this.$refs.localVideo.srcObject = stream
          this.$emit("addLocalStream", this.localStream)
        }, (err) => {
          console.log('访问用户媒体失败: ', err.name, err.message);
        })
      } else {
        alert('您的浏览器不兼容');
      }
    },


    tabsChange(index) {
      this.iconCurrentIndex = index
      // this.currentUser = null
    },

    toUser(user) {
      this.currentUser = user
      this.iconCurrentIndex = 1
      this.currentMessages = this.messageList.filter(value => {
        return (value.username === this.user.username && value.to.username === this.currentUser.username)
            || (value.username === this.currentUser.username && value.to.username === this.user.username)
      })
    },

    handlePress() {
      this.sendContentToServe();
    },

    handlePress2() {
      this.sendContentToUser()
    },

    handleCanvas() {
      const room = this.$refs.room;
      html2canvas(room).then((canvas) => {
        const imgUrl = canvas.toDataURL();
        this.$emit("handleFile", imgUrl);
      });
    },

    handleCanvas2() {
      const room = this.$refs.room;
      html2canvas(room).then((canvas) => {
        const imgUrl = canvas.toDataURL();
        this.$emit("handleFile", imgUrl);
      });
    },

    handleEmojiBlur() {
      this.emojiShow = false;
    },
    handleEmoji(emoji) {
      this.content = this.$refs.textarea.value;
      this.$refs.textarea.value += emoji.char;
    },
    loadOverImg() {
      this.$previewRefresh();
      this.handleScrollBottom();
    },
    handleImage(file) {
      this.handleMessageBox(file);
    },

    // 将文件对象发送给对方 （实际上图片对应的就是base64）
    handleFile(e) {
      const file = e.target.files[0];
      const reader = new FileReader(); // 创建读取文件对象
      reader.readAsDataURL(file); // 发起异步请求，读取文件
      reader.onload = (e) => {
        // 文件读取完成后
        // 读取完成后，将结果赋值给img的src
        this.$emit("handleFile", e.target.result);
      };
    },

    haveOneleaveRoom() {
      const leaveRoom = this.$store.state.leaveRoom;
      this.messageContent.push({ ...leaveRoom, type: 3 });
    },

    sendContentToServe() {
      // 获取到聊天的内容
      this.content = this.$refs.textarea.value;
      this.$refs.textarea.value = "";
      if (!this.content) {
        return alert("请输入内容");
      }
      //发送给服务器
      this.$emit("sendServer", this.content);
    },

    // 私聊
    sendContentToUser() {
      // 获取到聊天的内容
      this.content = this.$refs.textarea.value;
      this.$refs.textarea.value = "";
      if (!this.content) {
        return alert("请输入内容");
      }
      //发送给服务器
      this.$emit("sendUser", JSON.stringify(this.currentUser), this.content)
    },

    handleMessageBox(newValue) {
      if (newValue.username === this.user.username) {
        //是自己发的信息
        this.messageContent.push({ ...newValue, type: 1 })
      } else {
        //是别人发的信息
        this.messageContent.push({ ...newValue, type: 2 })
      }
    },

    // 私聊
    handleMessageBox2(newValue) {
      const from = JSON.parse(newValue.from)
      const to = JSON.parse(newValue.to)
      const content = newValue.content
      const data = {...from, content, to: to}
      console.log("私聊data: ", data)

      if (from.username === this.user.username) {
        //是自己发的信息
        this.messageList.push({ ...data, type: 1 })
        this.currentMessages.push({ ...data, type: 1 })
      }else {
        //是别人发的信息
        this.messageList.push({ ...data, type: 2 })
        this.currentMessages.push({ ...data, type: 2 })
      }

      console.log("message: ", this.messageList, this.currentMessages)
    },

    handleScrollBottom() {
      const ul = this.$refs.joinUs;
      ul.scrollTop = ul.scrollHeight;
    },
  },
  mounted() {
    this.joinRoom = this.$store.state.joinRoom
    this.emojiList = emoji
    this.InitCamera()
  },
  watch: {
    message(newValue) {
      this.handleMessageBox(newValue);
    },
    oneMessage(newValue) {
      this.handleMessageBox2(newValue);
    },
    // remoteVideoStream(remoteVideoStream) {
    //   console.log(remoteVideoStream, "----------")
    //   this.$refs.remoteVideo.srcObject = remoteVideoStream
    // }
  },

  updated() {
    // 聊天定位到底部
    this.handleScrollBottom();
  },
};
</script>

<style lang="less" scoped>
.Room {
  width: 1050px;
  height: 500px;
  margin: 30px 50px;
  display: flex;

  .videos {
    .remote-video {
      width: 200px;
      margin-right: 10px;
    }

    .video {
      width: 200px;
      margin-right: 10px;
    }
  }

  .room-left {
    background-color: #474574;
    width: 50px;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 10px;
    .icon-liaotianqingqiu,
    .icon-yonghu {
      font-size: 24px;
      padding-top: 10px;
      cursor: pointer;
    }
    .active {
      color: #ecf0f1;
    }
    img {
      width: 40px;
    }
  }
  .room-center {
    width: 200px;
    background-color: #e6e5e5;
    color: #000;
    display: flex;
    flex-direction: column;
    .center-h {
      padding: 9.6px 10px;
      height: 20px;
      border-bottom: 1px solid #e5e5e58c;
      box-shadow: 1px 1px 1px #b2c0c9;
      display: flex;
      align-items: center;
      img {
        width: 50px;
        height: 50px;
      }
    }
    .center-b {
      flex: 1;
      .user-item-name {
        padding: 5px 10px;
        height: 40px;
        display: flex;
        align-items: center;
        border-bottom: 1px solid #e5e5e58c;
        box-shadow: 1px 1px 1px #2980b9;
        font-size: 17px;
      }
      .user-item {
        padding: 5px 10px;
        height: 40px;
        display: flex;
        align-items: center;
        img {
          width: 40px;
          height: 40px;
        }
        span {
          margin-left: 5px;
        }
      }

      .user-item:hover {
        opacity: .9;
        text-decoration-line: underline;
        cursor: pointer;
        background: rgba(230, 229, 229, 0.8);
      }

    }
  }
  .room-right {
    flex: 1;
    background-color: #f6f6f6;
    display: flex;
    flex-direction: column;
    .name {
      display: flex;
      justify-content: center;
      line-height: 40px;
      font-size: 16px;
      border-bottom: 1px solid rgba(100, 100, 100, 0.3);
    }
    .chatcontent {
      height: 300px;
      .join {
        text-align: center;
        color: #ccc;
        overflow: auto;
        height: 300px;
        li {
          padding: 10px;
        }
      }
      .my-message {
        display: flex;
        justify-content: flex-end;
        div {
          display: flex;
          position: relative;
          &::after {
            content: "";
            right: 38px;
            top: 50%;
            transform: translateY(-50%);
            position: absolute;
            border-left: 6px solid #9eea6a;
            border-top: 6px solid transparent;
            border-bottom: 6px solid transparent;
            border-right: 6px solid transparent;
          }
          .file {
            max-width: 330px;
            max-height: 170px;
            margin-right: 10px;
            cursor: pointer;
          }
          .avatar {
            width: 40px;
            height: 40px;
          }
          span {
            box-sizing: border-box;
            display: inline-block;
            border-radius: 5px;
            line-height: 32px;
            background-color: #9eea6a;
            color: #000;
            padding: 5px;
            margin-right: 10px;
            min-width: 40px;
          }
        }
      }
      .other-message {
        position: relative;
        display: flex;
        justify-content: flex-start;
        div {
          display: flex;
          position: relative;

          &::before {
            content: "";
            left: 40px;
            top: 55%;
            transform: translateY(-50%);
            position: absolute;
            border-left: 5px solid transparent;
            border-top: 5px solid transparent;
            border-bottom: 5px solid transparent;
            border-right: 5px solid #ccc;
          }
          .file {
            max-width: 330px;
            max-height: 170px;
            margin-top: 14px;
            margin-left: 10px;
            cursor: pointer;
          }
          .avatar {
            width: 40px;
            height: 40px;
          }
          .username {
            position: absolute;
            left: 45px;
            top: -5px;
            font-size: 13px;
            color: #b2b2b2;
          }
          .content {
            margin-top: 12px;
            box-sizing: border-box;
            display: inline-block;
            border-radius: 5px;
            line-height: 32px;
            background-color: #fff;
            color: #000;
            padding: 5px;
            margin-left: 10px;
            border: 1px solid #ccc;
            min-width: 40px;
          }
        }
      }
    }
    .sendMessage {
      position: relative;
      flex: 1;
      background-color: #fff;
      padding-left: 10px;
      .icon {
        height: 25px;
        padding-top: 6px;
        display: flex;
        .icon-smile,
        .icon-wenjianjia,
        .icon-jietu {
          font-size: 20px;
          padding: 0 6px;
          cursor: pointer;
        }
        .icon-jietu {
          font-size: 23px !important;
        }
        .emoji {
          position: absolute;
          display: flex;
          flex-wrap: wrap;
          width: 276px;
          height: 218px;
          overflow: auto;
          bottom: 159px;
          background-color: #fff;
          border: 1px solid #cccccc;
          outline: none;
          span {
            padding: 7px;
            cursor: pointer;
          }
        }
      }
      textarea {
        border: none;
        resize: none;
        outline: none;
        font-size: 15px;
        padding-left: 9px;
      }
      .sendMessage {
        position: absolute;
        bottom: 10px;
        right: 10px;
        padding: 4px 10px;
        background-color: #f5f5f5;
        border: 1px solid #ccc;
      }
    }
  }
  .join {
    color: #333;
    font-size: 13px;
  }
  .send {
    background-color: #2ecc71;
    color: #000;
  }
  .receive {
    background-color: #fff;
    color: #000;
  }
  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.5s;
  }
  .fade-enter,
  .fade-leave-to {
    opacity: 0;
  }
}
</style>
