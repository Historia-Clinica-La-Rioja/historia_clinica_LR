import { TestBed } from '@angular/core/testing';

import { TriageMasterDataService } from './triage-master-data.service';

describe('TriageMasterDataService', () => {
  let service: TriageMasterDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TriageMasterDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
