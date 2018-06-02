package ru.spbau.mit.roguelike.model.factories;

import org.junit.Test;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.model.units.game.EmptyCell;
import ru.spbau.mit.roguelike.model.units.game.Field;

import static org.junit.Assert.*;

/**
 * Created by ArgentumWalker on 30.05.2018.
 */
public class FieldFactoryTest {
    @Test
    public void generateField() throws Exception {
        int height = 60;
        int width = 130;
        Field field = FieldFactory.generateField(height, width,
                0.8, 0.6, 0.4, 0.3);
        for (int j = 0; j < height + 2; j++) {
            for (int i = 0; i < width + 2; i++) {
                System.out.print(field.getCellAt(Point.of(i, j)) instanceof EmptyCell ? " " : "#");
            }
            System.out.println();
        }
    }
}