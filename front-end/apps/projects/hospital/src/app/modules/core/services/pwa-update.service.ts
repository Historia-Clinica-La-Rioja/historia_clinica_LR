import { ApplicationRef, Injectable } from '@angular/core';
import { SwUpdate, VersionReadyEvent } from '@angular/service-worker';
import { concat, interval, Observable, ReplaySubject } from 'rxjs';
import { filter, first, map, tap } from 'rxjs/operators';
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
		const everyHourOnceAppIsStable$ = concat(appIsStable$, everyHour$);

		everyHourOnceAppIsStable$.subscribe(() => {
			this.checkForUpdate();
		});

		updates.versionUpdates.pipe(
			tap(evt => console.log('event update app', evt)),
			filter((evt): evt is VersionReadyEvent => evt.type === 'VERSION_READY'),
			map(evt => ({
				type: 'UPDATE_AVAILABLE',
				current: evt.currentVersion,
				available: evt.latestVersion,
			}))
		).subscribe(event => {
			console.log('New App version available', event);
			this.updateSubject.next({
				run: () => {
					updates.activateUpdate().then(() => document.location.reload());
				}
			});
		});
	}

	checkForUpdate() {
		if (!this.updates.isEnabled) {
			console.warn('checkForUpdate: Service Worker not enabled');
			return;
		}
		console.debug('Checking App version');
		this.updates.checkForUpdate();
	}

}

