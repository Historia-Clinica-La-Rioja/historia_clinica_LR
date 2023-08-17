import { Injectable } from '@angular/core';
import { AccountService } from '@api-rest/services/account.service';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class JitsiCallService {


	private readonly emitter = new Subject<string>();
	readonly $roomId = this.emitter.asObservable();
	userName = '';

	constructor(
		private readonly accountService: AccountService
	) {
		this.accountService.getInfo().subscribe(
			info => {
				this.userName = `${info.personDto.lastName} ${info.personDto.firstName}`
			}
		)
	}

	open(roomId: string) {
		const url = `https://meet.jit.si/${roomId}#config.prejoinConfig.enabled=false&userInfo.displayName="${this.userName}"`
		window.open(url);
		this.emitter.next(roomId);
	}

	close() {
		this.emitter.next(null)
	}
}
