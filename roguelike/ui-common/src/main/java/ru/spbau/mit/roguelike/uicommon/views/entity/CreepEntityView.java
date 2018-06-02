package ru.spbau.mit.roguelike.uicommon.views.entity;

import ru.spbau.mit.roguelike.model.units.entity.CreepEntity;
import ru.spbau.mit.roguelike.uicommon.views.View;

/**
 * Creep entity view
 */
public abstract class CreepEntityView extends EntityView<CreepEntity> {
    public CreepEntityView(View parentView, CreepEntity entity) {
        super(parentView, entity);
    }

    @Override
    protected void draw() {
        drawNameAngLevel(entity.getName(), entity.getLevel());
        super.draw();
    }

    protected abstract void drawNameAngLevel(String name, int level);
}