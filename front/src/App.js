import React, { useState } from 'react';
import './App.scss'

import {
  BrowserRouter as Router
} from "react-router-dom";

import Routes from 'components/router/router'
import Footer from 'components/common/footer'
import Header from 'components/common/header'


function App() {
  
  return (
    <div className="App">
      <Router>
        <div>
          <Header />
          <Routes />
          <Footer />
        </div>
      </Router>
    </div>
  );
}

export default App;