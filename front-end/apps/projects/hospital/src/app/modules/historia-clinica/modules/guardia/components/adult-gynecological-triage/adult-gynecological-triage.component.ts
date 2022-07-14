import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TriageAdultGynecologicalDto } from '@api-rest/api-model';
import { FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { RiskFactorsValue, RiskFactorsFormService } from '../../../../services/risk-factors-form.service';
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
	@Output() confirm = new EventEmitter();
	@Output() cancel = new EventEmitter();

	private triageCategoryId: number;
	private doctorsOfficeId: number;

	adultGynecologicalForm: FormGroup;
	riskFactorsForm: FormGroup;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;


	constructor(
		private formBuilder: FormBuilder,
		private guardiaMapperService: GuardiaMapperService,
		public riskFactorsFormService: RiskFactorsFormService,
		private readonly translateService: TranslateService,
	) {
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(formBuilder, translateService);
	}

	ngOnInit(): void {
		this.adultGynecologicalForm = this.formBuilder.group({
			evaluation: [null]
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
			const riskFactorsValue: RiskFactorsValue = this.riskFactorsFormService.buildRiskFactorsValue(this.riskFactorsForm);
			const triage: TriageAdultGynecologicalDto = {
				categoryId: this.triageCategoryId,
				doctorsOfficeId: this.doctorsOfficeId,
				notes: formValue.evaluation,
				riskFactors: this.guardiaMapperService.riskFactorsValuetoNewRiskFactorsObservationDto(riskFactorsValue)
			};
			this.confirm.emit(triage);
		}
	}

	back(): void {
		this.cancel.emit();
	}
}
