import { pushIfNotExists, removeFrom } from "@core/utils/array.utils";
import { Observable, Subject } from "rxjs";

export class ProfessionalService {
	private professionals: Professional[] = [];
	private readonly proffesionalsEmmiter = new Subject<Professional[]>();
	professionals$: Observable<Professional[]> = this.proffesionalsEmmiter.asObservable();

	constructor() {}

	add(professional: Professional): boolean {
		const currentItems = this.professionals.length;
		this.professionals = pushIfNotExists<Professional>(this.professionals, professional, this.compareHealthcareProfessionalId);
		this.proffesionalsEmmiter.next(this.professionals);
		return currentItems === this.professionals.length;
	}

	compareHealthcareProfessionalId(data: Professional, data1: Professional): boolean {
		return data.healthcareProfessionalId === data1.healthcareProfessionalId;
	}

	remove(index: number): void {
		this.professionals = removeFrom<Professional>(this.professionals, index);
		this.proffesionalsEmmiter.next(this.professionals);
	}

	getProfessionals(): Professional[] {
		return this.professionals
	}

	isEmpty(): boolean {
		return (!this.professionals || this.professionals.length === 0);
	}

}
export interface Professional {
	completeName: string,
	healthcareProfessionalId: number,
	speciality: string,
	license: string,
}