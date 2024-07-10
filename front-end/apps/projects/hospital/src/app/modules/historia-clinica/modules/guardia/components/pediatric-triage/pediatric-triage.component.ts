import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { BasicPatientDto, MasterDataDto, NewEffectiveClinicalObservationDto, PersonPhotoDto, TriageListDto, TriagePediatricDto } from '@api-rest/api-model';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { getError, hasError } from '@core/utils/form.utils';
import { FACTORES_DE_RIESGO } from '@historia-clinica/constants/validation-constants';
import { EffectiveObservation, FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { Observable, forkJoin } from 'rxjs';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { Triage } from '../triage/triage.component';
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';
import { ActivatedRoute } from '@angular/router';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { toPatientSummary } from '../adult-gynecological-triage/adult-gynecological-triage.component';

@Component({
	selector: 'app-pediatric-triage',
	templateUrl: './pediatric-triage.component.html',
	styleUrls: ['./pediatric-triage.component.scss']
})
export class PediatricTriageComponent implements OnInit {

	private triageData: Triage;
	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Input() disableConfirmButton: boolean;
	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Input() lastTriage$: Observable<TriageListDto>;
	@Output() confirm = new EventEmitter();
	@Output() cancel = new EventEmitter();

	pediatricForm: UntypedFormGroup;
	bodyTemperatures$: Observable<MasterDataDto[]>;
	muscleHypertonyaOptions$: Observable<MasterDataDto[]>;
	perfusionOptions$: Observable<MasterDataDto[]>;
	respiratoryRetractionOptions$: Observable<MasterDataDto[]>;
	patientSummary: PatientSummary;
	patientDescription: string;

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
		private formBuilder: UntypedFormBuilder,
		private readonly triageMasterDataService: TriageMasterDataService,
		private readonly translateService: TranslateService,
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly patientNameService: PatientNameService
	) {
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(this.formBuilder, this.translateService);
	}

	ngOnInit(): void {
		this.route.queryParams.subscribe(param => {
			if (param.patientId) this.setPatientInfo(Number(param.patientId));
			else this.patientDescription = param.patientDescription;
		});

		this.pediatricForm = this.formBuilder.group({
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
		this.bodyTemperatures$ = this.triageMasterDataService.getBodyTemperature();
		this.muscleHypertonyaOptions$ = this.triageMasterDataService.getMuscleHypertonia();
		this.perfusionOptions$ = this.triageMasterDataService.getPerfusion();
		this.respiratoryRetractionOptions$ = this.triageMasterDataService.getRespiratoryRetraction();
	}

	setPatientInfo(patientId: number) {
		const patientBasicData$: Observable<BasicPatientDto> = this.patientService.getPatientBasicData(patientId);
		const patientPhoto$: Observable<PersonPhotoDto> = this.patientService.getPatientPhoto(patientId);
		forkJoin([patientBasicData$, patientPhoto$]).subscribe(([patientBasicData, patientPhoto]) => {
			this.patientSummary = toPatientSummary(patientBasicData, patientPhoto, this.patientNameService);
		});
	}

	setTriageData(triageData: Triage) {
		this.triageData = triageData;
	}

	confirmPediatricTriage(): void {
		if (this.pediatricForm.valid) {
			this.disableConfirmButton = true;
			const triage: TriagePediatricDto = this.buildTriagePediatricDto();
			this.confirm.emit(triage);
		}
	}

	setRiskFactorEffectiveTime(newEffectiveTime: Date, form: AbstractControl, field: string): void {
		(form.get(field) as UntypedFormGroup).controls.effectiveTime.setValue(newEffectiveTime);
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
			categoryId: this.triageData.triageCategoryId,
			doctorsOfficeId: this.triageData.doctorsOfficeId,
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
			reasons: this.triageData.reasons
		};
	}

	clear(control: any, value: string): void {
		this.CLEAR_CASES[value](control);
	}

}
