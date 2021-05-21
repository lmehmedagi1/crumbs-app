import axios from "axios";
import { env } from "../configs/env";

export function get(id) {
    return {
        type: "RECIPE_GET",
        payload: axios("http://localhost:8090/recipe-service/recipes", {
            method: "GET",
            params: {
                id
            },
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NWE4ZjM0Yi0yNTM5LTQ1MmEtOTMyNS1iNDMyZGJlM2I5OTUiLCJpYXQiOjE2MjE1MzU2NjgsImV4cCI6MTYyMTYzNDQwMH0.L57oPUx_l_a8gzlOIJVU4hYK7YZSA-VUevwv_zvOWXtlC92mlFkf6f6rWlTzOorhSelfWCmR2eg1ZOq2M4RImQ"
            }
        })
    };
}

export function getMostPopularRecipes(pageNumber) {
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

export function getRecipeRating(recipeId) {
    return {
        type: "RECIPE_GET_RATING",
        payload: axios("http://localhost:8090/review-service/reviews/rating", {
            method: "GET",
            params: {
                recipeId
            },
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NWE4ZjM0Yi0yNTM5LTQ1MmEtOTMyNS1iNDMyZGJlM2I5OTUiLCJpYXQiOjE2MjE1MzU2NjgsImV4cCI6MTYyMTYzNDQwMH0.L57oPUx_l_a8gzlOIJVU4hYK7YZSA-VUevwv_zvOWXtlC92mlFkf6f6rWlTzOorhSelfWCmR2eg1ZOq2M4RImQ"
            }
        })
    };
}


export function getRecipeReviews(recipeId) {
    return {
        type: "RECIPE_GET_REVIEWS",
        payload: axios("http://localhost:8090/review-service/reviews", {
            method: "GET",
            params: {
                recipeId
            },
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NWE4ZjM0Yi0yNTM5LTQ1MmEtOTMyNS1iNDMyZGJlM2I5OTUiLCJpYXQiOjE2MjE1MzU2NjgsImV4cCI6MTYyMTYzNDQwMH0.L57oPUx_l_a8gzlOIJVU4hYK7YZSA-VUevwv_zvOWXtlC92mlFkf6f6rWlTzOorhSelfWCmR2eg1ZOq2M4RImQ"
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
