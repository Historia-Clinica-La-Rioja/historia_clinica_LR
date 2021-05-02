
export const TOKEN_KEY_STORE = 'token';
export const TOKENREFRESH_KEY_STORE = 'refreshtoken';

export const retrieveToken = () => localStorage.getItem(TOKEN_KEY_STORE);
export const retrieveRefreshToken = () => localStorage.getItem(TOKENREFRESH_KEY_STORE);
