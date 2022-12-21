package rbasamoyai.betsyross;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import rbasamoyai.betsyross.flags.FlagBlock;
import rbasamoyai.betsyross.flags.FlagBlockEntity;
import rbasamoyai.betsyross.flags.FlagBlockItem;
import rbasamoyai.betsyross.network.BetsyRossNetwork;

@Mod(BetsyRoss.MOD_ID)
public class BetsyRoss {

    public static final String MOD_ID = "betsyross";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab MOD_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP,0)
            .title(Component.translatable("itemGroup." + MOD_ID))
            .build();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final RegistryObject<FlagBlock> FLAG_BLOCK = BLOCKS.register("flag_block",
            () -> new FlagBlock(FlagBlock.properties()));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<BlockItem> FLAG_ITEM = ITEMS.register("flag_block",
            () -> new FlagBlockItem(FLAG_BLOCK.get(), new Item.Properties().stacksTo(1)));

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public static final RegistryObject<BlockEntityType<FlagBlockEntity>> FLAG_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("flag",
            () -> BlockEntityType.Builder.of(FlagBlockEntity::new, FLAG_BLOCK.get()).build(null));

    public BetsyRoss() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITY_TYPES.register(modBus);

        modBus.addListener(this::onCommonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> BetsyRossClient.onCtor(modBus, forgeBus));
    }

    private void onCommonSetup(FMLCommonSetupEvent evt) {
        BetsyRossNetwork.init();
    }

    public static ResourceLocation path(String path) { return new ResourceLocation(MOD_ID, path); }

}