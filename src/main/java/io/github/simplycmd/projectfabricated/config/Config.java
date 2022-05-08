package io.github.simplycmd.projectfabricated.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.simplycmd.projectfabricated.ProjectFabricated;
import io.github.simplycmd.projectfabricated.core.FModule;
import io.github.simplycmd.projectfabricated.util.ModuleUtils;

import java.util.ArrayList;

public class Config {
    private final JsonObject object;
    private Config(JsonObject jsonObject) {
        this.object = jsonObject;
    }

    public void save() {
        FabricatedConfig.getManager().save(this);
    }
    public void load() {
        FabricatedConfig.getManager().load();
    }

    public JsonObject getAsJsonObject() {
        return object;
    }

    public String[] getDisabledModules() {
        var d = object.getAsJsonArray("disabled-modules");
        ArrayList<String> disabledModules = new ArrayList<>();
        for (JsonElement jsonElement : d) {
            disabledModules.add(jsonElement.getAsString());
        }
        return disabledModules.toArray(new String[0]);
    }

    public void setDisabled(FModule module) {
        object.getAsJsonArray("disabled-modules").add(module.moduleName());
    }

    public void removeDisabled(FModule module) {
        // Have to do this to avoid a ConcurrentModificationException
        ArrayList<JsonElement> toRemove = new ArrayList<>();
        var list = object.getAsJsonArray("disabled-modules");
        for (JsonElement jsonElement : list) {
            if (jsonElement.getAsString().equals(module.moduleName())) toRemove.add(jsonElement);
        }
        for (JsonElement jsonElement : toRemove) {
            list.remove(jsonElement);
        }
    }

    public static Config get() {
        return ModuleUtils.getModule("config", FabricatedConfig.class).getConfig();
    }
}
