import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MasterDataDto, NewEffectiveClinicalObservationDto, TriagePediatricDto } from '@api-rest/api-model';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { VITAL_SIGNS } from '@core/constants/validation-constants';
import { getError, hasError } from '@core/utils/form.utils';
import { Observable } from 'rxjs';
import { EffectiveObservation, VitalSignsFormService } from '../../../../services/vital-signs-form.service';
import { Moment } from 'moment';
import { newMoment } from '@core/utils/moment.utils';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';

@Component({
	selector: 'app-pediatric-triage',
	templateUrl: './pediatric-triage.component.html',
	styleUrls: ['./pediatric-triage.component.scss']
})
export class PediatricTriageComponent implements OnInit {

	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Input() disableConfirmButton: boolean;
	@Output() confirm = new EventEmitter();
	@Output() cancel = new EventEmitter();
	private triageCategoryId: number;
	private doctorsOfficeId: number;
	pediatricForm: FormGroup;
	bodyTemperatures$: Observable<MasterDataDto[]>;
	muscleHypertonyaOptions$: Observable<MasterDataDto[]>;
	perfusionOptions$: Observable<MasterDataDto[]>;
	respiratoryRetractionOptions$: Observable<MasterDataDto[]>;

	hasError = hasError;
	getError = getError;

	private CLEAR_CASES = {
		bodyTemperatureId: (control) => control.controls.bodyTemperatureId.reset(),
		muscleHypertoniaId: (control) => control.controls.muscleHypertoniaId.reset(),
		respiratoryRetractionId: (control) => control.controls.respiratoryRetractionId.reset(),
		perfusionId: (control) => control.controls.perfusionId.reset(),
	}

	constructor(
		private formBuilder: FormBuilder,
		private readonly triageMasterDataService: TriageMasterDataService,
		private readonly vitalSignsFormService: VitalSignsFormService
	) {
	}

	ngOnInit(): void {
		this.pediatricForm = this.formBuilder.group({
			notes: [null],
			appearance: this.formBuilder.group({
				bodyTemperatureId: [null],
				cryingExcessive: [null],
				muscleHypertoniaId: [null]

			}),
			breathing: this.formBuilder.group({
				bloodOxygenSaturation: this.formBuilder.group({
					value: [null, Validators.min(VITAL_SIGNS.min_value)],
					effectiveTime: [newMoment()]
				}),
				respiratoryRate: this.formBuilder.group({
					value: [null, Validators.min(VITAL_SIGNS.min_value)],
					effectiveTime: [newMoment()]
				}),
				respiratoryRetractionId: [null],
				stridor: [null]
			}),
			circulation: this.formBuilder.group({
				heartRate: this.formBuilder.group({
					value: [null, Validators.min(VITAL_SIGNS.min_value)],
					effectiveTime: [newMoment()]
				}),
				perfusionId: [null]
			}),
		});
		this.bodyTemperatures$ = this.triageMasterDataService.getBodyTemperature();
		this.muscleHypertonyaOptions$ = this.triageMasterDataService.getMuscleHypertonia();
		this.perfusionOptions$ = this.triageMasterDataService.getPerfusion();
		this.respiratoryRetractionOptions$ = this.triageMasterDataService.getRespiratoryRetraction();
	}

	setTriageCategoryId(triageCategoryId: number): void {
		this.triageCategoryId = triageCategoryId;
	}

	setDoctorsOfficeId(doctorsOfficeId: number): void {
		this.doctorsOfficeId = doctorsOfficeId;
	}

	confirmPediatricTriage(): void {
		if (this.pediatricForm.valid) {
			const triage: TriagePediatricDto = this.buildTriagePediatricDto();
			this.confirm.emit(triage);
		}
	}

	setVitalSignEffectiveTime(newEffectiveTime: Moment, form, field: string): void {
		this.vitalSignsFormService.setVitalSignEffectiveTime(newEffectiveTime, form as FormGroup, field);
	}

	back(): void {
		this.cancel.emit();
	}

	private mapVitalSignToDto(vitalSignValue): NewEffectiveClinicalObservationDto {
		const effectiveObservation: EffectiveObservation = this.vitalSignsFormService.getEffectiveObservation(vitalSignValue);
		return GuardiaMapperService._mapEffectiveObservationToNewEffectiveClinicalObservationDto(effectiveObservation);
	}


	private buildTriagePediatricDto(): TriagePediatricDto {
		const formValue = this.pediatricForm.value;
		return {
			categoryId: this.triageCategoryId,
			doctorsOfficeId: this.doctorsOfficeId,
			appearance: {
				...formValue.appearance,
			},
			breathing: {
				...formValue.breathing,
				bloodOxygenSaturation: this.mapVitalSignToDto(formValue.breathing.bloodOxygenSaturation),
				respiratoryRate: this.mapVitalSignToDto(formValue.breathing.respiratoryRate)
			},
			circulation: {
				...formValue.circulation,
				heartRate: this.mapVitalSignToDto(formValue.circulation.heartRate)
			},
			notes: formValue.notes
		};
	}

	clear(control: any, value: string): void {
		this.CLEAR_CASES[value](control);
	}

}
