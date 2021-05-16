import React, { useEffect, useState } from 'react'
import { NavDropdown, Overlay, OverlayTrigger, Tooltip } from 'react-bootstrap'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import moment from 'moment'

function NotificationBell(props) {

    const [notifications, setNotifications] = useState([]);
    const [notificationsCount, setNotificationsCount] = useState(0);

    const [show, setShow] = useState(false);

    const notificationSound = new Audio(`https://ia800203.us.archive.org/14/items/slack_sfx/flitterbug.mp3`);

    useEffect(() => {
        const socket = new SockJS('hostUrl' + '/ws');
        const stompClient = Stomp.over(socket);
        stompClient.debug = null;

        stompClient.connect({}, () => {
            if (stompClient.connected)
                stompClient.subscribe('/topic/id_usera', (notif) => {
                    receiveNotification(JSON.parse(notif.body))
                });
        });

        if (notifications.length == 0) fetchNotifications();
    }, []);

    useEffect(() => {
        if (notificationsCount == 0)
            document.title = "Crumbs App";
        else
            document.title = "(" + notificationsCount + ") Crumbs App";
    }, [notificationsCount]);

    const receiveNotification = (notification) => {
        let oldNotifications = notifications;
        oldNotifications.unshift(notification);
        setNotifications(oldNotifications);
        setNotificationsCount(c => c + 1);
        notificationSound.play();
    }

    const fetchNotifications = () => {
        // To do: send fetch notifications request
    }

    const markNotification = (notification, cb) => {
        // To do: send mark as read/unread request        
        let oldNotification = notification;
        if (oldNotification.read) setNotificationsCount(c => c + 1);
        else setNotificationsCount(c => c - 1);
        oldNotification.read = !notification.read;
        let oldNotifications = notifications.filter(x => {if (notification.id == x.id) return oldNotification; else return x; });
        setNotifications(oldNotifications);
    }

    const markAllAsRead = () => {
        // To do: send mark all as read request
        setNotificationsCount(0);
    }

    const getBellIcon = () => {
        return <div className="bellIcon">
            <div><i className="fa fa-bell" aria-hidden="true"/></div>
            <div className="count">{notificationsCount > 9 ? "9+" : notificationsCount}</div>
        </div>
    }

    const notificationSelected = (notification) => {
        markNotification(notification, () => {
            if (props.updateProduct == null)
                props.history.push({
                    pathname: '/single-product/' + notification.productId
                });
            else props.updateProduct();
        });
    }

    const timestampToDateTime = timestamp => {
        const longDateTimeFormat = "MMMM Do YYYY, h:mm:ss a";
        return moment.utc(timestamp).local().format(longDateTimeFormat);
    }

    return (
        <div className="notificationBellContainer">
            <NavDropdown title={getBellIcon()} id="nav-dropdown" show={show && notificationsCount > 0}
                onMouseEnter={() => {setShow(true);}} 
                onMouseLeave={() => {setShow(false);}}
                onClick={() => {setShow(true);}} 
                >
                    {notifications.length == 0 ? "No notifications yet." :
                    notifications.map((notification, index) => (
                        <NavDropdown.Item eventKey={index}  onClick={() => notificationSelected(notification)}>
                            <div className={notification.read ? "notification" : "notification unread"}>
                                <div className="content">
                                <h2>{notification.content}</h2>
                                <h3>{timestampToDateTime(notification.time)}</h3>
                                </div>
                                <div>
                                <OverlayTrigger
                                placement="bottom"
                                overlay={
                                    <Tooltip id="button-tooltip-2">Mark as {notification.read ? "un" : ""}read</Tooltip>
                                }
                                >
                                    <button><i onClick={(event) => { event.stopPropagation(); markNotification(notification, null);}} className={notification.read ? "fa fa-envelope" : "fa fa-envelope-open"} aria-hidden="true"/></button>
                                </OverlayTrigger>
                                </div>
                            </div>
                        </NavDropdown.Item>
                    ))}
                    {notificationsCount > 0 ? <button className="markAllButton" onClick={markAllAsRead}> Mark all as read </button> : null}
            </NavDropdown>
        </div>
    )
}

export default NotificationBell;
