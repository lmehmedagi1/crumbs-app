import Menu from 'components/common/menu'
import React, { useEffect } from 'react'
import { Col, Form, Row, ListGroup, Button} from 'react-bootstrap'
import { withRouter } from 'react-router-dom'
import { get, getRecipeRating, getRecipeReviews} from '../../actions/recipeActions';
import { useSelector, useDispatch } from 'react-redux'

function RecipePreview(props) {

    const recipe = useSelector(state => state.recipes.recipe);
    const dispatch = useDispatch()
    useEffect(() => {
        console.log("Id", props.match.params.id)
        dispatch(get(props.match.params.id));
        dispatch(getRecipeRating(props.match.params.id));
        dispatch(getRecipeReviews(props.match.params.id));
    }, []);

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    const btnCommentOnClick = () => {
        
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
                    <ListGroup horizontal>
                        {recipe.ingredients && (recipe.ingredients.map(row => <ListGroup.Item action> {row.name} </ListGroup.Item>)) }
                    </ListGroup>
                </Col>
                <Col md={6}>
                Ocjena: {recipe.rating}
                <br></br> <br></br>
                Komentari
                {recipe.comments && (recipe.comments.map(row => <ListGroup horizontal> <ListGroup.Item style={{width:"100%"}} action> {row.comment} </ListGroup.Item> </ListGroup>)) }
                <textarea
                        rows="5"
                        name="Comment"
                        className="comment-section form-control"
                    />
                {<Button className="btn-submit" onClick={btnCommentOnClick}> Comment </Button>}
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
                        className="form-control"
                    />
                </Col>
            </Row>
        </div>
    )
}

export default withRouter(RecipePreview);
