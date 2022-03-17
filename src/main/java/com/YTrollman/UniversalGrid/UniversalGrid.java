package com.YTrollman.UniversalGrid;

import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridGridFactory;
import com.YTrollman.UniversalGrid.item.WirelessUniversalGridItem;
import com.YTrollman.UniversalGrid.registry.ModItems;
import com.YTrollman.UniversalGrid.registry.ModKeyBindings;
import com.YTrollman.UniversalGrid.registry.ModNetworkHandler;
import com.refinedmods.refinedstorage.api.IRSAPI;
import com.refinedmods.refinedstorage.api.RSAPIInject;
import com.refinedmods.refinedstorage.item.property.NetworkItemPropertyGetter;
import com.refinedmods.refinedstorage.screen.KeyInputListener;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("universalgrid")
public class UniversalGrid {
    public static final String MOD_ID = "universalgrid";

    @RSAPIInject
    public static IRSAPI RSAPI;
    public static final ModNetworkHandler NETWORK_HANDLER = new ModNetworkHandler();

    public UniversalGrid() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::onRegisterItems);
        MinecraftForge.EVENT_BUS.addListener(this::onKeyInput);
    }

    private void setup(FMLCommonSetupEvent event) {
        UniversalGrid.NETWORK_HANDLER.register();
        UniversalGrid.RSAPI.getGridManager().add(WirelessUniversalGridGridFactory.ID, new WirelessUniversalGridGridFactory());
    }

    private void doClientStuff(FMLClientSetupEvent event) {
        ItemModelsProperties.register(ModItems.WIRELESS_UNIVERSAL_GRID, new ResourceLocation("connected"), new NetworkItemPropertyGetter());
        ItemModelsProperties.register(ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID, new ResourceLocation("connected"), new NetworkItemPropertyGetter());

        ClientRegistry.registerKeyBinding(ModKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    	
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(new WirelessUniversalGridItem(WirelessUniversalGridItem.Type.NORMAL));
        e.getRegistry().register(new WirelessUniversalGridItem(WirelessUniversalGridItem.Type.CREATIVE));
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        if (Minecraft.getInstance().player != null) {
            if (ModKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID.isDown()) {
                KeyInputListener.findAndOpen(ModItems.WIRELESS_UNIVERSAL_GRID, ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID);
            }
        }
    }
}
