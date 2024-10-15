import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable()
export class AttentionPlaceUpdateService {
	private updateSubject = new Subject<void>();

	get update$(): Observable<void> {
	  return this.updateSubject.asObservable();
	}

	notifyUpdate() {
	  this.updateSubject.next();
	}
}
