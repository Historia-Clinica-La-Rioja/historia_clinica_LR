import { Injectable } from '@angular/core';
import {
	AppointmentDailyAmountDto,
	AppointmentDto,
	AppointmentListDto,
	AppointmentOrderDetailImageDto,
	AppointmentShortSummaryDto,
	AssignedAppointmentDto,
	BookedAppointmentDto,
	BookingDto,
	CreateAppointmentDto,
	DetailsOrderImageDto,
	EquipmentAppointmentListDto,
	CreateCustomAppointmentDto,
	CustomRecurringAppointmentDto,
	ExternalPatientCoverageDto,
	HierarchicalUnitDto,
	InstitutionBasicInfoDto,
	PatientAppointmentHistoryDto,
	SavedBookingAppointmentDto,
	StudyIntanceUIDDto,
	UpdateAppointmentDateDto,
	UpdateAppointmentDto,
	RecurringTypeDto,
	WeekDayDto,
	UpdateAppointmentStateDto,
} from '@api-rest/api-model';
import { Observable, map, of } from 'rxjs';

import { HttpClient, HttpParams } from '@angular/common/http';

import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { environment } from '@environments/environment';
import { toFileFormat } from '@api-rest/mapper/date.mapper';
import { fixDate } from '@core/utils/date/format';
import { dateISOParseDate } from '@core/utils/moment.utils';

@Injectable({
	providedIn: 'root'
})
export class AppointmentsService {

	private BASE_URL: string;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
		private downloadService: DownloadService
	) {
		this.contextService.institutionId$.subscribe(institutionId =>
			this.BASE_URL = `${environment.apiBase}/institutions/${institutionId}/medicalConsultations/appointments`
		)
	}


	create(appointment: CreateAppointmentDto): Observable<number> {
		return this.http.post<number>(this.BASE_URL, appointment);
	}

	createAppointmentEquipment(appointment: CreateAppointmentDto, orderId?: number, studyId?: number): Observable<number> {
		const url = `${this.BASE_URL}/equipment`;

		let queryParams: HttpParams = new HttpParams();
		if (orderId) queryParams = queryParams.append('orderId', JSON.stringify(orderId));
		if (studyId) queryParams = queryParams.append('studyId', JSON.stringify(studyId));

		return this.http.post<number>(url, appointment, { params: queryParams });
	}

	createAppointmentEquipmentWithTranscribedOrder(appointment: CreateAppointmentDto, transcribedOrderId?: number): Observable<number> {
		const url = `${this.BASE_URL}/transcribedEquipment`;

		let queryParams: HttpParams = new HttpParams();
		if (transcribedOrderId) queryParams = queryParams.append('transcribedOrderId', JSON.stringify(transcribedOrderId));

		return this.http.post<number>(url, appointment, { params: queryParams });
	}

	getList(diaryIds: number[], healthcareProfessionalId: number, from: string, to: string): Observable<AppointmentListDto[]> {
		const url = this.BASE_URL + `/list/${healthcareProfessionalId}`;
		// Se filtra porque pueden llegar diaryIds como undefined
		diaryIds = diaryIds.filter(d => d !== undefined);
		if (!diaryIds || diaryIds.length === 0 || !healthcareProfessionalId) {
			return of([]);
		}

		return this.http.get<AppointmentListDto[]>(url, {
			params: {
				diaryIds: `${diaryIds.join(',')}`,
				from,
				to
			}
		});
	}

	getEquipmentList(diaryId: number, from: string, to: string): Observable<AppointmentListDto[]> {
		const url = this.BASE_URL + `/list-appoiments-equipment/${diaryId}`;
		if (!diaryId) {
			return of([]);
		}

		return this.http.get<AppointmentListDto[]>(url, {
			params: {
				from,
				to
			}
		});
	}

	changeState(appointmentId: number, appointmentStateId: number, reason?: string,patientInformationScan?: string): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/${appointmentId}/change-state`;
		const updateAppointment: UpdateAppointmentStateDto = {
			reason: reason,
			patientIdentificationBarCode:patientInformationScan,
		}
		return this.http.put<boolean>(url,updateAppointment , { params: this.getChangeStateParams(appointmentStateId) });
	}

	changeStateAppointmentEquipment(appointmentId: number, appointmentStateId: number, reason?: string): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/${appointmentId}/equipment-change-state`;
		return this.http.put<boolean>(url, {}, { params: this.getChangeStateParams(appointmentStateId, reason) });
	}

	private getChangeStateParams(appointmentStateId: number, reason?: string): HttpParams{
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('appointmentStateId', JSON.stringify(appointmentStateId));

		if (reason) {
			queryParams = queryParams.append('reason', reason);
		}
		return queryParams
	}

	updateAppointment(appointment: UpdateAppointmentDto) {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/update`;
		return this.http.post<number>(url, appointment);
	}

	updateAppointmentMedicalOrder(appointmentId: number, orderId: number, studyId: number, transcribed: boolean): Observable<boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = orderId ? queryParams.append('orderId', JSON.stringify(orderId)) : queryParams;
		queryParams = studyId ? queryParams.append('studyId', JSON.stringify(studyId)) : queryParams;
		queryParams = transcribed != undefined ? queryParams.append('transcribed', transcribed) : queryParams.append('transcribed', false);
		const url = `${this.BASE_URL}/${appointmentId}/update-orderId`;
		return this.http.put<boolean>(url, {}, { params: queryParams });
	}

	get(appoinmentId: number): Observable<AppointmentDto> {
		const url = `${this.BASE_URL}/${appoinmentId}`;
		return this.http.get<AppointmentDto>(url);
	}

	getAppointmentEquipment(appoinmentId: number): Observable<AppointmentDto> {
		const url = `${this.BASE_URL}/equipmentAppointment/${appoinmentId}`;
		return this.http.get<AppointmentDto>(url);
	}

	getAppointmentsByEquipment(equipmentId: number, from?: string, to?: string): Observable<EquipmentAppointmentListDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = from ? queryParams.append('from', from) : queryParams;
		queryParams = to ? queryParams.append('to', to) : queryParams;

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/list-appoiments-by-equipment/${equipmentId}`;
		return this.http.get<EquipmentAppointmentListDto[]>(url, { params: queryParams })
	}

	deriveReport(appointmentId: number, destInstitutionId: number): Observable<boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('destInstitutionId', JSON.stringify(destInstitutionId));

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/${appointmentId}/derive-report`;
		return this.http.put<boolean>(url, queryParams)
	}

	requireReport(appointmentId: number): Observable<boolean> {
		const url = `${this.BASE_URL}/${appointmentId}/require-report`;
		return this.http.post<boolean>(url, {})
	}

	appointmentCanBeDerived(appointmentId: number): Observable<InstitutionBasicInfoDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/check-derived-status/${appointmentId}`;
		return this.http.get<InstitutionBasicInfoDto>(url)
	}

	hasNewConsultationEnabled(patientId: number): Observable<boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('patientId', JSON.stringify(patientId));

		const url = `${this.BASE_URL}/current-appointment`;
		return this.http.get<boolean>(url, { params: queryParams });
	}

	considerAppointments(): Observable<boolean> {
		const url = `${this.BASE_URL}/consider-appointment`;
		return this.http.get<boolean>(url);
	}

	addStudyObservations(appoinmentId: number, observations: DetailsOrderImageDto): Observable<boolean> {
		const url = `${this.BASE_URL}/study-observations/${appoinmentId}`;
		return this.http.post<boolean>(url, observations);
	}

	updatePhoneNumber(appointmentId: number, phonePrefix: string, phoneNumber: string): Observable<boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = (phoneNumber) ? queryParams.append('phoneNumber', phoneNumber).append('phonePrefix', phonePrefix) : queryParams;

		const url = `${this.BASE_URL}/${appointmentId}/update-phone-number`;
		return this.http.put<boolean>(url, {}, { params: queryParams });
	}

	updateMedicalCoverage(appointmentId: number, patientMedicalCoverageId: number): Observable<Boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = (patientMedicalCoverageId) ? queryParams.append('patientMedicalCoverageId', JSON.stringify(patientMedicalCoverageId)) : queryParams;

		const url = `${this.BASE_URL}/${appointmentId}/update-medical-coverage`;
		return this.http.put<boolean>(url, {}, { params: queryParams });
	}

	updateObservation(appointmentId: number, observation: string): Observable<boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = (observation) ? queryParams.append('observation', observation) : queryParams;

		const url = `${this.BASE_URL}/${appointmentId}/update-observation`;
		return this.http.put<boolean>(url, {}, { params: queryParams });
	}

	updateDate(updateAppointmentDate: UpdateAppointmentDateDto): Observable<boolean> {
		const url = `${this.BASE_URL}/${updateAppointmentDate.appointmentId}/update-date`;
		return this.http.put<boolean>(url, updateAppointmentDate);
	}

	mqttCall(appointmentId: number): Observable<void> {
		const url = `${this.BASE_URL}/${appointmentId}/notifyPatient`;
		return this.http.post<void>(url, {});
	}

	getDailyAmounts(diaryId: number, from: string, to: string): Observable<AppointmentDailyAmountDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = (diaryId) ? queryParams.append('diaryId', JSON.stringify(diaryId)) : queryParams;
		queryParams = queryParams.append("from", from);
		queryParams = queryParams.append("to", to);

		const url = `${this.BASE_URL}/getDailyAmounts`;
		return this.http.get<AppointmentDailyAmountDto[]>(url,
			{
				params: queryParams
			});
	}

	getAnexoPdf(appointmentData: any): Observable<any> {
		const pdfName = 'AnexoII';
		const url = `${environment.apiBase}/reports/${this.contextService.institutionId}/appointment-annex`;
		return this.getAppointmentReport(url, appointmentData, pdfName);
	}

	getFormPdf(appointmentData: any): Observable<any> {
		const pdfName = 'FormularioV';
		const url = `${environment.apiBase}/reports/${this.contextService.institutionId}/appointment-formv`;
		return this.getAppointmentReport(url, appointmentData, pdfName);
	}

	getAppointmentTicketPdf(appointmentId: number): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointment-ticket-report/${appointmentId}`;
		const responseType = 'arraybuffer' as 'json';
		return this.http.get<any>(url, { responseType });
	}

	getAppointmentImageTicketPdf(appointmentId: number, isTranscribed: boolean): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointment-ticket-report/${appointmentId}/image/transcribed-order/${isTranscribed}`;
		const responseType = 'arraybuffer' as 'json';
		return this.http.get<any>(url, { responseType });
	}

	getAppointmentReport(url: string, appointmentData: any, pdfName: string): Observable<any> {
		const date = fixDate(appointmentData.date);
		const appointmentId: number = appointmentData.appointmentId;
		const fullNamePatient: string = appointmentData.patient.fullName.replace(' ', '');
		const appointmentDate: string = toFileFormat(date);
		const fileName = `${pdfName}_${fullNamePatient}_${appointmentDate}.pdf`;

		return this.downloadService.downloadPdfWithRequestParams(url, fileName, { appointmentId });
	}

	getAssignedAppointmentsList(patientId: number): Observable<AssignedAppointmentDto[]> {
		const url = `${this.BASE_URL}/${patientId}/get-assigned-appointments`;
		return this.http.get<AssignedAppointmentDto[]>(url);
	}

	getBookingAppointmentsList(identificationNumber: string): Observable<BookedAppointmentDto[]> {
		const url = `${this.BASE_URL}/${identificationNumber}/get-booking-appointments`;
		return this.http.get<BookedAppointmentDto[]>(url);
	}

	getAppointmentHistoric(pageNumber: number, patientId: number): Observable<PatientAppointmentHistoryDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/appointment-history/patient/${patientId}/by-professional-diaries`;
		let queryParam: HttpParams = new HttpParams();
		queryParam = queryParam.append('page', pageNumber);
		return this.http.get<PatientAppointmentHistoryDto[]>(url, { params: queryParam });
	}

	getCurrentAppointmentMedicalCoverage(patientId: number): Observable<ExternalPatientCoverageDto> {
		const url = `${this.BASE_URL}/patient/${patientId}/get-medical-coverage`;
		return this.http.get<ExternalPatientCoverageDto>(url);
	}

	verifyExistingAppointments(patientId: number, date: string, hour: string, institutionId?: number,): Observable<AppointmentShortSummaryDto> {
		const url = this.getVerifyExistingAppointmentUrl(patientId, institutionId);
		let queryParam: HttpParams = new HttpParams();
		queryParam = queryParam.append('date', date).append('hour', hour);
		return this.http.get<AppointmentShortSummaryDto>(url, { params: queryParam });
	}

	private getVerifyExistingAppointmentUrl(patientId: number, institutionId?: number): string {
		return institutionId ?
			`${environment.apiBase}/institutions/${institutionId}/medicalConsultations/appointments/patient/${patientId}/verify-existing-appointments`
			: `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/patient/${patientId}/verify-existing-appointments`;
	}

	verifyExistingEquipmentAppointments(patientId: number, date: string): Observable<AppointmentShortSummaryDto> {
		const url = `${this.BASE_URL}/patient/${patientId}/verify-existing-appointments-equipment`;
		let queryParam: HttpParams = new HttpParams();
		queryParam = queryParam.append('date', date);
		return this.http.get<AppointmentShortSummaryDto>(url, { params: queryParam });
	}

	getStudyInstanceUID(appointmentId: number): Observable<StudyIntanceUIDDto> {
		const url = `${this.BASE_URL}/get-study-instance-UID/${appointmentId}`;
		return this.http.get<StudyIntanceUIDDto>(url);
	}

	getCurrentAppointmentHierarchicalUnit(patientId: number): Observable<HierarchicalUnitDto> {
		const url = `${this.BASE_URL}/patient/${patientId}/get-hierarchical-unit`;
		return this.http.get<HierarchicalUnitDto>(url);
	}

	hasCurrentAppointment(patientId: number): Observable<number> {
		const url = `${this.BASE_URL}/patient/${patientId}/has-current-appointment`;
		return this.http.get<number>(url);
	}

	setAppointmentLabel(labelId: number, appointmentId: number) {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/${appointmentId}/label`;
		return this.http.post<boolean>(url, labelId);
	}

	getAppoinmentOrderDetail(appointmentId: number, isOrderTranscribed: boolean): Observable<AppointmentOrderDetailImageDto> {
		const url = `${this.BASE_URL}/${appointmentId}/detailOrderImage/transcribed-order/${isOrderTranscribed}`;
		return this.http.get<AppointmentOrderDetailImageDto>(url);
	}

	bookAppointment(bookingDto: BookingDto): Observable<SavedBookingAppointmentDto> {
		const url = `${this.BASE_URL}/third-party`;
		return this.http.post<SavedBookingAppointmentDto>(url, bookingDto);
	}

	getRecurringAppointmentType(): Observable<RecurringTypeDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/recurring-appointment-type`;
		return this.http.get<RecurringTypeDto[]>(url);
	}

	getWeekDay(): Observable<WeekDayDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/week-day`;
		return this.http.get<WeekDayDto[]>(url);
	}

	everyWeekSave(createAppointmentDto: CreateAppointmentDto): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/every-week-save`;
		return this.http.post<boolean>(url, createAppointmentDto);
	}

	customSave(createCustomAppointmentDto: CreateCustomAppointmentDto): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/custom-save`;
		return this.http.post<boolean>(url, createCustomAppointmentDto);
	}

	noRepeat(appointmentId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/${appointmentId}/no-repeat`;
		return this.http.put<boolean>(url, {});
	}

	cancelRecurringAppointments(appointmentId: number, cancelAllAppointments: boolean): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/${appointmentId}/cancel-recurring-appointments`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('cancelAllAppointments', cancelAllAppointments);
		return this.http.put<boolean>(url, {}, { params: queryParams });
	}

	getCustomAppointment(appointmentId: number): Observable<CustomRecurringAppointmentDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/${appointmentId}/custom-appointment`;
		return this.http.get<CustomRecurringAppointmentDto>(url).pipe(map(dto => { 
			return {
				...dto,
				endDate: (dto.endDate && dateISOParseDate(dto.endDate.toString())) || null
			} 
		}));
	}

	createExpiredAppointment(createAppointmentDto: CreateAppointmentDto): Observable<number> {
		const url = `${this.BASE_URL}/expired`;
		return this.http.post<number>(url, createAppointmentDto);
	}
}
