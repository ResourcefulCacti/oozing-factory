package com.grodomir.oozingfactory.lib;

import com.grodomir.oozingfactory.OozingFactoryMod;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class ModPaths {
    public static final Path OOZING = FMLPaths.GAMEDIR.get().resolve("config/"+ OozingFactoryMod.MODID + "/custom");

    private ModPaths(){}
}
