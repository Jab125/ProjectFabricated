package io.github.simplycmd.projectfabricated.config;

import io.github.simplycmd.projectfabricated.ProjectFabricated;
import io.github.simplycmd.projectfabricated.core.FModule;
import io.github.simplycmd.projectfabricated.util.ModuleUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class FabricatedConfig implements FModule {
    private ConfigManager manager;
    private Config config;
    @Override
    public void onInitialize() {
        FModule.super.onInitialize();
    }

    public static ConfigManager getManager() {
        return ModuleUtils.getModule("config", FabricatedConfig.class).getManager0();
    }

    private ConfigManager getManager0() {
        if (this.manager == null) this.manager = new ConfigManager(this);
        return this.manager;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        FModule.super.onInitializeClient();
    }

    @Override
    public String moduleName() {
        return "config";
    }

    public Config getConfig() {
        return config;
    }
}
