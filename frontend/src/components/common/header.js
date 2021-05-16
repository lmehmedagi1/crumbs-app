import React from 'react'
import { Link } from 'react-router-dom'

function Header() {

    const logout = () => {
        console.log("ARSLAN KRALJINA 4")
    }

    return (
        <div className="headerContainer">
            <div id="headerColumnLeft">
                <div>
                <a className="fa fa-facebook" rel="noopener noreferrer" target="_blank" href="https://www.facebook.com/arslan.turkusic.5/"></a>
                    <a className="fa fa-instagram" rel="noopener noreferrer" target="_blank" href="https://www.instagram.com/arslan"></a>
                    <a className="fa fa-twitter" rel="noopener noreferrer" target="_blank" href="https://twitter.com/Atrtba"></a>
                    <a className="fa fa-google-plus" rel="noopener noreferrer" target="_blank" href="mailto:arslan@softweare.ba"></a>
                </div>
            </div>
            <div id="headerColumnRight">
                {true ? 
                    <div>
                    <Link className="headerLink" to={"/login"}>
                        Login
                    </Link>
                    <span> or </span>
                    <Link className="headerLink" to={"/register"}>
                        Create an Account
                    </Link>
                    </div>
                    :
                    <div onClick={logout}>
                    <Link className="headerLink" to='#'>
                        Logout
                    </Link>
                    </div>
                }
            </div>
        </div>
    )
}

export default Header;
