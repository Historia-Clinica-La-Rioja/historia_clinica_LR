import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import {
	DoctorsOfficeDto,
	MasterDataInterface,
} from '@api-rest/api-model';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { hasError, NON_WHITESPACE_REGEX, TIME_PATTERN } from '@core/utils/form.utils';
import { Observable, Subscription } from 'rxjs';
import { AdministrativeAdmission } from '../../services/new-episode.service';
import { AMBULANCE, PERSON, POLICE_OFFICER } from '@core/constants/validation-constants';
import { EmergencyCareEntranceType } from '@api-rest/masterdata';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { SECTOR_AMBULATORIO } from '../../constants/masterdata';
import { MIN_DATE } from "@core/utils/date.utils";
import { ButtonType } from '@presentation/components/button/button.component';
import { EmergencyCarePatient } from '../../components/emergency-care-patient/emergency-care-patient.component';
import { EmergencyCareTemporaryPatientService } from '../../services/emergency-care-temporary-patient.service';
//import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'app-admision-administrativa',
	templateUrl: './admision-administrativa.component.html',
	styleUrls: ['./admision-administrativa.component.scss']
})
export class AdmisionAdministrativaComponent implements OnInit, OnDestroy {


	private patientDescriptionSubscription: Subscription;
	readonly POLICE_OFFICER = POLICE_OFFICER;
	readonly PERSON = PERSON;
	readonly AMBULANCE = AMBULANCE;
	readonly EMERGENCY_CARE_ENTRANCE_TYPE = EmergencyCareEntranceType;
	readonly minDate = MIN_DATE;
	readonly buttonType = ButtonType;
	readonly buttonThemeWarn = 'warn';
	readonly hasError = hasError;
	readonly TIME_PATTERN = TIME_PATTERN;
	readonly today: Date = new Date();

	emergencyCareEntranceType$: Observable<MasterDataInterface<number>[]>;
	emergencyCareType$: Observable<MasterDataInterface<number>[]>;
	form: UntypedFormGroup;

	doctorsOffices$: Observable<DoctorsOfficeDto[]>;
	emergencyCarePatientData: EmergencyCarePatient;

	@Input() initData: AdministrativeAdmission;
	@Input() isDoctorOfficeEditable = true;
	@Input() isEmergencyCareTypeEditable = true;
	@Output() confirm = new EventEmitter<AdministrativeAdmission>();
	@Output() cancel = new EventEmitter<void>();
	@Input() submitLabel = 'buttons.CONTINUE';

	constructor(
		private readonly emergencyCareMasterData: EmergencyCareMasterDataService,
		private formBuilder: UntypedFormBuilder,
		private readonly doctorsOfficeService: DoctorsOfficeService,
	//	private route: ActivatedRoute,,
		private readonly emergencyCareTemporaryPatientService: EmergencyCareTemporaryPatientService,
	) { }

	ngOnDestroy(): void {
		this.patientDescriptionSubscription?.unsubscribe();
	}

	ngOnInit(): void {
		// this.route.queryParams
		// 	.subscribe(params => {
		// 		if(params.patientId){
		// 			this.loadPatient(params.patientId);
		// 		}
		// });
		this.emergencyCareType$ = this.emergencyCareMasterData.getType();
		this.emergencyCareEntranceType$ = this.emergencyCareMasterData.getEntranceType();
		this.doctorsOffices$ = this.doctorsOfficeService.getBySectorType(SECTOR_AMBULATORIO);

		this.form = this.formBuilder.group({
			patientMedicalCoverageId: [null],
			emergencyCareTypeId: [{ value: null, disabled: !this.isEmergencyCareTypeEditable }],
			emergencyCareEntranceTypeId: [null],
			doctorsOfficeId: [{ value: null, disabled: !this.isDoctorOfficeEditable }],
			ambulanceCompanyId: [null, Validators.maxLength(AMBULANCE.COMPANY_ID.max_length)],
			hasPoliceIntervention: [null],
			callDate: [null],
			callTime: [null],
			plateNumber: [null, Validators.maxLength(POLICE_OFFICER.PLATE_NUMBER.max_length)],
			firstName: [null, Validators.maxLength(PERSON.MAX_LENGTH.firstName)],
			lastName: [null, Validators.maxLength(PERSON.MAX_LENGTH.lastName)],
			reason: [null, [Validators.required, Validators.pattern(NON_WHITESPACE_REGEX)]],
			patientId: [null],
			patientDescription: [null]
		});

		this.setExistingInfo();
	}

	dateChanged(date: Date) {
		this.form.controls.callDate.setValue(date);
	}

	continue(): void {
		const formValue: AdministrativeAdmission = this.form.getRawValue();

		if (!formValue.patientId && !formValue.patientDescription) {
			this.openTemporaryPatientDialog();
			return;
		}

		if (this.form.valid) {
			this.confirm.emit(formValue);
		}
		else {
			this.form.markAllAsTouched();
			this.form.updateValueAndValidity();
		}
	}

	onChange(): void {
		if (!this.form.controls.hasPoliceIntervention.value) {
			this.form.controls.callDate.setValue(null);
			this.form.controls.callTime.setValue(null);
			this.form.controls.plateNumber.setValue(null);
			this.form.controls.firstName.setValue(null);
			this.form.controls.lastName.setValue(null);
		}
	}

	goBack(): void {
		this.cancel.emit();
	}

	setAmbulanceCompanyIdStatus(): void {
		if (this.form.value.emergencyCareEntranceTypeId !== EmergencyCareEntranceType.AMBULANCIA_CON_MEDICO
			|| this.form.value.emergencyCareEntranceTypeId !== EmergencyCareEntranceType.AMBULANCIA_SIN_MEDICO) {
			this.form.controls.ambulanceCompanyId.setValue(null);
		}
	}

	private setExistingInfo(): void {
		if (this.initData) {
			this.setInitDataInForm();
			const { patientId, patientMedicalCoverageId, patientDescription, patientTypeId } = this.initData;
			this.emergencyCarePatientData = { patientId, patientMedicalCoverageId, patientDescription, patientTypeId };
		}
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

	private setInitDataInForm() {
		const { patientTypeId, ...formData } = this.initData
		this.form.setValue(formData);
		this.form.markAllAsTouched();
		this.form.updateValueAndValidity();
	}

	setPatientData(emergencyCarePatient: EmergencyCarePatient) {
		const { patientId, patientMedicalCoverageId, patientDescription } = emergencyCarePatient;
		this.form.controls.patientId.setValue(patientId);
		this.form.controls.patientMedicalCoverageId.setValue(patientMedicalCoverageId);
		this.form.controls.patientDescription.setValue(patientDescription);
	}

	private openTemporaryPatientDialog() {
		this.patientDescriptionSubscription = this.emergencyCareTemporaryPatientService.patientDescription$.subscribe(patientDescription => {
			if (patientDescription) {
				this.updatePatientDescription(patientDescription);
			}
		});

		this.emergencyCareTemporaryPatientService.openTemporaryPatient();
	}

	private updatePatientDescription(patientDescription: string) {
		this.form.controls.patientDescription.setValue(patientDescription);
		this.continue();
	}
}
