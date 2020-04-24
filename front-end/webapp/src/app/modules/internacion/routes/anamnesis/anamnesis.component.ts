import { Component, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';
import { PatientService } from '@api-rest/services/patient.service';
import { BasicPatientDto, InternmentSummaryDto } from '@api-rest/api-model';
import { PatientBasicData } from 'src/app/modules/presentation/components/patient-card/patient-card.component';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { InternmentEpisode } from '../../../presentation/components/internment-episode-summary/internment-episode-summary.component';
import { InternacionService } from '@api-rest/services/internacion.service';
import { MapperService } from '../../../presentation/services/mapper.service';

@Component({
	selector: 'app-anamnesis',
	templateUrl: './anamnesis.component.html',
	styleUrls: ['./anamnesis.component.scss']
})
export class AnamnesisComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;
	public internmentEpisode$: Observable<InternmentEpisode>;

	constructor(
		private patientService: PatientService,
		private internmentService: InternacionService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				let patientId = Number(params.get('idPaciente'));
				let internmentId = Number(params.get('idInternacion'));

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.internmentEpisode$ = this.internmentService.getInternmentEpisodeSummary<InternmentSummaryDto>(internmentId).pipe(
					map(internmentEpisodeSummary => this.mapperService.toInternmentEpisode(internmentEpisodeSummary))
				);
			}
		);
	}

}
