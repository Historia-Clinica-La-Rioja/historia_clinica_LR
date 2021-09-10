import {setHeader, sgxFetchApi} from '../../libs/sgx/utils/sgxFetch';
import {configureRefreshFetch} from 'refresh-fetch';
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
            saveTokens(token, refreshToken)
        })
        .catch(error => {
            clearTokens()
            throw error
        })
}

const fetchWithToken = (url, options = {}) => {
    const token = retrieveToken();

    options.headers = setHeader('Authorization', `Bearer ${token}`, options.headers);

    return sgxFetchApi(url, options);
};


class SgxApiRest {
    _permission$ = undefined;

    login(username, password, raToken) {
        this.logout();

        const headers = setHeader('recaptcha', raToken);
        const options = {
            ...buildPostOptions({ username, password }),
            headers,
        };

        return sgxFetchApi('/auth', options)
            .then(({ token, refreshToken }) => {
                saveTokens(token, refreshToken);
            });

    }

    get permission$() {
        if (!this.isAuthenticated()) {
            return Promise.reject();
        }
        if(!this._permission$) {
            this._permission$ = Promise.all([this.fetch('/account/permissions'), this.fetch('/public/info')])
                .then(values => {
                    return new SGXPermissions({ roleAssignments: values[0].roleAssignments, featureFlags: values[1].features})
                })
        }

        return this._permission$;
    }

    isAuthenticated = () => Boolean(retrieveToken());

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
