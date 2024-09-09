import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class EmergencyCareAttentionPlaceAvailabilityButtonSelectionService {

	private selectedButton = new BehaviorSubject<number | null>(null);
	selectedButtonId$ = this.selectedButton.asObservable();

	selectButton(buttonId: number) {
		this.selectedButton.next(buttonId);
	}

	clearSelection() {
		this.selectedButton.next(null);
	}
}
