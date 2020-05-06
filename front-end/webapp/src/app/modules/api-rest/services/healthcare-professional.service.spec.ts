import { TestBed } from '@angular/core/testing';

import { HealthcareProfessionalService } from './healthcare-professional.service';

describe('HealthcareProfessionalService', () => {
  let service: HealthcareProfessionalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HealthcareProfessionalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
