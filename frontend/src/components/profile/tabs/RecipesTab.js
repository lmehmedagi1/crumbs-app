import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function RecipesTab(props) {

    return (
        <div className="recipesTab">
        <CustomTable tab={props.tab} userId={props.userId} setShow={props.setShow} setMessage={props.setMessage} setVariant={props.setVariant} getToken={props.getToken} setToken={props.setToken} setLoading={props.setLoading}></CustomTable>
        </div>
    )
}

export default RecipesTab;
