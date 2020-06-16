<template>
  <v-expansion-panel-content
    id="rolesUsers"
    class="rolesUsers"
  >
    <template v-slot:header>
      <div>Attribution des rôles</div>
    </template>
    <table
      id="attRoles"
      class="roles"
    >
      <tr>
        <th>Nom</th>
        <th
          v-for="(item) in roles"
          :key="item.uri"
          :itemid="item.nom"
          :itemref="item.uri"
        >
          {{ item.nom }}
        </th>
      </tr>
      <tr
        v-for="(item) in users"
        :key="item.uri"
        :itemid="item.username"
        :itemref="item.uri"
      >
        <td>{{ item.username }}</td>
        <td
          v-for="(item2) in roles"
          :key="item2.uri"
          :itemid="item2.nom"
          :itemref="item2.uri"
        >
          <input
            type="checkbox"
            v-model="item.hasRoles[item2.nom]"
            @click="associate(item,item2)"
          >
        </td>
      </tr>
    </table>
  </v-expansion-panel-content>
</template>

<script>
//import { storage, Storage } from "@/storage";
import store from "@/store";
import EventBus from "@/eventBus";
export default {
  name: "RolesUsers",
  props:{
  },
  methods: {
    associate:function(item,item2){
      let roleUser ={
        user:item,
        role:item2,
      }
      let action = item.hasRoles[item2.nom]?'disAssociateRole':'associateRole'
      this.$emit(action,item,item2)
      store.dispatch(action,roleUser);
    }
  },
  data: () => ({
  }),
  beforeCompile() {},
  compiled() {},
  ready() {},
  mounted() {},
  created() {},
  computed: {
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
  },
  props: {},
  components: {
    //Configuration,
  },
  watch: {}
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
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
