import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
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

	consultorios$: Observable<DoctorsOfficeDto[]>;
	form: FormGroup;

	constructor(
		private readonly dialogRef: MatDialogRef<SelectConsultorioComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly doctorsOfficeService: DoctorsOfficeService,
		@Inject(MAT_DIALOG_DATA) public data: { title: string},
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			consultorio: [null, Validators.required]
		});

		this.consultorios$ = this.doctorsOfficeService.getBySectorType(SECTOR_AMBULATORIO);
	}

	submit(): void {
		if (this.form.valid) {
			this.dialogRef.close(this.form.value.consultorio);
		}
	}

}
