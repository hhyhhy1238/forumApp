<template>
  <div class="comment">
    <el-image :src="comment.avatar" class="avatar"></el-image>
    <div class="comment-text">
      <p class="nickname">{{ comment.nickname }}</p>
      <p>{{ comment.content }}</p>
      <p class="comment-time">{{ comment.time }}</p>
    </div>
  </div>
</template>

<script>
import { ElImage } from "element-plus";

export default {
  name: "Comment",
  components: {
    ElImage,
  },
  props: ["comment"],
};
</script>

<style scoped>
.comment {
  display: flex;
  //align-items:center;
  margin-bottom: 10px;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin: 30px;
  margin-top: 15px;
  margin-bottom: 15px;
  min-width: 50px;
  max-width: 50px;
}

.comment-text {
  display: flex;
  flex-direction: column;
}

.nickname {
  font-size: 15px;
  margin: 5px;
}
.comment-text p{
  margin: 5px;
}
.comment-time {
  color: #999;
  font-size: 12px;
}
</style>