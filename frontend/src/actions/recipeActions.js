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

export function setState(data) {
    return {
        type: "RECIPE_SET_STATE",
        payload: data,
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
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmYzk4NjEyYi1jMGMyLTQ3OTItYjJhYy1hY2I3ZGQ2NTU1YzEiLCJpYXQiOjE2MjI2Mjg2NDQsImV4cCI6MTYyMjc1NzYwMH0.ZykzuYy9nEJZT2Qt9g0wS9zKb2FMevL6sGQOfMbwgVyFzVMDXQ42Zxx5pWYtTgQ0xPQkMTeXTH3bCp0qxxu3Lw"
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
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmYzk4NjEyYi1jMGMyLTQ3OTItYjJhYy1hY2I3ZGQ2NTU1YzEiLCJpYXQiOjE2MjI2Mjg2NDQsImV4cCI6MTYyMjc1NzYwMH0.ZykzuYy9nEJZT2Qt9g0wS9zKb2FMevL6sGQOfMbwgVyFzVMDXQ42Zxx5pWYtTgQ0xPQkMTeXTH3bCp0qxxu3Lw"
            }
        })
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
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmYzk4NjEyYi1jMGMyLTQ3OTItYjJhYy1hY2I3ZGQ2NTU1YzEiLCJpYXQiOjE2MjI2Mjg2NDQsImV4cCI6MTYyMjc1NzYwMH0.ZykzuYy9nEJZT2Qt9g0wS9zKb2FMevL6sGQOfMbwgVyFzVMDXQ42Zxx5pWYtTgQ0xPQkMTeXTH3bCp0qxxu3Lw"
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
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmYzk4NjEyYi1jMGMyLTQ3OTItYjJhYy1hY2I3ZGQ2NTU1YzEiLCJpYXQiOjE2MjI2Mjg2NDQsImV4cCI6MTYyMjc1NzYwMH0.ZykzuYy9nEJZT2Qt9g0wS9zKb2FMevL6sGQOfMbwgVyFzVMDXQ42Zxx5pWYtTgQ0xPQkMTeXTH3bCp0qxxu3Lw"
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
                Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmYzk4NjEyYi1jMGMyLTQ3OTItYjJhYy1hY2I3ZGQ2NTU1YzEiLCJpYXQiOjE2MjI2Mjg2NDQsImV4cCI6MTYyMjc1NzYwMH0.ZykzuYy9nEJZT2Qt9g0wS9zKb2FMevL6sGQOfMbwgVyFzVMDXQ42Zxx5pWYtTgQ0xPQkMTeXTH3bCp0qxxu3Lw"
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

export function getDailyRecipes(pageNo) {
    return {
        type: "RECIPE_GET_DAILY",
        payload: axios(env.BASE_PATH + "recipe-service/recipes/topDaily", {
            method: "GET",
            params: { pageNo }
        })
    };
}
