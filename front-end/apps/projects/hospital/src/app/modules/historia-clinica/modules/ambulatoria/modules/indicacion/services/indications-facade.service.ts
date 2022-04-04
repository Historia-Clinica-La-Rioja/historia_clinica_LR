import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from "rxjs";
import { DietDto, OtherIndicationDto } from "@api-rest/api-model";
import { InternmentIndicationService } from "@api-rest/services/internment-indication.service";
import { Observable } from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class IndicationsFacadeService {

	private internmentEpisodeId: number;
	private dietsSubject: Subject<any> = new BehaviorSubject<DietDto[]>([]);

	private otherIndecationsSubject: Subject<any> = new BehaviorSubject<OtherIndicationDto[]>([]);
	readonly diets$ = this.dietsSubject.asObservable();

	readonly otherIndications$ = this.otherIndecationsSubject.asObservable();

	constructor(
		private readonly internmentIndicationService: InternmentIndicationService
	) { }

	addDiet(diet: DietDto): Observable<any> {
		return this.internmentIndicationService.addDiet(diet, this.internmentEpisodeId);
	}

	addOtherIndication(otherIndication: OtherIndicationDto): Observable<any> {
		return this.internmentIndicationService.addOtherIndication(otherIndication, this.internmentEpisodeId);
	}

	updateIndication(updateIndication: UpdateIndication) {
		if (updateIndication.diets) {
			this.internmentIndicationService.getInternmentEpisodeDiets(this.internmentEpisodeId).subscribe(d => this.dietsSubject.next(d));
		}
		if (updateIndication.otherIndication) {
			this.internmentIndicationService.getInternmentEpisodeOtherIndications(this.internmentEpisodeId).subscribe(d => this.otherIndecationsSubject.next(d));
		}

	}

	setInternmentEpisodeId(id: number) {
		this.internmentEpisodeId = id;
		this.updateIndication({
			diets: true,
			otherIndication: true
		})
	}
}

export interface UpdateIndication {
	diets?: boolean,
	parenteralPlan?: boolean,
	pharmaco?: boolean,
	otherIndication?: boolean
}
