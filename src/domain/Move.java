package src.domain;


import java.util.ArrayList;
import java.util.Random;
/**
 * Representa un movimiento que un Pokémon puede usar en combate. Contiene toda la lógica
 * de ejecución incluyendo cálculo de daño, efectos secundarios y gestión de PP.
 *
 * @author Palacios-Roa
 * @version 1.0
 */
public class Move {

	private String name;

	private String type;

	private String category;

	private int pp;

	private int maxPp;

	private boolean available;

	private int power;

	private int precision;

	private int critRate;

	private String description;

	private ArrayList<Effect> secondaryEffects;

	private int priority;

	private boolean autoEffect;
	/**
	 * Constructor principal para crear un movimiento
	 * @param name Nombre del movimiento
	 * @param type Tipo elemental del movimiento
	 * @param category Categoría (Physical/Special/State)
	 * @param pp Puntos de poder iniciales
	 * @param potence Potencia base del movimiento (asignado a power)
	 * @param precision Precisión base (0-100)
	 * @param priority Prioridad de ejecución
	 * @param secondaryEffects Lista de efectos secundarios aplicables
	 * @param description Descripción flavor text
	 * @param critRate Tasa de golpe crítico (1-100)
	 * @param autoEffect Indica si los efectos se aplican al usuario (true) o al objetivo (false)
	 */
	public Move(String name, String type, String category, int pp, int potence, int precision, int priority, ArrayList<Effect> secondaryEffects, String description, int critRate,boolean autoEffect) {
		this.name = name;
		this.type = type;
		this.category = category;
		this.pp = pp;
		this.maxPp = pp;
		this.power = potence;
		this.precision = precision;
		this.priority = priority;
		this.secondaryEffects = secondaryEffects;
		this.description = description;
		this.critRate = critRate;
		this.available = true;
		this.autoEffect = autoEffect;
	}
	public Move(Move other) {
		this.name             = other.name;
		this.type             = other.type;
		this.category         = other.category;
		this.pp               = other.pp;
		this.maxPp            = other.maxPp;
		this.power            = other.power;
		this.precision        = other.precision;
		this.priority         = other.priority;
		this.secondaryEffects = new ArrayList<>(other.secondaryEffects);
		this.description      = other.description;
		this.critRate         = other.critRate;
		this.available        = other.available;
		this.autoEffect = other.autoEffect;
	}
	/**
	 * Crea una copia independiente del movimiento
	 * @return Nueva instancia con mismo estado
	 */
	public Move copy() {
		return new Move(this);
	}
	/**
	 * Ejecuta el movimiento en combate:
	 * 1. Verifica estados inmovilizantes (parálisis/sueño/confusión)
	 * 2. Calcula probabilidad de éxito considerando precision/evasión
	 * 3. Calcula daño según categoría (Physical/Special)
	 * 4. Aplica efectos secundarios
	 * 5. Gestiona PP y disponibilidad
	 *
	 * @param field Campo de batalla actual
	 * @param user Pokémon que usa el movimiento
	 * @param target Pokémon objetivo
	 */
	public void execute(BattleField field, Pokemon user, Pokemon target) {
		Random rand = new Random();
		Pokemon effectTarget = target;
		if (autoEffect){
			effectTarget = user;
		}
		boolean unableToMove = false;
		boolean failed = true;
		if(user.getStatusEffect().equals("Sleep")||user.getStatusEffect().equals("Frezee")){
			unableToMove = true;
		}
		else if(user.getStatusEffect().equals("Paralyzed")){
			if(rand.nextInt(1,101)<= 25){
				unableToMove = true;
			}
		}
		else if(user.getIsConfused()){
			if(rand.nextInt(1,101)<= 50){
				user.receiveDamage(TypesTable.calculatePhysicalDamage(user, user, new Move("","","Physical",100,40,100,0,new ArrayList<>(),"",0,false), field));
				unableToMove = true;
			}
		}
		if (!unableToMove){
			this.pp--;
			double adjustedAccuracy = this.precision *
					Pokemon.STAGE_PRECISION_MULTIPLIERS.get(user.getPrecisionBoost()) /
					Pokemon.STAGE_PRECISION_MULTIPLIERS.get(target.getEvasionBoost());
			failed = rand.nextInt(1, 101) > adjustedAccuracy;
		}
		boolean isCrit = false;
		if(!failed || !unableToMove){
			int critRandom = rand.nextInt(1,101);
			if (this.category.equals("Physical") || this.category.equals("Special")) {
				int dmg = (this.category.equals("Physical"))
						? TypesTable.calculatePhysicalDamage(user, target, this, field)
						: TypesTable.calculateSpecialDamage(user, target, this, field);
				isCrit = critRandom <= this.critRate;
				dmg = (isCrit) ? dmg * 2 : dmg;
				target.receiveDamage(dmg);
				field.notify(new MoveNotification(this.name, this.category, user.getName(), target.getName(), TypesTable.getEffectivityMultiplier(this, target), isCrit, failed, unableToMove, user.getStatusEffect()));
			}
			for (Effect e : this.secondaryEffects) {
				e.apply(field, user, effectTarget);
			}
		}
		else{
			field.notify(new MoveNotification(this.name, this.category, user.getName(), target.getName(), TypesTable.getEffectivityMultiplier(this, target), isCrit, failed, unableToMove, user.getStatusEffect()));
		}
		checkPp();
	}

	private void checkPp() {
		if (this.pp == 0){
			this.available = false;
		}
	}
	/**
	 * Verifica disponibilidad del movimiento
	 * @return true si quedan PP disponibles
	 */
	public boolean isAvailable() {
		return this.available;
	}
	/**
	 * Obtiene el tipo elemental del movimiento
	 * @return Tipo del movimiento (ej: "Fire", "Water", "Electric")
	 */
	public String getType() {
		return this.type;
	}
	/**
	 * Obtiene la potencia base del movimiento para cálculos de daño
	 * @return Valor numérico de potencia (0 para movimientos de estado)
	 */
	public int getPower() {
		return this.power;
	}
	/**
	 * Obtiene la prioridad de ejecución en el sistema de turnos
	 * @return Valor de prioridad (mayor valor = se ejecuta primero)
	 */
	public int getPriority() {
		return this.priority;
	}
	/**
	 * Obtiene el nombre identificativo del movimiento
	 * @return Nombre en formato estándar (ej: "Flamethrower", "Iron Defense")
	 */
	public String getName() {
		return name;
	}
	/**
	 * Obtiene el pp actual del movimiento
	 * @return entero
	 */
	public int getPp() { return this.pp; }
	/**
	 * Obtiene los efectos secundarios asociados al movimiento
	 * @return Lista de efectos que se aplican al usar el movimiento
	 */
	public ArrayList<Effect> getSecondaryEffects() {
		return this.secondaryEffects;
	}
	/**
	 * Obtiene la descripcion del movimiento
	 * @return descripcion
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * Obtiene los max pp del movimiento
	 * @return Maxpp
	 */
	public int getMaxPp(){ return this.maxPp;}
}
