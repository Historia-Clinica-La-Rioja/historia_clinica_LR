import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { TriageAdultGynecologicalDto } from '@api-rest/api-model';
import { FactoresDeRiesgoFormService, RiskFactorsValue } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';

@Component({
	selector: 'app-adult-gynecological-triage',
	templateUrl: './adult-gynecological-triage.component.html',
	styleUrls: ['./adult-gynecological-triage.component.scss']
})
export class AdultGynecologicalTriageComponent implements OnInit {

	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Input() disableConfirmButton: boolean;
	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Output() confirm = new EventEmitter();
	@Output() cancel = new EventEmitter();

	private triageCategoryId: number;
	private doctorsOfficeId: number;

	adultGynecologicalForm: UntypedFormGroup;
	riskFactorsForm: UntypedFormGroup;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;


	constructor(
		private formBuilder: UntypedFormBuilder,
		private guardiaMapperService: GuardiaMapperService,
		private readonly translateService: TranslateService,
	) {
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(this.formBuilder, this.translateService);
	}

	ngOnInit(): void {
		this.adultGynecologicalForm = this.formBuilder.group({
			observation: [null]
		});
		this.riskFactorsForm = this.factoresDeRiesgoFormService.getForm();
	}

	setTriageCategoryId(triageCategoryId: number): void {
		this.triageCategoryId = triageCategoryId;
	}

	setDoctorsOfficeId(doctorsOfficeId: number): void {
		this.doctorsOfficeId = doctorsOfficeId;
	}

	confirmAdultGynecologicalTriage(): void {
		const formValue = this.adultGynecologicalForm.value;
		if (this.adultGynecologicalForm.valid && this.riskFactorsForm.valid) {
			this.disableConfirmButton = true;
			const riskFactorsValue: RiskFactorsValue = this.factoresDeRiesgoFormService.buildRiskFactorsValue(this.riskFactorsForm);
			const triage: TriageAdultGynecologicalDto = {
				categoryId: this.triageCategoryId,
				doctorsOfficeId: this.doctorsOfficeId,
				notes: formValue.observation,
				riskFactors: this.guardiaMapperService.riskFactorsValuetoNewRiskFactorsObservationDto(riskFactorsValue)
			};
			this.confirm.emit(triage);
		}
	}

	back(): void {
		this.cancel.emit();
	}
}
