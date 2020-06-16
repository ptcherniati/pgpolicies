<template>
  <div class="application">
    <v-container v-if="user.users!=null">
     {{user.username}} est connecté.
    </v-container>
  </div>
</template>

<script>
//import { storage, Storage } from "@/storage";
import store from "@/store";
import EventBus from "@/eventBus";
export default {
  name: "Application",
  mounted() {
  },
  computed: {
    user: {
      get() {
        return store.state.user;
      }
    },
  },
  props: {},
  data() {
    return {
    };
  },
  methods: {
  },
  components: {
    //Configuration,
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
