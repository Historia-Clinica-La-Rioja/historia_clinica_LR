import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BasicPatientDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { MapperService } from '@presentation/services/mapper.service';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';

@Component({
	selector: 'app-print-ambulatoria',
	templateUrl: './print-ambulatoria.component.html',
	styleUrls: ['./print-ambulatoria.component.scss']
})
export class PrintAmbulatoriaComponent implements OnInit {

	patient: PatientBasicData;
	patientId: number;
	personInformation: AdditionalInfo[] = [];
	personPhoto: PersonPhotoDto;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService
	) {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).subscribe(
					patient => {
						this.personInformation.push({ description: patient.person.identificationType, data: patient.person.identificationNumber });
						this.patient = this.mapperService.toPatientBasicData(patient);
					}
				);
				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
			}
		);
	}

	ngOnInit(): void {
	}

}
