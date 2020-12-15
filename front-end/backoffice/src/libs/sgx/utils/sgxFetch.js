/**
 * fetchUtils.fetchJson() function as HTTP client.
 * It’s similar to HTML5 fetch(), except it handles JSON decoding and HTTP error codes automatically.
 */
import { fetchUtils } from 'react-admin';

const API_CONTEXT_PATH = '/api';

const sgxFetch = (url, options = {}) => {

    options.headers = setHeader('Accept', 'application/json', options.headers);

    return fetchUtils.fetchJson(url, options)
        .then(response => response.json);
};

const sgxFetchApi = (url, options) => {
    return sgxFetch(API_CONTEXT_PATH + url, options);
};

const setHeader = (name, value, headers = new Headers()) => {
    headers.set(name, value);
    return headers;
};

export { sgxFetch, setHeader, sgxFetchApi };
