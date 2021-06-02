import React from 'react'
import Requests from 'api/requests'
import auth from 'api/auth'
import { env } from 'configs/env'

class RecipeApi extends React.Component {

    sendPostRecipe = (cb, token, params) => {
        Requests.sendPostRequest(cb, env.BASE_PATH + "recipe-service/recipes", params, Requests.getAuthorizationHeader(token),
            (response) => {
                cb(response);
            }, (err) => {
                cb(null, err)
                console.log(err)
            });
    }

    createRecipe = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendPostRecipe);
    }
}

export default new RecipeApi();
