import { Injectable } from '@angular/core';
import { InternmentEpisodeSummary } from '../../historia-clinica/modules/ambulatoria/modules/internacion/components/internment-episode-summary/internment-episode-summary.component';
import {
	BasicPatientDto,
	CompletePatientDto,
	InternmentSummaryDto,
	PatientType,
	PersonalInformationDto,
	BedSummaryDto,
	InternmentPatientDto,
	HCEEvolutionSummaryDto,
	InternmentEpisodeDto, DocumentsSummaryDto,
} from '@api-rest/api-model';
import { PatientBasicData } from '../components/patient-card/patient-card.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { DateFormat, momentParseDate, momentParseDateTime } from '@core/utils/moment.utils';
import { BedManagement } from '../../camas/routes/home/home.component';
import { HistoricalProblems } from '../../historia-clinica/modules/ambulatoria/services/historical-problems-facade.service';
import { InternmentPatientTableData } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-patient-table/internment-patient-table.component";

@Injectable({
	providedIn: 'root'
})
export class MapperService {

	toInternmentEpisodeSummary: (o: InternmentSummaryDto) => InternmentEpisodeSummary = MapperService._toInternmentEpisodeSummary;
	toPatientBasicData: (o: BasicPatientDto) => PatientBasicData = MapperService._toPatientBasicData;
	toPersonalInformationData: (o1: CompletePatientDto, o2: PersonalInformationDto) => PersonalInformation = MapperService._toPersonalInformationData;
	toPatientTypeData: (patientType: PatientType) => PatientTypeData = MapperService._toPatientTypeData;
	toInternmentPatientTableData: (patient: InternmentPatientDto | InternmentEpisodeDto) => InternmentPatientTableData = MapperService._toInternmentPatientTableData;
	toBedManagement: (bedSummary: BedSummaryDto[]) => BedManagement[] = MapperService._toBedManagement;
	toHistoricalProblems: (hceEvolutionSummaryDto: HCEEvolutionSummaryDto[]) => HistoricalProblems[] = MapperService._toHistoricalProblems;

	constructor() {
	}

	private static _toInternmentEpisodeSummary(internmentSummary: InternmentSummaryDto): InternmentEpisodeSummary {
		const internmentEpisodeSummary = {
			id: internmentSummary.id,
			bedNumber: internmentSummary.bed.bedNumber,
			roomNumber: internmentSummary.bed.room.roomNumber,
			sectorDescription: internmentSummary.bed.room.sector.description,
			totalInternmentDays: internmentSummary.totalInternmentDays,
			doctor: null,
			admissionDatetime: momentParseDate(String(internmentSummary.entryDate)).format(DateFormat.VIEW_DATE),
			probableDischargeDate: internmentSummary.probableDischargeDate ? momentParseDateTime(String(internmentSummary.probableDischargeDate)).format(DateFormat.VIEW_DATE) : 'Sin fecha definida',
			responsibleContact: null
		};
		if (internmentSummary.doctor) {
			internmentEpisodeSummary.doctor = {
				firstName: internmentSummary.doctor.firstName,
				lastName: internmentSummary.doctor.lastName,
				licenses: internmentSummary.doctor.licenses,
			};
		}
		if (internmentSummary.responsibleContact) {
			internmentEpisodeSummary.responsibleContact = {
				fullName: internmentSummary.responsibleContact?.fullName,
				phoneNumber: internmentSummary.responsibleContact?.phoneNumber,
				relationship: internmentSummary.responsibleContact?.relationship,
			};
		}
		return internmentEpisodeSummary;
	}

	private static _toPatientBasicData<T extends BasicPatientDto>(patient: T): PatientBasicData {
		return {
			id: patient.id,
			firstName: patient.person.firstName,
			middleNames: patient.person.middleNames,
			lastName: patient.person.lastName,
			otherLastNames: patient.person.otherLastNames,
			gender: patient.person.gender.description,
			age: patient.person.age,
			nameSelfDetermination: patient.person.nameSelfDetermination,
			selfPerceivedGender: patient.person.selfPerceivedGender
		};
	}

	private static _toPersonalInformationData(patient: CompletePatientDto, person: PersonalInformationDto): PersonalInformation {
		const personalInformation = {
			identificationNumber: person.identificationNumber,
			identificationType: person.identificationType,
			cuil: person.cuil,
			address: person.address,
			birthDate: person.birthDate ? momentParseDate(String(person.birthDate)).format(DateFormat.VIEW_DATE) : '',
			email: person.email,
			phonePrefix: person.phonePrefix,
			phoneNumber: person.phoneNumber,
			medicalCoverageName: patient.medicalCoverageName,
			medicalCoverageAffiliateNumber: patient.medicalCoverageAffiliateNumber
		};
		personalInformation.address.id = null;
		return personalInformation;
	}

	private static _toPatientTypeData(patientType: PatientType): PatientTypeData {
		return {
			id: patientType.id,
			description: patientType.description
		};
	}

	private static _toInternmentPatientTableData(patient: InternmentPatientDto | InternmentEpisodeDto): InternmentPatientTableData {
		if ("patientId" in patient)
			return mapInternmentPatientToInternmentTable(patient);
		return mapInternmentEpisodeToInternmentTable(<InternmentEpisodeDto>patient);

		function mapInternmentPatientToInternmentTable(patient: InternmentPatientDto): InternmentPatientTableData {
			return {
				patientId: patient.patientId,
				firstName: patient.firstName,
				identificationNumber: patient.identificationNumber,
				identificationTypeId: patient.identificationTypeId,
				internmentId: patient.internmentId,
				lastName: patient.lastName,
				fullName: getFullName(patient.firstName, patient.lastName),
				nameSelfDetermination: patient.nameSelfDetermination,
				bedInfo: {
					sector: patient.sectorDescription,
					roomNumber: patient.roomNumber,
					bedNumber: patient.bedNumber
				},
				hasPhysicalDischarge: patient.hasPhysicalDischarge,
				documentsSummary: patient.documentsSummary,
				hasMedicalDischarge: patient.hasMedicalDischarge
			}
			function getFullName(firstName: string, lastName: string): string {
				if (firstName && lastName)
					return `${firstName} ${lastName}`
				if (firstName)
					return `${firstName}`
				if (lastName)
					return `${lastName}`
				return undefined
			}
		}

		function mapInternmentEpisodeToInternmentTable(info: InternmentEpisodeDto): InternmentPatientTableData {
			return {
				patientId: info.patient.id,
				firstName: info.patient.firstName,
				identificationNumber: info.patient.identificationNumber,
				identificationTypeId: info.patient.identificationTypeId,
				internmentId: info.id,
				lastName: info.patient.lastName,
				fullName: getFullName(info.patient.firstName, info.patient.lastName),
				nameSelfDetermination: info.patient.nameSelfDetermination,
				bedInfo: {
					sector: info.bed.room.sector.description,
					roomNumber: info.bed.room.roomNumber,
					bedNumber: info.bed.bedNumber
				},
				hasPhysicalDischarge: info.hasPhysicalDischarge,
				documentsSummary: info.documentsSummary
			}
			function getFullName(firstName: string, lastName: string): string {
				if (firstName && lastName)
					return `${firstName} ${lastName}`
				if (firstName)
					return `${firstName}`
				if (lastName)
					return `${lastName}`
				return undefined
			}
		}
	}

	private static _toBedManagement(bedSummary: BedSummaryDto[]): BedManagement[] {
		const bedManagement: BedManagement[] = [];
		bedSummary.forEach(summary => {
			const sector = bedManagement.find(e => e.sectorId === summary.sector.id);

			if (sector) {
				const newBed = {
					bedId: summary.bed.id,
					bedNumber: summary.bed.bedNumber,
					free: summary.bed.free
				};
				sector.beds.push(newBed);
			} else {
				const clinicalSpecialties = [];
				summary.sector.clinicalSpecialties.forEach(specialty => {
					clinicalSpecialties.push({
						specialtyId: specialty.id,
						specialtyName: specialty.name,
					});
				});
				const newSector = {
					sectorId: summary.sector.id,
					sectorDescription: summary.sector.description,
					careType: summary.sector.careType,
					organizationType: summary.sector.organizationType,
					ageGroup: summary.sector.ageGroup,
					specialties: clinicalSpecialties,
					beds: [{
						bedId: summary.bed.id,
						bedNumber: summary.bed.bedNumber,
						free: summary.bed.free
					}]
				};
				bedManagement.push(newSector);
			}
		});
		return bedManagement;
	}

	private static _toHistoricalProblems(hceEvolutionSummaryDto: HCEEvolutionSummaryDto[]): HistoricalProblems[] {

		return hceEvolutionSummaryDto.reduce((historicalProblemsList, currentOutpatientEvolutionSummary) => {
			currentOutpatientEvolutionSummary.healthConditions.length ?
				historicalProblemsList = [...historicalProblemsList, ...currentOutpatientEvolutionSummary.healthConditions.map(problem => ({
					consultationDate: currentOutpatientEvolutionSummary.startDate,
					consultationEvolutionNote: currentOutpatientEvolutionSummary.evolutionNote,
					professionalFirstName: currentOutpatientEvolutionSummary.professional.person.firstName,
					professionalLastName: currentOutpatientEvolutionSummary.professional.person.lastName,
					professionalNameSelfDetermination: currentOutpatientEvolutionSummary.professional.person.nameSelfDetermination,
					consultationProfessionalId: currentOutpatientEvolutionSummary.professional.id,
					consultationProfessionalPersonId: currentOutpatientEvolutionSummary.professional.person.id,
					document: currentOutpatientEvolutionSummary.document,
					problemId: problem.snomed.sctid,
					problemPt: problem.snomed.pt,
					specialtyId: currentOutpatientEvolutionSummary.clinicalSpecialty?.id,
					specialityPt: currentOutpatientEvolutionSummary.clinicalSpecialty?.name,
					consultationReasons: currentOutpatientEvolutionSummary.reasons?.map(r => ({ reasonId: r.snomed.sctid, reasonPt: r.snomed.pt })),
					consultationProcedures: currentOutpatientEvolutionSummary.procedures.map(p => ({ procedureDate: p.performedDate, procedureId: p.snomed.sctid, procedurePt: p.snomed.pt })),
					reference: problem.references?.length > 0 ? problem.references : null

				}))] : historicalProblemsList = [...historicalProblemsList, {
					consultationDate: currentOutpatientEvolutionSummary.startDate,
					consultationEvolutionNote: currentOutpatientEvolutionSummary.evolutionNote,
					professionalFirstName: currentOutpatientEvolutionSummary.professional.person.firstName,
					professionalLastName: currentOutpatientEvolutionSummary.professional.person.lastName,
					professionalNameSelfDetermination: currentOutpatientEvolutionSummary.professional.person.nameSelfDetermination,
					consultationProfessionalId: currentOutpatientEvolutionSummary.professional.id,
					consultationProfessionalPersonId: currentOutpatientEvolutionSummary.professional.person.id,
					document: currentOutpatientEvolutionSummary.document,
					problemId: 'Problema no informado',
					problemPt: 'Problema no informado',
					specialtyId: currentOutpatientEvolutionSummary.clinicalSpecialty?.id,
					specialityPt: currentOutpatientEvolutionSummary.clinicalSpecialty?.name,
					consultationReasons: currentOutpatientEvolutionSummary.reasons.map(r => ({ reasonId: r.snomed.sctid, reasonPt: r.snomed.pt })),
					consultationProcedures: currentOutpatientEvolutionSummary.procedures.map(p => ({ procedureDate: p.performedDate, procedureId: p.snomed.sctid, procedurePt: p.snomed.pt })),
					reference: null,
				}];
			return historicalProblemsList;
		}, []);


	}

}
