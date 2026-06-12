package com.grodomir.oozingfactory.compat;

import com.grodomir.oozingfactory.common.block.custom.OozingBaseSlimeBlock;
import com.grodomir.oozingfactory.common.block.entity.OozingBaseSlimeBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class OozingJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(OozingJadeProvider.INSTANCE, OozingBaseSlimeBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(OozingJadeProvider.INSTANCE, OozingBaseSlimeBlock.class);
    }
}
