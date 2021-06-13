import { Injectable } from '@angular/core';
import { Observable, of, EMPTY } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class LocalStorageService {

	constructor() { }

	public updateItem<T>(key: string, value: T): Observable<T> {
		const valueToSave = JSON.stringify(value);
		const valueInCache = localStorage.getItem(key);
		if (valueToSave !== valueInCache) {
			localStorage.setItem(key, valueToSave);
			return of(value);
		}
		return EMPTY;
	}

	public getItem<T>(key: string): Observable<T> {
		const value = localStorage.getItem(key) || '';
		if (value !== '') {
			return of(JSON.parse(value));
		}
		return of(undefined);
	}

	public removeItem(key: string): Observable<void> {
		localStorage.removeItem(key);
		return of();
	}
}
