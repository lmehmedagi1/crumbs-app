import React, { useState, useEffect } from 'react'
import { withRouter, Link } from 'react-router-dom'
import { Spinner, Button, Form, FormControl, DropdownButton, Dropdown } from 'react-bootstrap'

import Alert from 'components/alert/alert'
import Menu from 'components/common/menu'

import dietApi from 'api/diet'
import { CustomImage } from 'components/common/customImage'

const sortingValues = {
    "Title: A to Z": "title-asc",
    "Title: Z to A": "title-desc",
    "Duration: Shortest first": "duration-asc",
    "Duration: Longest first": "duration-desc",
    "Time added: Newest first": "createdAt-asc",
    "Time added: Oldest first": "createdAt-desc"
}

const sortingKeys = Object.keys(sortingValues);

function BrowseDiets(props) {

    const [loading, setLoading] = useState(false);

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    const [diets, setDiets] = useState([]);

    const [activePageNo, setActivePageNo] = useState(0);
    const [activeHasNext, setActiveHasNext] = useState(true);
    const [activeSearched, setActiveSearched] = useState("");
    const [activeSort, setActiveSort] = useState({title: "Title: A to Z", value: "title-asc"});

    useEffect(() => {
        fetchDiets(activeSearched, activePageNo, activeSort);
    }, []);

    const fetchDiets = (searched, pageNo, sort) => {
        setActivePageNo(pageNo);
        setActiveSearched(searched);

        const params = {
            search: searched,
            pageNo: pageNo,
            sort: sort.value
        }

        setLoading(true);
        dietApi.getDiets((data) => {
            if (data == null) data = { diets: [], hasNext: false };
            if (data.diets == null) data.diets = [];
            if (pageNo == 0) setDiets(data.diets);
            else {
                let newDiets = [...diets, ...data.diets];
                setDiets(newDiets);
            }
            setActiveHasNext(data.hasNext);
            setLoading(false);
        }, params);
    }

    const exploreMore = () => {
        let pageNo = activePageNo + 1;
        setActivePageNo(pageNo);
        fetchDiets(activeSearched, pageNo, activeSort);
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
        fetchDiets(formDataObj.search, 0, activeSort);
    }

    const handleSortingSelect = event => {
        let title = event;
        let value = sortingValues[title];
        let sorting = {title, value};
        setActiveSort(sorting);
        setActivePageNo(0);
        fetchDiets(activeSearched, 0, sorting);
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
                        <div className="sort">
                            <DropdownButton id="dropdown-basic-button" title={activeSort.title} onSelect={handleSortingSelect} active>
                                {sortingKeys.map((title, index) => (
                                    <Dropdown.Item eventKey={title} active={activeSort.title === title}>{title}</Dropdown.Item>
                                ))}
                            </DropdownButton>
                        </div>
                    </div>
                    {loading ? <Spinner className="spinner" animation="border" role="status" /> : null}
                    <div className="list">
                        
                        {diets.map((diet, index) => (
                            <Link to={"/diet/" + diet.id}>
                                <div className="diet-card">
                                    <div className="diet-info">
                                        <div>
                                        <h1>{diet.title}</h1>
                                        <h2>{diet.author.firstName + " " + diet.author.lastName + " (@" + diet.author.username + ")"}</h2>
                                        </div>
                                        <div>
                                        <h3>Duration: {diet.duration} days</h3>
                                        <h3>{diet.description}</h3>
                                        </div>
                                    </div>
                                    <div className="recipe-list">
                                        {diet.recipes.map((recipe, index) => (
                                            <div className="recipe-card">
                                                <CustomImage imageId={recipe.image} className="image-wrapper" alt="Recipe image" />
                                                <h2> {recipe.title} </h2>
                                                <h3> {recipe.description} </h3>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>
                    {activeHasNext ?
                        <div className="load-more">
                            <Button variant="primary" type="submit" onClick={() => exploreMore()}>LOAD MORE</Button>
                        </div>
                        : null}
                </div>
            </div>
        </div>
    )
}

export default withRouter(BrowseDiets);
