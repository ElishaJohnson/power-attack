import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttack } from 'app/shared/model/attack.model';
import { AttackService } from './attack.service';
import { AttackDeleteDialogComponent } from './attack-delete-dialog.component';

@Component({
  selector: 'jhi-attack',
  templateUrl: './attack.component.html'
})
export class AttackComponent implements OnInit, OnDestroy {
  attacks?: IAttack[];
  eventSubscriber?: Subscription;

  constructor(protected attackService: AttackService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.attackService.query().subscribe((res: HttpResponse<IAttack[]>) => (this.attacks = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAttacks();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAttack): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAttacks(): void {
    this.eventSubscriber = this.eventManager.subscribe('attackListModification', () => this.loadAll());
  }

  delete(attack: IAttack): void {
    const modalRef = this.modalService.open(AttackDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attack = attack;
  }
}
