import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { DoctorsOfficeDto, ERole } from '@api-rest/api-model';
import { TriageCategoryDto, TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { MatOptionSelectionChange } from '@angular/material/core';
import { Observable, combineLatest } from 'rxjs';
import { SECTOR_AMBULATORIO, TRIAGE_LEVEL_V_ID } from '../../constants/masterdata';
import { PermissionsService } from '@core/services/permissions.service';

const WITHOUT_TRIAGE_LEVEL_NOT_VALID_ROLES: ERole[] = [ERole.ENFERMERO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD];
const WITHOUT_TRIAGE_CATEGORY_ID = 6;

@Component({
	selector: 'app-triage',
	templateUrl: './triage.component.html',
	styleUrls: ['./triage.component.scss']
})
export class TriageComponent implements OnInit {

	@Input() cancelFunction: () => void;
	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Output() triageCategoryIdChange = new EventEmitter();
	@Output() doctorsOfficeIdChange = new EventEmitter();
	triageForm: UntypedFormGroup;
	doctorsOffices$: Observable<DoctorsOfficeDto[]>;
	triageCategories: TriageCategoryDto[];

	constructor(
		private formBuilder: UntypedFormBuilder,
		private doctorsOfficeService: DoctorsOfficeService,
		private triageMasterDataService: TriageMasterDataService,
		private permissionsService: PermissionsService,
	) {
	}

	ngOnInit(): void {
		this.triageForm = this.formBuilder.group({
			triageCategoryId: [null, Validators.required],
			doctorsOfficeId: [null]
		});

		let role$ = this.permissionsService.hasContextAssignments$(WITHOUT_TRIAGE_LEVEL_NOT_VALID_ROLES);
		let triages$ = this.triageMasterDataService.getCategories();

		combineLatest([role$, triages$]).subscribe(([hasntTriageNotDefinedRole ,triageCategories]) => {
			this.triageCategories = triageCategories;
			if (!hasntTriageNotDefinedRole && this.canAssignNotDefinedTriageLevel)
				this.triageForm.controls.triageCategoryId.setValue(WITHOUT_TRIAGE_CATEGORY_ID);
			else {
				this.triageCategories = this.triageCategories.filter(category => category.id != WITHOUT_TRIAGE_CATEGORY_ID);
				this.triageForm.controls.triageCategoryId.setValue(this.triageCategories.find(category => category.id === TRIAGE_LEVEL_V_ID).id);
			}
			this.triageCategoryIdChange.emit(this.triageForm.controls.triageCategoryId.value);
		});

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
