import { setHeader, sgxFetchApi } from '../../libs/sgx/utils/sgxFetch';

import SGXPermissions from './SGXPermissions';

const TOKEN_KEY_STORE = 'token';
const TOKENREFRESH_KEY_STORE = 'refreshtoken';

const retrieveToken = () => localStorage.getItem(TOKEN_KEY_STORE)
const saveTokens = (token, refreshToken) => {
    localStorage.setItem(TOKEN_KEY_STORE, token);
    localStorage.setItem(TOKENREFRESH_KEY_STORE, refreshToken);
}
const clearTokens = () => {
    localStorage.removeItem(TOKEN_KEY_STORE);
    localStorage.removeItem(TOKENREFRESH_KEY_STORE);
}

const buildPostOptions = body => ({ method: 'POST', body: JSON.stringify(body) });

class SgxApiRest {
    _permission$ = undefined;

    login(username, password, raToken) {
        this.logout();

        let options = buildPostOptions({ username, password });
        options.headers = setHeader('recaptcha', raToken, options.headers);

        return sgxFetchApi('/auth', options)
            .then(({ token, refreshToken }) => {
                saveTokens(token, refreshToken);
            });

    }

    get permission$() {
        if (!this.isAuthenticated()) {
            return Promise.reject();
        }
        if (!this._permission$) {
            this._permission$ = this.fetch('/account/permissions')
                .then((json) => {
                    return new SGXPermissions(json);
                });
        }
        return this._permission$;
    }

    isAuthenticated() {
        const isTokenStored = !!retrieveToken();
        return isTokenStored;
    }

    fetch(url, options = {}) {
        const token = retrieveToken();
        options.headers = setHeader('X-Auth-Token', token, options.headers);
        return sgxFetchApi(url, options);
    }

    logout() {
        clearTokens();
        this._permission$ = undefined;
    }

  }

  export default new SgxApiRest();
