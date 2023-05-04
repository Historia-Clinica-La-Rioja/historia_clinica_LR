import { Injectable } from '@angular/core';
import { DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto } from '@api-rest/api-model';
import { EmergencyCareIndicationService } from '@api-rest/services/emergency-care-indication.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareIndicationsFacadeService {

	private emergencyCareEpisodeId: number;
	private dietsSubject: Subject<any> = new BehaviorSubject<DietDto[]>([]);

	private otherIndecationsSubject: Subject<any> = new BehaviorSubject<OtherIndicationDto[]>([]);
	readonly diets$ = this.dietsSubject.asObservable();
	readonly otherIndications$ = this.otherIndecationsSubject.asObservable();
	private parenteralPlansSubject = new BehaviorSubject<ParenteralPlanDto[]>([]);
	readonly parenteralPlans$ = this.parenteralPlansSubject.asObservable();
	private pharmacoSubject = new BehaviorSubject<PharmacoDto[]>([]);
	readonly pharmacos$ = this.pharmacoSubject.asObservable();

	constructor(
		private readonly emergencyCareIndicationService: EmergencyCareIndicationService,
	) { }

	addDiet(diet: DietDto): Observable<any> {
		return this.emergencyCareIndicationService.addDiet(diet, this.emergencyCareEpisodeId);
	}

	addOtherIndication(otherIndication: OtherIndicationDto): Observable<any> {
		return this.emergencyCareIndicationService.addOtherIndication(otherIndication, this.emergencyCareEpisodeId);
	}

	addParenteralPlan(parenteralPlan: ParenteralPlanDto): Observable<any> {
		return this.emergencyCareIndicationService.addParenteralPlan(parenteralPlan, this.emergencyCareEpisodeId);
	}

	addPharmaco(pharmaco: PharmacoDto): Observable<any> {
		return this.emergencyCareIndicationService.addPharamaco(pharmaco, this.emergencyCareEpisodeId);
	}

	updateIndication(updateIndication: UpdateIndication) {
		if (updateIndication.diets) {
			this.emergencyCareIndicationService.getInternmentEpisodeDiets(this.emergencyCareEpisodeId).subscribe(d => this.dietsSubject.next(d));
		}
		if (updateIndication.otherIndication) {
			this.emergencyCareIndicationService.getInternmentEpisodeOtherIndications(this.emergencyCareEpisodeId).subscribe(d => this.otherIndecationsSubject.next(d));
		}

		if (updateIndication.parenteralPlan) {
			this.emergencyCareIndicationService.getInternmentEpisodeParenteralPlans(this.emergencyCareEpisodeId).subscribe(p => this.parenteralPlansSubject.next(p));
		}
		if (updateIndication.pharmaco) {
			this.emergencyCareIndicationService.getInternmentEpisodePharmacos(this.emergencyCareEpisodeId).subscribe(p => this.pharmacoSubject.next(p));
		}
	}

	setEmergencyCareEpisodeId(id: number) {
		this.emergencyCareEpisodeId = id;
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

