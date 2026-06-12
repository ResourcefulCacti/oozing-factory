package com.grodomir.oozingfactory.datagen;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.common.block.ModBlocks;
import com.grodomir.oozingfactory.setup.OozingSetup;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class OozingBlockStateProvider extends BlockStateProvider {
    public OozingBlockStateProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, OozingFactoryMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        System.out.println("SLIME_MAP keys: " + ModBlocks.SLIME_MAP.keySet());
        System.out.println("OozingSetup loaded: " + OozingSetup.getLoaded().keySet());

        OozingSetup.getLoaded().forEach((name, data) -> {
            data.ingredient();
            //Turns minecraft:iron_ore into:
            //divided[0] = minecraft
            //divided[1] = iron_ore
            String rLocation = data.ingredient().toString();
            String regex = "[:]";
            String [] divided = rLocation.split(regex);

            //String blockName = name + "_slime_block";
            DeferredBlock<Block> block = ModBlocks.SLIME_MAP.get(name);

            slimeModel(block, divided[0], divided[1]);
        });
    }

    private void slimeModel(DeferredBlock<?> block, String customLoc, String inside){
        String name = block.getId().getPath();

        ModelFile model = models()
                .withExistingParent(name, mcLoc("block/block"))
                .renderType("translucent")
                .texture("particle", mcLoc("block/slime_block"))
                .texture("texture", mcLoc("block/slime_block"))
                .texture("inside", customLoc + ":block/" + inside)

                .element()
                .from(3, 3, 3)
                .to(13, 13, 13)
                .face(Direction.DOWN)
                .uvs(3, 3, 13, 13)
                .texture("#inside")
                .end()
                .face(Direction.UP)
                .uvs(3, 3, 13, 13)
                .texture("#inside")
                .end()
                .face(Direction.NORTH)
                .uvs(3, 3, 13, 13)
                .texture("#inside")
                .end()
                .face(Direction.SOUTH)
                .uvs(3, 3, 13, 13)
                .texture("#inside")
                .end()
                .face(Direction.WEST)
                .uvs(3, 3, 13, 13)
                .texture("#inside")
                .end()
                .face(Direction.EAST)
                .uvs(3, 3, 13, 13)
                .texture("#inside")
                .end()
                .end()

                .element()
                .from(0, 0, 0)
                .to(16, 16, 16)
                .face(Direction.DOWN)
                .uvs(0, 0, 16, 16)
                .texture("#texture")
                .cullface(Direction.DOWN)
                .end()
                .face(Direction.UP)
                .uvs(0, 0, 16, 16)
                .texture("#texture")
                .cullface(Direction.UP)
                .end()
                .face(Direction.NORTH)
                .uvs(0, 0, 16, 16)
                .texture("#texture")
                .cullface(Direction.NORTH)
                .end()
                .face(Direction.SOUTH)
                .uvs(0, 0, 16, 16)
                .texture("#texture")
                .cullface(Direction.SOUTH)
                .end()
                .face(Direction.WEST)
                .uvs(0, 0, 16, 16)
                .texture("#texture")
                .cullface(Direction.WEST)
                .end()
                .face(Direction.EAST)
                .uvs(0, 0, 16, 16)
                .texture("#texture")
                .cullface(Direction.EAST)
                .end()
                .end();

        simpleBlockWithItem(block.get(), model);
    }
}
