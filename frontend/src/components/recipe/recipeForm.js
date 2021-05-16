import React, { useState } from 'react';
import { Modal, Form, Row, Col } from "react-bootstrap";
import FileUploader from 'components/common/fileUploader/fileUploader'
import SelectField from 'components/common/selectField';


function RecipeForm(props) {

    const [method, setMethod] = useState()
    const [title, setTitle] = useState()
    const [preparation, setPreparation] = useState()
    const [meal, setMeal] = useState()
    const [type, setType] = useState()

    const [profileImages, setProfileImages] = useState([]);
    const [imagesError, setImagesError] = useState("");
    const [initialImages, setinitialImages] = useState({});

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
                            value={title}
                            onChange={e => setTitle(e.target.value)}
                            disabled={props.viewMode}
                        />
                    </Form.Group>
                    <SelectField
                        label="Preparation Time"
                        value={preparation}
                        name="selectFolder"
                        loadOptions={(inputValue, callback) => { callback(prepTimeoptions) }}
                        onChange={item => setPreparation(item)}
                        viewMode={props.viewMode} />
                    <SelectField
                        label="Meal of the Day"
                        value={meal}
                        isMulti={true}
                        name="selectFolder"
                        loadOptions={(inputValue, callback) => { callback(mealOptions) }}
                        onChange={item => setMeal(item)}
                        viewMode={props.viewMode} />

                    <SelectField
                        label="Meal Type"
                        value={type}
                        isMulti={true}
                        name="selectFolder"
                        loadOptions={(inputValue, callback) => { callback(typeOptions) }}
                        onChange={item => setType(item)}
                        viewMode={props.viewMode} />

                </Col>
                <Col md={6}>
                    <Form.Label className="form-label">Method of Preparation</Form.Label>
                    <textarea
                        rows="11"
                        name="Method"
                        placeholder="Specijalitet je nastao..."
                        value={method}
                        onChange={e => setMethod(e.target.value)}
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
