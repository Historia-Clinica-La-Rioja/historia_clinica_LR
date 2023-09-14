import { Component, Input, OnInit } from '@angular/core';
import { CareLineDto, ClinicalSpecialtyDto, DateTimeDto, EVirtualConsultationPriority, EVirtualConsultationStatus, InstitutionBasicInfoDto, VirtualConsultationDto, VirtualConsultationFilterDto, VirtualConsultationInstitutionDataDto, VirtualConsultationPatientDataDto, VirtualConsultationResponsibleDataDto } from '@api-rest/api-model';
import { mapPriority, statusLabel, status } from '../../virtualConsultations.utils';
import { timeDifference } from '@core/utils/date.utils';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { map, take, race, forkJoin, Observable } from 'rxjs';
import { VirtualConsultationsFacadeService } from '../../virtual-consultations-facade.service';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { InProgressCallComponent } from '../in-progress-call/in-progress-call.component';
import { EntryCallStompService } from '../../../api-web-socket/entry-call-stomp.service';
import { RejectedCallComponent } from '@institucion/components/rejected-call/rejected-call.component';
import { toCallDetails } from '@institucion/components/entry-call-renderer/entry-call-renderer.component';
import { ContextService } from '@core/services/context.service';
import { Router } from '@angular/router';
import { Option, filter } from '@presentation/components/filters-select/filters-select.component';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { InstitutionService } from '@api-rest/services/institution.service';

@Component({
	selector: 'app-request-attention',
	templateUrl: './request-attention.component.html',
	styleUrls: ['./request-attention.component.scss']
})
export class RequestAttentionComponent implements OnInit {
	@Input() priorityOptions: Option[];
	@Input() availitibyOptions: Option[];
	virtualConsultations$: Observable<VirtualConsultation[]>;
	toggleEnabled = false;
	virtualConsultatiosStatus = status;
	initialProfessionalStatus = false;
	FINISHED_STATUS = EVirtualConsultationStatus.FINISHED;
	CANCELLED_STATUS = EVirtualConsultationStatus.CANCELED;

	filters: filter[] = [];
	careLinesOptions: CareLineDto[];
	specialitiesOptions: ClinicalSpecialtyDto[];
	institutionOptions: InstitutionBasicInfoDto[];
	constructor(
		private readonly virtualConsultationsFacadeService: VirtualConsultationsFacadeService,
		private virtualConsultationService: VirtualConstultationService,
		private jitsiCallService: JitsiCallService,
		private readonly dialog: MatDialog,
		private readonly callStatesService: EntryCallStompService,
		private contextService: ContextService, private router: Router,
		private careLineService: CareLineService,private clinicalSpecialtyService: ClinicalSpecialtyService,
		private institucionService : InstitutionService
	) { }


	ngOnInit(): void {
		this.getOptionsFilters();
		this.virtualConsultations$ = this.virtualConsultationsFacadeService.virtualConsultationsAttention$.pipe(map(requests =>
			requests.map(request => this.toVCToBeShown(request)
			)
		))

		this.virtualConsultationService.getProfessionalAvailability().subscribe(
			status => {
				this.initialProfessionalStatus = status;
				this.toggleEnabled = status;
			}
		)
	}

	getOptionsFilters() {
		this.careLineService.getDomainVirtualConsultationCareLines().subscribe(options => {
			this.careLinesOptions = options;
			this.clinicalSpecialtyService.getDomainVirtualConsultationClinicalSpecialties().subscribe(options=>{
				this.specialitiesOptions=options;
				this.institucionService.getVirtualConsultationInstitutions().subscribe(options=>{
					this.institutionOptions=options;
					this.prepareFilters();
				})
			})
		})
	}

	prepareFilters() {
		let filters = [];
		let filterCareLines: filter = {
			key: 'careLine',
			name: 'telemedicina.requests.form.CARELINE',
			options: this.careLinesOptions,
		}
		filters.push(filterCareLines);

		let filterSpecialities: filter = {
			key: 'speciality',
			name: 'telemedicina.requests.form.SPECIALTY',
			options: this.specialitiesOptions,
		}
		filters.push(filterSpecialities);

		let filterPriority: filter = {
			key: 'priority',
			name: 'telemedicina.requests.form.PRIORITY',
			options: this.priorityOptions,
		}
		filters.push(filterPriority);

		let filterInstitution: filter = {
			key: 'institution',
			name: 'telemedicina.requests.form.INSTITUTION',
			options: this.institutionOptions,
		}
		filters.push(filterInstitution);

		let filterAvailability: filter = {
			key: 'availability',
			name: 'telemedicina.requests.form.AVAILABILITY',
			options: this.availitibyOptions,
		}
		filters.push(filterAvailability);

		this.filters = filters;
	}

	searchRequest(searchCriteria: VirtualConsultationFilterDto) {
		this.virtualConsultationsFacadeService.setSearchCriteriaForAttention(searchCriteria);
	}

	prepareDtoFilter($event) {
		let newCriteria: VirtualConsultationFilterDto = {};
		newCriteria.availability = $event.availability.status ? null : $event.availability;
		newCriteria.careLineId = $event.careLine.status ? null : $event.careLine;
		newCriteria.clinicalSpecialtyId = $event.speciality.status ? null : $event.speciality;
		newCriteria.priorityId = $event.priority.status ? null : $event.priority;
		newCriteria.institutionId = $event.institution.status? null : $event.instution;
		this.searchRequest(newCriteria);
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
										this.jitsiCallService.open(virtualConsultation.callLink)
									}
								}
							)
						}
					)

					ref.afterClosed().subscribe(
						r => {
							if (!r) {
								this.virtualConsultationService.notifyVirtualConsultationCancelledCall(virtualConsultation.id).subscribe();
							}
						}
					)
				}
			)
	}


	availabilityChanged(availability: boolean) {
		this.virtualConsultationService.changeClinicalProfessionalAvailability(availability).subscribe();
		this.toggleEnabled = availability;
	}

	goToClinicalHistory(patientId: number) {
		const route = 'institucion/' + this.contextService.institutionId + '/ambulatoria/paciente/' + patientId;
		this.router.navigate([route]);
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

interface VirtualConsultation {
	availableProfessionalsAmount?: number;
	callLink?: string;
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
