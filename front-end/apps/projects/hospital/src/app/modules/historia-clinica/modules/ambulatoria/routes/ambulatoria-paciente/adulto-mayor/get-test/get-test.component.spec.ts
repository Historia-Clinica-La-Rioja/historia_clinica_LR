import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetTestComponent } from './get-test.component';

describe('GetTestComponent', () => {
  let component: GetTestComponent;
  let fixture: ComponentFixture<GetTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GetTestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GetTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
