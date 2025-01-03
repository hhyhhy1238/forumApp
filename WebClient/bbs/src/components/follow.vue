<script>
export default {
  name: "follow.vue",
  data(){

  },
  props:["user"]
}
</script>

<template>
<div class="user">
  <el-image :src="user.avatar" alt="" class="avatar"></el-image>
  <p>{{user.nickname}}</p>
</div>

</template>

<style scoped>
.user{
  display:flex;
  align-self: center;
  padding: 10px;
  border-top: 1px solid #ccc;
  min-width: 300px;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}
.user p {
  font-size: 14px;
  color: #888;
}
</style>