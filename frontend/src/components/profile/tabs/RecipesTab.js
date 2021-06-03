import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function RecipesTab(props) {

    return (
        <div className="recipesTab">
        <CustomTable {...props} />
        </div>
    )
}

export default RecipesTab;
