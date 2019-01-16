package party.lemons.statues.statue;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.HashMap;

public class ImageData {
	static HashMap<IBlockState,ImageData> blockTextures= new HashMap<>();

	int w,h;
	int pixels[];

	ImageData(int[] pp,int ww,int hh){
		pixels=pp; w=ww; h=hh;
	}

	static ImageData invalidData=new ImageData(null,0,0);

	static ImageData getBlockTexture(IBlockState block){
		ImageData res=blockTextures.get(block);

		if(res==invalidData) return null;
		if(res!=null) return res;


		try {
			String name = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block).getIconName();
			ResourceLocation nameLoc = new ResourceLocation(name);
			ResourceLocation resource = new ResourceLocation(nameLoc.getNamespace(),  "textures/" + nameLoc.getPath() + ".png");

			BufferedImage origImage=ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream());
			BufferedImage image = new BufferedImage(origImage.getWidth(), origImage.getHeight(), 2);
			Graphics graphics = image.getGraphics();
			graphics.drawImage(origImage, 0, 0, null);
			graphics.dispose();

			res=new ImageData(((DataBufferInt) image.getRaster().getDataBuffer()).getData(),origImage.getWidth(), origImage.getHeight());
			blockTextures.put(block, res);
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		}

		blockTextures.put(block, invalidData);
		return null;
	}


}