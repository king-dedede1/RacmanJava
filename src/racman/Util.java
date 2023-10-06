package racman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Util
{
    public static byte[] readFileBytes(String path)
    {
        File file = new File(path);

        if (!file.exists())
        {
            throw new IllegalArgumentException("File doesn't exist");
        }

        try
        {
            FileInputStream stream = new FileInputStream(file);
            var bytes = stream.readAllBytes();
            stream.close();
            return bytes;
        }
        catch (IOException e)
        {
            // huh
            e.printStackTrace();
        }
        return null;
    }
}
