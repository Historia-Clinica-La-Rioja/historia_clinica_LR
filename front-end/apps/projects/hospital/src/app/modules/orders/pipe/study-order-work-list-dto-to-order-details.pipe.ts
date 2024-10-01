import { Pipe, PipeTransform } from '@angular/core';
import { OrderDetails } from '../components/order-detail/order-detail.component';
import { ESourceType, EStudyType, StudyOrderWorkListDto } from '@api-rest/api-model';

@Pipe({
	name: 'studyOrderWorkListDtoToOrderDetails'
})
export class StudyOrderWorkListDtoToOrderDetailsPipe implements PipeTransform {


	transform(studyOrder: StudyOrderWorkListDto): OrderDetails {
		const date = studyOrder.deferredDate ? studyOrder.deferredDate : studyOrder.createdDate;
		const sourceTypeId = (studyOrder.sourceTypeId === ESourceType.HOSPITALIZATION) ? "Internación" : "Guardia";
		const studyType = (studyOrder.studyTypeId === EStudyType.URGENT) ? "Urgente" : null;
		return {
			status: studyOrder.status.description,
			sourceTypeId: sourceTypeId,
			EStudyType: studyType,
			snomedPt: studyOrder.snomed.pt,
			requiresTransfer: studyOrder.requiresTransfer,
			date: {
				date: {
					year: date.date.year,
					month: date.date.month,
					day: date.date.day,
				},
				time: {
					hours: date.time.hours,
					minutes: date.time.minutes,
					seconds: date.time.seconds,
				}
			}
		};
	}

}
