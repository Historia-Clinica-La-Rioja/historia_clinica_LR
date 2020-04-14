import { TestBed } from '@angular/core/testing';

import { SearchPatientService } from './search-patient.service';

describe('SearchPatientService', () => {
  let service: SearchPatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchPatientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
