package party.lemons.statues.statue;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;


public class ImageStatueBufferDownload implements IImageBuffer
{
	EntityStatuePlayer player;
	IBlockState block;
	int blockSide;
	private int[]	imageData;
	private int		imageWidth;
	private int		imageHeight;
	String debugName;


	public ImageStatueBufferDownload(EntityStatuePlayer entityFakePlayer, IBlockState b, int side, String dn)
	{
		player=entityFakePlayer;
		block=b;
		blockSide=side;
		debugName=dn;
	}

	@Override
	public BufferedImage parseUserSkin(BufferedImage par1BufferedImage) {
		if (par1BufferedImage == null) {
			return null;
		} else {

			this.imageWidth = 64;
			this.imageHeight = 64;

			BufferedImage bufferedimage1 = new BufferedImage(this.imageWidth, 64, 2);

			Graphics graphics = bufferedimage1.getGraphics();
			graphics.drawImage(par1BufferedImage, 0, 0, null);
			graphics.dispose();

			imageData = ((DataBufferInt) bufferedimage1.getRaster().getDataBuffer()).getData();

			if(par1BufferedImage.getHeight() == 32)
			{
				copyArea(0, 16, 15, 32, 16, 48);
				copyArea(40, 16, 55, 32, 32, 48);
			}

			if(!block.getBlock().equals(Blocks.BEDROCK))
				blendArea(0, 0, this.imageWidth, this.imageHeight, block, blockSide);

			return bufferedimage1;
		}
	}

	@Override
	public void skinAvailable()
	{

	}

	private void copyArea(int x1, int y1, int x2, int y2, int tX1, int tY1)
	{
		int xO = 0;
		int yO = 0;
		for (int x = x1; x < x2; ++x)
		{
			for(int y = y1; y < y2; ++y)
			{
				this.imageData[(tX1 + xO) + (tY1 + yO) * imageWidth] = imageData[x + y * imageWidth];

				yO++;
			}
			yO = 0;
			xO ++;
		}
	}

	private void blendArea(int x1, int y1, int x2, int y2, IBlockState block,int side)
	{
		ImageData sprite = ImageData.getBlockTexture(block);
		if(sprite==null) sprite= ImageData.getBlockTexture(Blocks.STONE.getDefaultState());
		if(sprite==null) return;

		int stone[]=sprite.pixels;
		int w=sprite.w;
		int h=sprite.h;

		for (int x = x1; x < x2; ++x) {
			for (int y = y1; y < y2; ++y) {
				int rgba = imageData[x + y * imageWidth] & 0xffffffff;
				double a = ((rgba>>24)&0xff)/255.0;

				double L = (0.2125 * ((rgba >> 0) & 0xff) / 255.0) + (0.7154 * ((rgba >> 8) & 0xff) / 255.0) + (0.0721 * ((rgba >> 16) & 0xff) / 255.0);
				L*=4.0/3; if(L>1) L=1;
				int srgba=stone[(x%w) + (y%h) * w];

				double R=((srgba>> 0)&0xff)/255.0;
				double G=((srgba>> 8)&0xff)/255.0;
				double B=((srgba>>16)&0xff)/255.0;
				double A=((srgba>>24)&0xff)/255.0;

				/* overlay blending mode */
				R = R < 0.5 ? 2 * R * L : 1 - 2 * (1 - R) * (1 - L);
				G = G < 0.5 ? 2 * G * L : 1 - 2 * (1 - G) * (1 - L);
				B = B < 0.5 ? 2 * B * L : 1 - 2 * (1 - B) * (1 - L);

				int sr=(int) (R*0xff);
				int sg=(int) (G*0xff);
				int sb=(int) (B*0xff);

				imageData[x + y * imageWidth] = (sr<<0) | (sg<<8) | (sb<<16) | (((int)(a*A*0xff))<<24);
			}
		}
	}

	/**
	 * Makes the given area of the image transparent if it was previously
	 * completely opaque (used to remove the outer layer of a skin around the
	 * head if it was saved all opaque; this would be redundant so it's assumed
	 * that the skin maker is just using an image editor without an alpha
	 * channel)
	 */
	private void setAreaTransparent(int par1, int par2, int par3, int par4) {
		if (!this.hasTransparency(par1, par2, par3, par4)) {
			for (int i1 = par1; i1 < par3; ++i1) {
				for (int j1 = par2; j1 < par4; ++j1) {
					this.imageData[i1 + j1 * this.imageWidth] &= 16777215;
				}
			}
		}
	}

	/**
	 * Makes the given area of the image opaque
	 */
	private void setAreaOpaque(int par1, int par2, int par3, int par4) {
		for (int i1 = par1; i1 < par3; ++i1) {
			for (int j1 = par2; j1 < par4; ++j1) {
				this.imageData[i1 + j1 * this.imageWidth] |= -16777216;
			}
		}
	}

	/**
	 * Returns true if the given area of the image contains transparent pixels
	 */
	private boolean hasTransparency(int par1, int par2, int par3, int par4) {
		for (int i1 = par1; i1 < par3; ++i1) {
			for (int j1 = par2; j1 < par4; ++j1) {
				int k1 = this.imageData[i1 + j1 * this.imageWidth];

				if ((k1 >> 24 & 255) < 128) {
					return true;
				}
			}
		}

		return false;
	}

}
