import { TestBed } from '@angular/core/testing';

import { AddressMasterDataService } from './address-master-data.service';

describe('AddressMasterDataService', () => {
  let service: AddressMasterDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddressMasterDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
