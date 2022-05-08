package io.github.simplycmd.projectfabricated.util;

import io.github.simplycmd.projectfabricated.ProjectFabricated;
import io.github.simplycmd.projectfabricated.core.FModule;

public interface ModuleUtils {
    public static FModule getModule(String module) {
        return ProjectFabricated.getModule(module);
    }

    public static <T extends FModule> T getModule(String module, Class<T> clazz) {
        return ProjectFabricated.getModule(module, clazz);
    }
}
