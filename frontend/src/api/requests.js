import React from 'react'
import axios from 'axios'


class Requests extends React.Component {

    constructor() {
        super();
    }

    getCookieHeader = () => {
        return {
            withCredentials: true,
            timeout: 5000,
            headers: { "Access-Control-Allow-Origin": "*", 'Access-Control-Allow-Credentials': true, 'Content-Type': 'application/json' }
        }
    }

    getAuthorizationHeader = (token) => {
        return {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        }
    }

    handleError = (error, cb, failureCb) => {
        if (error.response == null)
            cb("Please check your internet connection!", "warning", null);
        else if (failureCb != null)
            failureCb(error.response.data.message);
        else
            cb(error.response.data.message, "warning", null);
    }

    sendPostRequest(cb, url, params, headers, successCb, failureCb) {
        axios
            .post(url, params, headers)
            .then((response) => { successCb(response); })
            .catch(error => { if (failureCb && error.response) failureCb(error.response.data.error); else failureCb(error.message) });
    }

    sendGetRequest(cb, url, params, successCb, failureCb) {
        axios
            .get(url, params)
            .then((response) => { successCb(response); })
            .catch(error => { if (failureCb && error.response) failureCb(error.response.data.message) });
    }

    sendPutRequest(cb, url, params, headers, successCb, failureCb) {
        axios
            .put(url, params, headers)
            .then((response) => { successCb(response); })
            .catch(error => { if (failureCb && error.response) failureCb(error.response.data.message) });
    }

    sendDeleteRequest(cb, url, params, successCb, failureCb) {
        axios
            .delete(url, params)
            .then((response) => { successCb(response); })
            .catch(error => { this.handleError(error, cb, failureCb); });
    }
}

export default new Requests();
