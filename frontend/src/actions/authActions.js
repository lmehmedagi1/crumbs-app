import axios from "axios";
import { env } from "../configs/env";


export const cookieHeader = {
    withCredentials: true,
    headers: { "Access-Control-Allow-Origin": "*", 'Access-Control-Allow-Credentials': true, 'Content-Type': 'application/json' }
}

export function login(values) {
    return {
        type: "AUTH_LOGIN",
        payload: axios.post(env.BASE_PATH + 'user-service/auth/login', values, cookieHeader)
    };
}

export function logout() {
    return {
        type: "AUTH_LOGOUT",
        payload: axios.post(env.BASE_PATH + 'user-service/auth/logout', {}, cookieHeader)
    };
}

export function setState(data) {
    return {
        type: "AUTH_SET_STATE",
        payload: data,
    };
}
