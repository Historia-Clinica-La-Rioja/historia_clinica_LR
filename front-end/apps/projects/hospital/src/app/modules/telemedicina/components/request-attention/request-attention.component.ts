import { Component, OnInit } from '@angular/core';
import { DateTimeDto, EVirtualConsultationPriority, EVirtualConsultationStatus, VirtualConsultationDto, VirtualConsultationInstitutionDataDto, VirtualConsultationPatientDataDto, VirtualConsultationResponsibleDataDto } from '@api-rest/api-model';
import { mapPriority, statusLabel, status } from '../../virtualConsultations.utils';
import { timeDifference } from '@core/utils/date.utils';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { Subscription, map, take, race, forkJoin } from 'rxjs';
import { VirtualConsultationsFacadeService } from '../../virtual-consultations-facade.service';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { InProgressCallComponent } from '../in-progress-call/in-progress-call.component';
import { EntryCallStompService } from '../../../api-web-socket/entry-call-stomp.service';
import { RejectedCallComponent } from '@institucion/components/rejected-call/rejected-call.component';
import { toCallDetails } from '@institucion/components/entry-call-renderer/entry-call-renderer.component';


@Component({
	selector: 'app-request-attention',
	templateUrl: './request-attention.component.html',
	styleUrls: ['./request-attention.component.scss']
})
export class RequestAttentionComponent implements OnInit {

	virtualConsultationsSubscription: Subscription;
	virtualConsultations: VirtualConsultation[] = [];
	toggleEnabled = false;
	virtualConsultatiosStatus = status;
	initialProfessionalStatus = false;

	constructor(
		private readonly virtualConsultationsFacadeService: VirtualConsultationsFacadeService,
		private virtualConsultationService: VirtualConstultationService,
		private jitsiCallService: JitsiCallService,
		private readonly dialog: MatDialog,
		private readonly callStatesService: EntryCallStompService
	) { }


	ngOnInit(): void {
		this.virtualConsultationsSubscription = this.virtualConsultationsFacadeService.virtualConsultationsAttention$
			.subscribe(virtualConsultations =>
				this.virtualConsultations = virtualConsultations.map(this.toVCToBeShown));

		this.virtualConsultationService.getProfessionalAvailability().subscribe(
			status => this.initialProfessionalStatus = status
		)
	}

	confirm(virtualConsultationId: number) {
		const ref = this.dialog.open(ConfirmDialogComponent, {
			data: {
				showMatIconError: true,
				title: `Confirmar atención`,
				cancelButtonLabel: 'NO, REGRESAR',
				okButtonLabel: 'SI, CONFIRMAR ATENCIÓN',
				content: `Si confirma la atención se asume que se efectuó una teleconsulta satisfactoriamente y la solicitud se quitará de la lista de espera. ¿Está seguro que desea confirmarla?`,
			},
			width: '33%'
		});

		ref.afterClosed().subscribe(
			closed => {
				if (closed) {
					this.virtualConsultationService.changeVirtualConsultationState(virtualConsultationId, { status: EVirtualConsultationStatus.FINISHED }).subscribe()
				}
			}
		)
	}

	cancel(virtualConsultationId: number) {
		const ref = this.dialog.open(ConfirmDialogComponent, {
			data: {
				showMatIconError: true,
				title: `Cancelar solicitud`,
				cancelButtonLabel: 'NO, REGRESAR',
				okButtonLabel: 'SI, CANCELAR ATENCIÓN',
				content: `Si cancela la solicitud la misma ya no se verá en el listado de espera de los profesionales para ser atendida. <strong>¿Está seguro que desea cancelarla?</strong>`,
				okBottonColor: 'warn'
			},
			width: '33%'
		});

		ref.afterClosed().subscribe(
			closed => {
				if (closed) {
					this.virtualConsultationService.changeVirtualConsultationState(virtualConsultationId, { status: EVirtualConsultationStatus.CANCELED }).subscribe()
				}
			}
		)
	}


	call(virtualConsultation: VirtualConsultation) {

		const notify$ = this.virtualConsultationService.notifyVirtualConsultationIncomingCall(virtualConsultation.id);
		const virtualConsultation$ = this.virtualConsultationService.getVirtualConsultationCall(virtualConsultation.id)
		forkJoin([notify$, virtualConsultation$])
			.subscribe(
				([notified, info]) => {
					const data = toCallDetails(info)
					const ref = this.dialog.open(InProgressCallComponent, { data, disableClose: true })

					ref.afterOpened().subscribe(
						_ => {

							const rejected$ = this.callStatesService.rejectedCall$.pipe(map(r => { return { ...r, origin: 'rejected' } }))
							const accepted$ = this.callStatesService.acceptedCall$.pipe(map(r => { return { ...r, origin: 'accepted' } }))

							race(rejected$, accepted$).pipe(take(1)).subscribe(
								(vc) => {
									ref.close();
									if (vc.origin === 'rejected') {
										const data = toCallDetails(vc);
										this.dialog.open(RejectedCallComponent, { data });
									} else {
										this.jitsiCallService.open(virtualConsultation.callId)
									}
								}
							)
						}
					)
				}
			)
	}


	availabilityChanged(availability: boolean) {
		this.virtualConsultationService.changeClinicalProfessionalAvailability(availability).subscribe();
		this.toggleEnabled = availability;
	}

	private toVCToBeShown(vc: VirtualConsultationDto): VirtualConsultation {
		return {
			...vc,
			statusLabel: statusLabel[vc.status],
			priorityLabel: mapPriority[vc.priority],
			waitingTime: timeDifference(dateTimeDtotoLocalDate(vc.creationDateTime))
		}
	}

}

interface VirtualConsultation {
	availableProfessionalsAmount?: number;
	callId?: string;
	careLine: string;
	clinicalSpecialty: string;
	creationDateTime: DateTimeDto;
	id: number;
	institutionData: VirtualConsultationInstitutionDataDto;
	motive: string;
	patientData: VirtualConsultationPatientDataDto;
	priority: EVirtualConsultationPriority;
	problem: string;
	responsibleData: VirtualConsultationResponsibleDataDto;
	status: EVirtualConsultationStatus;
	statusLabel: any,
	priorityLabel: string,
	waitingTime: string
}
