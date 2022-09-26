/**
 * HTTP clients
 * 
 * It handles JSON decoding. HTTP error will be translated to ApiHttpError.
 */
import { configureRefreshFetch } from 'refresh-fetch';
import { saveAs } from 'file-saver';
import { HttpError } from 'react-admin';

import {
    safeParseJson,
    safeStringifyJson,
} from '../shared/json';

import {
    FileInputData,
} from './model';

const API_CONTEXT_PATH = '/api/';

const shouldRefreshToken = (error: HttpError) => error.status === 401;

const downloadApiWithToken = (url: string, filename: string) => {
    return fetch(API_CONTEXT_PATH + url)
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
            text: body.text || body.message || response.statusText,
            args: body.args,
        }
    );
};

const doRefreshToken = (): Promise<void> => {
    const options = jsonPayload('POST', undefined);
    return sgxFetchApi<void>('auth/refresh', options)
        .catch(error => {
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
    fetch: sgxFetchApi,
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

const jsonPayload = (method: string, body: any) => ({ method, body: safeStringifyJson(body) });

const formDataPayload = (method: string, body: any) => ({ method, body: formData(body) });

const formData = ({file, ...data}: FileInputData) => {
    let formData = new FormData();
    formData.append("data", new Blob([JSON.stringify(data)], {type: "application/json"}));
    formData.append("file", file.rawFile, file.title);
    return formData;
};

export { 
    formDataPayload,
    jsonPayload,
    sgxDownload,
    sgxFetch,
    sgxFetchApi,
    sgxFetchApiWithToken,
    withHeader,
};
