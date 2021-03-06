import React from 'react'
import Requests from 'api/requests'
import { env } from 'configs/env'
import auth from 'api/auth'

class DietApi extends React.Component {

    getDiets = (cb, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH  + "recipe-service/diets", { params: params }, response =>{ cb(response.data); }, (error) => { cb(null, error); });
    }

    getPublicDiet = (cb, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH  + "recipe-service/diets/public", { params: params }, response =>{ cb(response.data); }, (error) => { cb(null, error); });
    }

    getPrivateDiet = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendGetPrivateDietRequest);
    }

    createDiet = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendPostDietRequest);
    }

    updateDiet = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendPatchUpdateDiet);
    }

    deleteDiet = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendDeleteDietRequest);
    }

    sendGetPrivateDietRequest = (cb, token, params) => {
        let parameters = {
            params: params, 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendGetRequest(cb, env.BASE_PATH + "recipe-service/diets/private", parameters, 
        response =>cb(response.data), err => cb(null, err));
    }

    sendPostDietRequest = (cb, token, params) => {
        Requests.sendPostRequest(cb, env.BASE_PATH + "recipe-service/diets", params, Requests.getAuthorizationHeader(token),
            response =>{
                cb(response);
            }, err => {
                cb(null, err)
            });
    }

    sendPatchUpdateDiet = (cb, token, params) => {
        var id = params.id;
        delete params["id"];

        Requests.sendPatchRequest(cb, env.BASE_PATH + "recipe-service/diets", params, Requests.getAuthorizationHeader(token),
            response =>cb(response), err => cb(null, err), id);
    }

    sendDeleteDietRequest = (cb, token, params) => {
        let parameters = {
            params: params,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendDeleteRequest(cb, env.BASE_PATH + "recipe-service/diets", parameters, 
        response =>{ cb(response.data, null); }, err => {cb(null, err)});
    }
}

export default new DietApi();
