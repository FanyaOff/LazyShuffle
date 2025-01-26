package com.fanya.lazyshuffle.mixin;

import com.fanya.lazyshuffle.client.gui.widgets.UpdateSkinsButton;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At("RETURN"))
    private void addCustomButton(CallbackInfo info) {
        int y = (this.height / 4 + 48) + 78;
        int x = this.width / 2 + 104 + 13;

        this.addDrawableChild(new UpdateSkinsButton(x, y, 72, 20));
    }
}
