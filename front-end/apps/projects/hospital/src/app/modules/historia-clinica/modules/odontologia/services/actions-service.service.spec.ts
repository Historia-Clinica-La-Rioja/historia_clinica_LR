import { TestBed } from '@angular/core/testing';

import { ActionsServiceService } from './actions-service.service';

describe('ActionsServiceService', () => {
  let service: ActionsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ActionsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
