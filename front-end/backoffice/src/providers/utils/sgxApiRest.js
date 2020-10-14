import { fetchUtils } from 'react-admin';

import SGXPermissions from './SGXPermissions';

const apiUrl = '/api';
const oauthInfo = 'oauth-info';
class SgxApiRest {
    _permission$ = undefined;

    auth(username, password, raToken) {
        this.logout();
        const options = {
            method: 'POST',
            headers: new Headers({ Accept: 'application/json',  recaptcha: raToken }),
            body: JSON.stringify({username, password})
        };
        return fetchUtils.fetchJson(`${apiUrl}/auth`, options)
            .then(response => {
                return response.json;
            }).then(({ token }) => {
                localStorage.setItem('token', token);
            });
    }

    get permission$() {
        const isTokenStored = !!localStorage.getItem('token');
        if (!isTokenStored) {
            return Promise.reject();
        }
        if (!this._permission$) {
            this._permission$ = this.fetch('/account/permissions')
                .then(({json}) => {
                    return new SGXPermissions(json);
                });
        }
        return this._permission$;
    }

    fetch(url, options = {}) {
        if (!options.headers) {
            options.headers = new Headers({ Accept: 'application/json' });
        }
        const token = localStorage.getItem('token');
        options.headers.set('X-Auth-Token', token);
        return fetchUtils.fetchJson(apiUrl + url, options);
    }

    logout() {
        localStorage.removeItem('token');
        this._permission$ = undefined;
    }

    loadInfo() {
        return fetchUtils.fetchJson(`${apiUrl}/oauth/config`, { method: 'GET' })
            .then(response => {
                return response.json;
            }).then((object) => {
                localStorage.setItem(oauthInfo, JSON.stringify({oauthConfig:object}));
            });
    }

    getInfo() {
        return JSON.parse(localStorage.getItem(oauthInfo));
    }

    isRecaptchaEnable(options = {}){
        if (!options.headers) {
            options.headers = new Headers({ Accept: 'application/json' });
        }
        return fetchUtils.fetchJson(apiUrl + '/recaptcha/is-enable', options);
    }

    getRecaptchaPublicConfig() {
        return fetchUtils.fetchJson(apiUrl + '/public/recaptcha', { method: 'GET' })
                .then(response => response.json);
    }

  }

  export default new SgxApiRest();
