import { TestBed } from '@angular/core/testing';

import { InternacionMasterDataService } from './internacion-master-data.service';

describe('InternacionMasterDataService', () => {
  let service: InternacionMasterDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InternacionMasterDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
