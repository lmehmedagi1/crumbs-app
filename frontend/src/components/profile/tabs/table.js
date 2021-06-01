import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Table } from 'react-bootstrap'

import BootstrapTable from 'react-bootstrap-table-next'
import paginationFactory from 'react-bootstrap-table2-paginator';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css'
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css'

import { CustomImage } from 'components/common/customImage'

import profileApi from 'api/profile'

function CustomTable(props) {

    const [values, setValues] = useState([]);
    const [currTab, setCurrTab] = useState("");

    useEffect(() => {

        let tab = props.tab;

        if (!["diets", "recipes", "subscribers", "subscriptions", "likedRecipes", "likedDiets"].includes(tab)) return;
        if (tab == currTab || !props.userId) return;
        setCurrTab(tab);
        
        props.setLoading(true);
        switch (tab) {
            case "recipes":
                profileApi.getUserRecipes((data) => {
                    if (data == null) data = [];
                    setValues(data); 
                    props.setLoading(false);
                }, {id: props.userId});        
                break;
            case "diets":
                profileApi.getUserDiets((data) => {
                    if (data == null) data = [];
                    setValues(data); 
                    props.setLoading(false);
                }, {id: props.userId});   
                break;
            case "subscribers":
                profileApi.getUserSubscribers((data) => {
                    if (data == null) data = [];
                    setValues(data); 
                    props.setLoading(false);
                }, {id: props.userId});   
                break;
            case "subscriptions":
                profileApi.getUserSubscriptions((data) => {
                    if (data == null) data = [];
                    setValues(data); 
                    props.setLoading(false);
                }, {id: props.userId});   
                break;
            case "likedRecipes":
                profileApi.getUserLikedRecipes((data) => {
                    if (data == null) data = [];
                    setValues(data); 
                    props.setLoading(false);
                }, {id: props.userId}, props.getToken(), props.setToken);   
                break;
            case "likedDiets":
                profileApi.getUserLikedDiets((data) => {
                    if (data == null) data = [];
                    setValues(data); 
                    props.setLoading(false);
                }, {id: props.userId}, props.getToken(), props.setToken);   
                break;
            default:
                console.log("default", values)
                props.setLoading(false);
                setValues([]);
                break;
        }
    }, [props.tab]);

    const recipesColumns = [
        {
            dataField: 'title',
            text: '',
            formatter: (value, row) => {
                return <CustomImage imageId={row.image} className="rowImage" alt="Image"/>
            }
        }, {
            dataField: 'title',
            text: 'Title'
        }, {
            dataField: 'rating',
            text: 'Rating',
            formatter: (value, row) => {
                return row.rating == 0 ? "Not rated" : parseFloat(row.rating).toFixed(1) + "/5";
            }
        }, {
            dataField: 'description',
            text: 'Description'
        }
    ];

    const userColumns = [
        {
            dataField: 'firstName',
            text: '',
            formatter: (value, row) => {
                return <CustomImage imageId={row.avatar} className="rowImage" alt="User avatar"/>
            }
        }, {
            dataField: 'firstName',
            text: 'Name',
            formatter: (value, row) => {
                return row.firstName + " " + row.lastName;
            }
        }, {
            dataField: 'username',
            text: 'Username'
        }, {
            dataField: 'email',
            text: 'Email'
        }
    ];

    const options = {
        paginationSize: 2,
        pageStartIndex: 0,
        hidePageListOnlyOnePage: true,
        prePageText: 'Back',
        nextPageText: 'Next',
        sizePerPageList: [{
          text: '2', value: 2
        }, {
          text: '10', value: 10
        }]
    };

    const clickEvents = {
        onClick: (e, row, rowIndex) => {
            props.handleRowClick(row.id, currTab);
        }
    }

    const getNoValuesMessage = () => {
        if (props.tab == "likedRecipes") return "liked recipes";
        if (props.tab == "likedDiets") return "liked diets";
        return props.tab;
    }

    const getTitle = () => {
        if (!values.length) return null;
        if (props.tab == "likedRecipes") return <p>Liked recipes</p>;
        if (props.tab == "likedDiets") return <p>Liked diets</p>;
        if (props.tab == "subscriptions") return <p>Subscriptions</p>;
        if (props.tab == "subscribers") return <p>Subscribers</p>;
        return null;
    }

    return (
        <div className="customTable">
            {getTitle()}
            {values.length ? 
            <BootstrapTable hover={true} keyField='id' data={ values } columns={ props.tab == "diets" || props.tab == "recipes" || props.tab.includes("liked") ? recipesColumns : userColumns } pagination={ paginationFactory(options) } rowEvents={ clickEvents } />
            :
            <Table className="emptyTable">
            <thead>
                <tr><th> {getNoValuesMessage().charAt(0).toUpperCase() + getNoValuesMessage().slice(1)}</th></tr>
            </thead>
            <tbody>
                <tr><td>No {getNoValuesMessage()} yet</td></tr>
            </tbody>
            </Table>
            }
        </div>
    )
}

export default CustomTable;
