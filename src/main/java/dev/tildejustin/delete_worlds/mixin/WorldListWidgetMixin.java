package dev.tildejustin.delete_worlds.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldListWidget.class)
public abstract class WorldListWidgetMixin extends AlwaysSelectedEntryListWidget<WorldListWidget.Entry> {
    public WorldListWidgetMixin(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight) {
        super(minecraftClient, width, height, y, itemHeight);
    }

    @Inject(method = "showSummaries", at = @At("TAIL"))
    private void showSummaries(CallbackInfo ci) {
        if (!this.children().isEmpty()) {
            this.setFocused(this.children().get(0));
        }
    }
}
