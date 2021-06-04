import React from 'react'
import Requests from 'api/requests'
import { env } from 'configs/env'

class PeopleApi extends React.Component {

    getPeople = (cb, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH  + "user-service/account/all", { params: params }, response =>{ cb(response.data); }, (error) => { cb(error); });
    }
}

export default new PeopleApi();
