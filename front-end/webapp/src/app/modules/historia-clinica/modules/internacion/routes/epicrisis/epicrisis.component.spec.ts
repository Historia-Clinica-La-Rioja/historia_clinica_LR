import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EpicrisisComponent } from './epicrisis.component';

describe('EpicrisisComponent', () => {
  let component: EpicrisisComponent;
  let fixture: ComponentFixture<EpicrisisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EpicrisisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EpicrisisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
