package io.github.simplycmd.projectfabricated;

import io.github.simplycmd.projectfabricated.camping.FabricatedCamping;
import io.github.simplycmd.projectfabricated.config.FabricatedConfig;
import io.github.simplycmd.projectfabricated.config.Config;
import io.github.simplycmd.projectfabricated.core.FModule;
import io.github.simplycmd.projectfabricated.core.FabricatedCore;
import net.fabricmc.api.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.vehicle.BoatEntity;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ClientModInitializer.class)
public class ProjectFabricated implements ClientModInitializer, ModInitializer {
    private static final ArrayList<FModule> moduleList = new ArrayList<>();
    // Contains disabled modules
    private static final ArrayList<FModule> totalModuleList = new ArrayList<>();
    private static final ArrayList<String> disabledModules = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger("fabricated");
    @Override
    public void onInitialize() {
        createModule(new FabricatedConfig());
        FabricatedConfig.getManager().initializeConfig();
        disabledModules.addAll(Arrays.stream(Config.get().getDisabledModules()).toList());
        createModule(new FabricatedCore());
        createModule(new FabricatedCamping());
        executeForAllModules(FModule::onInitialize);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        executeForAllModules(FModule::onInitializeClient);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// Under this line, is a huge amount of code that shouldn't be tampered with since I forgot how it works /////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean isForceLoaded(String name) {
        String[] forceloaded = new String[]{"core", "config"};
        if (Arrays.stream(forceloaded).toList().contains(name)) {
            return true;
        }
        return false;
    }

    public static void createModule(FModule module) {
        for (FModule fModule : totalModuleList) {
            if (module.moduleName().equals(fModule.moduleName())) throw new UnsupportedOperationException("Module name " + fModule.moduleName() + " is duplicated!");
        }
        totalModuleList.add(module);
        if (disabledModules.contains(module.moduleName())) {
            if (!isForceLoaded(module.moduleName())) {
                logger.info("Module " + module.moduleName() + " is disabled.");
                return;
            }
            logger.info("Module " + module.moduleName() + " is force-loaded and can't be disabled.");
        }
        moduleList.add(module);
    }
    public static void executeForAllModules(Consumer<FModule> method) {
        for (FModule fModule : moduleList) {
            method.accept(fModule);
        }
    }

    // for internal purposes
    @ApiStatus.Internal
    public static ModuleData isDisabled(FModule module) {
        if (isForceLoaded(module.moduleName())) {
            return new ModuleData(true, false);
        }
        boolean disabled = false;
        boolean alsoDisabled = false;
        for (String disabledModule : disabledModules) {
            if (module.moduleName().equals(disabledModule)) disabled = true;
        }
        for (String disabledModule : Config.get().getDisabledModules()) {
            if (module.moduleName().equals(disabledModule)) alsoDisabled = true;
        }
        return new ModuleData(!alsoDisabled, !(disabled == alsoDisabled));
    }

    public static class ModuleData {
        public final boolean enabled;
        public final boolean needsRestart;
        public ModuleData(boolean enabled, boolean needsRestart) {
            this.enabled = enabled;
            this.needsRestart = needsRestart;
        }
    }

    @ApiStatus.Internal
    public static void executeForAllTotalModules(Consumer<FModule> method) {
        for (FModule fModule : totalModuleList) {
            method.accept(fModule);
        }
    }

    // FModule is pronounced ef-muh-jul therefore the word "an" is used in preference to "a".
    /**
     * This method should be used for retrieving non-static public methods and fields from an {@link FModule}.
     * @param module The module name as a {@link String}.
     * @return the module.
     * @throws UnsupportedOperationException if the module can't be found.
     */
    public static FModule getModule(String module) {
        for (FModule fModule : moduleList) {
            if (fModule.moduleName().equals(module)) return fModule;
        }
        throw new RuntimeException("Module " + module + " does not exist or hasn't been initialized!");
    }

    // Method for automatically casting
    @SuppressWarnings("unchecked")
    public static <T extends FModule> T getModule(String module, Class<T> clazz) {
        return (T)getModule(module);
    }
}
