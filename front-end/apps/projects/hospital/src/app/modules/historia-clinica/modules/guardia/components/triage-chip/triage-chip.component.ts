import { Component, Input} from '@angular/core';
import { Triages } from '../../constants/masterdata';

@Component({
	selector: 'app-triage-chip',
	templateUrl: './triage-chip.component.html',
	styleUrls: ['./triage-chip.component.scss']
})
export class TriageChipComponent {

	@Input() category: TriageCategory;
	@Input() isFilled = false;

	constructor() {	}

}

export interface TriageCategory {
	id: Triages;
	name: string;
}
