import { Injectable, APP_INITIALIZER } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { PWAAction } from '@core/core-model';

const promptInstall = (event) => () => {
	event.prompt();
	// Wait for the user to respond to the prompt
	event.userChoice.then(
		(choiceResult) => console.log(`User ${choiceResult?.outcome}`)
	);
};

@Injectable({
	providedIn: 'root'
})
export class PwaInstallService {
	install$: Observable<PWAAction>;
	private installSubject: ReplaySubject<PWAAction>;

	constructor() {
		this.installSubject = new ReplaySubject<PWAAction>(1);
		this.install$ = this.installSubject.asObservable();
	}

	public initPwaPrompt() {
		window.addEventListener('beforeinstallprompt', (event: any) => {
			event.preventDefault();
			if (event?.prompt) {
				this.installSubject.next({ run: promptInstall(event)});
			}
		});
	}
}

const initializer = (pwaInstallService: PwaInstallService) => () => pwaInstallService.initPwaPrompt();

export const pwaInstallProviders = { provide: APP_INITIALIZER, useFactory: initializer, deps: [PwaInstallService], multi: true };
