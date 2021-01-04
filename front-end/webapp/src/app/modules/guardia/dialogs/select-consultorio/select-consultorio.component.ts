import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DoctorsOfficeDto } from '@api-rest/api-model';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { SECTOR_AMBULATORIO } from '../../constants/masterdata';

@Component({
	selector: 'app-select-consultorio',
	templateUrl: './select-consultorio.component.html',
	styleUrls: ['./select-consultorio.component.scss']
})
export class SelectConsultorioComponent implements OnInit {

	doctorsOffices$: Observable<DoctorsOfficeDto[]>;
	form: FormGroup;

	constructor(
		private readonly dialogRef: MatDialogRef<SelectConsultorioComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly doctorsOfficeService: DoctorsOfficeService
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			consultorio: [null, Validators.required]
		});

		this.doctorsOffices$ = this.doctorsOfficeService.getBySectorType(SECTOR_AMBULATORIO);
	}

	submit(): void {
		if (this.form.valid) {
			this.dialogRef.close(this.form.value);
		}
	}

}
