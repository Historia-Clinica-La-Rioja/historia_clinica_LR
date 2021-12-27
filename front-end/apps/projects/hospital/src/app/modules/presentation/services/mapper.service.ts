import { Injectable } from '@angular/core';
import { InternmentEpisodeSummary } from '../components/internment-episode-summary/internment-episode-summary.component';
import {
	BasicPatientDto,
	CompletePatientDto,
	InternmentSummaryDto,
	PatientType,
	PersonalInformationDto,
	BedSummaryDto,
	OutpatientEvolutionSummaryDto,
	InternmentPatientDto,
} from '@api-rest/api-model';
import { PatientBasicData } from '../components/patient-card/patient-card.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { DateFormat, momentParseDate, momentParseDateTime } from '@core/utils/moment.utils';
import { BedManagement } from '../../camas/routes/home/home.component';
import { HistoricalProblems } from '../../historia-clinica/modules/ambulatoria/services/historical-problems-facade.service';
import { PatientTableData } from '../../pacientes/component/pacientes-table/pacientes-table.component';

@Injectable({
	providedIn: 'root'
})
export class MapperService {

	toInternmentEpisodeSummary: (o: InternmentSummaryDto) => InternmentEpisodeSummary = MapperService._toInternmentEpisodeSummary;
	toPatientBasicData: (o: BasicPatientDto) => PatientBasicData = MapperService._toPatientBasicData;
	toPersonalInformationData: (o1: CompletePatientDto, o2: PersonalInformationDto) => PersonalInformation = MapperService._toPersonalInformationData;
	toPatientTypeData: (patientType: PatientType) => PatientTypeData = MapperService._toPatientTypeData;
	toPatientTableData: (patient: InternmentPatientDto) => PatientTableData = MapperService._toPatientTableData;
	toBedManagement: (bedSummary: BedSummaryDto[]) => BedManagement[] = MapperService._toBedManagement;
	toHistoricalProblems: (outpatientEvolutionSummary: OutpatientEvolutionSummaryDto[]) => HistoricalProblems[] = MapperService._toHistoricalProblems;

	constructor() {
	}

	private static _toInternmentEpisodeSummary(internmentSummary: InternmentSummaryDto): InternmentEpisodeSummary {
		const internmentEpisodeSummary = {
			bedNumber: internmentSummary.bed.bedNumber,
			roomNumber: internmentSummary.bed.room.roomNumber,
			sectorDescription: internmentSummary.bed.room.sector.description,
			episodeSpecialtyName: internmentSummary.specialty.name,
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
				license: internmentSummary.doctor.licence
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
			age: patient.person.age
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

	private static _toPatientTableData(patient: InternmentPatientDto): PatientTableData {
		return {
			birthDate: patient.birthDate,
			firstName: patient.firstName,
			genderId: patient.genderId,
			identificationNumber: patient.identificationNumber,
			identificationTypeId: patient.identificationTypeId,
			internmentId: patient.internmentId,
			lastName: patient.lastName,
			patientId: patient.patientId,
			fullName: `${patient.firstName} ${patient.lastName}`
		};
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

	private static _toHistoricalProblems(outpatientEvolutionSummary: OutpatientEvolutionSummaryDto[]): HistoricalProblems[] {

		return outpatientEvolutionSummary.reduce((historicalProblemsList, currentOutpatientEvolutionSummary) => {
			currentOutpatientEvolutionSummary.healthConditions.length ?
				historicalProblemsList = [...historicalProblemsList, ...currentOutpatientEvolutionSummary.healthConditions.map(problem => ({
					consultationDate: currentOutpatientEvolutionSummary.startDate,
					consultationEvolutionNote: currentOutpatientEvolutionSummary.evolutionNote,
					consultationProfessionalName: `${currentOutpatientEvolutionSummary.professional.person.firstName} ${currentOutpatientEvolutionSummary.professional.person.lastName}`,
					consultationProfessionalId: currentOutpatientEvolutionSummary.professional.id,
					consultationProfessionalPersonId: currentOutpatientEvolutionSummary.professional.personId,
					problemId: problem.snomed.sctid,
					problemPt: problem.snomed.pt,
					specialtyId: currentOutpatientEvolutionSummary.clinicalSpecialty?.id,
					specialityPt: currentOutpatientEvolutionSummary.clinicalSpecialty?.name,
					consultationReasons: currentOutpatientEvolutionSummary.reasons?.map(r => ({ reasonId: r.snomed.sctid, reasonPt: r.snomed.pt })),
					consultationProcedures: currentOutpatientEvolutionSummary.procedures.map(p => ({ procedureDate: p.performedDate, procedureId: p.snomed.sctid, procedurePt: p.snomed.pt })),
					reference: problem.reference?.careLine ? problem.reference : null

				}))] : historicalProblemsList = [...historicalProblemsList, {
					consultationDate: currentOutpatientEvolutionSummary.startDate,
					consultationEvolutionNote: currentOutpatientEvolutionSummary.evolutionNote,
					consultationProfessionalName: `${currentOutpatientEvolutionSummary.professional.person.firstName} ${currentOutpatientEvolutionSummary.professional.person.lastName}`,
					consultationProfessionalId: currentOutpatientEvolutionSummary.professional.id,
					consultationProfessionalPersonId: currentOutpatientEvolutionSummary.professional.personId,
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
