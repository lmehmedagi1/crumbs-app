import React, { isValidElement, useState } from 'react';
import { Modal, Form, Row, Col, Button, ProgressBar } from "react-bootstrap";
import FileUploader from 'components/common/fileUploader/fileUploader'
import SelectField from 'components/common/selectField';
import { useSelector, useDispatch } from 'react-redux'
import { setState, clearState } from 'actions/recipeActions';
import recipeApi from 'api/recipe'
import NumberFormat from "react-number-format";
import { uploadFiles } from 'components/common/dropbox';

function RecipeForm(props) {

    const category_api_path = "recipe-service/categories/type";
    const ingredient_api_path = "recipe-service/ingredients/type";
    const [loading, setLoading] = useState(false)

    const recipe = useSelector(state => state.recipes.recipe);
    const dispatch = useDispatch()

    const [files, setProfileImages] = useState([]);
    const [imagesError, setImagesError] = useState("");
    const [initialImages] = useState({});

    const updateUploadedFiles = (files) => {
        setProfileImages(files);
        setImagesError("");
    }

    const handleOnSelectChange = (value, name) => {
        value = value ? value : [];
        dispatch(setState({ [name]: value }))
    };

    const prepareData = () => {
        var categoryKeys = ["preparationLevel", "group", "season", "preparationMethod"]
        var categoryIds = categoryKeys.map(name => recipe[name] ? recipe[name].value : null)

        return {
            description: recipe.description,
            method: recipe.method,
            title: recipe.title,
            advice: recipe.advice,
            preparationTime: recipe.preparationTime,
            ingredients: recipe.ingredients.map(x => x.value),
            categories: categoryIds.map(x => x),
        }
    }

    const isValidForm = () => recipe.title && recipe.method

    const handleSubmit = (e) => {
        e.preventDefault()
        if (isValidForm()) {
            setLoading(true);
            uploadFiles(files, 'recipes').then(fileIds => {
                recipeApi.createRecipe((res, err) => {
                    setLoading(false);
                }, { ...prepareData(), images: fileIds }, props.getToken(), props.setToken);
            })
        } else { }

    }

    const onClose = () => {
        dispatch(clearState())
        props.onHide()
    }


    return (<Modal dialogClassName="my-modal" show={props.show} onHide={onClose} scrollable >
        <Modal.Header><Form.Label>{props.title}</Form.Label></Modal.Header>
        <Modal.Body>
            {loading && (
                <ProgressBar
                    animated
                    style={{ borderRadius: "0" }}
                    variant="mono"
                    now={100}
                />
            )}
            <Row>
                <Col md={4}>
                    <Form.Group>
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            required
                            type="text"
                            name="title"
                            placeholder="Title..."
                            value={recipe.title}
                            onChange={e => dispatch(setState({ title: e.target.value }))}
                            disabled={props.viewMode}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="form-label">
                            Preparation time (min)
                    </Form.Label>
                        <NumberFormat
                            className="form-control form-control-md text-right"
                            placeholder="..."
                            value={recipe.preparationTime}
                            thousandSeparator={true}
                            decimalScale={0}
                            suffix={" min"}
                            decimalPrecision={0}
                            onValueChange={(values) => {
                                const { formattedValue, value } = values;
                                dispatch(setState({ preparationTime: parseInt(value) }))
                            }}
                        />
                    </Form.Group>
                    <SelectField
                        label="Ingredients"
                        value={recipe.ingredients}
                        name="ingredients"
                        isMulti={true}
                        onChange={item => handleOnSelectChange(item, "ingredients")}
                        type="Tezina pripreme"
                        apiPath={ingredient_api_path}
                        viewMode={props.viewMode} />
                    <SelectField
                        label="Preparation Level"
                        value={recipe.preparationLevel}
                        name="preparationLevel"
                        onChange={item => handleOnSelectChange(item, "preparationLevel")}
                        type="Tezina pripreme"
                        apiPath={category_api_path}
                        viewMode={props.viewMode} />
                    <SelectField
                        label="Method"
                        value={recipe.preparationMethod}
                        name="preparationMethod"
                        type="Nacin pripreme"
                        apiPath={category_api_path}
                        onChange={item => dispatch(setState({ preparationMethod: item.id, preparationMethod: item }))}
                        viewMode={props.viewMode} />

                    <SelectField
                        label="Group"
                        value={recipe.group}
                        name="group"
                        apiPath={category_api_path}
                        onChange={item => dispatch(setState({ group: item.id, group: item }))}
                        viewMode={props.viewMode}
                        type="Grupa jela" />

                    <SelectField
                        label="Season"
                        value={recipe.season}
                        name="season"
                        apiPath={category_api_path}
                        onChange={item => dispatch(setState({ season: item }))}
                        viewMode={props.viewMode}
                        type="Sezona" />
                </Col>
                <Col md={8}>
                    <Form.Label className="form-label">Description</Form.Label>
                    <textarea
                        rows="4"
                        name="opis"
                        placeholder="Description..."
                        value={recipe.description}
                        onChange={e => dispatch(setState({ description: e.target.value }))}
                        disabled={props.viewMode}
                        className="comment-section form-control"
                    />
                    <Form.Label className="form-label">Method of Preparation</Form.Label>
                    <textarea
                        rows="9"
                        name="method"
                        placeholder="..."
                        value={recipe.method}
                        onChange={e => dispatch(setState({ method: e.target.value }))}
                        disabled={props.viewMode}
                        className="comment-section form-control"
                    />
                    <Form.Label className="form-label">Advice</Form.Label>
                    <textarea
                        rows="2"
                        name="advice"
                        placeholder="..."
                        value={recipe.advice}
                        onChange={e => dispatch(setState({ advice: e.target.value }))}
                        disabled={props.viewMode}
                        className="comment-section form-control"
                    />
                </Col>
            </Row>
            <FileUploader
                accept=".jpg,.png,.jpeg"
                label=""
                multiple
                updateFilesCb={updateUploadedFiles}
                errorMessage={imagesError}
                files={initialImages} />
        </Modal.Body>
        <Modal.Footer>
            <Button className="float-right submit-button" variant="primary" onClick={handleSubmit}>Save</Button>
        </Modal.Footer>
    </Modal>
    );
}

export default RecipeForm;
