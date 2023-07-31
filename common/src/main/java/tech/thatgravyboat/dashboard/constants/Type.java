package tech.thatgravyboat.dashboard.constants;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public interface Type<T> {

    void draw(GuiGraphics graphics, int x, int y);

    Component getName();

    boolean shouldRun(Level level);

    String getCommand();

    T getNext();
}
