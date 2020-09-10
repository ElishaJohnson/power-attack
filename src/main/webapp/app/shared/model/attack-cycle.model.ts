import { IAttack } from 'app/shared/model/attack.model';
import { ICharacter } from 'app/shared/model/character.model';

export interface IAttackCycle {
  id?: number;
  name?: string;
  attacks?: IAttack[];
  character?: ICharacter;
}

export class AttackCycle implements IAttackCycle {
  constructor(public id?: number, public name?: string, public attacks?: IAttack[], public character?: ICharacter) {}
}
