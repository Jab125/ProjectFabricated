package io.github.simplycmd.projectfabricated.config.client.screens;

import io.github.simplycmd.projectfabricated.ProjectFabricated;
import io.github.simplycmd.projectfabricated.config.Config;
import io.github.simplycmd.projectfabricated.core.FModule;
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
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

import static io.github.simplycmd.projectfabricated.ProjectFabricated.isDisabled;

public class ModulesSelectionScreen extends GameOptionsScreen {
    private ButtonListWidget list;
    private final Screen previous;
    public ModulesSelectionScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, new TranslatableText("project.fabricated.config.modules_selection"));
        this.previous = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.list = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        //this.list.addAll();
        ProjectFabricated.executeForAllTotalModules(fmodule -> {
            var name = fmodule.moduleName();
            this.list.addSingleOptionEntry(createOption(fmodule));
        });

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
        drawCenteredText(matrices, this.textRenderer, new TranslatableText("project.fabricated.config.modules_selection.restart_minecraft"), this.width / 2, 5+textRenderer.fontHeight+1, 0xffffff);
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
    public Option createOption(GetText text, InternalConsumer consumer) {
        return new Option("") {
            @Override
            public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
                return new ButtonWidget(x, y, width, 20, text.get(), (buttonWidget) -> {
                    consumer.execute(buttonWidget, ModulesSelectionScreen.this);
                });
            }
        };
    }

    @Environment(EnvType.CLIENT)
    public Option createOption(FModule module) {
        return new Option("") {
            @Override
            public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
                System.out.println("CALLED");
                var d = new CustomButtonWidget(x, y, width, 20, new LiteralText(""), (buttonWidget) -> {
                    if (!isDisabled(module).enabled) Config.get().removeDisabled(module);
                    else Config.get().setDisabled(module);
                }, module);
                d.active = !ProjectFabricated.isForceLoaded(module.moduleName());
                return d;
            }
        };
    }

    private class CustomButtonWidget extends ButtonWidget {
        private final FModule module;

        public CustomButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, FModule module) {
            super(x, y, width, height, message, onPress);
            this.module = module;
        }

        public CustomButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, TooltipSupplier tooltipSupplier, FModule module) {
            super(x, y, width, height, message, onPress, tooltipSupplier);
            this.module = module;
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            var text = new TranslatableText("project.fabricated.config." + module.moduleName()).append(!isDisabled(module).enabled ? ": Disabled" : ": Enabled").append(isDisabled(module).needsRestart ? "*" : "");
            if (isDisabled(module).needsRestart) text.styled(style->style.withItalic(true));
            super.renderButton(matrices, mouseX, mouseY, delta);
            int j = this.active ? 0xFFFFFF : 0xA0A0A0;
            ClickableWidget.drawCenteredText(matrices, ModulesSelectionScreen.this.textRenderer, text, this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);
        }
    }

    public interface InternalConsumer {
        void execute(ButtonWidget widget, Screen screen);
    }
    public interface GetText {
        Text get();
    }
}