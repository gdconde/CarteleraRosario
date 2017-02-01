package com.hitherejoe.mvpboilerplate.util;

import com.hitherejoe.mvpboilerplate.data.model.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by gdconde on 1/2/17.
 */

public final class WebParser {

    public static ArrayList<Movie> parseElCairoMovies(String html) {
        Document document = Jsoup.parse(html);

        //Get month and year
        String url = document.select("meta[property=og:url]").attr("content");
        String date = url.split("/")[5];
        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]) - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        //Get movies titles
        Elements moviesTitles = document.select("div.dia>ul>li>a[href]");

        //Create an array of movies titles and dates. To get the time I need to get another website
        ArrayList<Movie> movies = new ArrayList<>();
        for (Element title: moviesTitles) {
            //Get day
            int day = Integer.parseInt(title.parent().parent().firstElementSibling().text());
            calendar.set(Calendar.DAY_OF_MONTH, day);

            //Create new empty movie
            Movie movie = new Movie();
            movie.schedule = new ArrayList<>();

            //Populate movie element with data
            movie.title = title.text();
            movie.schedule.add(calendar.getTimeInMillis());

            //Add movie to array
            movies.add(movie);
        }
        return movies;
    }

}
