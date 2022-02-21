import { NursingConsultationDto } from '@api-rest/api-model';
import { TranslateService } from '@ngx-translate/core';

export class NewNurseConsultationSuggestedFieldsService {

  public nonCompletedFields: string[] = [];
  public presentFields: string[] = [];

  constructor(
    private nursingConsultationDto: NursingConsultationDto,
    private readonly translateService: TranslateService,
  ) {

    this.nursingConsultationDto.anthropometricData.weight ?
      this.addTranslated('ambulatoria.paciente.nueva-consulta.PESO', this.presentFields) :
      this.addTranslated('ambulatoria.paciente.nueva-consulta.PESO', this.nonCompletedFields);

    this.nursingConsultationDto.anthropometricData.height ?
      this.addTranslated('ambulatoria.paciente.nueva-consulta.TALLA', this.presentFields) :
      this.addTranslated('ambulatoria.paciente.nueva-consulta.TALLA', this.nonCompletedFields);

    this.nursingConsultationDto.riskFactors.systolicBloodPressure ?
      this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_SISTOLICA', this.presentFields) :
      this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_SISTOLICA', this.nonCompletedFields);

    this.nursingConsultationDto.riskFactors.diastolicBloodPressure ?
      this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_DIASTOLICA', this.presentFields) :
      this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_DIASTOLICA', this.nonCompletedFields);
  }

  private addTranslated(translationKey: string, fields: string[]) {
    this.translateService.get(translationKey).subscribe(traduccion =>
      fields.push(traduccion)
    );
  }

}
