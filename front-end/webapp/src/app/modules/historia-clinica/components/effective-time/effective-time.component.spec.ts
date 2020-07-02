import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EffectiveTimeComponent } from './effective-time.component';

describe('EffectiveTimeComponent', () => {
  let component: EffectiveTimeComponent;
  let fixture: ComponentFixture<EffectiveTimeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EffectiveTimeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EffectiveTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
