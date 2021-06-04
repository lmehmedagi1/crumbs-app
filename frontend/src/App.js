import Footer from 'components/common/footer';
import Header from 'components/common/header';
import Routes from 'components/router/router';
import React, { useState } from 'react';
import {
  BrowserRouter as Router
} from "react-router-dom";
import './App.scss';

function App(props) {

  const [token, setToken] = useState(null);
  const refreshToken = token => setToken(token);
  const getToken = () => token;


  return (
    <div className="App">
      <Router>
        <div>
          <Header {...props} />
          <Routes setToken={refreshToken} getToken={getToken} />
          <Footer />
        </div>
      </Router>
    </div>
  );
}

export default App;
