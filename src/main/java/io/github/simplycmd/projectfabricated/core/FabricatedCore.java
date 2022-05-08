package io.github.simplycmd.projectfabricated.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricatedCore implements FModule {
    @Override
    public void onInitialize() {
        FModule.super.onInitialize();
        getLogger().info("Core Module Initialized");
        //System.out.println("Core Module Initialized");
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        FModule.super.onInitializeClient();
        getLogger().info("[Client] Core Module Initalized");
    }

    @Override
    public String moduleName() {
        return "core";
    }
}
