import { getDailyRecipes, getMostPopularRecipes } from 'actions/recipeActions';
import { listFiles } from 'components/common/dropbox';
import Menu from 'components/common/menu';
import RecipeCard from 'components/common/recipeCard';
import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, withRouter } from 'react-router-dom';

function Home(props) {
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
        <div>
            <Menu handleSearchChange={handleSearchChange} {...props} />
            <div className="home-container">
            <div className="headerText">Daily Recipes</div>

            <div id="dailyGrid">
                <button disabled={countDaily === 0} onClick={btnOnClickDailyLeft} className="nextBtn">
                    <i className={countDaily > 0 ? "bi bi-chevron-left" : "bi bi-chevron-left disabled"}></i>
                </button>
                {dailyRecipes.map(recipe => (
                    <Link to={"recipe/" + recipe.recipeId}>
                        <RecipeCard {...recipe}> </RecipeCard>
                    </Link>

                ))}
                <button onClick={btnOnClickDailyRight} disabled={countDaily > 0} className="nextBtn">
                    <i className={countDaily == 0 ? "bi bi-chevron-right" : "bi bi-chevron-right disabled"}></i>
                </button>
            </div>

            <div className="headerText">Most Popular Recipes</div>
            <div id="mostPopularGrid" >
                <button disabled={count === 0} onClick={btnOnClickLeft} className="nextBtn">
                    <i className={count > 0 ? "bi bi-chevron-left" : "bi bi-chevron-left disabled"}></i>
                </button>
                {mostPopularRecipes.map(recipe => (
                    <Link to={"recipe/" + recipe.recipeId}>
                        <RecipeCard {...recipe}> </RecipeCard>
                    </Link>))
                }
                <button onClick={btnOnClickRight} disabled={count > 0} className="nextBtn">
                    <i className={count == 0 ? "bi bi-chevron-right" : "bi bi-chevron-right disabled"}></i>
                </button>
            </div>
            </div>
        </div>
    )
}

export default withRouter(Home);
