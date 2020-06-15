<template>
  <div class="data">
    <v-container v-if="user.users!=null">
      <v-btn
        @click="refresh">
        <v-icon>mdi-refresh</v-icon>
      </v-btn>
      <table>
        <tr>
          <th
            v-for="(item, key) in aliments"
            :key="key"
            :itemid="item.id"
            :itemref="getURI('Categories',item.id)"
          >
            {{ key }}
          </th>
        </tr>
        <tr>
          <td
            v-for="(item, key) in aliments"
            :key="key"
            :itemid="item.id"
            :itemref="getURI('Categories',item.id)"
          >
            <ul>
              <li
                v-for="(item2, key2) in sortKeys(item)"
                :key="key2"
                :itemid="item.value[item2].id"
                :itemref="getURI('Aliments',item.value[item2].id)"
              >
                {{ item2 }}
              </li>
            </ul>
          </td>
        </tr>
      </table>
    </v-container>
  </div>
</template>

<script>
//import { storage, Storage } from "@/storage";
import store from "@/store";
import Data from "@/components/application/Data";
import EventBus from "@/eventBus";
import config from "@/config";
export default {
  name: "Data",

  mounted () {
  },
  computed: {
    user: {
      get() {
        return store.state.user;
      }
    },
    aliments: {
      get() {
        return store.state.aliments.reduce((rObj,obj) => {
          rObj[obj.categoryVO.nom] = rObj[obj.categoryVO.nom]==null?{"id":obj.categoryVO.id,"value":{}}:rObj[obj.categoryVO.nom];
          rObj[obj.categoryVO.nom]["value"][obj.nom] = obj;
          return rObj;
        },{});
      }
    },
  },
  props: {},
  data () {
    return {
    }
  },
  methods: {
    getURI(api,id){
      return config.API_URL+api+'/'+id;
    },
    sortKeys(item){
      return Object.keys(item.value).sort(function(a, b) { return a.localeCompare(b) });
    },
    refresh:function(){
      store.dispatch('loadRoles');
    }
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
table{
    width:100%;
    border:#672e67 solid 3px;
    border-collapse:collapse
}
tr, td, th{
    border:#672e67 solid 1px;
}
th{
    background:linear-gradient(#f9f0f0,#cacae8)
}

ul[data-v-36ccc9c8] {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    align-content: center
}

li[data-v-36ccc9c8] {
    display: inline-block;
    margin: 0 10px
}
a {
  color: #42b983;
}
</style>
