import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { HCEAllergyDto, HCEAnthropometricDataDto, HCELast2RiskFactorsDto, HCEMedicationDto, HCEPersonalHistoryDto } from '@api-rest/api-model';
import { ANTECEDENTES_FAMILIARES, MEDICACION_HABITUAL, PROBLEMAS_ANTECEDENTES } from '../../../historia-clinica/constants/summaries';
import { PatientPortalService } from '@api-rest/services/patient-portal.service';

@Component({
	selector: 'app-resumen-historia-clinica',
	templateUrl: './resumen-historia-clinica.component.html',
	styleUrls: ['./resumen-historia-clinica.component.scss']
})
export class ResumenHistoriaClinicaComponent implements OnInit {

	public allergies$: Observable<HCEAllergyDto[]>;
	public familyHistories$: Observable<HCEPersonalHistoryDto[]>;
	public readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	public personalHistory$: Observable<HCEPersonalHistoryDto[]>;
	public readonly personalProblemsHeader = PROBLEMAS_ANTECEDENTES;
	public readonly medicationsHeader = MEDICACION_HABITUAL;
	public medications$: Observable<HCEMedicationDto[]>;
	public riskFactors$: Observable<HCELast2RiskFactorsDto>;
	public anthropometricData$: Observable<HCEAnthropometricDataDto>;

	constructor(
		private readonly patientPortalService: PatientPortalService,
	) { }

	ngOnInit(): void {
		this.personalHistory$ = this.patientPortalService.getPersonalHistories();
		this.familyHistories$ = this.patientPortalService.getFamilyHistories();
		this.medications$ = this.patientPortalService.getMedications();
		this.allergies$ = this.patientPortalService.getAllergies();
		this.anthropometricData$ = this.patientPortalService.getAnthropometricData();
		this.riskFactors$ = this.patientPortalService.getRiskFactors();
	}

}
