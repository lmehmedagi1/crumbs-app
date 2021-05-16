import React from 'react'
import { Link } from 'react-router-dom'
import { Button } from 'react-bootstrap'

function RecipeCard(props) {
    return (
        <div className="itemCard">
            <Link to={props.recipe.url}>
                <div className="itemImage">
                    <img src={props.recipe.image} alt="Product image"/>
                </div>
                <div className="itemInfo">
                <div className="itemName">
                    {props.recipe.name}
                </div>
                <div className="itemDetails">
                    {props.recipe.details}
                </div>
                <div className="itemPrice">
                    Start from ${props.recipe.startingPrice}
                </div>
                <div className="itemButtons">
                    <Button variant="primary" type="submit">
                        Bid <i className="fa fa-gavel" aria-hidden="true"></i>
                    </Button>
                </div>
                </div>
            </Link>
        </div>
    )
}

export default RecipeCard;
