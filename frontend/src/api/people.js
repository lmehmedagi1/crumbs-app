import React from 'react'
import { hostUrl } from 'components/utility/constants'
import Requests from 'api/requests'

class PeopleApi extends React.Component {

    constructor() {
        super();
    }

    getPeople = (cb, params) => {
        Requests.sendGetRequest(cb, hostUrl + "user-service/account/all", {params: params}, (response) => { cb(response.data); }, (error) => { cb(error); });
    }
}

export default new PeopleApi();
