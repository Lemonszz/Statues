package party.lemons.statues.statue;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.statues.Statues;
import party.lemons.statues.block.BlockStatue;
import party.lemons.statues.block.entity.ModelBipedStatue;
import party.lemons.statues.block.entity.TileEntityStatue;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class EntityStatuePlayer extends EntityPlayer
{
	static ResourceLocation STEVE = new ResourceLocation("textures/entity/steve.png");
	static ResourceLocation ALEX = new ResourceLocation("textures/entity/alex.png");

	private String name = "";
	private ResourceLocation skin;

	@SideOnly(Side.CLIENT)
	private ITextureObject dataSkin;
	public StatueInfo info;

	public EntityStatuePlayer(World world, String name, BlockPos pos)
	{
		super(world, new GameProfile(UUID.fromString("a9cb469c-f43d-4925-946d-c85a90e58a15"), name));

		this.setPosition(pos.getX(), pos.getY(), pos.getZ());
		this.setDead();
	}


	public TileEntityStatue getTE()
	{
		BlockPos ch = getPosition();
		if(ch.equals(BlockPos.ORIGIN))
			return null;

		if(!world.getBlockState(ch).getValue(BlockStatue.MAIN_PART))
			ch = ch.down();

		TileEntity statue = world.getTileEntity(ch);
		if(statue != null && statue instanceof TileEntityStatue)
		{
			return (TileEntityStatue) statue;
		}

		return null;
	}


	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
	{
		if (slotIn == EntityEquipmentSlot.MAINHAND)
		{
			return this.getHeldItemMainhand();
		}
		else if (slotIn == EntityEquipmentSlot.OFFHAND)
		{
			return this.getHeldItemOffhand();
		}
		else
		{
			TileEntityStatue statue = getTE();
			if(statue != null)
			{
				return statue.inventory.getStackInSlot(slotIn.getIndex());
			}

			return ItemStack.EMPTY;
		}
	}


	public ItemStack getHeldItemMainhand()
	{
		TileEntity statue = getTE();
		if(statue != null)
		{
			return ((TileEntityStatue) statue).inventory.getStackInSlot(4);
		}

		return ItemStack.EMPTY;
	}

	public ItemStack getHeldItemOffhand()
	{
		TileEntity statue = getTE();
		if(statue != null)
		{
			return ((TileEntityStatue) statue).inventory.getStackInSlot(5);
		}
		return ItemStack.EMPTY;
	}

	public EntityStatuePlayer(World world)
	{
		this(world, "Steve", new BlockPos(0,0,0));
	}

	@Override
	public boolean canUseCommand(int permLevel, String commandName)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void applySkin(String name, IBlockState state, byte facing)
	{
		if(!world.isRemote)
			return;

		if(state == null)
			state = Blocks.STONE.getDefaultState();

		ResourceLocation steveSkin = new ResourceLocation("textures/entity/steve.png|B" + state + "," + facing);
		AbstractTexture steveDataSkin = getDataForSteve(steveSkin, new ResourceLocation("textures/entity/steve.png"), state, facing);

		if(!name.isEmpty())
		{
			skin = new ResourceLocation("skins/" + StringUtils.stripControlCodes(name) + state + "," + facing);
			dataSkin = getTextureForSkin(skin, steveDataSkin, name, state, facing);
		}
		else
		{
			skin = steveSkin;
			dataSkin = steveDataSkin;
		}
	}

	@SideOnly(Side.CLIENT)
	public ITextureObject getTextureForSkin(ResourceLocation skin, AbstractTexture fallbackSkin, String name, IBlockState state, byte facing)
	{
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		AbstractTexture tex = (AbstractTexture) texturemanager.getTexture(skin);

		if (tex == null) {
			GameProfile profile = TileEntitySkull.profileCache.getGameProfileForUsername(name);

			tex = new StatueTextureDownloaded(profile.getId(), skin, Statues.skinServerLocation + profile.getId() + ".png", fallbackSkin, new ImageStatueBufferDownload(this, state, facing, name+"."+state));
			texturemanager.loadTexture(skin, tex);
		}

		return tex;
	}

	@SideOnly(Side.CLIENT)
	public AbstractTexture getDataForSteve(ResourceLocation skin, ResourceLocation base, IBlockState state, byte facing) {
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		AbstractTexture tex = (AbstractTexture) texturemanager.getTexture(skin);

		if (tex == null) {
			tex = new StatueTextureStatic(base, new ImageStatueBufferDownload(this, state, facing,"steve."+state));
			texturemanager.loadTexture(skin, tex);
		}

		return tex;
	}

	@SideOnly(Side.CLIENT)
	public ITextureObject getTextureSkin() {
		return dataSkin;
	}

	public ResourceLocation getLocationSkin() {
		return skin;
	}

	@Override
	public boolean isSpectator()
	{
		return false;
	}

	@Override
	public boolean isCreative()
	{
		return false;
	}
}
