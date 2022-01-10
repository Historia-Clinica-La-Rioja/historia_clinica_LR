import { setHeader, sgxFetchApi } from '../../libs/sgx/utils/sgxFetch';
import { retrieveToken, retrieveRefreshToken, saveTokens, clearTokens } from '../../libs/sgx/api/tokenStorage';
import { configureRefreshFetch } from 'refresh-fetch';


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

    fetch = configureRefreshFetch({
        fetch: fetchWithToken,
        shouldRefreshToken,
        refreshToken: doRefreshToken,
    });

}

export default new SgxApiRest();
