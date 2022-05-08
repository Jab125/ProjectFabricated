package io.github.simplycmd.projectfabricated.mixin;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Pseudo
@Mixin(CreditsScreen.class)
public interface CreditsScreenAccessor {
    @Accessor
    static Text getSEPARATOR_LINE() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static String getCENTERED_LINE_PREFIX() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Identifier getMINECRAFT_TITLE_TEXTURE() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Identifier getEDITION_TITLE_TEXTURE() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Identifier getVIGNETTE_TEXTURE() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    List<OrderedText> getCredits();

    @Accessor
    IntSet getCenteredLines();

    @Accessor
    float getSpeed();

    @Accessor
    void setCredits(List<OrderedText> credits);

    @Accessor
    void setCenteredLines(IntSet centeredLines);

    @Accessor
    void setCreditsHeight(int creditsHeight);

    @Invoker
    void callAddText(Text text, boolean centered);

    @Invoker
    void callAddEmptyLine();

    @Invoker
    void callLoad(String id, CreditsScreen.CreditsReader reader);

    @Invoker
    void callRenderBackground();

    @Accessor
    float getTime();

    @Accessor
    void setTime(float time);

    @Mutable
    @Accessor
    void setBaseSpeed(float baseSpeed);

    @Accessor
    void setSpeed(float speed);
}
