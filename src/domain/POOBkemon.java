package src.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
        this.generatePokemons();
    }

    private void generateMovements() {
        ArrayList<Effect> flamethrowerEffects = new ArrayList<>();
        flamethrowerEffects.add(new StatusEffect("Burn", 10));
        movements.put("Flamethrower", new Move(
                "Flamethrower", "Fire", "Special",
                15, 95, 100, 0,
                flamethrowerEffects,
                "Looses a stream of fire that may burn the foe.",
                6, false
        ));

        ArrayList<Effect> doubleTeamEffects = new ArrayList<>();
        doubleTeamEffects.add(new StatEffect("Evasion", 1, 100));
        movements.put("Double Team", new Move(
                "Double Team", "Normal", "State",
                15, 0, 100, 0,
                doubleTeamEffects,
                "Creates illusory copies to raise evasiveness.",
                0, true
        ));

        movements.put("Razor Leaf", new Move(
                "Razor Leaf", "Grass", "Physical",
                25, 55, 95, 0,
                new ArrayList<>(),
                "Cuts the enemy with leaves. High critical-hit ratio.",
                13, false
        ));

        ArrayList<Effect> rainDanceEffects = new ArrayList<>();
        rainDanceEffects.add(new WeatherEffect("Rain", 100));
        movements.put("Rain Dance", new Move(
                "Rain Dance", "Water", "State",
                5, 0, 100, 0,
                rainDanceEffects,
                "Boosts the power of Water-type moves for 5 turns.",
                0, false
        ));

        ArrayList<Effect> ironDefenseEffects = new ArrayList<>();
        ironDefenseEffects.add(new StatEffect("Defense", 2, 100));
        movements.put("Iron Defense", new Move(
                "Iron Defense", "Steel", "State",
                15, 0, 100, 0,
                ironDefenseEffects,
                "Hardens the body's surface to sharply raise Defense.",
                0, true
        ));

        movements.put("Dragon Claw", new Move(
                "Dragon Claw", "Dragon", "Physical",
                15, 80, 100, 0,
                new ArrayList<>(),
                "Slashes the foe with sharp claws.",
                6, false
        ));

        movements.put("Shadow Punch", new Move(
                "Shadow Punch", "Ghost", "Physical",
                20, 60, 1000, 0,
                new ArrayList<>(),
                "An unavoidable punch that is thrown from shadows.",
                6, true
        ));

        ArrayList<Effect> swordDanceEffects = new ArrayList<>();
        swordDanceEffects.add(new StatEffect("Attack", 2, 100));
        movements.put("Sword Dance", new Move(
                "Sword Dance", "Normal", "State",
                20, 0, 100, 0,
                swordDanceEffects,
                "A fighting dance that sharply raises Attack.",
                0, true
        ));
        ArrayList<Effect> thunderEffects = new ArrayList<>();
        thunderEffects.add(new StatusEffect("Paralized", 30));
        movements.put("Thunder", new Move(
                "Thunder", "Electric", "Special",
                10, 110, 70, 0,
                thunderEffects,
                "A lightning attack that may cause paralysis.",
                6, false
        ));
        ArrayList<Effect> EarthquakeEffects = new ArrayList<>();
        movements.put("Earthquake", new Move(
                "Earthquake", "Ground", "Physical",
                10, 100, 100, 0,
                EarthquakeEffects,
                "A powerful quake, but has no effect on flying foes.",
                6, false
        ));
    }

    private void generatePokemons(){
        pokemons.put("Sceptile",new Pokemon(new ArrayList<>(List.of("Grass")),"Sceptile",281,null,276, 206, 166, 246,206, new ArrayList<>(),100, "In the jungle, its power is without equal. This Pokémon carefully grows trees and plants. It regulates its body temperature by basking in sunlight.", false));
        pokemons.put("Charizard",new Pokemon(new ArrayList<>(List.of("Fire","Flying")), "Charizard", 297, null, 236, 204, 192, 254, 206, new ArrayList<>(), 100, "A Charizard flies about in search of strong opponents. It breathes intense flames that can melt any material. However, it will never torch a weaker foe.", false));
        pokemons.put("Raichu",new Pokemon(new ArrayList<>(List.of("Electric")),"Raichu",261,null,256,216,146,216,196, new ArrayList<>(),100,"If it stores too much electricity, its behavior turns aggressive. To avoid this, it occasionally discharges excess energy and calms itself down.",false));
        pokemons.put("Sandslash",new Pokemon(new ArrayList<>(List.of("Ground")),"Sandslash",291,null,166,236,256,126,146, new ArrayList<>(),100,"It curls up in a ball to protect itself from enemy attacks. It also curls up to prevent heatstroke during the daytime when temperatures rise sharply.",false));
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
    public Machine createTrainerMachine(String typeTrainer) throws PoobkemonException {
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
            Machine m = new AttackingTrainer(inventory);;
        if (typeTrainer.equals("attacking")){
             m = new AttackingTrainer(inventory);
            this.trainers.add(m);
        }
        else if (typeTrainer.equals("changing")){
             m = new ChangingTrainer(inventory);
            this.trainers.add(m);
        }
        else if (typeTrainer.equals("defensive")){
            m = new DefensiveTrainer(inventory);
            this.trainers.add(m);
        }
        else if (typeTrainer.equals("expert")){
            m = new ExpertTrainer(inventory);
            this.trainers.add(m);
        }
            return m;
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
        else if (itemName.equals("Super Potion")){
            return new SuperPotion();
        }
        else if (itemName.equals("Hyper Potion")) {
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
        Machine t1 = (Machine)battlefield.getTrainer1();
        Machine t2 = (Machine)battlefield.getTrainer2();
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

    public List<List<Action>> getAvailableActionsTrainer1(){
        Trainer trainer1 = this.battlefield.getTrainer1();
        return trainer1.getAvailableActions().get(getTrainer1ActivePokemon());
    }

    public List<List<Action>> getAvailableActionsTrainer2(){
        Trainer trainer2 = this.battlefield.getTrainer2();
        return trainer2.getAvailableActions().get(getTrainer2ActivePokemon());
    }

    public Trainer getTrainer1(){
        return this.battlefield.getTrainer1();
    }

    public Trainer getTrainer2(){
        return this.battlefield.getTrainer2();
    }

    public Pokemon getTrainer1ActivePokemon(){
        return this.battlefield.getTrainer1().getActivePokemon();
    }

    public Pokemon getTrainer2ActivePokemon(){
        return this.battlefield.getTrainer2().getActivePokemon();
    }

    public String getNameActivePokemonTrainer1(){
        return this.battlefield.getTrainer1().getActivePokemon().getName();
    }

    public String getNameActivePokemonTrainer2(){
        return this.battlefield.getTrainer2().getActivePokemon().getName();
    }

    public int getPsPokemonTrainer1(){
        return this.battlefield.getTrainer1().getActivePokemon().getPs();
    }

    public int getPsPokemonTrainer2(){
        return this.battlefield.getTrainer2().getActivePokemon().getPs();
    }

    public int getMaxPsPokemonTrainer1(){
        return this.battlefield.getTrainer1().getActivePokemon().getMaxPs();
    }

    public int getMaxPsPokemonTrainer2(){
        return this.battlefield.getTrainer2().getActivePokemon().getMaxPs();
    }

    public List<Trainer> getTrainers() {
        return this.trainers;
    }

    public List<String> getMovesTypes(boolean trainer1) {
        List<String> movesTypes = new ArrayList<>();
        if (trainer1){
            for (Move m : getTrainer1ActivePokemon().getMoves()){
                movesTypes.add(m.getType());
            }
        }else {
            for (Move m : getTrainer2ActivePokemon().getMoves()){
                movesTypes.add(m.getType());
            }
        }
        return movesTypes;
    }

    public List<String> getMovesNames(boolean trainer1) {
        List<String> movesNames = new ArrayList<>();
        if (trainer1){
            for (Move m : getTrainer1ActivePokemon().getMoves()){
                movesNames.add(m.getName());
            }
        }else {
            for (Move m : getTrainer2ActivePokemon().getMoves()){
                movesNames.add(m.getName());
            }
        }
        return movesNames;
    }

    public List<Integer> getMovesPp(boolean trainer1) {
        List<Integer> movesPp = new ArrayList<>();
        if (trainer1){
            for (Move m : getTrainer1ActivePokemon().getMoves()){
                movesPp.add(m.getPp());
            }
        }else {
            for (Move m : getTrainer2ActivePokemon().getMoves()){
                movesPp.add(m.getPp());
            }
        }
        return movesPp;
    }

    public List<Integer> getMoveMaxPp(boolean trainer1) {
        List<Integer> movesMaxPp = new ArrayList<>();
        if (trainer1){
            for (Move m : getTrainer1ActivePokemon().getMoves()){
                movesMaxPp.add(m.getMaxPp());
            }
        }else {
            for (Move m : getTrainer2ActivePokemon().getMoves()){
                movesMaxPp.add(m.getMaxPp());
            }
        }
        return movesMaxPp;
    }

    public List<String> getItemsNames(boolean trainer1) {
        List<String> itemsNames = new ArrayList<>();
        if (trainer1){
            for (Item i : this.getTrainer1().getInventory().keySet()){
                itemsNames.add(i.getName());
            }
        }else {
            for (Item i : this.getTrainer2().getInventory().keySet()){
                itemsNames.add(i.getName());
            }
        }
        return itemsNames;
    }

    public List<String> getItemDescriptions(boolean trainer1) {
        List<String> itemsDescriptions = new ArrayList<>();
        if (trainer1){
            for (Item i : this.getTrainer1().getInventory().keySet()){
                itemsDescriptions.add(i.getDescription());
            }
        }else {
            for (Item i : this.getTrainer2().getInventory().keySet()){
                itemsDescriptions.add(i.getDescription());
            }
        }
        return itemsDescriptions;
    }

    public List<Integer> getItemsAmounts(boolean trainer1) {
        List<Integer> itemsAmounts = new ArrayList<>();
        if (trainer1){
            for (Integer i : this.getTrainer1().getInventory().values()){
                itemsAmounts.add(i);
            }
        }else {
            for (Integer i : this.getTrainer2().getInventory().values()){
                itemsAmounts.add(i);
            }
        }
        return itemsAmounts;
    }

    public List<String> getPokemonsNames(boolean trainer1) {
        List<String> pokemonsNames = new ArrayList<>();
        if (trainer1){
            for (Pokemon p : this.getTrainer1().getTeam()){
                if(!Objects.equals(p, this.getTrainer1ActivePokemon())){
                    pokemonsNames.add(p.getName());
                }
            }
        }
        else {
            for (Pokemon p : this.getTrainer2().getTeam()){
                if(!Objects.equals(p, this.getTrainer2ActivePokemon())){
                    pokemonsNames.add(p.getName());
                }
            }
        }
        return pokemonsNames;
    }

    public List<Integer> getPokemonsPs(boolean trainer1) {
        List<Integer> pokemonsPs = new ArrayList<>();
        if (trainer1){
            for (Pokemon p : this.getTrainer1().getTeam()){
                if(!Objects.equals(p, this.getTrainer1ActivePokemon())){
                    pokemonsPs.add(p.getPs());
                }
            }
        }
        else {
            for (Pokemon p : this.getTrainer2().getTeam()){
                if(!Objects.equals(p, this.getTrainer2ActivePokemon())){
                    pokemonsPs.add(p.getPs());
                }
            }
        }
        return pokemonsPs;
    }

    public List<Integer> getPokemonsMaxPs(boolean trainer1) {
        List<Integer> pokemonsMaxPs = new ArrayList<>();
        if (trainer1){
            for (Pokemon p : this.getTrainer1().getTeam()){
                if(!Objects.equals(p, this.getTrainer1ActivePokemon())){
                    pokemonsMaxPs.add(p.getMaxPs());
                }
            }
        }
        else {
            for (Pokemon p : this.getTrainer2().getTeam()){
                if(!Objects.equals(p, this.getTrainer2ActivePokemon())){
                    pokemonsMaxPs.add(p.getMaxPs());
                }
            }
        }
        return pokemonsMaxPs;
    }

    public void saveBattlefield(File file) throws PoobkemonException{
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(battlefield);
        } catch (IOException e){
            throw new PoobkemonException(PoobkemonException.WRITE_ERROR);

        }
    }



    public BattleField getBattlefield() {
        return battlefield;
    }

    public boolean isOver() {
        return battlefield.isOver();
    }
}

