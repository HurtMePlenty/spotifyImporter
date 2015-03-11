package playlistExporter;

import org.apache.commons.lang.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EntryPoint
{
    //public static final String userId = "zdarklin";
    //public static final String clientSecret = "a3567a6b58f64f82980df940680069b0";
    //public static final String clientId = "4391e7421693481e813d357b56d65053";
    private static String userId = null;
    private static String clientSecret = null;
    private static String clientId = null;
    private static String outputPath = null;
    private static String separator;
    private static List<String> additionalFields = new ArrayList<String>();

    public static void main(String[] args)
    {

        String[] parts;
        try
        {
            String config = new String(Files.readAllBytes(Paths.get("./config.txt")));
            parts = config.split("\n");
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to read config file", e);
        }


        for (String part : parts)
        {
            if (StringUtils.isEmpty(part.trim()))
            {
                continue;
            }
            String[] keyValue = part.split("=");
            if (keyValue.length == 2)
            {
                String lowKey = keyValue[0].trim().toLowerCase();

                if (lowKey.equals("userid"))
                {
                    userId = keyValue[1].trim();
                }

                if (lowKey.equals("clientsecret"))
                {
                    clientSecret = keyValue[1].trim();
                }

                if (lowKey.equals("clientid"))
                {
                    clientId = keyValue[1].trim();
                }

                if (lowKey.equals("separator"))
                {
                    separator = keyValue[1].trim();
                }


                if (lowKey.equals("outputpath"))
                {
                    outputPath = keyValue[1].trim();
                }

                if (lowKey.equals("additionalfields"))
                {
                    String[] values = keyValue[1].trim().split("|");
                    for (String value : values)
                    {
                        if (StringUtils.isEmpty(value.trim()))
                        {
                            continue;
                        }
                        additionalFields.add(value);
                    }
                }
            }
        }

        if (userId == null || clientSecret == null || clientId == null)
        {
            throw new RuntimeException("");
        }

        if (separator == null)
        {
            separator = ",";
        }

        if (outputPath == null)
        {
            outputPath = ".";
        }

        PlaylistExporterEngine playlistExporterEngine = new PlaylistExporterEngine(clientId, clientSecret, userId);
        PlaylistWritter playlistWritter = new PlaylistWritter(outputPath, additionalFields, separator);
        playlistExporterEngine.export(playlistWritter);


    }
}
