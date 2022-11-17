import { DataProvider } from 'react-admin';
import { stringify } from 'query-string';
import { sgxFetchApiWithToken, jsonPayload } from './fetch';
import { GetListResponse } from './model';

const httpClient = sgxFetchApiWithToken;

const BACKOFFICE_API = 'backoffice';

const dataProvider: DataProvider = {
    getList: (resource, params) => {
        const { page, perPage } = params.pagination;
        const { field, order } = params.sort;
        const filter = stringify(params.filter);
        return httpClient<GetListResponse>(
            `${BACKOFFICE_API}/${resource}?page=${page-1}&size=${perPage}&sort=${field},${order}&${filter}`
        ).then(({content, totalElements}) => ({
            data: content,
            total: totalElements,
        }));
    },

    getOne: (resource, params) =>
        httpClient<any>(
            `${BACKOFFICE_API}/${resource}/${params.id}`
        ).then( data => ({ data }) ),

    getMany: (resource, params) => 
        httpClient<any>(
            `${BACKOFFICE_API}/${resource}?ids=${params.ids.join(',')}`
        ).then( data => ({ data }) ),

    getManyReference: (resource, params) => {
        const { page, perPage } = params.pagination;
        const { field, order } = params.sort;
        const filter = stringify({
            ...params.filter,
            [params.target]: params.id,
        });
        const url = `${BACKOFFICE_API}/${resource}?page=${page-1}&size=${perPage}&sort=${field},${order}&${filter}`;
        return httpClient<GetListResponse>(url).then(({content, totalElements}) => ({
            data: content,
            total: totalElements,
        }));
    },

    update: (resource, params) =>
        httpClient<any>(
            `${BACKOFFICE_API}/${resource}/${params.id}`, 
            jsonPayload('PUT', params.data),
        ).then( data => ({ data }) ),

    updateMany: (resource, params) => {
        const query = {
            filter: JSON.stringify({ id: params.ids}),
        };
        return httpClient<any>(
            `${BACKOFFICE_API}/${resource}?${stringify(query)}`, 
            jsonPayload('PUT', params.data),
        ).then( data => ({ data }) );
    },

    create: (resource, params) =>
        httpClient<any>(
            `${BACKOFFICE_API}/${resource}`, 
            jsonPayload('POST', params.data),
        ).then( data => ({ data }) ),

    delete: (resource, params) =>
        httpClient<any>(
            `${BACKOFFICE_API}/${resource}/${params.id}`, 
            { method: 'DELETE' }
        ).then( data => ({ data }) ),

    deleteMany: (resource, params) => 
        httpClient<any>(
            `${BACKOFFICE_API}/${resource}?ids=${params.ids.join(',')}`, 
            { method: 'DELETE' }
        ).then( data => ({ data }) ),

};

export default dataProvider;
