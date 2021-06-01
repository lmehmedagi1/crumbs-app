import React, { useEffect, useState } from 'react'
import { NavDropdown, Overlay, OverlayTrigger, Tooltip } from 'react-bootstrap'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import moment from 'moment'
import { getUser } from 'api/auth'
import notificationsApi from 'api/notification'

function NotificationBell(props) {

    const [notificationsCount, setNotificationsCount] = useState(0);
    const [notifications, setNotifications] = useState([]);

    const [show, setShow] = useState(false);

    const notificationSound = new Audio(`https://ia800203.us.archive.org/14/items/slack_sfx/flitterbug.mp3`);

    useEffect(() => {
        if (!(getUser() && getUser().id)) return;

        const socket = new SockJS('http://localhost:8084/ws');
        const stompClient = Stomp.over(socket);
        stompClient.debug = null;

        stompClient.connect({}, () => {
            if (stompClient.connected)
                stompClient.subscribe('/topic/' + getUser().id, (notif) => {
                    receiveNotification(JSON.parse(notif.body))
                });
        });

        fetchNotifications();

        return () => {
            stompClient.disconnect(() => {})
        }
    }, []);

    useEffect(() => {
        if (notificationsCount == 0)
            document.title = "Crumbs App";
        else
            document.title = "(" + notificationsCount + ") Crumbs App";
    }, [notificationsCount]);

    const receiveNotification = (notification) => {
        if (notifications.some(n => n.id == notification.notification.id)) return;
        if (notifications == undefined || notifications.length == 0) fetchNotifications(notification.notification);
        else addNotification(notifications, notification.notification);
    }

    const addNotification = (data, notification) => {
        if (data.some(n => n.id === notification.id)) {
            setNotifications(data)
            setNotificationsCount(data.filter(x => x.isRead == false).length);
        }
        else {
            setNotifications([notification, ...data]);
            setNotificationsCount(data.filter(x => x.isRead == false).length + 1);
        }
        notificationSound.play();
    }

    const fetchNotifications = (notif) => {
        notificationsApi.getUserNotifications((data, err) => {
            if (err != null) return;
            if (data == null) data = [];
            if (notif) {
                addNotification(data, notif)
            }
            else {
                setNotifications(data);
                setNotificationsCount(data.filter(x => x.isRead == false).length);
            }
        }, props.getToken(), props.setToken);
    }

    const markNotification = (notification, cb) => {
        notificationsApi.markNotificationAsReadOrUnread((res, err) => {
            let oldNotification = notification;
            if (oldNotification.isRead) setNotificationsCount(c => c + 1);
            else setNotificationsCount(c => c - 1);
            oldNotification.isRead = !notification.isRead;
            let oldNotifications = notifications.filter(x => { if (notification.id == x.id) return oldNotification; else return x; });
            setNotifications(oldNotifications);
            if (cb != null) cb();
        }, { id: notification.id }, props.getToken(), props.setToken);

    }

    const deleteNotification = (notification) => {
        notificationsApi.deleteNotification((res, err) => {
            let newNotifications = notifications.filter(x => { return (notification.id != x.id); });
            setNotifications(newNotifications);
            if (!notification.isRead) setNotificationsCount(c => c - 1);
        }, { id: notification.id }, props.getToken(), props.setToken);
    }

    const markAllAsRead = (event) => {
        event.preventDefault();
        event.stopPropagation();
        notificationsApi.markAllAsRead((res, err) => {
            let oldNotifications = notifications.filter(x => {
                if (x.isRead == false) {
                    let newNotification = x;
                    newNotification.isRead = true;
                    return newNotification;
                } return x;
            });
            setNotifications(oldNotifications);
            setNotificationsCount(0);
        }, props.getToken(), props.setToken);
    }

    const getBellIcon = () => {
        return <div className="bellIcon">
            <div><i className="fa fa-bell" aria-hidden="true" /></div>
            {notificationsCount > 0 ? <div className="count">{notificationsCount > 9 ? "9+" : notificationsCount}</div> : null}
        </div>
    }

    const redirectOnClick = (notification, url) => {
        if ((notification.entityType == "crumbs_user" && window.location.pathname.includes('profile')) || window.location.pathname.includes(notification.entityType + '/'))
            props.update(notification.entityId);
        else {
            props.history.push({
                pathname: url
            });
        }
    }

    const notificationSelected = (notification) => {

        let url = "";
        if (notification.entityType == "crumbs_user") url = "/profile/" + notification.entityId + "/about";
        else if (notification.entityType == "recipe") url = "/recipe/" + notification.entityId;
        else if (notification.entityType == "diet") url = "/diet/" + notification.entityId;

        if (notification.isRead) redirectOnClick(notification, url);
        else {
            markNotification(notification, (res, err) => {
                if (err) return;
                redirectOnClick(notification, url);
            });
        }
    }

    const timestampToDateTime = timestamp => {
        const longDateTimeFormat = "MMMM Do YYYY, h:mm:ss a";
        return moment.utc(timestamp).local().format(longDateTimeFormat);
    }

    const handleMouseDownClick = event => {
        event.preventDefault();
        event.stopPropagation();
    }

    return (
        <div className="notificationBellContainer">
            <NavDropdown title={getBellIcon()} id="nav-dropdown" show={show} onBlur={() => setShow(s => !s)} onFocus={() => setShow(s => !s)} >
                {notifications.length == 0 ? <div className="empty">No notifications yet.</div> :
                    notifications.map((notification, index) => (
                        <NavDropdown.Item eventKey={index} onMouseDown={() => notificationSelected(notification)}>
                            <div className={notification.isRead ? "notification" : "notification unread"}>
                                <div className="content">
                                    <h1>{notification.title}</h1>
                                    <h2>{notification.description}</h2>
                                    <h3>{timestampToDateTime(notification.createdAt)}</h3>
                                </div>
                                <div>
                                    <OverlayTrigger placement="bottom" overlay={
                                        <Tooltip id="button-tooltip-2">Delete</Tooltip>} >
                                        <button onMouseDown={handleMouseDownClick} onClick={(event) => { event.stopPropagation(); event.preventDefault(); deleteNotification(notification); }}><i className="fa fa-trash" aria-hidden="true" /></button>
                                    </OverlayTrigger>
                                    <OverlayTrigger placement="bottom" overlay={
                                        <Tooltip id="button-tooltip-2">Mark as {notification.isRead ? "un" : ""}read</Tooltip>
                                    } >
                                        <button onMouseDown={handleMouseDownClick} onClick={(event) => { event.stopPropagation(); event.preventDefault(); markNotification(notification, null); }}><i className={notification.isRead ? "fa fa-envelope" : "fa fa-envelope-open"} aria-hidden="true" /></button>
                                    </OverlayTrigger>
                                </div>
                            </div>
                        </NavDropdown.Item>
                    ))}
                {notificationsCount > 0 ? <button className="markAllButton" onMouseDown={handleMouseDownClick} onClick={markAllAsRead}> Mark all as read </button> : null}
            </NavDropdown>
        </div>
    )
}

export default NotificationBell;
