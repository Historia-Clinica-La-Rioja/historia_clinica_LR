import { ProcedureOrder } from "@historia-clinica/modules/odontologia/services/actions.service";

export class ShowActionsService {

	public secondProcedure: boolean;
	public thirdProcedure: boolean;
	public isNotPreviousProcedureSet = true;


	constructor(secondProcedure, thirdProcedure, isNotPreviousProcedureSet) {
		this.secondProcedure = secondProcedure ? true : false;
		this.thirdProcedure = thirdProcedure ? true : false;
		this.isNotPreviousProcedureSet = (isNotPreviousProcedureSet === -1 || isNotPreviousProcedureSet === 2) ? true : false;
	}

	public getSecondProcedure(): boolean {
		return this.secondProcedure;
	}

	public getThirdProcedure(): boolean {
		return this.thirdProcedure;
	}

	public getIsNotPreviousProcedureSet(): boolean {
		return this.isNotPreviousProcedureSet;
	}

	public setSecondProcedure(showSecondProcedure: boolean): void {
		this.secondProcedure = showSecondProcedure;
	}

	public setThirdProcedure(showThirdProcedure: boolean): void {
		this.thirdProcedure = showThirdProcedure;
	}

	public setIsNotPreviousProcedureSet(isNotPreviousProcedure: boolean): void {
		this.isNotPreviousProcedureSet = isNotPreviousProcedure;
	}
	

	public showProcedures(order: ProcedureOrder, isANewProcedure: boolean): void {
		if (order === ProcedureOrder.SECOND) {
			if (isANewProcedure){
				this.secondProcedure = true;
				this.thirdProcedure = true;
			}
			else{
				this.thirdProcedure = false;
			}
		}
		if (order === ProcedureOrder.FIRST) {
			if (isANewProcedure){
				this.secondProcedure = true;
			}
			else {
				this.secondProcedure = false;
			}
		}
	}

}

