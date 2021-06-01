import React, { useEffect, useState } from 'react'
import { Form, Button, Modal } from 'react-bootstrap'
import { Formik } from "formik"
import * as yup from 'yup'
import { getUser } from 'api/auth'

import profileApi from 'api/profile'

const imagePlaceholder = "https://www.firstfishonline.com/wp-content/uploads/2017/07/default-placeholder-700x700.png";

const schema = yup.object().shape({
    email: yup.string().email("*Email must be valid").required("*Email is required"),
    firstName: yup.string().required("*First name is required")
        .matches(/^[^\p{P}\p{S}\s\d]*$/u,  "*First name can't contain special characters, numbers or whitespaces"),
    lastName: yup.string().required("*Last name is required")
        .matches(/^([^\p{P}\p{S}\s\d]+[ -]?[^\p{P}\p{S}\s\d]+)*$/u,  "*Last name can only contain characters and a space or dash"),
    phoneNumber: yup.string().required("*Phone number is required")
        .test("digits-only", "*Phone number can only contain digits", value => /^\d*$/.test(value))
        .max(16, "*Phone number can't be longer than 16 characters")
}); 

function EditProfileModal(props) {

    const [loading, setLoading] = useState(false);

    const [image, setImage] = useState("");
    const [base64URL, setBase64URL] = useState("");

    const getInitialValues = () => {

        if (getUser() == null) return;

        return {
            firstName: getUser().user_profile.first_name,
            lastName: getUser().user_profile.last_name,
            gender: getUser().user_profile.gender,
            phoneNumber: getUser().user_profile.phone_number ? getUser().user_profile.phone_number : '',
            email: getUser().email,
            username: getUser().username
        }
    }

    const handleSubmit = (user) => {

        user.gender = lowerFirstLetter(user.gender);

        setLoading(true);

        profileApi.updateUserInfo(() => {
            setLoading(false);
            props.handleProfileUpdate();
        }, user, props.getToken(), props.setToken);   
    }

    const getBase64 = file => {
        return new Promise(resolve => {
            let reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => {
                let baseURL = reader.result;
                resolve(baseURL);
            };
        });
    };

    const handleFileInputChange = e => {

        let file = e.target.files[0];

        if (file.type.substring(0, 6) != "image/") {
            props.setVariant("warning");
            props.setMessage("Invalid image type");
            props.setShow(true);
            return;
        }

        getBase64(file)
            .then(result => {
                file["base64"] = result;
                setBase64URL(result.substring(13 + file.type.length));
                setImage(file);
            })
            .catch(err => {
                props.setVariant("warning");
                props.setMessage("Image loading failed");
                props.setShow(true);
            });

        setImage(e.target.files[0]);
    }

    const capitalizeFirstLetter = (text) => {
        if (text == null || text == "") return "";
        return text.charAt(0).toUpperCase() + text.slice(1)
    }

    const lowerFirstLetter = (text) => {
        if (text == null || text == "") return "";
        return text.charAt(0).toLowerCase() + text.slice(1)
    }

    return (
        <div className="editProfileModalContainer">
        <Modal
            show={props.showModal}
            onHide={props.handleCloseEditModal}
            backdrop="static"
            keyboard={false}
            className="paymentModalContainer"
        >
            <Modal.Header closeButton>
            <Modal.Title>PROFILE</Modal.Title>
            </Modal.Header>
            <Modal.Body>
            <div className={loading ? "blockedWait" : ""}>
            <div className={loading ? "blocked" : ""}>
            <div className="editForm">
                <Formik
                    validationSchema={schema}
                    initialValues={getInitialValues()}
                    onSubmit={handleSubmit}
                    enableReinitialize
                >
                    {( {handleSubmit, handleChange, touched, errors }) => (
                        <Form noValidate className="formBasic" onSubmit={handleSubmit}>

                        <div className="image">
                            <div id="myFileDiv">
                                <img src={imagePlaceholder} alt="Profile image"/>
                                <input type="file" id="inputFile" onChange={handleFileInputChange} accept="image/*"/>
                                <label for="inputFile">CHANGE PHOTO</label>
                            </div>
                        </div>

                            <Form.Group controlId="formBasicFirstName">
                                <Form.Label>First Name</Form.Label>
                                <Form.Control type="text" name="firstName" defaultValue={getUser() && getUser().user_profile.first_name} onChange={handleChange} isInvalid={touched.firstName && errors.firstName} />
                                <Form.Control.Feedback type="invalid">{errors.firstName}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="formBasicLastName">
                                <Form.Label>Last Name</Form.Label>
                                <Form.Control type="text" name="lastName" defaultValue={getUser() && getUser().user_profile.last_name} onChange={handleChange} isInvalid={touched.lastName && errors.lastName} />
                                <Form.Control.Feedback type="invalid">{errors.lastName}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="formBasicGender">
                                <Form.Label>I am</Form.Label>
                                <Form.Control as="select" name="gender" onChange={handleChange} defaultValue={getUser() && capitalizeFirstLetter(getUser().user_profile.gender)}>
                                <option>Male</option>
                                <option>Female</option>
                                <option>Other</option>
                                </Form.Control>
                            </Form.Group>
                            
                            <Form.Group controlId="formBasicPhoneNumber">
                                <Form.Label>Phone Number</Form.Label>
                                <Form.Control type="text" name="phoneNumber" defaultValue={getUser() && getUser().user_profile.phone_number} onChange={handleChange} isInvalid={touched.phoneNumber && errors.phoneNumber} />
                                <Form.Control.Feedback type="invalid">{errors.phoneNumber}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="formBasicUsername">
                                <Form.Label>Username</Form.Label>
                                <Form.Control type="text" name="username" defaultValue={getUser() && getUser().username} onChange={handleChange} isInvalid={touched.username && errors.username} />
                                <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="formBasicEmail">
                                <Form.Label>Enter Email</Form.Label>
                                <Form.Control type="email" name="email" defaultValue={getUser() && getUser().email} onChange={handleChange} isInvalid={touched.email && errors.email} />
                                <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                            </Form.Group>

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
        </div>
    )
}

export default EditProfileModal;
