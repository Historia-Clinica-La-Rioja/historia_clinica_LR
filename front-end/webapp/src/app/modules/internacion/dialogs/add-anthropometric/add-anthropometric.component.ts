import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MasterDataInterface } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-add-anthropometric',
	templateUrl: './add-anthropometric.component.html',
	styleUrls: ['./add-anthropometric.component.scss']
})
export class AddAnthropometricComponent implements OnInit {

	form: FormGroup;
	bloodTypes: MasterDataInterface<string>[];

	constructor(
		public dialogRef: MatDialogRef<AddAnthropometricComponent>,
		private formBuilder: FormBuilder,
		private internacionMasterDataService: InternacionMasterDataService,
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			bloodType: [null],
			height: [null],
			weight: [null],
		});

		const bloodTypes$ = this.internacionMasterDataService.getBloodTypes();
		bloodTypes$.subscribe(bloodTypes => this.bloodTypes = bloodTypes);
	}

	submit(): void {
		this.dialogRef.close(this.form.value);
	}

}
