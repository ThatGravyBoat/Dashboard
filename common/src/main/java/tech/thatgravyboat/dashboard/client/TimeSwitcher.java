package tech.thatgravyboat.dashboard.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import tech.thatgravyboat.dashboard.constants.Type;

public class TimeSwitcher extends SwitcherScreen<TimeSwitcher.TimeType> {

    @Override
    public TimeType getDefaultType() {
        Level level = Minecraft.getInstance().level;
        if (level == null) return TimeType.DAY;
        var time = level.getDayTime() % 24000;
        return time > 1000 && time < 6000 ? TimeType.MIDNIGHT : TimeType.DAY;
    }

    @Override
    public void addSlots() {
        slots.add(new TypeSlot(TimeType.DAY, (this.width / 2) - 60, (this.height / 2) - 31));
        slots.add(new TypeSlot(TimeType.NOON, (this.width / 2) - 30, (this.height / 2) - 31));
        slots.add(new TypeSlot(TimeType.NIGHT, (this.width / 2) + 4, (this.height / 2) - 31));
        slots.add(new TypeSlot(TimeType.MIDNIGHT, (this.width / 2) + 34, (this.height / 2) - 31));
    }

    @Override
    public int key() {
        return 294;
    }

    @Override
    public String keyName() {
        return "F5";
    }

    public enum TimeType implements Type<TimeType> {
        DAY(Component.translatable("debug.time.day"), "time set day", new ItemStack(Items.SUNFLOWER)),
        NOON(Component.translatable("debug.time.noon"), "time set noon", new ItemStack(Items.REDSTONE_LAMP)),
        NIGHT(Component.translatable("debug.time.night"), "time set night", new ItemStack(Items.RED_BED)),
        MIDNIGHT(Component.translatable("debug.time.midnight"), "time set midnight", new ItemStack(Items.FIREWORK_STAR));

        private final Component component;
        private final String command;
        private final ItemStack stack;

        TimeType(Component component, String command, ItemStack stack) {
            this.component = component;
            this.command = command;
            this.stack = stack;
        }

        @Override
        public void draw(GuiGraphics graphics, int x, int y) {
            graphics.renderFakeItem(stack, x, y);
        }

        @Override
        public Component getName() {
            return component;
        }

        @Override
        public boolean shouldRun(Level level) {
            return !level.dimensionType().hasFixedTime();
        }

        @Override
        public String getCommand() {
            return command;
        }

        @Override
        public TimeType getNext() {
            return switch (this) {
                case DAY -> NOON;
                case NOON -> NIGHT;
                case NIGHT -> MIDNIGHT;
                case MIDNIGHT -> DAY;
            };
        }
    }
}
