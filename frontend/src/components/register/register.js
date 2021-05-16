import React, { useState, useEffect } from 'react'
import { Link, withRouter } from 'react-router-dom'
import { Form, Button } from 'react-bootstrap'
import { Formik } from "formik"
import * as yup from 'yup'

const schema = yup.object().shape({
    firstName: yup.string().required("*First name is required")
        .matches(/^[^\p{P}\p{S}\s\d]*$/u,  "*First name can't contain special characters, numbers or whitespaces"),
    lastName: yup.string().required("*Last name is required")
        .matches(/^([^\p{P}\p{S}\s\d]+[ -]?[^\p{P}\p{S}\s\d]+)*$/u,  "*Last name can only contain characters and a space or dash"),
    email: yup.string().email("*Email must be valid").required("*Email is required"),
    username: yup.string().required("*Username is required"),
    password: yup.string().required("*Password is required").matches(/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!"#$%&'()*+,-./:;<=>?@\[\]^_`{|}~])(?=\S+$).{8,}$/, "*Password must be at least 8 characters long. There must be at least one digit, one lowercase and one uppercase letter, one special character and no whitespaces!")
});

const initialValues = {
    firstName: "",
    lastName: "",
    email: "",
    username: "",
    password: ""
}

function Register(props) {

    const [loading, setLoading] = useState(false);

    useEffect(() => {
        // To do: ako je korisnik prijavljen, redirect ga na pocetnu
    }, []);

    const handleSubmit = user => {
        console.log("Lejla kraljina")
    }

    const handleSearchChange = search => {
        console.log("Lejla kraljina 2")
    }

    return (
        <div className={loading ? "blockedWait" : ""}>
        <div className={loading ? "blocked" : ""}>
            <div className="formContainer">
                <div>
                    <div className="formTitle">
                        REGISTER
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
                                <Form.Group controlId="formBasicFirstName">
                                    <Form.Label>First Name</Form.Label>
                                    <Form.Control type="text" name="firstName" onChange={handleChange} isInvalid={touched.firstName && errors.firstName} />
                                    <Form.Control.Feedback type="invalid">{errors.firstName}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group controlId="formBasicLastName">
                                    <Form.Label>Last Name</Form.Label>
                                    <Form.Control type="text" name="lastName" onChange={handleChange} isInvalid={touched.lastName && errors.lastName} />
                                    <Form.Control.Feedback type="invalid">{errors.lastName}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group controlId="formBasicEmail">
                                    <Form.Label>Enter Email</Form.Label>
                                    <Form.Control type="email" name="email" onChange={handleChange} isInvalid={touched.email && errors.email} />
                                    <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group controlId="formBasicUsername">
                                    <Form.Label>Username</Form.Label>
                                    <Form.Control type="text" name="username" onChange={handleChange} isInvalid={touched.username && errors.username} />
                                    <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group controlId="formBasicPassword">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control type="password" name="password" onChange={handleChange} isInvalid={touched.password && errors.password} />
                                    <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group>
                                    <Button id="registerButton" variant="primary" type="submit">
                                        REGISTER
                                    </Button>
                                </Form.Group>
                                <Form.Group>
                                    <p>Already have an account? <span> </span>
                                        <Link className="formLink" to={'/login'}>
                                            Login
                                        </Link>
                                    </p>
                                </Form.Group>

                            </Form>
                        )}
                    </Formik>
                </div>
            </div>
        </div>
        </div>
    )
}

export default withRouter(Register);
