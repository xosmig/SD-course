package ru.spbau.mit.roguelike.uicommon.views;

import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.logic.GameExecutor;
import ru.spbau.mit.roguelike.logic.visitors.action.EntityAction;
import ru.spbau.mit.roguelike.model.units.entity.*;
import ru.spbau.mit.roguelike.model.units.game.Game;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;
import ru.spbau.mit.roguelike.uicommon.views.entity.EntityView;
import ru.spbau.mit.roguelike.uicommon.views.inventory.InventoryView;

import java.util.concurrent.TimeUnit;

/**
 * Simple game view
 */
public abstract class GameView extends View {
    private final CharacterEntity player;
    private final GameExecutor game;
    private boolean exit = false;
    private Point cameraPosition;
    private Point playerPosition;
    private long millisPerTurn = 25;


    public GameView(View parentView, CharacterEntity player) {
        super(parentView);
        this.player = player;
        game = new GameExecutor(this::onPlayerAction);
        playerPosition = game.spawnPlayer(player);
        cameraPosition = playerPosition;
    }

    protected EntityAction onPlayerAction(CharacterEntity player) {
        playerPosition = game.getGame().getEntityPositionById(player.getId());
        cameraPosition = playerPosition;
        draw();
        return onPlayerAction();
    }

    protected void refreshState(WorldEntity entity){
        game.refreshStatAndState(entity);
    }

    protected abstract void drawStats(StatDescriptor currentStat, StatDescriptor baseStat);
    protected abstract void drawState(StateDescriptor currentState, StateDescriptor ultState);
    protected abstract void drawField(Point cameraPosition, Game game);
    protected abstract void drawNameAndLevel(String name, int level);
    protected abstract EntityAction onPlayerAction();
    protected abstract InventoryView constructInventoryView(CharacterEntity player);
    protected abstract EntityView constructEntityView(WorldEntity player);

    protected void moveCamera(Point delta) {
        cameraPosition = Point.of(cameraPosition.getX() + delta.getX(), cameraPosition.getY() + delta.getY());
        draw();
    }

    protected void openInventory() {
        constructInventoryView(player).open();
    }

    protected void openEntityView() {
        EntityViewOpener opener = new EntityViewOpener();
        game.getGame().visitEntityAt(cameraPosition, opener);
        if (!opener.opened) {
            constructEntityView(player).open();
        }
    }

    protected void exit() {
        exit = true; //Todo exit without last turn
    }

    public void setMillisPerTurn(long millisPerTurn) {
        this.millisPerTurn = millisPerTurn;
    }

    @Override
    public void run() {
        while (!exit) {
            game.makeTurn();
            draw();
            try {
                TimeUnit.MILLISECONDS.sleep(millisPerTurn);
            } catch (InterruptedException e) {
                //Continue;
            }
        }
    }

    @Override
    protected void draw() {
        drawNameAndLevel(player.getName(), player.getLevel());
        drawStats(player.getCurrentStatDescriptor(), player.getBaseStatDescriptor());
        drawState(player.getStateDescriptor(), player.getCurrentStatDescriptor().generateStateDescriptor());
        drawField(cameraPosition, game.getGame());
    }

    private class EntityViewOpener implements EntityVisitor {
        private boolean opened = false;

        private void def() {
            opened = true;
            constructEntityView(player).open();
        }

        @Override
        public void visit(BarrierEntity entity) {
            def();
        }

        @Override
        public void visit(CharacterEntity entity) {
            def();
        }

        @Override
        public void visit(DropEntity entity) {
            def();
        }

        @Override
        public void visit(CreepEntity entity) {
            opened = true;
            constructEntityView(entity).open();
        }
    }
}
