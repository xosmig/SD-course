package ru.spbau.mit.roguelike.uicommon;

import org.junit.Test;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.logic.visitors.action.EntityAction;
import ru.spbau.mit.roguelike.model.factories.EffectFactory;
import ru.spbau.mit.roguelike.model.factories.EntityFactory;
import ru.spbau.mit.roguelike.model.factories.ItemFactory;
import ru.spbau.mit.roguelike.model.units.effect.Effect;
import ru.spbau.mit.roguelike.model.units.effect.EffectInstance;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.StateDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;
import ru.spbau.mit.roguelike.model.units.game.Game;
import ru.spbau.mit.roguelike.model.units.item.Item;
import ru.spbau.mit.roguelike.model.units.item.equipment.Equipment;
import ru.spbau.mit.roguelike.uicommon.views.GameView;
import ru.spbau.mit.roguelike.uicommon.views.MainMenuView;
import ru.spbau.mit.roguelike.uicommon.views.View;
import ru.spbau.mit.roguelike.uicommon.views.elements.TextButton;
import ru.spbau.mit.roguelike.uicommon.views.entity.EntityView;
import ru.spbau.mit.roguelike.uicommon.views.inventory.InventoryView;

import java.util.List;

import static org.junit.Assert.*;


public class CommonUITest {
    @Test
    public void run() throws Exception {
        CharacterEntity player = EntityFactory.getCharacter("Tester");
        player.getInventory().addItem(ItemFactory.getWeapon(1));
        player.getInventory().addItem(ItemFactory.getWeapon(1));
        player.addEquipment(ItemFactory.getHeadArmor(1));
        player.addEquipment(ItemFactory.getLegArmor(1));
        player.addEquipment(ItemFactory.getLegArmor(1));
        player.addEffectInstance(EffectFactory.getDiseaseEffect(1000, 100).instantiate());
        player.addEffectInstance(EffectFactory.getRegenerationEffect(1000, 100).instantiate());
        new GameV(null, player).run();
    }


    private class MainMenuV extends MainMenuView {
        @Override
        public void startGame(CharacterEntity player) {

        }

        @Override
        public void exit() {

        }

        @Override
        protected void draw(TextButton b) {

        }

        @Override
        public void run() {

        }
    }

    private class EntityV extends EntityView {
        private int drawStatsCalls = 0;
        private int drawStateCalls = 0;
        private int drawEquipment = 0;
        private int drawInventory = 0;
        private Equipment prevEquipment = null;
        private Equipment lastEquipment = null;
        private EffectInstance lastEffect = null;
        private EffectInstance prevEffect = null;

        public EntityV(View parentView, WorldEntity entity) {
            super(parentView, entity);
        }

        @Override
        protected void drawStat(StatDescriptor current, StatDescriptor base) {
            drawStatsCalls++;
        }

        @Override
        protected void drawState(StateDescriptor current, StateDescriptor currentStatDefault) {
            drawStateCalls++;
        }

        @Override
        protected void drawEquipment(List<Equipment> equipment) {
            prevEquipment = lastEquipment;
            lastEquipment = equipment.get(0);
            drawEquipment++;
        }


        @Override
        protected void drawEffects(List<EffectInstance> effects) {
            prevEffect = lastEffect;
            lastEffect = effects.get(0);
            drawInventory++;
        }

        @Override
        public void run() {
            swapScrolling();
            assertEquals(lastEffect, prevEffect);
            assertEquals(lastEquipment, prevEquipment);
            if (isScrollingEffects()) {
                swapScrolling();
            }

            scrollUp();
            assertEquals(lastEffect, prevEffect);
            assertEquals(lastEquipment, prevEquipment);

            scrollDown();
            assertEquals(lastEffect, prevEffect);
            assertNotEquals(lastEquipment, prevEquipment);

            scrollUp();
            assertEquals(lastEffect, prevEffect);
            assertNotEquals(lastEquipment, prevEquipment);

            swapScrolling();

            scrollUp();
            assertEquals(lastEquipment, prevEquipment);
            assertEquals(lastEffect, prevEffect);

            scrollDown();
            assertEquals(lastEquipment, prevEquipment);
            assertNotEquals(lastEffect, prevEffect);

            scrollUp();
            assertEquals(lastEquipment, prevEquipment);
            assertNotEquals(lastEffect, prevEffect);

            assertEquals(9, drawEquipment);
            assertEquals(9, drawInventory);
            assertEquals(9, drawStateCalls);
            assertEquals(9, drawStatsCalls);
        }
    }

    private class InventoryV extends InventoryView {
        private CharacterEntity entity;
        private int drawStatsCalls = 0;
        private int drawStateCalls = 0;
        private int drawEquipment = 0;
        private int drawInventory = 0;
        private Equipment prevEquipment = null;
        private Equipment lastEquipment = null;
        private Item lastItem = null;
        private Item prevItem = null;

        public InventoryV(View parentView, CharacterEntity entity, Runnable onEntityEquipmentChange) {
            super(parentView, entity, onEntityEquipmentChange);
            this.entity = entity;
        }

        @Override
        protected void drawStat(StatDescriptor current, StatDescriptor base) {
            drawStatsCalls++;
        }

        @Override
        protected void drawState(StateDescriptor current, StateDescriptor currentStatDefault) {
            drawStateCalls++;
        }

        @Override
        protected void drawEquipment(List<Equipment> equipment) {
            prevEquipment = lastEquipment;
            lastEquipment = equipment.get(0);
            drawEquipment++;
        }

        @Override
        protected void drawInventory(List<Item> items) {
            prevItem = lastItem;
            lastItem = items.get(0);
            drawInventory++;
        }

        @Override
        public void run() {
            swapScrolling();
            assertEquals(lastItem, prevItem);
            assertEquals(lastEquipment, prevEquipment);
            if (isScrollingEquipment()) {
                swapScrolling();
            }
            scrollUp();
            assertEquals(lastItem, prevItem);
            assertEquals(lastEquipment, prevEquipment);

            scrollDown();
            assertNotEquals(lastItem, prevItem);
            assertEquals(lastEquipment, prevEquipment);

            useSelectedItem();
            assertNotEquals(lastItem, prevItem);
            assertTrue(entity.getEquipment().contains(prevItem));
            assertFalse(entity.getInventory().getItems().contains(prevItem));

            unequipSelectedEquipment();
            assertFalse(entity.getEquipment().contains(prevEquipment));
            assertTrue(entity.getInventory().getItems().contains(prevEquipment));

            deleteSelectedItem();
            assertFalse(entity.getEquipment().contains(prevItem));
            assertFalse(entity.getInventory().getItems().contains(prevItem));

            assertEquals(7, drawEquipment);
            assertEquals(7, drawInventory);
            assertEquals(7, drawStateCalls);
            assertEquals(7, drawStatsCalls);
        }
    }

    private class GameV extends GameView {
        private int drawStatsCalls = 0;
        private int drawStateCalls = 0;
        private int drawFieldCalls = 0;
        private int drawNameCalls = 0;

        public GameV(View parentView, CharacterEntity player) {
            super(parentView, player);
        }

        @Override
        protected void drawStats(StatDescriptor currentStat, StatDescriptor baseStat) {
            drawStatsCalls++;
        }

        @Override
        protected void drawState(StateDescriptor currentState, StateDescriptor ultState) {
            drawStateCalls++;
        }

        @Override
        protected void drawField(Point cameraPosition, Game game) {
            drawFieldCalls++;
        }

        @Override
        protected void drawNameAndLevel(String name, int level) {
            drawNameCalls++;
        }

        @Override
        protected EntityAction onPlayerAction() {
            openInventory();
            openEntityView();
            moveCamera(Point.of(5, 5));
            moveCamera(Point.of(-5, -5));
            assertEquals(13, drawFieldCalls);
            assertEquals(13, drawStateCalls);
            assertEquals(13, drawStatsCalls);
            assertEquals(13, drawNameCalls);
            exit();
            return EntityAction.LEFT;
        }

        @Override
        protected InventoryView constructInventoryView(CharacterEntity player) {
            return new InventoryV(this, player, () -> refreshState(player));
        }

        @Override
        protected EntityView constructEntityView(WorldEntity player) {
            return new EntityV(this, player);
        }
    }
}