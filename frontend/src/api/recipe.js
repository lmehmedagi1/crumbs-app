import React from 'react'
import Requests from 'api/requests'
import auth from 'api/auth'
import { env } from 'configs/env'

class RecipeApi extends React.Component {

    sendPostRecipe = (cb, token, params) => {
        Requests.sendPostRequest(cb, env.BASE_PATH + "recipe-service/recipes", params, Requests.getAuthorizationHeader(token),
            response =>{
                cb(response);
            }, err => {
                cb(null, err)
                console.log(err)
            });
    }

    sendPatchComment = (cb, token, params) => {
        var id = params.id;
        delete params["id"];

        Requests.sendPatchRequest(cb, env.BASE_PATH + "review-service/reviews", params, Requests.getAuthorizationHeader(token),
            response =>{
                cb(response);
            }, err => {
                cb(null, err)
                console.log(err)
            }, id);
    }

    sendDeleteEntityReviewRequest = (cb, token, params) => {
        let parameters = {
            params: params,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendDeleteRequest(cb, env.BASE_PATH + "review-service/reviews", parameters,
            response =>{ cb(response.data, null); }, err => { cb(null, err) });
    }

    getEntityReviewForUserRequest = (cb, token, params) => {

        let parameters = {
            params: params,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendGetRequest(cb, env.BASE_PATH + "review-service/reviews/user-review", parameters,
            response =>cb(response.data), err => cb(null, err));
    }

    getEntityReviewForUser = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.getEntityReviewForUserRequest);
    }

    createRecipe = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendPostRecipe);
    }


    postComment = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendPatchComment);
    }

    sendPatchRecipe = (cb, token, params) => {
        var id = params.id;
        delete params["id"];

        Requests.sendPatchRequest(cb, env.BASE_PATH + "recipe-service/recipes", params, Requests.getAuthorizationHeader(token),
            response => cb(response)
            , err => {
                cb(null, err)
                console.log(err)
            }, id);
    }

    patchRecipe = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendPatchRecipe);
    }

    deleteEntityReview = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendDeleteEntityReviewRequest);
    }

    deleteRecipe = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendDeleteRecipeRequest);
    }

    sendDeleteRecipeRequest = (cb, token, params) => {
        let parameters = {
            params: params,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendDeleteRequest(cb, env.BASE_PATH + "recipe-service/recipes", parameters,
            response =>{ cb(response.data, null); }, err => { cb(null, err) });
    }
}

export default new RecipeApi();
