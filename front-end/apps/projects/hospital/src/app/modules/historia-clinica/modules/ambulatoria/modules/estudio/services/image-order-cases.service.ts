import { Injectable } from '@angular/core';
import { ServiceRequestService } from '@api-rest/services/service-request.service';
import { MapperStudyCasesService } from './mapper-study-cases.service';
import { Observable, forkJoin, map } from 'rxjs';
import { DiagnosticWithTypeReportInfoDto, E_TYPE_ORDER } from '../model/ImageModel';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { FilterImageOrderStudyService, StudyOrderFilters } from './filter-image-order-study.service';

@Injectable({
	providedIn: 'root'
})
export class ImageOrderCasesService {

	constructor(
		private readonly serviceRequestService: ServiceRequestService,
		private readonly mapperStudyCasesService: MapperStudyCasesService,
		private readonly filterImageOrderStudyService: FilterImageOrderStudyService,
		) { }


	getImageOrderCasesFiltered(patientId: number, filterValues: StudyOrderFilters): Observable<DiagnosticWithTypeReportInfoDto[]> {
		const sinOrden$: Observable<DiagnosticWithTypeReportInfoDto[]> = this.serviceRequestService.getStudyWithoutOrder(patientId)
			.pipe(map(diagnostics => this.mapperStudyCasesService.mapDiagnosticSinOrdenToDiagnosticWithTypeReportInfoDto(diagnostics)))
		const transcriptaOrden$: Observable<DiagnosticWithTypeReportInfoDto[]> = this.serviceRequestService.getStudyTranscribedOrder(patientId)
			.pipe(map(diagnostics => this.mapperStudyCasesService.mapDiagnosticTranscriptaToDiagnosticWithTypeReportInfoDto(diagnostics)))
		return forkJoin([sinOrden$, transcriptaOrden$])
			.pipe(map(streamDiagnostics => {
				const sinOrden: DiagnosticWithTypeReportInfoDto[] = streamDiagnostics[0]
				const transcriptaOrden: DiagnosticWithTypeReportInfoDto[] = streamDiagnostics[1]
				return this.applyFilterToOrderImageCases(sinOrden.concat(transcriptaOrden), filterValues)
			}))
	}

	getImagesDiagnosticAllCases(imageDiagnostics: DiagnosticReportInfoDto[], imageOrderCases: DiagnosticWithTypeReportInfoDto[]): DiagnosticWithTypeReportInfoDto[] {
		const imageDiagnosticToDiagnosticWithType = this.mapperStudyCasesService.mapToDiagnosticWithTypeReportInfoDto(imageDiagnostics, E_TYPE_ORDER.COMPLETA)
		return imageDiagnosticToDiagnosticWithType.concat(imageOrderCases)
	}

	applyFilterToOrderImageCases(diagnotics:DiagnosticWithTypeReportInfoDto[], filterValues: StudyOrderFilters ): DiagnosticWithTypeReportInfoDto[] {
		return diagnotics.filter(diagnostic => this.filterImageOrderStudyService.applyFilterStudyImageOrders(diagnostic,filterValues ))
	}

}