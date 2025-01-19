import { Pipe, PipeTransform } from '@angular/core';
import { ProfessionalDto } from '@api-rest/api-model';

@Pipe({
  name: 'professionalFullName'
})
export class ProfessionalFullNamePipe implements PipeTransform {

  transform(value: ProfessionalDto, ffIsOn: boolean): string {
    const otherLastNames = value.otherLastNames || ''
    const middleNames = value.middleNames || ''
    const baseNameProffesionalFF = value.nameSelfDetermination + " " + value.lastName
    if (ffIsOn) {
      const hasNameProffesionalFF = !!value.nameSelfDetermination
      return hasNameProffesionalFF ? baseNameProffesionalFF + " " + otherLastNames :
        value.firstName + " " + middleNames + " " + value.lastName + " " + otherLastNames
    }
    return value.firstName + " " + middleNames + " " + value.lastName + " " + otherLastNames
  }

}