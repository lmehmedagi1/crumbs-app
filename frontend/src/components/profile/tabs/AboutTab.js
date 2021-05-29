import React from 'react'

function AboutTab(props) {

    return (
        <div className="aboutTab">
            <div className="container">
                <div><h2>First name: </h2><h2>{props.user && props.user.firstName}</h2></div>
                <div><h2>Last name: </h2><h2>{props.user && props.user.lastName}</h2></div>
                <div><h2>Gender: </h2><h2>{props.user && props.user.gender && (props.user.gender.charAt(0).toUpperCase() +  props.user.gender.slice(1))}</h2></div>
                <div><h2>Username: </h2><h2>{props.user && props.user.username}</h2></div>
                <div><h2>Email: </h2><h2>{props.user && props.user.email}</h2></div>
                <div><h2>Phone number: </h2><h2>{props.user && props.user.phoneNumber}</h2></div>
            </div>
        </div>
    )
}

export default AboutTab;
