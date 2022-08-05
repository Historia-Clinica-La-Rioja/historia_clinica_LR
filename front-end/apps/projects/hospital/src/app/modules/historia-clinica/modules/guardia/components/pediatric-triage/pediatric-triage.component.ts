import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MasterDataDto, NewEffectiveClinicalObservationDto, TriagePediatricDto } from '@api-rest/api-model';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { getError, hasError } from '@core/utils/form.utils';
import { Observable } from 'rxjs';
import { Moment } from 'moment';
import { newMoment } from '@core/utils/moment.utils';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { EffectiveObservation, FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { FACTORES_DE_RIESGO } from '@historia-clinica/constants/validation-constants';
import { TranslateService } from '@ngx-translate/core';

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
	
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;

	constructor(
		private formBuilder: FormBuilder,
		private readonly triageMasterDataService: TriageMasterDataService,
		private readonly translateService: TranslateService,
	) {
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(formBuilder, translateService);
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
					value: [null, Validators.min(FACTORES_DE_RIESGO.MIN.bloodOxygenSaturation)],
					effectiveTime: [newMoment()]
				}),
				respiratoryRate: this.formBuilder.group({
					value: [null, Validators.min(FACTORES_DE_RIESGO.MIN.respiratoryRate)],
					effectiveTime: [newMoment()]
				}),
				respiratoryRetractionId: [null],
				stridor: [null]
			}),
			circulation: this.formBuilder.group({
				heartRate: this.formBuilder.group({
					value: [null, Validators.min(FACTORES_DE_RIESGO.MIN.heartRate)],
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

	setRiskFactorEffectiveTime(newEffectiveTime: Moment, form: AbstractControl, field: string): void {
		(form.get(field) as FormGroup).controls.effectiveTime.setValue(newEffectiveTime);
	}

	back(): void {
		this.cancel.emit();
	}

	private mapRiskFactorToDto(riskFactorValue): NewEffectiveClinicalObservationDto {
		const effectiveObservation: EffectiveObservation = this.factoresDeRiesgoFormService.getEffectiveObservation(riskFactorValue);
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
				bloodOxygenSaturation: this.mapRiskFactorToDto(formValue.breathing.bloodOxygenSaturation),
				respiratoryRate: this.mapRiskFactorToDto(formValue.breathing.respiratoryRate)
			},
			circulation: {
				...formValue.circulation,
				heartRate: this.mapRiskFactorToDto(formValue.circulation.heartRate)
			},
			notes: formValue.notes
		};
	}

	clear(control: any, value: string): void {
		this.CLEAR_CASES[value](control);
	}

}
