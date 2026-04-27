package com.grodomir.oozingfactory.common.block.custom;

import com.grodomir.oozingfactory.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


public class PlaceHolderBlock extends Block {
    public PlaceHolderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        //maybeTransferFluid(state, level, pos, random.nextFloat());

        BlockState blockStateAbove = level.getBlockState(pos.above());
        BlockState blockStateBelow = level.getBlockState(pos.below());

        System.out.println("Block above: " + getBlockAbove(level, pos));
        if(canDrip(level, pos)){
            System.out.println("Block above is Slime Block!" + getBlockAbove(level, pos));
            if(blockStateBelow.is(Blocks.CAULDRON)){
                level.setBlock(pos.below(), Blocks.WATER_CAULDRON.defaultBlockState(), 3);
            }else if(blockStateBelow.is(Blocks.WATER_CAULDRON)){
                int currentLevel = blockStateBelow.getValue(LayeredCauldronBlock.LEVEL);

                if(currentLevel < 3){
                    level.setBlock(
                            pos.below(),
                            blockStateBelow.setValue(LayeredCauldronBlock.LEVEL, currentLevel +1),
                            3
                    );
                }
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        /*if(canDrip(level, pos)){
            float f = random.nextFloat();
            if(!(f > 0.12F)){
                spawnDripParticle(level, pos, state, random);
            }
        }*/

        if(canDrip(level, pos)){
            for(int i = 0; i < 4; i++){
                if(random.nextFloat() < 0.2F){
                    spawnDripParticle(level, pos, state, random);
                }
            }
        }
    }

    private static void spawnDripParticle(Level level, BlockPos pos, BlockState state, RandomSource random){
        Vec3 vec3 = state.getOffset(level, pos);

        double d1 = (double)pos.getX() + random.nextDouble();
        double d2 = (double)pos.getY() + 0.9;
        double d3 = (double)pos.getZ() + random.nextDouble();


        //level.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER,d1, d2, d3, 0.0, 0.0, 0.0);
        //level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, getBlockAbove(level, pos)), d1, d2, d3, 0.0, 0.0, 0.0);
        level.addParticle(ModParticles.SLIME_FLUID_PARTICLES.get(), d1, d2, d3, 0.0, 0.5, 0.0);
    }

    public static boolean canDrip(Level level, BlockPos pos) {
        return getBlockAbove(level, pos).getBlock() instanceof SlimeBlock;
    }

    private static BlockState getBlockAbove(Level level, BlockPos pos){
        BlockPos blockPos = pos.above();
        BlockState blockState = level.getBlockState(blockPos);

        return blockState;
    }
}
