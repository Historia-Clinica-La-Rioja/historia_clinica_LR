import { Component, OnInit } from '@angular/core';
import { ELicenseNumberType, ElectronicJointSignatureInstitutionProfessionalDto } from '@api-rest/api-model';
import { ElectronicJointSignatureInstitutionalProfessionalLicenseService } from '@api-rest/services/electronic-joint-signature-institutional-professional-license.service';
import { Professional, ProfessionalService } from '@historia-clinica/services/professionals.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
  selector: 'app-intervening-professionals',
  templateUrl: './intervening-professionals.component.html',
  styleUrls: ['./intervening-professionals.component.scss']
})
export class InterveningProfessionalsComponent implements OnInit {
  professionalsTypeaheadOption: TypeaheadOption<any>[];
  professionals: ElectronicJointSignatureInstitutionProfessionalDto[];
  initValueProfessional:TypeaheadOption<any>;
  constructor(private readonly electronicJointSignatureInstitutionalProfessionalLicenseService: ElectronicJointSignatureInstitutionalProfessionalLicenseService, public professionalsService: ProfessionalService
  ) { }

  ngOnInit(): void {
    this.electronicJointSignatureInstitutionalProfessionalLicenseService.getInstitutionalProfessionalsLicense().subscribe(professionals => {
      this.professionals = professionals;
      this.professionalsTypeaheadOption = this.professionals.map(d => this.toProfessionalTypeahead(d));
    })
  }

  setProfessional(value) {
    if(value){
      this.professionalsService.add(this.toProfessional(this.professionals.find(p => p.healthcareProfessionalId === value)));
      this.initValueProfessional = null;
    }
  }

  private toProfessional(professionalDto: ElectronicJointSignatureInstitutionProfessionalDto): Professional {
    const mas = professionalDto.licenses.length >= 2 ? ' (+' + (professionalDto.licenses.length - 1) + ' más)' : "";
    return {
      completeName: professionalDto.completeName,
      healthcareProfessionalId: professionalDto.healthcareProfessionalId,
      speciality: professionalDto.licenses[0].clinicalSpecialtyName + mas ,
      license: professionalDto.licenses[0].type === ELicenseNumberType.NATIONAL ? 'MN N°'+ professionalDto.licenses[0].number : 'MP N°' + professionalDto.licenses[0].number,
    };
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
