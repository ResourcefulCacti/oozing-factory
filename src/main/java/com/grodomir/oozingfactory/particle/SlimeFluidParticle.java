package com.grodomir.oozingfactory.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class SlimeFluidParticle extends TextureSheetParticle {
    protected SlimeFluidParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.setSpriteFromAge(spriteSet);

        this.gravity = 0.05F;
        this.friction = 0.98F;
        this.xd = 0.00;
        this.yd = -0.02;
        this.zd = 0.00;
        this.lifetime = 40;
    }

    /*@Override
    public void tick() {
        super.tick();

        if(this.onGround){
            this.remove();

            this.level.addParticle(
                    ModParticles.SLIME_FLUID_PARTICLES.get(),
                    this.x, this,y, this.z,
                    0.0, 0.0, 0.0
            );
        }
    }*/

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>{
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SlimeFluidParticle(level, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}
