import React, { useState, useEffect } from 'react'
import { withRouter } from 'react-router-dom'
import {  Button, Form } from 'react-bootstrap'
import { Formik } from 'formik'
import * as yup from 'yup'

import authApi, { getUser } from 'api/auth'
import Alert from 'components/alert/alert'
import ScrollButton from 'components/utility/scrollButton'
import auth from 'api/auth'

const schema = yup.object().shape({
    username: yup.string().required("*Username is required"),
    password: yup.string().required("*Password is required").matches(/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!"#$%&'()*+,-./:;<=>?@\[\]^_`{|}~])(?=\S+$).{8,}$/, "*Password must be at least 8 characters long. There must be at least one digit, one lowercase and one uppercase letter, one special character and no whitespaces!")
});

const initialValues = {
    email: "",
    password: ""
}

function Login(props) {

    const [loading, setLoading] = useState(false);

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    useEffect(() => {
        if (getUser() != null) {
            props.history.push({
                pathname: '/'
            });
        }

        let searchArray = props.location.search.split('=');
        if (searchArray && searchArray.length > 1) {
            let verificationToken = searchArray[1]
            auth.confirmRegistration(() => {}, verificationToken);
        }
    }, []);

    const handleSubmit = user => {
        setLoading(true);
        setShow(false);
        
        authApi.login(token => {
            setLoading(false);
            props.setToken(token);
            props.history.push('/');
        }, err => {
            setLoading(false);
            setMessage(err);
            setVariant("warning");
            setShow(true);
            ScrollButton.scrollToTop();
        },  user);
    }

    const handleForgotPasswordCLick = () => {
        // To do: password reset logic
    }

    return (
        <div className={loading ? "blockedWait" : ""}>
        <div className={loading ? "blocked" : ""}>
            <Alert message={message} showAlert={show} variant={variant} onShowChange={setShow} />
            <div className="formContainer">
                <div>
                    <div className="formTitle">
                        LOGIN
                    </div>
                    <Formik
                        validationSchema={schema}
                        initialValues={initialValues}
                        onSubmit={handleSubmit}
                    >
                        {({
                            handleSubmit,
                            handleChange,
                            touched,
                            errors,
                        }) => (
                            <Form noValidate className="formBasic" onSubmit={handleSubmit}>
                                <Form.Group controlId="formBasicUsername">
                                    <Form.Label>Enter Username</Form.Label>
                                    <Form.Control type="email" name="username" onChange={handleChange} isInvalid={touched.username && errors.username} />
                                    <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group controlId="formBasicPassword">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control type="password" name="password" onChange={handleChange} isInvalid={touched.password && errors.password} />
                                    <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group controlId="formBasicCheckbox">
                                    <Form.Check type="checkbox" label="Remember me" />
                                </Form.Group>
                                <Form.Group>
                                    <Button variant="primary" type="submit">
                                        LOGIN
                                    </Button>
                                </Form.Group>
                                <div className="forgotPassword" onClick={handleForgotPasswordCLick}>Forgot Password?</div>
                            </Form>
                        )}
                    </Formik>
                </div>
            </div>
        </div>
        </div>
    )
}

export default withRouter(Login);
