import Vue from "vue";
import Router from "vue-router";
import Data from "./components/application/Data";
import Application from "./components/application/Application";
import Users from "./components/administration/Users";

Vue.use(Router);

export default new Router({
    mode: "history",
    base: process.env.BASE_URL,
    routes: [{
            path: "/application",
            name: "application",
            component: Application
        },
        {
            path: "/data",
            name: "data",
            component: Data,
        }, {
            path: "/users",
            name: "users",
            component: Users,
        },
    ]
});