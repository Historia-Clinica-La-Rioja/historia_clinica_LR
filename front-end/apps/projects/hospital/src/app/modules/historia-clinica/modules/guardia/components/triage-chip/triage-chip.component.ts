import { Component, Input} from '@angular/core';
import { Triages } from '../../constants/masterdata';

@Component({
	selector: 'app-triage-chip',
	templateUrl: './triage-chip.component.html',
	styleUrls: ['./triage-chip.component.scss']
})
export class TriageChipComponent {

	readonly triages = Triages;

	@Input() category: TriageCategory;

	constructor() {
	}

}

export interface TriageCategory {
	id: number;
	name: string;
	colorHex: string;
}
