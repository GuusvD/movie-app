package com.teamc11.MovieApp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpUriRequest;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void testGetAllMoviesEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/discover/movie?api_key=b12f4ce69c08c0214bf447763d5cf7ec&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_watch_monetization_types=flatrate");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetAllMoviesEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/discover/movie?api_key=b12f4ce69c08c0214bf447763d5cf7easda32c&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_watch_monetization_types=flatrate");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetMovieDetailsEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/movie/656663?api_key=b12f4ce69c08c0214bf447763d5cf7ec&language=en-US");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetMovieDetailsEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/movie/656663?api_key=b12f4ce69c08c0214bf447763d5cf23427ec&language=en-US");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetMovieTrailerEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/movie/656663/videos?api_key=b12f4ce69c08c0214bf447763d5cf7ec&language=en-US");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetMovieTrailerEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/movie/656663/videos?api_key=b12f4ce69c08c0214bf447763d5cf7fas1ec&language=en-US");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetMovieReviewsEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/movie/656663/reviews?api_key=b12f4ce69c08c0214bf447763d5cf7ec&language=en-US&page=1");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetMovieReviewsEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/movie/656663/reviews?api_key=b12f4ce69c08c0214bf447763d5cf7edasdas231c&language=en-US&page=1");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetMovieRatingEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/account/12168590/rated/movies?api_key=b12f4ce69c08c0214bf447763d5cf7ec&language=en-US&session_id=c9c797511e3c68da1b89611fa3a70c08ffa38121&sort_by=created_at.asc&page=1");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetMovieRatingEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/account/12168590/rated/movies?api_key=b12f4ce69c08c0214bf4477adasd3263d5cf7ec&language=en-US&session_id=c9c797511e3c68da1b89611fa3a70c08ffa38121&sort_by=created_at.asc&page=1");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetAllGenresEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/genre/movie/list?api_key=b12f4ce69c08c0214bf447763d5cf7ec&language=en-US");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetAllGenresEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/genre/movie/list?api_key=b12f4ce69c08c0214bf447763d5cf7easda32c&language=en-US");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetRequestTokenEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/authentication/token/new?api_key=b12f4ce69c08c0214bf447763d5cf7ec");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetRequestTokenEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/authentication/token/new?api_key=b12f4ce69c08c0214bf447763d5cf7easda32c");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetUserEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/account?api_key=b12f4ce69c08c0214bf447763d5cf7ec&session_id=c9c797511e3c68da1b89611fa3a70c08ffa38121");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetUserEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/account?api_key=b12f4ce69c08c0214bf447763d5cf7easda32c&session_id=c9c797511e3c68da1b89611fa3a70c08ffa38121");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetCreatedListEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/list/2000?api_key=b12f4ce69c08c0214bf447763d5cf7ec&language=en-US");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetCreatedListEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/list/2000?api_key=b12f4ce69c08c0214bf447763d5cf7easda32c&language=en-US");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetAllCreatedListsEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/account/12168590/lists?api_key=b12f4ce69c08c0214bf447763d5cf7ec&language=en-US&session_id=c9c797511e3c68da1b89611fa3a70c08ffa38121&page=1");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetAllCreatedListsEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/account/12168590/lists?api_key=b12f4ce69c08c0214bf447dsasa763d5cf7ec&language=en-US&session_id=c9c797511e3c68da1b89611fa3a70c08ffa38121&page=1");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetSearchResultEnsures200() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/search/movie?api_key=b12f4ce69c08c0214bf447763d5cf7ec&language=en-US&query=harry&page=1&include_adult=false");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetSearchResultEnsures401() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet( "https://api.themoviedb.org/3/search/movie?api_key=b12f4ce69c08c0214bdasdaf447763d5cf7ec&language=en-US&query=harry&page=1&include_adult=false");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(401, httpResponse.getStatusLine().getStatusCode());
    }
}