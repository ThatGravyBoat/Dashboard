package tech.thatgravyboat.dashboard.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import tech.thatgravyboat.dashboard.constants.Type;

public class DifficultySwitcher extends SwitcherScreen<DifficultySwitcher.DifficultyType>{

    @Override
    public DifficultyType getDefaultType() {
        Level level = Minecraft.getInstance().level;
        if (level == null) return DifficultyType.NORMAL;
        return switch (level.getDifficulty()) {
            case EASY, PEACEFUL, HARD -> DifficultyType.NORMAL;
            case NORMAL -> DifficultyType.PEACEFUL;
        };
    }

    @Override
    public void addSlots() {
        slots.add(new TypeSlot(DifficultyType.PEACEFUL, (this.width / 2) - 60, (this.height / 2) - 31));
        slots.add(new TypeSlot(DifficultyType.EASY, (this.width / 2) - 30, (this.height / 2) - 31));
        slots.add(new TypeSlot(DifficultyType.NORMAL, (this.width / 2) + 4, (this.height / 2) - 31));
        slots.add(new TypeSlot(DifficultyType.HARD, (this.width / 2) + 34, (this.height / 2) - 31));
    }

    @Override
    public int key() {
        return 296;
    }

    @Override
    public String keyName() {
        return "F7";
    }

    public enum DifficultyType implements Type<DifficultyType> {
        PEACEFUL(new TranslatableComponent("debug.difficulty.peaceful"), "/difficulty peaceful", new ItemStack(Items.LEATHER_CHESTPLATE)),
        EASY(new TranslatableComponent("debug.difficulty.easy"), "/difficulty easy", new ItemStack(Items.CHAINMAIL_CHESTPLATE)),
        NORMAL(new TranslatableComponent("debug.difficulty.normal"), "/difficulty normal", new ItemStack(Items.DIAMOND_CHESTPLATE)),
        HARD(new TranslatableComponent("debug.difficulty.hard"), "/difficulty hard", new ItemStack(Items.NETHERITE_CHESTPLATE));

        private final Component component;
        private final String command;
        private final ItemStack stack;

        DifficultyType(Component component, String command, ItemStack stack) {
            this.component = component;
            this.command = command;
            this.stack = stack;
        }

        @Override
        public void draw(ItemRenderer renderer, int x, int y) {
            renderer.renderAndDecorateItem(stack, x, y);
        }

        @Override
        public Component getName() {
            return component;
        }

        @Override
        public boolean shouldRun(Level level) {
            return switch (level.getDifficulty()) {
                case PEACEFUL -> this != PEACEFUL;
                case EASY -> this != EASY;
                case NORMAL -> this != NORMAL;
                case HARD -> this != HARD;
            };
        }

        @Override
        public String getCommand() {
            return command;
        }

        @Override
        public DifficultyType getNext() {
            return switch (this) {
                case PEACEFUL -> EASY;
                case EASY -> NORMAL;
                case NORMAL -> HARD;
                case HARD -> PEACEFUL;
            };
        }
    }
}
