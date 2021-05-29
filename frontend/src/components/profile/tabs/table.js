import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Table } from 'react-bootstrap'

import BootstrapTable from 'react-bootstrap-table-next'
import paginationFactory from 'react-bootstrap-table2-paginator';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css'
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css'

import profileApi from 'api/profile'

const imagePlaceholder = "https://www.firstfishonline.com/wp-content/uploads/2017/07/default-placeholder-700x700.png";

function CustomTable(props) {

    const [values, setValues] = useState([]);

    useEffect(() => {
        setValues([]);
        if (!props.userId) return;
        console.log("tabela", props)
        props.setLoading(true);
        switch (props.tab) {
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
                props.setLoading(false);
                break;
        }
    }, [props.tab]);

    const recipesColumns = [
        {
            dataField: 'title',
            text: '',
            formatter: (value, row) => {
                return <div><img src={imagePlaceholder}/></div>
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
                return <div><img src={imagePlaceholder}/></div>
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
            <BootstrapTable keyField='id' data={ values } columns={ props.tab == "diets" || props.tab == "recipes" || props.tab.includes("liked") ? recipesColumns : userColumns } pagination={ paginationFactory(options) } />
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
