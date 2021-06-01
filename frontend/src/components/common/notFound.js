import React from 'react'
import { Button } from 'react-bootstrap'
import { Link } from 'react-router-dom'
import Logo from 'assets/images/food-logo.ico'

function NotFound(props) {
    return (
        <div>
            <div className="notFoundContainer">
                <Link className="footerLink" to={'/'}>
                    <div className="logo">
                        <img src={Logo} alt="Logo Icon" /> 
                        CRUMBS APP
                    </div>
                </Link>
                <p id="errorCode">
                    404
                </p>
                <div>
                    Ooops! Looks like this page is Not Found!
                </div>
                <div>
                    <Button variant="primary" type="submit" onClick={() => props.history.push({ pathname: '/' })}>
                        GO BACK
                    </Button>
                </div>
            </div>
        </div>
    )
}

export default NotFound;
