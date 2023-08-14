import { Injectable } from '@angular/core';
import { VirtualConsultationDto, VirtualConsultationResponsibleProfessionalAvailabilityDto, VirtualConsultationStatusDataDto } from '@api-rest/api-model';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { Observable, ReplaySubject, map } from 'rxjs';
import { StompService } from '../../stomp.service';

@Injectable({
	providedIn: 'root'
})
export class VirtualConsultationsFacadeService {

	virtualConsultationsEmitter = new ReplaySubject<VirtualConsultationDto[]>();
	virtualConsultations$: Observable<VirtualConsultationDto[]> = this.virtualConsultationsEmitter.asObservable();

	virtualConsultations: VirtualConsultationDto[];

	private readonly virtualConsultationStatusChanged$: Observable<VirtualConsultationStatusDataDto> =
		this.stompService.watch('/topic/virtual-consultation-state-change')
			.pipe(map(m => JSON.parse(m.body)))

	private readonly solicitanteAvailableChanged$: Observable<any> =
		this.stompService.watch('/topic/virtual-consultation-responsible-state-change')
			.pipe(map(m => JSON.parse(m.body)))

	private readonly newRequest$: Observable<number> =
		this.stompService.watch('/topic/new-virtual-consultation')
			.pipe(map(m => JSON.parse(m.body)))

	constructor(
		private virtualConsultationService: VirtualConstultationService,
		private readonly stompService: StompService
	) {

		this.virtualConsultationService.getDomainVirtualConsultation()
			.subscribe(vc => {
				this.virtualConsultations = vc;
				this.virtualConsultationsEmitter.next(vc)
			});

		this.solicitanteAvailableChanged$.subscribe(
			(availabilityChanged: VirtualConsultationResponsibleProfessionalAvailabilityDto) => {
				this.virtualConsultations
					.forEach(vc => {
						if (this.responsibleChangedFilter(vc, availabilityChanged)) {
							vc.responsibleData.available = availabilityChanged.available
						}
					})
				this.virtualConsultationsEmitter.next(this.virtualConsultations)
			}
		)


		this.virtualConsultationStatusChanged$.subscribe(
			(newState: VirtualConsultationStatusDataDto) => {
				this.virtualConsultations.find(vc => vc.id === newState.virtualConsultationId).status = newState.status;
				this.virtualConsultationsEmitter.next(this.virtualConsultations)
			}
		)

		this.newRequest$.subscribe(
			(newVirtualConsultationId: number) => {
				this.virtualConsultationService.getVirtualConsultation(newVirtualConsultationId).subscribe(
					vc => {
						this.virtualConsultations = this.virtualConsultations.concat(vc);
						this.virtualConsultationsEmitter.next(this.virtualConsultations)
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
