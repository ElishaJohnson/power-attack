import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICharacter } from 'app/shared/model/character.model';
import { CharacterService } from './character.service';

@Component({
  templateUrl: './character-delete-dialog.component.html'
})
export class CharacterDeleteDialogComponent {
  character?: ICharacter;

  constructor(protected characterService: CharacterService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.characterService.delete(id).subscribe(() => {
      this.eventManager.broadcast('characterListModification');
      this.activeModal.close();
    });
  }
}
