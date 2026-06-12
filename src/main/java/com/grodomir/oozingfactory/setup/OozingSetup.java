package com.grodomir.oozingfactory.setup;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.grodomir.oozingfactory.lib.ModPaths;
import com.grodomir.oozingfactory.lib.OozingData;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class OozingSetup {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<String, OozingData> LOADED = new HashMap<>();

    private OozingSetup() {};

    public static void setup(){
        try {
            Files.createDirectories(ModPaths.OOZING);
        } catch (IOException e) {
            LOGGER.error("Failed to create oozingfactory directory", e);
        }

        LOGGER.info("Loading Oozing Factory data...");
        streamFilesAndParse(ModPaths.OOZING);
        LOGGER.info("Loading {} entries for Oozing Factory", LOADED.size());

        // Debug - remove later
        LOADED.forEach((name, data) ->
                LOGGER.info("Loaded entry '{}': ingredient={}, result={}", name, data.ingredient(), data.result())
        );
    }

    private static void streamFilesAndParse(Path oozing) {
        try(Stream<Path> paths = Files.walk(oozing)){
            paths.filter(p -> p.toString().endsWith(".json")).forEach(OozingSetup::parseFile);
        }catch (IOException e){
            LOGGER.error("Failed to walk Oozing Factory directory: {}", oozing, e);
        }
    }

    private static void parseFile(Path path) {
        String name = path.getFileName().toString().replace(".json", "");
        try(Reader reader = Files.newBufferedReader(path)){
            JsonElement jsonElement = JsonParser.parseReader(reader);
            OozingData.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                    .resultOrPartial(e -> LOGGER.error("Failed to parse {}: {}", name, e))
                    .ifPresent(data -> LOADED.put(name, data));
        }catch (IOException e){
            LOGGER.error("Failed to read file {}: {}", path, e);
        }
    }

    public static Map<String, OozingData> getLoaded(){
        return LOADED;
    }
}
