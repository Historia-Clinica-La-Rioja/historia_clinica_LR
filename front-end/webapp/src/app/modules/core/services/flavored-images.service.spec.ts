import { TestBed } from '@angular/core/testing';

import { FlavoredImagesService } from './flavored-images.service';

describe('FlavoredImagesService', () => {
  let service: FlavoredImagesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlavoredImagesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
