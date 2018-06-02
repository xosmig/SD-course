package ru.spbau.mit.roguelike.model.factories;

import org.junit.Test;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.model.units.game.EmptyCell;
import ru.spbau.mit.roguelike.model.units.game.Field;

import static org.junit.Assert.*;

public class FieldFactoryTest {
    @Test
    public void generateField() throws Exception {
        int height = 900;
        int width = 900;
        Field field = FieldFactory.generateField(height, width);
        //just print field
        for (int j = 0; j < height + 2; j++) {
            for (int i = 0; i < width + 2; i++) {
                System.out.print(field.getCellAt(Point.of(i, j)) instanceof EmptyCell ? " " : "#");
            }
            System.out.println();
        }
    }
}