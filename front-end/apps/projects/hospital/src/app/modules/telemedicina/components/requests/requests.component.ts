import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewTelemedicineRequestComponent } from '../../dialogs/new-telemedicine-request/new-telemedicine-request.component';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { ContextService } from '@core/services/context.service';
import { EVirtualConsultationStatus, VirtualConsultationDto } from '@api-rest/api-model';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { timeDifference } from '@core/utils/date.utils';
import { statusLabel, mapPriority, status } from '../../virtualConsultations.utils';
import { Subscription } from 'rxjs';
import { VirtualConsultationsFacadeService } from '../../virtual-consultations-facade.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';

@Component({
	selector: 'app-requests',
	templateUrl: './requests.component.html',
	styleUrls: ['./requests.component.scss']
})
export class RequestsComponent implements OnInit {

	virtualConsultations: VirtualConsultationDto[] = [];
	virtualConsultationsSubscription: Subscription;
	virtualConsultatiosStatus = status;
	initialResponsableStatus = false;
	constructor(
		private dialog: MatDialog,
		private virtualConsultationService: VirtualConstultationService,
		private contextService: ContextService,
		private readonly virtualConsultationsFacadeService: VirtualConsultationsFacadeService,
	) {
		this.virtualConsultationService.getResponsibleStatus(this.contextService.institutionId).subscribe(
			status => this.initialResponsableStatus = status
		)
	 }

	ngOnInit(): void {
		this.virtualConsultationsSubscription = this.virtualConsultationsFacadeService.virtualConsultationsRequest$
			.subscribe(virtualConsultations =>
				this.virtualConsultations = virtualConsultations.map(this.toVCToBeShown))
	}

	private toVCToBeShown(vc: VirtualConsultationDto) {
		return {
			...vc,
			statusLabel: statusLabel[vc.status],
			priorityLabel: mapPriority[vc.priority],
			waitingTime: timeDifference(dateTimeDtotoLocalDate(vc.creationDateTime))
		}
	}

	openAddRequest() {
		this.dialog.open(NewTelemedicineRequestComponent, {
			disableClose: true,
			width: '700px',
			height: '700px',
		})
	}

	availabilityChanged(available: boolean) {
		this.virtualConsultationService.changeResponsibleAttentionState(this.contextService.institutionId, available).subscribe();
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
}

