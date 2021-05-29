import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function DietTab(props) {

    return (
        <div className="dietsTab">
        <CustomTable userId={props.userId} tab={props.tab} handleRowClick={props.handleRowClick} setLoading={props.setLoading}></CustomTable>
        </div>
    )
}

export default DietTab;
