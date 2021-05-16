import React, { useState } from 'react'
import { withRouter } from 'react-router-dom'
import { Row, Col, Form} from 'react-bootstrap'
import Menu from 'components/common/menu'
import RecipeCard from 'components/common/recipeCard'
import SelectField from 'components/common/selectField';
import 'components/browse/browse.scss'

function Browse(props) {
    const [method, setMethod] = useState()
    const [title, setTitle] = useState()
    const [preparation, setPreparation] = useState()
    const [meal, setMeal] = useState()
    const [type, setType] = useState()
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
        },
        {
            recepieName: "Ime recepta3",
            text: "neki text bla bla bla bala3",
            author: "neki autor 3",
        },
        {
            recepieName: "Ime recepta3",
            text: "neki text bla bla bla bala3",
            author: "neki autor 3",
        },
        {
            recepieName: "Ime recepta3",
            text: "neki text bla bla bla bala3",
            author: "neki autor 3",
        },
        {
            recepieName: "Ime recepta3",
            text: "neki text bla bla bla bala3",
            author: "neki autor 3",
        },
        {
            recepieName: "Ime recepta3",
            text: "neki text bla bla bla bala3",
            author: "neki autor 3",
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
        <div>
            <Menu handleSearchChange={handleSearchChange} {...props}/>
            <p>Browse page</p>

            <Row>
                <Col md={3} className="filterCol">
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

                </Col>
                <Col md={9}>
                <div id="dailyGridRecepies">
                {products.map((product, index) => (
                        <RecipeCard {...product}> </RecipeCard>
                            
                        ))}
                </div>
                
                </Col>
            </Row>
        </div>
    )
}

export default withRouter(Browse);
