import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ShowEntryCallService } from 'projects/hospital/src/app/modules/telemedicina/show-entry-call.service';
import { EntryCallComponent } from '../../../institucion/components/entry-call/entry-call.component';
import { EVirtualConsultationPriority, VirtualConsultationNotificationDataDto } from '@api-rest/api-model';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { Priority } from '@presentation/components/priority/priority.component';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';
import { CallDetails } from '../../../telemedicina/components/call-details/call-details.component';

@Component({
	selector: 'app-entry-call-renderer',
	templateUrl: './entry-call-renderer.component.html',
	styleUrls: ['./entry-call-renderer.component.scss']
})

/*  Este es el componente que siempre esta activo esperando
	que (desde showEntryCallService) le digan que tiene que mostrar
	o cortar una solicitud de llamada ( dialogo )
*/
export class EntryCallRendererComponent implements OnInit {

	private dialogRef: MatDialogRef<EntryCallComponent>;

	constructor(
		private readonly showEntryCallService: ShowEntryCallService,
		private readonly dialog: MatDialog,
		private readonly virtualConsultationService: VirtualConstultationService,
		private readonly jitsiCallService: JitsiCallService
	) { }


	ngOnInit(): void {
		this.showEntryCallService.$entryCall.subscribe(
			(entryCall: VirtualConsultationNotificationDataDto) => {

				if (entryCall) {
					const data = toCallDetails(entryCall)
					this.dialogRef = this.dialog.open(EntryCallComponent,
						{
							data,
							disableClose: true,
						});

					this.dialogRef.afterClosed().subscribe(
						accepted => {
							if (accepted) {
								this.virtualConsultationService.notifyVirtualConsultationAcceptedCall(entryCall.virtualConsultationId).subscribe(
									_ => this.jitsiCallService.open(data.link)
								);
							} else {
								this.virtualConsultationService.notifyVirtualConsultationRejectedCall(entryCall.virtualConsultationId).subscribe(
									_ => {
										this.dialogRef.close();
									}
								)
							}
						}
					)

				}
			}
		)
	}

}

export const toCallDetails = (entryCall: VirtualConsultationNotificationDataDto): CallDetails => {
	return {
		link: entryCall.callLink,
		clinicalSpecialty: entryCall.clinicalSpecialty,
		createdOn: dateTimeDtotoLocalDate(entryCall.creationDateTime),
		institutionName: entryCall.institutionName,
		patient: {
			fullName: `${entryCall.patientData.firstName} ${entryCall.patientData.lastName}`,
		},
		priority: mappedPriorities[entryCall.priority],
		professionalFullName: `${entryCall.responsibleLastName} ${entryCall.responsibleFirstName}`,
	}
}


export const mappedPriorities = {
	[EVirtualConsultationPriority.HIGH]: Priority.HIGH,
	[EVirtualConsultationPriority.MEDIUM]: Priority.MEDIUM,
	[EVirtualConsultationPriority.LOW]: Priority.LOW
}
