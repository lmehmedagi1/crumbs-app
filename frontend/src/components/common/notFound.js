import React from 'react'
import { Button } from 'react-bootstrap'
import { Link } from 'react-router-dom'
import Logo from 'assets/images/food-logo.ico'
import { MdWarning } from "react-icons/md";

function NotFound(props) {
    return (
        <div>
            <div className="notFoundContainer text-secondary text-center text-muted ">
                <Link className="footerLink" to={'/'}>
                    <div className="logo">
                        <img src={Logo} alt="Logo Icon" />
                        CRUMBS APP
                    </div>
                </Link>
                <MdWarning style={{ fontSize: "20rem" }} />
                <div>
                    Ooops! Looks like this page is Not Found!
                </div>
                <div>
                    {props.history ? <Button variant="info" onClick={() => props.history.push({ pathname: '/' })}>
                        GO BACK
                    </Button> : null}
                </div>
            </div>
        </div>
    )
}

export default NotFound;
