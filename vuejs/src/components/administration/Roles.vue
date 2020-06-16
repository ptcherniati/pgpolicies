<template>
  <v-expansion-panel-content
    id="roles"
    class="roles"
  >
    <template v-slot:header>
      <div>Rôles</div>
    </template>
    <v-form>
      <table
        id="roles"
        class="roles"
      >
        <tr>
          <th>Nom</th>
        </tr>
        <tr
          v-for="(item) in roles"
          :key="item.uri"
          :itemid="item.nom"
          :itemref="item.uri"
        >
          <td>{{ item.nom }}</td>
        </tr>
        <!--tr>
          <td>
            <v-text-field
              v-model="newRoles"
              :rules="rules"
              append-icon="mdi-plus"
              messages="Ajouter un rôle."
              @click:append="ajouteRole"
            />
          </td>
        </tr-->
      </table>
    </v-form>
  </v-expansion-panel-content>
</template>

<script>
//import { storage, Storage } from "@/storage";
import store from "@/store";
import EventBus from "@/eventBus";
export default {
  name: "Roles",
  methods: {
    ajouteRole:function(){
      var role = {
        "nom":this.newRoles
        }
      this.$emit('addRole',role)
      store.dispatch('addRole',role);
      console.log(role);
    },
  },
  props:{
  },
  data: () => ({
    valid:false,
    newRoles:"ROLE_",
    rules: [
      value => !!value || 'Required.',
      value => (value || '').length >=6|| (value || '').length <= 20 || 'Roles doit avoir entre 6 et 20 caractères',
      value => {
        const pattern = /^ROLE_[A-Z]*$/
        return pattern.test(value) || 'Roles est doit commencer par ROLE_.';
      },
    ],
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
