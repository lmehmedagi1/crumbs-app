import React from 'react';
import {
  Route, Switch
} from "react-router-dom";
import ScrollToTop from 'components/utility/scrollToTop'

import NotFound from 'components/common/notFound'
import Login from 'components/login/login'
import Register from 'components/register/register'
import Home from 'components/home/home'
import Browse from 'components/browse/browse'
import People from 'components/people/people'
import Profile from 'components/profile/profile'
import Recipe from 'components/recipe/recipe'
import RecipePreview from 'components/recipe/recipePreview'
import PasswordReset from 'components/resetPassword/resetPassword'

function Routes(props) {
  return (
    <ScrollToTop>
      <Switch>
        <Route exact path={"/"} render={(routeProps) => <Home {...routeProps} {...props} />}></Route>
        <Route exact path={"/login"} render={(routeProps) => <Login {...routeProps} {...props} />}></Route>
        <Route exact path={"/register"} render={(routeProps) => <Register {...routeProps} {...props} />}></Route>
        <Route path={"/reset-password*"} render={(routeProps) => <PasswordReset {...routeProps} {...props} />}></Route>
        <Route exact path={"/browse"} render={(routeProps) => <Browse {...routeProps} {...props} />}></Route>
        <Route exact path={"/people"} render={(routeProps) => <People {...routeProps} {...props} />}></Route>
        <Route exact path={["/profile/*/about", "/profile/*/recipes", "/profile/*/diets", "/profile/*/subscriptions", "/profile/*/likes"]} render={(routeProps) => <Profile {...routeProps} {...props} />}></Route>
        <Route exact path={"/recipe"} render={(routeProps) => <Recipe {...routeProps} {...props} />}></Route>
        <Route exact path={"/recipe/:id"} render={(routeProps) => <RecipePreview {...routeProps} {...props} />}></Route>
        <Route  render={(routeProps) => <NotFound {...routeProps} {...props} />}></Route>
      </Switch>
    </ScrollToTop>
  )
}

export default Routes;
