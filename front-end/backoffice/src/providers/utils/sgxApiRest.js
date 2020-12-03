import { setHeader, sgxFetchApi } from '../../libs/sgx/utils/sgxFetch';
import { configureRefreshFetch } from 'refresh-fetch';
import SGXPermissions from './SGXPermissions';

const TOKEN_KEY_STORE = 'token';
const TOKENREFRESH_KEY_STORE = 'refreshtoken';

const retrieveToken = () => localStorage.getItem(TOKEN_KEY_STORE)
const retrieveRefreshToken = () => localStorage.getItem(TOKENREFRESH_KEY_STORE)
const saveTokens = (token, refreshToken) => {
    localStorage.setItem(TOKEN_KEY_STORE, token);
    localStorage.setItem(TOKENREFRESH_KEY_STORE, refreshToken);
}
const clearTokens = () => {
    localStorage.removeItem(TOKEN_KEY_STORE);
    localStorage.removeItem(TOKENREFRESH_KEY_STORE);
}

const buildPostOptions = body => ({ method: 'POST', body: JSON.stringify(body) });

const shouldRefreshToken = error => error.status === 401;

const doRefreshToken = () => {
    const refreshToken = retrieveRefreshToken();
    const options = buildPostOptions({ refreshToken });
    return sgxFetchApi('/auth/refresh', options)
        .then(({ token, refreshToken }) => {
            console.log('response', { token, refreshToken });
            saveTokens(token, refreshToken)
        })
        .catch(error => {
            clearTokens()
            throw error
        })
}

const fetchWithToken = (url, options = {}) => {
    const token = retrieveToken();

    options.headers = setHeader('X-Auth-Token', token, options.headers);

    return sgxFetchApi(url, options);
};


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

    fetch = configureRefreshFetch({
        fetch: fetchWithToken,
        shouldRefreshToken,
        refreshToken: doRefreshToken,
    });

    logout() {
        clearTokens();
        this._permission$ = undefined;
    }

  }

  export default new SgxApiRest();
