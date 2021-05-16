import React from 'react'
import { withRouter } from 'react-router-dom'
import Menu from 'components/common/menu'

function AddRecipe(props) {

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    return (
        <div>
            <Menu handleSearchChange={handleSearchChange} {...props}/>
            <p>add single recipe page</p>
        </div>
    )
}

export default withRouter(AddRecipe);
