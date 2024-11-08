import { DateDto, DateTimeDto, MasterDataDto, PatientCurrentIsolationAlertDto, ProfessionalInfoDto, TimeDto } from "@api-rest/api-model";
import { IsolationAlertsSummary, IsolationAlertStatus } from "@historia-clinica/components/isolation-alerts-summary-card/isolation-alerts-summary-card.component";
import { mapIsolationAlertsToIsolationAlertsDetails, mapIsolationAlertToIsolationAlertDetail, mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail, mapPatientCurrentIsolationAlertDtoToIsolationAlertSummary, mapPatientCurrentIsolationAlertsDtoToIsolationAlertsDetail, mapPatientCurrentIsolationAlertsDtoToIsolationAlertsSummary, toIsolationAlertStatus, toRegisterEditorInfo } from "./isolation-alerts.mapper";
import { IsolationAlertDetail } from "@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component";
import { RegisterEditor } from "@presentation/components/register-editor-info/register-editor-info.component";
import { IsolationAlert } from "@historia-clinica/components/isolation-alert-form/isolation-alert-form.component";

describe('isolation-alerts.mapper', () => {
	const dateDtoMock: DateDto = { day: 8, month: 11, year: 2024 }
	const timeDtoMock: TimeDto = { hours: 10, minutes: 30 };
	const dateTimeDtoMock: DateTimeDto = { date: dateDtoMock, time: timeDtoMock };
	const professionalInfoDto: ProfessionalInfoDto = {
		clinicalSpecialties: [],
		firstName: 'Joselina',
		fullName: 'Joselina Valdez',
		id: 1,
		identificationNumber: '41553415',
		lastName: 'Valdez',
		licenceNumber: '2',
		middleNames: '',
		nameSelfDetermination: '',
		otherLastNames: '',
		phoneNumber: '2281499109',
		useSelfDeterminedName: false,
	}
	const criticality: MasterDataDto = { id: 1, description: 'Alta' };
	const status: MasterDataDto = { id: 1, description: 'Activa' };
	const types: MasterDataDto[] = [{ id: 1, description: "Contacto" }];

	const patientCurrentIsolationAlertDto: PatientCurrentIsolationAlertDto = {
		author: professionalInfoDto,
		criticality,
		endDate: dateDtoMock,
		healthConditionPt: 'fiebre',
		healthConditionSctid: '12',
		isolationAlertId: 1,
		observations: 'Agrego observaciones',
		startDate: dateTimeDtoMock,
		status,
		types,
	};

	const isolationAlert: IsolationAlert = {
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

	const patientCurrentIsolationAlerDtoList: PatientCurrentIsolationAlertDto[] = [patientCurrentIsolationAlertDto];
	const isolationAlertList: IsolationAlert[] = [isolationAlert];

	it('it should return true if the MasterDataDto is correctly mapped to IsolationAlertStatus', () => {
		const expectedIsolationAlertStatus: IsolationAlertStatus = { id: 1, description: 'Activa' };
		const mappedIsolationAlertSummary = toIsolationAlertStatus(expectedIsolationAlertStatus);
		return expect(mappedIsolationAlertSummary).toEqual(expectedIsolationAlertStatus);
	});

	it('it should return true if the PatientCurrentIsolationAlertDto is correctly mapped to IsolationAlertsSummary', () => {
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

		const mappedIsolationAlertSummary = mapPatientCurrentIsolationAlertDtoToIsolationAlertSummary(patientCurrentIsolationAlertDto);
		expect(mappedIsolationAlertSummary).toEqual(expectedIsolationAlertSummary);
	});

	it('it should return true if the PatientCurrentIsolationAlertDtoList is correctly mapped to IsolationAlertsSummaryList', () => {
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

	it('it should return true if the ProfessionalInfoDto y DateTimeDto is correctly mapped to RegisterEditor', () => {
		const expectedRegistorEditor: RegisterEditor = {
			createdBy: 'Joselina Valdez',
			date: new Date(2024, 10, 8)
		}

		expectedRegistorEditor.date.setUTCHours(10, 30);
		const mappedRegisterEditor = toRegisterEditorInfo(professionalInfoDto, dateTimeDtoMock);
		expect(mappedRegisterEditor).toEqual(expectedRegistorEditor);
	});

	it('it should return true if the PatientCurrentIsolationAlertDto is correctly mapped to IsolationAlertDetail', () => {
		const expectedIsolationAlertDetail: IsolationAlertDetail = {
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			creator: {
				createdBy: 'Joselina Valdez',
				date: new Date(2024, 10, 8)
			},
			observations: 'Agrego observaciones',
		};
		expectedIsolationAlertDetail.creator.date.setUTCHours(10, 30);
		const mappedIsolationAlertDetail = mapPatientCurrentIsolationAlertDtoToIsolationAlertDetail(patientCurrentIsolationAlertDto);
		expect(mappedIsolationAlertDetail).toEqual(expectedIsolationAlertDetail);
	});

	it('it should return true if the PatientCurrentIsolationAlertDtoList is correctly mapped to IsolationAlertDetailList', () => {
		const expectedIsolationAlertDetailList: IsolationAlertDetail[] = [{
			diagnosis: 'fiebre',
			types: ['Contacto'],
			criticality: 'Alta',
			endDate: new Date(2024, 10, 8),
			creator: {
				createdBy: 'Joselina Valdez',
				date: new Date(2024, 10, 8)
			},
			observations: 'Agrego observaciones',
		}];
		expectedIsolationAlertDetailList.at(0).creator.date.setUTCHours(10, 30);
		const mappedIsolationAlertDetailList = mapPatientCurrentIsolationAlertsDtoToIsolationAlertsDetail(patientCurrentIsolationAlerDtoList);
		expect(mappedIsolationAlertDetailList).toEqual(expectedIsolationAlertDetailList);
	});

	it('it should return true if the IsolationAlert is correctly mapped to IsolationAlertDetail', () => {
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

	it('it should return true if the IsolationAlertList is correctly mapped to IsolationAlertDetailList', () => {
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