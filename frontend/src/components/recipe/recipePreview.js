import Menu from 'components/common/menu'
import React, { useEffect } from 'react'
import { useSelector } from 'react-redux'
import { Col, Form, Row, ListGroup } from 'react-bootstrap'
import { withRouter } from 'react-router-dom'
import { get } from '../../actions/recipeActions';

function RecipePreview(props) {

    const recipe = useSelector(state => state.recipes.recipe);

    useEffect(() => {
        console.log("Id", props.match.params.id)
        // Axios call to fetch recipe with provided id
        // dispatch(get(props.match.params.id))
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
                <Form.Label className="title">{recipe.title}</Form.Label>
            </Row>
            <Row>
                <Col md={6}>
                    <Form.Label className="form-label">Ingredients</Form.Label>
                    {recipe.ingredients && (recipe.ingredients.map(row =>
                        <ListGroup horizontal>  {row.map(i => <ListGroup.Item action> {i} </ListGroup.Item>)} </ListGroup>))}
                </Col>
            </Row>
            <Row>
                <Col md={6}>
                    <Form.Label className="form-label">Method of Preparation</Form.Label>
                    <textarea
                        rows="10"
                        name="Method"
                        value={recipe.method}
                        disabled
                        className="comment-section form-control"
                    />
                </Col>
            </Row>
        </div>
    )
}

export default withRouter(RecipePreview);
