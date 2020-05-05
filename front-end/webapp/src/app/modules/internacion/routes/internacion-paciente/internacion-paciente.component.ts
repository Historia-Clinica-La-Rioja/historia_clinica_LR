import { Component, OnInit } from '@angular/core';
import { PatientBasicData } from 'src/app/modules/presentation/components/patient-card/patient-card.component';
import { PatientService } from '@api-rest/services/patient.service';
import { BasicPatientDto, InternmentSummaryDto, AnamnesisSummaryDto } from '@api-rest/api-model';
import { map, tap } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MapperService } from 'src/app/modules/presentation/services/mapper.service';
import { InternacionService } from '@api-rest/services/internacion.service';
import { InternmentEpisode } from 'src/app/modules/presentation/components/internment-episode-summary/internment-episode-summary.component';
import { INTERNACION } from '../../constants/summaries';

@Component({
	selector: 'app-internacion-paciente',
	templateUrl: './internacion-paciente.component.html',
	styleUrls: ['./internacion-paciente.component.scss']
})
export class InternacionPacienteComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;

	public internacionSummary = INTERNACION;
	public internmentEpisode$: Observable<InternmentEpisode>;
	public anamnesisDoc: AnamnesisSummaryDto;

	constructor(
		private patientService: PatientService,
		private internmentService: InternacionService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private router: Router,
	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				let patientId = Number(params.get('idPaciente'));
				let internmentId = String(params.get('idInternacion'));

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.internmentEpisode$ = this.internmentService.getInternmentEpisodeSummary<InternmentSummaryDto>(internmentId).pipe(
					tap(internmentEpisodeSummary => this.anamnesisDoc = internmentEpisodeSummary.documents?.anamnesis),
					map(internmentEpisodeSummary => this.mapperService.toInternmentEpisode(internmentEpisodeSummary))
				);

			}
		);
	}

	goToAnamnesis(): void {
		if (this.anamnesisDoc?.id)
			this.router.navigate([`${this.router.url}/anamnesis/${this.anamnesisDoc?.id}`]);
		else
			this.router.navigate([`${this.router.url}/anamnesis`]);
	}

}
