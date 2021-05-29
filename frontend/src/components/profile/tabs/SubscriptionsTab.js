import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function SubscriptionsTab(props) {

    return (
        <div className="subscriptionsTab">
            <div>
            <CustomTable userId={props.userId} tab={"subscribers"} setShow={props.setShow} setMessage={props.setMessage} setVariant={props.setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={props.setLoading}></CustomTable>
            </div>
            <div>
            <CustomTable userId={props.userId} tab={"subscriptions"} setShow={props.setShow} setMessage={props.setMessage} setVariant={props.setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={props.setLoading}></CustomTable>
            </div>
        </div>
    )
}

export default SubscriptionsTab;
