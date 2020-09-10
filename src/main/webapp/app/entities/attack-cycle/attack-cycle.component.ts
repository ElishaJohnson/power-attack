import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttackCycle } from 'app/shared/model/attack-cycle.model';
import { AttackCycleService } from './attack-cycle.service';
import { AttackCycleDeleteDialogComponent } from './attack-cycle-delete-dialog.component';

@Component({
  selector: 'jhi-attack-cycle',
  templateUrl: './attack-cycle.component.html'
})
export class AttackCycleComponent implements OnInit, OnDestroy {
  attackCycles?: IAttackCycle[];
  eventSubscriber?: Subscription;

  constructor(
    protected attackCycleService: AttackCycleService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.attackCycleService.query().subscribe((res: HttpResponse<IAttackCycle[]>) => (this.attackCycles = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAttackCycles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAttackCycle): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAttackCycles(): void {
    this.eventSubscriber = this.eventManager.subscribe('attackCycleListModification', () => this.loadAll());
  }

  delete(attackCycle: IAttackCycle): void {
    const modalRef = this.modalService.open(AttackCycleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attackCycle = attackCycle;
  }
}
