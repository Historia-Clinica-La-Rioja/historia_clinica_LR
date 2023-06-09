import { Component, Input } from '@angular/core';
import { PacsUrlDto, StudyIntanceUIDDto, TokenDto, ViewerUrlDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { StudyPACAssociationService } from '@api-rest/services/study-PAC-association';
import { StudyPermissionService } from '@api-rest/services/study-permission.service';
import { ViewerService } from '@api-rest/services/viewer.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { switchMap, reduce } from 'rxjs';

@Component({
	selector: 'app-view-study',
	templateUrl: './view-study.component.html',
	styleUrls: ['./view-study.component.scss']
})
export class ViewStudyComponent {

	@Input() appointmentId: number;

	constructor(
		private readonly appointmentService: AppointmentsService,
		private readonly studyPACAssociationService: StudyPACAssociationService,
		private readonly studyPermissionService: StudyPermissionService,
		private readonly snackBarService: SnackBarService,
		private readonly viewerService: ViewerService
	) { }

	viewStudy() {
		this.appointmentService.getStudyInstanceUID(this.appointmentId).pipe(
			switchMap((studyInstanceUID: StudyIntanceUIDDto) =>
				this.studyPACAssociationService.getPacGlobalURL(studyInstanceUID.uid).pipe(
					switchMap((pacs: PacsUrlDto) =>
						this.studyPermissionService.getPermissionsJWT(studyInstanceUID.uid).pipe(
							switchMap((token: TokenDto) =>
								this.viewerService.getUrl().pipe(
									switchMap((url: ViewerUrlDto) =>
										this.buildUrl(url.url, studyInstanceUID.uid, token.token, pacs.pacs[0])
									),
									reduce((result, value) => result + value)
								)
							)
						)
					))
			))
			.subscribe({
				next: (url) => window.open(url, "_blank"),
				error: () => this.snackBarService.showError('image-network.worklist.details_study.SNACKBAR_ERROR_STUDY')
			});
	}

	private buildUrl(url: string, studyInstanceUID: string, token: string, server: string): string {
		return `${url}/${studyInstanceUID}?server=${server}&token=${token}`;
	}

}
