import { Injectable } from '@angular/core';
import { VirtualConsultationAvailableProfessionalAmountDto, VirtualConsultationDto, VirtualConsultationResponsibleProfessionalAvailabilityDto, VirtualConsultationStatusDataDto } from '@api-rest/api-model';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { Observable, ReplaySubject, map } from 'rxjs';
import { StompService } from '../../stomp.service';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class VirtualConsultationsFacadeService {

	virtualConsultationsRequestEmitter = new ReplaySubject<VirtualConsultationDto[]>();
	virtualConsultationsRequest$: Observable<VirtualConsultationDto[]> = this.virtualConsultationsRequestEmitter.asObservable();
	virtualConsultationsRequest: VirtualConsultationDto[];

	virtualConsultationsAttentionEmitter = new ReplaySubject<VirtualConsultationDto[]>();
	virtualConsultationsAttention$: Observable<VirtualConsultationDto[]> = this.virtualConsultationsAttentionEmitter.asObservable();
	virtualConsultationsAttention: VirtualConsultationDto[];

	private readonly virtualConsultationStatusChanged$: Observable<VirtualConsultationStatusDataDto> =
		this.stompService.watch('/topic/virtual-consultation-state-change')
			.pipe(map(m => JSON.parse(m.body)))

	private readonly solicitanteAvailableChanged$: Observable<any> =
		this.stompService.watch('/topic/virtual-consultation-responsible-state-change')
			.pipe(map(m => JSON.parse(m.body)))

	private readonly newRequest$: Observable<number> =
		this.stompService.watch('/topic/new-virtual-consultation')
			.pipe(map(m => JSON.parse(m.body)))

	private readonly professionalAvailableChanged$: Observable<any> =
		this.stompService.watch('/topic/virtual-consultation-professional-state-change')
			.pipe(map(m => JSON.parse(m.body)))

	constructor(
		private virtualConsultationService: VirtualConstultationService,
		private readonly stompService: StompService,
		private contextService: ContextService,
	) {

		this.virtualConsultationService.getDomainVirtualConsultation()
			.subscribe(vc => {
				this.virtualConsultationsAttention = vc;
				this.virtualConsultationsAttentionEmitter.next(vc)
			});

		this.solicitanteAvailableChanged$.subscribe(
			(availabilityChanged: VirtualConsultationResponsibleProfessionalAvailabilityDto) => {
				this.virtualConsultationsRequest
					.forEach(vc => {
						if (this.responsibleChangedFilter(vc, availabilityChanged)) {
							vc.responsibleData.available = availabilityChanged.available
						}
					})
				this.virtualConsultationsAttention
					.forEach(vc => {
						if (this.responsibleChangedFilter(vc, availabilityChanged)) {
							vc.responsibleData.available = availabilityChanged.available
						}
					})
				this.virtualConsultationsAttentionEmitter.next(this.virtualConsultationsRequest)
				this.virtualConsultationsAttentionEmitter.next(this.virtualConsultationsAttention);
			}
		)

		this.virtualConsultationService.getVirtualConsultationsByInstitution(this.contextService.institutionId).subscribe(vc => { //soli
			this.virtualConsultationsRequest = vc;
			this.virtualConsultationsRequestEmitter.next(vc);
		})

		this.professionalAvailableChanged$.subscribe(
			(availabilityChanged: VirtualConsultationAvailableProfessionalAmountDto[]) => {
				availabilityChanged.forEach(cv => {
					this.virtualConsultationsRequest.find(vc => vc.id === cv.virtualConsultationId).availableProfessionalsAmount = cv.professionalAmount;
				});
				this.virtualConsultationsRequestEmitter.next(this.virtualConsultationsRequest)
			}
		)

		this.virtualConsultationStatusChanged$.subscribe(
			(newState: VirtualConsultationStatusDataDto) => {
				const requesttoChange = this.virtualConsultationsRequest.find(vc => vc.id === newState.virtualConsultationId);
				if (requesttoChange) {
					requesttoChange.status = newState.status;
					this.virtualConsultationsRequestEmitter.next(this.virtualConsultationsRequest)
				}
				const attentionToChange = this.virtualConsultationsAttention.find(vc => vc.id === newState.virtualConsultationId);
				if (attentionToChange) {
					attentionToChange.status = newState.status;
					this.virtualConsultationsAttentionEmitter.next(this.virtualConsultationsAttention)
				}
			}
		)

		this.newRequest$.subscribe(
			(newVirtualConsultationId: number) => {
				this.virtualConsultationService.getVirtualConsultation(newVirtualConsultationId).subscribe(
					vc => {
						this.virtualConsultationsRequest = this.virtualConsultationsRequest.concat(vc);
						this.virtualConsultationsRequestEmitter.next(this.virtualConsultationsRequest);
						this.virtualConsultationsAttention = this.virtualConsultationsAttention.concat(vc);
						this.virtualConsultationsAttentionEmitter.next(this.virtualConsultationsAttention);
					}
				)
			}
		)

	}

	private responsibleChangedFilter(virtualConsultationDto: VirtualConsultationDto, solicitanteChanged: VirtualConsultationResponsibleProfessionalAvailabilityDto): boolean {
		return virtualConsultationDto.responsibleData.healthcareProfessionalId === solicitanteChanged.healthcareProfessionalId
			&& virtualConsultationDto.institutionData.id === solicitanteChanged.institutionId
	}
}
