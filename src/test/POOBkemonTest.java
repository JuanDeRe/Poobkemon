package src.test;

import src.domain.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class POOBkemonTest {

    private POOBkemon game;
    private ArrayList<Move> validMoveSet;
    private Map<Item, Integer> testInventory;

    @BeforeEach
    void setUp() throws PoobkemonException {
        game = new POOBkemon();
        validMoveSet = new ArrayList<>() {{
            add(POOBkemon.createMove("Razor Leaf"));
        }};

        testInventory = new HashMap<>() {{
            put(new Potion("Potion", "Restores 20 PS"), 3);
            put(new Revive(), 1);
        }};
    }

    // Pruebas para createPokemon()
    @Test
    public void testCreateValidPokemon() {
        try {
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", validMoveSet);
            assertAll(
                    () -> assertEquals("Sceptile", sceptile.getName()),
                    () -> assertFalse(sceptile.getMoves().isEmpty()),
                    () -> assertTrue(sceptile.isAlive())
            );
        } catch (PoobkemonException e) {
            fail("No debería lanzar excepción");
        }
    }

    @Test
    public void testCreatePokemonWithInvalidName() {
        Exception exception = assertThrows(PoobkemonException.class, () ->
            POOBkemon.createPokemon("Pikachu", validMoveSet) // "Pikachu" no está registrado
        );
        assertEquals(PoobkemonException.INVALID_NAME, exception.getMessage());
    }

    @Test
    public void testCreatePokemonWithTooManyMoves() {
        ArrayList<Move> invalidMoves = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            invalidMoves.add(POOBkemon.createMove("Razor Leaf")); // Asegúrate de que "Razor Leaf" existe
        }
        Exception exception = assertThrows(PoobkemonException.class, () ->
            POOBkemon.createPokemon("Sceptile", invalidMoves)
        );
        assertEquals(PoobkemonException.NO_MORE_THAN_4_MOVES, exception.getMessage());
    }

    // Pruebas para createMove()
    @Test
    public void testCreateValidMove() {
        try {
            Move move = POOBkemon.createMove("Flamethrower");
            assertAll(
                    () -> assertEquals("Flamethrower", move.getName()),
                    () -> assertEquals("Fire", move.getType()),
                    () -> assertTrue(move.getPower() > 0)
            );
        } catch (PoobkemonException e) {
            fail("No debería lanzar excepción");
        }
    }

    @Test
    public void testCreateInvalidMove() {
        Exception exception = assertThrows(PoobkemonException.class, () ->
            POOBkemon.createMove("InvalidMoveName") // Nombre que no existe
        );
        assertEquals(PoobkemonException.INVALID_NAME, exception.getMessage());
    }

    // Pruebas para createTrainerPlayer()
    @Test
    public void testCreateValidHumanTrainer() {
        try {
            ArrayList<Pokemon> team = new ArrayList<>() {{
                add(POOBkemon.createPokemon("Sceptile", validMoveSet));
            }};

            Player p = game.createTrainerPlayer("Ash", team, testInventory, "red");
            assertFalse(game.getAvailableActions(p).get(team.get(0)).isEmpty());
        } catch (PoobkemonException e) {
            fail("No debería lanzar excepción");
        }
    }

    @Test
        public void testCreateTrainerWithEmptyTeam() {
            Exception exception = assertThrows(PoobkemonException.class, () ->
                game.createTrainerPlayer("Misty", new ArrayList<>(), testInventory, "blue")
            );
            assertEquals(PoobkemonException.NO_POKEMONS_SELECTED, exception.getMessage());
        }


    @Test
    public void testCreateInvalidMachineType() {
        Exception exception = assertThrows(PoobkemonException.class, () ->
                game.createTrainerMachine("invalid_type")
        );
        assertTrue(exception.getMessage().contains("inválido"));
    }

    // Pruebas de batalla
    @Test
    public void testStartValidBattle() {
        try {
        // Configuración
            ArrayList<Pokemon> team = new ArrayList<>();
            team.add(POOBkemon.createPokemon("Sceptile", validMoveSet));
            
            Player human = new Player("Ash", team, testInventory, "red");
            Machine machine = new AttackingTrainer(testInventory);
            
            // Ejecución
            game.startOnePlayerBattle(human, machine);
            
            // Verificaciones
            Map<Pokemon, List<List<Action>>> actions = game.getAvailableActions(human);
            assertAll(
                () -> assertNotNull(actions, "Acciones no deberían ser nulas"),
                () -> assertFalse(actions.isEmpty(), "Debería haber acciones disponibles"),
                () -> assertEquals(1, actions.size(), "Debería tener acciones para 1 Pokémon"),
                () -> assertTrue(actions.containsKey(team.get(0)), "Debería tener acciones para el Pokémon activo")
            );
            
        } catch (PoobkemonException e) {
            fail("Configuración falló: " + e.getMessage());
        }
    }

    @Test
    public void testBattleTurnOrder() {
        try {
            List<Integer> order = game.decideOrder();
            assertAll(
                    () -> assertEquals(2, order.size()),
                    () -> assertTrue(order.contains(0)),
                    () -> assertTrue(order.contains(1))
            );
        } catch (Exception e) {
            fail("Error en generación de orden");
        }
    }

    // Prueba de regeneración de PP
    @Test
    public void testMovePPDepletion() {
        try {
            ArrayList<Move> moves = new ArrayList<>(List.of(POOBkemon.createMove("Rain Dance")));
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", moves);
            Pokemon charizard = POOBkemon.createPokemon("Charizard", moves);
            
            ArrayList<Pokemon> team1 = new ArrayList<>();
            team1.add(sceptile);
            
            ArrayList<Pokemon> team2 = new ArrayList<>();
            team2.add(charizard);
            
            Trainer t1 = new Player("Trainer_1", team1, testInventory, "red");
            Trainer t2 = new Player("Trainer_2", team2, testInventory, "blue");
            
            BattleField battlefield = new BattleField(t1, t2);
            Move move = sceptile.getMoves().get(0);
            int initialPP = move.getPp(); // Usar getCurrentPP() en lugar de getPower()
    
            for (int i = 0; i < initialPP; i++) {
                move.execute(battlefield, sceptile, charizard);
            }
    
            assertAll(
                () -> assertEquals(0, move.getPp(), "El PP debería agotarse"),
                () -> assertFalse(move.isAvailable(), "El movimiento no debería estar disponible")
            );
        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }
    
    @Test
    public void testReceiveDamageAndFaint() {
        try {
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", validMoveSet);
            sceptile.receiveDamage(300); // Daño mayor que sus PS
            assertAll(
                () -> assertEquals(0, sceptile.getPs()),
                () -> assertFalse(sceptile.isAlive())
            );
        } catch (PoobkemonException e) {
            fail("No debería lanzar excepción");
        }
    }
    
    @Test
    public void testStatBoostsLimits() {
        try {
            Pokemon charizard = POOBkemon.createPokemon("Charizard", validMoveSet);
            // Aumentar ataque 7 veces (máximo permitido es +6)
            for (int i = 0; i < 7; i++) {
                charizard.boostStat("Attack", 1);
            }
            assertEquals(6, charizard.getAttackBoost());
        } catch (PoobkemonException e) {
            fail("No debería lanzar excepción");
        }
    }
    @Test
public void testWeatherEffectApplication() {
    try {
        // 1. Crear movimiento "Rain Dance" y Pokémon con él
        Move rainDance = POOBkemon.createMove("Rain Dance");
        ArrayList<Move> sceptileMoves = new ArrayList<>();
        sceptileMoves.add(rainDance);
        Pokemon sceptile = POOBkemon.createPokemon("Sceptile", sceptileMoves);

        // 2. Crear Pokémon oponente (sin movimientos relevantes)
        ArrayList<Move> charizardMoves = new ArrayList<>();
        charizardMoves.add(POOBkemon.createMove("Iron Defense")); // Movimiento obligatorio
        Pokemon charizard = POOBkemon.createPokemon("Charizard", charizardMoves);

        // 3. Configurar equipos
        ArrayList<Pokemon> playerTeam = new ArrayList<>();
        playerTeam.add(sceptile);
        Player player = new Player("Ash", playerTeam, testInventory, "red");

        List<Pokemon> opponentTeam = new ArrayList<>();
        opponentTeam.add(charizard);
        DefensiveTrainer opponent = new DefensiveTrainer(testInventory);
        opponent.changeTeam(opponentTeam); // Usar changeTeam para asignar equipo

        // 4. Inicializar campo de batalla
        BattleField field = new BattleField(player, opponent);

        // 5. Obtener acción de "Rain Dance" desde las acciones disponibles
        Map<Pokemon, List<List<Action>>> playerActions = player.getAvailableActions();
        List<Action> rainDanceActions = playerActions.get(sceptile).get(0); // Índice 0 = movimientos
        Action rainDanceAction = rainDanceActions.get(0); // Primer movimiento = Rain Dance
        
        // 6. Ejecutar turno (oponente no hace nada)
        field.playTurn(rainDanceAction,opponent.chooseAction(field));

        // 7. Verificar clima y duración
        assertAll(
            () -> assertEquals("Rain", field.getWeather(), "El clima no se actualizó a Rain")
        );

    } catch (PoobkemonException e) {
        fail("Error en configuración: " + e.getMessage());
    }
}


@Test
public void testTurnOrderPriority() {
    try {
        // 1. Crear Pokémon y equipos
        Pokemon charizard = POOBkemon.createPokemon("Charizard", new ArrayList<>(List.of(POOBkemon.createMove("Razor Leaf"))));
        Pokemon sceptile = POOBkemon.createPokemon("Sceptile", new ArrayList<>(List.of(POOBkemon.createMove("Razor Leaf"))));
        Pokemon backupSceptile = POOBkemon.createPokemon("Sceptile", new ArrayList<>(List.of(POOBkemon.createMove("Razor Leaf"))));

        // 2. Configurar equipos (player2 tiene 2 Pokémon)
        ArrayList<Pokemon> team1 = new ArrayList<>(List.of(charizard));
        Player player1 = new Player("Ash", team1, testInventory, "red");

        ArrayList<Pokemon> team2 = new ArrayList<>(List.of(sceptile, backupSceptile)); // 2 Pokémon
        Player player2 = new Player("Misty", team2, testInventory, "blue");

        // 3. Obtener acciones disponibles
        Map<Pokemon, List<List<Action>>> player2Actions = player2.getAvailableActions();
        List<Action> availableSwitches = player2Actions.get(sceptile).get(2); // Índice 2 = cambios

        // Validar que hay acciones de cambio (debe ser 1 acción: cambiar a backupSceptile)
        assertFalse(availableSwitches.isEmpty(), "No hay acciones de cambio disponibles");
        Action switchAction = availableSwitches.get(0); // Cambiar al backupSceptile

        Map<Pokemon, List<List<Action>>> player1Actions = player1.getAvailableActions();
        Action moveAction = player1Actions.get(charizard).get(0).get(0); // Movimiento

        // 4. Ejecutar turno
        BattleField battlefield = new BattleField(player1, player2);
        List<Notification> log = battlefield.playTurn(moveAction, switchAction);

        // 5. Verificar que el cambio se ejecuta primero
        boolean switchOccurredFirst = log.stream()
            .anyMatch(n -> n instanceof SwitchNotification && n.getMessage().get(0).contains("changed to"));

        System.out.println(log.get(0).getMessage().get(0));
        System.out.println(log.get(1).getMessage().get(0));
         System.out.println(log.get(1).getMessage().get(1));
        assertTrue(
            
            switchOccurredFirst,
            "El cambio debe ejecutarse primero (prioridad 20 > 0). Log: " + log
        );
    } catch (PoobkemonException e) {
        fail("Error: " + e.getMessage());
    }
}
@Test
public void testAttackingTrainerPrefersHighPowerMoves() {
    try {
        // 1. Crear AttackingTrainer con inventario y equipo válido
        AttackingTrainer trainer = new AttackingTrainer(testInventory);
        
        // 2. Crear oponente concreto (DefensiveTrainer) y equipo
        List<Pokemon> opponentTeam = new ArrayList<>();
        opponentTeam.add(POOBkemon.createPokemon("Sceptile", validMoveSet));
        DefensiveTrainer opponent = new DefensiveTrainer(testInventory);
        
        // 3. Inicializar BattleField con ambos entrenadores
        BattleField field = new BattleField(trainer, opponent);
        
        // 4. Obtener acción del entrenador ofensivo
        Action action = trainer.chooseAction(field);
        Move move = ((MoveAction) action).getMove();
        
        // 5. Verificar que el movimiento tenga alta potencia
        assertTrue( move.getPower() >= 80);
    } catch (PoobkemonException e) {
        fail("Error en configuración: " + e.getMessage());
    }
}

 
@Test
public void testBurnEffectReducesAttack() {
    try {
        Pokemon charizard = POOBkemon.createPokemon("Charizard", validMoveSet);
        charizard.changeStatusEffect("Burn");
        assertAll(
            () -> assertEquals("Burn", charizard.getStatusEffect()),
            () -> assertEquals(-2, charizard.getAttackBoost())
        );
    } catch (PoobkemonException e) {
        fail("No debería lanzar excepción");
    }
}

@Test
public void testParalysisReducesSpeed() {
    try {
        Pokemon sceptile = POOBkemon.createPokemon("Sceptile", validMoveSet);
        sceptile.changeStatusEffect("Paralyzed");
        assertAll(
            () -> assertEquals("Paralyzed", sceptile.getStatusEffect()),
            () -> assertEquals(-2, sceptile.getSpeedBoost())
        );
    } catch (PoobkemonException e) {
        fail("No debería lanzar excepción");
    }
}

    @Test
    public void testMovePPDecreasesOnUse() {
        try {
            Move move = POOBkemon.createMove("Flamethrower");
            int initialPP = move.getPp();

            ArrayList<Move> moveSet = new ArrayList<>();
            moveSet.add(move);
            Pokemon user = POOBkemon.createPokemon("Charizard", moveSet);

            // Añadir movimiento al Pokémon objetivo
            ArrayList<Move> targetMoves = new ArrayList<>();
            targetMoves.add(POOBkemon.createMove("Rain Dance")); // Movimiento válido
            Pokemon target = POOBkemon.createPokemon("Sceptile", targetMoves);

            // Configurar equipos
            ArrayList<Pokemon> team1 = new ArrayList<>();
            team1.add(user);
            Player trainer1 = new Player("Ash", team1, testInventory, "red");

            ArrayList<Pokemon> team2 = new ArrayList<>();
            team2.add(target);
            AttackingTrainer trainer2 = new AttackingTrainer(testInventory);

            BattleField field = new BattleField(trainer1, trainer2);

            // Ejecutar movimiento
            move.execute(field, user, target);

            // Verificar PP
            assertEquals(initialPP - 1, move.getPp(), "El PP debería disminuir en 1");

        } catch (PoobkemonException e) {
            fail("Error: " + e.getMessage());
        }
    }

    @Test
public void testStruggleMoveWhenPPExhausted() {
    try {
        // 1. Configurar Pokémon y movimientos
        Move razorLeaf = POOBkemon.createMove("Razor Leaf");
        ArrayList<Move> moves = new ArrayList<>(List.of(razorLeaf));
        Pokemon sceptile = POOBkemon.createPokemon("Sceptile", moves);

        // 2. Configurar equipos
        ArrayList<Pokemon> team1 = new ArrayList<>(List.of(sceptile));
        Player trainer1 = new Player("Ash", team1, testInventory, "red");

        ArrayList<Pokemon> team2 = new ArrayList<>();
        team2.add(POOBkemon.createPokemon("Charizard", new ArrayList<>(List.of(POOBkemon.createMove("Rain Dance")))));
        DefensiveTrainer trainer2 = new DefensiveTrainer(testInventory);

        // 3. Inicializar BattleField
        BattleField field = new BattleField(trainer1, trainer2);

        // 4. Agotar PP usando availableActions y playTurn()
        while (razorLeaf.isAvailable()) {
            // Obtener acciones disponibles del jugador
            Map<Pokemon, List<List<Action>>> actions = trainer1.getAvailableActions();
            List<Action> moveActions = actions.get(sceptile).get(0); // Movimientos

            // Seleccionar acción de Razor Leaf (índice 0)
            Action playerAction = moveActions.get(0);
            
            // Obtener acción del oponente (usar movimiento "Rain Dance")
            Action opponentAction = trainer2.chooseAction(field);

            // Ejecutar turno
            field.playTurn(playerAction, opponentAction);
        }

        // 5. Verificar que se añade "Struggle"
        assertTrue(
            sceptile.getMoves().get(0).getName().equals("Struggle"),
            "Debería añadirse 'Struggle' cuando se agotan todos los PP"
        );

    } catch (PoobkemonException e) {
        fail("Error: " + e.getMessage());
    }
}

}
