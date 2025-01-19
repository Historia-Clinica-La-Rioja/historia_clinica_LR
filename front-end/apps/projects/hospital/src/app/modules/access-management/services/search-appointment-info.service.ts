import { Injectable } from '@angular/core';
import { CareLineDto, ClinicalSpecialtyDto, DateDto, EAppointmentModality, ReferenceDataDto, ReferenceSummaryDto, SharedSnomedDto } from '@api-rest/api-model';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { objectToTypeaheadOption } from '@presentation/utils/typeahead.mapper.utils';
import { SearchCriteria } from '@turnos/components/search-criteria/search-criteria.component';

@Injectable({
	providedIn: 'root'
})
export class SearchAppointmentsInfoService {

	patientId: number;
	referenceData: ReferenceDataDto;
	searchAppointmentsInTabs = false;
	searchCriteriaAppointment: SearchCriteriaValues;

	constructor() { }

	loadInformation(patientId: number, referenceData: ReferenceDataDto) {
		this.patientId = patientId;
		this.referenceData = referenceData;
	}

	setSearchCriteria(values: SearchCriteriaValues){
		this.searchCriteriaAppointment = values;
	}

	clearInfo() {
		this.referenceData = null;
	}

	getSearchAppointmentInfo(): SearchAppointmentInformation {
		return this.referenceData ? {
			formInformation: this.getExteralValues(),
			disabledInput: {
				specialty: this.enableSpecialtyActions(),
				others: true
			},
			referenceCompleteData: this.referenceData,
			referenceSummary: this.getReferenceSummary(),
			enableSectionToSearchAppointmentInOtherTab: this.searchAppointmentsInTabs,
			patientId: this.patientId
		} : null;
	}

	getSearchCriteriaValues(): SearchCriteriaValues{
		return this.searchCriteriaAppointment;
	}

	private getReferenceSummary(): ReferenceSummaryDto {
		return this.toReferenceSummary();
	}

	private getExteralValues(): ExternalSetValues {
		const practice = this.referenceData.procedure;
		return {
			careLine: objectToTypeaheadOption(this.referenceData.careLine, 'description'),
			clinicalSpecialties: this.referenceData.destinationClinicalSpecialties.length ? this.referenceData.destinationClinicalSpecialties : null,
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

	private enableSpecialtyActions(): boolean {
		return this.referenceData.destinationClinicalSpecialties.length <= 1
	}

}

export interface SearchAppointmentInformation {
	formInformation: ExternalSetValues;
	disabledInput: {
		specialty: boolean,
		others: boolean
	}
	referenceCompleteData: ReferenceDataDto;
	referenceSummary: ReferenceSummaryDto;
	enableSectionToSearchAppointmentInOtherTab: boolean;
	patientId: number;
}

export interface ExternalSetValues {
	careLine: TypeaheadOption<CareLineDto>;
	practice: SharedSnomedDto;
	clinicalSpecialties: ClinicalSpecialtyDto[];
	searchCriteria: SearchCriteria;
}

export interface SearchCriteriaValues{
	searchCriteria: SearchCriteria;
	careModality: EAppointmentModality;
	startDate: DateDto;
}
