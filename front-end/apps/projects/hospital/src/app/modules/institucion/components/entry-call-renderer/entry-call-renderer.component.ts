import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ShowEntryCallService } from 'projects/hospital/src/app/modules/telemedicina/show-entry-call.service';
import { EntryCall, EntryCallComponent } from '../entry-call/entry-call.component';
import { EVirtualConsultationPriority, EVirtualConsultationStatus, VirtualConsultationNotificationDataDto } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { Priority } from '@presentation/components/priority/priority.component';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';
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
					const data = toEntryCall(entryCall)
					this.dialogRef = this.dialog.open(EntryCallComponent,
						{ data });

					this.dialogRef.afterClosed().subscribe(
						accepted => {
							if (accepted) {
								this.virtualConsultationService.changeVirtualConsultationState(entryCall.virtualConsultationId, { status: EVirtualConsultationStatus.IN_PROGRESS}).subscribe();
								this.jitsiCallService.open(data.callId);
							}
						}
					)

				} else {
					this.dialogRef.close()
				}
			}
		)
	}

}

const toEntryCall = (entryCall: VirtualConsultationNotificationDataDto): EntryCall => {
	return {
		callId: entryCall.callId,
		clinicalSpecialty: entryCall.clinicalSpecialty,
		createdOn: dateTimeDtoToDate(entryCall.creationDateTime),
		institutionName: entryCall.institutionName,
		patient: {
			firstName: entryCall.patientName,
			id: null,
			lastName: entryCall.patientLastName,
			gender: null
		},
		priority: mappedPriorities[entryCall.priority],
		professionalFullName: `${entryCall.responsibleLastName} ${entryCall.responsibleFirstName}`,
	}
}


const mappedPriorities = {
	[EVirtualConsultationPriority.HIGH]: Priority.HIGH,
	[EVirtualConsultationPriority.MEDIUM]: Priority.MEDIUM,
	[EVirtualConsultationPriority.LOW]: Priority.LOW
}
