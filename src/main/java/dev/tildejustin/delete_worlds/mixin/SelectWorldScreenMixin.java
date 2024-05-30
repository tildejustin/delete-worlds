package dev.tildejustin.delete_worlds.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.*;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.*;


@Mixin(SelectWorldScreen.class)
public abstract class SelectWorldScreenMixin extends Screen {
    @Unique
    private static final Method method_20170;

    static {
        try {
            // lambda in WorldListWidget$Entry#delete
            method_20170 = WorldListWidget.Entry.class.getDeclaredMethod("method_20170", boolean.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        method_20170.setAccessible(true);
    }

    @Shadow
    private WorldListWidget levelList;

    protected SelectWorldScreenMixin(Text title) {
        super(title);
    }

    @ModifyExpressionValue(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;keyPressed(III)Z", ordinal = 0))
    public boolean keyPressed(boolean original, int keyCode, int scanCode, int modifiers) {
        if (original) return true;
        if (keyCode == GLFW.GLFW_KEY_DELETE) {
            this.levelList.getSelectedAsOptional().ifPresent(this::tryDeleteWorld);
            if (!this.levelList.children().isEmpty()) {
                this.levelList.setSelected(this.levelList.children().get(0));
                this.setFocused(this.levelList);
            }
            return true;
        }
        return false;
    }

    @Unique
    private void tryDeleteWorld(WorldListWidget.Entry entry) {
        try {
            method_20170.invoke(entry, true);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
