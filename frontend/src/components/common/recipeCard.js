import React from 'react'
import Card from 'react-bootstrap/Card'
import { CustomImage } from 'components/common/customImage'

function RecipeCard(props) {
    return (
        <div className="itemCard">
            <Card border="primary" style={{ width: '18rem', color: "black" }}>
                <CustomImage imageId={props.image} className="image-wrapper" alt="Recipe image" />
                <Card.Body>
                    <Card.Title>{props.title}</Card.Title>
                    <Card.Text style={{ textDecoration: "none" }}>
                        {props.description}
                    </Card.Text>
                </Card.Body>
                <Card.Footer>
                    <small className="text-muted" style={{ float: 'right' }} >{props.author.username}</small>
                </Card.Footer>
            </Card>
        </div >
    )
}

export default RecipeCard;
