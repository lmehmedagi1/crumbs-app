import React from 'react'
import { hostUrl } from 'components/utility/constants'
import Requests from 'api/requests'
import auth from 'api/auth'

class NotificationApi extends React.Component {

    constructor() {
        super();
    }

    sendGetUserNotificationsRequest = (cb, token, params) => {
        Requests.sendGetRequest(cb, hostUrl + "notification-service/notifications", Requests.getAuthorizationHeader(token), 
        (response) => cb(response.data), (err) => cb(null, err));
    }
    
    sendMarkNotificationAsReadRequest = (cb, token, params) => {
        Requests.sendPostRequest(cb, hostUrl + "notification-service/notifications/mark", params, Requests.getAuthorizationHeader(token), 
        (response) => cb(response.data), (err) => cb(null, err));
    }

    sendMarkAllAsReadRequest = (cb, token, params) => {
        Requests.sendPostRequest(cb, hostUrl + "notification-service/notifications/mark-all-as-read", params, Requests.getAuthorizationHeader(token), 
        (response) => cb(response.data), (err) => cb(null, err));
    }

    sendDeleteNotificationRequest = (cb, token, params) => {
        let parameters = {
            params: params,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${token}`
            }
        };
        Requests.sendDeleteRequest(cb, hostUrl + "notification-service/notifications", parameters, 
        (response) => { cb(response.data, null); }, (err) => {cb(null, err)});
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
