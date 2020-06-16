import Vue from "vue";
import Vuetify from 'vuetify'
import App from "./App.vue";
import router from "./router";
import store from "./store";
import dateFormat from "@/filter/dateFormat.filter.js";
import directive from "@/directive";
import 'vuetify/dist/vuetify.min.css'
import MultiFiltersPlugin from './plugins/MultiFilters.js'
// Import the Auth0 configuration
import {
    domain,
    clientId
} from "./auth_config.json";

// Import the plugin here
import {
    Auth0Plugin
} from "./auth";

Vue.config.productionTip = false;
Vue.use(Vuetify)
Vue.use(Vuetify, {
    iconfont: 'mdi' // 'md' || 'mdi' || 'fa' || 'fa4'
})
Vue.use(MultiFiltersPlugin);

/*auth2
Vue.use(Auth0Plugin, {
    domain,
    clientId,
    onRedirectCallback: appState => {
        router.push(
            appState && appState.targetUrl ?
            appState.targetUrl :
            window.location.pathname
        );
    }
});

Vue.config.productionTip = false;
/*auth2*/
directive.forEach(directive => Vue.use(directive));
Vue.config.devtools = true
new Vue({
    router,
    store,
    filters: {
        dateFormat
    },
    render: h => h(App)
}).$mount("#app");