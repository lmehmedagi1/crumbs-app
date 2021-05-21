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
    
    mostPopularRecipes: []
};

const recipeReducer = (state = { ...initialState }, action) => {
    console.log("idemoo", action.type)
    switch (action.type) {
        case "RECIPE_SET_STATE":
            return Object.assign({}, state, {
                recipe: {
                    ...state.recipe,
                    ...action.payload,
                },
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
        case "RECIPE_GET_REVIEWS_FULFILLED":
            console.log("OCJENNA", action.payload);
            return Object.assign({}, state, {
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    comments: action.payload.data._embedded.reviewList,
                }),
            });
            break;
        case "RECIPE_GET_MOST_POPULAR_FULFILLED":
            console.log("idemoo", action.payload)
            return Object.assign({}, state, {
                ...state,
                mostPopularRecipes: action.payload.data._embedded.recipeViewList
            });
        break;
        default:
            return state;
            break;
    }
}

export default recipeReducer;