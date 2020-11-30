<template>
  <div id="app">
<!--    <router-link to="/login">-->
<!--      <div>-->
<!--        {{ getString }}-->
<!--      </div>-->
<!--    </router-link>-->
<!--    <router-link to="/home">-->
<!--      <div>-->
<!--        home-->
<!--      </div>-->
<!--    </router-link>-->
    <router-view></router-view>
  </div>
</template>

<script>

export default {
  name: 'app',
  components: {},
  data() {
    return {
      getString: "ready to load"
    };
  },
  methods: {
    onClick() {
      this.getData();
    },
    async getData() {
      let data = await this.axios.get("/")
      this.getString = data.data
    }
  }

}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
