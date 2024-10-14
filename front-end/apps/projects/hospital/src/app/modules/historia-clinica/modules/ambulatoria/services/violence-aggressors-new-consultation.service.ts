import { Injectable } from '@angular/core';
import { ViolenceReportAggressorDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ViolenceAggressorsNewConsultationService {

	violenceAggressors$ = new BehaviorSubject<CustomViolenceReportAggressorDto[]>([]);
	violenceAggressors: CustomViolenceReportAggressorDto[] = [];

	addToList(aggressor: CustomViolenceReportAggressorDto) {
		this.violenceAggressors.push(aggressor);
		this.violenceAggressors$.next(this.violenceAggressors);
	}

	removeAggressor(index: number) {
		this.violenceAggressors.splice(index, 1);
		this.violenceAggressors$.next(this.violenceAggressors);
	}
	
	reset(){
		this.violenceAggressors = [];
		this.violenceAggressors$.next(this.violenceAggressors);
	}
}

export interface CustomViolenceReportAggressorDto extends ViolenceReportAggressorDto{
	descriptionMunicipality: string,
	descriptionLocality: string,
	canBeDeleted?: boolean
}

