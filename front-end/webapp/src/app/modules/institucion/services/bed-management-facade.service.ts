import { tap } from 'rxjs/operators';
import { BedSummaryDto } from '@api-rest/api-model';
import { BedService } from '@api-rest/services/bed.service';
import { Observable, ReplaySubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { BedManagementFilter } from '../components/bed-filters/bed-filters.component';
import { momentParseDateTime, momentParseDate } from '@core/utils/moment.utils';
import { pushIfNotExists } from '@core/utils/array.utils';

@Injectable()
export class BedManagementFacadeService {

  	public sectors: Sector[] = [];
	public specialities: Speciality[] = [];
	public categories: Category[] = [];

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
		if (!this.originalBedManagement.length) {
			this.bedService.getBedsSummary().pipe(
				tap((bedsSummary: BedSummaryDto[]) => this.filterOptions(bedsSummary))
			).subscribe(data => {
				this.originalBedManagement = data;
				this.initialFilters ? this.sendBedManagementFilter(this.initialFilters) : this.sendBedManagement(this.originalBedManagement);
			});
		}
		return this.bedSummary$;
	}

	public getBedManagementFilter(): Observable<BedManagementFilter>{
		return this.bedManagementFilter$;
	}

	public sendBedManagement(bedManagement: BedSummaryDto[]) {
		this.bedSummarySubject.next(bedManagement);
	}

	public sendBedManagementFilter(newFilter: BedManagementFilter) {
		const bedManagementCopy = [...this.originalBedManagement];
		const result = bedManagementCopy.filter(bedManagement => (this.filterBySector(newFilter, bedManagement)
																	&& this.filterBySpeciality(newFilter, bedManagement)
																	&& this.filterByCategory(newFilter, bedManagement)
																	&& this.filterByProbableDischargeDate(newFilter, bedManagement)
																	&& this.filterByFreeBed(newFilter, bedManagement)));
		this.bedSummarySubject.next(result);
		this.bedManagementFilterSubject.next(newFilter);
	}

	private filterBySector(filter: BedManagementFilter, bed: BedSummaryDto): boolean {
		return (filter.sector ? bed.sector.id === filter.sector : true);
	}

	private filterBySpeciality(filter: BedManagementFilter, bed: BedSummaryDto): boolean {
		return (filter.speciality ? bed.clinicalSpecialty.id === filter.speciality : true);
	}

	private filterByCategory(filter: BedManagementFilter, bed: BedSummaryDto): boolean {
		return (filter.category ? bed.bedCategory.id === filter.category : true);
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
			specialities: this.specialities,
			categories: this.categories
		};
	}

	private filterOptions(bedsSummary: BedSummaryDto[]): void {
		bedsSummary.forEach(bedSummary => {

			this.sectors = pushIfNotExists(this.sectors, {sectorId: bedSummary.sector.id, sectorDescription: bedSummary.sector.description}, this.compareSector);

			this.specialities = pushIfNotExists(this.specialities, {specialityId: bedSummary.clinicalSpecialty.id, specialityDescription: bedSummary.clinicalSpecialty.name}, this.compareSpeciality)

			this.categories = pushIfNotExists(this.categories, {categoryId: bedSummary.bedCategory.id, categoryDescription: bedSummary.bedCategory.description}, this.compareCategory);
		});
	}

	private compareSector(sector: Sector, sector2: Sector): boolean {
		return sector.sectorId === sector2.sectorId;
	}

	private compareSpeciality(speciality: Speciality, speciality2: Speciality): boolean {
		return speciality.specialityId === speciality2.specialityId;
	}

	private compareCategory(category: Category, category2: Category): boolean {
		return category.categoryId === category2.categoryId;
	}

}

export class Sector {
	sectorId: number;
	sectorDescription: string;
}

export class Speciality {
	specialityId: number;
	specialityDescription: string;
}

export class Category {
	categoryId: number;
	categoryDescription: string;
}

