import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { TriageListDto } from '@api-rest/api-model';
import { FactoresDeRiesgoFormService, RiskFactorsValue } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { Triage } from '../triage/triage.component';
import { Observable, Subscription } from 'rxjs';
import { TriageActionsService } from '../../services/triage-actions.service';
import { SpecialtySectorFormValidityService } from '../../services/specialty-sector-form-validity.service';

@Component({
	selector: 'app-adult-gynecological-triage',
	templateUrl: './adult-gynecological-triage.component.html',
	styleUrls: ['./adult-gynecological-triage.component.scss']
})
export class AdultGynecologicalTriageComponent implements OnInit, OnDestroy {

	private verifyFormValidationSuscription: Subscription;
	adultGynecologicalForm: UntypedFormGroup;
	riskFactorsForm: UntypedFormGroup;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	isSpecialtySectorsFormValid: boolean;

	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Input() lastTriage$: Observable<TriageListDto>;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly translateService: TranslateService,
		private readonly triageActionsService: TriageActionsService,
		private specialtySectorFormValidityService: SpecialtySectorFormValidityService,
	) {
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(this.formBuilder, this.translateService);
	}

	ngOnInit() {
		this.specialtySectorFormValidityService.formValid$.subscribe((isValid) => {
			this.isSpecialtySectorsFormValid = isValid;
		});
		this.adultGynecologicalForm = this.formBuilder.group({
			observation: [null]
		});
		this.riskFactorsForm = this.factoresDeRiesgoFormService.getForm();
		this.subscribeToFormsChanges();
		this.verifyFormValidations();
	}

	ngOnDestroy() {
		this.specialtySectorFormValidityService.resetConfirmAttempt();
		this.verifyFormValidationSuscription.unsubscribe();
	}

	setTriageData(triageData: Triage) {
		this.triageActionsService.triageAdultGynecological = {
			...this.triageActionsService.triageAdultGynecological,
			categoryId: triageData.triageCategoryId,
			doctorsOfficeId: triageData.doctorsOfficeId,
			reasons: triageData.reasons,
			clinicalSpecialtySectorId: triageData.specialtySectorId
		}
	}

	private subscribeToFormsChanges() {
		this.subscribeToAdultGynecologicalFormChanges();
		this.subscribeToRiskFactorsFormChanges();
	}

	private subscribeToAdultGynecologicalFormChanges() {
		this.adultGynecologicalForm.valueChanges.subscribe(formValuesChanges => {
			this.triageActionsService.triageAdultGynecological = {
				...this.triageActionsService.triageAdultGynecological,
				notes: formValuesChanges.observation
			}
		});
	}

	private subscribeToRiskFactorsFormChanges() {
		this.riskFactorsForm.valueChanges.subscribe(riskFactorsValueChanges => {
			const riskFactorsValue: RiskFactorsValue = this.factoresDeRiesgoFormService.buildRiskFactorsValue(this.riskFactorsForm);
			const riskFactors = this.guardiaMapperService.riskFactorsValuetoNewRiskFactorsObservationDto(riskFactorsValue);
			this.triageActionsService.triageAdultGynecological = {
				...this.triageActionsService.triageAdultGynecological, riskFactors
			}
		});
	}

	private verifyFormValidations() {
		this.verifyFormValidationSuscription = this.triageActionsService.verifyFormValidation$.subscribe(_ => {
			this.specialtySectorFormValidityService.notifyConfirmAttempt();
			if (this.adultGynecologicalForm.valid && this.riskFactorsForm.valid && this.isSpecialtySectorsFormValid)
				this.triageActionsService.persist.next();
		});
	}
}
