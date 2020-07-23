import { Component, OnInit } from '@angular/core';
import { HCEAllergyDto, HCEPersonalHistoryDto, HCEMedicationDto, HCELast2VitalSignsDto, HCEAnthropometricDataDto } from "@api-rest/api-model";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { HceGeneralStateService } from "@api-rest/services/hce-general-state.service";
import { ANTECEDENTES_FAMILIARES, PROBLEMAS_ANTECEDENTES } from "../../../../constants/summaries";

@Component({
	selector: 'app-resumen',
	templateUrl: './resumen.component.html',
	styleUrls: ['./resumen.component.scss']
})
export class ResumenComponent implements OnInit {

	public allergies$: Observable<HCEAllergyDto[]>;
	public patientId: number;
	public familyHistories$: Observable<HCEPersonalHistoryDto[]>;
	public personalHistory$: Observable<HCEPersonalHistoryDto[]>;
	public medications$: Observable<HCEMedicationDto[]>;
	public vitalSigns$: Observable<HCELast2VitalSignsDto>;
	public anthropometricData$: Observable<HCEAnthropometricDataDto>;
	public readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	public readonly personalProblemsHeader = PROBLEMAS_ANTECEDENTES;

	constructor(private readonly hceGeneralStateService: HceGeneralStateService,
		private route: ActivatedRoute) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.initSummaries();
			});
	}

	initSummaries() {
		this.allergies$ = this.hceGeneralStateService.getAllergies(this.patientId);
		this.familyHistories$ = this.hceGeneralStateService.getFamilyHistories(this.patientId);
		this.personalHistory$ = this.hceGeneralStateService.getPersonalHistories(this.patientId);
		this.medications$ = this.hceGeneralStateService.getMedications(this.patientId);
		this.vitalSigns$ = this.hceGeneralStateService.getVitalSigns(this.patientId);
		this.anthropometricData$ = this.hceGeneralStateService.getAnthropometricData(this.patientId);
	}

}
