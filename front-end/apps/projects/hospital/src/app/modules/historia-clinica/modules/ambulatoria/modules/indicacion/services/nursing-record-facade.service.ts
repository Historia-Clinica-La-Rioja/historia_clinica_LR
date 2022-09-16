import { Injectable } from '@angular/core';
import { NursingRecordDto } from '@api-rest/api-model';
import { InternmentNursingRecordService } from '@api-rest/services/internment-nursing-record.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class NursingRecordFacadeService {

	private internmentEpisodeId: number;
	private nursingRecordsSubject = new BehaviorSubject<NursingRecordDto[]>([]);
	public readonly nursingRecords$ = this.nursingRecordsSubject.asObservable();

	constructor(
		private readonly internmentNursingRecordService: InternmentNursingRecordService
	) { }

	setInformation(id: number) {
		this.internmentEpisodeId = id;
		this.getNursingRecords();
	}

	getNursingRecords() {
		this.internmentNursingRecordService.getInternmentNursingRecords(this.internmentEpisodeId)
			.subscribe(nr => this.nursingRecordsSubject.next(nr));
	}

	changeStateOfNursingRecord(nursingRecordId: number, data: any): Observable<boolean> {
		return this.internmentNursingRecordService.updateNursingRecord(this.internmentEpisodeId, nursingRecordId, data);
	}
}
