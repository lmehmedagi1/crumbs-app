import axios from "axios";
import { env } from "../configs/env";

export function get(id) {
    return {
        type: "RECIPE_GET",
        payload: axios(env.BASE_PATH + "/api/recipes/" + id, {
            method: "GET",
        })
    };
}

export function getMostPopularRecipes(pageNumber) {
    return {
        type: "RECIPE_GET_MOST_POPULAR",
        payload: axios(env.BASE_PATH + "recipe-service/recipes/topMonthly", {
            method: "GET",
            params: {
                pageNo: pageNumber
            },
        })
    };
}

export function setState(data) {
    return {
        type: "RECIPE_SET_STATE",
        payload: data,
    };
}

export function clearState() {
    return {
        type: "RECIPE_CLEAR_STATE",
    };
}


export function getRecipeRating(recipeId) {
    return {
        type: "RECIPE_GET_RATING",
        payload: axios(env.BASE_PATH + "review-service/reviews/rating", {
            method: "GET",
            params: {
                recipeId
            },
        })
    };
}

export function getRecipeReviews(recipeId) {
    return {
        type: "RECIPE_GET_REVIEWS",
        payload: axios(env.BASE_PATH + "review-service/reviews", {
            method: "GET",
            params: { recipeId },
        })
    }
}

export function getDailyRecipes(pageNo) {
    return {
        type: "RECIPE_GET_DAILY",
        payload: axios(env.BASE_PATH + "recipe-service/recipes/topDaily", {
            method: "GET",
            params: { pageNo }
        })
    };
}