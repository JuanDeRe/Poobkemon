package src.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Clase abstracta que representa la entidad central de un entrenador Pokémon.
 * Gestiona equipo, inventario, Pokémon activo y acciones disponibles durante el combate.
 * @author Palacios-Roa
 * @version 1.0
 */
public abstract class  Trainer implements Serializable {

    private String name;

    List<Pokemon> team;

    private Map<Item, Integer> inventory;

    private String color;

    private Pokemon activePokemon;

    private Map<Pokemon,List<List<Action>>> availableActions; // primer lista es una lista de movimientos, segunda lista es una lista de items que se pueden usar y la tercera lista es de  pokemones por los que se puede cambiar
    /**
     * Construye un entrenador con configuración inicial completa.
     * @param name Identificador del entrenador
     * @param team Lista de Pokémon en el equipo (mínimo 1)
     * @param inventory Mapa de ítems con sus cantidades
     * @param color Representación visual asociada al entrenador
     * @implNote Inicializa:
     *           1. Pokémon activo como el primero del equipo
     *           2. Estructura availableActions con:
     *              - Movimientos disponibles por Pokémon
     *              - Ítems aplicables
     *              - Opciones de cambio
     *           Las acciones se generan automáticamente para todo el equipo
     */
    public Trainer(String name, List<Pokemon> team, Map<Item, Integer> inventory, String color) {
        this.name = name;
        this.team = team;
        this.inventory = inventory;
        this.color = color;
        this.activePokemon = team.get(0);
        updateAvailableActions();
    }
    /**
     * Registra el uso de un ítem y actualiza el inventario.
     * @param usedItem Ítem consumido
     * @implNote Realiza:
     *           1. Reduce la cantidad del ítem
     *           2. Elimina el ítem de las acciones disponibles si se agota
     */
    public void useItem(Item usedItem) {
        inventory.replace(usedItem, inventory.get(usedItem)-1);
        if (inventory.get(usedItem) == 0){
            for(List<List<Action>> list: availableActions.values()) {
                list.get(1).removeIf(a ->
                        ((ItemAction) a).getItem().getName().equals(usedItem.getName())
                );
            }
            inventory.remove(usedItem);
        }
        updateAvailableActions();
    }
    /**
     * Cambia el Pokémon activo por otro del equipo.
     * @param teamMember Índice del nuevo Pokémon activo (0-based)
     */
    public void changePokemon(int teamMember) {
        this.activePokemon.notActive();
        this.activePokemon = team.get(teamMember);
    }
    /**
     * Obtiene el inventario del entrenador.
     * @return Mapa de ítems con cantidades (implementación pendiente)
     * @implNote Actualmente devuelve null - requiere implementación
     */
    public Map<Item, Integer> getInventory() {
        return this.inventory;
    }
    /**
     * Obtiene el equipo completo del entrenador.
     * @return Lista de Pokémon (implementación pendiente)
     * @implNote Actualmente devuelve null - requiere implementación
     */
    public List<Pokemon> getTeam() {
        return this.team;
    }
    /**
     * Proporciona las acciones disponibles organizadas por Pokémon.
     * @return Mapa con estructura:
     *         - Key: Pokémon
     *         - Value: Lista de listas de acciones [movimientos, ítems, cambios]
     */
    public Map<Pokemon, List<List<Action>>> getAvailableActions() {
        return availableActions;
    }
    /**
     * Obtiene el Pokémon actualmente activo en combate.
     * @return Instancia del Pokémon activo
     */
    public Pokemon getActivePokemon(){
        return this.activePokemon;
    }
    /**
     * Devuelve el nombre del entrenador.
     * @return Identificador en formato String
     */
    public String getName() {
        return this.name;
    }
    /**
     * Ejecuta un movimiento y gestiona su disponibilidad.
     * @param pokemon Pokémon que ejecuta el movimiento
     * @param move Índice del movimiento en la lista del Pokémon
     * @implNote Si el movimiento no está disponible:
     *           - Lo elimina de las acciones
     *           - Añade "Struggle" si corresponde
     */
    public void makeMove(Pokemon pokemon, int move){
        boolean moveAvailable = pokemon.makeMove(move);
        if (!moveAvailable){
            this.availableActions.get(pokemon).get(0).remove(move);
        }
        if(pokemon.getMoves().get(0).getName().equals("Struggle")){
            this.availableActions.get(pokemon).get(0).add(new MoveAction(this,pokemon,0));
        }
    }
    /**
     * Devuelve el color del entrenador.
     * @return Identificador en formato String
     */
    public String getColor() {
        return color;
    }
    /**
     * Cambia el equipo del entrenador
     * @param newTeam Nuevo equipo
     */
    public void changeTeam(List<Pokemon> newTeam) throws PoobkemonException{
        if (newTeam.size() < 1){
            throw new PoobkemonException(PoobkemonException.NO_POKEMONS_SELECTED);
        }
        else if ( newTeam.size() > 6){
            throw new PoobkemonException(PoobkemonException.NO_MORE_THAN_6_POKEMONS_CAN_BE_SELECTED);
        }
        this.team = newTeam;
        this.activePokemon = team.get(0);
        updateAvailableActions();
    }
    protected void updateAvailableActions(){
        this.availableActions = new HashMap<>();
        for(Pokemon p : team) {
            List<Action> availableMoves = new ArrayList<>();
            List<Action> availableItems = new ArrayList<>();
            List<Action> availableSwitches = new ArrayList<>();
            for(int i = 0; i<p.getMoves().size(); i++){
                MoveAction move = new MoveAction(this,p,i);
                availableMoves.add(move);
            }
            for (Item i : inventory.keySet()) {
                ItemAction item = new ItemAction(this,p,i);
                availableItems.add(item);
            }
            for (int i = 0; i < team.size(); i++) {
                Pokemon candidate = team.get(i);
                if (candidate != p && candidate.isAlive()) {
                    availableSwitches.add(new SwitchAction(i, this));
                }
            }
            List<List<Action>> availableActionsList = new ArrayList<>();
            availableActionsList.add(availableMoves);
            availableActionsList.add(availableItems);
            availableActionsList.add(availableSwitches);
            availableActions.put(p, availableActionsList);
        }
    }
}
