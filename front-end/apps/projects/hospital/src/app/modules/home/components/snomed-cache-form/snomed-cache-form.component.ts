import { Component, EventEmitter, OnInit, Output } from '@angular/core';

import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { TerminologyCSVDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { delay, of } from 'rxjs';

@Component({
	selector: 'app-snomed-cache-form',
	templateUrl: './snomed-cache-form.component.html',
	styleUrls: ['./snomed-cache-form.component.scss']
})
export class SnomedCacheFormComponent implements OnInit {
	@Output() add = new EventEmitter<TerminologyCSVDto>();
	ECL_LIST = [
		SnomedECL.ALLERGY,
		SnomedECL.BLOOD_TYPE,
		SnomedECL.CONSULTATION_REASON,
		SnomedECL.DIAGNOSIS,
		SnomedECL.EVENT,
		SnomedECL.FAMILY_RECORD,
		SnomedECL.HOSPITAL_REASON,
		SnomedECL.MEDICINE,
		SnomedECL.PERSONAL_RECORD,
		SnomedECL.PROCEDURE,
		SnomedECL.VACCINE,
		SnomedECL.DIABETES,
		SnomedECL.HYPERTENSION
	];

	form: UntypedFormGroup;
	loading = false;

	constructor(
		private formBuilder: UntypedFormBuilder,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			ecl: [null, Validators.required],
			url: ['', Validators.required],
		});
	}

	submit() {
		if (!this.form.valid) {
			return;
		}
		const {url, ecl} = this.form.value;
		this.add.emit({ url, ecl })
		this.loading = true;
		of(false).pipe(delay(2000)).subscribe(v => this.loading = v);
	}

	hasError(type: string, control: string): boolean {
		return this.form.get(control).hasError(type);
	}

}
