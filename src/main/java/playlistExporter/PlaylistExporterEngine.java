package playlistExporter;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.HttpManager;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.SimplePlaylist;

public class PlaylistExporterEngine
{
    private String clientId;
    private String clientSecret;
    private String userId;

    public PlaylistExporterEngine(String clientId, String clientSecret, String userId)
    {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userId = userId;
    }

    public void export(PlaylistWritter playlistWritter)
    {
        try
        {
            Api.Builder builder = Api.builder();
            builder.clientId(clientId);
            builder.clientSecret(clientSecret);
            Api api = builder.build();

            UserPlaylistsRequest.Builder playlistBuilder = api.getPlaylistsForUser(userId);

            HttpManager httpManager = SpotifyHttpManager.builder().build();

            final ClientCredentialsGrantRequest clientCredentialsGrantRequest = api.clientCredentialsGrant().httpManager(httpManager).build();

            ClientCredentials clientCredentials = clientCredentialsGrantRequest.get();
            api.setAccessToken(clientCredentials.getAccessToken());

            UserPlaylistsRequest userPlaylistsRequest = playlistBuilder.accessToken(clientCredentials.getAccessToken()).build();

            Page<SimplePlaylist> simplePlaylistPage = userPlaylistsRequest.get();

            System.out.println(String.format("Getting playlists for user: '%s'", userId));

            for (SimplePlaylist simplePlaylist : simplePlaylistPage.getItems())
            {
                String playListId = simplePlaylist.getId();

                // api.getPlaylist(userId, playListId).build().
                PlaylistRequest playlistRequest = api.getPlaylist(simplePlaylist.getOwner().getId(), playListId).build();
                try
                {
                    System.out.println(String.format("Getting playlist with name '%s' owner: '%s'", simplePlaylist.getName(), simplePlaylist.getOwner().getId()));
                    Playlist playlist = playlistRequest.get();
                    playlistWritter.write(playlist);
                }
                catch (Exception e)
                {
                    //suppress
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
