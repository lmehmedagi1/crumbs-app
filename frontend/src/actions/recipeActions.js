import axios from "axios";
import { env } from "../configs/env";

export function get(id) {
    return {
        type: "RECIPE_GET",
        payload: axios(env.SERVER_ENV.url + "/api/recipes/" + id, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + localStorage.getItem("token")
            }
        })
    };
}

export function setState(data) {
    return {
        type: "RECIPE_SET_STATE",
        payload: data,
    };
}
