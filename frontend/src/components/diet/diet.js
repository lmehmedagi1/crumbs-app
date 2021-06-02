import React, { useState, useEffect } from 'react'
import { withRouter, Link } from 'react-router-dom'
import { Spinner, Button, Form, FormControl } from 'react-bootstrap'

import Alert from 'components/alert/alert'
import Menu from 'components/common/menu'

import dietApi from 'api/diet'
import { CustomImage } from 'components/common/customImage'

function Diet(props) {

    const [loading, setLoading] = useState(false);

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    const [diet, setDiet] = useState([]);

    useEffect(() => {
        fetchDiet("");
    }, []);

    const fetchDiet = (id) => {
        setLoading(true);
        // To do: fetch a single diet
        setLoading(false);
    }

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    return (
        <div className={loading ? "blockedWait" : ""}>
            <div className={loading ? "blocked" : ""}>
                <Menu handleSearchChange={handleSearchChange} {...props} />
                <Alert message={message} showAlert={show} variant={variant} onShowChange={setShow} />
                <div className="dietContainer">
                    <div className="header">
                        Single diet page
                    </div>
                </div>
            </div>
        </div>
    )
}

export default withRouter(Diet);
