import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-violence-situation-relevant-information-section',
	templateUrl: './violence-situation-relevant-information-section.component.html',
	styleUrls: ['./violence-situation-relevant-information-section.component.scss']
})
export class ViolenceSituationRelevantInformationSectionComponent implements OnInit {

	form: FormGroup<{
		observations: FormControl<string>,
	}>;

	constructor() { }

	ngOnInit(): void {
		this.form = new FormGroup({
			observations: new FormControl(null),
		});
	}

}
