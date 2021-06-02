import React from 'react'
import Requests from 'api/requests'
import { env } from 'configs/env'

class DietApi extends React.Component {

    getDiets = (cb, params) => {
        Requests.sendGetRequest(cb, env.BASE_PATH  + "recipe-service/diets", { params: params }, (response) => { cb(response.data); }, (error) => { cb(null, error); });
    }
}

export default new DietApi();
