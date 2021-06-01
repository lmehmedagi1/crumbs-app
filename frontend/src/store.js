import { applyMiddleware, combineReducers, createStore } from "redux";
import { composeWithDevTools } from 'redux-devtools-extension/developmentOnly';
import promise from "redux-promise-middleware";
import thunk from "redux-thunk";
import recipeReducer from './reducers/recipeReducer';

const composeEnhancers = composeWithDevTools({});

const enhancer = composeEnhancers(
  applyMiddleware(thunk, promise),

);
export default createStore(
  combineReducers({
    recipes: recipeReducer
  }),
  enhancer
);
