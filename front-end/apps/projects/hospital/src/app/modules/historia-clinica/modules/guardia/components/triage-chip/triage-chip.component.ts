import { Component, Input, OnInit} from '@angular/core';
import { Triages } from '../../constants/masterdata';

@Component({
	selector: 'app-triage-chip',
	templateUrl: './triage-chip.component.html',
	styleUrls: ['./triage-chip.component.scss']
})
export class TriageChipComponent implements OnInit {

	readonly triages = Triages;

	@Input() category: TriageCategory;
	@Input() isFilled: boolean;

	constructor() {	}

	ngOnInit() {
		if (!this.isFilled) this.isFilled = false;
	}

}

export interface TriageCategory {
	id: number;
	name: string;
	colorHex: string;
}
