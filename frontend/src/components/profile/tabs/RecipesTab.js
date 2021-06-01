import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function RecipesTab(props) {

    return (
        <div className="recipesTab">
        <CustomTable tab={props.tab} userId={props.userId} handleRowClick={props.handleRowClick} setLoading={props.setLoading}></CustomTable>
        </div>
    )
}

export default RecipesTab;
