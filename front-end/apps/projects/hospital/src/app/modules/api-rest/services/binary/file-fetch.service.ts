import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, scan } from 'rxjs/operators';

import {
	HttpClient,
	HttpEvent,
	HttpEventType,
	HttpHeaderResponse,
	HttpHeaders,
	HttpParams,
	HttpProgressEvent,
	HttpRequest,
	HttpResponse,
	HttpSentEvent,
} from '@angular/common/http';
import {
	FileDownloadProgress,
	FileDownloadStatus,
	FileResponse,
} from './file-download.model';

@Injectable({
	providedIn: 'root'
})
export class FileFetchService {

	constructor(
		private http: HttpClient,
	) {	}

	downloadRequestParams(url: string, params: Record<string, string | number | boolean> = {}): Observable<FileDownloadStatus> {
		const absoluteURL = completeURL(url);
		const httpParams = searchParamsString(params);
		const httpOptions = buildHttpOptions(httpParams);
		const req = new HttpRequest('GET', absoluteURL, null, httpOptions);
		return this.http.request(req)
			.pipe(
				catchError((e) => {
					const decoder = new TextDecoder('utf-8');
					const jsonString = decoder.decode(e);
					throw safeParseJson(jsonString);
				}),
				download(),
			);
	}
}

const extractFileName = (contentDisposition: string): string => {
	if (!contentDisposition) {
		return null;
	}
	const regex = /filename\*?=UTF-8''([^']+)|filename="([^"]+)"/;
	const match = contentDisposition.match(regex);
	return match ? decodeURIComponent(match[1]): null;
};

const fileResponseHeaders = (event: HttpHeaderResponse): FileResponse => {
	return {
		status: event.status,
		contentType: event.headers.get('content-type'),
		filename: extractFileName(event.headers.get('content-disposition')),
		content: undefined,
	};
}

const fileResponseContent = (previousResponse: FileResponse, body: Blob): FileResponse => {
	return {
		...previousResponse,
		content: new Blob([body], {type: previousResponse.contentType}),
	};
}

const buildProgress = (progress: FileDownloadProgress, currentBytes = 0, totalBytes = 0): FileDownloadProgress =>
	!totalBytes ? progress : {value: Math.round((100 * currentBytes) / totalBytes), currentBytes, totalBytes};

const statusPending = (): FileDownloadStatus =>
	({state: 'PENDING', progress: undefined, response: undefined});

const statusError = (response: FileResponse): FileDownloadStatus =>
	({state: 'ERROR', progress: undefined, response});

const statusInProgress = (progress: FileDownloadProgress, response: FileResponse): FileDownloadStatus =>
	({state: 'IN_PROGRESS', progress, response});

const statusDone = (progress: FileDownloadProgress, response: FileResponse): FileDownloadStatus =>
	({state: 'DONE', progress, response});

const completeURL = (url: string): string => new URL(url, window.location.href).toString();

const searchParamsString = (params: Record<string, string | number | boolean>): HttpParams => {
	let queryParams: HttpParams = new HttpParams();
	return queryParams.appendAll(params);
}

const buildHttpOptions = (httpParams: HttpParams) => {
	return {
		headers: new HttpHeaders({
			'Content-Type': 'application/json',
			// Agrega cualquier otra cabecera que necesites
		}),
		responseType  : 'arraybuffer' as 'json',
		reportProgress: true, // Habilita la información de progreso
		params: httpParams,
	};
}

function isRequestSent<T>(event: HttpEvent<unknown>): event is HttpSentEvent {
	return event.type === HttpEventType.Sent
}

function isHttpResponse<T>(event: HttpEvent<T>): event is HttpResponse<T> {
	return event.type === HttpEventType.Response
}

function isHttpProgressEvent(event: HttpEvent<unknown>): event is HttpProgressEvent {
return event.type === HttpEventType.DownloadProgress
	|| event.type === HttpEventType.UploadProgress
}

function isHttpHeaderResponse<T>(event: HttpEvent<T>): event is HttpHeaderResponse {
	return event.type === HttpEventType.ResponseHeader
}

const download = (): (source: Observable<HttpEvent<Blob>>) => Observable<FileDownloadStatus> => {
	return (source: Observable<HttpEvent<Blob>>) =>
		source.pipe(
			scan((previous: FileDownloadStatus, event: HttpEvent<Blob>): FileDownloadStatus => {
				if (isRequestSent(event)) {
					return statusInProgress(undefined, undefined);
				}

				if (isHttpHeaderResponse(event)) {
					const response = fileResponseHeaders(event);

					if (!event.ok) {
						return statusError(response);
					}

					return statusInProgress(previous.progress, response);
				}

				if (isHttpResponse(event)) {
					return statusDone(
						previous.progress,
						fileResponseContent(previous.response, event.body)
					);
				}

				if (previous.state === 'ERROR') {
					return previous;
				}

				if (isHttpProgressEvent(event)) {
					return statusInProgress(
						buildProgress(previous.progress, event.loaded, event.total),
						previous.response
					);
				}
				return previous;
			},
				statusPending()
			)
		)
}

const safeParseJson = (text: string): any => {
	try {
		return text ? JSON.parse(text) : undefined;
	} catch(e) {
		console.warn(`Please check JSON response ${text}`, e);
	}
	return undefined;
};
