import { getUser, userIsLoggedIn } from 'api/auth'
import dietApi from 'api/diet'
import Alert from 'components/alert/alert'
import ConfirmationModal from 'components/common/confirmationModal'
import { CustomImage } from 'components/common/customImage'
import Menu from 'components/common/menu'
import RecipeCard from 'components/common/recipeCard'
import DietForm from 'components/diet/dietForm'
import moment from 'moment'
import React, { useEffect, useState } from 'react'
import { Link, withRouter } from 'react-router-dom'

function Diet(props) {

    const [loading, setLoading] = useState(false);

    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [variant, setVariant] = useState("");

    const [diet, setDiet] = useState(null);
    const [showDietModal, setShowDietModal] = useState(false);
    const [showConfirmationModal, setShowConfirmationModal] = useState(false);

    useEffect(() => {
        let dietId = window.location.pathname.split("/").pop();
        fetchDiet(dietId);
    }, []);

    const handleError = message => {
        setShow(true);
        setMessage(message);
        setVariant("warning");
    }

    const fetchDiet = (id) => {
        setLoading(true);
        if (userIsLoggedIn()) {
            dietApi.getPrivateDiet((res, err) => {
                setLoading(false);
                if (err) handleError(err);
                else setDiet(res);
            }, {id}, props.getToken(), props.setToken);
        }
        else {
            dietApi.getPublicDiet((diet, err) => {
                setLoading(false);
                if (err) handleError(err);
                else setDiet(diet);
            }, {id});
        }
    }

    const handleSearchChange = search => {
        props.history.push({
            pathname: '/browse',
            state: { search: search }
        });
    }

    const update = id => {
        fetchDiet(id);
    }

    const timestampToDateTime = timestamp => {
        const longDateTimeFormat = "MMMM Do YYYY, h:mm:ss a";
        return moment.utc(timestamp).local().format(longDateTimeFormat);
    }

    const handleDietDelete = () => {
        dietApi.deleteDiet((res, err) => {
            if (err) handleError(err);
            else {
                props.history.push({
                    pathname: '/'
                });
            }
        }, { id: diet.id }, props.getToken(), props.setToken);
    }

    return (
        <div className={loading ? "blockedWait" : ""}>
            <div className={loading ? "blocked" : ""}>
                <Menu handleSearchChange={handleSearchChange} {...props} update={update} />
                <Alert message={message} showAlert={show} variant={variant} onShowChange={setShow} />
                {diet && <div className="dietContainer">
                    <div className="title">{diet.title}</div>
                    <div className="grid">
                        <div className="information">
                            <div className="subtitle">Description</div>
                            <h2>{diet.description}</h2>
                            <h2><span>Duration: </span> {diet.duration} days</h2>
                            <h2><span>Created: </span> {timestampToDateTime(diet.createdAt)}</h2>

                            <div className="subtitle">Ingredients</div>
                            <h2>For this diet you will need:</h2>
                            <h2>
                            {diet.ingredients.map((ingredient, index) => (
                                <span>{ingredient.name} {(index < diet.ingredients.length-1) && ", "}</span>
                            ))}</h2>

                            <div className="subtitle">Author</div>
                            <div className="author-view">
                                <CustomImage imageId={diet.author.avatar} className="image-wrapper" alt="User avatar" />
                                <div>
                                    <h2><span>Name: </span>{diet.author.firstName + " " + diet.author.lastName}</h2>
                                    <h2><span>Username: </span> {diet.author.username}</h2>
                                    <h2><span>Email: </span> {diet.author.email}</h2>
                                </div>
                            </div>

                            {userIsLoggedIn() && getUser().id === diet.author.id && 
                            <div className="edit-buttons">
                                <button onClick={(() => setShowDietModal(true))}>EDIT</button>
                                <button onClick={(() => setShowConfirmationModal(true))}>DELETE</button>
                            </div>}
                        </div>
                        <div className="recipes">
                            {diet.recipes.map((recipe, index) => (
                                <Link to={"/recipe/" + recipe.recipeId}>
                                    <RecipeCard {...recipe}> </RecipeCard>
                                </Link>
                            ))}
                        </div>
                    </div>
                </div>}
                <DietForm show={showDietModal} title="Edit Diet" onHide={() => setShowDietModal(false)} diet={diet} getToken={props.getToken} setToken={props.setToken} update={update}/>
                <ConfirmationModal 
                    show={showConfirmationModal} onHide={() => setShowConfirmationModal(false)} 
                    title="Remove diet" message="Are you sure you want to remove this diet? This action cannot be undone."
                    onConfirm={handleDietDelete} confirmMessage="Delete"
                />
            </div>
        </div>
    )
}

export default withRouter(Diet);
