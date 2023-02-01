import { Injectable } from '@angular/core';
import { AddressDto, DepartmentDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { ContextService } from '@core/services/context.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { BehaviorSubject, forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

const COUNTRY = 14;

@Injectable()

export class ReferenceOriginInstitutionService {

    private provincesSource = new BehaviorSubject<TypeaheadOption<any>[]>(null);
    private originInstitutionInfoSource = new BehaviorSubject<AddressDto>(null);
    private originDepartmentSource = new BehaviorSubject<DepartmentDto>(null);
    provinces$: Observable<TypeaheadOption<any>[]> = this.provincesSource.asObservable();
    originInstitutionInfo$: Observable<AddressDto> = this.originInstitutionInfoSource.asObservable();
    originDepartment$: Observable<DepartmentDto> = this.originDepartmentSource.asObservable();

    constructor(
        private readonly adressMasterData: AddressMasterDataService,
        private readonly institutionService: InstitutionService,
        private readonly contextService: ContextService
    ) {
        const provincesInfo$ = this.adressMasterData.getByCountry(COUNTRY);
        const institutionAddress$ = this.institutionService.getAddress(this.contextService.institutionId);
        forkJoin([provincesInfo$, institutionAddress$]).subscribe(originInstitutionInformation => {
            const provs = this.toTypeaheadOptions(originInstitutionInformation[0], 'description');
            this.provincesSource.next(provs);
            this.originInstitutionInfoSource.next(originInstitutionInformation[1]);
        });
    }

    getProvinceById(provinceId: number): Observable<TypeaheadOption<any>> {
        return this.provinces$.pipe(map(provs => this.filterProvinces(provinceId, provs)));
    }

    private filterProvinces(provinceId: number, provinces: TypeaheadOption<any>[]): TypeaheadOption<any> {
        return provinces?.find(p => p.value === provinceId);
    }

    setOriginDepartment(department: DepartmentDto) {
        this.originDepartmentSource.next(department);
    }

    private toTypeaheadOptions(response: any[], attribute: string): TypeaheadOption<any>[] {
        return response.map(r => {
            return {
                value: r.id,
                compareValue: r[attribute],
                viewValue: r[attribute]
            }
        })
    }
}
