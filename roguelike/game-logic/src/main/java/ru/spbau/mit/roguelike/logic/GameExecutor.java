package ru.spbau.mit.roguelike.logic;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.logic.visitors.action.BattleTargetEntityVisitor;
import ru.spbau.mit.roguelike.logic.visitors.action.EntityAction;
import ru.spbau.mit.roguelike.logic.visitors.action.MakeTurnEntityVisitor;
import ru.spbau.mit.roguelike.logic.visitors.effects.ApplyStatEffectVisitor;
import ru.spbau.mit.roguelike.logic.visitors.effects.ApplyStateEffectVisitor;
import ru.spbau.mit.roguelike.logic.visitors.effects.CheckEquipmentVisitor;
import ru.spbau.mit.roguelike.model.factories.EntityFactory;
import ru.spbau.mit.roguelike.model.factories.FieldFactory;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.CreepEntity;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;
import ru.spbau.mit.roguelike.model.units.entity.inventory.Inventory;
import ru.spbau.mit.roguelike.model.units.game.Game;
import ru.spbau.mit.roguelike.model.units.item.equipment.Equipment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Executes game turn-by-turn
 */
public class GameExecutor {
    private final Game game;
    private final Map<Integer, Integer> id2lastTurn = new HashMap<>();
    private final MakeTurnEntityVisitor makeTurnEntityVisitor;
    private final Set<CharacterEntity> players = new HashSet<>();
    private int turn = 0;

    public GameExecutor(Function<CharacterEntity, EntityAction> onPlayerAction) {
        game = new Game(FieldFactory.generateField(
                Configuration.getInt("GAME_FIELD_HEIGHT"),
                Configuration.getInt("GAME_FIELD_WIDTH")));
        makeTurnEntityVisitor = new MakeTurnEntityVisitor(onPlayerAction, game);
        long monsterCount = Configuration.getInt("GAME_MONSTERS_COUNT") -
                game.getEntitities().stream().filter(e -> e instanceof CreepEntity).count();
        while (monsterCount > 0) {
            spawnCreep();
            monsterCount--;
        }
    }

    public Point spawnPlayer(CharacterEntity player) {
        players.add(player);
        while (!game.moveOrSpawnEntity(RandomUtils.getPoint(game.getWidth(), game.getHeight()), player)) {}
        id2lastTurn.put(player.getId(), turn);
        return game.getEntityPositionById(player.getId());
    }

    public void makeTurn() {
        turn++;
        //Firstly apply effects
        Set<WorldEntity> gameEntities = game.getEntitities();
        for (WorldEntity entity : gameEntities) {
            boolean statsChanging = true;
            while (statsChanging) {
                entity.resetCurrentStatDescriptor();
                ApplyStatEffectVisitor.process(entity);
                Set<Equipment> dropedEquipment = CheckEquipmentVisitor.process(entity);
                statsChanging = !dropedEquipment.isEmpty();
                dropedEquipment.forEach(entity::removeEquipment);
                if (entity instanceof CharacterEntity) {
                    Inventory inventory = ((CharacterEntity) entity).getInventory();
                    dropedEquipment.forEach(inventory::addItem);
                }
            }
            entity.resetStateDescriptor();
            ApplyStateEffectVisitor.process(entity, game);
        }
        //Secondly make turn
        gameEntities = game.getEntitities();
        for (WorldEntity entity : gameEntities) {
            if (game.getEntityById(entity.getId()) != null) {
                if (turn - id2lastTurn.getOrDefault(entity.getId(),0) >=
                        entity.getCurrentStatDescriptor().getSlowness()) {
                    entity.accept(makeTurnEntityVisitor);
                    id2lastTurn.put(entity.getId(), turn);
                }
            } else {
                id2lastTurn.remove(entity.getId());
            }
        }

        long monsterCount = Configuration.getInt("GAME_MONSTERS_COUNT") -
                game.getEntitities().stream().filter(e -> e instanceof CreepEntity).count();
        while (monsterCount > 0) {
            spawnCreep();
            monsterCount--;
        }

        for (CharacterEntity player : players) {
            if (player.getLevel() < game.getWorldLevel()) {
                StatDescriptor ps = player.getBaseStatDescriptor();
                int increase = game.getWorldLevel() - player.getLevel();
                ps.setStrength(ps.getStrength() + increase);
                ps.setDexterity(ps.getDexterity() + increase);
                ps.setStamina(ps.getStamina() + increase);
                ps.setIntelligence(ps.getIntelligence() + increase);
                ps.setWisdom(ps.getWisdom() + increase);
                ps.setLuck(ps.getLuck() + increase);
                player.setLevel(game.getWorldLevel());
            }
            if (game.getEntityById(player.getId()) == null) {
                spawnPlayer(player);
            }
        }
    }

    public Game getGame() {
        return game;
    }

    private void spawnCreep() {
        CreepEntity creep = EntityFactory.getCreep(game.getWorldLevel());
        while (!game.moveOrSpawnEntity(RandomUtils.getPoint(game.getWidth(), game.getHeight()), creep)) {}
        id2lastTurn.put(creep.getId(), turn);
    }
}
