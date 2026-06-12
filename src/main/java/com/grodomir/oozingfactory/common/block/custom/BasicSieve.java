package com.grodomir.oozingfactory.common.block.custom;

import com.grodomir.oozingfactory.common.block.ModBlocks;
import com.grodomir.oozingfactory.common.block.entity.BasicSieveEntity;
import com.grodomir.oozingfactory.particle.ModParticles;
import com.grodomir.oozingfactory.registry.OozingEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class BasicSieve extends BaseEntityBlock {
    public static final MapCodec<BasicSieve> CODEC = simpleCodec(BasicSieve::new);

    private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    private static final int MAX_ITERATIONS = 5;

    public BasicSieve(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState blockStateAbove = level.getBlockState(pos.above());
        BlockState blockStateBelow = level.getBlockState(pos.below());

        //System.out.println("Block above: " + getBlockAbove(level, pos));
        if(canDrip(level, pos)){
           // System.out.println("Block above is Slime Block!" + getBlockAbove(level, pos));
            //if(blockStateBelow.is(Blocks.CAULDRON)){
            BlockPos cauldronPos = findCauldronVertical(level, pos, MAX_ITERATIONS);
            if(cauldronPos != null){
                level.setBlock(cauldronPos, ModBlocks.SLIME_CAULDRON.get().defaultBlockState(), 3);
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

    public static boolean findCauldron(Level level, BlockPos pos){
        BlockState below = level.getBlockState(pos.below());
        return below.is(Blocks.CAULDRON);
    }

    public static boolean isCauldronEmpty(Level level, BlockPos pos){
        BlockPos cauldronPos = findCauldronVertical(level, pos, MAX_ITERATIONS);
        if(cauldronPos != null){
            BlockState cauldron = level.getBlockState(cauldronPos);
            return cauldron.is(Blocks.CAULDRON);
        }
        return false;

    }

    public static BlockPos findCauldronVertical(Level level, BlockPos pos, int maxIterations){
        BlockPos.MutableBlockPos searchPos = pos.mutable();

        for(int i = 0; i <= maxIterations; i++){
            searchPos.move(Direction.DOWN);
            BlockState state = level.getBlockState(searchPos);

            if(state.is(Blocks.CAULDRON)){
                return searchPos.immutable();
            }

            if(!canDripThrough(level, searchPos, state)){
                return null;
            }
        }
        return null;
    }

    private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.isAir()) {
            return true;
        } else if (state.isSolidRender(level, pos)) {
            return false;
        } else if (!state.getFluidState().isEmpty()) {
            return false;
        } else {
            VoxelShape voxelshape = state.getCollisionShape(level, pos);
            return !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, voxelshape, BooleanOp.AND);
        }
    }

    private static void spawnDripParticle(Level level, BlockPos pos, BlockState state, RandomSource random){
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

    public static BlockState getBlockAbove(Level level, BlockPos pos){
        BlockPos blockPos = pos.above();
        BlockState blockState = level.getBlockState(blockPos);

        return blockState;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof BasicSieveEntity basicSieveEntity){
                basicSieveEntity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide()){
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof BasicSieveEntity basicSieveEntity){
                ((ServerPlayer) player).openMenu(new SimpleMenuProvider(basicSieveEntity, Component.translatable("menu.oozingfactory.basic_sieve")), pos);
            }else{
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()){
            return null;
        }

        return createTickerHelper(blockEntityType, OozingEntities.BASIC_SIEVE_BE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BasicSieveEntity(pos, state);
    }
}
