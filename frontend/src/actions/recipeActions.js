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
        payload: axios("http://localhost:8090/recipe-service/recipes", {
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
        payload: axios("http://localhost:8090/recipe-service/recipes/topMonthly", {
            method: "GET",
            params: {
                pageNo: pageNumber
            }
        })
    };
}

export function getDailyRecipes(pageNumber) {
    return {
        type: "RECIPE_GET_DAILY",
        payload: axios("http://localhost:8090/recipe-service/recipes/topDaily", {
            method: "GET",
            params: {
                pageNo: pageNumber
            }
        })
    };
}

export function updateRating(rating, entity, entityId, reviewId) {
    return {
        type: "RECIPE_POST_RATING",
        payload: axios("http://localhost:8090/review-service/reviews", {
            method: "PATCH",
            params: {
                id: reviewId
            },
            data: {
                entity_id: entityId,
                entity_type: entity,
                rating
            },
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NWE4ZjM0Yi0yNTM5LTQ1MmEtOTMyNS1iNDMyZGJlM2I5OTUiLCJpYXQiOjE2MjI0ODk3NTcsImV4cCI6MTYyMjU4NDgwMH0.CtLNVMSg7njioQCez4H27vgpaKcuCN08XNNpoMKJynW7SezpwK8qDtNd6_-kyBQ_w8XebrGPIb7GiQUqoYr2yA"
            }
        })
    };
}

export function updateLike(is_liked, entity, entityId, reviewId) {
    
    return {
        type: "RECIPE_POST_LIKE",
        payload: axios("http://localhost:8090/review-service/reviews", {
            method: "PATCH",
            params: {
                id: reviewId
            },
            data: {
                entity_id: entityId,
                entity_type: entity,
                is_liked
            },
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NWE4ZjM0Yi0yNTM5LTQ1MmEtOTMyNS1iNDMyZGJlM2I5OTUiLCJpYXQiOjE2MjI0ODk3NTcsImV4cCI6MTYyMjU4NDgwMH0.CtLNVMSg7njioQCez4H27vgpaKcuCN08XNNpoMKJynW7SezpwK8qDtNd6_-kyBQ_w8XebrGPIb7GiQUqoYr2yA"
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
            }
        })
    };
}

export function getEntityReviewForUser(entityId) {
    console.log("idEntity", entityId);
    return {
        type: "RECIPE_GET_REVIEW",
        payload: axios("http://localhost:8090/review-service/reviews/review-of-user", {
            method: "GET",
            params: {
                entityId
            },
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NWE4ZjM0Yi0yNTM5LTQ1MmEtOTMyNS1iNDMyZGJlM2I5OTUiLCJpYXQiOjE2MjI0ODk3NTcsImV4cCI6MTYyMjU4NDgwMH0.CtLNVMSg7njioQCez4H27vgpaKcuCN08XNNpoMKJynW7SezpwK8qDtNd6_-kyBQ_w8XebrGPIb7GiQUqoYr2yA"
            }
        })
    };
}



export function postComment(comment, entity, entityId, reviewId) {
    return {
        type: "RECIPE_POST_COMMENT",
        payload: axios("http://localhost:8090/review-service/reviews", {
            method: "PATCH",
            params: {
                id: reviewId
            },
            data: {
                entity_id: entityId,
                entity_type: entity,
                comment
            },
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NWE4ZjM0Yi0yNTM5LTQ1MmEtOTMyNS1iNDMyZGJlM2I5OTUiLCJpYXQiOjE2MjI0ODk3NTcsImV4cCI6MTYyMjU4NDgwMH0.CtLNVMSg7njioQCez4H27vgpaKcuCN08XNNpoMKJynW7SezpwK8qDtNd6_-kyBQ_w8XebrGPIb7GiQUqoYr2yA"
            }
        })
    };
}


export function editComment(comment, entity, entityId, reviewId) {
    console.log("alooo", comment)
    return {
        type: "RECIPE_EDIT_COMMENT",
        payload: axios("http://localhost:8090/review-service/reviews", {
            method: "PATCH",
            params: {
                id: reviewId
            },
            data: {
                entity_id: entityId,
                entity_type: entity,
                comment
            },
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NWE4ZjM0Yi0yNTM5LTQ1MmEtOTMyNS1iNDMyZGJlM2I5OTUiLCJpYXQiOjE2MjI0ODk3NTcsImV4cCI6MTYyMjU4NDgwMH0.CtLNVMSg7njioQCez4H27vgpaKcuCN08XNNpoMKJynW7SezpwK8qDtNd6_-kyBQ_w8XebrGPIb7GiQUqoYr2yA"
            }
        })
    };
}


export function getRecipeReviews(recipeId, pageNo) {
    return {
        type: "RECIPE_GET_REVIEWS",
        payload: axios("http://localhost:8090/review-service/reviews/comments", {
            method: "GET",
            params: {
                recipeId,
                pageNo
            }
        })
    }
}


export function clearState() {
    return {
        type: "RECIPE_CLEAR_STATE",
    };
}

export function setState(data) {
    return {
        type: "RECIPE_SET_STATE",
        payload: data,
    };
}
