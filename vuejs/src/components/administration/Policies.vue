<template>
  <v-expansion-panel-content
    id="policies"
    class="policies"
  >
    <template v-slot:header>
      <div>Policies</div>
    </template>
    <table
      id="policies"
      class="roles"
    >
      <tr>
        <th
          v-for="(item) in policiesHeader"
          :key="item"
        >
          {{ item }}
        </th>
      </tr>
      <tr
        v-for="(item2) in policies"
        :key="item2.uri"
        :itemid="item2.id"
        :itemref="item2.uri"
      >
        <td
          v-for="(item) in policiesHeader"
          :key="item"
        >
          <span v-if="item=='nom'">
            <v-select
              v-model="item2[item].nom "
              :items="nomSelect"
            />
          </span>
          <span v-else-if="item=='for'">
            <v-select
              v-model="item2.for"
              :items="forSelect"
            />
          </span>
          <span v-else>{{ item2[item] }}</span>
        </td>
        <td v-if="item2.nom.nom!='ROLE_ADMIN'">
          <v-btn
            @click="removeRule(item2)">
            <v-icon>mdi-minus</v-icon>
          </v-btn>
        </td>
      </tr>
      <tr>
        <td
          v-for="(item) in policiesHeader"
          :key="item"
        >
          <span v-if="item=='for'">
            <v-select
              v-model="newPolicy.for"
              :items="forSelect"
            />
          </span>
          <span v-else-if="item=='nom'">
            <v-text-field
              v-model="newPolicy.nom.nom"
              :rules="rules"
            />
          </span>
          <span v-else>
            <v-text-field
              v-model="newPolicy[item]"
              :rules="rules"
            />
          </span>
        </td>
        <td>
          <v-btn
            @click="addRule">
            <v-icon>mdi-plus</v-icon>
          </v-btn>
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
  name: "Policies",
  methods: {
  },
  props:{
  },
  data: () => ({
    newItem:{
      nom:{nom:""},
      description:"",
      for:"ALL",
      using:"",
      check:""
    },
    newPolicy:{
      nom:{nom:""},
      description:"",
      for:"ALL",
      using:"",
      check:""
    },
    valid:false,
    rules: [
      value => !!value || 'Required.',
      value => (value || '').length >=6|| (value || '').length <= 20 || 'La règle doit avoir entre 6 et 20 caractères',
    ],
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
    },
  },
  methods: {
    addRule:function(){
      console.log("ajouter une règle")
      var policy = this.newPolicy;
      this.newPolicy=this.newItem;
      console.dir(policy)
      this.$emit('addPolicy',policy)
      store.dispatch('addPolicy',policy);
    },
    removeRule:function(policy){
      console.log("supprimer une règle")
      this.$emit('removePolicy',policy)
      store.dispatch('removePolicy',policy);
      console.log(policy);
    },
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
