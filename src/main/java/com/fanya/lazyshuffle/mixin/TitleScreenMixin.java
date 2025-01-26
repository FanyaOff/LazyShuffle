package com.fanya.lazyshuffle.mixin;

import com.fanya.lazyshuffle.client.gui.widgets.UpdateSkinsButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addCustomButton(CallbackInfo info) {
        int y = (this.height / 4 + 48) + 106;
        int x = this.width / 2 + 104 + 25;
        this.addDrawableChild(new UpdateSkinsButton(x, y, 72, 20));
    }
}
