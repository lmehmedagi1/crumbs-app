import Menu from 'components/common/menu'
import React, { useEffect, useState } from 'react'
import { Col, Form, Row, ListGroup, Button} from 'react-bootstrap'
import StarRatings from 'react-star-ratings';
import HeartCheckbox from 'react-heart-checkbox';
import { withRouter } from 'react-router-dom'
import { get, getRecipeRating, getRecipeReviews , postComment, getEntityReviewForUser, editComment, clearState, updateRating, updateLike} from '../../actions/recipeActions';
import { useSelector, useDispatch } from 'react-redux'

function RecipePreview(props) {

    const recipe = useSelector(state => state.recipes.recipe);
    const reviewOfUser = useSelector(state => state.recipes.reviewOfUser);
    const [txtComment, setTxtComment] = useState("");
    const [txtCommentEdit, setTxtCommentEdit] = useState("");
    const hideShowMore = (state => state.recipes.hidden);
    const [countComment, setCount] = useState(0);
    const [editMode, setEditMode] = useState(false);

    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(clearState());
        console.log("Id", props.match.params.id)
        dispatch(get(props.match.params.id));
        dispatch(getRecipeRating(props.match.params.id));
        dispatch(getRecipeReviews(props.match.params.id, countComment));
        dispatch(getEntityReviewForUser(props.match.params.id));
    }, []);

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    const btnLoadMoreOnClick = () => {
        dispatch(getRecipeReviews(props.match.params.id, countComment + 1));
        setCount(countComment + 1);
    }

    const btnCommentOnClick = () => {
        dispatch(postComment(txtComment, "recipe", props.match.params.id, reviewOfUser.id ? reviewOfUser.id : "noId"));
        setTxtComment("");
    }

    const btnSaveEditCommentOnClick = () => {
        dispatch(editComment(txtCommentEdit, "recipe", props.match.params.id, reviewOfUser.id ? reviewOfUser.id : "noId"));
        setEditMode(false);
    }

    const handleCommentChange = (event) => {
        setTxtComment(event.target.value);
    }

    const handleEditCommentChange = (event) => {
        setTxtCommentEdit(event.target.value);
    }

    const editCommentOnClick = (c) => {
        setEditMode(true);
        setTxtCommentEdit(c);
    }

    const changeRating = ( newRating, name ) => {
        dispatch(updateRating(newRating, "recipe", props.match.params.id, reviewOfUser.id ? reviewOfUser.id : "noId"));
    }

    const chkBoxOnClick = ( event, e ) => {
        dispatch(updateLike(!reviewOfUser.is_liked ? true : false, "recipe", props.match.params.id, reviewOfUser.id ? reviewOfUser.id : "noId"));
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
                <HeartCheckbox checked={ reviewOfUser.is_liked ? reviewOfUser.is_liked : false }  onClick={chkBoxOnClick} > </HeartCheckbox>
                <Row>
                    <h1>{parseFloat(recipe.rating).toFixed(1)}</h1>
                    <StarRatings
                            rating={recipe.rating}
                            starRatedColor="purple"
                            starDimension="15px"
                            numberOfStars={5}
                            name='rating'
                            /></Row>
                <Row>
                    <StarRatings
                            rating={reviewOfUser.rating ? reviewOfUser.rating : 0}
                            starRatedColor="orange"
                            changeRating={changeRating}
                            numberOfStars={5}
                            name='rating'
                            />
                </Row>
                <br></br> <br></br>
                <i className="fa fa-comments"></i>   Komentari
                {recipe.comments && (recipe.comments.map(row => 
                <Row className="border border-secondary">   
                        <Col><h4 className="comment-username">{row.author.username} </h4></Col>
                        <Col md={8}> { editMode && row.author.id=="75a8f34b-2539-452a-9325-b432dbe3b995" ? 
                                            <textarea
                                                rows="2"
                                                name="Comment"
                                                value={editMode ? txtCommentEdit : ""}
                                                onChange={handleEditCommentChange}
                                                className="comment-section form-control"
                                            /> :
                                            <text className="comment-comment">{row.comment}</text> }
                        
                        </Col>
                        <Col className="comment-edit" > { row.author.id=="75a8f34b-2539-452a-9325-b432dbe3b995" && !editMode ? 
                                <Button onClick={() => editCommentOnClick(row.comment)} className="btn-comment-edit"><i className="fa fa-pencil"></i></Button> : null 
                                }
                                { row.author.id=="75a8f34b-2539-452a-9325-b432dbe3b995" && editMode ? 
                                 <Button className="btnEditMode" onClick={btnSaveEditCommentOnClick}> Save </Button> : null }
                                { row.author.id=="75a8f34b-2539-452a-9325-b432dbe3b995" && editMode ?
                                  <Button style={{ background: "red"}} class="btnEditMode" > Delete </Button> : null}
                        </Col>
                        <text className="comment-createdAt">{row.createdAt}</text>
                </Row>)) }
                {<Button ID="btn-outline-primary" onClick={btnLoadMoreOnClick}> Load more... </Button>}
                <textarea
                        rows="5"
                        name="Comment"
                        value={!editMode ? txtComment : ""}
                        onChange={handleCommentChange}
                        disabled={ !reviewOfUser.comment || reviewOfUser.comment == "" ? false : true}
                        className="comment-section form-control"
                    />
                {<Button disabled = { !reviewOfUser.comment || reviewOfUser.comment == "" ? false : true}
                        className="btn-submit" 
                        onClick={btnCommentOnClick}> 
                        Comment 
                        </Button>}
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
