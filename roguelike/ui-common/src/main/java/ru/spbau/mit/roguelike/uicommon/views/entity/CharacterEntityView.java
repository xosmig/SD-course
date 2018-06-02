package ru.spbau.mit.roguelike.uicommon.views.entity;

import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.model.units.entity.WorldEntity;
import ru.spbau.mit.roguelike.uicommon.views.View;

/**
 * Character entity view
 */
public abstract class CharacterEntityView extends EntityView<CharacterEntity> {
    public CharacterEntityView(View parentView, CharacterEntity entity) {
        super(parentView, entity);
    }

    @Override
    protected void draw() {
        drawName(entity.getName());
        super.draw();
    }

    protected abstract void drawName(String name);
}
