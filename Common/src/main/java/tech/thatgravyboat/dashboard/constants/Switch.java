package tech.thatgravyboat.dashboard.constants;

import net.minecraft.client.gui.screens.Screen;

public record Switch(String id, SwitchFactory factory) {

    public Screen screen() {
        return factory.create();
    }

    @FunctionalInterface
    public interface SwitchFactory {
        Screen create();
    }
}
