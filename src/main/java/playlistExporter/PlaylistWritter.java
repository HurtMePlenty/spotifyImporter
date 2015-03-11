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
                Path path = Paths.get(outputPath, playlist.getName() + ".csv");
                File file = new File(path.toString());
                if (!file.exists())
                {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
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
    }
}
