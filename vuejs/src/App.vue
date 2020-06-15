<template>
  <v-app>
    <!--v-navigation-drawer app>nav</v-navigation-drawer-->
    <v-toolbar app>
      <v-toolbar-title class="headline text-uppercase">
        <router-link
          to="/application"
          v-if="user.users!=null"
        >
          <v-btn
            color="blue"
            flat
            round
          >
            Application
          </v-btn>
        </router-link>
        <router-link
          to="/data"
          v-if="user.users!=null"
        >
          <v-btn
            color="blue"
            flat
            round
          >
            Aliments
          </v-btn>
        </router-link>
        <router-link
          to="/users"
          v-if="user.users!=null && isAdmin()"
        >
          <v-btn
            color="blue"
            flat
            round
          >
            Utilisateur
          </v-btn>
        </router-link>
      </v-toolbar-title>
      <v-spacer />
      <v-tooltip
        bottom
        v-if="user.users!=null"
      >
        <template v-slot:activator="{ on }">
          <v-btn
            @click="logout"
            color="primary"
            flat
            v-on="on"
          >
            {{ user.username }}
            <v-icon
              left
              x-large
              color="error"
            >
              mdi-logout
            </v-icon>
          </v-btn>
        </template>
        <span>Se déconnecter</span>
      </v-tooltip>
      <v-tooltip
        bottom
        v-else-if="climatikUser!=null"
      >
        <template v-slot:activator="{ on }">
          <v-btn
            @click="logoutclimatik"
            color="primary"
            flat
            v-on="on"
          >
            {{ climatikUser.user.login }}
            <v-icon
              left
              x-large
              color="error"
            >
              mdi-logout
            </v-icon>
          </v-btn>
        </template>
        <span>Se déconnecter</span>
      </v-tooltip>
    </v-toolbar>

    <v-content transition="slide-x-transition">
      <Login v-if="user.init" />
      <Application v-if="user.users!=null" />
      <router-view />
    </v-content>
    <v-footer
      height="auto"
      color="primary lighten-1"
    >
      <v-layout
        justify-center
        row
        wrap
      >
        <v-flex
          primary
          lighten-2
          py-3
          text-xs-center
          white--text
          xs12
        >
          &copy;2018 —
          <strong>Vuetify</strong>
        </v-flex>
      </v-layout>
    </v-footer>
  </v-app>
</template>

<script>
import router from "./router";
import Login from "@/components/Login";
import Application from "@/components/application/Application";
import store from "@/store";
import '@mdi/font/css/materialdesignicons.css'// Ensure you are using css-

export default {
  created() {},
  name: "App",
  components: {
    Login
  },
  computed: {
    menus: {
      get() {
        return router.options.routes;
      }
    },
    user: {
      get() {
        return store.state.user;
      }
    },
    climatikUser: {
      get() {
        return store.state.climatikUser;
      }
    },
    configuration: {
      get() {
        return store.state.configuration;
      }
    }
  },
  methods :{
    logout(){
      store.dispatch('logOut')
    },
    logoutClimatik(){
      alert("not implemented")
    },
    isAdmin(){
      return this.user.authorities.find(
        function(a){
        return a.authorities=='ROLE_ADMIN'!=null
        });
    }
  }
};
</script>
