import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpEventType, } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, finalize,tap } from 'rxjs/operators';
import { PendingRequestsService } from '../pending-requests.service';

@Injectable()
export class PendingRequestsInterceptor implements HttpInterceptor {

	constructor(
		private readonly pendingRequestService: PendingRequestsService
	) { }

	intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		return request.method !== 'GET' ? this.handleRequest(request, next) : next.handle(request);
	}

	private handleRequest(request: HttpRequest<any>, next: HttpHandler) {
		return next.handle(request).pipe(
			filter(r => r.type !== HttpEventType.Sent),
			tap(() => this.pendingRequestService.addPendingRequest()),
			finalize(() => {
				this.pendingRequestService.removePendingRequest();
			})
		)
	}
}

