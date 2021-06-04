import { userIsLoggedIn } from 'api/auth'
import Logo from 'assets/images/food-logo.ico'
import NotificationBell from 'components/notifications/notificationBell'
import React from 'react'
import { Form, FormControl, Nav, Navbar } from 'react-bootstrap'
import { LinkContainer } from "react-router-bootstrap"
import { withRouter } from 'react-router-dom'

function Menu(props) {

    const handleSearch = event => {
        event.preventDefault();
        const formDataObj = Object.fromEntries(new FormData(event.target).entries());
        if (props.handleSearchChange) props.handleSearchChange(formDataObj.search);
    }

    return (
        <div className="menuContainer">
            <Navbar>
                <LinkContainer to="/">
                    <Navbar.Brand>
                        <div className="logo">
                            <img src={Logo} alt="Svg Gavel Icon" />
                            CRUMBS APP
                        </div>
                    </Navbar.Brand>
                </LinkContainer>
                <Form noValidate inline onSubmit={handleSearch}>
                    <FormControl type="text" name="search" placeholder="Try enter: Cheese" defaultValue={props.initial} className="mr-sm-2" />
                    <i className="fa fa-search" aria-hidden="true"></i>
                </Form>
                <Nav className="mr-auto" defaultActiveKey="/my-account">
                    <LinkContainer to={'/'} exact>
                        <Nav.Link active={window.location.pathname.length == 0}>Home</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/browse">
                        <Nav.Link active={window.location.pathname.includes("/browse")}>Recipes</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/diets">
                        <Nav.Link active={window.location.pathname.includes("/diets")}>Diets</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/people">
                        <Nav.Link active={window.location.pathname.includes("/people")}>People</Nav.Link>
                    </LinkContainer>
                    {userIsLoggedIn() ? <NotificationBell {...props} /> : null}
                </Nav>
            </Navbar>
        </div>
    )
}

export default withRouter(Menu);
