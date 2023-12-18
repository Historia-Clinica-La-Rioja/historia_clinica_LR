import { Pipe, PipeTransform } from '@angular/core';
import { DoctorInfoDto } from '@api-rest/api-model';

@Pipe({
  name: 'professionalFullName'
})
export class ProfessionalFullNamePipe implements PipeTransform {

  transform(value: DoctorInfoDto): string {
    if (value.nameSelfDetermination) {
      return value.nameSelfDetermination + " " + value.lastName
    }
    return value.firstName + " " + value.lastName
  }

}