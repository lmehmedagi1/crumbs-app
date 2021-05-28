import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function DietTab(props) {

    return (
        <div className="dietsTab">
        <CustomTable userId={props.userId} tab={props.tab} setShow={props.setShow} setMessage={props.setMessage} setVariant={props.setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={props.setLoading}></CustomTable>
        </div>
    )
}

export default DietTab;
