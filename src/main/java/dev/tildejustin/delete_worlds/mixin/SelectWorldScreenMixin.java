package dev.tildejustin.delete_worlds.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.*;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SelectWorldScreen.class)
public abstract class SelectWorldScreenMixin extends Screen {
    @Shadow
    private WorldListWidget levelList;

    protected SelectWorldScreenMixin(Text title) {
        super(title);
    }

    @ModifyExpressionValue(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;keyPressed(III)Z", ordinal = 0))
    public boolean keyPressed(boolean original, int keyCode, int scanCode, int modifiers) {
        if (original) return true;
        if (keyCode == GLFW.GLFW_KEY_DELETE) {
            this.levelList.getSelectedAsOptional().ifPresent(WorldListWidget.WorldEntry::delete);
            return true;
        }
        return false;
    }
}
