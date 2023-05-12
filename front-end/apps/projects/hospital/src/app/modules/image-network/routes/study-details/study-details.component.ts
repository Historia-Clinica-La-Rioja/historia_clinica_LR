import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InformerObservationDto, StudyAppointmentDto } from '@api-rest/api-model';
import { WorklistService } from '@api-rest/services/worklist.service';
import { ContextService } from '@core/services/context.service';
import { State } from '../../components/worklist/worklist.component';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { mapToState } from '../../utils/study.utils';

@Component({
	selector: 'app-study-details',
	templateUrl: './study-details.component.html',
	styleUrls: ['./study-details.component.scss']
})
export class StudyDetailsComponent implements OnInit {

	study: StudyAppointment;
	appointmentId: number;
	datePipeFormat = DatePipeFormat;
	url: string;

	constructor(
		private readonly router: Router,
		readonly contextService: ContextService,
		private readonly route: ActivatedRoute,
		private readonly worklistService: WorklistService,
		readonly datePipe: DatePipe,
	) { }

	ngOnInit(): void {
		this.url = `institucion/${this.contextService.institutionId}/ambulatoria/paciente/`
		this.route.paramMap.subscribe(
			(params) => {
				this.appointmentId = Number(params.get('id'));
				this.getStudy();
			});
	}

	goBack() {
		this.router.navigate([`institucion/${this.contextService.institutionId}/imagenes/lista-trabajos`]);
	}

	private getStudy() {
		this.worklistService.getStudyByAppointmentId(this.appointmentId).subscribe((study: StudyAppointmentDto) => this.study = this.mapToStudyAppointment(study));
	}

	private mapToStudyAppointment(study: StudyAppointmentDto): StudyAppointment {
		return {
			informerObservations: study.informerObservations ? this.mapToInformerObservations(study.informerObservations) : null,
			patientFullName: study.patientFullName,
			state: mapToState(study.statusId),
			actionTime: dateTimeDtotoLocalDate(study.actionTime),
			patientId: study.patientId
		}
	}

	private mapToInformerObservations(informerObservations: InformerObservationDto): InformerObservation {
		return {
			observation: informerObservations.observations,
			createdBy: informerObservations.createdBy,
			createdOn: dateTimeDtotoLocalDate(informerObservations.createdOn),
		}
	}

}

interface StudyAppointment {
	informerObservations: InformerObservation;
	patientFullName: string;
	state: State;
	actionTime: Date;
	patientId: number;
}

interface InformerObservation {
	observation: string;
	createdBy: string;
	createdOn: Date;
}
