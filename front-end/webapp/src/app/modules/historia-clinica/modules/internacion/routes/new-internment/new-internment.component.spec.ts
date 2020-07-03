import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewInternmentComponent } from './new-internment.component';

describe('NewInternmentComponent', () => {
  let component: NewInternmentComponent;
  let fixture: ComponentFixture<NewInternmentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewInternmentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewInternmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
