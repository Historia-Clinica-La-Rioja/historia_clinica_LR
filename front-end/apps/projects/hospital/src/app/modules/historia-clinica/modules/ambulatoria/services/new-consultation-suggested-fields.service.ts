import { CreateOutpatientDto } from "@api-rest/api-model";
import { TranslateService } from "@ngx-translate/core";

export class NewConsultationSuggestedFieldsService {


	public nonCompletedFields: string[] = [];
	public presentFields: string[] = [];

	constructor(
		private nuevaConsulta: CreateOutpatientDto,
		private readonly translateService: TranslateService,
	) {

		this.nuevaConsulta.reasons?.length ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.MOTIVO', this.presentFields) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.MOTIVO', this.nonCompletedFields);

		this.nuevaConsulta.problems?.length ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.PROBLEMA', this.presentFields) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.PROBLEMA', this.nonCompletedFields);
		this.nuevaConsulta.anthropometricData.weight ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.PESO', this.presentFields) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.PESO', this.nonCompletedFields);

		this.nuevaConsulta.anthropometricData.height ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TALLA', this.presentFields) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TALLA', this.nonCompletedFields);

		this.nuevaConsulta.riskFactors.systolicBloodPressure ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_SISTOLICA', this.presentFields) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_SISTOLICA', this.nonCompletedFields);

		this.nuevaConsulta.riskFactors.diastolicBloodPressure ?
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_DIASTOLICA', this.presentFields) :
			this.addTranslated('ambulatoria.paciente.nueva-consulta.TENSION_DIASTOLICA', this.nonCompletedFields);

	}

	private addTranslated(translationKey: string, fields: string[]) {
		this.translateService.get(translationKey).subscribe(traduccion =>
			fields.push(traduccion)
		);
	}

}
