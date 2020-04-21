import { TestBed } from '@angular/core/testing';

import { PatientMasterDataService } from './patient-master-data.service';

describe('PatientMasterDataService', () => {
  let service: PatientMasterDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PatientMasterDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
