import axios from "axios";
import { env } from "../configs/env";

axios.interceptors.response.use(
    response => response,
    error => {
      throw error
    }
  )
  
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

export function getMostPopularRecipes(pageNumber) {
    try {

        axios.interceptors.response.use(
            response => response,
            error => {
              throw error
            }
          )

        return {
            type: "RECIPE_GET_MOST_POPULAR",
            payload: axios("http://localhost:8090/recipe-service/recipes/topMonthly", {
                method: "GET",
                params: {
                    pageNo: pageNumber
                },
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NWE4ZjM0Yi0yNTM5LTQ1MmEtOTMyNS1iNDMyZGJlM2I5OTUiLCJpYXQiOjE2MjE1MzU2NjgsImV4cCI6MTYyMTYzNDQwMH0.L57oPUx_l_a8gzlOIJVU4hYK7YZSA-VUevwv_zvOWXtlC92mlFkf6f6rWlTzOorhSelfWCmR2eg1ZOq2M4RImQ"
                }
            })
        };
    }
    catch(err) {
        return null
    }
}

export function setState(data) {
    return {
        type: "RECIPE_SET_STATE",
        payload: data,
    };
}
