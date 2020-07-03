import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EpicrisisFormComponent } from './epicrisis-form.component';

describe('EpicrisisFormComponent', () => {
  let component: EpicrisisFormComponent;
  let fixture: ComponentFixture<EpicrisisFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EpicrisisFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EpicrisisFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
