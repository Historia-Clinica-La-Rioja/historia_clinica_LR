import { TestBed } from '@angular/core/testing';

import { ServiceRequestService } from './service-request.service';

describe('NewServiceRequestService', () => {
  let service: ServiceRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
