<template>
  <v-form class="login">
    <v-container>
      <v-text-field
        label="Login"
        v-model="user.username"
        required
        name="Login"
      />
      <v-text-field
        label="Password"
        v-model="user.password"
        required
        name="Password"
      />
      <v-btn
        @click="login"
        value="Se connecter"
      >
        Se connecter
      </v-btn>
    </v-container>
    <!--v-container
      v-if="climatikUser!=null"
    >
      {{ climatikUser }}
    </v-container>
    <v-container v-else-if="code!=null">
      Loading...
    </v-container>
    <v-container v-else>
      <a
        :href="
          climatik"
      >
        Se connecter 2
      </a>
    </v-container-->
  </v-form>
</template>

<script>
import store from "@/store";
import config from "@/config";
export default {
  name: "Login",
  beforeCreate: function() {
    console.log ('beforeCreate')
      //store.dispatch("createChallenge");
  },
  created: function() {
    this.user = store.state.user;
    //this.climatikUser = store.state.climatikUser;
  },
  computed: {
    code: {
      get() {
        var route = this.$route.query.code;
        if (store.state.climatikUser==null && route != null) {
          store.dispatch("route", route);
        }
        return this.$route.query.code;
      }

    },
    challenge: {
      get() {
        if(store.state.challenge==null){
          store.dispatch("createChallenge");
          return null;
        } else{
          return store.state.challenge.challenge;
        }
      }

    },
    verifier: {
      get() {
        return store.state.challenge==null?null:store.state.challenge.verifier;
      }

    },
    climatikUser:{
      get(){
        return store.state.climatikUser;
      }
    },
    climatik:{
      get(){
        return store.state.challenge==null?null:config.CLIMATIK.replace('%%code_challenge%%',this.challenge)
      }
    },
  },
  props: {},
  methods: {
    login() {
      store.dispatch("loadUser", this.user);
    },
    logout() {
      store.dispatch("unloadClimatikUser");
      this.$router.push({ path: "/" });
    },
    /*loginClimatikAuth() {
      this.$auth.loginWithRedirect();
    },
    logoutClimatikAuth() {
      this.$auth.logout({
        returnTo: window.location.origin
      });
    },*/
    loginClimatik() {
      store.dispatch("loadClimatikUser");
    },
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
