import React from 'react'
import { Link } from 'react-router-dom';

function Footer() {
    return (
        <div className="footerContainer">
            <div className="footerColumn">
                <div>
                    CRUMBS
                </div>
                <div>
                    <Link className="footerLink" to={"/"}>
                        About Us
                    </Link>
                </div>
                <div>
                    <Link className="footerLink" to={"/"}>
                        Terms and Conditions
                    </Link>
                </div>
                <div>
                    <Link className="footerLink" to={"/"}>
                        Privacy Policy
                    </Link>
                </div>
            </div>
            <div className="footerColumn">
                <div>
                    GET IN TOUCH
                </div>
                <div>
                    Call Us at +123 797-567-2535
                </div>
                <div>
                    support@crumbs.com
                </div>
                <div>
                    <a className="fa fa-facebook" rel="noopener noreferrer" target="_blank" href="https://www.facebook.com/arslan.turkusic.5/"></a>
                    <a className="fa fa-instagram" rel="noopener noreferrer" target="_blank" href="https://www.instagram.com/arslan"></a>
                    <a className="fa fa-twitter" rel="noopener noreferrer" target="_blank" href="https://twitter.com/Atrtba"></a>
                    <a className="fa fa-google-plus" rel="noopener noreferrer" target="_blank" href="mailto:arslan@softweare.ba"></a>
                </div>
            </div>
        </div>
    )
}

export default Footer;
