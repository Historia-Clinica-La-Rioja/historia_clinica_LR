import { Component, OnInit } from '@angular/core';
import { PatientBasicData } from 'src/app/modules/presentation/components/patient-card/patient-card.component';
import { PatientService } from '@api-rest/services/patient.service';
import { BasicPatientDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { SummaryHeader } from 'src/app/modules/presentation/components/summary-card/summary-card.component';

@Component({
	selector: 'app-internacion-paciente',
	templateUrl: './internacion-paciente.component.html',
	styleUrls: ['./internacion-paciente.component.scss']
})
export class InternacionPacienteComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;
	public internacionSummary: SummaryHeader = {
		title: 'Resumen internación',
		matIcon: 'single_bed'
	};

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
