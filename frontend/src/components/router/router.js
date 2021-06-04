import Browse from 'components/browse/browse';
import NotFound from 'components/common/notFound';
import BrowseDiets from 'components/diet/browseDiets';
import Diet from 'components/diet/diet';
import Home from 'components/home/home';
import Login from 'components/login/login';
import People from 'components/people/people';
import Profile from 'components/profile/profile';
import RecipePreview from 'components/recipe/recipePreview';
import Register from 'components/register/register';
import PasswordReset from 'components/resetPassword/resetPassword';
import ScrollToTop from 'components/utility/scrollToTop';
import React from 'react';
import {
  Route, Switch
} from "react-router-dom";


function Routes(props) {
  return (
    <ScrollToTop>
      <Switch>
        <Route exact path={"/"} render={(routeProps) => <Home {...routeProps} {...props} />}></Route>
        <Route exact path={"/login"} render={(routeProps) => <Login {...routeProps} {...props} />}></Route>
        <Route exact path={"/register"} render={(routeProps) => <Register {...routeProps} {...props} />}></Route>
        <Route path={"/reset-password*"} render={(routeProps) => <PasswordReset {...routeProps} {...props} />}></Route>
        <Route exact path={"/browse"} render={(routeProps) => <Browse {...routeProps} {...props} />}></Route>
        <Route exact path={"/diets"} render={(routeProps) => <BrowseDiets {...routeProps} {...props} />}></Route>
        <Route exact path={"/diet/**"} render={(routeProps) => <Diet {...routeProps} {...props} />}></Route>
        <Route exact path={"/people"} render={(routeProps) => <People {...routeProps} {...props} />}></Route>
        <Route exact path={["/profile/*/about", "/profile/*/recipes", "/profile/*/diets", "/profile/*/subscriptions", "/profile/*/likes"]} render={(routeProps) => <Profile {...routeProps} {...props} />}></Route>
        <Route exact path={"/recipe/:id"} render={(routeProps) => <RecipePreview {...routeProps} {...props} />}></Route>
        <Route  render={(routeProps) => <NotFound {...routeProps} {...props} />}></Route>
      </Switch>
    </ScrollToTop>
  )
}

export default Routes;
