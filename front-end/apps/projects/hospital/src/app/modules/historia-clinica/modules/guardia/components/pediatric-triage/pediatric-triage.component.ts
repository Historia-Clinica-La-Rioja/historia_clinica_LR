import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MasterDataDto, NewEffectiveClinicalObservationDto, TriageListDto, TriagePediatricDto } from '@api-rest/api-model';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { getError, hasError } from '@core/utils/form.utils';
import { FACTORES_DE_RIESGO } from '@historia-clinica/constants/validation-constants';
import { EffectiveObservation, FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { Observable, Subscription } from 'rxjs';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { Triage } from '../triage/triage.component';
import { TriageActionsService } from '../../services/triage-actions.service';

@Component({
	selector: 'app-pediatric-triage',
	templateUrl: './pediatric-triage.component.html',
	styleUrls: ['./pediatric-triage.component.scss']
})
export class PediatricTriageComponent implements OnInit, OnDestroy {

	private verifyFormSuscription: Subscription;
	private CLEAR_CASES = {
		bodyTemperatureId: (control) => control.controls.bodyTemperatureId.reset(),
		muscleHypertoniaId: (control) => control.controls.muscleHypertoniaId.reset(),
		respiratoryRetractionId: (control) => control.controls.respiratoryRetractionId.reset(),
		perfusionId: (control) => control.controls.perfusionId.reset(),
	}
	
	pediatricForm: UntypedFormGroup;
	bodyTemperatures$: Observable<MasterDataDto[]>;
	muscleHypertonyaOptions$: Observable<MasterDataDto[]>;
	perfusionOptions$: Observable<MasterDataDto[]>;
	respiratoryRetractionOptions$: Observable<MasterDataDto[]>;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	hasError = hasError;
	getError = getError;

	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Input() lastTriage$: Observable<TriageListDto>;


	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly triageMasterDataService: TriageMasterDataService,
		private readonly translateService: TranslateService,
		private readonly triageActionsService: TriageActionsService,
	) {
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(this.formBuilder, this.translateService);
	}

	ngOnDestroy(): void {
		this.verifyFormSuscription.unsubscribe();
	}

	ngOnInit(): void {

		this.pediatricForm = this.buildPediatricForm();
		this.bodyTemperatures$ = this.triageMasterDataService.getBodyTemperature();
		this.muscleHypertonyaOptions$ = this.triageMasterDataService.getMuscleHypertonia();
		this.perfusionOptions$ = this.triageMasterDataService.getPerfusion();
		this.respiratoryRetractionOptions$ = this.triageMasterDataService.getRespiratoryRetraction();
		this.subscribeToFormChanges();
		this.initializePediatricInformation();

		this.verifyFormSuscription = this.triageActionsService.verifyFormValidation$.subscribe(_ => {
			if (this.pediatricForm.valid)
				this.triageActionsService.persist.next();
		});
	}

	setTriageData(triageData: Triage) {
		this.triageActionsService.pediatricTriage = {
			...this.triageActionsService.pediatricTriage,
			categoryId: triageData.triageCategoryId,
			doctorsOfficeId: triageData.doctorsOfficeId,
			reasons: triageData.reasons
		}
	}

	setRiskFactorEffectiveTime(newEffectiveTime: Date, form: AbstractControl, field: string): void {
		(form.get(field) as UntypedFormGroup).controls.effectiveTime.setValue(newEffectiveTime);
	}

	private mapRiskFactorToDto(riskFactorValue): NewEffectiveClinicalObservationDto {
		const effectiveObservation: EffectiveObservation = this.factoresDeRiesgoFormService.getEffectiveObservation(riskFactorValue);
		return GuardiaMapperService._mapEffectiveObservationToNewEffectiveClinicalObservationDto(effectiveObservation);
	}


	private buildTriagePediatricDto(): TriagePediatricDto {
		const formValue = this.pediatricForm.value;
		return {
			...this.triageActionsService.pediatricTriage,
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
			notes: formValue.observation,
		};
	}

	clear(control: any, value: string): void {
		this.CLEAR_CASES[value](control);
	}

	private buildPediatricForm() {
		return this.formBuilder.group({
			observation: [null],
			appearance: this.formBuilder.group({
				bodyTemperatureId: [null],
				cryingExcessive: [null],
				muscleHypertoniaId: [null]

			}),
			breathing: this.formBuilder.group({
				bloodOxygenSaturation: this.formBuilder.group({
					value: [null, Validators.min(FACTORES_DE_RIESGO.MIN.bloodOxygenSaturation)],
					effectiveTime: [new Date()]
				}),
				respiratoryRate: this.formBuilder.group({
					value: [null, Validators.min(FACTORES_DE_RIESGO.MIN.respiratoryRate)],
					effectiveTime: [new Date()]
				}),
				respiratoryRetractionId: [null],
				stridor: [null]
			}),
			circulation: this.formBuilder.group({
				heartRate: this.formBuilder.group({
					value: [null, Validators.min(FACTORES_DE_RIESGO.MIN.heartRate)],
					effectiveTime: [new Date()]
				}),
				perfusionId: [null]
			}),
		});
	}

	private subscribeToFormChanges() {
		this.pediatricForm.valueChanges.subscribe(pediatricFormChanges => {
			this.triageActionsService.pediatricTriage = this.buildTriagePediatricDto();
		});
	}

	private initializePediatricInformation() {
		this.triageActionsService.pediatricTriage = this.buildTriagePediatricDto();
	}

}
