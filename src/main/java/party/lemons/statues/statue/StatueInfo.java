package party.lemons.statues.statue;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class StatueInfo
{
	public float armLeftA;
	public float armLeftB;
	public float armRightA;
	public float armRightB;
	public float legLeftA;
	public float legLeftB;
	public float legRightA;
	public float legRightB;
	public float headA;
	public float headB;
	public float bodyA;
	public float bodyB;
	public float itemLeftA;
	public float itemRightA;

	public String name;

	public StatueInfo()
	{
		armLeftA=2.0f/3;
		armRightA=2.0f/3;
		armLeftB=0.0f;
		armRightB=0.0f;

		legLeftA=1.0f;
		legLeftB=0.5f;
		legRightA=1.0f;
		legRightB=0.5f;

		headA=0.5f;
		headB=0.5f;
		bodyA=0.5f;
		bodyB=0.5f;

		itemLeftA=1.0f;

		name = "";
	}

	public void copyFrom(StatueInfo s){
		armLeftA=s.armLeftA;
		armLeftB=s.armLeftB;
		armRightA=s.armRightA;
		armRightB=s.armRightB;
		legLeftA=s.legLeftA;
		legLeftB=s.legLeftB;
		legRightA=s.legRightA;
		legRightB=s.legRightB;
		headA=s.headA;
		headB=s.headB;
		bodyA=s.bodyA;
		bodyB=s.bodyB;
		itemLeftA=s.itemLeftA;
		itemRightA=s.itemRightA;

		name = s.name;
	}

	public void write(ByteBuf buf)
	{
		buf.writeFloat(armLeftA);
		buf.writeFloat(armLeftB);
		buf.writeFloat(armRightA);
		buf.writeFloat(armRightB);
		buf.writeFloat(legLeftA);
		buf.writeFloat(legLeftB);
		buf.writeFloat(legRightA);
		buf.writeFloat(legRightB);
		buf.writeFloat(headA);
		buf.writeFloat(headB);
		buf.writeFloat(bodyA);
		buf.writeFloat(bodyB);
		buf.writeFloat(itemLeftA);
		buf.writeFloat(itemRightA);

		ByteBufUtils.writeUTF8String(buf, name);
	}

	public void read(ByteBuf buf){
		armLeftA=buf.readFloat();
		armLeftB=buf.readFloat();
		armRightA=buf.readFloat();
		armRightB=buf.readFloat();
		legLeftA=buf.readFloat();
		legLeftB=buf.readFloat();
		legRightA=buf.readFloat();
		legRightB=buf.readFloat();
		headA=buf.readFloat();
		headB=buf.readFloat();
		bodyA=buf.readFloat();
		bodyB=buf.readFloat();
		itemLeftA=buf.readFloat();
		itemRightA=buf.readFloat();

		name = ByteBufUtils.readUTF8String(buf);
	}

	public void readFromNBT(NBTTagCompound tag) {
		if(!tag.hasKey("ala")) return;

		armLeftA=tag.getFloat("ala");
		armLeftB=tag.getFloat("alb");
		armRightA=tag.getFloat("ara");
		armRightB=tag.getFloat("arb");
		legLeftA=tag.getFloat("lla");
		legLeftB=tag.getFloat("llb");
		legRightA=tag.getFloat("lra");
		legRightB=tag.getFloat("lrb");
		headA=tag.getFloat("ha");
		headB=tag.getFloat("hb");
		bodyA=tag.getFloat("ba");
		bodyB=tag.getFloat("bb");
		itemLeftA=tag.getFloat("ila");
		itemRightA=tag.getFloat("ira");

		name = tag.getString("name");

	}

	public NBTTagCompound toNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("ala",armLeftA);
		tag.setFloat("alb",armLeftB);
		tag.setFloat("ara",armRightA);
		tag.setFloat("arb",armRightB);
		tag.setFloat("lla",legLeftA);
		tag.setFloat("llb",legLeftB);
		tag.setFloat("lra",legRightA);
		tag.setFloat("lrb",legRightB);
		tag.setFloat("ha",headA);
		tag.setFloat("hb",headB);
		tag.setFloat("ba",bodyA);
		tag.setFloat("bb",bodyB);
		tag.setFloat("ila",itemLeftA);
		tag.setFloat("ira",itemRightA);

		tag.setString("name", name);

		return tag;
	}


}