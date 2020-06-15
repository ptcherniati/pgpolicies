<template>
  <div id="roles">
    <v-expansion-panel value="[0,1,2]]">
      <Roles />
      <RolesUsers />
      <Policies />
    </v-expansion-panel>
  </div>
</template>

<script>
//import { storage, Storage } from "@/storage";
import store from "@/store";
import EventBus from "@/eventBus";
import Roles from "@/components/administration/Roles";
import RolesUsers from "@/components/administration/RolesUsers";
import Policies from "@/components/administration/Policies";
export default {
  name: "Users",
  methods: {
    fetchData() {
      this.error = this.users = null;
      this.loading = true;
    }
  },
  data: () => ({
    panel: [0, 1, 2]
  }),
  beforeCompile() {},
  compiled() {},
  ready() {},
  mounted() {},
  created() {},
  computed: {
    policiesHeader: {
      get() {
        return ["nom", "description", "for", "using", "check"];
      }
    },
    forSelect: {
      get() {
        return ["ALL", "SELECT", "INSERT", "UPDATE", "DELETE"];
      }
    },
    nomSelect: {
      get() {
        return this.roles.map(r => r.nom);
      }
    },

    isAdmin: {
      get() {
        return (
          store.state.users != null &&
          store.state.user != null &&
          store.state.user.authorities.find(function(a) {
            return (a.authorities == "ROLE_ADMIN") != null;
          })
        );
      }
    },
    user: {
      get() {
        return store.state.user;
      }
    },
    users: {
      get() {
        return store.state.users;
      }
    },
    roles: {
      get() {
        var u = this.user;
        return store.state.roles;
      }
    },
    policies: {
      get() {
        return store.state.policies;
      }
    }
  },
  props: {},
  data() {
    return {
      admin: false,
    };
  },
  components: {
    Roles,
    RolesUsers,
    Policies,
  },
  watch: {}
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
-expansion-panel__body {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-content: center;
    align-items: stretch;
}
.user[data-v-74303864] {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-content: center;
  align-items: baseline;
  width: 100%;
}

table.roles {
  border-collapse: collapse;
  font-size: 1.5em;
}
table.roles td,
table.roles th {
  text-align: center;
  border: solid 1px grey;
  padding-left: 0.5em;
  padding-right: 0.5em;
}
table.roles td:first-child,
table.roles td:first-child {
  text-align: left;
}
</style>
