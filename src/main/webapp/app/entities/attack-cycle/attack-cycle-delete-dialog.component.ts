import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAttackCycle } from 'app/shared/model/attack-cycle.model';
import { AttackCycleService } from './attack-cycle.service';

@Component({
  templateUrl: './attack-cycle-delete-dialog.component.html'
})
export class AttackCycleDeleteDialogComponent {
  attackCycle?: IAttackCycle;

  constructor(
    protected attackCycleService: AttackCycleService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attackCycleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('attackCycleListModification');
      this.activeModal.close();
    });
  }
}
