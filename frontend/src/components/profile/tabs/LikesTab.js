import React from 'react'

import CustomTable from 'components/profile/tabs/table'

function LikesTab(props) {

    return (
        <div className="likesTab">
            <div>
            <CustomTable tab={"likedRecipes"} {...props} />
            </div>
            <div>
            <CustomTable tab={"likedDiets"} {...props} />
            </div>
        </div>
    )
}

export default LikesTab;
