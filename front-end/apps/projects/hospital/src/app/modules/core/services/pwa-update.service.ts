import { ApplicationRef, Injectable } from '@angular/core';
import { SwUpdate } from '@angular/service-worker';
import { concat, interval, Observable, ReplaySubject } from 'rxjs';
import { first } from 'rxjs/operators';
import { PWAAction } from '@core/core-model';

@Injectable({
	providedIn: 'root'
})
export class PwaUpdateService {

	update$: Observable<PWAAction>;
	private updateSubject: ReplaySubject<PWAAction>;

	constructor(
		appRef: ApplicationRef,
		private updates: SwUpdate,
	) {

		this.updateSubject = new ReplaySubject<PWAAction>(1);
		this.update$ = this.updateSubject.asObservable();

		// Allow the app to stabilize first, before starting polling for updates with `interval()`.
		const appIsStable$ = appRef.isStable.pipe(first(isStable => isStable === true));
		// const everySixHours$ = interval(6 * 60 * 60 * 1000);
		const everyHour$ = interval(60 * 60 * 1000);
		const everySixHoursOnceAppIsStable$ = concat(appIsStable$, everyHour$);

		everySixHoursOnceAppIsStable$.subscribe(() => {
			this.checkForUpdate();
		});

		updates.available.subscribe(_ => {
			// console.log('SW updates.available', event);
			updates.activateUpdate().then(() => {
				// console.log('SW activateUpdate');
				this.updateSubject.next({ run: () => document.location.reload() });
			});
		});
	}

	checkForUpdate() {
		if (this.updates.isEnabled) {
			this.updates.checkForUpdate();
		} else {
			console.warn('Service Worker not enabled');
		}
	}

}

