package io.github.simplycmd.projectfabricated.config;

import com.google.gson.*;
import io.github.simplycmd.projectfabricated.ProjectFabricated;
import io.github.simplycmd.projectfabricated.util.ModuleUtils;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;

public class ConfigManager {
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final FabricatedConfig module;

    public ConfigManager(FabricatedConfig config) {
        this.module = config;
    }

    public void initializeConfig() {
        try {
            var object = getConfigFromFile();
            var q = Config.class.getDeclaredConstructor(JsonObject.class);
            q.setAccessible(true);
            Config config = q.newInstance(object);
            var cla = ModuleUtils.getModule("config", FabricatedConfig.class);
            var l = FabricatedConfig.class.getDeclaredField("config");
            l.setAccessible(true);
            l.set(cla, config);
            ProjectFabricated.getModule("config").getLogger().info("Configs are Initialized!");
        } catch (Exception e) {throw new RuntimeException(e);}
    }

    public JsonObject getConfigFromFile() {
        var d = new File(FabricLoader.getInstance().getConfigDir().toFile(), "fabricated-config.json");
        try {
            if (!d.exists()) {
                FabricLoader.getInstance().getConfigDir().toFile().mkdirs();
                d.createNewFile();
                var filewriter = new FileWriter(d);
                GSON.toJson(defaultConfig(), filewriter);
                filewriter.close();
            } else
                d.mkdirs();
            var reader = Files.newBufferedReader(d.toPath());
            return GSON.fromJson(reader, JsonObject.class);
        } catch (JsonSyntaxException e) {
            try {
                var filewriter = new FileWriter(d);
                GSON.toJson(defaultConfig(), filewriter);
                filewriter.close();
                var reader = Files.newBufferedReader(d.toPath());
                return GSON.fromJson(reader, JsonObject.class);
            } catch (Exception f) {
                throw new RuntimeException(f);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Config config) {
        try {
            var d = new File(FabricLoader.getInstance().getConfigDir().toFile(), "fabricated-config.json");
            var filewriter = new FileWriter(d);
            GSON.toJson(config.getAsJsonObject(), filewriter);
            filewriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        initializeConfig();
    }

    private JsonObject defaultConfig() {
        var q = new JsonObject();
        q.add("disabled-modules", new JsonArray());
        return q;
    }
}
