import { setHeader, sgxFetchApi } from '../../libs/sgx/utils/sgxFetch';
import { configureRefreshFetch } from 'refresh-fetch';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import roleAssignments from '../../libs/sgx/api/role-assignments'

import {
    retrieveToken,
    retrieveRefreshToken, 
    saveTokens,
    clearTokens,
} from '../../libs/sgx/api/tokenStorage';

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
        if (!this._permission$) {
            this._permission$ = Promise.all([
                roleAssignments(this.fetch('/account/permissions')), 
                this.fetch('/public/info'),
            ])
                .then(([{ roleAssignments }, { features }]) => {
                    return new SGXPermissions(roleAssignments, features)
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
