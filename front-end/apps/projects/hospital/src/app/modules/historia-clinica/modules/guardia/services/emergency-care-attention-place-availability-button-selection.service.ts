import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { SelectedSpace } from '../components/emergency-care-attention-place-space/emergency-care-attention-place-space.component';

@Injectable()
export class EmergencyCareAttentionPlaceAvailabilityButtonSelectionService {

	private selectedButton = new BehaviorSubject<SelectedSpace>(null);
	selectedButton$ = this.selectedButton.asObservable();

	selectButton(button: SelectedSpace) {
		this.selectedButton.next(button);
	}

	clearSelection() {
		this.selectedButton.next(null);
	}
}
