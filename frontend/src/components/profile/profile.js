import { userHasPermission, userIsLoggedIn } from 'api/auth'
import profileApi from 'api/profile'
import Alert from 'components/alert/alert'
import { CustomImage } from 'components/common/customImage'
import Menu from 'components/common/menu'
import EditProfileModal from 'components/profile/editProfile'
import RecipeForm from 'components/recipe/recipeForm'
import DietForm from 'components/diet/dietForm'
import AboutTab from 'components/profile/tabs/AboutTab'
import DietsTab from 'components/profile/tabs/DietTab'
import LikesTab from 'components/profile/tabs/LikesTab'
import RecipesTab from 'components/profile/tabs/RecipesTab'
import SubscriptionsTab from 'components/profile/tabs/SubscriptionsTab'
import ScrollButton from 'components/utility/scrollButton'
import React, { useEffect, useState } from 'react'
import { Nav, Row, Spinner, Tab } from 'react-bootstrap'
import { withRouter } from 'react-router-dom'

function Profile(props) {

    const [loading, setLoading] = useState(false);

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    const [activeTab, setActiveTab] = useState("about");
    const [tableKey, setTableKey] = useState(7);

    const [user, setUser] = useState({ firstName: '', lastName: '' });
    const [isSubscribed, setIsSubscribed] = useState(false);

    const [showEditModal, setShowEditModal] = useState(false);
    const [showRecipeModal, setShowRecipeModal] = useState(false);
    const [showDietModal, setShowDietModal] = useState(false);

    useEffect(() => {
        readLocationPathname();
        if (props.location.state && props.location.state.activeKey) setActiveTab(props.location.state.activeKey);
        updateState(window.location.pathname.split('/')[3]);
    }, []);

    useEffect(() => {
        const newTableKey = tableKey * 89;
        setTableKey(newTableKey);
    }, [activeTab]);

    const readLocationPathname = () => {
        let values = window.location.pathname.split('/');
        let userId = values[2];
        let tab = values[3];
        loadUser(userId, tab);
    }

    const loadUser = (id, tab) => {

        setLoading(true);
        profileApi.getUserInfo((data) => {
            setUser(data);
            setActiveTab(tab);
            const newTableKey = tableKey * 89;
            setTableKey(newTableKey);
            setLoading(false);
        }, { id: id })

        if (userIsLoggedIn() && !userHasPermission(user.id)) {
            profileApi.checkIfUserIsSubscribed((data) => {
                setIsSubscribed(data);
            }, { id: id }, props.getToken(), props.setToken);
        }
    }

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    const updateState = (active) => {
        if (activeTab == active) return;
        props.history.replace('/profile/' + window.location.pathname.split('/')[2] + '/' + active, { activeKey: active });
        setActiveTab(active);
    }

    const getSubscriptionButtonText = () => {
        if (!isSubscribed) return "Subscribe";
        return "Unsubscribe";
    }

    const handleSubscribeClick = () => {
        setLoading(true);
        setIsSubscribed(value => !value);
        profileApi.subscribe(() => {
            setLoading(false);
        }, { id: user.id }, props.getToken(), props.setToken);
    }

    const handleProfileUpdate = () => {
        readLocationPathname();
        setShowEditModal(false);
    }

    const update = (id) => {
        props.history.replace('/profile/' + id + '/about', { activeKey: 'about' });
        loadUser(id, 'about');
    }

    const onSuccessAdded = () => {
        setShowDietModal(false);
        setShowRecipeModal(false);
        const newTableKey = tableKey * 89;
        setTableKey(newTableKey);
        setLoading(false);
    }

    const handleRowClick = (id, type) => {
        if (type == "subscribers" || type == "subscriptions") {
            props.history.replace('/profile/' + id + '/' + activeTab, { activeKey: activeTab });
            loadUser(id, activeTab);
        }
        else if (type.toLowerCase().includes("recipe")) {
            props.history.push({
                pathname: '/recipe/' + id
            });
        }
        else if (type.toLowerCase().includes("diet")) {
            props.history.push({
                pathname: '/diet/' + id
            });
        }
        ScrollButton.scrollToTop();
    }

    return (
        <div className={loading ? "blockedWait" : ""}>
            <div className={loading ? "blocked" : ""}>
                <Menu handleSearchChange={handleSearchChange} update={update} {...props} />
                <Alert message={message} showAlert={show} variant={variant} onShowChange={setShow} />
                <div className="profileContainer">
                    <div className="profileHeader">
                        <div>
                            <CustomImage imageId={user.avatar} className="avatarWrapper" alt="User avatar" />
                            <p>{user && user.firstName} {user && user.lastName}</p>
                        </div>
                        <div className="buttonWrapper">
                            {userHasPermission(user.id) ?
                                <button onClick={() => setShowEditModal(true)} className="editButton">Edit</button>
                                :
                                <button disabled={!userIsLoggedIn()} onClick={handleSubscribeClick} className={isSubscribed ? "unsubscribeButton" : "subscribeButton"}>{getSubscriptionButtonText()}</button>
                            }
                            {!userIsLoggedIn() ? <p>You have to be logged in to subscribe</p> : null}
                        </div>
                    </div>
                    <div className="profileBody">
                        <Tab.Container id="left-tabs-example" defaultActiveKey={activeTab} onSelect={updateState}>
                            <Row>
                                <Nav variant="tabs">
                                    <Nav.Item>
                                        <Nav.Link eventKey="about" active={activeTab == "about"}><i className="fa fa-user" aria-hidden="true" /> About</Nav.Link>
                                    </Nav.Item>
                                    <Nav.Item>
                                        <Nav.Link eventKey="recipes" active={activeTab == "recipes"}><i className="fa fa-spoon" aria-hidden="true" /> Recipes</Nav.Link>
                                    </Nav.Item>
                                    <Nav.Item>
                                        <Nav.Link eventKey="diets" active={activeTab == "diets"}><i className="fa fa-list" aria-hidden="true" /> Diets</Nav.Link>
                                    </Nav.Item>
                                    <Nav.Item>
                                        <Nav.Link eventKey="subscriptions" active={activeTab == "subscriptions"}><i className="fa fa-users" aria-hidden="true" /> Subscriptions</Nav.Link>
                                    </Nav.Item>
                                    {userHasPermission(user.id) ?
                                        <Nav.Item>
                                            <Nav.Link eventKey="likes" active={activeTab == "likes"}><i className="fa fa-heart" aria-hidden="true" /> Likes</Nav.Link>
                                        </Nav.Item>
                                        : null}
                                    {loading ? <Spinner className="spinner" animation="border" role="status" /> : null}
                                </Nav>
                            </Row>
                            <Row>
                                <Tab.Content key={tableKey}>
                                    <Tab.Pane eventKey="about" active={activeTab == "about"}>
                                        <AboutTab user={user && user} />
                                    </Tab.Pane>
                                    <Tab.Pane eventKey="recipes" active={activeTab == "recipes"}>
                                        <RecipesTab key={tableKey} activeTab={activeTab} tab={activeTab} userId={user && user.id} handleRowClick={handleRowClick} setLoading={setLoading} setShow={setShow} getToken={props.getToken} setToken={props.setToken} setMessage={setMessage} setVariant={setVariant} />
                                    </Tab.Pane>
                                    <Tab.Pane eventKey="diets" active={activeTab == "diets"}>
                                        <DietsTab key={tableKey} activeTab={activeTab} tab={activeTab} userId={user && user.id} handleRowClick={handleRowClick} setLoading={setLoading} setShow={setShow} setMessage={setMessage} setVariant={setVariant} getToken={props.getToken} setToken={props.setToken} />
                                    </Tab.Pane>
                                    <Tab.Pane eventKey="subscriptions" active={activeTab == "subscriptions"}>
                                        <SubscriptionsTab activeTab={activeTab} key={tableKey} userId={user && user.id} handleRowClick={handleRowClick} setLoading={setLoading} setShow={setShow} getToken={props.getToken} setToken={props.setToken} setMessage={setMessage} setVariant={setVariant} />
                                    </Tab.Pane>
                                    <Tab.Pane eventKey="likes" active={activeTab == "likes"}>
                                        <LikesTab activeTab={activeTab} key={tableKey} userId={user && user.id} setShow={setShow} setMessage={setMessage} setVariant={setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={setLoading} />
                                    </Tab.Pane>
                                </Tab.Content>
                            </Row>
                        </Tab.Container>

                        {userHasPermission(user.id) && activeTab == "recipes" ? <button className="addButton" onClick={() => { setShowRecipeModal(true) }}> ADD NEW RECIPE </button> : null}
                        {userHasPermission(user.id) && activeTab == "diets" ? <button className="addButton" onClick={() => { setShowDietModal(true) }}> ADD NEW DIET </button> : null}
                    </div>
                </div>
                <EditProfileModal showModal={showEditModal} handleCloseEditModal={() => setShowEditModal(false)} getToken={props.getToken} setToken={props.setToken} handleProfileUpdate={handleProfileUpdate} />
                <RecipeForm show={showRecipeModal} title="Add Recipe" onHide={() => setShowRecipeModal(false)} onSuccess={onSuccessAdded} getToken={props.getToken} setToken={props.setToken} />
                <DietForm show={showDietModal} title="Add Diet" onHide={() => setShowDietModal(false)} onSuccess={onSuccessAdded} getToken={props.getToken} setToken={props.setToken} />
            </div>

        </div>
    )
}

export default withRouter(Profile);
