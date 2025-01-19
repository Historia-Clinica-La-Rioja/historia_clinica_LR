import { ERole, VirtualConsultationAvailableProfessionalAmountDto, VirtualConsultationDto, VirtualConsultationFilterDto, VirtualConsultationResponsibleProfessionalAvailabilityDto, VirtualConsultationStatusDataDto } from '@api-rest/api-model';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { Observable, ReplaySubject} from 'rxjs';
import { ContextService } from '@core/services/context.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { Injectable } from '@angular/core';
import { VirtualConsultationStompService } from '../api-web-socket/virtual-consultation-stomp.service';

@Injectable()
export class VirtualConsultationsFacadeService {

	virtualConsultationsRequestEmitter = new ReplaySubject<VirtualConsultationDto[]>();
	virtualConsultationsRequest$: Observable<VirtualConsultationDto[]> = this.virtualConsultationsRequestEmitter.asObservable();
	virtualConsultationsRequest: VirtualConsultationDto[];

	virtualConsultationsAttentionEmitter = new ReplaySubject<VirtualConsultationDto[]>();
	virtualConsultationsAttention$: Observable<VirtualConsultationDto[]> = this.virtualConsultationsAttentionEmitter.asObservable();
	virtualConsultationsAttention: VirtualConsultationDto[];

	isVirtualConsultatitioResponsible: boolean = false;

	constructor(
		private virtualConsultationService: VirtualConstultationService,
		private contextService: ContextService,
		private readonly permissionsService: PermissionsService,
		private readonly virtualConsultationStompService: VirtualConsultationStompService,
	) {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.isVirtualConsultatitioResponsible = anyMatch<ERole>(userRoles, [ERole.VIRTUAL_CONSULTATION_RESPONSIBLE]);
		});

		const filterCriteria: VirtualConsultationFilterDto = {
			availability: null,
			careLineId: null,
			clinicalSpecialtyId: null,
			institutionId: null,
			priority: null,
			responsibleHealthcareProfessionalId: null,
			status: null,
		};
			if (!this.isVirtualConsultatitioResponsible) {
				this.getDomainVirtualConsultation(filterCriteria);
		}

		this.getVirtualConsultationByInstitution(filterCriteria);

		this.virtualConsultationStompService.solicitanteAvailableChanged$.subscribe(
			(availabilityChanged: VirtualConsultationResponsibleProfessionalAvailabilityDto) => {
				this.virtualConsultationsRequest
					?.forEach(vc => {
						if (this.responsibleChangedFilter(vc, availabilityChanged)) {
							vc.responsibleData.available = availabilityChanged.available
						}
					})
				this.virtualConsultationsAttention
					?.forEach(vc => {
						if (this.responsibleChangedFilter(vc, availabilityChanged)) {
							vc.responsibleData.available = availabilityChanged.available
						}
					})
				this.virtualConsultationsAttentionEmitter.next(this.virtualConsultationsRequest)
				this.virtualConsultationsAttentionEmitter.next(this.virtualConsultationsAttention);
			}
		)

		this.virtualConsultationStompService.responsibleProfessionalChanged$.subscribe((responsibleProfessional : any)=>{
			this.virtualConsultationService.getResponsibleProfessional(responsibleProfessional.healthcareProfessionalId,this.contextService.institutionId).subscribe(res=>{
				if (!this.isVirtualConsultatitioResponsible) {
					const attentionToChange = this.virtualConsultationsAttention.find(vc => vc.id === responsibleProfessional.virtualConsultationId);
					attentionToChange.responsibleData = res;
					this.virtualConsultationsAttentionEmitter.next(this.virtualConsultationsAttention)
				}
				const requesttoChange = this.virtualConsultationsRequest.find(vc => vc.id === responsibleProfessional.virtualConsultationId);
				requesttoChange.responsibleData = res;
				this.virtualConsultationsRequestEmitter.next(this.virtualConsultationsRequest)	
			})
		})

		this.virtualConsultationStompService.professionalAvailableChanged$.subscribe(
			(availabilityChanged: VirtualConsultationAvailableProfessionalAmountDto[]) => {
				availabilityChanged.forEach(cv => {
					const virtualConsultation = this.virtualConsultationsRequest.find(vc => vc.id === cv.virtualConsultationId);
					if (virtualConsultation)
						virtualConsultation.availableProfessionalsAmount = cv.professionalAmount;
				});
				this.virtualConsultationsRequestEmitter.next(this.virtualConsultationsRequest)
			}
		)

		this.virtualConsultationStompService.virtualConsultationStatusChanged$.subscribe(
			(newState: VirtualConsultationStatusDataDto) => {
				if (!this.isVirtualConsultatitioResponsible) {
					const attentionToChange = this.virtualConsultationsAttention.find(vc => vc.id === newState.virtualConsultationId);
					if (attentionToChange) {
						attentionToChange.status = newState.status;
						this.virtualConsultationsAttentionEmitter.next(this.virtualConsultationsAttention)
					}
				}
				const requesttoChange = this.virtualConsultationsRequest.find(vc => vc.id === newState.virtualConsultationId);
				if (requesttoChange) {
					requesttoChange.status = newState.status;
					this.virtualConsultationsRequestEmitter.next(this.virtualConsultationsRequest)
				}

			}
		)

		this.virtualConsultationStompService.newRequest$.subscribe(
			(newVirtualConsultationId: number) => {
				this.virtualConsultationService.getVirtualConsultation(newVirtualConsultationId).subscribe(
					vc => {
						this.virtualConsultationsRequest = this.virtualConsultationsRequest.concat(vc);
						this.virtualConsultationsRequestEmitter.next(this.virtualConsultationsRequest);
						this.virtualConsultationsAttention = this.virtualConsultationsAttention?.concat(vc);
						this.virtualConsultationsAttentionEmitter.next(this.virtualConsultationsAttention);
					}
				)
			}
		)

	}

	setSearchCriteriaForRequest(searchCriteria: VirtualConsultationFilterDto) {
		this.getVirtualConsultationByInstitution(searchCriteria);
	}

	private getVirtualConsultationByInstitution(searchCriteria?: VirtualConsultationFilterDto) {
		this.virtualConsultationService.getVirtualConsultationsByInstitution(this.contextService.institutionId, searchCriteria).subscribe(vc => { //soli
			this.virtualConsultationsRequest = vc;
			this.virtualConsultationsRequestEmitter.next(vc);
		})
	}

	setSearchCriteriaForAttention(searchCriteria: VirtualConsultationFilterDto) {
		this.getDomainVirtualConsultation(searchCriteria);
	}

	private getDomainVirtualConsultation(searchCriteria?: VirtualConsultationFilterDto) {
		this.virtualConsultationService.getDomainVirtualConsultation(this.contextService.institutionId, searchCriteria).subscribe(vc => {
			this.virtualConsultationsAttention = vc;
			this.virtualConsultationsAttentionEmitter.next(vc)
		});
	}

	private responsibleChangedFilter(virtualConsultationDto: VirtualConsultationDto, solicitanteChanged: VirtualConsultationResponsibleProfessionalAvailabilityDto): boolean {
		return virtualConsultationDto.responsibleData.healthcareProfessionalId === solicitanteChanged.healthcareProfessionalId
			&& virtualConsultationDto.institutionData.id === solicitanteChanged.institutionId
	}
}
