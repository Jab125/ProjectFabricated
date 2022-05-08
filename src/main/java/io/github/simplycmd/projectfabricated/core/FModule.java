package io.github.simplycmd.projectfabricated.core;

import io.github.simplycmd.projectfabricated.util.ModuleUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface FModule {
    default void onInitialize() {

    }
    @Environment(EnvType.CLIENT)
    default void onInitializeClient() {

    }

    String moduleName();

    default Logger getLogger() {
        return LoggerFactory.getLogger("fabricated-" + moduleName());
    }
}
