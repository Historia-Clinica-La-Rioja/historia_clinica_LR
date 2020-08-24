import { TestBed } from '@angular/core/testing';

import { BedManagementService } from './bed-management.service';

describe('BedManagementService', () => {
  let service: BedManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BedManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
