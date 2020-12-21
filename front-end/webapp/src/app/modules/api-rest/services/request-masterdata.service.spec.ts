import { TestBed } from '@angular/core/testing';

import { RequestMasterDataService } from './request-masterdata.service';

describe('RequestMasterDataService', () => {
  let service: RequestMasterDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RequestMasterDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
