import { Component } from '@angular/core';
import { PendingRequestsService } from '../pending-requests.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-api-overlay-delay',
	templateUrl: './api-overlay-delay.component.html',
	styleUrls: ['./api-overlay-delay.component.scss']
})
export class ApiOverlayDelayComponent {

	requestPending$: Observable<boolean> = this.pendingRequests.arePendingRequests$;

	constructor(
		private readonly pendingRequests: PendingRequestsService
	) { }

}
