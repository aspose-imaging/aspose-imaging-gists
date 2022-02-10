import com.aspose.imaging.*;
import com.aspose.imaging.customfonthandler.CustomFontData;
import com.aspose.imaging.imageoptions.PngOptions;
import com.aspose.imaging.imageoptions.VectorRasterizationOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

String dataDir = "C:/folder/";
String[] files = new String[] { "missing-font.emf", "missing-font.odg", "missing-font.wmf", "missing-font.svg" };

for (String file : files)
{
	String outputPath = dataDir + file + ".png";
	CustomFontSourceTest(dataDir, dataDir, file, dataDir + "Fonts");
	new File(outputPath).delete();
}    

public static void CustomFontSourceTest(String inputPath, String outputPath, String fileName, String fontPath)
{
	LoadOptions loadOptions = new LoadOptions();
	loadOptions.addCustomFontSource(new CustomFontSource(), fontPath);
	try (Image img = Image.load(inputPath + fileName, loadOptions))
	{
		VectorRasterizationOptions vectorRasterizationOptions =
				(VectorRasterizationOptions)img.getDefaultOptions(new Object[] { Color.getWhite(), img.getWidth(), img.getHeight() });
		vectorRasterizationOptions.setTextRenderingHint(TextRenderingHint.SingleBitPerPixel);
		vectorRasterizationOptions.setSmoothingMode(SmoothingMode.None);

		img.save(outputPath + fileName + ".png", new PngOptions()
		{{
			setVectorRasterizationOptions(vectorRasterizationOptions);
		}});
	}
}

// The custom fonts' provider example.
static class CustomFontSource implements com.aspose.imaging.CustomFontSource
{
	@Override
	public CustomFontData[] get(Object... args)
	{
		String fontsPath = "";
		if (args.length > 0)
		{
			fontsPath = args[0].toString();
		}

		List<CustomFontData> customFontData = new ArrayList<>();
		final File[] files = new File(fontsPath).listFiles();
		if (files != null)
		{
			for (File font : files)
			{
				try
				{
					final String fontName = font.getName();
					customFontData.add(new CustomFontData(fontName.substring(0, fontName.lastIndexOf('.')), Files.readAllBytes(font.toPath())));
				}
				catch (IOException ignored)
				{
					// Skip this
				}
			}
		}

		return customFontData.toArray(new CustomFontData[0]);
	}
}