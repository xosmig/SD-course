package ru.spbau.mit.roguelike.uicommon.views;

import ru.spbau.mit.roguelike.model.factories.EntityFactory;
import ru.spbau.mit.roguelike.model.units.entity.CharacterEntity;
import ru.spbau.mit.roguelike.uicommon.views.elements.Button;
import ru.spbau.mit.roguelike.uicommon.views.elements.TextButton;

/**
 * View for game main menu
 */
public abstract class MainMenuView extends ListMenu<TextButton> {
    public MainMenuView() {
        super(null);
        //TODO config
        super.addButton(new TextButton("Play") {
            @Override
            public void onClick() {
                startGame(EntityFactory.getCharacter("Hero"));
            }
        });
        super.addButton(new TextButton("Exit") {
            @Override
            public void onClick() {
                exit();
            }
        });
    }

    public abstract void startGame(CharacterEntity player);
    public abstract void exit();

    @Override
    public ListMenu<TextButton> addButton(TextButton button) {
        //DoNothing
        return this;
    }
}
