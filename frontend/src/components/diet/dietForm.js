import dietApi from 'api/diet'
import Alert from 'components/alert/alert'
import SelectField from 'components/common/selectField'
import { Formik } from 'formik'
import React, { useEffect, useState } from 'react'
import { Button, Form, Modal } from 'react-bootstrap'
import { withRouter } from 'react-router-dom'
import * as yup from 'yup'

const schema = yup.object().shape({
    title: yup.string().required("*Title is required"),
    description: yup.string().required("*Description is required"),
    duration: yup.number().required("*Duration is required").positive("*Duration can't be negative").integer("*Duration can't be rational")
});

function DietForm(props) {

    const [loading, setLoading] = useState(false);

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    const [recipes, setRecipes] = useState([]);

    useEffect(() => {
        if (props.diet && props.diet.recipes) {
            let initialRecipes = props.diet.recipes;
            let formatRecipes = initialRecipes.map(recipe => {return {value: recipe.recipeId, label: recipe.title}});
            setRecipes(formatRecipes);
        }
    }, []);

    const handleError = message => {
        setShow(true);
        setMessage(message);
        setVariant("warning");
    }

    const handleSubmit = diet => {
        setLoading(true);
        if (props.title.includes("Edit")) {
            let params = {
                ...diet,
                id: props.diet.id,
                recipes: recipes.map(recipe => recipe.value)
            }
            dietApi.updateDiet((res, err) => {
                setLoading(false);
                if (err) handleError(err);
                else props.update(diet.id);
            }, params, props.getToken(), props.setToken);
        }
        else {
            let params = {
                ...diet,
                recipes: recipes.map(recipe => recipe.value)
            }
            dietApi.createDiet((res, err) => {
                setLoading(false);
                if (err) handleError(err);
                else props.onSuccess();
            }, params, props.getToken(), props.setToken);
        }
    }

    const getInitialValues = () => {
        return {
            title: props.diet ? props.diet.title : "",
            description: props.diet ? props.diet.description : "",
            duration: props.diet ? props.diet.duration : 0,
            isPrivate: props.diet ? props.diet.private : false
        }
    }

    const handleOnSelectChange = (value, name) => {
        value = value ? value : [];
        setRecipes(value);
    };

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

                            <Alert message={message} showAlert={show} variant={variant} onShowChange={setShow} />           

                            <Form.Group controlId="formBasicTitle">
                                <Form.Label>Title</Form.Label>
                                <Form.Control type="text" name="title" defaultValue={props.diet && props.diet.title} onChange={handleChange} isInvalid={touched.title && errors.title} />
                                <Form.Control.Feedback type="invalid">{errors.title}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="formBasicDescription">
                                <Form.Label>Description</Form.Label>
                                <Form.Control as="textarea" rows={3} name="description" defaultValue={props.diet && props.diet.description} onChange={handleChange} isInvalid={touched.description && errors.description} />
                                <Form.Control.Feedback type="invalid">{errors.description}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group controlId="formBasicDuration">
                                <Form.Label>Duration (in days)</Form.Label>
                                <Form.Control type="number" name="duration" defaultValue={props.diet && props.diet.duration} onChange={handleChange} isInvalid={touched.duration && errors.duration} />
                                <Form.Control.Feedback type="invalid">{errors.duration}</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group id="formGridCheckbox">
                                <Form.Check type="checkbox" label="Is private" id="isPrivate" defaultChecked={props.diet && props.diet.private} onChange={handleChange} isInvalid={touched.isPrivate && errors.isPrivate}/>
                            </Form.Group>
                            <p>Check diet as private if you don't want other users to see it</p>

                            <Form.Group id="formGridRecipes">
                            <SelectField
                                label="Recipes"
                                value={recipes}
                                name="Recipes"
                                isMulti={true}
                                onChange={item => handleOnSelectChange(item, "recipes")}
                                type="Tezina pripreme"
                                apiPath={"recipe-service/recipes/type"}
                                viewMode={props.viewMode} />
                            </Form.Group>

                            <Form.Group className="submit-button-wrapper">
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
