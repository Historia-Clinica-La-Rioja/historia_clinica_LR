import { Component, Input, OnInit } from '@angular/core';
import { CareLineDto, ClinicalSpecialtyDto, DateTimeDto, EVirtualConsultationPriority, EVirtualConsultationStatus, InstitutionBasicInfoDto, VirtualConsultationDto, VirtualConsultationFilterDto, VirtualConsultationInstitutionDataDto, VirtualConsultationPatientDataDto, VirtualConsultationResponsibleDataDto } from '@api-rest/api-model';
import { mapPriority, statusLabel, status } from '../../virtualConsultations.utils';
import { timeDifference } from '@core/utils/date.utils';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { map, race, forkJoin, Observable, take, Subscription, of } from 'rxjs';
import { VirtualConsultationsFacadeService } from '../../virtual-consultations-facade.service';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { JitsiCallService } from '../../../jitsi/jitsi-call.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { InProgressCallComponent } from '../in-progress-call/in-progress-call.component';
import { EntryCallStompService } from '../../../api-web-socket/entry-call-stomp.service';
import { RejectedCallComponent } from '@institucion/components/rejected-call/rejected-call.component';
import { toCallDetails } from 'projects/hospital/src/app/modules/telemedicina/components/entry-call-renderer/entry-call-renderer.component';
import { ContextService } from '@core/services/context.service';
import { Router } from '@angular/router';
import { Option, filter } from '@presentation/components/filters-select/filters-select.component';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { capitalize } from '@core/utils/core.utils';
import { VirtualConsultationCustom } from '../request-info-card/request-info-card.component';

@Component({
	selector: 'app-request-attention',
	templateUrl: './request-attention.component.html',
	styleUrls: ['./request-attention.component.scss']
})
export class RequestAttentionComponent implements OnInit {
	@Input() priorityOptions: Option[];
	@Input() availitibyOptions: Option[];
	@Input() virtualConsultationsFacadeService: VirtualConsultationsFacadeService;
	virtualConsultationsFiltered$: Observable<VirtualConsultationCustom[]>;
	virtualConsultationsBackUp$: Observable<VirtualConsultationCustom[]>;
	toggleEnabled = false;
	virtualConsultatiosStatus = status;
	initialProfessionalStatus = false;
	FINISHED_STATUS = EVirtualConsultationStatus.FINISHED;
	CANCELLED_STATUS = EVirtualConsultationStatus.CANCELED;

	filters: filter[] = [];
	careLinesOptions: CareLineDto[];
	specialitiesOptions: ClinicalSpecialtyDto[];
	institutionOptions: InstitutionBasicInfoDto[];
	patientFilter:string;
	applySearchFilter = '';

	constructor(
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

		this.virtualConsultationsFiltered$ = this.virtualConsultationsFacadeService.virtualConsultationsAttention$.pipe(map(requests =>
			requests.map(request => this.toVCToBeShown(request)
			)
		))

		this.virtualConsultationsBackUp$ = this.virtualConsultationsFacadeService.virtualConsultationsAttention$.pipe(map(requests =>
			requests.map(request =>   this.toVCToBeShown(request)
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
		this.careLineService.getCareLinesAttachedToInstitution(this.contextService.institutionId).subscribe(options => {
			this.careLinesOptions = options;
			this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(options=>{
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
		newCriteria.availability = $event.availability;
		newCriteria.careLineId = $event.careLine;
		newCriteria.clinicalSpecialtyId = $event.speciality;
		newCriteria.priority = $event.priority;
		newCriteria.institutionId = $event.institution;
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
					let subscription: Subscription;
					ref.afterOpened().subscribe(
						_ => {

							const rejected$ = this.callStatesService.rejectedCall$.pipe(map(r => { return { ...r, origin: 'rejected' } }))
							const accepted$ = this.callStatesService.acceptedCall$.pipe(map(r => { return { ...r, origin: 'accepted' } }))

							subscription = race(rejected$, accepted$).pipe(take(1)).subscribe(
								(vc) => {
									ref.close();
									if (vc.origin === 'rejected') {
										const data = toCallDetails(vc);
										this.dialog.open(RejectedCallComponent, { data });
									} else {
										this.jitsiCallService.open(vc.callLink)
										this.goToClinicalHistory(vc.patientData.id);
									}
								}
							)
						}
					)

					ref.afterClosed().subscribe(
						r => {
							subscription.unsubscribe();
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

	private toVCToBeShown(vc: VirtualConsultationDto) : VirtualConsultationCustom {
		return {
			...vc,
			responsibleData: {available:vc.responsibleData.available,firstName:null,healthcareProfessionalId:vc.responsibleData.healthcareProfessionalId,lastName:null},
			statusLabel: statusLabel[vc.status],
			priorityLabel: mapPriority[vc.priority],
			waitingTime: timeDifference(dateTimeDtotoLocalDate(vc.creationDateTime))
		}
	}

	applyFilter($event: any): void {
		this.applySearchFilter = ($event.target as HTMLInputElement).value;
		this.applyFiltes();
	}

	private applyFiltes(): void {
		if (this.applySearchFilter.length) {
		this.virtualConsultationsFiltered$= of(this.filter());
		}else{
			this.virtualConsultationsFiltered$ = this.virtualConsultationsBackUp$;
		}
	}

	private filter(): VirtualConsultationCustom[] {
		let listFilter =[];
		 this.virtualConsultationsBackUp$.subscribe(data=>{
			listFilter = data;
		});
			return listFilter.filter((e: VirtualConsultationDto) => this.getFullName(e).toLowerCase().includes(this.applySearchFilter.toLowerCase()))
	}

	getFullName(patient: VirtualConsultationDto): string {
		const names = [
			patient?.patientData.name,
			patient?.patientData.lastName,
		].filter(name => name !== undefined && name.trim() !== '');

		const capitalizedNames = names.map(name => capitalize(name));
		return capitalizedNames.join(' ');
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
