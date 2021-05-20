import { configureStore } from '@reduxjs/toolkit';
import authReducer from 'reducers/authReducer';
import recipeReducer from './reducers/recipeReducer';

export const store = configureStore({
  reducer: {
    recipes: recipeReducer,
    authReducer,
  },
});
