import store from "@/store";
let FREEBOX_HOSt = 'https://82.229.109.241';
let LOCALHOST = 'http://localhost';
let HOST = LOCALHOST;
export default {
    CLIMATIK: 'https://intranet-preproduction.inra.fr/climatik_v2/rs/oauth/authorize?' +
        'client_id=2345&' +
        'code_challenge=%%code_challenge%%&' +
        'code_challenge_method=s256&' +
        'redirect_uri=https%3A%2F%2F82.229.109.241%3A9090%2Fstatic%2Findex.html&' +
        'response_type=code&' +
        'scope=data%3Aread',
    CLIMATIK_BASE: 'https://intranet-preproduction.inra.fr/climatik_v2/rs/',
    BASE: HOST + ': 9090 / api / v1 / ',
    API_URL: HOST + ':9090/api/v1/',
    WS_URL: 'wss://https://82.229.109.241:9090/api/V1/',
    SWAGGER: HOST + ':9090/swagger-ui.html',
    credentials: {
        client: {
            client_id: 2345,
            client_secret: "B9vKm1aGFUMh",
            redirect_uri: "https://82.229.109.241:9090/static/index.html"
        },
        auth: {
            tokenHost: 'https://intranet-preproduction.inra.fr/climatik_v2/rs/oauth/authorize'
        },
    }
}