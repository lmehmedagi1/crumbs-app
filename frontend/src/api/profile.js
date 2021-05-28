import React from 'react'
import { hostUrl } from 'components/utility/constants'
import Requests from 'api/requests'
import auth, {removeUserSession, setUserSession} from 'api/auth'

class ProfileApi extends React.Component {

    constructor() {
        super();
    }

    sendCheckIfUserIsSubbedRequest = (cb, token, params) => {
        let parameters = {
            params: params, 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendGetRequest(cb, hostUrl + "user-service/account/subscribed", parameters, (response) => { cb(response.data); }, (err) => { cb(null); });
    }

    sendGetUserLikedRecipesRequest = (cb, token, params) => {
        let parameters = {
            params: params, 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendGetRequest(cb, hostUrl + "review-service/reviews/likes/recipes", parameters, (response) => { cb(response.data); }, (err) => { cb(null); });
    }

    sendGetUserLikedDietsRequest = (cb, token, params) => {
        let parameters = {
            params: params, 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendGetRequest(cb, hostUrl + "review-service/reviews/likes/diets", parameters, (response) => { cb(response.data); }, (err) => { cb(null); });
    }

    sendPutUpdateUserInfo = (cb, token, params) => {
        Requests.sendPutRequest(cb, hostUrl + "user-service/account/updateInfo", params, Requests.getAuthorizationHeader(token), 
            (response) => {
                removeUserSession();
                setUserSession(response.data);
                cb();
            }, (err) => {console.log(err)});
    }

    getUserInfo = (cb, params) => {
        Requests.sendGetRequest(cb, hostUrl + "user-service/account/info", {params: params}, (response) => { cb(response.data); }, (err) => { cb(null); });
    }

    checkIfUserIsSubscribed = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendCheckIfUserIsSubbedRequest);
    }

    getUserRecipes = (cb, params) => {
        Requests.sendGetRequest(cb, hostUrl + "recipe-service/recipes/user", {params: params}, (response) => { cb(response.data); }, (err) => { cb(null); });
    } 

    getUserDiets = (cb, params) => {
        Requests.sendGetRequest(cb, hostUrl + "recipe-service/diets/user", {params: params}, (response) => { cb(response.data); }, (err) => { cb(null); });
    } 

    getUserSubscribers = (cb, params) => {
        Requests.sendGetRequest(cb, hostUrl + "user-service/account/subscribers", {params: params}, (response) => { cb(response.data); }, (err) => { cb(null); });
    } 

    getUserSubscriptions = (cb, params) => {
        Requests.sendGetRequest(cb, hostUrl + "user-service/account/subscriptions", {params: params}, (response) => { cb(response.data); }, (err) => { cb(null); });
    } 

    getUserLikedRecipes = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendGetUserLikedRecipesRequest);
    }

    getUserLikedDiets = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendGetUserLikedDietsRequest);
    }

    updateUserInfo = (cb, params, token, setToken) => {
        console.log("poarams", params)
        auth.forwardRequest(cb, params, token, setToken, this.sendPutUpdateUserInfo);
    }
}

export default new ProfileApi();
