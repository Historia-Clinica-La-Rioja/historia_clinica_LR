import { pushIfNotExists, removeFrom } from "@core/utils/array.utils";
import { BehaviorSubject, Observable } from "rxjs";

export class ProfessionalService {
	private professionals: Professional[] = [];
	private readonly proffesionalsEmmiter = new BehaviorSubject<Professional[]>(this.professionals);
	private readonly hasProfessional = new BehaviorSubject<boolean>(false);
	professionals$: Observable<Professional[]> = this.proffesionalsEmmiter.asObservable();
	hasProfessional$: Observable<boolean> = this.hasProfessional.asObservable();
	constructor() { }

	add(professional: Professional) {
		this.professionals = pushIfNotExists<Professional>(this.professionals, professional, this.compareHealthcareProfessionalId);
		this.proffesionalsEmmiter.next(this.professionals);
		this.hasProfessional.next(this.hasAddedProfessionals());
	}

	compareHealthcareProfessionalId(data: Professional, data1: Professional): boolean {
		return data.healthcareProfessionalId === data1.healthcareProfessionalId;
	}

	remove(index: number): void {
		this.professionals = removeFrom<Professional>(this.professionals, index);
		this.proffesionalsEmmiter.next(this.professionals);
		this.hasProfessional.next(this.hasAddedProfessionals());
	}

	getProfessionals(): Professional[] {
		return this.professionals
	}

	hasAddedProfessionals(): boolean {
		return this.professionals.length !== 0;
	}

}
export interface Professional {
	completeName: string,
	healthcareProfessionalId: number,
	speciality: string,
	license: string,
}