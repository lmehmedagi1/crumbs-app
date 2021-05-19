import Menu from 'components/common/menu'
import RecipeCard from 'components/common/recipeCard'
import 'components/home/home.scss'
import RecipeForm from 'components/recipe/recipeForm'
import React, { useState } from 'react'
import { CardGroup, Container, Row } from 'react-bootstrap'
import Button from 'react-bootstrap/Button'
import { BsPlusCircleFill } from "react-icons/bs"
import { ImArrowRight } from "react-icons/im"
import { Link, withRouter } from 'react-router-dom'

function Home(props) {
    const [show, setShow] = useState(false);
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
        }
    ]);

    const [popularProducts, setProductsMP] = useState([
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

    return (
        <Container className="container">
            <Menu handleSearchChange={handleSearchChange} {...props} />

            <Button variant="outline-primary" className="addRecipe float-right" onClick={() => setShow(true)}>
                <BsPlusCircleFill />
            </Button>

            <Row className="headerText">Daily Recipes</Row>

            <CardGroup id="dailyGrid">
                {products.map(product => (
                    <Link to={"recipe/" + product.id}>
                        <RecipeCard {...product}> </RecipeCard>
                    </Link>

                ))}
                <Button variant="outline-primary" className="nextBtn">
                    <ImArrowRight />
                </Button>
            </CardGroup>

            <Row className="headerText">Most Popular Recipes</Row>
            <Row id="mostPopularGrid" >
                {popularProducts.map(product => (
                    <Link to={"recipe/" + product.id}>
                        <RecipeCard {...product}> </RecipeCard>
                    </Link>))}

                <Button variant="outline-primary" className="nextBtn" >
                    <ImArrowRight />
                </Button>
            </Row>

            <RecipeForm title="Create New Recipe" show={show} onHide={() => setShow(false)} />
        </Container>
    )
}

export default withRouter(Home);
