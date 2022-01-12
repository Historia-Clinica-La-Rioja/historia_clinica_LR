import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { DoctorsOfficeDto } from '@api-rest/api-model';
import { TriageCategoryDto, TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { MatOptionSelectionChange } from '@angular/material/core';
import { Observable } from 'rxjs';
import { SECTOR_AMBULATORIO, TRIAGE_LEVEL_V_ID } from '../../constants/masterdata';

@Component({
	selector: 'app-triage',
	templateUrl: './triage.component.html',
	styleUrls: ['./triage.component.scss']
})
export class TriageComponent implements OnInit {

	@Input() cancelFunction: () => void;
	@Output() triageCategoryIdChange = new EventEmitter();
	@Output() doctorsOfficeIdChange = new EventEmitter();
	triageForm: FormGroup;
	doctorsOffices$: Observable<DoctorsOfficeDto[]>;
	triageCategories: TriageCategoryDto[];

	constructor(
		private formBuilder: FormBuilder,
		private doctorsOfficeService: DoctorsOfficeService,
		private triageMasterDataService: TriageMasterDataService,
	) {
	}

	ngOnInit(): void {
		this.triageForm = this.formBuilder.group({
			triageCategoryId: [null, Validators.required],
			doctorsOfficeId: [null]
		});

		this.triageMasterDataService.getCategories()
			.subscribe(
				triageCategories => {
					this.triageCategories = triageCategories;
					this.triageForm.controls.triageCategoryId.setValue(
						this.triageCategories.find(category => category.id === TRIAGE_LEVEL_V_ID).id
					);
					this.triageCategoryIdChange.emit(this.triageForm.controls.triageCategoryId.value);
				}
			);

		this.doctorsOffices$ = this.doctorsOfficeService.getBySectorType(SECTOR_AMBULATORIO);
	}

	selectTriageCategoryId(event: MatOptionSelectionChange, choosed: number): void {
		if (event.isUserInput) {
			this.triageCategoryIdChange.emit(choosed);
		}
	}

	selectDoctorsOfficeId(event: MatOptionSelectionChange, choosed: number): void {
		if (event.isUserInput) {
			this.doctorsOfficeIdChange.emit(choosed);
		}
	}

	clear(control: AbstractControl): void {
		control.reset();
	}
}
