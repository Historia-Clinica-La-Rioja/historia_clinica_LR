import { Injectable } from '@angular/core';
import { AccountService } from '@api-rest/services/account.service';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class JitsiCallService {


	private readonly emitter = new Subject<string>();
	readonly $meetingLink = this.emitter.asObservable();
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

	open(meetingLink: string) {
/* 		const url = `https://meet.jit.si/${meetingLink}#config.prejoinConfig.enabled=false&userInfo.displayName="${this.userName}"`
		window.open(url); */
		this.emitter.next(meetingLink);
	}

	close() {
		this.emitter.next(null)
	}
}
