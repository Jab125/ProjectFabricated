package io.github.simplycmd.projectfabricated.config.client.screens;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import io.github.simplycmd.projectfabricated.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends GameOptionsScreen {
    private ButtonListWidget list;
    private final Screen previous;
    public ConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, new TranslatableText("project.fabricated.title"));
        this.previous = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.list = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        //this.list.addAll();
        this.list.addSingleOptionEntry(createOption(new TranslatableText("project.fabricated.config.modules_selection"), ModulesSelectionScreen::new));
        this.addSelectableChild(this.list);
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 98, 20, ScreenTexts.DONE, (button) -> {
            Config.get().save();
            this.client.setScreen(this.previous);
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, this.height - 27, 98, 20, new TranslatableText("project.fabricated.reload"), (button) -> {
            Config.get().load();
            this.client.setScreen(this.previous);
        }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> list = getHoveredButtonTooltip(this.list, mouseX, mouseY);
        if (list != null) {
            this.renderOrderedTooltip(matrices, list, mouseX, mouseY);
        }

    }

    public void removed() {
        Config.get().save();
    }

    @Environment(EnvType.CLIENT)
    public Option createOption(Text text, ConfigScreenFactory<?> screen) {
        return new Option("") {
            @Override
            public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
                return new ButtonWidget(x, y, width, 20, text, (buttonWidget) -> {
                    MinecraftClient.getInstance().setScreen(screen.create(ConfigScreen.this));
                });
            }
        };
    }
}
