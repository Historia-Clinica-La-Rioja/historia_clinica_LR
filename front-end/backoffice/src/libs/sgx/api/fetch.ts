/**
 * HTTP clients
 * 
 * It handles JSON decoding. HTTP error will be translated to ApiHttpError.
 */
import { configureRefreshFetch } from 'refresh-fetch';
import { retrieveToken, retrieveRefreshToken, saveTokens, clearTokens } from './tokenStorage';
import { safeParseJson, safeStringifyJson } from '../shared/json';
import { JWTokenDto } from './model';
import { saveAs } from 'file-saver';

import { HttpError } from 'react-admin';

const API_CONTEXT_PATH = '/api/';

const shouldRefreshToken = (error: HttpError) => error.status === 401;

const fetchApiWithToken = <T>(url: string, options: any = {}) => {
    return sgxFetchApi<T>(
        url,
        addAuth(options)
    );
};

const downloadApiWithToken = (url: string, filename: string) => {
    const options = addAuth();
    return fetch(API_CONTEXT_PATH + url, options)
        .then(ifErrorThrow(mapToApiHttpError()))
        .then((response) => {
            return response.blob();
        })
        .then(blob => saveAs(blob, filename));
};

const parseTextAsJson = <T>(response: Response): Promise<T> => {
    return response.text().then(safeParseJson);
};

const ifErrorThrow = (mapper: (body: any, response: Response) => HttpError) => (response: Response): Response | Promise<Response> => {
    if (response.status < 200 || response.status >= 300) {
        return parseTextAsJson<HttpError>(response).then((json: any) => {
            return Promise.reject(mapper(json, response));
        });
    }
    return response;
};

const mapToApiHttpError = (defaultValue = { code: 'error.generic.title' }) => (body: any = {}, response: Response): HttpError => {
    return new HttpError(
        body.text || body.message || response.statusText,
        response.status,
        {
            traceId: body.traceId,
            code: body.code || defaultValue.code,
            args: body.args,
        }
    );
};

const doRefreshToken = (): Promise<void> => {
    const refreshToken = retrieveRefreshToken();
    const options = jsonPayload('POST', { refreshToken });
    return sgxFetchApi<JWTokenDto>('auth/refresh', options)
        .then(({ token, refreshToken }) => {
            saveTokens(token, refreshToken)
        })
        .catch(error => {
            clearTokens()
            throw error
        })
}
const bodyIsFormData = (body: any) => (typeof body === 'object')

const sgxFetch = <T>(url: string, options: any = {}): Promise<T> => {
    const optionsWithAccept = withHeader(options, 'Accept', 'application/json');

    const optionsWithContentType = bodyIsFormData(options.body) ? optionsWithAccept : withHeader(optionsWithAccept, 'Content-Type', 'application/json');
    return fetch(url, optionsWithContentType)
        .then(ifErrorThrow(mapToApiHttpError()))
        .then(response => parseTextAsJson<T>(response));

};


const sgxFetchApi = <T>(url: string, options: any = {}): Promise<T> => {
    return sgxFetch<T>(API_CONTEXT_PATH + url, options);
};

const sgxFetchApiWithToken = configureRefreshFetch({
    fetch: fetchApiWithToken,
    shouldRefreshToken,
    refreshToken: doRefreshToken,
});

const sgxDownload = configureRefreshFetch({
    fetch: downloadApiWithToken,
    shouldRefreshToken,
    refreshToken: doRefreshToken,
});

const withHeader = ({ headers = new Headers(), ...rest }, name: string, value: string) => {
    headers.set(name, value);
    return { headers, ...rest };
};

const addAuth = (options: any = {}) => {
    const token = retrieveToken();
    return (!token) ? options : withHeader(options, 'Authorization', token);
};

const jsonPayload = (method: string, body: any) => ({ method, body: safeStringifyJson(body) });

export { 
    sgxFetch, 
    sgxFetchApi, 
    jsonPayload, 
    withHeader, 
    sgxFetchApiWithToken, 
    sgxDownload, 
    safeParseJson,
};
