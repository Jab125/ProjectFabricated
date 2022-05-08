package io.github.simplycmd.projectfabricated.mixin;

import com.terraformersmc.modmenu.gui.ModsScreen;
import com.terraformersmc.modmenu.gui.widget.DescriptionListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(DescriptionListWidget.class)
public interface DescriptionListWidgetAccessor {
    @Accessor
    ModsScreen getParent();
}
