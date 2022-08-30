import { tap } from 'rxjs/operators';
import { BedSummaryDto, SectorSummaryDto } from '@api-rest/api-model';
import { BedService } from '@api-rest/services/bed.service';
import { Observable, ReplaySubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { BedManagementFilter } from '../components/bed-filters/bed-filters.component';
import { momentParseDateTime, momentParseDate } from '@core/utils/moment.utils';
import { pushIfNotExists } from '@core/utils/array.utils';

@Injectable()
export class BedManagementFacadeService {

	public sectors: Sector[] = [];
	public services: Service[] = [];

	private bedSummarySubject = new ReplaySubject<BedSummaryDto[]>(1);
	private bedSummary$: Observable<BedSummaryDto[]>;
	private bedManagementFilterSubject = new ReplaySubject<BedManagementFilter>(1);
	private bedManagementFilter$: Observable<BedManagementFilter>;
	private originalBedManagement: BedSummaryDto[] = [];
	private initialFilters: BedManagementFilter;

	constructor(
		private bedService: BedService
	) {
		this.bedSummary$ = this.bedSummarySubject.asObservable();
		this.bedManagementFilter$ = this.bedManagementFilterSubject.asObservable();
	}

	public setInitialFilters(initialFilters: BedManagementFilter) {
		this.initialFilters = initialFilters;
		this.bedManagementFilterSubject.next(initialFilters);
	}

	public getBedManagement(): Observable<BedSummaryDto[]> {
		this.bedService.getBedsSummary().pipe(
			tap((bedsSummary: BedSummaryDto[]) => this.filterOptions(bedsSummary))
		).subscribe(data => {
			this.originalBedManagement = data;
			this.initialFilters ? this.sendBedManagementFilter(this.initialFilters) : this.sendBedManagement(this.originalBedManagement);
		});
		return this.bedSummary$;
	}

	public getBedManagementFilter(): Observable<BedManagementFilter> {
		return this.bedManagementFilter$;
	}

	public sendBedManagement(bedManagement: BedSummaryDto[]) {
		this.bedSummarySubject.next(bedManagement);
	}

	public sendBedManagementFilter(newFilter: BedManagementFilter) {
		const bedManagementCopy = [...this.originalBedManagement];
		const result = bedManagementCopy.filter(bedManagement => (
			this.filterBySector(newFilter, bedManagement)
			&& this.filterByService(newFilter, bedManagement)
			&& this.filterByProbableDischargeDate(newFilter, bedManagement)
			&& this.filterByFreeBed(newFilter, bedManagement)));
		this.initialFilters = newFilter;
		this.bedSummarySubject.next(result);
		this.bedManagementFilterSubject.next(newFilter);
	}

	private filterBySector(filter: BedManagementFilter, bed: BedSummaryDto): boolean {
		return (filter.sector ? bed.sector.id === filter.sector : true);
	}

	private filterByService(filter: BedManagementFilter, bedSummary: BedSummaryDto): boolean {
		return (filter.service ? this.sectorHasSpecialty(bedSummary.sector, filter.service) : true);
	}

	private filterByProbableDischargeDate(filter: BedManagementFilter, bed: BedSummaryDto): boolean {
		return (filter.probableDischargeDate ? bed.probableDischargeDate ? momentParseDateTime(bed.probableDischargeDate).isSameOrBefore(momentParseDate(filter.probableDischargeDate)) : false : true);
	}

	private filterByFreeBed(filter: BedManagementFilter, bed: BedSummaryDto): boolean {
		return (filter.filled ? true : bed.bed.free);
	}

	public getFilterOptions() {
		return {
			sectors: this.sectors,
			services: this.services
		};
	}

	private filterOptions(bedsSummary: BedSummaryDto[]): void {
		bedsSummary.forEach(bedSummary => {

			this.sectors = pushIfNotExists(this.sectors,
				{ sectorId: bedSummary.sector.id, sectorDescription: bedSummary.sector.description }, this.compareSector);

			bedSummary.sector.clinicalSpecialties.forEach(clinicalService => {
				this.services = pushIfNotExists(this.services,
					{ serviceId: clinicalService.id, serviceDescription: clinicalService.name }, this.compareService);
			}
			);
		});
	}

	private compareSector(sector: Sector, sector2: Sector): boolean {
		return sector.sectorId === sector2.sectorId;
	}

	private compareService(service: Service, service2: Service): boolean {
		return service.serviceId === service2.serviceId;
	}

	private sectorHasSpecialty(sector: SectorSummaryDto, specialtyId: number): boolean {
		return sector.clinicalSpecialties.some(clinicalSpecialty => clinicalSpecialty.id === specialtyId);
	}

	public getBedSummary(): Observable<BedSummaryDto[]> {
		return this.bedSummary$;
	}

	updateBeds() {
		this.getBedManagement();
	}
}

export class Sector {
	sectorId: number;
	sectorDescription: string;
}

export class Service {
	serviceId: number;
	serviceDescription: string;
}

