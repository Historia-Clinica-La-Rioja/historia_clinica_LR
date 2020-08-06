import { tap } from 'rxjs/operators';
import { BedSummaryDto } from '@api-rest/api-model';
import { BedService } from '@api-rest/services/bed.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { BedManagmentFilter } from '../components/bed-filters/bed-filters.component';
import { momentParseDateTime } from '@core/utils/moment.utils';
import { pushIfNotExists } from '@core/utils/array.utils';

@Injectable()
export class BedManagmentService {

	public sectors: Sector[] = [];
	public specialities: Speciality[] = [];
	public categories: Category[] = [];

	private subject = new BehaviorSubject<BedSummaryDto[]>(null);
	private originalBedManagment: BedSummaryDto[] = [];

	constructor(
		private bedService: BedService
  	) { }

	public getBedManagment(): Observable<BedSummaryDto[]> {
		if (!this.originalBedManagment.length) {
			this.bedService.getBedsSummary().pipe(
				tap((bedsSummary: BedSummaryDto[]) => this.filterOptions(bedsSummary))
			).subscribe(data => {
				this.originalBedManagment = data;
				this.sendBedManagment(this.originalBedManagment);
			});
		}
		return this.subject.asObservable();
	}

	public sendBedManagment(bedManagment: BedSummaryDto[]) {
		this.subject.next(bedManagment);
	}

	public sendBedManagmentFilter(newFilter: BedManagmentFilter) {
		const bedManagmentCopy = [...this.originalBedManagment];
		const result = bedManagmentCopy.filter(bedManagment => ((newFilter.sector ? bedManagment.sector.id === newFilter.sector : true)
																		&& (newFilter.speciality ? bedManagment.clinicalSpecialty.id === newFilter.speciality : true)
																		&& (newFilter.category ? bedManagment.bedCategory.id === newFilter.category : true)
																		&& (newFilter.probableDischargeDate ? bedManagment.probableDischargeDate ? momentParseDateTime(bedManagment.probableDischargeDate).toDate() < momentParseDateTime(newFilter.probableDischargeDate).toDate() : false : true)
																		&& (newFilter.filled ? true : bedManagment.bed.free)));
		this.subject.next(result);
	}

	public getFilterOptions() {
		return {
			sectors: this.sectors,
			specialities: this.specialities,
			categories: this.categories
		};
	}

	private filterOptions(bedsSummary: BedSummaryDto[]) {
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
