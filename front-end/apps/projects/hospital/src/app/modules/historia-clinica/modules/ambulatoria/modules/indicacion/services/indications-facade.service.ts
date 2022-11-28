import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject, Observable } from "rxjs";
import { DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto } from "@api-rest/api-model";
import { InternmentIndicationService } from "@api-rest/services/internment-indication.service";

@Injectable({
	providedIn: 'root'
})
export class IndicationsFacadeService {

	private internmentEpisodeId: number;
	private dietsSubject: Subject<any> = new BehaviorSubject<DietDto[]>([]);

	private otherIndecationsSubject: Subject<any> = new BehaviorSubject<OtherIndicationDto[]>([]);
	readonly diets$ = this.dietsSubject.asObservable();
	readonly otherIndications$ = this.otherIndecationsSubject.asObservable();
	private parenteralPlansSubject = new BehaviorSubject<ParenteralPlanDto[]>([]);
	readonly parenteralPlans$ = this.parenteralPlansSubject.asObservable();
	private pharmacoSubject = new BehaviorSubject<PharmacoDto[]>([]);
	readonly pharmacos$ = this.pharmacoSubject.asObservable();

	constructor(
		private readonly internmentIndicationService: InternmentIndicationService
	) { }

	addDiet(diet: DietDto): Observable<any> {
		return this.internmentIndicationService.addDiet(diet, this.internmentEpisodeId);
	}

	addOtherIndication(otherIndication: OtherIndicationDto): Observable<any> {
		return this.internmentIndicationService.addOtherIndication(otherIndication, this.internmentEpisodeId);
	}

	addParenteralPlan(parenteralPlan: ParenteralPlanDto): Observable<any> {
		return this.internmentIndicationService.addParenteralPlan(parenteralPlan, this.internmentEpisodeId);
	}

	addPharmaco(pharmaco: PharmacoDto): Observable<any> {
		return this.internmentIndicationService.addPharamaco(pharmaco, this.internmentEpisodeId);
	}

	updateIndication(updateIndication: UpdateIndication) {
		if (updateIndication.diets) {
			this.internmentIndicationService.getInternmentEpisodeDiets(this.internmentEpisodeId).subscribe(d => this.dietsSubject.next(d));
		}
		if (updateIndication.otherIndication) {
			this.internmentIndicationService.getInternmentEpisodeOtherIndications(this.internmentEpisodeId).subscribe(d => this.otherIndecationsSubject.next(d));
		}

		if (updateIndication.parenteralPlan) {
			this.internmentIndicationService.getInternmentEpisodeParenteralPlans(this.internmentEpisodeId).subscribe(p => this.parenteralPlansSubject.next(p));
		}
		if (updateIndication.pharmaco) {
			this.internmentIndicationService.getInternmentEpisodePharmacos(this.internmentEpisodeId).subscribe(p => this.pharmacoSubject.next(p));
		}
	}

	setInternmentEpisodeId(id: number) {
		this.internmentEpisodeId = id;
		this.updateIndication({
			diets: true,
			otherIndication: true,
			parenteralPlan: true,
			pharmaco: true
		})
	}
}

export interface UpdateIndication {
	diets?: boolean,
	parenteralPlan?: boolean,
	pharmaco?: boolean,
	otherIndication?: boolean
}
