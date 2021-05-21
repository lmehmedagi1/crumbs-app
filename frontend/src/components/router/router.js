import React from 'react';
import {
  Route, Switch
} from "react-router-dom";
import ScrollToTop from 'components/utility/scrollToTop';

import NotFound from 'components/common/notFound';
import Login from 'components/login/login';
import Register from 'components/register/register';
import Home from 'components/home/home';
import Browse from 'components/browse/browse';
import Recipe from 'components/recipe/recipe';
import RecipePreview from 'components/recipe/recipePreview';

function Routes(props) {
  return (
    <ScrollToTop>
      <Switch>
        <Route exact path={"/"} render={(routeProps) => <Home {...routeProps} {...props} />}></Route>
        <Route exact path={"/login"} render={(routeProps) => <Login {...routeProps} {...props} />}></Route>
        <Route exact path={"/register"} render={(routeProps) => <Register {...routeProps} {...props} />}></Route>
        <Route exact path={"/browse"} render={(routeProps) => <Browse {...routeProps} {...props} />}></Route>
        <Route exact path={"/recipe"} render={(routeProps) => <Recipe {...routeProps} {...props} />}></Route>
        <Route exact path={"/recipe/:id"} render={(routeProps) => <RecipePreview {...routeProps} {...props} />}></Route>
        <Route><NotFound /></Route>
      </Switch>
    </ScrollToTop>
  )
}

export default Routes;
