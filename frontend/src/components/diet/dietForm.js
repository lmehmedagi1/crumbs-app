import React, { useState, useEffect } from 'react'
import { withRouter } from 'react-router-dom'
import {  Button, Form, Modal } from 'react-bootstrap'
import { Formik } from 'formik'
import * as yup from 'yup'

import authApi, { getUser } from 'api/auth'
import Alert from 'components/alert/alert'
import ScrollButton from 'components/utility/scrollButton'
import auth from 'api/auth'

const schema = yup.object().shape({
    title: yup.string().required("*Title is required"),
    description: yup.string().required("*Description is required")
});

function DietForm(props) {

    const [loading, setLoading] = useState(false);

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    useEffect(() => {
        // To do: everything
    }, []);

    const handleSubmit = user => {
        setLoading(true);
        // To do: add or edit the diet
        setLoading(false);
    }

    const getInitialValues = () => {
        return {
            title: "",
            description: "",
            duration: 0
        }
    }

    return (
        <Modal
            show={props.show}
            onHide={props.onHide}
            backdrop="static"
            keyboard={false}
            className="dietFormModalContainer"
        >
            <Modal.Header closeButton>
            <Modal.Title>{props.title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
            <div className={loading ? "blockedWait" : ""}>
            <div className={loading ? "blocked" : ""}>
            <div className="dietForm">
                <Formik
                    validationSchema={schema}
                    initialValues={getInitialValues()}
                    onSubmit={handleSubmit}
                    enableReinitialize
                >
                    {( {handleSubmit, handleChange, touched, errors }) => (
                        <Form noValidate className="formBasic" onSubmit={handleSubmit}>

                            <Form.Group controlId="formBasicFirstName">
                                <Form.Label>Title</Form.Label>
                                <Form.Control type="text" name="title" defaultValue={"Title"} onChange={handleChange} isInvalid={touched.title && errors.title} />
                                <Form.Control.Feedback type="invalid">{errors.title}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="description">
                                <Form.Label>Description</Form.Label>
                                <Form.Control type="textarea" rows={3} name="description" defaultValue={"Description"} onChange={handleChange} isInvalid={touched.description && errors.description} />
                                <Form.Control.Feedback type="invalid">{errors.description}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="formBasicLastName">
                                <Form.Label>Duration</Form.Label>
                                <Form.Control type="number" name="duration" defaultValue={0} onChange={handleChange} isInvalid={touched.duration && errors.duration} />
                                <Form.Control.Feedback type="invalid">{errors.duration}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group id="formGridCheckbox">
                                <Form.Check type="checkbox" label="Check me out" />
                            </Form.Group>

                            <div>Select recipes nekako</div>

                            <Form.Group className="submitButtonWrapper">
                                <Button variant="primary" type="submit">
                                    SAVE
                                </Button>
                            </Form.Group>
                        </Form>
                    )}
                </Formik>
            </div>
            </div>
            </div>
            </Modal.Body>
        </Modal>
    )
}

export default withRouter(DietForm);
