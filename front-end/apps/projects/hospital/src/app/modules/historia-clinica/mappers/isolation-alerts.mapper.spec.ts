import { DateDto, DateTimeDto, MasterDataDto, PatientCurrentIsolationAlertDto, IsolationAlertAuthorDto, TimeDto } from "@api-rest/api-model";
import { IsolationAlertsSummary, IsolationAlertStatus } from "@historia-clinica/components/isolation-alerts-summary-card/isolation-alerts-summary-card.component";
import { mapIsolationAlertsToIsolationAlertsDetails, mapIsolationAlertToIsolationAlertDetail, mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail, mapPatientCurrentIsolationAlertDtoToIsolationAlertSummary, mapPatientCurrentIsolationAlertsDtoToIsolationAlertsDetail, mapPatientCurrentIsolationAlertsDtoToIsolationAlertsSummary, toIsolationAlertStatus, toRegisterEditorInfo } from "./isolation-alerts.mapper";
import { IsolationAlertDetail } from "@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component";
import { RegisterEditor } from "@presentation/components/register-editor-info/register-editor-info.component";
import { IsolationAlert } from "@historia-clinica/components/isolation-alert-form/isolation-alert-form.component";
import { convertDateTimeDtoToDate } from "@api-rest/mapper/date-dto.mapper";

describe('isolation-alerts.mapper', () => {
	const dateDtoMock: DateDto = { day: 8, month: 11, year: 2024 }
	const timeDtoMock: TimeDto = { hours: 13, minutes: 30 };
	const dateTimeDtoMock: DateTimeDto = { date: dateDtoMock, time: timeDtoMock };
	const isolationAlertAuthorDto: IsolationAlertAuthorDto = {
		fullName: 'Joselina Valdez',
		id: 1,
	}
	const criticality: MasterDataDto = { id: 1, description: 'Alta' };
	const status: MasterDataDto = { id: 1, description: 'Activa' };
	const types: MasterDataDto[] = [{ id: 1, description: "Contacto" }];

	const patientIsolationAlertDtoWithoutObservations: PatientCurrentIsolationAlertDto = {
		author: isolationAlertAuthorDto,
		criticality,
		endDate: dateDtoMock,
		healthConditionPt: 'fiebre',
		healthConditionSctid: '12',
		isolationAlertId: 1,
		startDate: dateTimeDtoMock,
		status,
		types,
		isModified: false,
		updatedBy: null,
		updatedOn: null,
	};

	const patientIsolationAlertDto: PatientCurrentIsolationAlertDto = {
		author: isolationAlertAuthorDto,
		criticality,
		endDate: dateDtoMock,
		healthConditionPt: 'fiebre',
		healthConditionSctid: '12',
		isolationAlertId: 1,
		observations: 'Agrego observaciones',
		startDate: dateTimeDtoMock,
		status,
		types,
		isModified: false,
		updatedBy: null,
		updatedOn: null,
	};

	const updatedPatientIsolationAlertDto: PatientCurrentIsolationAlertDto = {
		author: isolationAlertAuthorDto,
		criticality,
		endDate: dateDtoMock,
		healthConditionPt: 'fiebre',
		healthConditionSctid: '12',
		isolationAlertId: 1,
		observations: 'Agrego observaciones',
		startDate: dateTimeDtoMock,
		status,
		types,
		isModified: true,
		updatedBy: isolationAlertAuthorDto,
		updatedOn: dateTimeDtoMock
	};

	const isolationAlert: IsolationAlert = {
		id: 1,
		diagnosis: {
			snomed: {
				pt: "fiebre",
				sctid: "12",
			}
		},
		types,
		criticality,
		endDate: new Date(2024, 10, 8),
		observations: 'Agrego observaciones',
	}

	const patientCurrentIsolationAlerDtoList: PatientCurrentIsolationAlertDto[] = [patientIsolationAlertDto];
	const updatedPatientIsolationAlertDtoList: PatientCurrentIsolationAlertDto[] = [updatedPatientIsolationAlertDto];
	const isolationAlertList: IsolationAlert[] = [isolationAlert];

	it('it should return true if MasterDataDto is correctly mapped to IsolationAlertStatus', () => {
		const expectedIsolationAlertStatus: IsolationAlertStatus = { id: 1, description: 'Activa' };
		const mappedIsolationAlertSummary = toIsolationAlertStatus(expectedIsolationAlertStatus);
		return expect(mappedIsolationAlertSummary).toEqual(expectedIsolationAlertStatus);
	});

	it('it should return true if PatientCurrentIsolationAlertDto is correctly mapped to IsolationAlertsSummary', () => {
		const expectedIsolationAlertSummary: IsolationAlertsSummary = {
			id: 1,
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			status: {
				id: 1,
				description: 'Activa'
			}
		};

		const mappedIsolationAlertSummary = mapPatientCurrentIsolationAlertDtoToIsolationAlertSummary(patientIsolationAlertDto);
		expect(mappedIsolationAlertSummary).toEqual(expectedIsolationAlertSummary);
	});

	it('it should return true if PatientCurrentIsolationAlertDtoList is correctly mapped to IsolationAlertsSummaryList', () => {
		const expectedIsolationAlertSummaryList: IsolationAlertsSummary[] = [{
			id: 1,
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			status: {
				id: 1,
				description: 'Activa'
			}
		}];
		const mappedIsolationAlertSummaryList = mapPatientCurrentIsolationAlertsDtoToIsolationAlertsSummary(patientCurrentIsolationAlerDtoList);
		expect(mappedIsolationAlertSummaryList).toEqual(expectedIsolationAlertSummaryList);
	});

	it('it should return true if ProfessionalInfoDto and DateTimeDto is correctly mapped to RegisterEditor', () => {
		const date = new Date(2024, 10, 8);
		date.setUTCHours(13, 30);
		const expectedRegistorEditor: RegisterEditor = {
			createdBy: 'Joselina Valdez',
			date
		}
		const mappedRegisterEditor = toRegisterEditorInfo(isolationAlertAuthorDto.fullName, convertDateTimeDtoToDate(dateTimeDtoMock));
		expect(mappedRegisterEditor).toEqual(expectedRegistorEditor);
	});

	it('it should return true if PatientCurrentIsolationAlertDtoWithoutObservations is correctly mapped to IsolationAlertDetail', () => {
		const date = new Date(2024, 10, 8);
		date.setUTCHours(13, 30);
		const expectedIsolationAlertDetail: IsolationAlertDetail = {
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			creator: {
				createdBy: 'Joselina Valdez',
				date
			},
		};
		const mappedIsolationAlertDetail = mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail(patientIsolationAlertDtoWithoutObservations);
		expect(mappedIsolationAlertDetail).toEqual(expectedIsolationAlertDetail);
	});

	it('it should return true if PatientCurrentIsolationAlertDto is correctly mapped to IsolationAlertDetail', () => {
		const date = new Date(2024, 10, 8);
		date.setUTCHours(13, 30);
		const expectedIsolationAlertDetail: IsolationAlertDetail = {
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			creator: {
				createdBy: 'Joselina Valdez',
				date
			},
			observations: 'Agrego observaciones',
		};
		const mappedIsolationAlertDetail = mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail(patientIsolationAlertDto);
		expect(mappedIsolationAlertDetail).toEqual(expectedIsolationAlertDetail);
	});

	it('it should return true if updated PatientCurrentIsolationAlertDto is correctly mapped to IsolationAlertDetail', () => {
		const date = new Date(2024, 10, 8);
		date.setUTCHours(13, 30);
		const expectedIsolationAlertDetail: IsolationAlertDetail = {
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			creator: {
				createdBy: 'Joselina Valdez',
				date
			},
			observations: 'Agrego observaciones',
			editor: {
				createdBy: 'Joselina Valdez',
				date
			},
		};
		const mappedIsolationAlertDetail = mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail(updatedPatientIsolationAlertDto);
		expect(mappedIsolationAlertDetail).toEqual(expectedIsolationAlertDetail);
	});

	it('it should return true if PatientCurrentIsolationAlertDtoList is correctly mapped to IsolationAlertDetailList', () => {
		const date = new Date(2024, 10, 8);
		date.setUTCHours(13, 30);
		const expectedIsolationAlertDetailList: IsolationAlertDetail[] = [{
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			creator: {
				createdBy: 'Joselina Valdez',
				date
			},
			observations: 'Agrego observaciones',
		}];
		const mappedIsolationAlertDetailList = mapPatientCurrentIsolationAlertsDtoToIsolationAlertsDetail(patientCurrentIsolationAlerDtoList);
		expect(mappedIsolationAlertDetailList).toEqual(expectedIsolationAlertDetailList);
	});

	it('it should return true if updated PatientCurrentIsolationAlertDtoList is correctly mapped to IsolationAlertDetailList', () => {
		const date = new Date(2024, 10, 8);
		date.setUTCHours(13, 30);
		const expectedIsolationAlertDetailList: IsolationAlertDetail[] = [{
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			creator: {
				createdBy: 'Joselina Valdez',
				date
			},
			editor: {
				createdBy: 'Joselina Valdez',
				date
			},
			observations: 'Agrego observaciones',
		}];
		const mappedIsolationAlertDetailList = mapPatientCurrentIsolationAlertsDtoToIsolationAlertsDetail(updatedPatientIsolationAlertDtoList);
		expect(mappedIsolationAlertDetailList).toEqual(expectedIsolationAlertDetailList);
	});

	it('it should return true if IsolationAlert is correctly mapped to IsolationAlertDetail', () => {
		const expectedIsolationAlertDetail: IsolationAlertDetail = {
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			observations: 'Agrego observaciones',
		};
		const mappedIsolationAlertDetails = mapIsolationAlertToIsolationAlertDetail(isolationAlert);
		expect(mappedIsolationAlertDetails).toEqual(expectedIsolationAlertDetail);
	});

	it('it should return true if IsolationAlertList is correctly mapped to IsolationAlertDetailList', () => {
		const expectedIsolationAlertDetailList: IsolationAlertDetail[] = [{
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			observations: 'Agrego observaciones',
		}];
		const mappedIsolationAlertDetailsList = mapIsolationAlertsToIsolationAlertsDetails(isolationAlertList);
		expect(mappedIsolationAlertDetailsList).toEqual(expectedIsolationAlertDetailList);
	});

});