import React, { useState, useEffect } from 'react'
import { withRouter } from 'react-router-dom'
import { Navbar, Nav, Form, FormControl, NavDropdown } from 'react-bootstrap'
import { LinkContainer } from "react-router-bootstrap"
import Logo from 'assets/images/food-logo.ico'
import NotificationBell from 'components/notifications/notificationBell'

function Menu(props) {

    const [initialSearch, setInitialSearch] = useState("");

    const handleSearch = event => {
        event.preventDefault();
        const formData = new FormData(event.target),
        formDataObj = Object.fromEntries(formData.entries());
        if (props.handleSearchChange) props.handleSearchChange(formDataObj.search);
    }

    useEffect(() => {
        if (props.initial) setInitialSearch(props.initial);
    }, [props]);

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
                        <Nav.Link active={!window.location.pathname.includes("/single-product") && !window.location.pathname.includes("/my-account")}>Home</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/browse">
                        <Nav.Link active={window.location.pathname.includes("/single-product")}>Recipes</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/my-account">
                        <Nav.Link active={window.location.pathname.includes("/my-account")}>My account</Nav.Link>
                    </LinkContainer>
                    {"userIsLoggedIn()" ? <NotificationBell {...props}/> : null}
                </Nav>
            </Navbar>
        </div>
    )
}

export default withRouter(Menu);
