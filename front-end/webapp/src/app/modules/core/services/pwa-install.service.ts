import { Injectable, APP_INITIALIZER } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { filter } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class PwaInstallService {
	install$: Observable<InstallEvent>;
	private installSubject: ReplaySubject<InstallEvent>;

	constructor() {
		this.installSubject = new ReplaySubject<InstallEvent>(1);
		this.install$ = this.installSubject.asObservable().pipe(
			filter(installEvent => installEvent.event?.prompt),
		);
	}

	public initPwaPrompt() {
		window.addEventListener('beforeinstallprompt', (event: any) => {
			event.preventDefault();
			this.installSubject.next({ event })
		});
	}
}

export interface InstallEvent {
	event: any;
}

const initializer = (pwaInstallService: PwaInstallService) => () => pwaInstallService.initPwaPrompt();

export const pwaInstallProviders = { provide: APP_INITIALIZER, useFactory: initializer, deps: [PwaInstallService], multi: true };
