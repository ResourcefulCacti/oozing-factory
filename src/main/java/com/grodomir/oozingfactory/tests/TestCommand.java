package com.grodomir.oozingfactory.tests;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class TestCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal(OozingFactoryMod.MODID)
                        .then(Commands.literal("register")
                            .then(Commands.argument("name", StringArgumentType.word())
                                .then(Commands.argument("base", ResourceLocationArgument.id())
                                .suggests(TestCommand::suggestBlocksAndItems)
                                    .then(Commands.argument("ingredient", ResourceLocationArgument.id())
                                            .suggests(TestCommand::suggestBlocksAndItems)
                                            .executes(TestCommand::execute)
                                    )
                                )
                            )
                        )
        );
    }

    private static CompletableFuture<Suggestions> suggestBlocksAndItems(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        String remaining = builder.getRemaining().toLowerCase();

        /*BuiltInRegistries.BLOCK.keySet().stream()
                .map(ResourceLocation::toString)
                .filter(s -> s.startsWith(remaining))
                .forEach(builder::suggest);*/

        BuiltInRegistries.ITEM.keySet().stream()
                .map(ResourceLocation::toString)
                .filter(s -> s.startsWith(remaining))
                .forEach(builder::suggest);

        return builder.buildFuture();
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        String name = StringArgumentType.getString(ctx, "name");
        ResourceLocation blockOrItem1 = ResourceLocationArgument.getId(ctx, "base");
        ResourceLocation blockOrItem2 = ResourceLocationArgument.getId(ctx, "ingredient");

        boolean valid1 = isValidBlockOrItem(blockOrItem1);
        boolean valid2 = isValidBlockOrItem(blockOrItem2);

        if(!valid1){
            ctx.getSource().sendFailure(Component.literal(
                    "'" + blockOrItem1 + "' is not valid block or item"
            ));
            return 0;
        }

        if(!valid2){
            ctx.getSource().sendFailure(Component.literal(
                    "'" + blockOrItem1 + "' is not valid block or item"
            ));
            return 0;
        }

        TestOfRegister.registerSlimeFromCommand(name, blockOrItem1.toString(), blockOrItem2.toString());

        ctx.getSource().sendSuccess(
                () -> Component.literal(
                        "Registered '" + name+ "' with " + blockOrItem1 + " and " + blockOrItem2
                ),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static boolean isValidBlockOrItem(ResourceLocation id) {
        boolean isBlock = BuiltInRegistries.BLOCK.containsKey(id)
                && BuiltInRegistries.BLOCK.get(id) != Blocks.AIR;
        boolean isItem = BuiltInRegistries.ITEM.containsKey(id)
                && BuiltInRegistries.ITEM.get(id) != Items.AIR;
        return isBlock || isItem;
    }

}
