package com.wrapper.spotify;

import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimplePlaylist;

public class EntryPoint
{
    public static final String userId = "zdarklin";
    public static final String clientSecret = "a3567a6b58f64f82980df940680069b0";
    public static final String clientId = "4391e7421693481e813d357b56d65053";

    public static void main(String[] args)
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

            UserPlaylistsRequest userPlaylistsRequest = playlistBuilder.username("zdarklin").accessToken(clientCredentials.getAccessToken()).build();

            Page<SimplePlaylist> simplePlaylistPage = userPlaylistsRequest.get();

            int a = 1;


        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
