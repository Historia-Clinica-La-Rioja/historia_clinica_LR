import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PacsListDto, StudyIntanceUIDDto, TokenDto, ViewerUrlDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { StudyPACAssociationService } from '@api-rest/services/study-PAC-association';
import { StudyPermissionService } from '@api-rest/services/study-permission.service';
import { ViewerService } from '@api-rest/services/viewer.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { switchMap, reduce, Observable, of, throwError } from 'rxjs';

@Component({
	selector: 'app-view-study',
	templateUrl: './view-study.component.html',
	styleUrls: ['./view-study.component.scss']
})
export class ViewStudyComponent {

	@Input() appointmentId: number;
	@Input() isImageId: boolean = false;
	@Input() studyAvailable?: boolean;

	constructor(
		private readonly appointmentService: AppointmentsService,
		private readonly studyPACAssociationService: StudyPACAssociationService,
		private readonly studyPermissionService: StudyPermissionService,
		private readonly viewerService: ViewerService,
		public dialog: MatDialog,
	) { }

	viewStudy() {
		const sourceView$: Observable<StudyIntanceUIDDto> = this.isImageId ? of( {uid:this.appointmentId.toString()}) : this.appointmentService.getStudyInstanceUID(this.appointmentId)
		sourceView$.pipe(
		switchMap((studyInstanceUID: StudyIntanceUIDDto) =>
			this.studyPACAssociationService.getPacGlobalURL(studyInstanceUID.uid).pipe(
				switchMap((pacs: PacsListDto) => {
					if (pacs) {
						return this.studyPermissionService.getPermissionsJWT(studyInstanceUID.uid).pipe(
							switchMap((token: TokenDto) =>
								this.viewerService.getUrl().pipe(
									switchMap((url: ViewerUrlDto) =>
										this.buildUrl(url.url, studyInstanceUID.uid, token.token, pacs.pacs[0].url) // se queda el primero no dominio
									),
									reduce((result, value) => result + value)
								)
							)
						) 
					} else {
						throwError;
					}
				})
			)
		))
		.subscribe({
			next: (url) => window.open(url, "_blank"),
			error: (e) => {
				this.dialog.open(DiscardWarningComponent, {
					data: getErrorDataDialog(),
					minWidth: '30%'
				});

				function getErrorDataDialog() {
					return {
						title: 'image-network.worklist.details_study.ERROR_VIEW_STUDY',
						content: '',
						okButtonLabel: 'buttons.ACCEPT',
						errorMode: true,
						color: 'warn'
					};
				}
			}
		});
	}

	private buildUrl(url: string, studyInstanceUID: string, token: string, server: string): string {
		return `${url}?StudyInstanceUIDs=${studyInstanceUID}&server=${server}&token=${token}`;
	}

}

export interface ViewStudyModel {
	idReference: number;
	isImageId: boolean;
}


