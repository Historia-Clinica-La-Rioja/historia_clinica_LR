import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import {HCEAllergyDto, HCEAnthropometricDataDto, HCELast2VitalSignsDto, HCEMedicationDto, HCEPersonalHistoryDto} from '@api-rest/api-model';
import {ANTECEDENTES_FAMILIARES, PROBLEMAS_ANTECEDENTES} from '../../../historia-clinica/constants/summaries';
import {PatientHceService} from '@api-rest/services/patient-hce.service';

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
	public medications$: Observable<HCEMedicationDto[]>;
	public vitalSigns$: Observable<HCELast2VitalSignsDto>;
	public anthropometricData$: Observable<HCEAnthropometricDataDto>;

  	constructor(
		private readonly patientHceService: PatientHceService,
	) { }

  	ngOnInit(): void {
  		this.personalHistory$ = this.patientHceService.getPersonalHistories();
  		this.familyHistories$ = this.patientHceService.getFamilyHistories();
  		this.medications$ = this.patientHceService.getMedications();
  		this.allergies$ = this.patientHceService.getAllergies();
  		this.anthropometricData$ = this.patientHceService.getAnthropometricData();
  		this.vitalSigns$ = this.patientHceService.getVitalSigns();
  	}



}
