import { Injectable } from '@angular/core';
import { InstitutionBasicInfoDto } from '@api-rest/api-model';
import { AddressMasterDataService, AddressProjection } from '@api-rest/services/address-master-data.service';
import { ContextInstitutionService } from '@api-rest/services/context-institution.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { listToTypeaheadOptions } from '@presentation/utils/typeahead.mapper.utils';
import { Observable, map } from 'rxjs';

@Injectable()
export class DestinationInstitutionInformationService {

	constructor(
		private readonly contextInstitutionService: ContextInstitutionService,
		private readonly adressMasterData: AddressMasterDataService,
	) { }

	getDepartmentsByDestinationDataFilter(practiceId: number, clinicalSpecialtiesIds: number[], careLineId: number): Observable<TypeaheadOption<AddressProjection>[]> {
		return (practiceId) ?
			this.adressMasterData.getDepartmentsByCareLineAndPracticesAndClinicalSpecialty(practiceId, clinicalSpecialtiesIds, careLineId)
				.pipe(map(departments => listToTypeaheadOptions(departments, 'description')))
			:
			this.adressMasterData.getDeparmentsByCareLineAndClinicalSpecialty(clinicalSpecialtiesIds, careLineId)
				.pipe(map(departments => listToTypeaheadOptions(departments, 'description')));
	}

	getInstitutionsTypeaheadOptionsByDepartment(departmentId: number, practiceId: number, clinicalSpecialtiesIds: number[], careLineId: number): Observable<TypeaheadOption<InstitutionBasicInfoDto>[]> {
		return practiceId ?
			this.contextInstitutionService.getInstitutionsByReferenceByPracticeFilter
				(practiceId, departmentId, careLineId, clinicalSpecialtiesIds)
				.pipe(map((institutions: InstitutionBasicInfoDto[]) => listToTypeaheadOptions(institutions, 'name')))
			:
			this.contextInstitutionService.getInstitutionsByReferenceByClinicalSpecialtyFilter(departmentId, clinicalSpecialtiesIds, careLineId)
				.pipe(map((institutions: InstitutionBasicInfoDto[]) => listToTypeaheadOptions(institutions, 'name')));
	}
}
