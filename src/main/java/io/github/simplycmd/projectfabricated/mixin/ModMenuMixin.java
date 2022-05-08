package io.github.simplycmd.projectfabricated.mixin;

import com.terraformersmc.modmenu.config.ModMenuConfig;
import com.terraformersmc.modmenu.config.option.BooleanConfigOption;
import com.terraformersmc.modmenu.gui.widget.DescriptionListWidget;
import com.terraformersmc.modmenu.util.mod.Mod;
import io.github.simplycmd.projectfabricated.util.FabricatedExposeDescriptionWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(DescriptionListWidget.class)
public abstract class ModMenuMixin extends EntryListWidget {
    public ModMenuMixin(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }
    private Mod storedMod;

    @ModifyVariable(method = "render", at = @At(value = "STORE"))
    private Mod getMod(Mod value) {
        storedMod = value;
        return value;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/terraformersmc/modmenu/config/option/BooleanConfigOption;getValue()Z", ordinal = 2))
    private void fabricated$addCredits(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!ModMenuConfig.HIDE_MOD_CREDITS.getValue() && "project-fabricated".equals(storedMod.getId())) {
            children().add(FabricatedExposeDescriptionWidget.instance().exposedDescriptionEntry(LiteralText.EMPTY.asOrderedText(), (DescriptionListWidget)(Object)this));
            children().add(FabricatedExposeDescriptionWidget.instance().exposedCreditsEntry(((DescriptionListWidgetAccessor) this).getParent(), new TranslatableText("modmenu.viewCredits").formatted(Formatting.BLUE).formatted(Formatting.UNDERLINE).asOrderedText(),  (DescriptionListWidget)(Object)this));
        }
        //return null;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcom/terraformersmc/modmenu/config/option/BooleanConfigOption;getValue()Z", ordinal = 2))
    private boolean fabricated$stopCreditLoading(BooleanConfigOption instance) {
        if ("project-fabricated".equals(storedMod.getId())) {
            return true;
        }
        return instance.getValue();
    }
}
