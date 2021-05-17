import React from 'react'
import { withRouter } from 'react-router-dom'
import Menu from 'components/common/menu'
import RecipeForm from 'components/recipe/recipeForm'

function Recipe(props) {

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    return (
        <div>
            <Menu handleSearchChange={handleSearchChange} {...props} />
            <RecipeForm show={true} title="Add Recipe" />
            <p>single recipe page</p>
        </div>
    )
}

export default withRouter(Recipe);
