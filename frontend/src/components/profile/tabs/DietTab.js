import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function DietTab(props) {

    return (
        <div className="dietsTab">
        <CustomTable {...props} />
        </div>
    )
}

export default DietTab;
