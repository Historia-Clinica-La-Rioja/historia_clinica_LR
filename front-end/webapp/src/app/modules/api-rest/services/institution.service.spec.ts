import { TestBed } from '@angular/core/testing';

import { InstitutionService } from './institution.service';

describe('InstitutionService', () => {
	let service: InstitutionService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(InstitutionService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
