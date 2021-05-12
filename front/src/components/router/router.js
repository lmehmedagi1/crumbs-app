import React from 'react';
import {
    Route, Switch
} from "react-router-dom";
import ScrollToTop from 'components/utility/scrollToTop';
import Login from 'components/login/login';
import NotFound from 'components/common/notFound';

function Routes(props) {
  return (
    <ScrollToTop>
      <Switch>
        <Route exact path={"/"} render={(routeProps) => <Login {...routeProps} />}></Route>
        <Route><NotFound/></Route>
      </Switch>
    </ScrollToTop>
  )
}

export default Routes;