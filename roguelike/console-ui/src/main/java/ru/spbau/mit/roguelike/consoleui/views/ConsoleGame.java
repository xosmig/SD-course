package ru.spbau.mit.roguelike.consoleui.views;

import org.codetome.zircon.api.Position;
import org.codetome.zircon.api.color.TextColorFactory;
import org.codetome.zircon.api.terminal.Terminal;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.commons.logging.Logging;
import ru.spbau.mit.roguelike.consoleui.LoggingHolder;
import ru.spbau.mit.roguelike.consoleui.UserInputHolder;
import ru.spbau.mit.roguelike.logic.visitors.action.EntityAction;
import ru.spbau.mit.roguelike.model.units.entity.*;
import ru.spbau.mit.roguelike.model.units.game.Game;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;
import ru.spbau.mit.roguelike.uicommon.views.GameView;
import ru.spbau.mit.roguelike.uicommon.views.View;
import ru.spbau.mit.roguelike.uicommon.views.entity.EntityView;
import ru.spbau.mit.roguelike.uicommon.views.inventory.InventoryView;

/**
 * Console game view
 */
public class ConsoleGame extends GameView {
    private final static int LOG_WIDTH = 23;
    private final Terminal terminal;
    private final LoggingHolder log;
    private final int right;
    private final int bottom;
    private final int fieldCenterX;
    private final int fieldCenterY;
    private boolean freeCamera = false;


    public ConsoleGame(View parentView, CharacterEntity player, Terminal terminal) {
        super(parentView, player);
        this.terminal = terminal;
        right = terminal.getBoundableSize().getColumns();
        bottom = terminal.getBoundableSize().getRows();
        fieldCenterX = (right - LOG_WIDTH) / 2;
        fieldCenterY = (bottom - 3) / 2;
        log = new LoggingHolder(LOG_WIDTH, bottom - 4);
        Logging.addHandler(log);
    }

    @Override
    protected void draw() {
        terminal.clear();
        drawGUIBorders();
        writeLog();
        super.draw();
        if (freeCamera) {
            terminal.setForegroundColor(TextColorFactory.fromRGB(0, 0, 235));
            terminal.setCharacterAt(Position.of(fieldCenterX, fieldCenterY), '+');
            terminal.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
        }
        terminal.flush();
    }

    protected void writeLog() {
        int i = 0;
        for (String s : log.getLog()) {
            TerminalUtils.printText(s, terminal, Position.of(right - LOG_WIDTH, i));
            i++;
        }
    }

    protected void drawGUIBorders() {
        TerminalUtils.horizontalLine(terminal, Position.of(0, bottom - 4), right);
        TerminalUtils.verticalLine(terminal, Position.of(right - LOG_WIDTH - 1, 0), bottom - 5);
        terminal.setCharacterAt(Position.of(right - LOG_WIDTH - 1, bottom - 5), '\\');
        TerminalUtils.printDesignations(terminal, bottom - 3);

    }

    @Override
    protected void drawStats(StatDescriptor currentStat, StatDescriptor baseStat) {
        TerminalUtils.printStat(terminal, bottom - 3, currentStat, baseStat);
    }

    @Override
    protected void drawState(StateDescriptor currentState, StateDescriptor ultState) {
        TerminalUtils.printState(terminal, bottom - 3, currentState, ultState);
    }

    @Override
    protected void drawField(Point cameraPosition, Game game) {
        for (int x = 0; x < right - LOG_WIDTH - 1; x++) {
            for (int y = 0; y < bottom - 4; y++) {
                Point position = Point.of(
                        cameraPosition.getX() - fieldCenterX + x,
                        cameraPosition.getY() - fieldCenterY + y
                );
                Position terminalPosition = Position.of(x, y);
                if (position.getX() < 0 || position.getX() > game.getWidth() ||
                        position.getY() < 0 || position.getY() > game.getHeight()) {

                    terminal.setForegroundColor(TextColorFactory.fromRGB(0, 0, 0));
                    terminal.setBackgroundColor(TextColorFactory.fromRGB(90, 90, 90));
                    terminal.setCharacterAt(terminalPosition, '#');
                    terminal.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
                    terminal.setBackgroundColor(TextColorFactory.DEFAULT_BACKGROUND_COLOR);
                    continue;
                }
                game.visitEntityAt(position, new EntityDrawer(terminalPosition));
            }
        }
    }

    @Override
    protected void drawNameAndLevel(String name, int level) {
        TerminalUtils.printNameAndLevel(terminal, bottom - 3, name, level);
    }

    @Override
    protected EntityAction onPlayerAction() {
        UserInputHolder.clear(terminal);
        log.add("~~Next turn~~");
        freeCamera = false;
        while (true) {
            try {
                char key = UserInputHolder.poll(terminal);
                switch (key) {
                    case 'w': {
                        if (freeCamera) {
                            moveCamera(Point.of(0, -1));
                            break;
                        } else {
                            return EntityAction.UP;
                        }
                    }
                    case 'a': {
                        if (freeCamera) {
                            moveCamera(Point.of(-1, 0));
                            break;
                        } else {
                            return EntityAction.LEFT;
                        }
                    }
                    case 'd': {
                        if (freeCamera) {
                            moveCamera(Point.of(+1, 0));
                            break;
                        } else {
                            return EntityAction.RIGHT;
                        }
                    }
                    case 's': {
                        if (freeCamera) {
                            moveCamera(Point.of(0, +1));
                            break;
                        } else {
                            return EntityAction.DOWN;
                        }
                    }
                    case 'c': {
                        freeCamera = !freeCamera;
                        if (!freeCamera) {
                            draw();
                        }
                        break;
                    }
                    case ' ': {
                        openEntityView();
                        break;
                    }
                    case 'e': {
                        openInventory();
                        break;
                    }
                    case 'q': {
                        exit();
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected InventoryView constructInventoryView(CharacterEntity player) {
        return new ConsoleInventory(this, player, () -> refreshState(player), terminal);
    }

    @Override
    protected EntityView constructEntityView(WorldEntity entity) {
        String name = "";
        if (entity instanceof CharacterEntity) {
            name = ((CharacterEntity) entity).getName();
        }
        if (entity instanceof CreepEntity) {
            name = ((CreepEntity) entity).getName();
        }
        return new ConsoleEntityView(this, entity, terminal, name);
    }

    private class EntityDrawer implements EntityVisitor {
        private final Position position;

        private EntityDrawer(Position position) {
            this.position = position;
        }

        @Override
        public void visit(BarrierEntity entity) {
            if (entity.isImmortal()) {
                terminal.setForegroundColor(TextColorFactory.fromRGB(0, 0, 0));
                terminal.setBackgroundColor(TextColorFactory.fromRGB(90, 90, 90));
                terminal.setCharacterAt(position, '#');
                terminal.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
                terminal.setBackgroundColor(TextColorFactory.DEFAULT_BACKGROUND_COLOR);
            } else {
                terminal.setForegroundColor(TextColorFactory.fromRGB(200, 200, 0));
                terminal.setBackgroundColor(TextColorFactory.fromRGB(90, 90, 90));
                terminal.setCharacterAt(position, '?');
                terminal.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
                terminal.setBackgroundColor(TextColorFactory.DEFAULT_BACKGROUND_COLOR);
            }
        }

        @Override
        public void visit(CharacterEntity entity) {
            terminal.setForegroundColor(TextColorFactory.fromRGB(0, 230, 0));
            terminal.setCharacterAt(position, '@');
            terminal.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
        }

        @Override
        public void visit(DropEntity entity) {
            terminal.setForegroundColor(TextColorFactory.fromRGB(230, 230, 0));
            terminal.setCharacterAt(position, '!');
            terminal.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
        }

        @Override
        public void visit(CreepEntity entity) {
            terminal.setForegroundColor(TextColorFactory.fromRGB(230, 0, 0));
            terminal.setCharacterAt(position, '&');
            terminal.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
        }
    }
}
