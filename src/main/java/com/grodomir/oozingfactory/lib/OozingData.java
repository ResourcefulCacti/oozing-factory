package com.grodomir.oozingfactory.lib;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record OozingData(
        ResourceLocation ingredient,
        ResourceLocation result,
        Integer tier
) {
    public static final Codec<OozingData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("ingredient").forGetter(OozingData::ingredient),
                    ResourceLocation.CODEC.fieldOf("result").forGetter(OozingData::result),
                    Codec.INT.fieldOf("tier").orElse(69).forGetter(OozingData::tier)
            ).apply(instance, OozingData::new)
    );
}