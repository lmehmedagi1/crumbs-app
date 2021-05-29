import React, { useState, useEffect } from 'react'
import { withRouter } from 'react-router-dom'
import { Tab, Nav, Row, Spinner } from 'react-bootstrap'

import { getUser, userIsLoggedIn } from 'api/auth'
import Alert from 'components/alert/alert'
import Menu from 'components/common/menu'


import AboutTab from 'components/profile/tabs/AboutTab'
import RecipesTab from 'components/profile/tabs/RecipesTab'
import DietsTab from 'components/profile/tabs/DietTab'
import SubscriptionsTab from 'components/profile/tabs/SubscriptionsTab'
import LikesTab from 'components/profile/tabs/LikesTab'

import EditProfileModal from 'components/profile/editProfile'

import profileApi from 'api/profile'

const imagePlaceholder = "https://www.firstfishonline.com/wp-content/uploads/2017/07/default-placeholder-700x700.png";

function Profile(props) {

    const [loading, setLoading] = useState(false);

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    const [activeTab, setActiveTab] = useState("about");

    const [user, setUser] = useState({firstName: '', lastName: ''});
    const [isSubscribed, setIsSubscribed] = useState(false);

    const [showEditModal, setShowEditModal] = useState(false);

    useEffect(() => {
        readLocationPathname();
        if (props.location.state && props.location.state.activeKey) setActiveTab(props.location.state.activeKey);
        updateState(window.location.pathname.split('/')[3]);
    }, []);

    const readLocationPathname = () => {
        let values = window.location.pathname.split('/');
        let userId = values[2];
        let tab = values[3];
        loadUser(userId, tab);
    }

    const loadUser = (id, tab) => {

        profileApi.getUserInfo((data) => {
            setUser(data);
            setActiveTab(tab);
        }, {id: id})

        if (userIsLoggedIn() && !isMyAccount()) {
            profileApi.checkIfUserIsSubscribed((data) => {
                console.log("Is subbed: ", data);
                setIsSubscribed(data);
            }, {id: id}, props.getToken(), props.setToken);
        }
    }

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/shop',
            state: { search: search }
        });
    }

    const updateState = (active) => {
        if (activeTab == active) return;
        props.history.replace('/profile/' + window.location.pathname.split('/')[2] + '/' + active, { activeKey: active });
        setActiveTab(active);
    }

    const isMyAccount = () => {
        if (!userIsLoggedIn()) return false;
        return getUser().id == user.id;
    }

    const getSubscriptionButtonText = () => {
        if (!isSubscribed) return "Subscribe";
        return "Unsubscribe";
    }

    const handleSubscribeClick = () => {
        // To do: change button and call sub/unsub function
    }

    const handleProfileUpdate = () => {
        readLocationPathname();
        setShowEditModal(false);
    }

    return (
        <div className={loading ? "blockedWait" : ""}>
        <div className={loading ? "blocked" : ""}>
            <Menu handleSearchChange={handleSearchChange} {...props}/>
            <Alert message={message} showAlert={show} variant={variant} onShowChange={setShow} />
            <div className="profileContainer">
                <div className="profileHeader">
                    <div>
                        <div className="avatarWrapper"><img src={imagePlaceholder} alt="Avatar"/></div>
                        <p>{user && user.firstName} {user && user.lastName}</p>
                    </div>
                    <div className="buttonWrapper">
                        {isMyAccount() ?
                        <button onClick={() => setShowEditModal(true)} className="editButton">Edit</button>
                        :
                        <button onClick={handleSubscribeClick} className={isSubscribed ? "unsubscribeButton" : "subscribeButton"}>{getSubscriptionButtonText()}</button>
                        }
                    </div>
                </div>
                <div className="profileBody">
                <Tab.Container id="left-tabs-example" defaultActiveKey={activeTab} onSelect={updateState}>
                <Row>
                    <Nav variant="tabs">
                        <Nav.Item>
                        <Nav.Link eventKey="about"  active={activeTab == "about"}><i className="fa fa-user" aria-hidden="true"/> About</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                        <Nav.Link eventKey="recipes" active={activeTab == "recipes"}><i className="fa fa-spoon" aria-hidden="true"/> Recipes</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                        <Nav.Link eventKey="diets"   active={activeTab == "diets"}><i className="fa fa-list" aria-hidden="true"/> Diets</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                        <Nav.Link eventKey="subscriptions" active={activeTab == "subscriptions"}><i className="fa fa-users" aria-hidden="true"/> Subscriptions</Nav.Link>
                        </Nav.Item>
                        { isMyAccount() ? 
                        <Nav.Item>
                        <Nav.Link eventKey="likes"   active={activeTab == "likes"}><i className="fa fa-heart" aria-hidden="true"/> Likes</Nav.Link>
                        </Nav.Item>
                        : null }
                        {loading ? <Spinner className="spinner" animation="border" role="status"/> : null}
                    </Nav>
                </Row>
                <Row>
                    <Tab.Content>
                        <Tab.Pane eventKey="about"   active={activeTab == "about"}>
                        <AboutTab user={user && user} /> 
                        </Tab.Pane>
                        <Tab.Pane eventKey="recipes" active={activeTab == "recipes"}>
                        <RecipesTab tab={activeTab} userId={user && user.id} setShow={setShow} setMessage={setMessage} setVariant={setVariant} setLoading={setLoading}/>
                        </Tab.Pane>
                        <Tab.Pane eventKey="diets"   active={activeTab == "diets"}>
                        <DietsTab  tab={activeTab} userId={user && user.id} setShow={setShow} setMessage={setMessage} setVariant={setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={setLoading}/>
                        </Tab.Pane>
                        <Tab.Pane eventKey="subscriptions" active={activeTab == "subscriptions"}>
                        <SubscriptionsTab userId={user && user.id} setShow={setShow} setMessage={setMessage} setVariant={setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={setLoading} />
                        </Tab.Pane>
                        <Tab.Pane eventKey="likes"   active={activeTab == "likes"}>
                        <LikesTab userId={user && user.id} setShow={setShow} setMessage={setMessage} setVariant={setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={setLoading}/>
                        </Tab.Pane>
                    </Tab.Content>
                </Row>
                </Tab.Container>
                </div>
            </div>
            <EditProfileModal showModal={showEditModal} handleCloseEditModal={() => setShowEditModal(false)} getToken={props.getToken} setToken={props.setToken} handleProfileUpdate={handleProfileUpdate} />
        </div>
        </div>
    )
}

export default withRouter(Profile);
