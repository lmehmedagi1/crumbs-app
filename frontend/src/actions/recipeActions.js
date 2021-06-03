import axios from "axios"
import { env } from "../configs/env"

export function get(id) {
    return {
        type: "RECIPE_GET",
        payload: axios(env.BASE_PATH + "recipe-service/recipes/recipe", {
            method: "GET",
            params: {
                id
            }
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

export function pushFile(payload) {
    return {
        type: "RECIPE_PUSH_FILE",
        payload,
    };
}

export function setState(data) {
    return {
        type: "RECIPE_SET_STATE",
        payload: data,
    };
}

export function updateRating(payload) {
    return {
        type: "RECIPE_POST_RATING",
        payload
    };
}

export function updateLike(payload) {
    
    return {
        type: "RECIPE_POST_LIKE",
        payload
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
            }
        })
    };
}

export function deleteReview(payload) {
    return {
        type: "RECIPE_DELETE_REVIEW",
        payload
    };
}

export function getEntityReviewForUser(payload) {
    return {
        type: "RECIPE_GET_REVIEW",
        payload
    };
}

export function postComment(payload) {
    return {
        type: "RECIPE_POST_COMMENT",
        payload
    };
}


export function editComment(payload) {
    return {
        type: "RECIPE_EDIT_COMMENT",
        payload
    };
}


export function getRecipeReviews(recipeId, pageNo, userId) {
    return {
        type: "RECIPE_GET_REVIEWS",
        payload: axios("http://localhost:8090/review-service/reviews/comments", {
            method: "GET",
            params: {
                recipeId,
                pageNo,
                userId
            }
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
