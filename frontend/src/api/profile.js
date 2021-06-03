import React from 'react'
import Requests from 'api/requests'
import auth, {removeUserSession, setUserSession} from 'api/auth'
import { env } from 'configs/env'

class ProfileApi extends React.Component {

    sendCheckIfUserIsSubbedRequest = (cb, token, params) => {
        let parameters = {
            params: params, 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendGetRequest(cb, env.BASE_PATH + "user-service/account/subscribed", parameters, (response) => { cb(response.data); }, (err) => { cb(null); });
    }

    sendGetUserLikedRecipesRequest = (cb, token, params) => {
        let parameters = {
            params: params, 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendGetRequest(cb, env.BASE_PATH + "review-service/reviews/likes/recipes", parameters, (response) => { cb(response.data); }, (err) => { cb(null); });
    }

    sendGetUserLikedDietsRequest = (cb, token, params) => {
        let parameters = {
            params: params, 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendGetRequest(cb, env.BASE_PATH + "review-service/reviews/likes/diets", parameters, (response) => { cb(response.data); }, (err) => { cb(null); });
    }

    sendPutUpdateUserInfo = (cb, token, params) => {
        Requests.sendPutRequest(cb, env.BASE_PATH + "user-service/account/updateInfo", params, Requests.getAuthorizationHeader(token), 
            (response) => {
                removeUserSession();
                setUserSession(response.data);
                cb();
            }, (err) => {console.log(err)});
    }

    sendPostSubscribeRequest = (cb, token, params) => {
        Requests.sendPostRequest(cb, env.BASE_PATH + "user-service/account/subscribe", params, Requests.getAuthorizationHeader(token), 
            (response) => { cb(response.data); }, (err) => { console.log(err); cb(null); },
        );
    }

    sendGetUserDietsRequest = (cb, token, params) => {
        let parameters = {
            params: params, 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendGetRequest(cb, env.BASE_PATH + "recipe-service/diets/user", parameters, response => cb(response.data), err => cb(null, err));
    }

    getUserInfo = (cb, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH + "user-service/account/info", {params: params}, response => cb(response.data), err => cb(null, err));
    }

    checkIfUserIsSubscribed = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendCheckIfUserIsSubbedRequest);
    }

    getUserRecipes = (cb, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH + "recipe-service/recipes/user", {params: params}, (response) => { cb(response.data); }, (err) => { cb(null); });
    } 

    getUserPublicDiets = (cb, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH + "recipe-service/diets/user", {params: params}, (response) => { cb(response.data); }, (err) => { cb(null); });
    } 

    getUserDiets = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendGetUserDietsRequest);
    } 

    getUserSubscribers = (cb, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH + "user-service/account/subscribers", {params: params}, (response) => { cb(response.data); }, (err) => { cb(null); });
    } 

    getUserSubscriptions = (cb, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH + "user-service/account/subscriptions", {params: params}, (response) => { cb(response.data); }, (err) => { cb(null); });
    } 

    getUserLikedRecipes = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendGetUserLikedRecipesRequest);
    }

    getUserLikedDiets = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendGetUserLikedDietsRequest);
    }

    updateUserInfo = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendPutUpdateUserInfo);
    }

    subscribe = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendPostSubscribeRequest);
    }
}

export default new ProfileApi();
