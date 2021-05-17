import React from 'react'
import Card from 'react-bootstrap/Card'

function RecipeCard(props) {
    return (
        <div className="itemCard">
            <Card border="primary" style={{ width: '18rem', color: "black" }}>
                <Card.Img variant="top" src="https://source.unsplash.com/random/300x300" />
                <Card.Body>
                    <Card.Title>{props.ButtonrecipeName}</Card.Title>
                    <Card.Text style={{ textDecoration: "none" }}>
                        {props.text}
                    </Card.Text>
                </Card.Body>
                <Card.Footer>
                    <small className="text-muted" style={{ float: 'right' }} >{props.author}</small>
                </Card.Footer>
            </Card>
        </div >
    )
}

export default RecipeCard;
