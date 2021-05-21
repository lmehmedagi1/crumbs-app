import React, { useState } from 'react';
import './App.scss'

import {
  BrowserRouter as Router
} from "react-router-dom";

import Routes from 'components/router/router'
import Footer from 'components/common/footer'
import Header from 'components/common/header'


function App() {
  
  const [token, setToken] = useState(null);

  const refreshToken = (token) => {
    setToken(token);
  }

  const getToken = () => {
    return token;
  }

  return (
    <div className="App">
      <Router>
        <div>
          <Header />
          <Routes setToken={refreshToken} getToken={getToken}/>
          <Footer />
        </div>
      </Router>
    </div>
  );
}

export default App;
