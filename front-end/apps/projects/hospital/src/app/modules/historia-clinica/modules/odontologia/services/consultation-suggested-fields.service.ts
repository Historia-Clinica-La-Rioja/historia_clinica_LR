import { OdontologyConsultationDto } from "@api-rest/api-model";

export class ConsultationSuggestedFieldsService {

	public nonCompletedFields: string[] = [];
	public presentFields: string[] = [];

	constructor(
		private odontologyDto: OdontologyConsultationDto,
	) {
		const reasons = 'Motivos de consulta';
		this.odontologyDto.reasons?.length ?
			this.presentFields.push(reasons) :
			this.nonCompletedFields.push(reasons);
	}
}
