import { setState } from 'actions/recipeActions'
import Menu from 'components/common/menu'
import RecipeCard from 'components/common/recipeCard'
import SelectField from 'components/common/selectField'
import RecipeForm from 'components/recipe/recipeForm'
import React, { useState } from 'react'
import { Button, CardGroup, Col, Container, Form, Row } from 'react-bootstrap'
import { BsPlusCircleFill } from "react-icons/bs"
import { useDispatch, useSelector } from 'react-redux'
import { Link, withRouter } from 'react-router-dom'

function Browse(props) {

    const recipe = useSelector(state => state.recipes.recipe);
    const dispatch = useDispatch()

    const [show, setShow] = useState(false);
    const [title, setTitle] = useState()

    const category_api_path = "recipe-service/categories/type";
    const ingredient_api_path = "recipe-service/ingredients/type";

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

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    const handleOnSelectChange = (value, name) => {
        value = value ? value : [];
        dispatch(setState({ [name]: value }))
    };


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
