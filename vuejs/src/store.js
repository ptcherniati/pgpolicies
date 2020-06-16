import Vue from 'vue'
import Vuex from 'vuex'
import defaultData from '@/data'
import http from "@/http/http.js";
import EventBus from '@/eventBus';

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        data: defaultData,
        user: {
            init: true,
            username: "admin",
            password: "password"
        },
        climatikUser: null,
        challenge: null,
        users: {},
        roles: null,
        aliments: null,
        applications: [],
        policies: [],
        code: null,
    },
    mutations: {
        removeUser(state) {
            this.state.user = null;
        },
        setUser(state, data) {
            this.state.user = data;
            this.dispatch('loadRoles');
            EventBus.$emit('user:connected', data)
        },
        setCode(state, code) {
            this.state.code = code;
        },
        setUsers(state, data) {
            this.state.users = data.map(
                function(user) {
                    user.hasRoles = {};
                    user = this
                        .reduce((acc, role) => {
                            acc.hasRoles[role.nom] = acc.authorities.find(authority => role.nom == authority.authorities, {}) != null;
                            return acc
                        }, user);
                    return user;
                }, this.state.roles);
            EventBus.$emit('users:loaded', data)
        },
        setChallenge(state, data) {
            this.state.challenge = data
            EventBus.$emit('challenge:loaded', data)
            if (this.state.code != null) {
                http
                    .route(this.state.code)
                    .then(response => {
                        if (response.ok) {
                            response.json().then(function(data) {
                                console.log(challenge)
                            })
                        } else {
                            console.log(response)
                        }
                    })
                    .catch(error => {
                        console.log(error);
                        EventBus.$emit('setChallenge:cantload')
                    });
            }
        },
        logOut(state, payload) {
            if (payload.ok) {
                EventBus.$emit('user:disconnected', payload)
            } else {
                EventBus.$emit('user:disconnected:error', payload)
            }
            this.state.user = {
                init: true,
            };
            this.state.data = defaultData;
        },
        setAliments(state, payload) {
            this.state.aliments = payload;
            EventBus.$emit('aliments:loaded', payload)
        },
        setRoles(state, data) {
            EventBus.$emit('roles:loaded', data);
            this.state.roles = data;
            this.dispatch('loadPolicies');
        },
        setPolicies(state, data) {
            this.state.policies = data;
            EventBus.$emit('policies:loaded', data)
            this.dispatch('loadUsers');
            this.dispatch('loadAliments');
        },
    },
    actions: {
        loadUser({
            commit
        }, user) {
            http
                .login(user)
                .then(response => {
                    response.json().then(function(data) {
                            commit('setUser', data)
                        })
                        .catch(error => {
                            console.log(error);
                            EventBus.$emit('user:disconnected')
                        });
                    EventBus.$emit('user:disconnected')
                })
                .catch(error => {
                    console.log(error);
                    EventBus.$emit('user:disconnected')
                });
        },
        loadUsers({
            commit
        }) {
            http
                .loadUsers()
                .then(response => response.json())
                .then(response => {
                    if (response.ok) {
                        response.json().then(function(data) {
                            commit('setUsers', data)
                        })
                    } else {
                        commit('setUsers', response)
                    }
                })
                .catch(error => {
                    console.log(error);
                    EventBus.$emit('users:cantload')
                });
        },
        loadClimatikUser({
            commit
        }) {
            http
                .loginClimatikUser()
                .then(response => {
                    if (response.ok) {
                        response.json().then(function(data) {
                            commit('setClimatikUser', data)
                        })
                    } else {
                        commit('setClimatikUser', response)
                    }
                })
                .catch(error => {
                    console.log(error);
                    EventBus.$emit('climatikUrser:disconnected')
                });
        },
        route({
            commit
        }, code) {
            commit('setCode', code);
        },
        logOut({
            commit
        }) {
            http
                .logOut()
                .then(response => {
                    commit('logOut', response);
                })
                .catch(error => {
                    console.log(error)
                    EventBus.$emit('users:cantlogOut')
                });
        },
        loadAliments({
            commit
        }) {
            http
                .loadAliments()
                .then(response => {
                    if (response.ok) {
                        response.json().then(function(data) {
                            commit('setAliments', data);
                        })
                    } else {
                        commit('setAliments', response)
                    }
                })
                .catch(error => {
                    console.log(error)
                    EventBus.$emit('aliment:cantget')
                });
        },
        loadRoles({
            commit
        }) {
            http
                .loadRoles()
                .then(response => {
                    if (response.ok) {
                        response.json().then(function(data) {
                            commit('setRoles', data);
                        })
                    } else {
                        commit('setRoles', response)
                    }
                })
                .catch(error => {
                    console.log(error)
                    EventBus.$emit('roles:cantget')
                });
        },
        addRole({
            commit
        }, role) {
            console.log('evenement add rôle bien reçu' + role)
            http
                .addRoles(role)
                .then(response => {
                    if (response.ok) {
                        commit('roles:added', response.headers.get('Location'))
                        this.dispatch('loadRoles');
                    } else {
                        commit('roles/added:fail', response.json())
                    }
                })
                .catch(error => {
                    console.log(error)
                    EventBus.$emit('roles:cantLoad')
                });
        },
        addPolicy({
            commit
        }, policy) {
            console.log('evenement add policy bien reçu' + policy.nom)
            http
                .addPolicies(policy)
                .then(response => {
                    if (response.ok) {
                        commit('policies:added', response.headers.get('Location'))
                        this.dispatch('loadRoles');
                    } else {
                        commit('policies/added:fail', response.json())
                    }
                })
                .catch(error => {
                    console.log(error)
                    EventBus.$emit('policies:cantload')
                });
        },
        removePolicy({
            commit
        }, policy) {
            console.log('evenement add policy bien reçu' + policy.nom)
            http
                .removePolicies(policy)
                .then(response => {
                    if (response.ok) {
                        commit('policies:removed', response.headers.get('Location'))
                        this.dispatch('loadRoles');
                    } else {
                        commit('policies/removed:fail', response.json())
                    }
                })
                .catch(error => {
                    console.log(error)
                    EventBus.$emit('policies:cantremove')
                });
        },
        associateRole({
            commit
        }, roleUser) {
            console.log('evenement associateRole bien reçu :' + roleUser.user.username + ' / ' + roleUser.role.nom)
            http
                .associateRole(roleUser.user, roleUser.role)
                .then(response => {
                    if (response.ok) {
                        commit('role:associated', response.headers.get('Location'))
                        this.dispatch('loadRoles');
                    } else {
                        commit('role/associated:fail', response.json())
                    }
                })
                .catch(error => {
                    console.log(error)
                    EventBus.$emit('role:cantassociate')
                });
        },
        disAssociateRole({
            commit
        }, roleUser) {
            console.log('evenement disAssociateRole bien reçu :' + roleUser.user.username + ' / ' + roleUser.role.nom)
            http
                .disAssociateRole(roleUser.user, roleUser.role)
                .then(response => {
                    if (response.ok) {
                        commit('role:disAssociated', response.headers.get('Location'))
                        this.dispatch('loadRoles');
                    } else {
                        commit('role/disAssociated:fail', response.json())
                    }
                })
                .catch(error => {
                    console.log(error)
                    EventBus.$emit('policies:cantremove')
                });
        },
        loadPolicies({
            commit
        }) {
            http
                .loadPolicies()
                .then(response => {
                    if (response.ok) {
                        response.json().then(function(data) {
                            commit('setPolicies', data);
                        })
                    } else {
                        commit('setPolicies', response)
                    }
                })
                .catch(error => {
                    console.log(error)
                    EventBus.$emit('policies:cantget')
                });
        },
        createChallenge({
            commit
        }) {
            if (this.state.challenge == null) {
                http
                    .challenge()
                    .then(response => {
                        if (response.ok) {
                            response.json().then(function(body) {
                                commit('setChallenge', body);
                            })
                        } else {
                            commit('setChallenge', response)
                        }
                    })
                    .catch(error => {
                        console.log(error)
                        EventBus.$emit('challenge:cantget')
                    });
            }
        },
    },
    getters: {
        isLogged(state) {
            return state.user != null;
        },
    }
})