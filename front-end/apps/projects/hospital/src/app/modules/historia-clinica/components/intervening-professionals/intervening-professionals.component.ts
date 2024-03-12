import { Component, OnInit } from '@angular/core';
import { ELicenseNumberType, ElectronicJointSignatureInstitutionProfessionalDto } from '@api-rest/api-model';
import { ElectronicJointSignatureInstitutionalProfessionalLicenseService } from '@api-rest/services/electronic-joint-signature-institutional-professional-license.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
  selector: 'app-intervening-professionals',
  templateUrl: './intervening-professionals.component.html',
  styleUrls: ['./intervening-professionals.component.scss']
})
export class InterveningProfessionalsComponent implements OnInit {

  professionals: TypeaheadOption<any>[];
  constructor(private readonly electronicJointSignatureInstitutionalProfessionalLicenseService: ElectronicJointSignatureInstitutionalProfessionalLicenseService) { }

  ngOnInit(): void {
    this.electronicJointSignatureInstitutionalProfessionalLicenseService.getInstitutionalProfessionalsLicense().subscribe(professionals => {
      this.professionals = professionals.map(d => this.toProfessionalTypeahead(d));;
    })
  }

  setProfessional(event) {
  }

  private toProfessionalTypeahead(professionalDto: ElectronicJointSignatureInstitutionProfessionalDto): TypeaheadOption<any> {
    const licence = professionalDto.licenses[0].type === ELicenseNumberType.NATIONAL ? ', MN' : ', MP';
    const mas = professionalDto.licenses.length >= 2 ? ' (+' + (professionalDto.licenses.length - 1) + ' más)' : "";
    return {
      compareValue: professionalDto.completeName + licence + ' ' + professionalDto.licenses[0].number + ' ' + professionalDto.licenses[0].clinicalSpecialtyName + mas,
      value: professionalDto.healthcareProfessionalId,
    };
  }

}
