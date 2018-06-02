package ru.spbau.mit.roguelike.logic.visitors.action;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.model.factories.EntityFactory;
import ru.spbau.mit.roguelike.model.units.effect.Effect;
import ru.spbau.mit.roguelike.model.units.effect.EffectInstance;
import ru.spbau.mit.roguelike.model.units.entity.*;
import ru.spbau.mit.roguelike.model.units.entity.inventory.Inventory;
import ru.spbau.mit.roguelike.model.units.game.Game;
import ru.spbau.mit.roguelike.model.units.item.Item;
import ru.spbau.mit.roguelike.model.units.item.equipment.Equipment;
import ru.spbau.mit.roguelike.model.units.item.equipment.Weapon;
import ru.spbau.mit.roguelike.model.visitors.EntityVisitor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Battle visitor that performs attack action
 */
public class BattleTargetEntityVisitor implements EntityVisitor {
    private final WorldEntity attacker;
    private final Game game;

    public BattleTargetEntityVisitor(WorldEntity attacker, Game game) {
        this.attacker = attacker;
        this.game = game;
    }

    @Override
    public void visit(BarrierEntity entity) {
        if (!entity.isImmortal()) {
            attack(attacker, entity);
        }
    }

    @Override
    public void visit(CharacterEntity entity) {
        attack(attacker, entity);
    }

    @Override
    public void visit(DropEntity entity) {
        if (attacker instanceof CharacterEntity) {
            game.removeEntity(entity.getId());
            Inventory inventory = ((CharacterEntity) attacker).getInventory();
            for (Item item : entity.getContent()){
                inventory.addItem(item);
            }
        }
    }

    @Override
    public void visit(CreepEntity entity) {
        if (attacker instanceof CreepEntity) {
            return;
        }
        attack(attacker, entity);
        if (game.getEntityById(entity.getId()) == null) {
            game.increaseWorldExp(entity.getLevel());
        }
    }

    private void attack(WorldEntity attacker, WorldEntity target) {
        StateDescriptor attackerState = attacker.getStateDescriptor();
        StateDescriptor targetState = target.getStateDescriptor();
        StatDescriptor attackerStat = attacker.getCurrentStatDescriptor();
        StatDescriptor targetStat = target.getCurrentStatDescriptor();
        if (attackerState.isFrozen()) {
            return;
        }
        int magicalDamage = attackerState.isSilenced() ? 0 : Math.min(attackerState.getMagicalDamage(), attackerState.getMana());
        boolean physicalAttack = (attackerState.getPhysicalDamage() != 0) && RandomUtils.getBoolean(
                Configuration.getDouble("GAME_BATTLE_PHYSICAL_PROBABILITY_CONSTANT") *
                        attackerStat.getDexterity() / (attackerStat.getDexterity() + targetStat.getDexterity()) *
                        attackerStat.getLuck() / (attackerStat.getLuck() + targetStat.getLuck())
        );
        boolean magicalAttack = (magicalDamage != 0) && RandomUtils.getBoolean(
                Configuration.getDouble("GAME_BATTLE_MAGICAL_PROBABILITY_CONSTANT") *
                        attackerStat.getLuck() / (attackerStat.getLuck() + targetStat.getLuck())
        );
        targetState.setHealth(targetState.getHealth() -
                ((physicalAttack ? attackerState.getPhysicalDamage() : 0) + (magicalAttack ? magicalDamage : 0)));
        attackerState.setMana(attackerState.getMana() - magicalDamage);
        if (magicalAttack || physicalAttack) {
            weaponaryEffects().forEach(target::addEffectInstance);
        }
        if (targetState.getHealth() == 0) {
            Point position = game.getEntityPositionById(target.getId());
            game.removeEntity(target.getId());
            game.moveOrSpawnEntity(position, EntityFactory.getDrop(target.getLevel()));
        }
    }

    private Set<EffectInstance> weaponaryEffects() {
        //TODO Visitor that collects target effects
        Set<Effect> effects = new HashSet<>();
        for (Equipment e : attacker.getEquipment()) {
            if (e instanceof Weapon) {
                effects.addAll(((Weapon) e).getTargetEffects());
            }
        }
        return effects.stream().map(Effect::instantiate).collect(Collectors.toSet());
    }
}
