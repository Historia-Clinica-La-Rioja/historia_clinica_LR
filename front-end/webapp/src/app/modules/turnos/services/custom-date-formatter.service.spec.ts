import { TestBed } from '@angular/core/testing';

import { CustomDateFormatter } from './custom-date-formatter.service';

describe('CustomDateFormatterService', () => {
	let service: CustomDateFormatter;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(CustomDateFormatter);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
