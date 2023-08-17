import { Component, OnInit } from '@angular/core';
import { EVirtualConsultationStatus, VirtualConsultationDto } from '@api-rest/api-model';
import { mapPriority, statusLabel, status } from '../../virtualConsultations.utils';
import { timeDifference } from '@core/utils/date.utils';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { Subscription } from 'rxjs';
import { VirtualConsultationsFacadeService } from '../../virtual-consultations-facade.service';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';

@Component({
	selector: 'app-request-attention',
	templateUrl: './request-attention.component.html',
	styleUrls: ['./request-attention.component.scss']
})
export class RequestAttentionComponent implements OnInit {

	virtualConsultationsSubscription: Subscription;
	virtualConsultations: any[] = [];
	toggleEnabled = false;
	virtualConsultatiosStatus = status;
	initialProfessionalStatus = false;

	constructor(
		private readonly virtualConsultationsFacadeService: VirtualConsultationsFacadeService,
		private virtualConsultationService: VirtualConstultationService,
		private jitsiCallService: JitsiCallService,
		private readonly dialog: MatDialog,
	) { }


	ngOnInit(): void {

		this.virtualConsultationsSubscription = this.virtualConsultationsFacadeService.virtualConsultations$
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


	joinMeet(virtualConsultation) {
		this.virtualConsultationService.notifyVirtualConsultationCall(virtualConsultation.id).subscribe(
			_ => {
				this.jitsiCallService.open(virtualConsultation.callId);
			}
		)
	}

	availabilityChanged(availability: boolean) {
		this.virtualConsultationService.changeClinicalProfessionalAvailability(availability).subscribe();
		this.toggleEnabled = availability;
	}

	private toVCToBeShown(vc: VirtualConsultationDto) {
		return {
			...vc,
			statusLabel: statusLabel[vc.status],
			priorityLabel: mapPriority[vc.priority],
			waitingTime: timeDifference(dateTimeDtotoLocalDate(vc.creationDateTime))
		}
	}

}
