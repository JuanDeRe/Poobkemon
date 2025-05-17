package src.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Representa un Pokémon con todas sus características de combate, estadísticas,
 * movimientos y mecanismos de estado. Gestiona la lógica de daño, curación,
 * modificadores de estadísticas y efectos de estado.
 * @author Palacios-Roa
 * @version 1.0
 */
public class Pokemon implements Serializable {

	private List<String> types;

	private String name;

	private int level,ps,maxPs;

	private int speed,speedBoost,attack,attackBoost,defense,defenseBoost,specialAttack,specialAttackBoost,specialDefense,specialDefenseBoost,evasionBoost,precisionBoost;

	private boolean isAlive;

	private Ability ability;

	private List<Move> moveSet;

	private String statusEffect;

	private int statusMobilityTimer;

	private String description;

	private boolean isShiny;

	private boolean isConfused;

	private int confusedCounter;
	/**
	 * Mapa de multiplicadores para precisión/evasión por nivel
	 * Keys: -6 to +6
	 */
	public static final Map<Integer, Double> STAGE_PRECISION_MULTIPLIERS = Map.ofEntries(
			Map.entry(-6, 0.3333),
			Map.entry(-5, 0.375),
			Map.entry(-4, 0.4286),
			Map.entry(-3, 0.5),
			Map.entry(-2, 0.6),
			Map.entry(-1, 0.75),
			Map.entry( 0, 1.0),
			Map.entry( 1, 1.3333),
			Map.entry( 2, 1.6667),
			Map.entry( 3, 2.0),
			Map.entry( 4, 2.3333),
			Map.entry( 5, 2.6667),
			Map.entry( 6, 3.0)
	);
	/**
	 * Mapa de multiplicadores para estadísticas de combate por nivel
	 * Keys: -6 to +6
	 */
	public static final Map<Integer, Double> STAGE_STATS_MULTIPLIERS = Map.ofEntries(
			Map.entry(-6, 0.25),
			Map.entry(-5, 0.28),
			Map.entry(-4, 0.33),
			Map.entry(-3, 0.40),
			Map.entry(-2, 0.50),
			Map.entry(-1, 0.66),
			Map.entry( 0, 1.00),
			Map.entry( 1, 1.50),
			Map.entry( 2, 2.00),
			Map.entry( 3, 2.50),
			Map.entry( 4, 3.00),
			Map.entry( 5, 3.50),
			Map.entry( 6, 4.00)
	);

	/**
	 * Constructor principal para crear un Pokémon con atributos base
	 * @param types Lista de tipos elementales del Pokémon
	 * @param name Nombre del Pokémon
	 * @param ps Puntos de salud iniciales
	 * @param ability Habilidad especial (no implementada en esta versión)
	 * @param speed Velocidad base
	 * @param attack Ataque físico base
	 * @param defense Defensa física base
	 * @param specialAttack Ataque especial base
	 * @param specialDefense Defensa especial base
	 * @param moveSet Lista inicial de movimientos
	 * @param level Nivel del Pokémon
	 * @param description Descripción flavor text
	 * @param isShiny Indica si es una variante shiny
	 */
	public Pokemon(List<String> types, String name, int ps, Ability ability, int speed, int attack, int defense, int specialAttack, int specialDefense, List<Move> moveSet, int level, String description, boolean isShiny) {
		this.types = types;
		this.name = name;
		this.level = level;
		this.ps = ps;
		this.maxPs = ps;
		this.isAlive = true;
		this.ability = ability;
		this.speed = speed;
		this.speedBoost = 0;
		this.attack = attack;
		this.attackBoost = 0;
		this.defense = defense;
		this.defenseBoost = 0;
		this.specialAttack = specialAttack;
		this.specialAttackBoost = 0;
		this.specialDefense = specialDefense;
		this.specialDefenseBoost = 0;
		this.evasionBoost = 0;
		this.precisionBoost = 0;
		this.moveSet = new ArrayList<>(moveSet);
		this.statusEffect = "none";
		this.description = description;
		this.isShiny = isShiny;
		this.isConfused = false;
		this.confusedCounter = 0;
		this.statusMobilityTimer = 0;
	}
	/**
	 * Crea una copia del Pokémon con nuevo moveSet y 5% de chance de ser shiny
	 * @param moveSet Nuevo conjunto de movimientos
	 * @return Nueva instancia clonada con movimientos actualizados
	 */
	public Pokemon copy(List<Move> moveSet) {
		boolean isShiny = false;
			Random randomNumber = new Random();
			int shinyNumber = randomNumber.nextInt(100);
			if(shinyNumber < 5){
				isShiny = true;
			}
		return new Pokemon(this,moveSet,isShiny);
	}

	private Pokemon(Pokemon proto, List<Move> moveSet, boolean isShiny) {
		this.types              = new ArrayList<>(proto.types);
		this.name               = proto.name;
		this.level              = proto.level;
		this.ps                 = proto.ps;
		this.maxPs              = proto.maxPs;
		this.isAlive            = proto.isAlive;
		this.ability            = proto.ability;
		this.speed              = proto.speed;
		this.speedBoost         = proto.speedBoost;
		this.attack             = proto.attack;
		this.attackBoost        = proto.attackBoost;
		this.defense            = proto.defense;
		this.defenseBoost       = proto.defenseBoost;
		this.specialAttack      = proto.specialAttack;
		this.specialAttackBoost = proto.specialAttackBoost;
		this.specialDefense     = proto.specialDefense;
		this.specialDefenseBoost= proto.specialDefenseBoost;
		this.evasionBoost       = proto.evasionBoost;
		this.precisionBoost     = proto.precisionBoost;
		this.moveSet            = new ArrayList<>(moveSet);;
		this.description        = proto.description;
		this.isShiny            = isShiny;
		this.statusEffect       = "none";
		this.isConfused         = false;
		this.confusedCounter    = 0;
		this.statusMobilityTimer= 0;
	}

	/**
	 * Obtiene la lista de movimientos disponibles del Pokémon
	 * @return Lista de movimientos actuales
	 */
	public List<Move> getMoves() {
		return this.moveSet;
	}
	/**
	 * Verifica si el Pokémon está consciente y capaz de combatir
	 * @return true si los PS son mayores que 0
	 */
	public boolean isAlive() {
		return this.isAlive;
	}

	/**
	 * Reduce los PS por daño recibido y actualiza estado de vitalidad
	 * @param amount Cantidad de daño a aplicar
	 */
	public void receiveDamage(int amount) {
		this.ps -= amount;
		if (this.ps <= 0) {
			this.isAlive = false;
			this.ps = 0;
		}
	}
	/**
	 * Restaura PS sin exceder el máximo
	 * @param amount Cantidad a curar
	 */
	public void heal(int amount) {
		this.ps += amount;
		if (this.ps > this.maxPs) {
			this.ps = this.maxPs;
		}
	}
	/**
	 * Modifica los estadios de una estadística (range: -6 a +6)
	 * @param stat Nombre de la estadística a modificar
	 * @param increase Cantidad de etapas a aumentar/disminuir
	 */
	public void boostStat(String stat, int increase) {
		int actualStage;
		double newBoost;
		if (stat.equals("Attack")){
			if (!(this.attackBoost + increase > 6 || this.attackBoost + increase < -6)) {
				this.attackBoost += increase;
			}
			else if (this.attackBoost + increase > 6){
				this.attackBoost = 6;
			}
			else if (this.attackBoost + increase < -6) {
				this.attackBoost = -6;
			}
		}
		else if (stat.equals("Defense")){
			if (!(this.defenseBoost + increase > 6 || this.defenseBoost + increase < -6)) {
				this.defenseBoost += increase;
			}
			else if (this.defenseBoost + increase > 6){
				this.defenseBoost = 6;
			}
			else if (this.defenseBoost + increase < -6) {
				this.defenseBoost = -6;
			}
		}
		else if (stat.equals("Special Attack")){
			if (!(this.specialAttackBoost + increase > 6 || this.specialAttackBoost + increase < -6)) {
				this.specialAttackBoost += increase;
			}
			else if (this.specialAttackBoost + increase > 6){
				this.specialAttackBoost = 6;
			}
			else if (this.specialAttackBoost + increase < -6) {
				this.specialAttackBoost = -6;
			}
		}
		else if (stat.equals("Special Defense")){
			if (!(this.specialDefenseBoost + increase > 6 || this.specialDefenseBoost + increase < -6)) {
				this.specialDefenseBoost += increase;
			}
			else if (this.specialDefenseBoost + increase > 6){
				this.specialDefenseBoost = 6;
			}
			else if (this.specialDefenseBoost + increase < -6) {
				this.specialDefenseBoost = -6;
			}
		}
		else if (stat.equals("Speed")){
			if (!(this.speedBoost + increase > 6 || this.speedBoost + increase < -6)) {
				this.speedBoost += increase;
			}
			else if (this.speedBoost + increase > 6){
				this.speedBoost = 6;
			}
			else if (this.speedBoost + increase < -6) {
				this.speedBoost = -6;
			}
		}
		else if (stat.equals("Evasion")) {
			if (!(this.evasionBoost + increase > 6 || this.evasionBoost + increase < -6)) {
				this.evasionBoost += increase;
			}
			else if (this.evasionBoost + increase > 6){
				this.evasionBoost = 6;
			}
			else if (this.evasionBoost + increase < -6) {
				this.evasionBoost = -6;
			}
		}
	}
	/**
	 * Aplica un efecto de estado si el Pokémon no es inmune
	 * @param status Nombre del efecto a aplicar
	 * @return true si el efecto fue aplicado exitosamente
	 */
	public boolean changeStatusEffect(String status) {
		boolean receiveStatus = false;
		if (this.statusEffect.equals("none")){
			if(!this.types.contains("Electric") && status.equals("Paralyzed")){
				this.statusEffect = status;
				receiveStatus = true;
				this.boostStat("Speed",-2);
			}
			else if (!this.types.contains("Fire") && status.equals("Burn")) {
				this.statusEffect = status;
				receiveStatus = true;
				this.boostStat("Attack",-2);
			}
			else if ((!this.types.contains("Poison") || !this.types.contains("Steel")) && status.equals("Burn")) {
				this.statusEffect = status;
				receiveStatus = true;
			}
			else if (!this.types.contains("Ice") && status.equals("Freeze")) {
				this.statusMobilityTimer = 10000;
				this.statusEffect = status;
				receiveStatus = true;
			}
			else if (status.equals("Confusion") && !this.getIsConfused()) {
				this.confuse();
				receiveStatus = true;
			}
			else if(status.equals("Sleep")){
				Random rand = new Random();
				this.statusMobilityTimer = rand.nextInt(1,4);
				this.statusEffect = status;
				receiveStatus = true;
			}
		}
		return receiveStatus;
	}
	/**
	 * Confunde al Pokémon y establece una cuenta regresiva
	 * hasta que se le pase el efecto
	 */
	private void confuse() {
		Random rand = new Random();
		this.isConfused = true;
		this.confusedCounter = rand.nextInt(2,6);
	}
	/**
	 * Revive al Pokémon restaurando una cantidad específica de PS
	 * @param ps Cantidad de PS a restaurar (no excede el máximo)
	 */
	public void revive(int ps){
		this.isAlive = true;
		this.heal(ps);
	}
	/**
	 * Obtiene el valor máximo de PS del Pokémon
	 * @return PS máximo posible
	 */
	public Integer getMaxPs() {
		return this.maxPs;
	}
	/**
	 * Obtiene los PS actuales del Pokémon
	 * @return Valor actual de PS
	 */
	public Integer getPs() {
		return this.ps;
	}
	/**
	 * Obtiene el nivel del Pokémon
	 * @return Nivel entre 1 y 100
	 */
	public int getLevel() {
		return this.level;
	}
	/**
	 * Obtiene el valor base de ataque físico
	 * @return Valor base sin modificadores
	 */
	public int getAttack() {
		return this.attack;
	}
	/**
	 * Obtiene el valor real de ataque considerando los modificadores
	 * @return Valor de ataque ajustado por nivel
	 */
	public double getRealAttack(){
		return this.attack * STAGE_STATS_MULTIPLIERS.get(this.attackBoost);
	}
	/**
	 * Obtiene el valor base de defensa física
	 * @return Valor base sin modificadores
	 */
	public int getDefense() {
		return this.defense;
	}
	/**
	 * Obtiene el valor real de defensa considerando los modificadores
	 * @return Valor de defensa ajustado por nivel
	 */
	public double getRealDefense(){
		return this.defense * STAGE_STATS_MULTIPLIERS.get(this.defenseBoost);
	}
	/**
	 * Obtiene La lista de tipos del pokemon
	 * @return Lista de tipos
	 */
	public List<String> getTypes() {
		return this.types;
	}
	/**
	 * Obtiene el valor base de ataque especial
	 * @return Valor base sin modificadores
	 */
	public int getSpecialAttack() {
		return this.specialAttack;
	}
	/**
	 * Obtiene el valor real de ataque especial considerando los modificadores
	 * @return Valor de ataque especial ajustado por nivel
	 */
	public double getRealSpecialAttack() { return this.specialAttack * STAGE_STATS_MULTIPLIERS.get(this.specialAttackBoost); }
	/**
	 * Obtiene el valor base de defensa especial
	 * @return Valor base sin modificadores
	 */
	public int getSpecialDefense() {
		return this.specialDefense;
	}
	/**
	 * Obtiene el valor real de defensa especial considerando los modificadores
	 * @return Valor de defensa especial ajustado por nivel
	 */
	public double getRealSpecialDefense(){
		return this.specialDefense * STAGE_STATS_MULTIPLIERS.get(this.specialDefenseBoost);
	}
	/**
	 * Obtiene el valor real de velocidad considerando los modificadores
	 * @return Valor de velocidad ajustado por nivel
	 */
	public double getRealSpeed(){
		return this.speed * STAGE_STATS_MULTIPLIERS.get(this.speedBoost);
	}
	/**
	 * Obtiene el nombre del Pokémon
	 * @return Nombre en formato String
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Obtiene el efecto de estado actual
	 * @return Nombre del estado ("none" si no hay efecto)
	 */
	public String getStatusEffect (){
		return this.statusEffect;
	}
	/**
	 * Obtiene el modificador actual de precisión
	 * @return Valor entre -6 y +6
	 */
	public int getPrecisionBoost() {
		return this.precisionBoost;
	}
	/**
	 * Obtiene el modificador actual de evasión
	 * @return Valor entre -6 y +6
	 */
	public int getEvasionBoost() {
		return this.evasionBoost;
	}
	/**
	 * Verifica si el Pokémon está confundido
	 * @return true si el contador de confusión es mayor a 0
	 */
	public boolean getIsConfused() {
		return this.isConfused;
	}

	/**
	 * Verifica si el Pokémon recupera movilidad por fin de efecto de estado
	 * @return true si el estado fue removido
	 */
	public boolean gainsMobility() {
		boolean gainsMobility = false;
		if (this.statusEffect.equals("Freeze")){
			Random rand = new Random();
			if(rand.nextInt(1,101) <= 20){
				this.statusEffect = "None";
				this.statusMobilityTimer = 0;
				gainsMobility = true;
			}
		}
		else if(this.statusEffect.equals("Sleep")){
			this.statusMobilityTimer -= 1;
			if(this.statusMobilityTimer == 0){
				this.statusEffect = "None";
				gainsMobility = true;
			}
		}
		return gainsMobility;
	}
	/**
	 * Maneja la reducción del contador de confusión
	 * @return true si la confusión termina
	 */
	public boolean confusionFades() {
		boolean confusionFades = false;
		this.confusedCounter -= 1;
		if (this.confusedCounter == 0) {
			this.isConfused = false;
			confusionFades = true;
		}
		return confusionFades;
	}
	/**
	 * Intenta ejecutar un movimiento específico
	 * @param move Índice del movimiento en la lista
	 * @return true si el movimiento está disponible para usar
	 */
	public boolean makeMove(int move) {
		boolean moveIsAvailable = this.moveSet.get(move).isAvailable();
		boolean availableMoves = false;
		for(Move m : this.moveSet){
			if(m.isAvailable()){
				availableMoves = true;
				break;
			}
		}
		if (!availableMoves){
			this.moveSet.clear();
			this.moveSet.add(new Move("Struggle","Normal","Physical",99,50,100,0,new ArrayList<>(),"Used only if all PP are gone. Also hurts the user a little.",0,false));
		}
		return moveIsAvailable;
	}
	/**
	 * Obtiene el modificador actual de ataque físico
	 * @return Valor entre -6 y +6
	 */
	public int getAttackBoost() {

		return this.attackBoost;
	}
	/**
	 * Obtiene el modificador actual de defensa física
	 * @return Valor entre -6 y +6
	 */
	public int getDefenseBoost() {
		return this.defenseBoost;
	}
	/**
	 * Obtiene el modificador actual de ataque especial
	 * @return Valor entre -6 y +6
	 */
	public int getSpecialAttackBoost() {
		return this.specialAttackBoost;
	}
	/**
	 * Obtiene el modificador actual de defensa especial
	 * @return Valor entre -6 y +6
	 */
	public int getSpecialDefenseBoost() {
		return this.specialDefenseBoost;
	}
	/**
	 * Obtiene el modificador actual de velocidad
	 * @return Valor entre -6 y +6
	 */
	public int getSpeedBoost() { return this.speedBoost; }
	/**
	 * Cuando el Pokemon deja de estra activo
	 * resetea sus boost y confusion.
	 */
	public void notActive() {
		if (this.isAlive) {
			resetBoost();
		}
	}

	private void resetBoost() {
		this.evasionBoost = 0;
		if (this.getStatusEffect().equals("Paralyzed")){
			this.speedBoost = -2;
		}else{ this.speedBoost = 0; }
		this.speedBoost = 0;
		if (this.getStatusEffect().equals("Burn")){
			this.attackBoost = -2;
		} else{ this.attackBoost = 0;}
		this.defenseBoost = 0;
		this.specialAttackBoost = 0;
		this.specialDefenseBoost = 0;
		this.precisionBoost = 0;
		this.isConfused = false;
		this.confusedCounter = 0;
	}
}
