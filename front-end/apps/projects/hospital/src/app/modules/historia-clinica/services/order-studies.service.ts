import { SnomedDto } from '@api-rest/api-model';
import { removeFrom } from '@core/utils/array.utils';

export interface Study {
	snomed: SnomedDto;
}

export class OrderStudiesService {

	private data: any[];

	constructor() {
		this.data = [];
	}

	add(study: Study): boolean {
		const alreadyPresent = this.data.some(s => s.snomed.sctid === study.snomed.sctid && s.snomed.pt === study.snomed.pt);
		if (!alreadyPresent) {
			this.addAll([study]);
		}
		return !alreadyPresent;
	}

	addAll(studies: Study[]) {
		this.data = this.data.concat(studies);
	}

	getStudies(): Study[] {
		return this.data;
	}

	remove(index: number): void {
		this.data = removeFrom<Study>(this.data, index);
	}

}
