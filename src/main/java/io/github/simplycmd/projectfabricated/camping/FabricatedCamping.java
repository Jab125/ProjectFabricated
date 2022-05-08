package io.github.simplycmd.projectfabricated.camping;

import io.github.simplycmd.projectfabricated.core.FModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class FabricatedCamping implements FModule {
    @Override
    public void onInitialize() {
        FModule.super.onInitialize();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        FModule.super.onInitializeClient();
    }

    @Override
    public String moduleName() {
        return "camping";
    }
}
