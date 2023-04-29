package tech.thatgravyboat.dashboard.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import tech.thatgravyboat.dashboard.constants.Type;

public class WeatherSwitcher extends SwitcherScreen<WeatherSwitcher.WeatherType> {

    @Override
    public WeatherType getDefaultType() {
        Level level = Minecraft.getInstance().level;
        if (level == null) return WeatherType.CLEAR;
        return level.isRaining() || level.isThundering() ? WeatherType.CLEAR : WeatherType.THUNDER;
    }

    @Override
    public void addSlots() {
        slots.add(new TypeSlot(WeatherType.CLEAR, (this.width / 2) - 55, (this.height / 2) - 31));
        slots.add(new TypeSlot(WeatherType.RAIN, (this.width / 2) - 13, (this.height / 2) - 31));
        slots.add(new TypeSlot(WeatherType.THUNDER, (this.width / 2) + 34, (this.height / 2) - 31));
    }

    @Override
    public int key() {
        return 295;
    }

    @Override
    public String keyName() {
        return "F6";
    }

    public enum WeatherType implements Type<WeatherType> {
        CLEAR(Component.translatable("debug.weather.clear"), "weather clear", new ItemStack(Items.SUNFLOWER)),
        RAIN(Component.translatable("debug.weather.rain"), "weather rain", new ItemStack(Items.WATER_BUCKET)),
        THUNDER(Component.translatable("debug.weather.thunder"), "weather thunder", new ItemStack(Items.TRIDENT));

        private final Component component;
        private final String command;
        private final ItemStack stack;

        WeatherType(Component component, String command, ItemStack stack) {
            this.component = component;
            this.command = command;
            this.stack = stack;
        }

        @Override
        public void draw(PoseStack pose, ItemRenderer renderer, int x, int y) {
            renderer.renderAndDecorateItem(pose, this.stack, x, y);
        }

        public Component getName() {
            return this.component;
        }

        public String getCommand() {
            return this.command;
        }

        public boolean shouldRun(Level level) {
            if (this == CLEAR) return level.isRaining() || level.isThundering();
            if (this == RAIN) return !level.isRaining() || (level.isRaining() && !level.isThundering());
            return this == THUNDER && !level.isThundering();
        }

        public WeatherType getNext() {
            return switch (this) {
                case CLEAR -> RAIN;
                case RAIN -> THUNDER;
                case THUNDER -> CLEAR;
            };
        }
    }
}
