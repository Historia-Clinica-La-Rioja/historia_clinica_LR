import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApiOverlayDelayComponent } from './api-overlay-delay.component';

describe('ApiOverlayDelayComponent', () => {
  let component: ApiOverlayDelayComponent;
  let fixture: ComponentFixture<ApiOverlayDelayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApiOverlayDelayComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApiOverlayDelayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
