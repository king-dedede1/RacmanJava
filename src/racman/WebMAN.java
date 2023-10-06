package racman;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public final class WebMAN
{
    static HttpClient client = HttpClient.newHttpClient();
    static String ip;

    // Don't allow this class to be instantiated.
    private WebMAN() {}

    private static String GetData(String url)
    {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try
        {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static int getCurrentPID()
    {
        String output = GetData("http://" + ip + "/getmem.ps3mapi?");
        int ebootPos = output.indexOf("_main_EBOOT.BIN");
        if (ebootPos != -1)
        {
            String processHex = output.substring(ebootPos - 8, ebootPos);
            int processDec = Integer.parseInt(processHex, 16);
            return processDec;
        }
        else
        {
            return 0;
        }
    }

    public static void writeMemory(int pid, int address, byte[] memory)
    {
        String addr = Integer.toHexString(address);

        StringBuilder sb = new StringBuilder(memory.length * 2);
        for (byte b : memory)
        {
            sb.append(String.format("%02X", b));
        }

        GetData("http://"+ip+"/setmem.ps3mapi?proc="+pid+"$addr="+addr+"&val="+sb.toString());
    }

    public static void writeMemory(int pid, int address, String memory)
    {
        String addr = Integer.toHexString(address);
        GetData("http://"+ip+"/setmem.ps3mapi?proc="+pid+"$addr="+addr+"&val="+memory);
    }

    public static void writeMemoryBig(int pid, int address, byte[] memory)
    {
        final int BLOCK_SIZE = 0x100;
        int bytesToWrite = memory.length;
        int idx = 0;

        while (bytesToWrite > 0)
        {
            byte[] block = new byte[Math.min(bytesToWrite, BLOCK_SIZE)];
            System.arraycopy(memory, idx, block, 0, block.length);
            writeMemory(pid, address + idx, block);
            bytesToWrite -= block.length;
            idx += block.length;
        }
    }

    public static byte[] readMemory(int pid, int address, int size)
    {
        String addr = Integer.toHexString(address);
        String output = GetData("http://"+ip+"/getmem.ps3mapi?proc="+pid+"$addr="+addr+"&len="+size);
        int resPos = output.indexOf("</textarea>");
        String resultString = output.substring(resPos - (int) size*2, resPos);

        byte[] out = new byte[size];
        for (int arrayp = 0, stringp = 0; stringp < resultString.length(); arrayp++, stringp+=2)
        {
            out[arrayp] = (byte) Integer.parseInt(resultString.substring(stringp, stringp+2), 16);
        }
        return out;
    }

    public static void pauseRSX()
    {
        GetData("http://"+ip+"/xmb.ps3$rsx_pause");
    }

    public static void continueRSX()
    {
        GetData("http://"+ip+"/xmb.ps3$rsx_continue");
    }

    public static void notify(String message)
    {
        GetData("http://"+ip+"/popup.ps3/"+ URLEncoder.encode(message, StandardCharsets.US_ASCII));
    }
}
