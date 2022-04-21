import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { HCEAllergyDto, HCEAnthropometricDataDto, HCELast2RiskFactorsDto, HCEMedicationDto, HCEPersonalHistoryDto } from '@api-rest/api-model';
import { ANTECEDENTES_FAMILIARES, MEDICACION_HABITUAL, PROBLEMAS_ANTECEDENTES } from '../../../historia-clinica/constants/summaries';
import { PatientPortalService } from '@api-rest/services/patient-portal.service';

@Component({
	selector: 'app-resumen-historia-clinica',
	templateUrl: './resumen-historia-clinica.component.html',
	styleUrls: ['./resumen-historia-clinica.component.scss']
})
export class ResumenHistoriaClinicaComponent implements OnInit {

	private anthropometricDataListSubject: Subject<HCEAnthropometricDataDto[]> = new BehaviorSubject<HCEAnthropometricDataDto[]>([]);

	public allergies$: Observable<HCEAllergyDto[]>;
	public familyHistories$: Observable<HCEPersonalHistoryDto[]>;
	public readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	public personalHistory$: Observable<HCEPersonalHistoryDto[]>;
	public readonly personalProblemsHeader = PROBLEMAS_ANTECEDENTES;
	public readonly medicationsHeader = MEDICACION_HABITUAL;
	public medications$: Observable<HCEMedicationDto[]>;
	public riskFactors$: Observable<HCELast2RiskFactorsDto>;
	public anthropometricDataList$: Observable<HCEAnthropometricDataDto[]> = this.anthropometricDataListSubject.asObservable();


	constructor(
		private readonly patientPortalService: PatientPortalService,
	) { }

	ngOnInit(): void {
		this.personalHistory$ = this.patientPortalService.getPersonalHistories();
		this.familyHistories$ = this.patientPortalService.getFamilyHistories();
		this.medications$ = this.patientPortalService.getMedications();
		this.allergies$ = this.patientPortalService.getAllergies();
		this.riskFactors$ = this.patientPortalService.getRiskFactors();

		this.patientPortalService.getLast2AnthropometricData().subscribe(
			aD => this.anthropometricDataListSubject.next(aD)
		);
	}

}
