
const USER_AUTH_KEY_STORE = 'userAuth';

const clearUserAuth = () => {
    localStorage.setItem(USER_AUTH_KEY_STORE, "false");
}
const saveUserAuth = () => {
    localStorage.setItem(USER_AUTH_KEY_STORE, "true");
}

const retrieveUserAuth = () => {
    return localStorage.getItem(USER_AUTH_KEY_STORE) !== "false";
}

export { 
    clearUserAuth,
    retrieveUserAuth,
    saveUserAuth,
};
