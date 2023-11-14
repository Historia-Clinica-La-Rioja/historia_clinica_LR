import { Injectable } from '@angular/core';
import { CareLineDto, ClinicalSpecialtyDto, ReferenceDataDto, ReferenceSummaryDto, SharedSnomedDto } from '@api-rest/api-model';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SearchCriteria } from '@turnos/components/search-criteria/search-criteria.component';

@Injectable({
	providedIn: 'root'
})
export class SearchAppointmentsInfoService {

	patientId: number;
	referenceData: ReferenceDataDto;
	searchAppointmentsInTabs = false;

	constructor() { }

	loadInformation(patientId: number, referenceData: ReferenceDataDto) {
		this.patientId = patientId;
		this.referenceData = referenceData;
	}

	clearInfo() {
		this.referenceData = null;
	}

	getSearchAppointmentInfo(): SearchAppointmentInformation {
		return this.referenceData ? {
			formInformation: this.getExteralValues(),
			disabledInput: true,
			referenceCompleteData: this.referenceData,
			referenceSummary: this.getReferenceSummary(),
			enableSectionToSearchAppointmentInOtherTab: this.searchAppointmentsInTabs,
			patientId: this.patientId
		} : null;
	}

	private getReferenceSummary(): ReferenceSummaryDto {
		return this.toReferenceSummary();
	}

	private getExteralValues(): ExternalSetValues {
		const practice = this.referenceData.procedure;
		return {
			careLine: this.careLineToTypeaheadOption(this.referenceData.careLine),
			clinicalSpecialty: this.referenceData.clinicalSpecialtyDestination.id ? this.specialtyToTypeaheadOption(this.referenceData.clinicalSpecialtyDestination) : null,
			practice,
			searchCriteria: practice ? SearchCriteria.PRACTICES : SearchCriteria.CONSULTATION
		}

	}

	private toReferenceSummary(): ReferenceSummaryDto {
		return {
			date: this.referenceData.date.date,
			id: this.referenceData.id,
			institution: this.referenceData.institutionOrigin.description,
			professionalFullName: this.referenceData.professionalFullName
		}
	}

	private careLineToTypeaheadOption(careLine: CareLineDto): TypeaheadOption<CareLineDto> {
		return {
			compareValue: careLine.description,
			value: careLine,
			viewValue: careLine.description
		};
	}

	private specialtyToTypeaheadOption(specialty: ClinicalSpecialtyDto): TypeaheadOption<ClinicalSpecialtyDto> {
		return {
			compareValue: specialty.name,
			value: specialty,
			viewValue: specialty.name
		};
	}
}

export interface SearchAppointmentInformation {
	formInformation: ExternalSetValues;
	disabledInput: boolean;
	referenceCompleteData: ReferenceDataDto;
	referenceSummary: ReferenceSummaryDto;
	enableSectionToSearchAppointmentInOtherTab: boolean;
	patientId: number;
}

export interface ExternalSetValues {
	careLine: TypeaheadOption<CareLineDto>;
	practice: SharedSnomedDto;
	clinicalSpecialty: TypeaheadOption<ClinicalSpecialtyDto>;
	searchCriteria: SearchCriteria;
}
