import { TestBed } from '@angular/core/testing';

import { ClinicalSpecialtySectorService } from './clinical-specialty-sector.service';

describe('ClinicalSpecialtySectorService', () => {
  let service: ClinicalSpecialtySectorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClinicalSpecialtySectorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
