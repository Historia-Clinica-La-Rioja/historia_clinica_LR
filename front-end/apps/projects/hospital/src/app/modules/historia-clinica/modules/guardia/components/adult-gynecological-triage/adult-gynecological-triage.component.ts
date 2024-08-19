import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { TriageAdultGynecologicalDto, TriageListDto } from '@api-rest/api-model';
import { FactoresDeRiesgoFormService, RiskFactorsValue } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { Triage } from '../triage/triage.component';
import { Observable } from 'rxjs';


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
	) {
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(this.formBuilder, this.translateService);
	}

	ngOnInit(): void {
		this.adultGynecologicalForm = this.formBuilder.group({
			observation: [null]
		});
		this.riskFactorsForm = this.factoresDeRiesgoFormService.getForm();
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
