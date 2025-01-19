import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewTelemedicineRequestComponent } from '../../dialogs/new-telemedicine-request/new-telemedicine-request.component';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { ContextService } from '@core/services/context.service';
import { CareLineDto, ClinicalSpecialtyDto, ERole, EVirtualConsultationStatus, VirtualConsultationDto, VirtualConsultationFilterDto } from '@api-rest/api-model';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { timeDifference } from '@core/utils/date.utils';
import { statusLabel, mapPriority, status } from '../../virtualConsultations.utils';
import { Observable, map, of } from 'rxjs';
import { VirtualConsultationsFacadeService } from '../../virtual-consultations-facade.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { CareLineService } from '@api-rest/services/care-line.service';
import { Option, filter } from '@presentation/components/filters-select/filters-select.component';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { capitalize } from '@core/utils/core.utils';
import { TransferRequestComponent } from '../../dialogs/transfer-request/transfer-request.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { VirtualConsultationCustom } from '../request-info-card/request-info-card.component';

@Component({
	selector: 'app-requests',
	templateUrl: './requests.component.html',
	styleUrls: ['./requests.component.scss']
})
export class RequestsComponent implements OnInit {
	@Input() priorityOptions: Option[];
	@Input() availitibyOptions: Option[];
	@Input() virtualConsultationsFacadeService: VirtualConsultationsFacadeService;
	virtualConsultationsFiltered$: Observable<VirtualConsultationCustom[]>;
	virtualConsultationsBackUp$: Observable<VirtualConsultationCustom[]>;
	virtualConsultatiosStatus = status;
	initialResponsableStatus = false;
	careLinesOptions: CareLineDto[];
	specialitiesOptions: ClinicalSpecialtyDto[];
	professionalsOptions: Option[] = [];
	stateOptions: Option[] = [];
	filters: filter[] = [];
	statusFinished = EVirtualConsultationStatus.FINISHED;
	statusCanceled = EVirtualConsultationStatus.CANCELED;
	isVirtualConsultatitioProfessional: boolean;
	patientFilter: string;
	applySearchFilter = '';

	constructor(
		private dialog: MatDialog,
		private virtualConsultationService: VirtualConstultationService,
		private contextService: ContextService,
		private careLineService: CareLineService, private clinicalSpecialtyService: ClinicalSpecialtyService,
		private healthcareProfessionalByInstitucion: HealthcareProfessionalByInstitutionService,
		private readonly permissionsService: PermissionsService,
		private readonly snackBarService: SnackBarService,
	) {
	}

	ngOnInit(): void {
		this.setStateOptions();
		this.getOptionsFilters();
		this.virtualConsultationService.getResponsibleStatus(this.contextService.institutionId).subscribe(
			status => this.initialResponsableStatus = status
		)

		this.virtualConsultationsFiltered$ = this.virtualConsultationsFacadeService.virtualConsultationsRequest$.pipe(map(requests =>
			requests.map(request => this.toVCToBeShown(request)
			)
		))
		this.virtualConsultationsBackUp$ = this.virtualConsultationsFacadeService.virtualConsultationsRequest$.pipe(map(requests =>
			requests.map(request => this.toVCToBeShown(request)
			)
		))

		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.isVirtualConsultatitioProfessional = anyMatch<ERole>(userRoles, [ERole.VIRTUAL_CONSULTATION_PROFESSIONAL]);
		});
	}

	getOptionsFilters() {
		this.careLineService.getVirtualConsultationCareLinesByInstitutionId().subscribe(careLines => {
			this.careLinesOptions = careLines;
			this.clinicalSpecialtyService.getVirtualConsultationClinicalSpecialtiesByInstitutionId().subscribe(specialities => {
				this.specialitiesOptions = specialities;
				this.healthcareProfessionalByInstitucion.getVirtualConsultationHealthcareProfessionalsByInstitutionId().subscribe(professionals => {
					professionals.forEach(professional => {
						let option: Option = {
							id: professional.id,
							description: professional.firstName + ' ' + professional.lastName,
						}
						this.professionalsOptions.push(option);
					})
					this.prepareFilters();
				})
			})
		})
	}

	setStateOptions() {
		let state: Option = {
			id: EVirtualConsultationStatus.FINISHED,
			description: statusLabel[EVirtualConsultationStatus.FINISHED].description,
		}
		this.stateOptions.push(state);

		state = {
			id: EVirtualConsultationStatus.CANCELED,
			description: statusLabel[EVirtualConsultationStatus.CANCELED].description,
		}
		this.stateOptions.push(state);
		state = {
			id: EVirtualConsultationStatus.IN_PROGRESS,
			description: statusLabel[EVirtualConsultationStatus.IN_PROGRESS].description,
		}
		this.stateOptions.push(state);
		state = {
			id: EVirtualConsultationStatus.PENDING,
			description: statusLabel[EVirtualConsultationStatus.PENDING].description,
		}
		this.stateOptions.push(state);
	}

	private toVCToBeShown(vc: VirtualConsultationDto): VirtualConsultationCustom {
		return {
			...vc,
			institutionData: {name:null,id:vc.institutionData.id},
			statusLabel: statusLabel[vc.status],
			priorityLabel: mapPriority[vc.priority],
			waitingTime: timeDifference(dateTimeDtotoLocalDate(vc.creationDateTime)),
		}
	}

	searchRequest(searchCriteria: VirtualConsultationFilterDto) {
		this.virtualConsultationsFacadeService.setSearchCriteriaForRequest(searchCriteria);
	}

	prepareDtoFilter($event) {
		let newCriteria: VirtualConsultationFilterDto = {};
		newCriteria.availability = $event.availability;
		newCriteria.careLineId = $event.careLine;
		newCriteria.clinicalSpecialtyId = $event.speciality;
		newCriteria.priority = $event.priority;
		newCriteria.responsibleHealthcareProfessionalId = $event.professional;
		newCriteria.status = $event.state;
		this.searchRequest(newCriteria);
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

	transfer(virtualConsultation: VirtualConsultationDto) {
		const ref = this.dialog.open(TransferRequestComponent, {
			data: {
				virtualConsultation: virtualConsultation
			},
			width: '33%'
		});
		ref.afterClosed().subscribe(
			responsibleId => {
				if (responsibleId) {
					this.virtualConsultationService.transferResponsibleProfessionaltOfVirtualConsultation(virtualConsultation.id, responsibleId).subscribe(res => {
						if (res) {
							this.snackBarService.showSuccess('¡Solicitud transferida!');
						}
					})
				}
			}
		)
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

		let filterProfessionals: filter = {
			key: 'professional',
			name: 'telemedicina.requests.form.APPLICANT_PROFESSIONAL',
			options: this.professionalsOptions,
		}
		filters.push(filterProfessionals);

		let filterAvailability: filter = {
			key: 'availability',
			name: 'telemedicina.requests.form.AVAILABILITY',
			options: this.availitibyOptions,
		}
		filters.push(filterAvailability);

		let filterState: filter = {
			key: 'state',
			name: 'telemedicina.requests.form.STATE',
			options: this.stateOptions,
		}
		filters.push(filterState);

		this.filters = filters;
	}

	applyFilter($event: any): void {
		this.applySearchFilter = ($event.target as HTMLInputElement).value;
		this.applyFiltes();
	}

	private applyFiltes(): void {
		if (this.applySearchFilter.length) {
			this.virtualConsultationsFiltered$ = of(this.filter());
		} else {
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

