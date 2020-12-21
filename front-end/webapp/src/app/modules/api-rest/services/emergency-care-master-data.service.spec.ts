import { TestBed } from '@angular/core/testing';

import { EmergencyCareMasterDataService } from './emergency-care-master-data.service';

describe('EmergencyCareMasterDataService', () => {
  let service: EmergencyCareMasterDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmergencyCareMasterDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
