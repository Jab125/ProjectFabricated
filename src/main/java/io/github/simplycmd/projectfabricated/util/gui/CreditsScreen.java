package io.github.simplycmd.projectfabricated.util.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.simplycmd.projectfabricated.mixin.CreditsScreenAccessor;
import io.github.simplycmd.projectfabricated.mixin.ScreenAccessor;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.InputStreamReader;

public class CreditsScreen extends net.minecraft.client.gui.screen.CreditsScreen {
    private static final Identifier PROJECT_FABRICATED_TITLE_TEXTURE = new Identifier("projectfabricated:textures/gui/title/projectfabricated.png");
    private final Screen parent;
    private final Runnable finishAction;
    public CreditsScreen(Screen parent, Runnable finishAction) {
        super(false, finishAction);
        this.parent = parent;
        this.finishAction = finishAction;
        ((CreditsScreenAccessor)this).setBaseSpeed(0.75F);
        ((CreditsScreenAccessor)this)    .setSpeed(0.75F);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        var accessor = (CreditsScreenAccessor) this;
        int l;
        accessor.setTime(accessor.getTime() + delta * accessor.getSpeed());
        accessor.callRenderBackground();
        int i = this.width / 2 - 137;
        int j = this.height + 50;
        float f = -accessor.getTime();
        matrices.push();
        matrices.translate(0.0, f, 0.0);
        RenderSystem.setShaderTexture(0, PROJECT_FABRICATED_TITLE_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        this.drawWithOutline(i, j, (x, y) -> {
            this.drawTexture(matrices, x, (int)y, 0, 0, 310, 44, 310, 128);
            //this.drawTexture(matrices, x + 155, (int)y, 0, 45, 512, 44);
        });
        RenderSystem.disableBlend();
        //RenderSystem.setShaderTexture(0, CreditsScreenAccessor.getEDITION_TITLE_TEXTURE());
        matrices.push();
        matrices.scale(0.5F, 0.5F, 0.5F);
        net.minecraft.client.gui.screen.CreditsScreen.drawTexture(matrices, (i + 88)*2, (j - 3)*2, 0.0f, 93.0f, 177, 35, 310, 128);
        matrices.pop();
        int k = j + 100;
        for (l = 0; l < accessor.getCredits().size(); ++l) {
            float g;
            if (l == accessor.getCredits().size() - 1 && (g = (float)k + f - (float)(this.height / 2 - 6)) < 0.0f && false) {
                matrices.translate(0.0, -g, 0.0);
            }
            if ((float)k + f + 12.0f + 8.0f > 0.0f && (float)k + f < (float)this.height) {
                OrderedText orderedText = accessor.getCredits().get(l);
                if (accessor.getCenteredLines().contains(l)) {
                    this.textRenderer.drawWithShadow(matrices, orderedText, (float)(i + (274 - this.textRenderer.getWidth(orderedText)) / 2), (float)k, 0xFFFFFF);
                } else {
                    this.textRenderer.drawWithShadow(matrices, orderedText, (float)i, (float)k, 0xFFFFFF);
                }
            }
            k += 12;
        }
        matrices.pop();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, CreditsScreenAccessor.getVIGNETTE_TEXTURE());
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR);
        l = this.width;
        int m = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(0.0, m, this.getZOffset()).texture(0.0f, 1.0f).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferBuilder.vertex(l, m, this.getZOffset()).texture(1.0f, 1.0f).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferBuilder.vertex(l, 0.0, this.getZOffset()).texture(1.0f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        bufferBuilder.vertex(0.0, 0.0, this.getZOffset()).texture(0.0f, 0.0f).color(1.0f, 1.0f, 1.0f, 1.0f).next();
        tessellator.draw();
        RenderSystem.disableBlend();


        for (Drawable drawable : ((ScreenAccessor) this).getDrawables()) {
            drawable.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    protected void closeScreen() {
        this.finishAction.run();
        this.client.setScreen(parent);
    }

    @Override
    protected void init() {
        if (((CreditsScreenAccessor) this).getCredits() != null) {
            return;
        }
        ((CreditsScreenAccessor)this).setCredits(Lists.newArrayList());
        ((CreditsScreenAccessor) this).setCenteredLines(new IntOpenHashSet());
        this.adText("These credits were created with $0 budget.", true);
        this.adText("And that very high quality logo up", true);
        this.adText("there was made with dedication and care.", true);
        ((CreditsScreenAccessor) this).callLoad("projectfabricated:texts/credits.json", this::readFabricatedCredits);
        ((CreditsScreenAccessor) this).callAddEmptyLine();
        ((CreditsScreenAccessor) this).callAddEmptyLine();
        ((CreditsScreenAccessor) this).callAddEmptyLine();
        ((CreditsScreenAccessor) this).callAddEmptyLine();
        ((CreditsScreenAccessor) this).callAddEmptyLine();
        this.adText("I do wish that the text don't take long", true);

        ((CreditsScreenAccessor)this).setCreditsHeight(((CreditsScreenAccessor)this).getCredits().size() * 12);
    }

    private void readFabricatedCredits(InputStreamReader inputStreamReader) {
        JsonArray jsonArray = JsonHelper.deserializeArray(inputStreamReader);
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String string = jsonObject.get("section").getAsString();
            this.adText(CreditsScreenAccessor.getSEPARATOR_LINE(), true);
            this.adText(new LiteralText(string).formatted(Formatting.YELLOW), true);
            this.adText(CreditsScreenAccessor.getSEPARATOR_LINE(), true);
            ((CreditsScreenAccessor) this).callAddEmptyLine();
            ((CreditsScreenAccessor) this).callAddEmptyLine();
            JsonArray jsonArray2 = jsonObject.getAsJsonArray("titles");
            for (JsonElement jsonElement2 : jsonArray2) {
                JsonObject jsonObject2 = jsonElement2.getAsJsonObject();
                String string2 = jsonObject2.get("title").getAsString();
                JsonArray jsonArray3 = jsonObject2.getAsJsonArray("names");
                this.adText(new LiteralText(string2).formatted(Formatting.GRAY), false);
                for (JsonElement jsonElement3 : jsonArray3) {
                    String string3 = jsonElement3.getAsString();
                    this.adText(new LiteralText(CreditsScreenAccessor.getCENTERED_LINE_PREFIX()).append(string3).formatted(Formatting.WHITE), false);
                }
                ((CreditsScreenAccessor) this).callAddEmptyLine();
                ((CreditsScreenAccessor) this).callAddEmptyLine();
            }
        }
    }

    private void adText(Text text, boolean centered) {
        ((CreditsScreenAccessor)this).callAddText(text, centered);
    }
    private void adText(String text, boolean centered) {
        ((CreditsScreenAccessor)this).callAddText(new LiteralText(text), centered);
    }
}
