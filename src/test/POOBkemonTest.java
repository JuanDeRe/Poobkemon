package src.test;

import src.domain.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class POOBkemonTest {

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
            put(new Potion(), 3);
            put(new Revive(), 1);
        }};
    }

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
            POOBkemon.createPokemon("Pikachu", validMoveSet)
        );
        assertEquals(PoobkemonException.INVALID_NAME, exception.getMessage());
    }

    @Test
    public void testCreatePokemonWithTooManyMoves() {
        ArrayList<Move> invalidMoves = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            invalidMoves.add(POOBkemon.createMove("Razor Leaf"));
        }
        Exception exception = assertThrows(PoobkemonException.class, () ->
            POOBkemon.createPokemon("Sceptile", invalidMoves)
        );
        assertEquals(PoobkemonException.NO_MORE_THAN_4_MOVES, exception.getMessage());
    }

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
            POOBkemon.createMove("InvalidMoveName")
        );
        assertEquals(PoobkemonException.INVALID_NAME, exception.getMessage());
    }

    @Test
    public void testCreateValidHumanTrainer() {
        try {
            ArrayList<Pokemon> team = new ArrayList<>() {{
                add(POOBkemon.createPokemon("Sceptile", validMoveSet));
            }};

            Player p = game.createTrainerPlayer("Ash", team, testInventory, "red");
            assertFalse(p.getAvailableActions().isEmpty());
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

    @Test
    public void testStartValidBattle() {
        try {
            ArrayList<Pokemon> team = new ArrayList<>();
            team.add(POOBkemon.createPokemon("Sceptile", validMoveSet));
            Player human = new Player("Ash", team, testInventory, "red");
            Machine machine = new AttackingTrainer(testInventory);
            game.startOnePlayerBattle(human, machine);
            Map<Pokemon, List<List<Action>>> actions = game.getAvailableActions(true);
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
            int initialPP = move.getPp();
    
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
            sceptile.receiveDamage(300);
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
        Move rainDance = POOBkemon.createMove("Rain Dance");
        ArrayList<Move> sceptileMoves = new ArrayList<>();
        sceptileMoves.add(rainDance);
        Pokemon sceptile = POOBkemon.createPokemon("Sceptile", sceptileMoves);
        ArrayList<Move> charizardMoves = new ArrayList<>();
        charizardMoves.add(POOBkemon.createMove("Iron Defense"));
        Pokemon charizard = POOBkemon.createPokemon("Charizard", charizardMoves);
        ArrayList<Pokemon> playerTeam = new ArrayList<>();
        playerTeam.add(sceptile);
        Player player = new Player("Ash", playerTeam, testInventory, "red");

        List<Pokemon> opponentTeam = new ArrayList<>();
        opponentTeam.add(charizard);
        DefensiveTrainer opponent = new DefensiveTrainer(testInventory);
        opponent.changeTeam(opponentTeam);
        BattleField field = new BattleField(player, opponent);
        Map<Pokemon, List<List<Action>>> playerActions = player.getAvailableActions();
        List<Action> rainDanceActions = playerActions.get(sceptile).get(0);
        Action rainDanceAction = rainDanceActions.get(0);
        field.playTurn(rainDanceAction,opponent.chooseAction(field));
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
        Pokemon charizard = POOBkemon.createPokemon("Charizard", new ArrayList<>(List.of(POOBkemon.createMove("Razor Leaf"))));
        Pokemon sceptile = POOBkemon.createPokemon("Sceptile", new ArrayList<>(List.of(POOBkemon.createMove("Razor Leaf"))));
        Pokemon backupSceptile = POOBkemon.createPokemon("Sceptile", new ArrayList<>(List.of(POOBkemon.createMove("Razor Leaf"))));
        ArrayList<Pokemon> team1 = new ArrayList<>(List.of(charizard));
        Player player1 = new Player("Ash", team1, testInventory, "red");

        ArrayList<Pokemon> team2 = new ArrayList<>(List.of(sceptile, backupSceptile));
        Player player2 = new Player("Misty", team2, testInventory, "blue");
        Map<Pokemon, List<List<Action>>> player2Actions = player2.getAvailableActions();
        List<Action> availableSwitches = player2Actions.get(sceptile).get(2);
        assertFalse(availableSwitches.isEmpty(), "No hay acciones de cambio disponibles");
        Action switchAction = availableSwitches.get(0);

        Map<Pokemon, List<List<Action>>> player1Actions = player1.getAvailableActions();
        Action moveAction = player1Actions.get(charizard).get(0).get(0);
        BattleField battlefield = new BattleField(player1, player2);
        List<Notification> log = battlefield.playTurn(moveAction, switchAction);
        boolean switchOccurredFirst = log.stream()
            .anyMatch(n -> n instanceof SwitchNotification && n.getMessage().get(0).contains("changed to"));

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
        AttackingTrainer trainer = new AttackingTrainer(testInventory);
        List<Pokemon> opponentTeam = new ArrayList<>();
        opponentTeam.add(POOBkemon.createPokemon("Sceptile", validMoveSet));
        DefensiveTrainer opponent = new DefensiveTrainer(testInventory);
        BattleField field = new BattleField(trainer, opponent);
        Action action = trainer.chooseAction(field);
        Move move = ((MoveAction) action).getMove();
        assertTrue( move.getPower() >= 80);
    } catch (PoobkemonException e) {
        fail("Error en configuración: " + e.getMessage());
    }
}

    @Test
    public void testBurnEffectReducesAttack() {
        try {
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", validMoveSet);
            sceptile.changeStatusEffect("Burn");

            assertAll(
                    () -> assertEquals("Burn", sceptile.getStatusEffect()),
                    () -> assertEquals(-2, sceptile.getAttackBoost())
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
            ArrayList<Move> targetMoves = new ArrayList<>();
            targetMoves.add(POOBkemon.createMove("Rain Dance"));
            Pokemon target = POOBkemon.createPokemon("Sceptile", targetMoves);
            ArrayList<Pokemon> team1 = new ArrayList<>();
            team1.add(user);
            Player trainer1 = new Player("Ash", team1, testInventory, "red");
            ArrayList<Pokemon> team2 = new ArrayList<>();
            team2.add(target);
            AttackingTrainer trainer2 = new AttackingTrainer(testInventory);
            BattleField field = new BattleField(trainer1, trainer2);
            move.execute(field, user, target);
            assertEquals(initialPP - 1, move.getPp(), "El PP debería disminuir en 1");

        } catch (PoobkemonException e) {
            fail("Error: " + e.getMessage());
        }
    }

    @Test
public void testStruggleMoveWhenPPExhausted() {
    try {
        Move razorLeaf = POOBkemon.createMove("Rain Dance");
        ArrayList<Move> moves = new ArrayList<>(List.of(razorLeaf));
        Pokemon sceptile = POOBkemon.createPokemon("Sceptile", moves);
        ArrayList<Pokemon> team1 = new ArrayList<>(List.of(sceptile));
        Player trainer1 = new Player("Ash", team1, testInventory, "red");
        ArrayList<Pokemon> team2 = new ArrayList<>();
        team2.add(POOBkemon.createPokemon("Charizard", new ArrayList<>(List.of(POOBkemon.createMove("Double Team")))));
        DefensiveTrainer trainer2 = new DefensiveTrainer(testInventory);
        BattleField field = new BattleField(trainer1, trainer2);
        while (razorLeaf.isAvailable()) {
            Map<Pokemon, List<List<Action>>> actions = trainer1.getAvailableActions();
            List<Action> moveActions = actions.get(sceptile).get(0);
            Action playerAction = moveActions.get(0);
            Action opponentAction = trainer2.chooseAction(field);
            field.playTurn(playerAction, opponentAction);
        }
        assertTrue(
            sceptile.getMoves().get(0).getName().equals("Struggle"),
            "Debería añadirse 'Struggle' cuando se agotan todos los PP"
        );

    } catch (PoobkemonException e) {
        fail("Error: " + e.getMessage());
    }
}
    @Test
    public void testUsePotionHealsPokemon() {
        try {
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", validMoveSet);
            sceptile.receiveDamage(100);
            int initialPS = sceptile.getPs();
            Potion potion = new Potion();
            potion.Effect(sceptile);

            assertEquals(initialPS + 20, sceptile.getPs(), "La poción debe curar 20 PS");
        } catch (PoobkemonException e) {
            fail("Error inesperado: " + e.getMessage());
        }
    }

    @Test
    public void testReviveRestoresFaintedPokemon() {
        try {
            Pokemon charizard = POOBkemon.createPokemon("Charizard", validMoveSet);
            charizard.receiveDamage(300);
            Revive revive = new Revive();
            revive.Effect(charizard);
            assertAll(
                    () -> assertTrue(charizard.isAlive(), "Debe revivir al Pokémon"),
                    () -> assertEquals(charizard.getMaxPs()/2, charizard.getPs(), "PS deben ser 50% del máximo")
            );
        } catch (PoobkemonException e) {
            fail("Error inesperado: " + e.getMessage());
        }
    }
    @Test
    public void testRainDanceBoostsWaterMoves() {
        try {
            Move waterMove = POOBkemon.createMove("Rain Dance");
            Pokemon user = POOBkemon.createPokemon("Sceptile", new ArrayList<>(List.of(waterMove)));
            Pokemon target = POOBkemon.createPokemon("Charizard", validMoveSet);
            BattleField field = new BattleField(new Player("Test", new ArrayList<>(List.of(user)), testInventory, "red"),
                    new DefensiveTrainer(testInventory));
            waterMove.execute(field, user, target);
            assertEquals("Rain", field.getWeather(), "Debe activar lluvia");
        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }

    @Test
    public void testWeatherDamageFromSandstorm() {
        try {
            Pokemon rockType = POOBkemon.createPokemon("Sceptile", validMoveSet);
            BattleField field = new BattleField(new Player("Test", new ArrayList<>(List.of(rockType)), testInventory, "red"),
                    new DefensiveTrainer(testInventory));
            field.changeWeather("Sandstorm");
            int damage = TypesTable.calculateWeatherDamage(field, rockType);
            assertEquals(rockType.getMaxPs()/16, damage, "Daño de tormenta arena incorrecto");
        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }
    @Test
    public void testFireMoveAgainstGrassType() {
        try {
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", validMoveSet);
            Move flamethrower = POOBkemon.createMove("Flamethrower");
            double multiplier = TypesTable.getEffectivityMultiplier(flamethrower, sceptile);
            assertEquals(2.0, multiplier, 0.1, "Fuego debería ser supereficaz (2x) contra Planta");
        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }
    @Test
    public void testFlamethrowerMayBurnTarget() {
        try {
            Move flamethrower = POOBkemon.createMove("Flamethrower");
            Pokemon attacker = POOBkemon.createPokemon("Charizard", new ArrayList<>(List.of(flamethrower)));
            Pokemon target = POOBkemon.createPokemon("Sceptile", validMoveSet);
            BattleField field = new BattleField(new Player("Test", new ArrayList<>(List.of(attacker)), testInventory, "red"),
                    new DefensiveTrainer(testInventory));
            boolean burned = false;
            for(int i = 0; i < 20; i++) {
                flamethrower.execute(field, attacker, target);
                if(target.getStatusEffect().equals("Burn")) {
                    burned = true;
                    break;
                }
                target.changeStatusEffect("none");
            }
            assertTrue(burned, "Debería aplicar quemadura al menos una vez en 20 intentos");
        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }
    @Test
    public void testDamageCalculation() {
        try {
            ArrayList<Move> moves = new ArrayList<>();
            Move razorLeaf = POOBkemon.createMove("Razor Leaf");
            moves.add(razorLeaf);
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", moves);
            Pokemon charizard = POOBkemon.createPokemon("Charizard", validMoveSet);
            Map<Item, Integer> inventory = new HashMap<>();
            inventory.put(new Potion(), 3);
            Player trainer1 = game.createTrainerPlayer("Ash", new ArrayList<>(List.of(sceptile)), inventory, "red");
            Player trainer2 = game.createTrainerPlayer("Rival", new ArrayList<>(List.of(charizard)), inventory, "blue");
            BattleField battlefield = new BattleField(trainer1, trainer2);
            Map<Pokemon, List<List<Action>>> actions1 = trainer1.getAvailableActions();
            Action moveAction1 = actions1.get(sceptile).get(0).get(0);
            Map<Pokemon, List<List<Action>>> actions2 = trainer2.getAvailableActions();
            Action moveAction2 = actions2.get(charizard).get(0).get(0);
            battlefield.playTurn(moveAction1, moveAction2);
            assertTrue(charizard.getPs() < charizard.getMaxPs(), "El ataque debería reducir los PS");
        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }
    @Test
    public void testWeatherEffectNotification() {
        try {
            ArrayList<Move> moves = new ArrayList<>();
            Move rainDance = POOBkemon.createMove("Rain Dance");
            moves.add(rainDance);
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", moves);
            Map<Item, Integer> inventory = new HashMap<>();
            Player trainer1 = game.createTrainerPlayer("Ash", new ArrayList<>(List.of(sceptile)), inventory, "red");
            DefensiveTrainer trainer2 = new DefensiveTrainer(inventory);
            trainer2.changeTeam(new ArrayList<>(List.of(POOBkemon.createPokemon("Charizard", validMoveSet))));
            BattleField battlefield = new BattleField(trainer1, trainer2);
            Action rainAction = trainer1.getAvailableActions().get(sceptile).get(0).get(0);
            Action opponentAction = trainer2.chooseAction(battlefield);
            List<Notification> log = battlefield.playTurn(rainAction, opponentAction);
            boolean hasRainMessage = log.stream()
                    .filter(n -> n instanceof WeatherNotification)
                    .anyMatch(n -> n.getMessage().get(0).contains("rain"));

            assertTrue(hasRainMessage, "Debería aparecer notificación de lluvia");

        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }
    @Test
    public void testFireMoveSuperEffectiveAgainstGrass() {
        try {
            ArrayList<Move> fireMoves = new ArrayList<>();
            Move flamethrower = POOBkemon.createMove("Flamethrower");
            fireMoves.add(flamethrower);

            Pokemon charizard = POOBkemon.createPokemon("Charizard", fireMoves);
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", validMoveSet);
            Map<Item, Integer> inventory = new HashMap<>();
            Player trainer1 = game.createTrainerPlayer("Ash", new ArrayList<>(List.of(charizard)), inventory, "red");
            DefensiveTrainer trainer2 = new DefensiveTrainer(inventory);
            trainer2.changeTeam(new ArrayList<>(List.of(sceptile)));
            BattleField battlefield = new BattleField(trainer1, trainer2);
            Action fireAction = trainer1.getAvailableActions().get(charizard).get(0).get(0);
            Action opponentAction = trainer2.chooseAction(battlefield);
            List<Notification> log = battlefield.playTurn(fireAction, opponentAction);
            boolean isSuperEffective = log.stream()
                    .filter(n -> n instanceof MoveNotification)
                    .anyMatch(n -> n.getMessage().get(1).contains("super effective"));

            assertTrue(isSuperEffective, "Debería mostrar 'It's super effective!'");

        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }
    @Test
    public void testGrassMoveNotVeryEffectiveAgainstFire() {
        try {
            ArrayList<Move> grassMoves = new ArrayList<>();
            Move razor = POOBkemon.createMove("Razor Leaf");
            grassMoves.add(razor);
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", grassMoves);
            Pokemon charizard = POOBkemon.createPokemon("Charizard", validMoveSet);
            Map<Item, Integer> inventory = new HashMap<>();
            Player trainer1 = game.createTrainerPlayer("Ash", new ArrayList<>(List.of(sceptile)), inventory, "red");
            DefensiveTrainer trainer2 = new DefensiveTrainer(inventory);
            trainer2.changeTeam(new ArrayList<>(List.of(charizard)));
            BattleField battlefield = new BattleField(trainer1, trainer2);
            Action grassAction = trainer1.getAvailableActions().get(sceptile).get(0).get(0);
            Action opponentAction = trainer2.chooseAction(battlefield);
            List<Notification> log = battlefield.playTurn(grassAction, opponentAction);
            boolean isNotEffective = log.stream()
                    .filter(n -> n instanceof MoveNotification)
                    .anyMatch(n -> n.getMessage().get(1).contains("not very effective"));

            assertTrue(isNotEffective, "Debería mostrar 'It's not very effective...'");

        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }
    @Test
    public void testGrassMoveEffectiveAgainstGrass() {
        try {
            ArrayList<Move> grassMoves = new ArrayList<>();
            Move razor = POOBkemon.createMove("Razor Leaf");
            grassMoves.add(razor);
            Pokemon sceptile = POOBkemon.createPokemon("Sceptile", grassMoves);
            Pokemon sceptile2 = POOBkemon.createPokemon("Sceptile", validMoveSet);
            Map<Item, Integer> inventory = new HashMap<>();
            Player trainer1 = game.createTrainerPlayer("Ash", new ArrayList<>(List.of(sceptile)), inventory, "red");
            DefensiveTrainer trainer2 = new DefensiveTrainer(inventory);
            trainer2.changeTeam(new ArrayList<>(List.of(sceptile2)));
            BattleField battlefield = new BattleField(trainer1, trainer2);
            Action grassAction = trainer1.getAvailableActions().get(sceptile).get(0).get(0);
            Action opponentAction = trainer2.chooseAction(battlefield);
            List<Notification> log = battlefield.playTurn(grassAction, opponentAction);
            boolean isNotEffective = log.stream()
                    .filter(n -> n instanceof MoveNotification)
                    .anyMatch(n -> n.getMessage().get(1).contains("effective"));
            assertTrue(isNotEffective, "Debería mostrar 'It's effective'");
        } catch (PoobkemonException e) {
            fail("Error en configuración: " + e.getMessage());
        }
    }

}
