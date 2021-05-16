import React from 'react'
import { withRouter } from 'react-router-dom'
import Menu from 'components/common/menu'

function Browse(props) {

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    return (
        <div>
            <Menu handleSearchChange={handleSearchChange} {...props}/>
            <p>Browse page</p>
        </div>
    )
}

export default withRouter(Browse);
