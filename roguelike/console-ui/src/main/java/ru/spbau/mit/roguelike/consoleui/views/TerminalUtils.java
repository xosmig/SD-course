package ru.spbau.mit.roguelike.consoleui.views;

import org.codetome.zircon.api.Position;
import org.codetome.zircon.api.color.TextColor;
import org.codetome.zircon.api.color.TextColorFactory;
import org.codetome.zircon.api.terminal.Terminal;
import ru.spbau.mit.roguelike.model.units.effect.*;
import ru.spbau.mit.roguelike.model.units.entity.StatDescriptor;
import ru.spbau.mit.roguelike.model.units.entity.StateDescriptor;
import ru.spbau.mit.roguelike.model.units.item.Item;
import ru.spbau.mit.roguelike.model.units.item.equipment.*;
import ru.spbau.mit.roguelike.model.visitors.EffectVisitor;
import ru.spbau.mit.roguelike.model.visitors.ItemVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils for working with terminal
 */
public final class TerminalUtils {
    public static final int STAT_START_FROM_RIGHT = 44;
    public static final int STATE_START_FROM_RIGHT = 57;

    public static void printText(String s, Terminal t, Position pos) {
        t.putCursorAt(pos);
        for (char c : s.toCharArray()) {
            t.putCharacter(c);
        }
    }

    public static void horizontalLine(Terminal t, Position start, int len) {
        for (int i = 0; i < len; i++) {
            t.setCharacterAt(Position.of(start.getColumn() + i, start.getRow()), '—');
        }
    }

    public static void verticalLine(Terminal t, Position start, int len) {
        for (int i = 0; i < len; i++) {
            t.setCharacterAt(Position.of(start.getColumn(), start.getRow() + i), '|');
        }
    }

    public static void fill(Terminal t, Position start, Position end, TextColor color) {
        t.setBackgroundColor(color);
        for (int i = start.getColumn(); i <= end.getColumn(); i++) {
            for (int j = start.getColumn(); j <= end.getColumn(); j++) {
                t.setCharacterAt(Position.of(i, j), ' ');
            }
        }
        t.setBackgroundColor(TextColorFactory.DEFAULT_BACKGROUND_COLOR);
    }

    public static void printDesignations(Terminal t, int row) {
        int barsLen = t.getBoundableSize().getColumns() - 87 + 8;
        TerminalUtils.printText(" HP [", t, Position.of(0, row + 1));
        TerminalUtils.printText(" MP [", t, Position.of(0, row + 2));
        TerminalUtils.printText("]     /     +   | P.DMG      | STR     /     INT     /     DEX     /",
                t, Position.of(5 + barsLen, row + 1));
        TerminalUtils.printText("]     /     +   | M.DMG      | STM     /     WIS     /     LCK     /",
                t, Position.of(5 + barsLen, row + 2));
        TerminalUtils.printText("| LVL        | SLOWNESS     /",
                t, Position.of(21 + barsLen, row));
    }

    public static void printStat(Terminal t, int row, StatDescriptor currentStat, StatDescriptor baseStat) {
        int offset = t.getBoundableSize().getColumns() - 87 + 8 + 35;
        TerminalUtils.printText(String.valueOf(currentStat.getStrength()), t, Position.of(offset + 5, row + 1));
        TerminalUtils.printText(String.valueOf(baseStat.getStrength()), t, Position.of(offset + 10, row + 1));

        TerminalUtils.printText(String.valueOf(currentStat.getIntelligence()), t, Position.of(offset + 19, row + 1));
        TerminalUtils.printText(String.valueOf(baseStat.getIntelligence()), t, Position.of(offset + 24, row + 1));

        TerminalUtils.printText(String.valueOf(currentStat.getDexterity()), t, Position.of(offset + 33, row + 1));
        TerminalUtils.printText(String.valueOf(baseStat.getDexterity()), t, Position.of(offset + 38, row + 1));


        TerminalUtils.printText(String.valueOf(currentStat.getStamina()), t, Position.of(offset + 5, row + 2));
        TerminalUtils.printText(String.valueOf(baseStat.getStamina()), t, Position.of(offset + 10, row + 2));

        TerminalUtils.printText(String.valueOf(currentStat.getWisdom()), t, Position.of(offset + 19, row + 2));
        TerminalUtils.printText(String.valueOf(baseStat.getWisdom()), t, Position.of(offset + 24, row + 2));

        TerminalUtils.printText(String.valueOf(currentStat.getLuck()), t, Position.of(offset + 33, row + 2));
        TerminalUtils.printText(String.valueOf(baseStat.getLuck()), t, Position.of(offset + 38, row + 2));


        TerminalUtils.printText(String.valueOf(currentStat.getSlowness()), t, Position.of(offset + 10, row));
        TerminalUtils.printText(String.valueOf(baseStat.getSlowness()), t, Position.of(offset + 15, row));
    }

    public static void printState(Terminal t, int row, StateDescriptor currentState, StateDescriptor ultState) {
        int barsLen = t.getBoundableSize().getColumns() - 87 + 8;
        int hpParts = (barsLen * currentState.getHealth()) / ultState.getHealth();
        int mpParts = (barsLen * currentState.getMana()) / ultState.getMana();
        int offset = 5;
        t.setForegroundColor(TextColorFactory.fromRGB(220, 0, 0));
        for (int i = 0; i < hpParts; i++) {
            t.setCharacterAt(Position.of(i + offset, row + 1), '|');
        }
        for (int i = 0; i < mpParts; i++) {
            t.setForegroundColor(TextColorFactory.fromRGB(0, 220, 220));
            t.setCharacterAt(Position.of(i + offset, row + 2), '|');
        }
        t.setForegroundColor(TextColorFactory.DEFAULT_FOREGROUND_COLOR);
        offset = 7 + barsLen;
        TerminalUtils.printText(String.valueOf(currentState.getHealth()), t, Position.of(offset, row + 1));
        TerminalUtils.printText(String.valueOf(ultState.getHealth()), t, Position.of(offset + 5, row + 1));
        TerminalUtils.printText(String.valueOf(currentState.getMana()), t, Position.of(offset, row + 2));
        TerminalUtils.printText(String.valueOf(ultState.getMana()), t, Position.of(offset + 5, row + 2));


        TerminalUtils.printText(String.valueOf(currentState.getRegeneration()), t, Position.of(offset + 11, row + 1));
        TerminalUtils.printText(String.valueOf(currentState.getManaRegeneration()), t, Position.of(offset + 11, row + 2));

        TerminalUtils.printText(String.valueOf(currentState.getPhysicalDamage()), t, Position.of(offset + 22, row + 1));
        TerminalUtils.printText(String.valueOf(currentState.getMagicalDamage()), t, Position.of(offset + 22, row + 2));

    }

    public static void printNameAndLevel(Terminal t, int row, String name, int level) {
        int offset = 27 + t.getBoundableSize().getColumns() - 87 + 8;

        TerminalUtils.printText(name, t, Position.of(1, row));
        TerminalUtils.printText(String.valueOf(level), t, Position.of(offset, row));
    }

    public static void printItem(Terminal t, Position pos, Item item) {
        PrintItemVisitor visitor = new PrintItemVisitor();
        item.accept(visitor);
        printText(item.getName(), t, pos);
        printText(visitor.type, t, Position.of(pos.getColumn(), pos.getRow() + 1));
        printText("Effects", t, Position.of(pos.getColumn() + 1, pos.getRow() + 2));
        horizontalLine(t, Position.of(pos.getColumn() + 8, pos.getRow() + 2), 30);
        verticalLine(t,  Position.of(pos.getColumn() + 1, pos.getRow() + 3), 17);
        verticalLine(t,  Position.of(pos.getColumn() + 20, pos.getRow() + 3), 17);
        verticalLine(t,  Position.of(pos.getColumn() + 38, pos.getRow() + 3), 17);
        horizontalLine(t, Position.of(pos.getColumn() + 1, pos.getRow() + 19), 37);
        int p = 0;
        boolean left = true;
        for (Effect effect : visitor.passiveEffects) {
            p += printEffect(t, Position.of(pos.getColumn() + (left ? 3 : 22), pos.getRow() + 3 + p), effect);
            if (p > 17 - 5) {
                if (left) {
                    p = 0;
                    left = false;
                } else {
                    return;
                }
            }
        }
        for (Effect effect : visitor.activeEffects) {
            p += printEffectWithDuration(t, Position.of(pos.getColumn() + (left ? 3 : 22), pos.getRow() + 3 + p),
                    effect, effect.getDuration());
            if (p > 17 - 5) {
                if (left) {
                    p = 0;
                    left = false;
                } else {
                    return;
                }
            }
        }
    }

    public static int printEffect(Terminal t, Position pos, Effect e) {
        PrintEffectVisitor visitor = new PrintEffectVisitor(t, Position.of(pos.getColumn() + 1, pos.getRow()));
        e.accept(visitor);
        printText(e.getName(), t, pos);
        return visitor.len + 1;
    }

    public static int printEffectWithDuration(Terminal t, Position pos, Effect e, int duration) {
        int result = printEffect(t, pos, e);
        printText("T: " + duration, t, Position.of(pos.getColumn() + 12, pos.getRow()));
        return result;
    }

    private static class PrintItemVisitor implements ItemVisitor {
        private String type;
        private final List<Effect> passiveEffects = new ArrayList<>();
        private final List<Effect> activeEffects = new ArrayList<>();

        @Override
        public void visit(BodyArmor item) {
            passiveEffects.addAll(item.getEffects());
            type = "Equipment/Body";
        }

        @Override
        public void visit(HandArmor item) {
            passiveEffects.addAll(item.getEffects());
            type = "Equipment/Hand";
        }

        @Override
        public void visit(HeadArmor item) {
            passiveEffects.addAll(item.getEffects());
            type = "Equipment/Head";
        }

        @Override
        public void visit(LegArmor item) {
            passiveEffects.addAll(item.getEffects());
            type = "Equipment/Leg";
        }

        @Override
        public void visit(Weapon item) {
            passiveEffects.addAll(item.getEffects());
            activeEffects.addAll(item.getTargetEffects());
            type = "Equipment/Weapon";
        }
    }

    private static class PrintEffectVisitor implements EffectVisitor {
        private final Terminal terminal;
        private final Position position;
        private int len = 0;

        private PrintEffectVisitor(Terminal terminal, Position position) {
            this.terminal = terminal;
            this.position = position;
        }

        @Override
        public void visit(BodyEffect effect) {
            printText(" LEGS:  " + effect.getLegsDelta(), terminal, Position.of(position.getColumn(), position.getRow() + 1));
            printText(" HEADS: " + effect.getHeadDelta(), terminal, Position.of(position.getColumn(), position.getRow() + 2));
            printText(" HANDS: " + effect.getHandDelta(), terminal, Position.of(position.getColumn(), position.getRow() + 3));
            len = 3;
        }

        @Override
        public void visit(BurnEffect effect) {
            printText(" DPS:  " + effect.getDamage(), terminal, Position.of(position.getColumn(), position.getRow() + 1));
            len = 1;
        }

        @Override
        public void visit(DisableEffect effect) {
            len = 0;
        }

        @Override
        public void visit(DiseaseEffect effect) {
            printText(" P.DMG: " + (-effect.getPhysicalDamageDelta()), terminal, Position.of(position.getColumn(), position.getRow() + 1));
            printText(" M.DMG: " + (-effect.getMagicalDamageDelta()), terminal, Position.of(position.getColumn(), position.getRow() + 2));
            printText(" REG:   " + (-effect.getRegenerationReduce()), terminal, Position.of(position.getColumn(), position.getRow() + 3));
            printText(" M.REG: " + (-effect.getManaRegenerationReduce()), terminal, Position.of(position.getColumn(), position.getRow() + 4));
            len = 4;
        }

        @Override
        public void visit(ManaBurnEffect effect) {
            printText(" M.DPS:  " + effect.getManaDamage(), terminal, Position.of(position.getColumn(), position.getRow() + 1));
            len = 1;
        }

        @Override
        public void visit(RegenerationEffect effect) {
            printText(" REG:   " + effect.getHealthRegeneration(), terminal, Position.of(position.getColumn(), position.getRow() + 1));
            printText(" M.REG: " + effect.getManaRegeneration(), terminal, Position.of(position.getColumn(), position.getRow() + 2));
            len = 2;
        }

        @Override
        public void visit(StatsEffect effect) {
            printText(" STR: " + effect.getStrengthDelta(), terminal, Position.of(position.getColumn(), position.getRow() + 1));
            printText(" INT: " + effect.getIntelligenceDelta(), terminal, Position.of(position.getColumn(), position.getRow() + 2));
            printText(" DEX: " + effect.getDextirityDelta(), terminal, Position.of(position.getColumn(), position.getRow() + 3));
            printText(" STM: " + effect.getStaminaDelta(), terminal, Position.of(position.getColumn() + 8, position.getRow() + 1));
            printText(" WIS: " + effect.getWisdomDelta(), terminal, Position.of(position.getColumn() + 8, position.getRow() + 2));
            printText(" LCK: " + effect.getLuckDelta(), terminal, Position.of(position.getColumn() + 8, position.getRow() + 3));
            printText(" SLOWNESS: " + effect.getSlownessDelta(), terminal, Position.of(position.getColumn(), position.getRow() + 4));
            len = 4;
        }
    }
}
