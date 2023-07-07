import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestEdmontonPopupComponent } from './test-edmonton-popup.component';

describe('TestEdmontonPopupComponent', () => {
  let component: TestEdmontonPopupComponent;
  let fixture: ComponentFixture<TestEdmontonPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TestEdmontonPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestEdmontonPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
