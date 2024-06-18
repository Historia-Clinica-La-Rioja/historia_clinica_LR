import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { map, take, Observable } from 'rxjs';
import { toStudyAppointment } from '../../utils/mapper.utils';
import { StudyAppointmentReportService } from '@api-rest/services/study-appointment-report.service';
import { StudyAppointment } from '../../models/models';
import { WINDOW } from '../../constants/token';

@Component({
	selector: 'app-study-details',
	templateUrl: './study-details.component.html',
	styleUrls: ['./study-details.component.scss']
})
export class StudyDetailsComponent implements OnInit {

	url: string;
	study$: Observable<StudyAppointment>;
	appointmentId: number;
	reportIsDone = false;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly studyAppointmentReportService: StudyAppointmentReportService,
		@Inject(WINDOW) private window: Window,
	) { }

	ngOnInit(): void {
		this.url = `institucion/${this.contextService.institutionId}/ambulatoria/paciente/`;

		this.route.paramMap.pipe(take(1)).subscribe(
			(params) => {
				this.appointmentId = Number(params.get('id'));
				this.getStudy();
			});

	}

	goBack() {
		this.router.navigate([`institucion/${this.contextService.institutionId}/imagenes/lista-trabajos`]);
	}

	goUrlLocalStudy(studyLocalUrl: string) {
		this.window.open(studyLocalUrl, "_blank")
	}

	private getStudy() {
		this.study$ = this.studyAppointmentReportService.getStudyByAppointmentId(this.appointmentId)
			.pipe(
				map(study => toStudyAppointment(study))
			);
	}

	setUpdatedStatus(event: Observable<StudyAppointment>) {
		this.study$ = event;
		this.reportIsDone = true;
	}

}
