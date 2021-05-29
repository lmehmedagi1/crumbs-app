import React from 'react'
import { hostUrl } from 'components/utility/constants'
import Requests from 'api/requests'

// return the user data from the session storage
export const getUser = () => {
    const userStr = localStorage.getItem('user');
    if (userStr) return JSON.parse(userStr);
    else return null;
}

// remove the user from the session storage
export const removeUserSession = () => {
    localStorage.removeItem('user');
}

// set the user from the session storage
export const setUserSession = (user) => {
    user.password = null;
    localStorage.setItem('user', JSON.stringify(user));
}

// check if user is logged in
export const userIsLoggedIn = () => {
    return getUser() != null;
}

class Auth extends React.Component {

    constructor() {
        super();
    }

    forwardRequest = (cb, params, token, setToken, functionCb) => {
        if (token == null || token == "") 
            this.refreshToken(cb, token, setToken, params, functionCb);
        else 
            functionCb(cb, token, params);
    }

    refreshToken = (cb, token, setToken, params, successCb) => {

        Requests.sendPostRequest(cb, hostUrl + "auth/refresh-token", {}, Requests.getCookieHeader(), 
            (response) => { 
                token = response.headers.authorization;
                setToken(token);
                successCb(cb, token, params);
            },  
            (message) => {
                removeUserSession();
                cb("Your session has expired, log in again!", "warning", null);
                return;
            }
        );
    }

    login(cb, failCb, values) {
        let url = hostUrl + 'user-service/auth/login';
        let parameters = {
            username: values.username,
            password: values.password
        };
        Requests.sendPostRequest(cb, url, parameters, Requests.getCookieHeader(), 
            (response) => {
                setUserSession(response.data);
                cb(response.headers.authorization);
        }, failCb);
    }

    logout(cb) {
        Requests.sendPostRequest(cb, hostUrl + 'user-service/auth/logout', {}, Requests.getCookieHeader(), 
        (response) => { removeUserSession(); cb(); }, 
        (error) => { removeUserSession(); cb(); });
    }

    register(cb, failCb, values) {
        console.log("REGISTER 2 ", values)
        let url = hostUrl + 'user-service/auth/register';
        let parameters = {
            first_name: values.firstName,
            last_name: values.lastName,
            email: values.email,
            username: values.username,
            gender: 'male',
            phone_number: '062123123',
            password: values.password
        };
        Requests.sendPostRequest(cb, url, parameters, Requests.getCookieHeader(), 
            (response) => {
                if (response.data.length === 0) {
                    failCb("Something went wrong!");
                    return;
                }
                cb(response.data);
        }, failCb);
    }

    confirmRegistration(cb, token) {
        let url = hostUrl + 'user-service/auth/registration-confirmation';
        Requests.sendGetRequest(cb, url, {params: {token: token}}, (response) => {}, null);
    }
}

export default new Auth();
