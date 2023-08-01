import { Injectable } from '@angular/core';
import { StompService } from '../../stomp.service';
import { Observable, map } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EntryCallStompService {

	callId$: Observable<string> = this.stompService.watch('/USER/QUEUE/VIRTUAL-CONSULTATION').pipe(map(m => m ? JSON.parse(m.body).callId : null ))

	constructor(
		private stompService: StompService,
	) { }

}
