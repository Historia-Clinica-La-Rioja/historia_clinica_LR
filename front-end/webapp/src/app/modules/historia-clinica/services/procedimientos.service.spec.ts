import { TestBed } from '@angular/core/testing';

import { Procedimientos } from './procedimientos.service';

describe('Procedimientos', () => {
	let service: Procedimientos;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(Procedimientos);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
