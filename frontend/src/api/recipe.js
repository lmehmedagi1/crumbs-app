import React from 'react'
import { hostUrl } from 'components/utility/constants'
import Requests from 'api/requests'
import auth, { removeUserSession, setUserSession } from 'api/auth'
import { env } from 'configs/env'

class RecipeApi extends React.Component {

    constructor() {
        super();
    }

    sendPostRecipe = (cb, token, params) => {
        Requests.sendPostRequest(cb, env.BASE_PATH + "recipe-service/recipes", params, Requests.getAuthorizationHeader(token),
            (response) => {
                cb(response.data);
            }, (err) => { console.log(err) });
    }

    createRecipe = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendPostRecipe);
    }
}

export default new RecipeApi();
