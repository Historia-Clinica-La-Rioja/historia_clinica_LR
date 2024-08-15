import { Injectable } from '@angular/core';
import { SaveMedicationStatementInstitutionalSupplyDto, SaveMedicationStatementInstitutionalSupplyMedicationDto } from '@api-rest/api-model';
import { Pharmaco } from '../components/pharmacos-to-dispense/pharmacos-to-dispense.component';

@Injectable({
  	providedIn: 'root'
})
export class MedicationToDispenseService {

	private saveMedicationStatementInstitutionalSupplyDto: SaveMedicationStatementInstitutionalSupplyDto;

	get saveMedicationStatement(): SaveMedicationStatementInstitutionalSupplyDto {
        return this.saveMedicationStatementInstitutionalSupplyDto;
    }

	mapToSaveMedicationStatementInstitutionalSupplyDto = (pharmacos: Pharmaco[], id: number) => {
		this.saveMedicationStatementInstitutionalSupplyDto = {
			medicationStatementId: id,
			medications: this.mapToSaveMedicationStatementInstitutionalSupplyMedicationDto(pharmacos)
		}
	}	

	private mapToSaveMedicationStatementInstitutionalSupplyMedicationDto = (pharmacos: Pharmaco[]): SaveMedicationStatementInstitutionalSupplyMedicationDto[]  => {
		return pharmacos.map((pharmaco: Pharmaco) => 
			{
				return {
					pt: pharmaco.snomed?.pt,
					sctid: pharmaco.snomed?.sctid,
					quantity: Number(pharmaco.quantity)
				}
			})
	}
}
