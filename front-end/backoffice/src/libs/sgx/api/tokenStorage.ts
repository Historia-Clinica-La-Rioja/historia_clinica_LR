

const TOKEN_KEY_STORE = 'token';
const TOKENREFRESH_KEY_STORE = 'refreshtoken';

const retrieveToken = () => localStorage.getItem(TOKEN_KEY_STORE)
const retrieveRefreshToken = () => localStorage.getItem(TOKENREFRESH_KEY_STORE)
const saveTokens = (token: string, refreshToken: string) => {
    localStorage.setItem(TOKEN_KEY_STORE, token);
    localStorage.setItem(TOKENREFRESH_KEY_STORE, refreshToken);
}
const clearTokens = () => {
    localStorage.removeItem(TOKEN_KEY_STORE);
    localStorage.removeItem(TOKENREFRESH_KEY_STORE);
}

export { saveTokens, retrieveToken, clearTokens, retrieveRefreshToken };
