import axios from "axios";
import config from "@/config";
import store from "../store";

class HttpClient {
    helloWorld() {
        return axios.get(`${config.API_URL}`);
    }

    login(user) {
        let formData = new FormData();
        formData.append("username", user.username);
        formData.append("password", user.password);
        return fetch(
            `${config.API_URL}Login`, {
                method: "POST",
                body: formData,
            }
        )
    }

    loadUsers() {
        return fetch(
            `${config.API_URL}Users`, {
                method: "GET",
                mode: 'no-cors',
            }
        )
    }

    route(code) {
        var json = {
            "client_id": config.credentials.client.client_id,
            "code": code,
            "code_verifier": store.state.challenge.verifier,
            "grant_type": "code",
        };

        let formBody = [];
        for (let property in json) {
            let encodedKey = encodeURIComponent(property);
            let encodedValue = encodeURIComponent(json[property]);
            formBody.push(encodedKey + "=" + encodedValue);
            console.log("json." + encodedKey + " = " + encodedValue);
        }
        console.log("code_verifier=" + store.state.challenge.verifier);
        console.log("code_challenge=" + store.state.challenge.challenge);


        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/x-www-form-urlencoded");
        myHeaders.append("Access-Control-Allow-Headers", "Accept, Content-Type");
        myHeaders.append("accept", "application/json ");
        return fetch(
            `${config.CLIMATIK_BASE}oauth/token`, {
                method: "POST",
                headers: myHeaders,
                referrer: "https: //82.229.109.241:9090/static/index.html",
                referrerPolicy: "same-origin",
                body: formBody,
                mode: "cors",
                redirect: 'manual',
                credentials: "same-origin",
            }
        );
    };

    loginClimatikUser() {
        var myHeaders = new Headers();
        myHeaders.append("Access-Control-Allow-Origin", "https://82.229.109.241:9090");
        return fetch(
            `${config.CLIMATIK}`, {
                headers: myHeaders,
                //referrer: "https://82.229.109.241:9090",
                referrerPolicy: "origin",
            }
        )
    }

    logOut() {
        return fetch(
            `${config.API_URL}Logout`, {
                method: "DELETE",
            }
        )
    }

    loadAliments() {
        return fetch(`${config.API_URL}Aliments`, {
            credentials: "same-origin"
        });
    }

    loadRoles() {
        return fetch(`${config.API_URL}Roles`, {
            "Content-Type": "text/plain",
            credentials: "same-origin"
        });
    }

    addPolicies(policy) {
        var json = JSON.stringify(policy);
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json")
        myHeaders.append("Content-Length", json.length.toString());
        return fetch(
            `${config.API_URL}Policies`, {
                method: "POST",
                credentials: "same-origin",
                headers: myHeaders,
                body: json
            }
        );
    }

    removePolicies(policy) {
        var json = JSON.stringify(policy);
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json")
        myHeaders.append("Content-Length", json.length.toString());
        return fetch(
            `${config.API_URL}Policies`, {
                method: "DELETE",
                credentials: "same-origin",
                headers: myHeaders,
                body: json
            }
        );
    }

    disAssociateRole(user, role) {
        let authorities = user.authorities.find(function(o, i) {
            return o.authorities == role.nom;
        })
        authorities.roles = authorities.authorities;
        var json = JSON.stringify(authorities);
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json")
        myHeaders.append("Content-Length", json.length.toString());
        return fetch(
            `${config.API_URL}Authorities`, {
                method: "DELETE",
                credentials: "same-origin",
                headers: myHeaders,
                body: json
            }
        );
    }

    associateRole(user, role) {
        let authorities = {
            username: user.username,
            roles: role
        }
        var json = JSON.stringify(authorities);
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json")
        myHeaders.append("Content-Length", json.length.toString());
        return fetch(
            `${config.API_URL}Authorities`, {
                method: "POST",
                credentials: "same-origin",
                headers: myHeaders,
                body: json
            }
        );
    }

    loadPolicies() {
        return fetch(`${config.API_URL}Policies`, {
            credentials: "same-origin"
        });
    }

    loadDataset(dataset, applicationName) {
        return fetch(`${config.API_URL}/${applicationName}/data/${dataset}`, {
            credentials: "same-origin"
        });
    }
    challenge() {
        return fetch(
            `${config.API_URL}Verifier`
        )
    }
}
const http = new HttpClient()
export default http