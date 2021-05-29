import Menu from 'components/common/menu'
import RecipeCard from 'components/common/recipeCard'
import 'components/home/home.scss'
import axios from "axios";
import RecipeForm from 'components/recipe/recipeForm'
import React, { useState, useEffect } from 'react'
import { CardGroup, Container, Row } from 'react-bootstrap'
import Button from 'react-bootstrap/Button'
import { BsPlusCircleFill } from "react-icons/bs"
import { ImArrowRight, ImArrowLeft } from "react-icons/im"
import { Link, withRouter } from 'react-router-dom'
import { getDailyRecipes, getMostPopularRecipes } from 'actions/recipeActions'
import { useSelector, useDispatch } from 'react-redux'
import { listFiles } from 'components/common/dropbox'



function Home(props) {
    const [show, setShow] = useState(false);
    const [count, setCount] = useState(0);
    const [countDaily, setDailyCount] = useState(0)
    const mostPopularRecipes = useSelector(state => state.recipes.mostPopularRecipes);
    const dailyRecipes = useSelector(state => state.recipes.dailyRecipes);
    const dispatch = useDispatch()
    useEffect(() => {
        dispatch(getMostPopularRecipes(count))
        dispatch(getDailyRecipes(countDaily))
        listFiles()
    }, []);

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    const btnOnClickRight = () => {
        dispatch(getMostPopularRecipes(count + 1))
        setCount(count + 1);
    }

    const btnOnClickLeft = () => {
        dispatch(getMostPopularRecipes(count - 1))
        setCount(count - 1);
    }

    const btnOnClickDailyRight = () => {
        dispatch(getDailyRecipes(countDaily + 1))
        setDailyCount(countDaily + 1);
    }

    const btnOnClickDailyLeft = () => {
        dispatch(getDailyRecipes(countDaily - 1))
        setDailyCount(countDaily - 1);
    }


    return (
        <Container className="container">
            <Menu handleSearchChange={handleSearchChange} {...props} />

            <Button variant="outline-primary" className="addRecipe float-right" onClick={() => setShow(true)}>
                <BsPlusCircleFill />
            </Button>

            <Row className="headerText">Daily Recipes</Row>

            <CardGroup id="dailyGrid">
                {countDaily > 0 ? <Button onClick={btnOnClickDailyLeft} variant="outline-primary" className="nextBtn">
                    <ImArrowLeft />
                </Button> : null}
                {dailyRecipes.map(recipe => (
                    <Link to={"recipe/" + recipe.recipeId}>
                        <RecipeCard {...recipe}> </RecipeCard>
                    </Link>

                ))}
                {countDaily == 0 ? <Button onClick={btnOnClickDailyRight} variant="outline-primary" className="nextBtn">
                    <ImArrowRight />
                </Button> : null}
            </CardGroup>

            <Row className="headerText">Most Popular Recipes</Row>
            <Row id="mostPopularGrid" >
                {count > 0 ? <Button onClick={btnOnClickLeft} variant="outline-primary" className="nextBtn" >
                    <ImArrowLeft />
                </Button> : null}
                {mostPopularRecipes.map(recipe => (
                    <Link to={"recipe/" + recipe.recipeId}>
                        <RecipeCard {...recipe}> </RecipeCard>
                    </Link>))}

                {count == 0 ? <Button onClick={btnOnClickRight} variant="outline-primary" className="nextBtn" >
                    <ImArrowRight />
                </Button> : null}
            </Row>

            <RecipeForm title="Create New Recipe" show={show} onHide={() => setShow(false)}
                getToken={props.getToken} setToken={props.setToken} />
        </Container>
    )
}

export default withRouter(Home);
