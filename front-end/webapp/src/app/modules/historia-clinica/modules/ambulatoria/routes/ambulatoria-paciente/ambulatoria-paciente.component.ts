import { Component, OnInit } from '@angular/core';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { ActivatedRoute, Router } from '@angular/router';
import { BasicPatientDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '@presentation/services/mapper.service';

@Component({
	selector: 'app-ambulatoria-paciente',
	templateUrl: './ambulatoria-paciente.component.html',
	styleUrls: ['./ambulatoria-paciente.component.scss']
})
export class AmbulatoriaPacienteComponent implements OnInit {

	patient$: Observable<PatientBasicData>;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly router: Router,
) {}

	ngOnInit(): void {
		this.route.paramMap.subscribe((params) => {
			const patientId = Number(params.get('idPaciente'));
			this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(patientId).pipe(
				map(patient => this.mapperService.toPatientBasicData(patient))
			);
		});
	}

	goToNuevaConsulta() {
		this.router.navigateByUrl(`${this.router.url}/nueva`);
	}
}
