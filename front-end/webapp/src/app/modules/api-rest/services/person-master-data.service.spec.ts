import { TestBed } from '@angular/core/testing';

import { PersonMasterDataService } from './person-master-data.service';

describe('PersonMasterDataService', () => {
  let service: PersonMasterDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PersonMasterDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
