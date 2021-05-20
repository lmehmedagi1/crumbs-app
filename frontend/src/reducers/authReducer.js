import { setUserSession, removeUserSession } from 'api/auth';

const initialState = {
    auth: {
        token: null,
        errorMessage: null
    }
};

const authReducer = (state = { ...initialState }, action) => {
    console.log("Akcija: " + action.type);
    switch (action.type) {
        case "AUTH_SET_STATE":
            return Object.assign({}, state, {
                auth: {
                    ...state.auth,
                    ...action.payload,
                },
            });
            break;
        case "AUTH_LOGIN_FULFILLED":
            setUserSession(action.payload.data);
            console.log(action.payload);
            return Object.assign({}, state, {
                auth: Object.assign({}, state.auth, {
                    ...initialState.auth,
                    ...action.payload.headers.authorization,
                }),
            });
            break;
        case "AUTH_LOGIN_REJECTED": 
            console.log(action.payload);
            return Object.assign({}, state, {
                auth: Object.assign({}, state.auth, {
                    token: null,
                    errorMessage: action.payload.data.message
                }),
            });
            break;
        case "AUTH_LOGOUT_FULFILLED":
            removeUserSession();
            return initialState;
            break;
        case "AUTH_LOGOUT_REJECTED":
            removeUserSession();
            return initialState;
            break;

        default:
            return state;
            break;
    }
}

export default authReducer;
