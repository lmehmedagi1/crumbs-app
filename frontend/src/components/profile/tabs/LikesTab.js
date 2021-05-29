import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function LikesTab(props) {

    return (
        <div className="likesTab">
            <div>
            <CustomTable userId={props.userId} tab={"likedRecipes"} setShow={props.setShow} handleRowClick={props.handleRowClick} setMessage={props.setMessage} setVariant={props.setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={props.setLoading}></CustomTable>
            </div>
            <div>
            <CustomTable userId={props.userId} tab={"likedDiets"} setShow={props.setShow} handleRowClick={props.handleRowClick} setMessage={props.setMessage} setVariant={props.setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={props.setLoading}></CustomTable>
            </div>
        </div>
    )
}

export default LikesTab;
