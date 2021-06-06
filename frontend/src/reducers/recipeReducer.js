
import { getUser } from 'api/auth'
import { getMimeTypes } from 'components/common/imageUtility'

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
        comments: [],
        files: {},
        hasData: true,
        preparationLevel: null,
        group: null,
        season: null,
        preparationMethod: null
    },
    recipes: [],
    hidden: false,
    reviewOfUser: {
        rating: 0
    },
    dailyRecipes: [],
    mostPopularRecipes: []
};

const categoryFields = {
    "preparationLevel": "Tezina pripreme",
    "group": "Grupa jela",
    "season": "Sezona",
    "preparationMethod": "Nacin pripreme"
}

const mimeType = getMimeTypes()

const prepareCategories = (categories) => {
    var obj = {}
    Object.keys(categoryFields).forEach(element => {
        var category = categories.find(x => x.categoryType.name === categoryFields[element])
        if (category)
            obj[element] = { value: category.id, label: category.name }
    });
    return obj
}

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
        case "RECIPE_PUSH_FILE":
            var files = state.recipe.files
            var name = action.payload.name
            files[name] = new File([action.payload.file.fileBlob], name, { type: mimeType[name.split('.').pop()] })
            return Object.assign({}, state, {
                recipe: {
                    ...state.recipe,
                    files,
                },
            });
            break;
        case "RECIPE_GET_ALL_FULFILLED":
            var list = action.payload.data._embedded ? [...action.payload.data._embedded.recipeViewList] : []
            return Object.assign({}, state, {
                ...state,
                recipes: list
            });
            break;
        case "RECIPE_CLEAR_STATE":
            return Object.assign({}, state, {
                ...initialState
            });
            break;
        case "RECIPE_GET_FULFILLED":
            const { images, method } = action.payload.data

            return Object.assign({}, state, {
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    ...action.payload.data,
                    activeImage: images.length > 0 ? images[0].image : null,
                    ...prepareCategories(action.payload.data.categories),
                    ingredients: action.payload.data.ingredients.map(x => { return { ...x, value: x.id, label: x.name } }),
                    method: method ? method.replaceAll('$', '\n') : ""
                }),
            });
            break;
        case "RECIPE_GET_REJECTED":
            return Object.assign({}, state, {
                ...state,
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    hasData: false
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
        case "RECIPE_POST_LIKE":
            if (action.payload)
                return Object.assign({}, state, {
                    reviewOfUser: { ...state.reviewOfUser, is_liked: action.payload.data.is_liked }
                });
            break;
        case "RECIPE_POST_RATING":
            if (action.payload)
                return Object.assign({}, state, {
                    recipe: {
                        ...state.recipe,
                        rating: action.payload.data.recipeRating,
                    },
                    reviewOfUser: { ...state.reviewOfUser, rating: action.payload.data.rating },
                });
            return state;
            break;
        case "RECIPE_POST_COMMENT":
            return Object.assign({}, state, {
                ...state,
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    comments: [action.payload.data, ...state.recipe.comments]
                }),
                reviewOfUser: { ...state.reviewOfUser, comment: action.payload.data }
            });
            break;
        case "RECIPE_EDIT_COMMENT":
            return Object.assign({}, state, {
                ...state,
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    comments: state.recipe.comments.map(function (item) { return item.reviewId == action.payload.data.reviewId ? action.payload.data : item; })
                }),
            });
            break;
        case "RECIPE_GET_REVIEWS_FULFILLED":
            if (action.payload.data._embedded) {
                var com;
                var tempArr = [...state.recipe.comments, ...action.payload.data._embedded.reviewViewList];
                if (getUser()) {
                    tempArr = tempArr.filter(x => {
                        if (x.author.id === getUser().id)
                            com = x;
                        return x.author.id !== getUser().id
                    })
                    if (com) {
                        tempArr.unshift(com)
                    }
                }
                return Object.assign({}, state, {
                    ...state,
                    recipe: Object.assign({}, state.recipe, {
                        ...state.recipe,
                        comments: [...tempArr]
                    }),
                });

            }
            return state;
            break;
        case "RECIPE_GET_MOST_POPULAR_FULFILLED":
            if (action.payload.data._embedded)
                return Object.assign({}, state, {
                    ...state,
                    mostPopularRecipes: action.payload.data._embedded.recipeViewList
                });
            return state;
            break;
        case "RECIPE_GET_DAILY_FULFILLED":
            if (action.payload.data._embedded)
                return Object.assign({}, state, {
                    ...state,
                    dailyRecipes: action.payload.data._embedded.recipeViewList
                });
            return state;
            break;
        case "RECIPE_GET_REVIEW":
            if (action.payload)
                return Object.assign({}, state, {
                    ...state,
                    reviewOfUser: action.payload
                });
            return state;
            break;
        case "RECIPE_GET_REVIEW_REJECTED":
            return Object.assign({}, state, {
                ...state,
                reviewOfUser: {}
            });
            break;
        case "RECIPE_DELETE_REVIEW":
            return Object.assign({}, state, {
                recipe: Object.assign({}, state.recipe, {
                    ...state.recipe,
                    comments: state.recipe.comments.filter(x => x.reviewId != action.payload),
                }),
                reviewOfUser: { ...state.reviewOfUser, comment: null }
            });
            break;
        case "RECIPE_GET_MOST_POPULAR_REJECTED":
            return state
            break;
        default:
            return state;
            break;
    }
}

export default recipeReducer;
