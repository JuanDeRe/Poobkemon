package src.domain;

import java.io.Serializable;

/**
 * Concrete action representing a Pokémon switch during battle.
 * Handles the logistics of changing the active Pokémon and updating battle state.
 * @author Palacios-Roa
 * @version 1.0
 */
public class SwitchAction implements Action, Serializable {

	private int teamMember;

	private Trainer trainer;
	/**
	 * Constructs a switch action with target Pokémon selection.
	 * @param teamMember Index of the Pokémon in the trainer's team to switch to (0-based).
	 * @param trainer Trainer executing the switch. Must control the target Pokémon team.
	 */
	public SwitchAction(int teamMember, Trainer trainer){
		this.trainer = trainer;
		this.teamMember = teamMember;
	}
	/**
	 * Gets the execution priority for switch actions.
	 * @return Fixed priority of 20, giving switches higher precedence than most actions.
	 */
	public int getPriority(){
		return 20;
	}
	/**
	 * Gets action speed (irrelevant for switches).
	 * @return 0, as speed stat doesn't affect switch order resolution.
	 */
	public int getSpeed(){
		return 0;
	}
	/**
	 * Executes the full switch sequence:
	 * 1. Marks current active Pokémon as inactive
	 * 2. Changes to selected team member
	 * 3. Updates battlefield state
	 * 4. Notifies observers about the switch
	 * @param field Battle context where the switch occurs
	 * @implNote Ensures:
	 *           - Previous active Pokémon status is cleared
	 *           - BattleField synchronizes active Pokémon states
	 *           - SwitchNotification contains new active's name and trainer
	 */
	public void execute(BattleField field){
		trainer.getActivePokemon().notActive();
		trainer.changePokemon(teamMember);
		field.updateActives();
		field.notify(new SwitchNotification(trainer.getActivePokemon().getName(),trainer.getName()));
	}

}
