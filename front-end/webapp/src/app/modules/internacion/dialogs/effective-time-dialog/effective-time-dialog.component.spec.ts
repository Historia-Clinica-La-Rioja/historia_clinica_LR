import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EffectiveTimeDialogComponent } from './effective-time-dialog.component';

describe('EffectiveTimeDialogComponent', () => {
  let component: EffectiveTimeDialogComponent;
  let fixture: ComponentFixture<EffectiveTimeDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EffectiveTimeDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EffectiveTimeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
