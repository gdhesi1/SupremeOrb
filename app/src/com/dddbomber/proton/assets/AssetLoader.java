package com.dddbomber.proton.assets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

public class AssetLoader {
	public static Bitmap loadBitmap(String location){
		try{
			BufferedImage img = ImageIO.read(AssetLoader.class.getResource(location));
			int w = img.getWidth();
			int h = img.getHeight();
			Bitmap result = new Bitmap(w, h);
			img.getRGB(0, 0, w, h, result.pixels, 0, w);
			for(int i = 0; i < result.pixels.length; i++){
				int src = result.pixels[i];
				if(src == 0 || src == 16777215){
					src = -2;
				}
				result.pixels[i] = src;
			}
			
			System.out.println("Loaded Bitmap: " +location);
			return result;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public static TaggedBitmap loadTaggedBitmap(String location, String tag){
		try{
			BufferedImage img = ImageIO.read(AssetLoader.class.getResource(location));
			int w = img.getWidth();
			int h = img.getHeight();
			TaggedBitmap result = new TaggedBitmap(w, h, tag);
			img.getRGB(0, 0, w, h, result.pixels, 0, w);
			for(int i = 0; i < result.pixels.length; i++){
				int src = result.pixels[i];
				if(src == 0 || src == 16777215){
					src = -2;
				}
				result.pixels[i] = src;
			}
			
			System.out.println("Loaded Bitmap: " +location);
			return result;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static Bitmap loadBitmapFromWeb(String location) throws Exception{
		final URL url = new URL(location);
		final HttpURLConnection connection = (HttpURLConnection) url
				.openConnection();
		connection.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
		final BufferedImage img = ImageIO.read(connection.getInputStream());
		int w = img.getWidth();
		int h = img.getHeight();
		Bitmap result = new Bitmap(w, h);
		img.getRGB(0, 0, w, h, result.pixels, 0, w);
		for(int i = 0; i < result.pixels.length; i++){
			int src = result.pixels[i];
			if(src == 0 || src == 16777215){
				src = -2;
			}
			result.pixels[i] = src;
		}

		System.out.println("Loaded Bitmap: " +location);
		return result;
	}

	public static Bitmap[] loadBitmapGroup(String string) {
		File folder;
		try {
			folder = new File(AssetLoader.class.getResource(string).getPath());
			File[] listOfFiles = folder.listFiles();
			
			Bitmap[] b = new Bitmap[listOfFiles.length];
			for(int i = 0; i < listOfFiles.length; i++){
				b[i] = AssetLoader.loadBitmap(string+"/"+listOfFiles[i].getName());
			}
			return b;
		} catch (Exception e) {
			
		}
		return null;
	}
}
