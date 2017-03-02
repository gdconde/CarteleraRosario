package com.gdconde.cartelerarosario.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gdconde on 2/3/17.
 */

public class Cinema {

    public Cinema() {}

    public String name;

    public String address;

    public boolean has3d;

    public boolean has4d;

    public boolean parking;

    @SerializedName("online_purchase")
    public boolean onlinePurchase;

    public Price price;

    public static class Price {

        public Price() {}

        public int general;

        public int wednesday;

        public int kids;

        public Price3d price3d;

        public Price4d price4d;
    }

    public static class Price3d {

        public Price3d() {}

        public int general;

        public int wednesday;
    }

    public static class Price4d {

        public Price4d() {}

        public int general;

        public int wednesday;
    }


}
