package ru.spbau.mit.roguelike.logic.visitors.action;

import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.model.units.entity.CreepEntity;
import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;
import ru.spbau.mit.roguelike.model.units.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Implementation of creep's action policy
 */
public final class CreepAI {
    public static EntityAction getAction(Game game, CreepEntity entity) {
        //TODO
        List<EntityAction> variants = Arrays.asList(EntityAction.LEFT, EntityAction.RIGHT,
                EntityAction.UP, EntityAction.DOWN);
        return RandomUtils.chooseRandom(variants);
    }
}
