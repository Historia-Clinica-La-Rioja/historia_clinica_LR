import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { InternmentEpisodeSummary } from '@presentation/components/internment-episode-summary/internment-episode-summary.component';
import { PatientService } from '@api-rest/services/patient.service';
import { InternacionService } from '@api-rest/services/internacion.service';
import { MapperService } from '@presentation/services/mapper.service';
import { ActivatedRoute } from '@angular/router';
import { BasicPatientDto, InternmentSummaryDto, PersonPhotoDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';
import { ContextService } from '@core/services/context.service';

@Component({
  selector: 'app-epicrisis',
  templateUrl: './epicrisis.component.html',
  styleUrls: ['./epicrisis.component.scss']
})
export class EpicrisisComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;
	public personPhoto: PersonPhotoDto;
	public internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;

	constructor(
		private patientService: PatientService,
		private internmentService: InternacionService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				const patientId = Number(params.get('idPaciente'));
				const internmentId = Number(params.get('idInternacion'));

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.patientService.getPatientPhoto(patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => {this.personPhoto = personPhotoDto;
				});

				this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(internmentId).pipe(
					map((internmentEpisodeSummary: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisodeSummary))
				);
			}
		);
	}

}
