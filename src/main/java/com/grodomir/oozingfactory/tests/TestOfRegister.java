package com.grodomir.oozingfactory.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.lib.OozingData;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestOfRegister {
    public static void registerSlimeFromCommand(String filename, String base, String result){
        List<OozingData> dataList = new ArrayList<>();

        dataList.add(new OozingData(
                ResourceLocation.parse(base),
                ResourceLocation.parse(result),
                1
        ));

        registerSlime(dataList, filename);
    }

    private static void registerSlime(List<OozingData> data, String filename){
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ResourceLocation.class,
                        (JsonSerializer<ResourceLocation>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString())).create();

        String path = "/config/" + OozingFactoryMod.MODID;
        String fileName = filename + ".json";

        String filePath = FMLPaths.GAMEDIR.get().toString() + path + "/" + fileName;

        File directory = new File(FMLPaths.GAMEDIR.get().toString() + path);
        if(!directory.exists()){
            directory.mkdirs();
        }

        try(FileWriter writer = new FileWriter(filePath)){
            /*gson.toJson(data, writer);
            System.out.println("Json created: " + filePath);*/
            if(data.size() == 1){
                gson.toJson(data.get(0), writer);
            }else{
                gson.toJson(data, writer);
            }
            System.out.println("Json created: " + filePath);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
