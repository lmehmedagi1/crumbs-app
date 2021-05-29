import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function SubscriptionsTab(props) {

    return (
        <div className="subscriptionsTab">
            <div>
            <CustomTable userId={props.userId} tab={"subscribers"} handleRowClick={props.handleRowClick} setLoading={props.setLoading}></CustomTable>
            </div>
            <div>
            <CustomTable userId={props.userId} tab={"subscriptions"} handleRowClick={props.handleRowClick} setLoading={props.setLoading}></CustomTable>
            </div>
        </div>
    )
}

export default SubscriptionsTab;
