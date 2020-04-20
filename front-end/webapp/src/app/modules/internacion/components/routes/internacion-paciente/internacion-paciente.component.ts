import { Component, OnInit } from '@angular/core';
import { PatientBasicData } from 'src/app/modules/presentation/patient-card/patient-card.component';
import { PatientService } from '@api-rest/services/patient.service';
import { BasicPatientDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-internacion-paciente',
	templateUrl: './internacion-paciente.component.html',
	styleUrls: ['./internacion-paciente.component.scss']
})
export class InternacionPacienteComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;

	constructor(
		private patientService: PatientService,
		private route: ActivatedRoute,
	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				let patientId = Number(params.get('idPaciente'));
				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(patientId).pipe(
					map((patient: BasicPatientDto): PatientBasicData => {
						return {
							id: patient.id,
							firstName: patient.person.firstName,
							lastName: patient.person.lastName,
							gender: patient.person.gender.description,
							age: patient.person.age
						}
					})
				);
			}
		);
	}

}
