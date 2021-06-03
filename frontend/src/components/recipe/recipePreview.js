import { pushFile, setState } from 'actions/recipeActions'
import { getUser, userHasPermission, userIsLoggedIn } from 'api/auth'
import { CustomImage } from 'components/common/customImage'
import { dbx } from 'components/common/dropbox'
import Menu from 'components/common/menu'
import SelectField from 'components/common/selectField'
import RecipeForm from 'components/recipe/recipeForm'
import { category_api_path } from 'configs/env'
import moment from 'moment'
import React, { useEffect, useState } from 'react'
import { Button, Col, Form, Row } from 'react-bootstrap'
import HeartCheckbox from 'react-heart-checkbox'
import NumberFormat from "react-number-format"
import { useDispatch, useSelector } from 'react-redux'
import { withRouter } from 'react-router-dom'
import StarRatings from 'react-star-ratings'
import {
    clearState, editComment, get,
    getRecipeRating, getRecipeReviews, postComment, updateLike, updateRating
} from '../../actions/recipeActions'

function RecipePreview(props) {

    const recipe = useSelector(state => state.recipes.recipe);
    const userReview = useSelector(state => state.recipes.reviewOfUser);
    const [txtComment, setTxtComment] = useState("");
    const [txtCommentEdit, setTxtCommentEdit] = useState("");
    const [countComment, setCount] = useState(0);
    const [editMode, setEditMode] = useState(false);
    const [show, setShow] = useState(false);

    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(clearState());
        console.log("Id", props.match.params.id)
        dispatch(get(props.match.params.id));
        dispatch(getRecipeRating(props.match.params.id));
        dispatch(getRecipeReviews(props.match.params.id, countComment));

    }, []);

    useEffect(() => {
        if (recipe.images && recipe.images.length > 0) {
            recipe.images.forEach(element => {
                dbx.filesDownload({
                    path: element.image
                }).then(x => {
                    dispatch(pushFile({ file: x.result, name: x.result.name }));
                })
            })
        }

    }, [recipe.images]);


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
        dispatch(postComment(txtComment, "recipe", props.match.params.id, userReview.id ? userReview.id : "noId"));
        setTxtComment("");
    }

    const btnSaveEditCommentOnClick = () => {
        dispatch(editComment(txtCommentEdit, "recipe", props.match.params.id, userReview.id ? userReview.id : "noId"));
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

    const changeRating = (newRating) =>
        dispatch(updateRating(newRating, "recipe", props.match.params.id, userReview.id ? userReview.id : "noId"));

    const chkBoxOnClick = () => {
        dispatch(updateLike(!userReview.is_liked ? true : false, "recipe", props.match.params.id, userReview.id ? userReview.id : "noId"));
    }

    const timestampToDateTime = timestamp => {
        const longDateTimeFormat = "MMMM Do YYYY, h:mm:ss a";
        return moment.utc(timestamp).local().format(longDateTimeFormat);
    }

    const handleOnSelectChange = (value, name) => {
        value = value ? value : [];
        dispatch(setState({ [name]: value }))
    };


    return (
        <div className="recipePreview">
            <Menu handleSearchChange={handleSearchChange} {...props} />
            <div className="float-right">
                {userHasPermission(recipe.userId) && <Button className="float-right" onClick={() => setShow(true)}> Edit </Button>}
            </div>
            <Row>
                <Form.Label className="title">{recipe.title}</Form.Label>
            </Row>
            <Row>
                <Col md={6}>
                    <Row>
                        <SelectField
                            as={Col}
                            label="Preparation Level"
                            value={recipe.preparationLevel}
                            name="preparationLevel"
                            onChange={item => handleOnSelectChange(item, "preparationLevel")}
                            type="Tezina pripreme"
                            apiPath={category_api_path}
                            viewMode />
                        <SelectField
                            as={Col}
                            label="Method"
                            value={recipe.preparationMethod}
                            name="preparationMethod"
                            type="Nacin pripreme"
                            apiPath={category_api_path}
                            onChange={item => dispatch(setState({ preparationMethod: item.id, preparationMethod: item }))}
                            viewMode />
                    </Row>
                    <Row>
                        <SelectField
                            as={Col}
                            label="Group"
                            value={recipe.group}
                            name="group"
                            apiPath={category_api_path}
                            onChange={item => dispatch(setState({ group: item.id, group: item }))}
                            viewMode
                            type="Grupa jela" />

                        <SelectField
                            as={Col}
                            label="Season"
                            value={recipe.season}
                            name="season"
                            apiPath={category_api_path}
                            onChange={item => dispatch(setState({ season: item }))}
                            viewMode
                            type="Sezona" />
                    </Row>
                    <Row>
                        <Col md={9} />
                        <Col>
                            <Form.Group>
                                <Form.Label className="form-label">
                                    Preparation time
                        </Form.Label>
                                <NumberFormat
                                    className="form-control form-control-md text-right"
                                    placeholder="..."
                                    value={recipe.preparationTime}
                                    thousandSeparator={true}
                                    decimalScale={0}
                                    suffix={" min"}
                                    decimalPrecision={0}
                                    disabled
                                />
                            </Form.Group>
                        </Col>
                    </Row>

                    <Form.Label className="form-label subtitle">Ingredients</Form.Label>
                    <div>
                        {recipe.ingredients && (recipe.ingredients.map((row, index) => <span className="listElement">
                            {row.label + (index < recipe.ingredients.length - 1 ? "," : "")} </span>))}
                    </div>
                </Col>

                <Col>
                    {recipe.images && recipe.images.length > 0 && <div className="recipeImages">
                        <div>
                            <CustomImage imageId={recipe.activeImage} className="" alt="Recipe Image" />
                        </div>
                        <div className="recipeImagesGrid">
                            {recipe.images.map(x => (
                                <div className="landingPagerecipe" onClick={() => dispatch(setState({ activeImage: x.image }))}>
                                    <CustomImage imageId={x.image} className="" alt="Image" />
                                </div>
                            ))}
                        </div>
                    </div>}
                </Col>
            </Row>

            <Row>
                <Col md={6}>
                    <Form.Label className="form-label subtitle">Description</Form.Label>
                    <textarea
                        rows="4"
                        name="opis"
                        placeholder="Description..."
                        value={recipe.description}
                        disabled
                        className="comment-section form-control"
                    />
                    <Form.Label className="form-label subtitle">Method of Preparation</Form.Label>
                    <textarea
                        rows="11"
                        name="method"
                        placeholder="..."
                        value={recipe.method}
                        disabled
                        className="comment-section form-control"
                    />
                    <Form.Label className="form-label subtitle">Advice</Form.Label>
                    <textarea
                        rows="3"
                        name="advice"
                        placeholder="..."
                        value={recipe.advice}
                        disabled
                        className="comment-section form-control"
                    />

                </Col>
                <Col md={6} className="review" >
                    <HeartCheckbox
                        checked={userReview.is_liked ? userReview.is_liked : false} onClick={chkBoxOnClick} >
                    </HeartCheckbox>
                    <Row>
                        <Form.Label>{recipe.rating ? parseFloat(recipe.rating).toFixed(1) : null}</Form.Label>
                        <StarRatings
                            rating={recipe.rating}
                            starRatedColor="purple"
                            starDimension="15px"
                            numberOfStars={5}
                            name='rating'
                        />
                    </Row>
                    <Row>
                        <StarRatings
                            rating={userReview.rating ? userReview.rating : 0}
                            starRatedColor="orange"
                            changeRating={changeRating}
                            numberOfStars={5}
                            name='rating'
                        />
                    </Row>

                    <div className="commentsTitle"> <i className="fa fa-comments"></i> Komentari</div>

                    {recipe.comments && (recipe.comments.map(row =>
                        <Row className="border border-secondary">
                            <Col><h4 className="comment-username">{row.author.username} </h4></Col>
                            <Col md={8}> {editMode && userIsLoggedIn() && row.author.id === getUser().id ?
                                <textarea
                                    rows="2"
                                    name="Comment"
                                    value={editMode ? txtCommentEdit : ""}
                                    onChange={handleEditCommentChange}
                                    className="comment-section form-control"
                                /> : <text className="comment-comment">{row.comment}</text>}

                            </Col>
                            <Col className="comment-edit" > {userIsLoggedIn() && row.author.id === getUser().id && !editMode ?
                                <Button onClick={() => editCommentOnClick(row.comment)}
                                    className="btn-comment-edit">
                                    <i className="fa fa-pencil"></i>
                                </Button> : null}
                                {userIsLoggedIn() && row.author.id === getUser().id && editMode ?
                                    <Button className="btnEditMode" onClick={btnSaveEditCommentOnClick}> Save </Button> : null}
                                {userIsLoggedIn() && row.author.id === getUser().id && editMode ?
                                    <Button style={{ background: "red" }} class="btnEditMode" > Delete </Button> : null}
                            </Col>
                            <text className="comment-createdAt">{timestampToDateTime(row.createdAt)}</text>
                        </Row>))}
                    {<Button id="btn-outline-primary" onClick={btnLoadMoreOnClick}> Load More </Button>}
                    <textarea
                        rows="5"
                        name="Comment"
                        value={!editMode ? txtComment : ""}
                        onChange={handleCommentChange}
                        disabled={!userReview.comment || userReview.comment == "" ? false : true}
                        className="comment-section form-control"
                    />
                    {<Button disabled={!userReview.comment || userReview.comment == "" ? false : true}
                        className="btn-submit"
                        onClick={btnCommentOnClick}>
                        Comment
                        </Button>}
                </Col>

            </Row>
            <RecipeForm title="Edit"
                show={show}
                onHide={() => {
                    dispatch(get(props.match.params.id));
                    setShow(false)
                }}
                getToken={props.getToken}
                setToken={props.setToken}
                isEdit={true} />
        </div >
    )
}

export default withRouter(RecipePreview);
