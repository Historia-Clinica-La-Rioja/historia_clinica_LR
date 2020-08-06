import { TestBed } from '@angular/core/testing';

import { BedManagmentService } from './bed-managment.service';

describe('BedManagmentService', () => {
  let service: BedManagmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BedManagmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
