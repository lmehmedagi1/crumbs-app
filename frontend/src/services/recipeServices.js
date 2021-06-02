import axios from 'axios'
import { env } from "configs/env"

export function getMostPopularRecipes(pageNo) {
    return (dispatch) => {
        return {
            request: axios(env.BASE_PATH + "recipe-service/recipes/topMonthly", {
                method: "GET",
                params: { pageNo }
            }).then(res => {
                dispatch({ type: 'RECIPE_GET_MOST_POPULAR', payload: res.data });
            }).catch(err => {
                console.log("HALO", err)
            })
        }
    }
}
