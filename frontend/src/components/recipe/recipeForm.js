import React, { useState } from 'react';
import { Modal, Form, Row, Col } from "react-bootstrap";
import FileUploader from 'components/common/fileUploader/fileUploader'
import SelectField from 'components/common/selectField';
import { useSelector, useDispatch } from 'react-redux'
import { setState } from 'actions/recipeActions';

function RecipeForm(props) {

    const recipe = useSelector(state => state.recipes.recipe);
    const dispatch = useDispatch()

    const [, setProfileImages] = useState([]);
    const [imagesError, setImagesError] = useState("");
    const [initialImages] = useState({});

    const updateUploadedFiles = (files) => {
        setProfileImages(files);
        setImagesError("");
    }

    // will be fetched from server
    const prepTimeoptions = [
        { value: '30min', label: '< 30 min' },
        { value: '30-60m', label: '30 - 60 min' },
        { value: '1h', label: '1h+' },
    ]

    const mealOptions = [
        { value: 'b', label: 'Breakfast' },
        { value: 'l', label: 'Lunch' },
        { value: 'd', label: 'Dinner' },
    ]

    const typeOptions = [
        { value: 'v', label: 'Vegan' },
        { value: 'g', label: 'Gluten Free' }
    ]
    return (<Modal show={props.show} onHide={props.onHide} size="xl" scrollable >
        <Modal.Header><Form.Label>{props.title}</Form.Label></Modal.Header>
        <Modal.Body>
            <Row>
                <Col md={6}>
                    <Form.Group>
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            type="text"
                            name="Title"
                            placeholder="Recept..."
                            value={recipe.title}
                            onChange={e => dispatch(setState({ title: e.target.value }))}
                            disabled={props.viewMode}
                        />
                    </Form.Group>
                    <SelectField
                        label="Preparation Time"
                        value={recipe.preparation}
                        name="selectFolder"
                        loadOptions={(inputValue, callback) => { callback(prepTimeoptions) }}
                        onChange={item => dispatch(setState({ preparation: item }))}
                        viewMode={props.viewMode} />
                    <SelectField
                        label="Meal of the Day"
                        value={recipe.meal}
                        isMulti={true}
                        name="selectFolder"
                        loadOptions={(inputValue, callback) => { callback(mealOptions) }}
                        onChange={item => dispatch(setState({ meal: item }))}
                        viewMode={props.viewMode} />

                    <SelectField
                        label="Meal Type"
                        value={recipe.type}
                        isMulti={true}
                        name="selectFolder"
                        loadOptions={(inputValue, callback) => { callback(typeOptions) }}
                        onChange={item => dispatch(setState({ type: item }))}
                        viewMode={props.viewMode} />

                </Col>
                <Col md={6}>
                    <Form.Label className="form-label">Method of Preparation</Form.Label>
                    <textarea
                        rows="11"
                        name="Method"
                        placeholder="Specijalitet je nastao..."
                        value={recipe.method}
                        onChange={e => dispatch(setState({ method: e.target.value }))}
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
    </Modal>
    );
}

export default RecipeForm;
