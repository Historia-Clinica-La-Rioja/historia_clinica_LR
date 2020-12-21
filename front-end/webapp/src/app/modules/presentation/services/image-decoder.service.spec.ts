import { TestBed } from '@angular/core/testing';

import { ImageDecoderService } from './image-decoder.service';

describe('ImageDecoderService', () => {
  let service: ImageDecoderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageDecoderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
