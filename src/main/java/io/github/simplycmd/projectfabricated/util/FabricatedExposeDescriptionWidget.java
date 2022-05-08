package io.github.simplycmd.projectfabricated.util;

import com.google.common.util.concurrent.Runnables;
import com.terraformersmc.modmenu.gui.ModsScreen;
import com.terraformersmc.modmenu.gui.widget.DescriptionListWidget;
import io.github.simplycmd.projectfabricated.util.gui.CreditsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.OrderedText;

public class FabricatedExposeDescriptionWidget extends DescriptionListWidget {
    public FabricatedExposeDescriptionWidget(MinecraftClient client, int width, int height, int top, int bottom, int entryHeight, ModsScreen parent) {
        super(client, width, height, top, bottom, entryHeight, parent);
        throw new RuntimeException("no");
    }

    private FabricatedExposeDescriptionWidget() {
        super(MinecraftClient.getInstance(), 0, 0, 0, 0, 0, null);
    }

    private static FabricatedExposeDescriptionWidget instance;
    public static FabricatedExposeDescriptionWidget instance() {
        if (instance == null) instance = new FabricatedExposeDescriptionWidget();
        return instance;
    }

    public ExposedDescriptionEntry exposedDescriptionEntry(OrderedText text, DescriptionListWidget widget) {
        return new ExposedDescriptionEntry(text,widget);
    }

    public FabricatedCreditsScreen exposedCreditsEntry(Screen parent, OrderedText text, DescriptionListWidget widget) {
        return new FabricatedCreditsScreen(parent, text, widget);
    }

    public class ExposedDescriptionEntry extends DescriptionEntry {
        public ExposedDescriptionEntry(OrderedText text, DescriptionListWidget widget) {
            super(text, widget);
        }
    }

    public class FabricatedCreditsScreen extends DescriptionEntry {
        private final Screen parent;
        public FabricatedCreditsScreen(Screen parent, OrderedText text, DescriptionListWidget widget) {
            super(text, widget);
            this.parent = parent;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isMouseOver(mouseX, mouseY)) {
                client.setScreen(new CreditsScreen(parent, Runnables.doNothing()));
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
