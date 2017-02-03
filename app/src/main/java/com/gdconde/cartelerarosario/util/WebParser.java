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

    public static ArrayList<String> parseElCairoMovies(String html) {
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

    public static Movie parseElCairoMovie(String html) {
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

        return movie;
    }

}

