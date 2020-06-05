import { TestBed } from '@angular/core/testing';

import { DocumentSearchService } from './document-search.service';

describe('DocumentSearchService', () => {
  let service: DocumentSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
