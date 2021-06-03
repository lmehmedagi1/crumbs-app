import { clearState, getRecipes, setState } from 'actions/recipeActions'
import Menu from 'components/common/menu'
import { Paginator } from 'components/common/Paginator/Paginator'
import RecipeCard from 'components/common/recipeCard'
import SelectField from 'components/common/selectField'
import RecipeForm from 'components/recipe/recipeForm'
import { category_api_path } from 'configs/env'
import React, { useEffect, useState } from 'react'
import { Button, CardGroup, Col, Container, Form, Row } from 'react-bootstrap'
import { useDispatch, useSelector } from 'react-redux'
import { Link, withRouter } from 'react-router-dom'

function Browse(props) {

    const { recipe, recipes } = useSelector(state => state.recipes);
    const dispatch = useDispatch()

    const [show, setShow] = useState(false);
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(3);

    useEffect(() => {
        dispatch(clearState());
        var browseFilter = props.location.state
        if (browseFilter && browseFilter.search) {
            dispatch(setState({ title: browseFilter.search }))
            dispatch(getRecipes(getFilters(browseFilter.search), page, pageSize));
            props.history.replace('/browse');
        }
        else
            dispatch(getRecipes(getFilters(), page, pageSize));
    }, []);

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

    const onPageChange = page => {
        setPage(page);
        dispatch(getRecipes(getFilters(), page, pageSize));
    };

    const onPageSizeChange = pageSize => {
        var size = Math.ceil(recipes.length / pageSize);
        if (size > page) size = page;

        setPageSize(pageSize)
        dispatch(getRecipes(getFilters(), size, pageSize));
    };


    const getFilters = (param) => {
        var categoryKeys = ["preparationLevel", "group", "season", "preparationMethod"]
        var categories = categoryKeys.map(name => recipe[name] ? recipe[name].value : null).filter(x => x)

        return {
            categories,
            title: param ? param : recipe.title
        }
    }

    const handleSearch = () => dispatch(getRecipes(getFilters(), page, pageSize));


    return (
        <Container className="browseContainer">
            <Menu handleSearchChange={handleSearchChange} {...props} />
            <Row>
                <Col md={4}>
                    <Form.Group>
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            type="text"
                            name="Title"
                            placeholder="Title..."
                            value={recipe.title}
                            onChange={e => dispatch(setState({ title: e.target.value }))}
                            disabled={props.viewMode}
                        />
                    </Form.Group>
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

                    <Paginator
                        page={page}
                        pageSize={pageSize}
                        onPageChange={onPageChange}
                        onPageSizeChange={onPageSizeChange}
                    />
                    <Button variant="primary" className="search" onClick={handleSearch}>
                        Search
                    </Button>
                </Col>
                <Col md={8}>
                    <CardGroup >
                        {recipes.map(product => (
                            <Link style={{ margin: "1%" }} to={"recipe/" + product.recipeId}>
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
