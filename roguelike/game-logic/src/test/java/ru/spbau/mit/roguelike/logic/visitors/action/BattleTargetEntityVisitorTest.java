package ru.spbau.mit.roguelike.logic.visitors.action;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.model.factories.EffectFactory;
import ru.spbau.mit.roguelike.model.factories.EntityFactory;
import ru.spbau.mit.roguelike.model.units.effect.Effect;
import ru.spbau.mit.roguelike.model.units.effect.EffectInstance;
import ru.spbau.mit.roguelike.model.units.entity.BarrierEntity;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.CreepEntity;
import ru.spbau.mit.roguelike.model.units.entity.DropEntity;
import ru.spbau.mit.roguelike.model.units.game.Cell;
import ru.spbau.mit.roguelike.model.units.game.EmptyCell;
import ru.spbau.mit.roguelike.model.units.game.Field;
import ru.spbau.mit.roguelike.model.units.game.Game;
import ru.spbau.mit.roguelike.model.units.item.equipment.Weapon;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BattleTargetEntityVisitorTest {
    private Game game;
    private BattleTargetEntityVisitor weekCharacterVisitor;
    private BattleTargetEntityVisitor weekCreepVisitor;
    private BattleTargetEntityVisitor strongCharacterVisitor;
    private BattleTargetEntityVisitor strongCreepVisitor;
    private BattleTargetEntityVisitor characterWithWeaponvisitor;
    private BattleTargetEntityVisitor creepWithWeaponVisitor;

    private List<Effect> weaponEffects = Arrays.asList(
            EffectFactory.getDiseaseEffect(100, 1000),
            EffectFactory.getStatsEffect(100, 1000, false)
    );


    @Before
    public void before() {
        Cell[][] cells = new Cell[92][92];
        for (int i = 0; i < 92; i++) {
            for (int j = 0; j < 92; j++) {
                cells[i][j] = new EmptyCell();
            }
        }
        game = new Game(new Field(cells, 90, 90));
        CharacterEntity weekCharacter = EntityFactory.getCharacter("Week");

        CreepEntity weekCreep = EntityFactory.getCreep(1);

        CreepEntity strongCreep = EntityFactory.getCreep(1000);
        strongCreep.getEquipment().clear();

        CharacterEntity strongCharacter = EntityFactory.getCharacter("Strong");
        strongCharacter.getCurrentStatDescriptor().setIntelligence(1000);
        strongCharacter.getCurrentStatDescriptor().setWisdom(1000);
        strongCharacter.getCurrentStatDescriptor().setLuck(1000);
        strongCharacter.resetStateDescriptor();

        Weapon ultimateWeapon = new Weapon("Supreme", 1);
        weaponEffects.forEach(ultimateWeapon::addtargetEffect);

        CharacterEntity characterWithWeapon = EntityFactory.getCharacter("Hero");
        characterWithWeapon.addEquipment(ultimateWeapon);

        CreepEntity creepWithWeapon = EntityFactory.getCreep(1);
        creepWithWeapon.addEquipment(ultimateWeapon);

        weekCharacterVisitor = new BattleTargetEntityVisitor(weekCharacter, "Week", game);
        weekCreepVisitor = new BattleTargetEntityVisitor(weekCreep, "Week", game);
        strongCharacterVisitor = new BattleTargetEntityVisitor(strongCharacter, "Strong", game);
        strongCreepVisitor = new BattleTargetEntityVisitor(strongCreep, "Strong creep", game);
        characterWithWeaponvisitor = new BattleTargetEntityVisitor(characterWithWeapon, "Weapon", game);
        creepWithWeaponVisitor = new BattleTargetEntityVisitor(creepWithWeapon, "Weapon", game);
    }

    @Test
    public void visitBarrier() throws Exception {
        BarrierEntity source = EntityFactory.getImmortalBarrier();
        game.moveOrSpawnEntity(Point.of(20, 20), source);
        weekCharacterVisitor.visit(source);
        strongCharacterVisitor.visit(source);
        characterWithWeaponvisitor.visit(source);
        weekCreepVisitor.visit(source);
        strongCreepVisitor.visit(source);
        creepWithWeaponVisitor.visit(source);
        assertNotEquals(null, game.getEntityById(source.getId()));
        assertEquals(0, source.getEffectInstances().size());
    }

    @Test
    public void visitDrop() throws Exception {
        DropEntity source = EntityFactory.getDrop(20);
        game.moveOrSpawnEntity(Point.of(20, 20), source);
        weekCreepVisitor.visit(source);
        strongCreepVisitor.visit(source);
        creepWithWeaponVisitor.visit(source);
        assertNotEquals(null, game.getEntityById(source.getId()));
        assertEquals(0, source.getEffectInstances().size());
        weekCharacterVisitor.visit(source);
        assertEquals(null, game.getEntityById(source.getId()));
    }

    @Test
    public void visitCreep() throws Exception {
        CreepEntity source = EntityFactory.getCreep(10);
        game.moveOrSpawnEntity(Point.of(20, 20), source);
        weekCreepVisitor.visit(source);
        strongCreepVisitor.visit(source);
        creepWithWeaponVisitor.visit(source);
        assertNotEquals(null, game.getEntityById(source.getId()));
        assertEquals(0, source.getEffectInstances().size());
        weekCharacterVisitor.visit(source);
        assertNotEquals(null, game.getEntityById(source.getId()));
        characterWithWeaponvisitor.visit(source);
        assertNotEquals(null, game.getEntityById(source.getId()));
        assertEquals(weaponEffects.size(), source.getEffectInstances().size());
        for (EffectInstance instance : source.getEffectInstances()) {
            assertTrue(weaponEffects.contains(instance.getEffect()));
        }
        strongCharacterVisitor.visit(source);
        assertEquals(null, game.getEntityById(source.getId()));
    }

    @Test
    public void visitCharacter() throws Exception {
        CharacterEntity source = EntityFactory.getCharacter("Rofler");
        game.moveOrSpawnEntity(Point.of(20, 20), source);
        weekCreepVisitor.visit(source);
        assertEquals(0, source.getEffectInstances().size());
        assertNotEquals(null, game.getEntityById(source.getId()));
        creepWithWeaponVisitor.visit(source);
        assertNotEquals(null, game.getEntityById(source.getId()));
        assertEquals(weaponEffects.size(), source.getEffectInstances().size());
        for (EffectInstance instance : source.getEffectInstances()) {
            assertTrue(weaponEffects.contains(instance.getEffect()));
        }
        strongCreepVisitor.visit(source);
        assertEquals(null, game.getEntityById(source.getId()));
    }

}