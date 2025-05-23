package src.domain;

import java.util.*;
/**
 * Clase principal del juego que gestiona toda la lógica del sistema POOBkemon
 * @author Palacios-Roa
 * @version 1.0
 */
public class POOBkemon {

    private List<Trainer> trainers;

    private static Map<String,Pokemon> pokemons;

    private List<Item> items;

    private BattleField battlefield;

    private static Map<String,Move> movements;
    /**
     * Constructor que inicializa todos los componentes del juego:
     * - Listas de entrenadores, Pokémon e ítems
     * - Genera movimientos y Pokémon base
     * - Prepara el campo de batalla
     */
    public POOBkemon() {
        this.trainers = new ArrayList<>();
        pokemons = new TreeMap<>();
        this.items = new ArrayList<>();
        movements = new TreeMap<>();
        this.generateItems();
        this.generateMovements();
        this.generatePokeons();
    }

    private void generateMovements() {
        // Flamethrower: Quema con 10% de probabilidad
        ArrayList<Effect> flamethrowerEffects = new ArrayList<>();
        flamethrowerEffects.add(new StatusEffect("Burn", 10));
        movements.put("Flamethrower", new Move(
                "Flamethrower", "Fire", "Special",
                15, 95, 100, 0,
                flamethrowerEffects,
                "Looses a stream of fire that may burn the foe.",
                6, false
        ));

        // Double Team: Aumenta evasión en 1 etapa (100% de probabilidad)
        ArrayList<Effect> doubleTeamEffects = new ArrayList<>();
        doubleTeamEffects.add(new StatEffect("Evasion", 1, 100));
        movements.put("Double Team", new Move(
                "Double Team", "Normal", "State",
                15, 0, 100, 0,
                doubleTeamEffects,
                "Creates illusory copies to raise evasiveness.",
                0, true
        ));

        // Razor Leaf: Sin efectos secundarios
        movements.put("Razor Leaf", new Move(
                "Razor Leaf", "Grass", "Physical",
                25, 55, 95, 0,
                new ArrayList<>(),
                "Cuts the enemy with leaves. High critical-hit ratio.",
                13, false
        ));

        // Rain Dance: Cambia el clima a "Rain" (100% de probabilidad)
        ArrayList<Effect> rainDanceEffects = new ArrayList<>();
        rainDanceEffects.add(new WeatherEffect("Rain", 100));
        movements.put("Rain Dance", new Move(
                "Rain Dance", "Water", "State",
                5, 0, 100, 0,
                rainDanceEffects,
                "Boosts the power of Water-type moves for 5 turns.",
                0, false
        ));

        // Iron Defense: Aumenta defensa en 2 etapas (100% de probabilidad)
        ArrayList<Effect> ironDefenseEffects = new ArrayList<>();
        ironDefenseEffects.add(new StatEffect("Defense", 2, 100));
        movements.put("Iron Defense", new Move(
                "Iron Defense", "Steel", "State",
                15, 0, 100, 0,
                ironDefenseEffects,
                "Hardens the body's surface to sharply raise Defense.",
                0, true
        ));

        // Dragon Claw: Sin efectos secundarios
        movements.put("Dragon Claw", new Move(
                "Dragon Claw", "Dragon", "Physical",
                15, 80, 100, 0,
                new ArrayList<>(),
                "Slashes the foe with sharp claws.",
                6, false
        ));

        // Shadow Punch: Precisión corregida a 100%
        movements.put("Shadow Punch", new Move(
                "Shadow Punch", "Ghost", "Physical",
                20, 60, 1000, 0,
                new ArrayList<>(),
                "An unavoidable punch that is thrown from shadows.",
                6, true
        ));

        // Sword Dance: Aumenta ataque en 2 etapas (100% de probabilidad)
        ArrayList<Effect> swordDanceEffects = new ArrayList<>();
        swordDanceEffects.add(new StatEffect("Attack", 2, 100));
        movements.put("Sword Dance", new Move(
                "Sword Dance", "Normal", "State",
                20, 0, 100, 0,
                swordDanceEffects,
                "A fighting dance that sharply raises Attack.",
                0, true
        ));
    }

    private void generatePokeons(){
        List<String> types = new ArrayList<>();
        types.add("Grass");
        pokemons.put("Sceptile",new Pokemon(types,"Sceptile",281,null,276, 206, 166, 246,206, new ArrayList<>(),100, "Cuando está en la selva, posee una fuerza sin igual. Este POKéMON se ocupa de que los árboles y las plantas crezcan bien. Regula su temperatura corporal con la luz del sol.", false));
        types.clear();
        types.add("Fire");
        types.add("Flying");
        pokemons.put("Charizard",new Pokemon(types, "Charizard", 297, null, 236, 204, 192, 254, 206, new ArrayList<>(), 100, "Charizard va volando en busca de rivales fuertes. Echa fuego por la boca y es capaz de derretirlo todo. Ahora bien, si su rival es más débil que él, no usará este ataque.", false));
        types.clear();
    }
    /**
     * Crea un Pokémon jugable con un conjunto específico de movimientos
     * @param name Nombre del Pokémon a crear (debe existir en la lista base)
     * @param moveSet Lista de 1 a 4 movimientos
     * @return Nueva instancia del Pokémon solicitado
     * @throws PoobkemonException Si el nombre es inválido o el moveSet no cumple las reglas
     */
    public static Pokemon createPokemon(String name, ArrayList<Move> moveSet) throws PoobkemonException {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new PoobkemonException("Nombre de Pokémon inválido");
            }

            if(moveSet.size() > 4){
                throw new PoobkemonException(PoobkemonException.NO_MORE_THAN_4_MOVES);
            }
            else if(moveSet.isEmpty()){
                throw new PoobkemonException(PoobkemonException.NO_MOVES_SELECTED);
            }
            else if(!pokemons.keySet().contains(name)){
                throw new PoobkemonException(PoobkemonException.INVALID_NAME);
            }
            Pokemon proto = pokemons.get(name);
            return proto.copy(moveSet);
        } catch (PoobkemonException e) {
            Logger.logException(e);
            throw e;
        }
    }
    /**
     * Retorna la lista de movimientos disponibles en el juego
     * @return Mapa de movimientos
     * @throws PoobkemonException Si el nombre no corresponde a un movimiento registrado
     */
    public Map<String,Move> getMovements() throws PoobkemonException{
        return movements;
    }
    /**
     * Crea un movimiento predefinido del juego
     * @param name Nombre del movimiento a crear (debe existir en el sistema)
     * @return Nueva instancia del movimiento solicitado
     * @throws PoobkemonException Si el nombre no corresponde a un movimiento registrado
     */
    public static Move createMove(String name) throws PoobkemonException{
        try {
            if(!movements.keySet().contains(name)){
                throw new PoobkemonException(PoobkemonException.INVALID_NAME);
            }
            Move proto = movements.get(name);
            return proto.copy();
        } catch (PoobkemonException e) {
            Logger.logException(e);
            throw e;
        }
    }
    /**
     * Crea un nuevo entrenador humano con configuración inicial
     * @param name Nombre del entrenador (no vacío)
     * @param team Equipo de 1 a 6 Pokémon
     * @param items Inventario de objetos
     * @param color Color identificativo
     * @throws PoobkemonException Si hay error en la configuración
     */
    public Player createTrainerPlayer(String name, ArrayList<Pokemon> team, Map<Item, Integer> items, String color) throws PoobkemonException {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new PoobkemonException("Nombre de entrenador inválido");
            }

            if(team.size() > 6){
                throw new PoobkemonException(PoobkemonException.NO_MORE_THAN_6_POKEMONS_CAN_BE_SELECTED);
            }
            else if(team.isEmpty()){
                throw new PoobkemonException(PoobkemonException.NO_POKEMONS_SELECTED);
            }
            Player player = new Player(name, team, items, color);
            this.trainers.add(player);
            return player;
        } catch (PoobkemonException e) {
            Logger.logException(e);
            throw e;
        }
        
    }
    /**
     * Crea un entrenador controlado por IA con estrategia específica
     * @param typeTrainer Tipo de máquina a crear (attacking/changing/defensive/expert)
     * @throws PoobkemonException Si el tipo especificado no es válido
     */
    public void createTrainerMachine(String typeTrainer) throws PoobkemonException {
        try {
            String[] validTypes = {"attacking", "changing", "defensive", "expert"};
            if (!Arrays.asList(validTypes).contains(typeTrainer)) {
                throw new PoobkemonException("Tipo de máquina inválido: " + typeTrainer);
            }
            Map<Item, Integer> inventory = new LinkedHashMap<>();
        for (Item i : this.items){
            if(!(i.getName().equals("Revive"))){
                inventory.put(i, 2);
            }
            else if(i.getName().equals("Revive")){
                inventory.put(i, 1);
            }
        }
        if (typeTrainer.equals("attacking")){
            this.trainers.add(new AttackingTrainer(inventory));
        }
        else if (typeTrainer.equals("changing")){
            this.trainers.add(new ChangingTrainer(inventory));
        }
        else if (typeTrainer.equals("defensive")){
            this.trainers.add(new DefensiveTrainer(inventory));
        }
        else if (typeTrainer.equals("expert")){
            this.trainers.add(new ExpertTrainer(inventory));
        }
        } catch (PoobkemonException e) {
            Logger.logException(e);
            throw e;
        }
    }

    private void generateItems(){
        this.items.add(new Revive());
        this.items.add(new SuperPotion());
        this.items.add(new HyperPotion());
        this.items.add(new Potion());
    }

    public List<Item> getItems(){
        return this.items;
    }

    public Item createItem(String itemName) throws PoobkemonException{
        if(itemName == null || itemName.trim().isEmpty()){
            throw new PoobkemonException(PoobkemonException.INVALID_NAME);
        }
        if (itemName.equals("Revive")){
            return new Revive();
        }
        else if (itemName.equals("SuperPotion")){
            return new SuperPotion();
        }
        else if (itemName.equals("HyperPotion")) {
            return new HyperPotion();
        }
        else {
            return new Potion();
        }
    }
    public Map<String, Item> getItemsMap() {
        Map<String, Item> itemsMap = new TreeMap<>();
        for(Item item : this.items) {
            itemsMap.put(item.getName(), item);
        }
        return itemsMap;
    }
    /**
     * Inicia una batalla entre jugador humano y máquina
     * @param player Entrenador humano
     * @param machine Entrenador controlado por IA
     */
    public void startOnePlayerBattle(Trainer player, Machine machine) {
        this.battlefield = new BattleField(player, machine);
    }
    /**
     * Inicia una batalla entre dos jugadores humanos
     * @param player1 Primer jugador humano
     * @param player2 Segundo jugador humano
     */
    public void startTwoPlayerBattle(Player player1, Player player2) {
        this.battlefield = new BattleField(player1, player2);
    }
    /**
     * Inicia una batalla entre dos entrenadores controlados por IA
     * @param machine1 Primera máquina
     * @param machine2 Segunda máquina
     */
    public void startBotBattle(Machine machine1, Machine machine2) {
        this.battlefield = new BattleField(machine1, machine2);
    }
    /**
     * Ejecuta un turno de combate con acciones de dos jugadores humanos
     * @param humanAction1 Acción del primer jugador
     * @param humanAction2 Acción del segundo jugador
     * @return Lista de notificaciones generadas durante el turno
     */
    public List<Notification> playTurn(Action humanAction1, Action humanAction2){
        return this.battlefield.playTurn(humanAction1, humanAction2);
    }
    /**
     * Ejecuta un turno de combate con acción de un jugador humano y acción generada por IA
     * @param humanAction Acción del jugador humano
     * @return Lista de notificaciones generadas durante el turno
     */
    public List<Notification> playTurn(Action humanAction) {
        Action aiAction = battlefield.getCurrentOpponent().chooseAction(battlefield);
        return battlefield.playTurn(humanAction, aiAction);
    }
    /**
     * Ejecuta un turno de combate completamente automático entre IAs
     * @return Lista de notificaciones generadas durante el turno
     */
    public List<Notification> playTurn() {
        Machine t1 = battlefield.getTrainer1();
        Machine t2 = battlefield.getTrainer2();
        Action a1  = t1.chooseAction(battlefield);
        Action a2  = t2.chooseAction(battlefield);
        return battlefield.playTurn(a1, a2);
    }
    /**
     * Determina aleatoriamente el orden de acción inicial en un combate
     * @return Lista con orden de ejecución [primero, segundo]
     */
    public List<Integer> decideOrder(){
        Random randomNumber = new Random();
        List<Integer> order = new ArrayList<Integer>();
        int goesFirst = randomNumber.nextInt(0,2);
        if(goesFirst == 0) {
            order.add(0);
            order.add(1);
        }
        else{
            order.add(1);
            order.add(0);
        }
        return order;
    }
    /**
     * Obtiene todas las acciones disponibles para un entrenador durante su turno
     * @param trainer Entrenador del que se solicitan las acciones
     * @return Mapa con estructura:
     *         Pokémon activo -> [
     *             Lista de movimientos,
     *             Lista de objetos,
     *             Lista de cambios de Pokémon
     *         ]
     */
    public Map<Pokemon,List<List<Action>>> getAvailableActions(Trainer trainer) {
        return trainer.getAvailableActions();
    }

    public List<Trainer> getTrainers() {
        return this.trainers;
    }
}

