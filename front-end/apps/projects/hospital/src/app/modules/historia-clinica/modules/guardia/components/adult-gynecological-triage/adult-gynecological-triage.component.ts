import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { BasicPatientDto, PersonPhotoDto, TriageAdultGynecologicalDto, TriageListDto } from '@api-rest/api-model';
import { FactoresDeRiesgoFormService, RiskFactorsValue } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { Triage } from '../triage/triage.component';
import { Observable, forkJoin } from 'rxjs';
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientNameService } from '@core/services/patient-name.service';


@Component({
	selector: 'app-adult-gynecological-triage',
	templateUrl: './adult-gynecological-triage.component.html',
	styleUrls: ['./adult-gynecological-triage.component.scss']
})
export class AdultGynecologicalTriageComponent implements OnInit {

	private triageData: Triage;
	adultGynecologicalForm: UntypedFormGroup;
	riskFactorsForm: UntypedFormGroup;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	patientSummary: PatientSummary;
	patientDescription: string;

	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Input() disableConfirmButton: boolean;
	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Input() lastTriage$: Observable<TriageListDto>;
	@Output() confirm = new EventEmitter();
	@Output() cancel = new EventEmitter();

	constructor(
		private formBuilder: UntypedFormBuilder,
		private guardiaMapperService: GuardiaMapperService,
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
		this.adultGynecologicalForm = this.formBuilder.group({
			observation: [null]
		});
		this.riskFactorsForm = this.factoresDeRiesgoFormService.getForm();
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

	confirmAdultGynecologicalTriage(): void {
		const formValue = this.adultGynecologicalForm.value;
		if (this.adultGynecologicalForm.valid && this.riskFactorsForm.valid) {
			this.disableConfirmButton = true;
			const riskFactorsValue: RiskFactorsValue = this.factoresDeRiesgoFormService.buildRiskFactorsValue(this.riskFactorsForm);
			const triage: TriageAdultGynecologicalDto = {
				categoryId: this.triageData.triageCategoryId,
				doctorsOfficeId: this.triageData.doctorsOfficeId,
				notes: formValue.observation,
				riskFactors: this.guardiaMapperService.riskFactorsValuetoNewRiskFactorsObservationDto(riskFactorsValue),
				reasons: this.triageData.reasons
			};
			this.confirm.emit(triage);
		}
	}

	back(): void {
		this.cancel.emit();
	}
}

export function toPatientSummary(basicData: BasicPatientDto, photo: PersonPhotoDto, patientNameService: PatientNameService): PatientSummary {
	const { firstName, nameSelfDetermination, lastName, middleNames, otherLastNames } = basicData.person;
	return {
		fullName: patientNameService.completeName(firstName, nameSelfDetermination, lastName, middleNames, otherLastNames),
		...(basicData.identificationType && {
			identification: {
				type: basicData.identificationType,
				number: +basicData.identificationNumber
			}
		}),
		id: basicData.id,
		gender: basicData.person.gender?.description || null,
		age: basicData.person.age || null,
		photo: photo.imageData
	}
}