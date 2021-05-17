import Menu from 'components/common/menu'
import React, { useEffect, useState } from 'react'
import { Col, Form, Row, ListGroup } from 'react-bootstrap'
import { withRouter } from 'react-router-dom'

function RecipePreview(props) {

    const [data, setData] = useState({});
    useEffect(() => {
        console.log("Id", props.match.params.id)
        // Axios call to fetch recipe with provided id
        const result = {
            title: "Best Pancake Ever",
            method: "Bjelanjke istuci u snijeg. Zutanjke izmutiti s secerom, dodati brasno, kakao i prasak i mijesati dok se dobije glatka smjesa. Dodati 3 zlice snijega od bjelanjka i lagano mijesati da se dobije lijepa smjesa, zatim sve izliti u ostatak bjelanjaka i jos lagano mijesati dok smjesa bude jednolicna.",
            ingredients: [["jaje", "brasno", "ulje", "maslac", "cokolada"], ["mlijeko"]],
            comment: "Odlican recept!"
        }
        setData(result)
    }, []);

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    return (
        <div className="recipePreview">
            <Menu handleSearchChange={handleSearchChange} {...props} />
            <Row>
                <Form.Label className="title">{data.title}</Form.Label>
            </Row>
            <Row>
                <Col md={6}>
                    <Form.Label className="form-label">Ingredients</Form.Label>
                    {data.ingredients && (data.ingredients.map(row =>
                        <ListGroup horizontal>  {row.map(i => <ListGroup.Item action> {i} </ListGroup.Item>)} </ListGroup>))}
                </Col>
            </Row>
            <Row>
                <Col md={6}>
                    <Form.Label className="form-label">Method of Preparation</Form.Label>
                    <textarea
                        rows="10"
                        name="Method"
                        value={data.method}
                        disabled
                        className="comment-section form-control"
                    />
                </Col>
            </Row>
        </div>
    )
}

export default withRouter(RecipePreview);
