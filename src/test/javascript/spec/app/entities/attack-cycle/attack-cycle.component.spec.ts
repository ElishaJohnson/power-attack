import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PowerattackTestModule } from '../../../test.module';
import { AttackCycleComponent } from 'app/entities/attack-cycle/attack-cycle.component';
import { AttackCycleService } from 'app/entities/attack-cycle/attack-cycle.service';
import { AttackCycle } from 'app/shared/model/attack-cycle.model';

describe('Component Tests', () => {
  describe('AttackCycle Management Component', () => {
    let comp: AttackCycleComponent;
    let fixture: ComponentFixture<AttackCycleComponent>;
    let service: AttackCycleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PowerattackTestModule],
        declarations: [AttackCycleComponent]
      })
        .overrideTemplate(AttackCycleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttackCycleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttackCycleService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new AttackCycle(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.attackCycles && comp.attackCycles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
