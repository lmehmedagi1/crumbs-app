import React from 'react'
import Requests from 'api/requests'
import auth from 'api/auth'
import { env } from 'configs/env'

class NotificationApi extends React.Component {

    sendGetUserNotificationsRequest = (cb, token, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH + "notification-service/notifications", Requests.getAuthorizationHeader(token), 
        response =>cb(response.data), err => cb(null, err));
    }
    
    sendMarkNotificationAsReadRequest = (cb, token, params) => {
        Requests.sendPostRequest(cb, env.BASE_PATH + "notification-service/notifications/mark", params, Requests.getAuthorizationHeader(token), 
        response =>cb(response.data), err => cb(null, err));
    }

    sendMarkAllAsReadRequest = (cb, token, params) => {
        Requests.sendPostRequest(cb, env.BASE_PATH + "notification-service/notifications/mark-all-as-read", params, Requests.getAuthorizationHeader(token), 
        response =>cb(response.data), err => cb(null, err));
    }

    sendDeleteNotificationRequest = (cb, token, params) => {
        let parameters = {
            params: params,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendDeleteRequest(cb, env.BASE_PATH + "notification-service/notifications", parameters, 
        response =>{ cb(response.data, null); }, err => {cb(null, err)});
    }

    getUserNotifications = (cb, token, setToken) => {
        auth.forwardRequest(cb, {}, token, setToken, this.sendGetUserNotificationsRequest);
    }

    markAllAsRead = (cb, token, setToken) => {
        auth.forwardRequest(cb, {}, token, setToken, this.sendMarkAllAsReadRequest);
    }

    markNotificationAsReadOrUnread = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendMarkNotificationAsReadRequest);
    }

    deleteNotification = (cb, params, token, setToken) => {
        auth.forwardRequest(cb, params, token, setToken, this.sendDeleteNotificationRequest);
    }
}

export default new NotificationApi();
