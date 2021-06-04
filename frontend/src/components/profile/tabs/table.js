import { getUser, userIsLoggedIn } from 'api/auth'
import profileApi from 'api/profile'
import { CustomImage } from 'components/common/customImage'
import React, { useEffect, useState } from 'react'
import { Table } from 'react-bootstrap'
import BootstrapTable from 'react-bootstrap-table-next'
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css'
import paginationFactory from 'react-bootstrap-table2-paginator'
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css'

function CustomTable(props) {

    const [values, setValues] = useState([]);
    const [currTab, setCurrTab] = useState("");

    useEffect(() => {

        let tab = props.tab;

        if (!["diets", "recipes", "subscribers", "subscriptions", "likedRecipes", "likedDiets"].includes(tab)) return;
        if (tab == currTab || !props.userId) return;

        if (props.tab.includes("like") && !props.activeTab.includes("like")) return;
        if (props.tab.includes("subscri") && !props.activeTab.includes("subscri")) return;

        setCurrTab(tab);

        props.setLoading(true);
        switch (tab) {
            case "recipes":
                profileApi.getUserRecipes((data, err) => updateData(data, err), { id: props.userId });
                break;
            case "diets":
                if (userIsLoggedIn() && getUser().id === props.userId)
                    profileApi.getUserDiets((data, err) => updateData(data, err), { id: props.userId }, props.getToken(), props.setToken);
                else profileApi.getUserPublicDiets((data, err) => updateData(data, err), { id: props.userId });
                break;
            case "subscribers":
                profileApi.getUserSubscribers((data, err) => updateData(data, err), { id: props.userId });
                break;
            case "subscriptions":
                profileApi.getUserSubscriptions((data, err) => updateData(data, err), { id: props.userId });
                break;
            case "likedRecipes":
                profileApi.getUserLikedRecipes((data, err) => updateData(data, err), { id: props.userId }, props.getToken(), props.setToken);
                break;
            case "likedDiets":
                profileApi.getUserLikedDiets((data, err) => updateData(data, err), { id: props.userId }, props.getToken(), props.setToken);
                break;
            default:
                props.setLoading(false);
                setValues([]);
                break;
        }
    }, [props.tab]);

    const updateData = (data, err) => {
        props.setLoading(false);
        if (err != null) {
            setValues([]);
            props.setShow(true);
            props.setMessage(err);
            props.setVariant("warning");
            return;
        }
        if (data == null) data = [];
        setValues(data.map((item) => { return item; }));
    }

    const recipesColumns = [
        {
            dataField: 'title',
            text: '',
            formatter: (value, row) => {
                return <CustomImage imageId={row.image} className="rowImage" alt="Image" />
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
                return <CustomImage imageId={row.avatar} className="rowImage" alt="User avatar" />
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
        onClick: (e, row) => {
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
                <BootstrapTable hover={true} keyField='id' data={values} columns={(props.tab == "diets" || props.tab == "recipes" || props.tab.includes("liked")) ? recipesColumns : userColumns} pagination={paginationFactory(options)} rowEvents={clickEvents} />
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
