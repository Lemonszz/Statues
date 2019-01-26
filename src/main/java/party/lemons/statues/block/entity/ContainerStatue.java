package party.lemons.statues.block.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.statues.init.StatuesNetwork;
import party.lemons.statues.network.MessageUpdateEquipment;
import party.lemons.statues.statue.EntityStatuePlayer;

import javax.annotation.Nullable;

public class ContainerStatue extends Container
{
	private InventoryPlayer inventoryPlayer;
	private TileEntityStatue statue;

	public ContainerStatue(EntityPlayer player, InventoryPlayer playerInventory, TileEntityStatue te)
	{
		super();
		this.inventoryPlayer = playerInventory;
		this.statue = te;

		EntityEquipmentSlot[] slots = {EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD};

		for(int i = 0; i < slots.length; i++)
		{
			final EntityEquipmentSlot entityequipmentslot = slots[i];
			addSlotToContainer(new SlotEquipment(te, entityequipmentslot, player, te.inventory, entityequipmentslot.getIndex(), 80, 87 - i * 18));
		}
		addSlotToContainer(new SlotEquipment(te, EntityEquipmentSlot.MAINHAND, player, te.inventory, 4, 49, 51));
		addSlotToContainer(new SlotEquipment(te, EntityEquipmentSlot.MAINHAND, player, te.inventory, 5, 111, 51));

		for(int i = 0; i < 3; ++i) {
			for(int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(playerInventory, k + i * 9 + 9, 8 + k * 18, i * 18 + 144));
			}
		}

		for(int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 202));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer)
	{
		return true;
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(index > 5)
			{
				if(!this.mergeItemStack(itemstack1, 0, 6, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if(!mergeItemStack(itemstack1, 6, 42, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		detectAndSendChanges();
		return itemstack;
	}
	private static class SlotEquipment extends Slot
	{
		private EntityEquipmentSlot type;
		private EntityPlayer entity;
		private TileEntityStatue te;

		public SlotEquipment(TileEntityStatue te, EntityEquipmentSlot type, EntityPlayer entity, IInventory inventoryIn, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);

			this.type = type;
			this.entity = entity;
			this.te = te;
		}

		@Override
		public void onSlotChanged()
		{
			if(!te.getWorld().isRemote)
			{
				te.markDirty();

				//StatuesNetwork.INSTANCE.sendToAllAround(new MessageUpdateEquipment(te.getPos(), te), new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), 128));
				StatuesNetwork.INSTANCE.sendToAll(new MessageUpdateEquipment(te.getPos(), te));
			}
			super.onSlotChanged();
		}

		public int getSlotStackLimit()
		{
			return 1;
		}

		public boolean isItemValid(ItemStack stack)
		{
			return type.getSlotType() == EntityEquipmentSlot.Type.HAND || stack.getItem().isValidArmor(stack, type, entity);
		}

		public boolean canTakeStack(EntityPlayer playerIn)
		{
			ItemStack itemstack = this.getStack();
			return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
		}

		@Nullable
		@SideOnly(Side.CLIENT)
		public String getSlotTexture()
		{
			switch(type)
			{
				default:
					return null;
				case FEET:
					return ItemArmor.EMPTY_SLOT_NAMES[0];
				case LEGS:
					return ItemArmor.EMPTY_SLOT_NAMES[1];
				case CHEST:
					return ItemArmor.EMPTY_SLOT_NAMES[2];
				case HEAD:
					return ItemArmor.EMPTY_SLOT_NAMES[3];
			}
		}
	}
}
