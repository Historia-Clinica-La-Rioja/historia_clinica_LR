import { Component } from '@angular/core';
import { PendingRequestsService } from '../pending-requests.service';
import { map } from 'rxjs';

@Component({
	selector: 'app-api-overlay-delay',
	templateUrl: './api-overlay-delay.component.html',
	styleUrls: ['./api-overlay-delay.component.scss']
})
export class ApiOverlayDelayComponent {

	class$ = this.pendingRequests.arePendingRequests$.pipe(map(requestPending => requestPending ? 'overlay' : null))

	constructor(
		private readonly pendingRequests: PendingRequestsService
	) { }

}
