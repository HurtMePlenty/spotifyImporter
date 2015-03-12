package playlistExporter;

import com.google.common.base.Charsets;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlaylistWritter
{
    private String outputPath;
    private List<String> additionalFields;
    private String separator;

    public PlaylistWritter(String outputPath, List<String> additionalFields, String separator)
    {
        this.outputPath = outputPath;
        this.additionalFields = additionalFields;
        this.separator = separator;
    }

    public void write(Playlist playlist)
    {
        try
        {
            if (playlist != null)
            {
                Path path = Paths.get(outputPath, playlist.getName().replaceAll("[^a-zA-Z0-9.-]", "_") + ".csv");
                File file = new File(path.toString());
                if (!file.exists())
                {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                if (playlist.getTracks() != null)
                {
                    StringBuilder builder = new StringBuilder();
                    renderHeader(builder);
                    builder.append("\n");

                    for (PlaylistTrack playlistTrack : playlist.getTracks().getItems())
                    {
                        Track track = playlistTrack.getTrack();
                        renderBody(builder, track);
                        builder.append("\n");
                    }


                    FileOutputStream out = new FileOutputStream(file);
                    out.write(builder.toString().getBytes(Charsets.UTF_8));
                }
            }
        }
        catch (Exception e)
        {
            //suppress
        }
    }

    private void renderHeader(StringBuilder builder)
    {
        builder.append("Title");
        builder.append(separator);
        builder.append("Artist");

        if (additionalFields.contains("duration"))
        {
            builder.append(separator);
            builder.append("Duration");
        }

        if (additionalFields.contains("discnumber"))
        {
            builder.append(separator);
            builder.append("Disc number");
        }

        if (additionalFields.contains("tracknumber"))
        {
            builder.append(separator);
            builder.append("Track number");
        }

        if (additionalFields.contains("externalurl"))
        {
            builder.append(separator);
            builder.append("External url");
        }


        if (additionalFields.contains("availablemarkets"))
        {
            builder.append(separator);
            builder.append("Available markets");
        }

        if (additionalFields.contains("previewurl"))
        {
            builder.append(separator);
            builder.append("Preview Url");
        }

        if (additionalFields.contains("popularity"))
        {
            builder.append(separator);
            builder.append("Popularity");
        }

        if (additionalFields.contains("href"))
        {
            builder.append(separator);
            builder.append("href");
        }

        if (additionalFields.contains("uri"))
        {
            builder.append(separator);
            builder.append("uri");
        }

        if (additionalFields.contains("id"))
        {
            builder.append(separator);
            builder.append("id");
        }

    }

    private void renderBody(StringBuilder builder, Track track)
    {
        builder.append(track.getName());

        builder.append(separator);

        List<SimpleArtist> simpleArtistList = track.getArtists();
        if (simpleArtistList != null && simpleArtistList.size() > 0)
        {
            builder.append(track.getArtists().get(0).getName());
        } else
        {
            builder.append("");
        }

        if (additionalFields.contains("duration"))
        {
            builder.append(separator);
            builder.append(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(track.getDuration()), TimeUnit.MILLISECONDS.toSeconds(track.getDuration()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(track.getDuration()))));
        }

        if (additionalFields.contains("discnumber"))
        {
            builder.append(separator);
            builder.append(track.getDiscNumber());
        }

        if (additionalFields.contains("tracknumber"))
        {
            builder.append(separator);
            builder.append(track.getTrackNumber());
        }

        if (additionalFields.contains("externalurl"))
        {
            builder.append(separator);
            StringBuilder result = new StringBuilder();
            for (String key : track.getExternalUrls().getExternalUrls().keySet())
            {
                result.append(track.getExternalUrls().get(key));
                result.append("  ");
            }
            builder.append(result);
        }

        if (additionalFields.contains("availablemarkets"))
        {
            builder.append(separator);
            StringBuilder result = new StringBuilder();
            for (String market : track.getAvailableMarkets())
            {
                result.append(market);
                if (track.getAvailableMarkets().indexOf(market) != track.getAvailableMarkets().size())
                {
                    result.append(", ");
                }
            }
            builder.append(result);
        }

        if (additionalFields.contains("previewurl"))
        {
            builder.append(separator);
            builder.append(track.getPreviewUrl());
        }

        if (additionalFields.contains("popularity"))
        {
            builder.append(separator);
            builder.append(track.getPopularity());
        }

        if (additionalFields.contains("href"))
        {
            builder.append(separator);
            builder.append(track.getHref());
        }

        if (additionalFields.contains("uri"))
        {
            builder.append(separator);
            builder.append(track.getUri());
        }

        if (additionalFields.contains("id"))
        {
            builder.append(separator);
            builder.append(track.getId());
        }

    }
}
