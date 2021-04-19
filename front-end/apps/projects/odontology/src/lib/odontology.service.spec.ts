import { TestBed } from '@angular/core/testing';

import { OdontologyService } from './odontology.service';

describe('OdontologyService', () => {
  let service: OdontologyService;

  beforeEach(() => {
	TestBed.configureTestingModule({});
	service = TestBed.inject(OdontologyService);
  });

  it('should be created', () => {
	expect(service).toBeTruthy();
  });
});
