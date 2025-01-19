import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DocumentElectronicSignatureProfessionalStatusDto, EElectronicSignatureStatus, ElectronicJointSignatureInvolvedDocumentListFilterDto, ElectronicSignatureInvolvedDocumentDto, GenericMasterDataDto, PageDto, RejectDocumentElectronicJointSignatureDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class JointSignatureService {

	private readonly BASE_URL = `${environment.apiBase}/institution/${this.contextService.institutionId}/electronic-joint-signature`;

	constructor(private readonly http: HttpClient,
		private readonly contextService: ContextService) { }

	getProfessionalInvolvedDocumentList(pageSize: number, pageNumber: number, filter?: ElectronicJointSignatureInvolvedDocumentListFilterDto): Observable<PageDto<ElectronicSignatureInvolvedDocumentDto>> {
		const url = `${this.BASE_URL}/get-involved-document-list`;
		let queryParam = new HttpParams().append('pageNumber', pageNumber).append('pageSize', pageSize);
		if (filter){
			queryParam = queryParam.append('filter', JSON.stringify(filter));
		}
		return this.http.get<PageDto<ElectronicSignatureInvolvedDocumentDto>>(url, { params: queryParam });
	}

	getDocumentElectronicSignatureProfessionalStatus(documentId: number): Observable<DocumentElectronicSignatureProfessionalStatusDto[]> {
		const URL =  `${environment.apiBase}/institution/${this.contextService.institutionId}/document/${documentId}/electronic-joint-signature/get-professionals-status`
		return this.http.get<DocumentElectronicSignatureProfessionalStatusDto[]>(URL);
	}

	rejectDocumentElectronicJointSignature(rejectReason: RejectDocumentElectronicJointSignatureDto): Observable<number> {
		const url = this.BASE_URL + `/reject`;
		return this.http.put<number>(url, rejectReason);
	}

	signDocumentElectronicJointSignature(documentIds: number[]) {
		const url = this.BASE_URL + `/sign`;
		return this.http.put<number>(url, documentIds);
	}

	getElectronicJointSignatureDocumentPossibleStatuses(): Observable<GenericMasterDataDto<EElectronicSignatureStatus>[]> {
		const url = `${environment.apiBase}/electronic-joint-signature/master-data/get-document-possible-statuses`;
		return this.http.get<GenericMasterDataDto<EElectronicSignatureStatus>[]>(url);
	}
}
