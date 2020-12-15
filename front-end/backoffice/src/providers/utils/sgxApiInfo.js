import { sgxFetchApi } from '../../libs/sgx/utils/sgxFetch';

const OAUTH_INFO = 'oauth-info';

const loadInfo = () => {
    return sgxFetchApi('/oauth/config', { method: 'GET' })
        .then((object) => {
            localStorage.setItem(OAUTH_INFO, JSON.stringify({oauthConfig:object}));
        });
}

const getInfo = () => {
    return JSON.parse(localStorage.getItem(OAUTH_INFO));
}

const getRecaptchaPublicConfig = () => {
    return sgxFetchApi('/public/recaptcha', { method: 'GET' });
}

export {loadInfo, getInfo, getRecaptchaPublicConfig};
