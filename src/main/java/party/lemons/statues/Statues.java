package party.lemons.statues;

import net.minecraftforge.fml.common.Mod;

@Mod(modid = Statues.MODID, name = Statues.NAME, version = Statues.VERSION, dependencies = "required-after:lemonlib")
public class Statues
{
	public static final String MODID = "statues";
	public static final String NAME = "Statues";
	public static final String VERSION = "1.0.1";

	public static final String skinServerLocation = "https://crafatar.com/skins/";

	@Mod.Instance(MODID)
	public static Statues Instance;
}
