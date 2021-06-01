const initialState = {
    recipe: {
        title: "",
        method: "",
        ingredients: [],
        description: "",
        profileImages: [],
        imagesError: "",
        initialImages: {},
        categories: [],
        rating: 0,
        comments: []
    },
    hidden: false,
    reviewOfUser: {
        rating: 0
    },
    dailyRecipes: [],
    mostPopularRecipes: []
};

const recipeReducer = (state = { ...initialState }, action) => {
    switch (action.type) {
        case "RECIPE_SET_STATE":
            return Object.assign({}, state, {
                recipe: {
                    ...state.recipe,
                    ...action.payload,
                },
            });
            break;
        case "RECIPE_CLEAR_STATE":
            return Object.assign({}, state, {
                recipe: {title: "",
                        method: "",
                        ingredients: [],
                        description: "",
                        profileImages: [],
                        imagesError: "",
                        initialImages: {},
                        categories: [],
                        comments: []
                    },
                hidden: false,
                reviewOfUser: {},
                dailyRecipes: [],
                mostPopularRecipes: []
            });
            break;
        case "RECIPE_GET_FULFILLED":
            return Object.assign({}, state, {
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    ...action.payload.data,
                }),
            });
            break;
        case "RECIPE_GET_RATING_FULFILLED":
            return Object.assign({}, state, {
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    rating: action.payload.data,
                }),
            });
            break;
        case "RECIPE_POST_LIKE_FULFILLED":
            return Object.assign({}, state, {
                reviewOfUser: {...state.reviewOfUser, is_liked: action.payload.data.is_liked}
            });
            break;
        case "RECIPE_POST_RATING_FULFILLED":
            return Object.assign({}, state, {
                reviewOfUser: {...state.reviewOfUser, rating: action.payload.data.rating}
            });
            break;
        case "RECIPE_POST_COMMENT_FULFILLED":
            return Object.assign({}, state, {
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    comments: [action.payload.data, ...state.recipe.comments]
                }),
                reviewOfUser: {...state.reviewOfUser, comment: action.payload.data}
            });
            break;
        case "RECIPE_EDIT_COMMENT_FULFILLED":
            return Object.assign({}, state, {
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    comments: state.recipe.comments.map(function(item) { return item.reviewId == action.payload.data.reviewId ? action.payload.data : item; })
                }),
            });
            break;
        case "RECIPE_GET_REVIEWS_FULFILLED":
            if(action.payload.data._embedded)
                return Object.assign({}, state, {
                    recipe: Object.assign({}, state.recipe, {
                        ...state.recipe,
                        comments: [...state.recipe.comments, ...action.payload.data._embedded.reviewViewList].filter(x => x),
                    }),
                });
            break;
        case "RECIPE_GET_MOST_POPULAR_FULFILLED":
            return Object.assign({}, state, {
                ...state,
                mostPopularRecipes: action.payload.data._embedded.recipeViewList
            });
            break;
        case "RECIPE_GET_DAILY_FULFILLED":
            return Object.assign({}, state, {
                ...state,
                dailyRecipes: action.payload.data._embedded.recipeViewList
            });
            break;
        case "RECIPE_GET_REVIEW_FULFILLED":
            return Object.assign({}, state, {
                ...state,
                reviewOfUser: action.payload.data
            });
            break;
            case "RECIPE_GET_REVIEW_REJECTED":
                return Object.assign({}, state, {
                    ...state,
                    reviewOfUser: {}
                });
                break;
        case "RECIPE_GET_MOST_POPULAR_REJECTED":
            // console.log("idemoo", action.payload)
            // return Object.assign({}, state, {
            //     ...state,
                
            // });
            return state
            break;
        default:
            return state;
            break;
    }
}

export default recipeReducer;
