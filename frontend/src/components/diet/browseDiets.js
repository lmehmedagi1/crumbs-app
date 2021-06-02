import React, { useState, useEffect } from 'react'
import { withRouter, Link } from 'react-router-dom'
import { Spinner, Button, Form, FormControl } from 'react-bootstrap'

import Alert from 'components/alert/alert'
import Menu from 'components/common/menu'

import dietApi from 'api/diet'
import { CustomImage } from 'components/common/customImage'

function BrowseDiets(props) {

    const [loading, setLoading] = useState(false);

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    const [diets, setDiets] = useState([]);

    const [activePageNo, setActivePageNo] = useState(0);
    const [activeHasNext, setActiveHasNext] = useState(true);
    const [activeSearched, setActiveSearched] = useState("");

    useEffect(() => {
        fetchDiets("", 0);
    }, []);

    const fetchDiets = (searched, pageNo) => {
        setActivePageNo(pageNo);
        setActiveSearched(searched);

        const params = {
            search: searched,
            pageNo: pageNo
        }

        setLoading(true);
        // To do: Fetch diets
        setLoading(false);
        // dietApi.getDiets((data) => {
        //     if (data == null) data = { users: [], hasNext: false };
        //     if (data.users == null) data.users = [];
        //     if (pageNo == 0) setPeople(data.users);
        //     else {
        //         let newUsers = [...people, ...data.users];
        //         setPeople(newUsers);
        //     }
        //     setActiveHasNext(data.hasNext);
        //     setLoading(false);
        // }, params);
    }

    const exploreMore = () => {
        let pageNo = activePageNo + 1;
        setActivePageNo(pageNo);
        fetchDiets(activeSearched, pageNo);
    }

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    const handleDietSearch = event => {
        setDiets([]);
        event.preventDefault();
        const formData = new FormData(event.target),
        formDataObj = Object.fromEntries(formData.entries());
        fetchDiets(formDataObj.search, 0);
    }

    return (
        <div className={loading ? "blockedWait" : ""}>
            <div className={loading ? "blocked" : ""}>
                <Menu handleSearchChange={handleSearchChange} {...props} />
                <Alert message={message} showAlert={show} variant={variant} onShowChange={setShow} />
                <div className="dietsContainer">
                    <div className="header">
                        <div className="title">Browse diets</div>
                        <div className="search">
                            <Form noValidate inline onSubmit={handleDietSearch}>
                                <FormControl type="text" name="search" placeholder="Try enter: Sport" defaultValue={props.initial} className="mr-sm-2" />
                                <i className="fa fa-search" aria-hidden="true"></i>
                            </Form>
                        </div>
                    </div>
                    {loading ? <Spinner className="spinner" animation="border" role="status" /> : null}
                    <div className="list">
                        {diets.map((user, index) => (
                            <Link to={"/diet/" + user.id}>
                                <div className="userCard">
                                    <CustomImage imageId={user.avatar} className="imageWrapper" alt="User avatar" />
                                    <h1>{user.firstName + " " + user.lastName}</h1>
                                    <h2>@{user.username}</h2>
                                </div>
                            </Link>
                        ))}
                    </div>
                    {activeHasNext ?
                        <div className="loadMore">
                            <Button variant="primary" type="submit" onClick={() => exploreMore()}>LOAD MORE</Button>
                        </div>
                        : null}
                </div>
            </div>
        </div>
    )
}

export default withRouter(BrowseDiets);
