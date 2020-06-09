import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { SnomedDto } from '@api-rest/api-model';
import { FormGroup } from '@angular/forms';
import { SEMANTICS_CONFIG } from '../../constants/snomed-semantics';

@Component({
	selector: 'app-add-allergy',
	templateUrl: './add-allergy.component.html',
	styleUrls: ['./add-allergy.component.scss']
})
export class AddAllergyComponent implements OnInit {

	snomedConcept: SnomedDto;
	form: FormGroup;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	constructor(
		public dialogRef: MatDialogRef<AddAllergyComponent>,
	) {
	}

	ngOnInit(): void {
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	submit(): void {
		console.log('save allergy', this.form.value);
	}

}
