import React, { useState } from 'react'
import { withRouter, Link } from 'react-router-dom'
import { Row, Col, Form, CardGroup, Container, Button } from 'react-bootstrap'
import Menu from 'components/common/menu'
import RecipeCard from 'components/common/recipeCard'
import SelectField from 'components/common/selectField';
import { BsPlusCircleFill } from "react-icons/bs"
import RecipeForm from 'components/recipe/recipeForm'
import { env } from 'configs/env'

function Browse(props) {

    const [show, setShow] = useState(false);
    const [title, setTitle] = useState()
    const [preparation, setPreparation] = useState()
    const [meal, setMeal] = useState()
    const [type, setType] = useState()
    const [products] = useState([
        {
            recipeName: "Ime recepta",
            text: "Learn to swim 1",
            author: "Autor 1",
            id: 1
        },
        {
            recipeName: "Ime recepta 2",
            text: "Learn to swim 2",
            author: "Autor 2",
            id: 2
        },
        {
            recipeName: "Ime recepta 3",
            text: "Learn to swim 3",
            author: "Autor 3",
            id: 3
        },
        {
            recipeName: "Ime recepta 4",
            text: "Learn to swim 3",
            author: "Autor 4",
            id: 4
        },
        {
            recipeName: "Ime recepta",
            text: "Learn to swim 1",
            author: "Autor 1",
            id: 1
        },
        {
            recipeName: "Ime recepta 2",
            text: "Learn to swim 2",
            author: "Autor 2",
            id: 2
        },
        {
            recipeName: "Ime recepta 3",
            text: "Learn to swim 3",
            author: "Autor 3",
            id: 3
        },
        {
            recipeName: "Ime recepta 4",
            text: "Learn to swim 3",
            author: "Autor 4",
            id: 4
        }

    ]);

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

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    return (
        <Container>
            <Menu handleSearchChange={handleSearchChange} {...props} />
            <Row>
                <Col md={4}>
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

                    <Button variant="outline-primary" className="addRecipe float-right" onClick={() => setShow(true)}>
                        <BsPlusCircleFill />
                    </Button>
                </Col>
                <Col md={8}>
                    <CardGroup >
                        {products.map(product => (
                            <Link style={{ margin: "1%" }} to={"recipe/" + product.id}>
                                <RecipeCard  {...product}> </RecipeCard>
                            </Link>
                        ))}
                    </CardGroup>
                </Col>
            </Row>
            <RecipeForm title="Create" show={show} onHide={() => setShow(false)} getToken={props.getToken} setToken={props.setToken} />
        </Container>
    )
}

export default withRouter(Browse);
