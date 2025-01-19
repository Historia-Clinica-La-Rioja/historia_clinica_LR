import { Injectable } from '@angular/core';
import { StompService } from '../../stomp.service';
import { Observable, map } from 'rxjs';
import { VirtualConsultationStatusDataDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class VirtualConsultationStompService {


	virtualConsultationStatusChanged$: Observable<VirtualConsultationStatusDataDto> =
		this.stompService.watch('/topic/virtual-consultation-state-change')
			.pipe(map(m => JSON.parse(m.body)))

	solicitanteAvailableChanged$: Observable<any> =
		this.stompService.watch('/topic/virtual-consultation-responsible-state-change')
			.pipe(map(m => JSON.parse(m.body)))

	newRequest$: Observable<number> =
		this.stompService.watch('/topic/new-virtual-consultation')
			.pipe(map(m => JSON.parse(m.body)))

	professionalAvailableChanged$: Observable<any> =
		this.stompService.watch('/topic/virtual-consultation-professional-state-change')
			.pipe(map(m => JSON.parse(m.body)))

	responsibleProfessionalChanged$: Observable<any> =
		this.stompService.watch('/topic/virtual-consultation-responsible-professional-change')
			.pipe(map(m => JSON.parse(m.body)))

	constructor(
		private readonly stompService: StompService,
	) { }
}
