import { TestBed } from '@angular/core/testing';

import { BedManagementFacadeService } from './bed-management-facade.service';

describe('BedManagementFacadeService', () => {
  let service: BedManagementFacadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BedManagementFacadeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
