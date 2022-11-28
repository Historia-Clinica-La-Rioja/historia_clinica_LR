import { Pipe, PipeTransform } from '@angular/core';
import { ProfessionalProfessionsDto } from '@api-rest/api-model';
import { ProfessionalLicenseNumberDto } from '@api-rest/api-model';
import { ProfessionalSpecialties } from '@pacientes/routes/profile/profile.component';

@Pipe({
	name: 'viewNameProfessionAndSpecialty'
})
export class ViewNameProfessionAndSpecialtyPipe implements PipeTransform {

	transform(ownProfessionsAndSpecialties: ProfessionalProfessionsDto[], item: ProfessionalLicenseNumberDto): string {

		let listProfessionsAndSpecialties = this.generateNamesList(ownProfessionsAndSpecialties);

		return listProfessionsAndSpecialties ? this.getProfessionName(listProfessionsAndSpecialties, item.professionalProfessionId) +
			this.getSpecialtyName(listProfessionsAndSpecialties, item.professionalProfessionId, item.healthcareProfessionalSpecialtyId) : null;
	}

	private generateNamesList(ownProfessionsAndSpecialties: any[]): ProfessionalSpecialties[] {
		return ownProfessionsAndSpecialties ? ownProfessionsAndSpecialties.map((e: ProfessionalProfessionsDto) => {
			return {
				profession: { description: e.profession.description, id: e.id },
				specialties: e.specialties.map(e => { return { name: e.clinicalSpecialty.name, id: e.id } })
			}
		}) : null;
	}

	private getProfessionName(listProfessionsAndSpecialties: ProfessionalSpecialties[], idPrefession: number): string {
		return listProfessionsAndSpecialties.find(e => e.profession.id === idPrefession).profession.description;
	}

	private getSpecialtyName(listProfessionsAndSpecialties: ProfessionalSpecialties[], idPrefession: number, idSpecialty: number): string {
		let nameSpecialtyName = idSpecialty ? listProfessionsAndSpecialties.find(e => e.profession.id === idPrefession).specialties.find(e => e.id == idSpecialty)?.name : '';
		return nameSpecialtyName ? '  (' + nameSpecialtyName + ')' : '';
	}
}
