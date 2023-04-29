package tech.thatgravyboat.dashboard.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.dashboard.constants.Type;

import java.util.ArrayList;
import java.util.List;

public abstract class SwitcherScreen<T extends Type<T>> extends Screen {

    public static final ResourceLocation LOCATION = new ResourceLocation("textures/gui/container/gamemode_switcher.png");

    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    protected final List<TypeSlot> slots = new ArrayList<>();
    private T currentlyHovered;

    public SwitcherScreen() {
        super(CommonComponents.EMPTY);
        this.currentlyHovered = getDefaultType();
    }

    public abstract T getDefaultType();

    public abstract void addSlots();

    public abstract int key();

    public abstract String keyName();

    @Override
    protected void init() {
        super.init();
        addSlots();
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (!this.checkToClose()) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            stack.pushPose();
            RenderSystem.enableBlend();
            RenderSystem.setShaderTexture(0, LOCATION);
            int i = this.width / 2 - 62;
            int j = this.height / 2 - 31 - 27;
            blit(stack, i, j, 0f, 0f, 125, 75, 128, 128);
            stack.popPose();
            super.render(stack, mouseX, mouseY, partialTicks);
            drawCenteredString(stack, this.font, this.currentlyHovered.getName(), this.width / 2, this.height / 2 - 31- 20, -1);
            var selectKey = Component.translatable("debug.gamemodes.select_next", (Component.literal("[ " + keyName() + " ]")).withStyle(ChatFormatting.AQUA));
            drawCenteredString(stack, this.font, selectKey, this.width / 2, this.height / 2 + 5, 16777215);
            if (!this.setFirstMousePos) {
                this.firstMouseX = mouseX;
                this.firstMouseY = mouseY;
                this.setFirstMousePos = true;
            }

            boolean flag = this.firstMouseX == mouseX && this.firstMouseY == mouseY;

            for (TypeSlot slot : this.slots) {
                slot.render(stack, mouseX, mouseY, partialTicks);
                slot.setSelected(this.currentlyHovered == slot.type);
                if (!flag && slot.isHoveredOrFocused()) {
                    this.currentlyHovered = slot.type;
                }
            }
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    private boolean checkToClose() {
        if (this.minecraft == null || this.minecraft.player == null) return false;
        if (!InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), 292)) {
            if (this.minecraft.player.hasPermissions(2) && this.currentlyHovered.shouldRun(this.minecraft.level) && this.minecraft.getConnection() != null) {
                this.minecraft.getConnection().sendUnsignedCommand(this.currentlyHovered.getCommand());
            }
            this.minecraft.setScreen(null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyPressed(int code, int scanCode, int modifiers) {
        if (code == key()) {
            this.setFirstMousePos = false;
            this.currentlyHovered = this.currentlyHovered.getNext();
            return true;
        }
        return super.keyPressed(code, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public class TypeSlot extends AbstractWidget {

        private final T type;
        private boolean isSelected;

        public TypeSlot(T type, int x, int y) {
            super(x, y, 26, 26, type.getName());
            this.type = type;
        }

        @Override
        public void renderWidget(PoseStack pose, int i, int j, float f) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, LOCATION);
            pose.pushPose();
            pose.translate(getX(), getY(), 0.0D);
            blit(pose, 0, 0, 0.0F, 75.0F, 26, 26, 128, 128);
            if (this.isSelected) blit(pose, 0, 0, 26.0F, 75.0F, 26, 26, 128, 128);
            pose.popPose();
            this.type.draw(pose, SwitcherScreen.this.itemRenderer, getX() + 5, getY() + 5);
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
        }

        @Override
        public boolean isHoveredOrFocused() {
            return super.isHoveredOrFocused() || this.isSelected;
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
