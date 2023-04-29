package tech.thatgravyboat.dashboard.constants;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public interface Type<T> {

    void draw(PoseStack pose, ItemRenderer renderer, int x, int y);

    Component getName();

    boolean shouldRun(Level level);

    String getCommand();

    T getNext();
}
