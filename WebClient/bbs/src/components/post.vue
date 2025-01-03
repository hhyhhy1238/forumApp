<template>
  <div class="post">
    <div class="info">
      <el-image class="avatar" :src="post.avatar" fit="cover"></el-image>
      <div class="textinfo">
        <h6>{{ post.ownernickname }}</h6>
        <p>{{ post.time }}</p>
      </div>
    </div>

    <div class="content">
      <h4>{{ post.title }}</h4>
      <p class="content-text">{{ post.content }}</p>
      <div class="images">
        <el-image class="image" v-for="(image, index) in limitedImages" :key="index" :src="image" fit="contain"></el-image>
        <div v-if="post.images.length > 5" class="image-count">+{{ post.images.length - 5 }}</div>
      </div>
    </div>
    <div class="numinfo">
      <div class="likenum">
        <el-icon class="like"></el-icon>
        {{ post.likenum }}
      </div>

      <div class="favornum">
        <el-icon class="favor"></el-icon>
        {{ post.favornum }}
      </div>
      <div class="commentnum">
        <el-icon class="comment"></el-icon>
        {{ post.commentnum }}
      </div>
    </div>
  </div>
</template>

<script>
import { ElImage, ElIcon } from "element-plus";
import {Plus, Star} from "@element-plus/icons-vue";
export default {
  name: "Post",
  components: {
    Star,
    Plus,
    ElImage,
    ElIcon,
  },
  props: ["post"],
  computed: {
    limitedImages() {
      console.log(this.post.images)
      return this.post.images.slice(0, 5);
    }
  }
};
</script>

<style scoped>
.post {
  border: 1px solid #ccc;
  padding: 5%;
  padding-top: 10px ;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.info {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 15px;
}

.textinfo {
  display: flex;
  flex-direction: column;
}

.textinfo h6 {
  font-size: 16px;
  font-weight: bold;
  margin-top: 5px;
  margin-bottom: 5px;
}

.textinfo p {
  font-size: 12px;
  color: #999;
  margin: 0;
}

.content {
  border-top: 1px solid #ccc;
  margin-top: 10px;
  padding-top: 10px;
}

.content h4 {
  font-size: 18px;
  font-weight: bold;
  margin-top: 5px;
  margin-bottom: 5px;
}

.content p {
  font-size: 14px;
  line-height: 1.5;
  overflow: hidden;
  text-overflow:ellipsis;
  -webkit-line-clamp: 3;
  word-wrap: break-word;
  display: -webkit-box;
  -webkit-box-orient: vertical;

}

.images {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10px;
  padding: 5px;
}

.image{
  width: 100px;
  height: 100px;
  object-fit: contain;
  margin-right: 10px;
  margin-bottom: 10px;
}

.image-count {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
  font-size: 24px;
}

.numinfo {
  display: flex;
  align-items: center;
  border-top: 1px solid #ccc;
  padding-top: 10px;
  margin-top: 10px;
}

.numinfo div {
  display: flex;
  align-items: center;
  justify-items: center;
  margin-left: auto;
  margin-right: auto;
  font-size: 14px;
  color: #999;
}

.numinfo div el-icon {
  margin-right: 5px;
}
.like{
  width: 18px;
  height: 18px;
  background-image: url("@/assets/heart.png");
  margin-right: 20px;
}
.favor{
  width: 18px;
  height: 18px;
  background-image: url("@/assets/star.png");
  margin-right: 20px;
}
.comment{
  width: 18px;
  height: 18px;
  background-image: url("@/assets/comment.png");
  margin-right: 20px;
}
</style>