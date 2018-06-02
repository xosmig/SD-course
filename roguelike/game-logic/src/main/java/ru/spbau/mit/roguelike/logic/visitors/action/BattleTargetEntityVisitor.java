package ru.spbau.mit.roguelike.logic.visitors.action;

import ru.spbau.mit.roguelike.commons.Configuration;
import ru.spbau.mit.roguelike.commons.Point;
import ru.spbau.mit.roguelike.commons.RandomUtils;
import ru.spbau.mit.roguelike.commons.logging.Logging;
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
    private final String attackerName;
    private final Game game;

    public BattleTargetEntityVisitor(WorldEntity attacker, String attackerName, Game game) {
        this.attacker = attacker;
        this.attackerName = attackerName;
        this.game = game;
    }

    @Override
    public void visit(BarrierEntity entity) {
        if (!entity.isImmortal()) {
            attack(attacker, entity, "bounty chest");
        }
    }

    @Override
    public void visit(CharacterEntity entity) {
        attack(attacker, entity, entity.getName());
    }

    @Override
    public void visit(DropEntity entity) {
        if (attacker instanceof CharacterEntity) {
            Logging.log(attackerName
                    + " collects drop and gain "
                    + entity.getContent().size()
                    + " items.");
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
        attack(attacker, entity, entity.getName());
        if (game.getEntityById(entity.getId()) == null) {
            game.increaseWorldExp(entity.getLevel());
        }
    }

    private void attack(WorldEntity attacker, WorldEntity target, String targetName) {
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

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(attackerName).append(" deals ")
                .append(physicalAttack ? attackerState.getPhysicalDamage() : 0)
                .append(" physical and ")
                .append(magicalAttack ? magicalDamage : 0)
                .append(" magical damage to ")
                .append(targetName);


        targetState.setHealth(targetState.getHealth() -
                ((physicalAttack ? attackerState.getPhysicalDamage() : 0) + (magicalAttack ? magicalDamage : 0)));
        attackerState.setMana(attackerState.getMana() - magicalDamage);

        if (magicalAttack || physicalAttack) {
            Logging.log(messageBuilder.toString());
            weaponaryEffects().forEach(target::addEffectInstance);
        } else {
            Logging.log(targetName + " dodged " + attackerName + "'s attack");
        }
        if (targetState.getHealth() == 0) {
            Point position = game.getEntityPositionById(target.getId());
            Logging.log(attackerName
                    + " murders "
                    + targetName);
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
