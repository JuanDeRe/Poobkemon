package src.domain;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * Representa el campo de batalla donde se desarrollan los combates entre entrenadores.
 * Gestiona el estado del clima, los efectos residuales, y la ejecución de turnos.
 * @author Palacios-Roa
 * @version 1.0
 */
public class BattleField {

	private boolean turn; // true = trainer2, false = trainer1

	private String weather;

	private int weatherTurnsRemaining;

	private Trainer trainer1;

	private Trainer trainer2;

	private Pokemon activePokemon1;

	private Pokemon activePokemon2;

	private List<Notification> notifications;

	/**
	 * Construye un nuevo campo de batalla con dos entrenadores
	 * @param trainer1 Primer entrenador en el combate
	 * @param trainer2 Segundo entrenador en el combate
	 */
	public BattleField(Trainer trainer1, Trainer trainer2) {
		this.trainer1 = trainer1;
		this.trainer2 = trainer2;
		this.activePokemon1 = trainer1.getActivePokemon();
		this.activePokemon2 = trainer2.getActivePokemon();
		this.turn = false;
		this.weather = "None";
		this.weatherTurnsRemaining = 0;
		this.notifications = new ArrayList<>();
	}
	/**
	 * Registra una notificación de evento en el campo de batalla
	 * @param notification Notificación a registrar
	 */
	public void notify(Notification notification) {
		this.notifications.add(notification);
	}
	/**
	 * Obtiene la lista de notificaciones generadas durante el turno
	 * @return Lista de notificaciones pendientes
	 */
	public List<Notification> getNotifications() {
		return notifications;
	}
	/**
	 * Ejecuta un turno completo con las acciones de ambos entrenadores
	 * @param action1 Acción del primer entrenador
	 * @param action2 Acción del segundo entrenador
	 * @return Lista de notificaciones generadas durante el turno
	 */
	public List<Notification> playTurn(Action action1, Action action2) {
		List<Action> orden = List.of(action1, action2).stream()
				.sorted(
						Comparator.comparingInt(Action::getPriority).reversed()
								.thenComparing(Comparator.comparingInt(Action::getSpeed).reversed())
				).toList();

		for (Action action : orden) {
			action.execute(this);
		}
		applyWeatherResidual();     // daño de arena, granizo…
		applyStatusResiduals();     // veneno, quemadura, sueño…
		//advanceMultiTurnPhase();    // pasa de fase carga → ataque para movimientos multi-turno
		decrementWeather();         // reduce contador de clima y limpia si llega a 0
		List<Notification> log = new ArrayList<>(notifications);
		notifications.clear();
		return log;
	}

	private void decrementWeather() {
		this.weatherTurnsRemaining--;
		if (this.weatherTurnsRemaining == 0) {
			this.weather = "None";
		}
	}

	private void applyStatusResiduals() {
		int receivedDamage = 0;
		boolean gainedMobility = false;
		boolean confusedFaded= false;
		if(this.activePokemon1.getStatusEffect().equals("Burn") || this.activePokemon1.getStatusEffect().equals("Poison")){
			receivedDamage = TypesTable.calculateStatusDamage(this,this.activePokemon1);
			this.activePokemon1.receiveDamage(receivedDamage);
		}
		else if(!this.activePokemon1.getStatusEffect().equals("None")){
			gainedMobility = this.activePokemon1.gainsMobility();
		}
		if (this.activePokemon1.confusionFades()){
			confusedFaded = true;
		}
		if (receivedDamage != 0 || gainedMobility || confusedFaded) {
			notify(new StatusNotification(this.activePokemon1.getName(),this.activePokemon1.getStatusEffect(), gainedMobility,this.activePokemon1.getIsConfused(),confusedFaded));
		}
		if(this.activePokemon2.getStatusEffect().equals("Burn") || this.activePokemon2.getStatusEffect().equals("Poison")){
			receivedDamage = TypesTable.calculateStatusDamage(this,this.activePokemon2);
			this.activePokemon2.receiveDamage(receivedDamage);
		}
		else if(!this.activePokemon2.getStatusEffect().equals("None")){
			gainedMobility = this.activePokemon2.gainsMobility();
		}
		if (this.activePokemon2.confusionFades()){
			confusedFaded = true;
		}
		if (receivedDamage != 0 || gainedMobility || confusedFaded) {
			notify(new StatusNotification(this.activePokemon2.getName(),this.activePokemon2.getStatusEffect(), gainedMobility,this.activePokemon2.getIsConfused(),confusedFaded));
		}

	}

	private void applyWeatherResidual() {
		int receivedDamage;
		receivedDamage = TypesTable.calculateWeatherDamage(this,this.activePokemon1);
		if (receivedDamage != 0) {
			this.activePokemon1.receiveDamage(receivedDamage);
			notify(new WeatherNotification(this.activePokemon1.getName(),this.weather,true,true));
		}
		receivedDamage = TypesTable.calculateWeatherDamage(this,this.activePokemon2);
		if (receivedDamage != 0) {
			this.activePokemon2.receiveDamage(receivedDamage);
			notify(new WeatherNotification(this.activePokemon2.getName(),this.weather,true,true));
		}
	}
	/**
	 * Verifica si el combate ha terminado
	 * @return true si todos los Pokémon de algún equipo están debilitados
	 */
	public boolean isOver() {
		boolean over = true;
		for(Pokemon p: trainer1.getTeam()){
			if(p.isAlive()){
				over = false;
			}
		}
		for(Pokemon p: trainer2.getTeam()){
			if(p.isAlive()){
				over = false;
			}
		}
		return over;
	}
	/**
	 * Cambia el estado climático del campo de batalla
	 * @param weather Nuevo tipo de clima a aplicar
	 */
	public void changeWeather(String weather) {
		this.weather = weather;
		this.weatherTurnsRemaining = 5;
		notify(new WeatherNotification("",this.weather, true,false));
	}



	public Pokemon getOpponent(Pokemon pokemon) {
		if (pokemon == this.activePokemon1) {
			return this.activePokemon2;
		}
		else {
			return this.activePokemon1;
		}
    }
	/**
	 * Actualiza las referencias de los Pokémon activos
	 */
	public void updateActives(){
		this.activePokemon1 = trainer1.getActivePokemon();
		this.activePokemon2 = trainer2.getActivePokemon();
	}
	// Getters para entrenadores
	public Machine getCurrentOpponent() {
		return (Machine)trainer2;
	}

	public Trainer getTrainer1() {
		return this.trainer1;
	}

	public Trainer getTrainer2() {
		return this.trainer2;
	}
	/**
	 * Obtiene el clima actual del campo de batalla
	 * @return String con el tipo de clima activo
	 */
	public String getWeather() {
		return this.weather;
	}
}
