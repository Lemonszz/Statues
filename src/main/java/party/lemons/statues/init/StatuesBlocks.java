package party.lemons.statues.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import party.lemons.lemonlib.block.BlockRegistry;
import party.lemons.statues.Statues;
import party.lemons.statues.block.BlockStatue;
import party.lemons.statues.block.entity.TileEntityStatue;

@Mod.EventBusSubscriber(modid = Statues.MODID)
@GameRegistry.ObjectHolder(Statues.MODID)
public class StatuesBlocks
{
	public static final Block STATUE = Blocks.AIR;

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		BlockRegistry.setup(Statues.MODID, event.getRegistry(), CreativeTabs.DECORATIONS);

		BlockRegistry.registerBlock(new BlockStatue().setHardness(1F).setResistance(1F), "statue");

		GameRegistry.registerTileEntity(TileEntityStatue.class, new ResourceLocation(Statues.MODID, "statue"));
	}
}
