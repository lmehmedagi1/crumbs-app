import React, { useState } from 'react'
import { withRouter } from 'react-router-dom'
import { Row, Col } from 'react-bootstrap'
import Menu from 'components/common/menu'
import RecipeCard from 'components/common/recipeCard'
import 'components/home/home.scss'
import Button from 'react-bootstrap/Button'

function Home(props) {
    const [counter, setCounter] = useState(0);
    const [counterMP, setCounterMP] = useState(0);
    const [products, setProducts] = useState([
        {
            recepieName: "Ime recepta",
            text: "neki text bla bla bla bala 1",
            author: "neki autor 1",
        },
        {
            recepieName: "Ime recepta2",
            text: "neki text bla bla bla bala 2",
            author: "neki autor 2",
        },
        {
            recepieName: "Ime recepta3",
            text: "neki text bla bla bla bala3",
            author: "neki autor 3",
        }

    ]);

    const [productsMP, setProductsMP] = useState([
        {
            recepieName: "Ime recepta",
            text: "neki text bla bla bla bala 1",
            author: "neki autor 1",
        },
        {
            recepieName: "Ime recepta2",
            text: "neki text bla bla bla bala 2",
            author: "neki autor 2",
        },
        {
            recepieName: "Ime recepta3",
            text: "neki text bla bla bla bala3",
            author: "neki autor 3",
        },
        {
            recepieName: "Ime recepta4",
            text: "neki text bla bla bla bala3",
            author: "neki autor 4",
        }

    ]);

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    return (
        <div>
            
            <Menu handleSearchChange={handleSearchChange} {...props}/>
            <p>Home page</p>
            <Row>
                <h2 className="titleHome">Daily recepies</h2>
            </Row>
            <Row id="dailyGrid" >
                {products.map((product, index) => (
                        <RecipeCard {...product}> </RecipeCard>
                            
                        ))}
                <Button style={{height: "30px", width: "70px", alignSelf: "center"}}>Next</Button>
            </Row>
            <Row >
                <h2 className="titleHome">Most popular recepies</h2>
            </Row>
            <Row id="mostPopularGrid" >
                {productsMP.map((product, index) => (
                         <RecipeCard {...product}> </RecipeCard>
                            
                        ))}
                <Button style={{height: "30px", width: "70px", alignSelf: "center"}}>Next</Button>
            </Row>
        </div>
    )
}

export default withRouter(Home);
