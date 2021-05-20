const initialState = {
    recipe: {
        title: "Best Pancake Ever",
        method: "Bjelanjke istuci u snijeg. Zutanjke izmutiti s secerom, dodati brasno, kakao i prasak i mijesati dok se dobije glatka smjesa. Dodati 3 zlice snijega od bjelanjka i lagano mijesati da se dobije lijepa smjesa, zatim sve izliti u ostatak bjelanjaka i jos lagano mijesati dok smjesa bude jednolicna.",
        ingredients: [["jaje", "brasno", "ulje", "maslac", "cokolada"], ["mlijeko"]],
        comment: "Odlican recept!",
        profileImages: [],
        imagesError: "",
        initialImages: {}
    }
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
        case "RECIPE_GET_FULFILLED":
            return Object.assign({}, state, {
                recipe: Object.assign({}, state.recipe, {
                    ...initialState.recipe,
                    ...action.payload.data,
                }),
            });
            break;
        default:
            return state;
            break;
    }
}

export default recipeReducer;