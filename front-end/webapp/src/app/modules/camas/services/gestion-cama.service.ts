import { tap } from 'rxjs/operators';
import { BedSummaryDto } from '@api-rest/api-model';
import { BedService } from '@api-rest/services/bed.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { BedManagmentFilter } from '../components/filtros-camas/filtros-camas.component';
import { momentParseDateTime } from '@core/utils/moment.utils';

@Injectable({
  providedIn: 'root'
})
export class GestionCamaService {

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
			if (this.sectors.map(s => s.sectorId).indexOf(bedSummary.sector.id) === -1) {
				this.sectors.push({sectorId: bedSummary.sector.id, sectorDescription: bedSummary.sector.description});
			}

			if (this.specialities.map(s => s.specialityId).indexOf(bedSummary.clinicalSpecialty.id) === -1) {
			this.specialities.push({specialityId: bedSummary.clinicalSpecialty.id, specialityDescription: bedSummary.clinicalSpecialty.name});
			}

			if (this.categories.map(c => c.categoryId).indexOf(bedSummary.bedCategory.id) === -1) {
				this.categories.push({categoryId: bedSummary.bedCategory.id, categoryDescription: bedSummary.bedCategory.description});
			}
		});
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
