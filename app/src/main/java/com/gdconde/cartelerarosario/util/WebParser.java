package com.gdconde.cartelerarosario.util;

import com.gdconde.cartelerarosario.data.model.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by gdconde on 1/2/17.
 */

public final class WebParser {

    public static ArrayList<Movie> getElCairoMoviesTitles(String html) {
        Document document = Jsoup.parse(html);

        //Get movies URLs
        Elements moviesLinks = document.select("div.dia>ul>li>a[href]");

        //Create an array of movies links
        ArrayList<Movie> movies = new ArrayList<>();
        for (Element movieLink : moviesLinks) {
            String link = movieLink.attr("href");
            int indexOfSlash = link.lastIndexOf("/");
            String usefulLink = link.substring(indexOfSlash + 1);
            String title = movieLink.attr("title");
            if(title.contains("/")) {
                title = title.substring(title.lastIndexOf("/"));
            }
            Movie movie = new Movie();
            movie.title = title;
            movie.cinemas.add(Movie.EL_CAIRO);
            movie.link = usefulLink;
            movies.add(movie);
        }
        return movies;
    }

    public static ArrayList<String> getElCairoMoviesLinks(String html) {
        Document document = Jsoup.parse(html);

        //Get movies URLs
        Elements moviesLinks = document.select("div.dia>ul>li>a[href]");

        //Create an array of movies links
        ArrayList<String> moviesUrls = new ArrayList<>();
        for (Element movieLink : moviesLinks) {
            String link = movieLink.attr("href");
            int indexOfSlash = link.lastIndexOf("/");
            String usefulLink = link.substring(indexOfSlash + 1);
            moviesUrls.add(usefulLink);
        }
        return moviesUrls;
    }

    public static Movie getElCairoMovieData(String html) {
        Document document = Jsoup.parse(html);

        //Create new Movie object
        Movie movie = new Movie();
        movie.schedule = new ArrayList<>();

        //Get movie title
        String movieTitle = document.select("h1.fichatitle").text();
        movie.title = movieTitle.substring(movieTitle.lastIndexOf("/") + 1);

        //Get movie schedule
        String scheduled = document.select("div.details>ul>li").text();
        String month = document.select("div.rightcol>div.sidemenu>a").attr("title");
        movie.schedule.add(ElCairoDateCreator.stringToTimestamp(scheduled, month));

        //Get movie sinopsis
        movie.sinopsis = document.select("div.sinopsis>div.text").text();

        movie.cinemas.add(Movie.EL_CAIRO);

        return movie;
    }

    public static ArrayList<Movie> getShowcaseMoviesTitles(String html) {
        Document document = Jsoup.parse(html);

        Elements options = document.select("select#fs-movie-m>option");

        ArrayList<Movie> movies = new ArrayList<>();
        for(int i = 1; i < options.size(); i++) {
            Movie movie = new Movie();
            movie.title = options.get(i).text();
            movie.cinemas.add(Movie.SHOWCASE);
            movies.add(movie);
        }
        return movies;
    }

}

