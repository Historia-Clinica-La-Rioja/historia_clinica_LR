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
