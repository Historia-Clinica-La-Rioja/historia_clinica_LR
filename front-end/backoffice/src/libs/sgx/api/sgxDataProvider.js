import { stringify } from "query-string";
import {
  GET_LIST,
  GET_ONE,
  GET_MANY,
  GET_MANY_REFERENCE,
  CREATE,
  UPDATE,
  DELETE,
  DELETE_MANY,
  HttpError,
} from "react-admin";

import { sgxFetchApiWithToken } from './fetch';

/**
 * Maps react-admin queries to a REST API implemented using Java Spring Boot and Swagger
 *
 * @example
 * GET_LIST     => GET http://my.api.url/posts?page=0&size=10
 * GET_ONE      => GET http://my.api.url/posts/123
 * GET_MANY     => GET http://my.api.url/posts?id=1234&id=5678
 * UPDATE       => PUT http://my.api.url/posts/123
 * CREATE       => POST http://my.api.url/posts
 * DELETE       => DELETE http://my.api.url/posts/123
 */
const sgxDataProvider = (apiUrl, mappers) => {

  const mapResponse = (resource, list) => {
    const mapper = mappers[resource];
    if (!mapper) {
        return list;
    }
    return list.map(mapper);
  };

  /**
     * @param {String} type One of the constants appearing at the top if this file, e.g. 'UPDATE'
     * @param {String} resource Name of the resource to fetch, e.g. 'posts'
     * @param {Object} params The data request params, depending on the type
     * @returns {Object} { url, options } The HTTP request parameters
     */
  const convertDataRequestToHTTP = (type, resource, params) => {
    let url = "";
    const options = {};
    switch (type) {

      case GET_LIST: {
        const { page, perPage } = params.pagination;
        const { field, order } = params.sort;
        const filter = stringify(params.filter);
        url = `${apiUrl}/${resource}?page=${page-1}&size=${perPage}&sort=${field},${order}&${filter}`;
        break;
      }

      case GET_ONE:
        url = `${apiUrl}/${resource}/${params.id}`;
        break;
      case GET_MANY: {
        const queryString = `ids=${params.ids.join(',')}`;
        url = `${apiUrl}/${resource}?${queryString}`;
        break;
      }

      case GET_MANY_REFERENCE: {
        const { target, id } = params;
        const { field, order } = params.sort;
        const { page, perPage } = params.pagination;
        const filter = stringify(params.filter);

        url = `${apiUrl}/${resource}?page=${page-1}&size=${perPage}&sort=${field},${order}&${target}=${id}&${filter}`;
        break;
      }

      case UPDATE:
        url = `${apiUrl}/${resource}/${params.id}`;
        options.method = "PUT";
        options.body = JSON.stringify(params.data);
        break;

      case CREATE:
        url = `${apiUrl}/${resource}`;
        options.method = "POST";
        options.body = JSON.stringify(params.data);
        break;

      case DELETE:
        url = `${apiUrl}/${resource}/${params.id}`;
        options.method = "DELETE";
        break;

      case DELETE_MANY:
        const queryString = `ids=${params.ids.join(',')}`;
        url = `${apiUrl}/${resource}?${queryString}`;
        options.method = "DELETE";
        break;

      default:
        throw new Error(`Unsupported fetch action type ${type}`);
    }
    return { url, options };
  };

  /**
     * @param {Object} json JSON response from fetch()
     * @param {String} type One of the constants appearing at the top if this file, e.g. 'UPDATE'
     * @param {String} resource Name of the resource to fetch, e.g. 'posts'
     * @param {Object} params The data request params, depending on the type
     * @returns {Object} Data response
     */
  const convertHTTPResponse = (json, type, resource, params) => {
    switch (type) {
      case GET_LIST:
      case GET_MANY_REFERENCE:
        if (!json.hasOwnProperty("totalElements")) {
          throw new Error(
            "The numberOfElements property must be must be present in the Json response"
          );
        }
        return {
          data: mapResponse(resource, json.content),
          total: parseInt(json.totalElements, 10)
        };
      case CREATE:
        return { data: json };
      default:
        return { data: json };
    }
  };

  /**
     * @param {string} type Request type, e.g GET_LIST
     * @param {string} resource Resource name, e.g. "posts"
     * @param {Object} payload Request parameters. Depends on the request type
     * @returns {Promise} the Promise for a data response
     */
  return (type, resource, params) => {
    const { url, options } = convertDataRequestToHTTP(type, resource, params);
    return sgxFetchApiWithToken(url, options)
      .then(
        jsonResponse => convertHTTPResponse(jsonResponse, type, resource, params),
        ({ status, statusText, body }) => {
          return Promise.reject(new HttpError(
              (body && body.text && `${body.text}`) || (body && body.code && `error.${body.code}`) || statusText,
            status,
            body,
          ));
        }
    );
  };
};

export default sgxDataProvider;
