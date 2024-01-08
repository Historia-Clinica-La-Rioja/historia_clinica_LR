import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-violence-situation-relevant-information-section',
	templateUrl: './violence-situation-relevant-information-section.component.html',
	styleUrls: ['./violence-situation-relevant-information-section.component.scss']
})
export class ViolenceSituationRelevantInformationSectionComponent implements OnInit {
	@Input() confirmForm: Observable<boolean>;
	@Output() relevantInformation = new EventEmitter<any>();
	
	form: FormGroup<{
		observations: FormControl<string>,
	}>;

	constructor() { }

	ngOnInit(): void {
		this.form = new FormGroup({
			observations: new FormControl(null),
		});
	}

	ngOnChanges(changes: SimpleChanges) {
		if(!changes.confirmForm.isFirstChange()){
			this.relevantInformation.emit(this.form.value);
		}
	}
}
