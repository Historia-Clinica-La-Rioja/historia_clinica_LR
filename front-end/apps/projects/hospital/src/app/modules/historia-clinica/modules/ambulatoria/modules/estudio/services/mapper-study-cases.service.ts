import { Injectable } from '@angular/core';
import { DiagnosticReportInfoDto, StudyOrderReportInfoDto, StudyWithoutOrderReportInfoDto, TranscribedOrderReportInfoDto } from '@api-rest/api-model';
import { CATEGORY_IMAGE, DiagnosticWithTypeReportInfoDto, E_TYPE_ORDER, InfoNewTypeOrderDto } from '../model/ImageModel';
import { STUDY_STATUS } from '@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata';

@Injectable({
  providedIn: 'root'
})
export class MapperStudyCasesService {

constructor() { }

	mapToDiagnosticWithTypeReportInfoDto(diagnosticsReport: DiagnosticReportInfoDto[], typeOrderSlected: E_TYPE_ORDER ): DiagnosticWithTypeReportInfoDto[] {
		return diagnosticsReport.map(diagnostic => {return {...diagnostic,typeOrder: typeOrderSlected, infoOrderInstances: null}})
	}

	mapDiagnosticTranscriptaToDiagnosticWithTypeReportInfoDto(diagnosticsReport: TranscribedOrderReportInfoDto[] ): DiagnosticWithTypeReportInfoDto[] {
		return diagnosticsReport.map(transcripta =>
			{
			return {
			category: CATEGORY_IMAGE ,
			creationDate: null,
			doctor: null,
			healthCondition: null,
			id: null,
			observations: null,
			serviceRequestId: null,
			snomed: {id: null, sctid: null, pt: transcripta.snomed},
			source: null,
			sourceId: null,
			statusId: transcripta.status ? STUDY_STATUS.FINAL.id : STUDY_STATUS.REGISTERED.id,
			typeOrder: E_TYPE_ORDER.TRANSCRIPTA,
			infoOrderInstances: this.mapToInfoNewTypeOrderDto(transcripta)
	}})
	}


	mapDiagnosticSinOrdenToDiagnosticWithTypeReportInfoDto(diagnosticsReport: StudyWithoutOrderReportInfoDto[]): DiagnosticWithTypeReportInfoDto[] {
		return diagnosticsReport.map(sinOrden =>
			{
			return {
			category: CATEGORY_IMAGE ,
			creationDate: null,
			doctor: null,
			healthCondition: null,
			id: null,
			observations: null,
			serviceRequestId: null,
			snomed: {id: null, sctid: null, pt: sinOrden.snomed},
			source: null,
			sourceId: null,
			statusId: sinOrden.status ? STUDY_STATUS.FINAL.id : STUDY_STATUS.REGISTERED.id,
			typeOrder: E_TYPE_ORDER.SIN_ORDEN,
			infoOrderInstances: this.mapToInfoNewTypeOrderDto(sinOrden)
	}})
	}


	mapToInfoNewTypeOrderDto(source: TranscribedOrderReportInfoDto | StudyWithoutOrderReportInfoDto ): InfoNewTypeOrderDto {
		return {
			imageId: source.imageId,
			hceDocumentDataDto: source.hceDocumentDataDto,
			status: source.status,
			seeStudy: source.seeStudy,
			viewReport: source.viewReport,
		}
	}

	mapStudyOrderToDiagnosticWithTypeReportInfoDto(diagnosticsReport: StudyOrderReportInfoDto[]): DiagnosticWithTypeReportInfoDto[]{
		return diagnosticsReport.map(studyOrder =>
			{
			return {
			category: CATEGORY_IMAGE ,
			creationDate: new Date(studyOrder.creationDate),
			doctor: studyOrder.doctor,
			healthCondition: {id: null , snomed:{sctid: null , pt:studyOrder.healthCondition}},
			id: null,
			observations: null,
			serviceRequestId: null,
			snomed: {id: null, sctid: null, pt: studyOrder.snomed},
			source: studyOrder.source,
			sourceId: null,
			statusId: studyOrder.status ? STUDY_STATUS.FINAL.id : STUDY_STATUS.REGISTERED.id,
			typeOrder: E_TYPE_ORDER.COMPLETA,
			infoOrderInstances: this.mapToInfoNewTypeOrderDto(studyOrder)
	}})
	}

}
