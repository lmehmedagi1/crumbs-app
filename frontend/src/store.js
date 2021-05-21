import { configureStore } from '@reduxjs/toolkit';
import authReducer from 'reducers/authReducer';
import recipeReducer from './reducers/recipeReducer';
import { createStore, combineReducers, applyMiddleware, compose } from "redux";
import { composeWithDevTools } from 'redux-devtools-extension/developmentOnly';
import thunk from "redux-thunk";
import promise from "redux-promise-middleware";

const composeEnhancers = composeWithDevTools({});

const enhancer = composeEnhancers(
  applyMiddleware(thunk, promise)
);
export default createStore(
  combineReducers({
    recipes: recipeReducer,
    authReducer
  }),
  enhancer
);
