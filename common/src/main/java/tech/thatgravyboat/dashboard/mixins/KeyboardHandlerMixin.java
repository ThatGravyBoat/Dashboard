package tech.thatgravyboat.dashboard.mixins;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.thatgravyboat.dashboard.constants.Switchers;
import tech.thatgravyboat.dashboard.constants.Switch;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {

    @Shadow protected abstract void debugFeedbackTranslated(String id, Object... args);

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "handleDebugKeys", at = @At("HEAD"), cancellable = true)
    public void handleDebugKey(int key, CallbackInfoReturnable<Boolean> cir) {
        Switch switcher = Switchers.get(key);
        if (switcher != null && this.minecraft.player != null) {
            if (!this.minecraft.player.hasPermissions(2)) {
                this.debugFeedbackTranslated("debug." + switcher.id() + ".error");
            } else {
                this.minecraft.setScreen(switcher.screen());
            }
            cir.setReturnValue(true);
        }
    }
}
