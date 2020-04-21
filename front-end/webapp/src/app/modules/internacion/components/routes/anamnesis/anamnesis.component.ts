import { Component, OnInit } from '@angular/core';
import { map, tap } from 'rxjs/operators';
import { PatientService } from '@api-rest/services/patient.service';
import { BasicPatientDto, InternmentSummaryDto } from '@api-rest/api-model';
import { PatientBasicData } from 'src/app/modules/presentation/patient-card/patient-card.component';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { InternmentEpisode } from '../../../../presentation/internment-episode-summary/internment-episode-summary.component';
import { InternacionService } from '@api-rest/services/internacion.service';

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
		private route: ActivatedRoute,
	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				let patientId = Number(params.get('idPaciente'));
				let internmentId = Number(params.get('idInternacion'));

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(patientId).pipe(
					map((patient: BasicPatientDto): PatientBasicData => {
						return {
							id: patient.id,
							firstName: patient.person.firstName,
							lastName: patient.person.lastName,
							gender: patient.person.gender.description,
							age: patient.person.age
						};
					})
				);

				this.internmentEpisode$ = this.internmentService.getInternmentEpisodeSummary<InternmentSummaryDto>(internmentId).pipe(
					map((internmentSummary: InternmentSummaryDto): InternmentEpisode => {
						return {
							room: {
								number: internmentSummary.bed.bedNumber
							},
							bed: {
								number: internmentSummary.bed.room.roomNumber
							},
							specialty: {
								name: internmentSummary.specialty.name
							},
							doctor: {
								firstName: internmentSummary.doctor.firstName,
								lastName: internmentSummary.doctor.lastName,
								license: internmentSummary.doctor.licence
							},
							totalInternmentDays: internmentSummary.totalInternmentDays,
							admissionDatetime: internmentSummary.createdOn.toString()
						};
					})
				);
			}
		);
	}

}
