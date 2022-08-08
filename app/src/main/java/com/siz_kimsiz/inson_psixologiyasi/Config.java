package com.siz_kimsiz.inson_psixologiyasi;

public class Config {

    //this is the path of your admin panel url

    public static final boolean ENABLE_ADMOB_BANNER_ADS = true;
    public static final boolean ENABLE_ADMOB_INTERSTITIAL_ADS = true;
    public static final int ADMOB_INTERSTITIAL_ADS_INTERVAL = 2;

    //set true if you want to enable finger swipe left or right to go to previous or next story
    public static final boolean ENABLE_VIEW_PAGER = true;

    //set true if you want to enable RTL (Right To Left) mode, e.g : Arabic Language
    public static final boolean ENABLE_RTL_MODE = false;

    //zoom function in the story detail
    public static final boolean ENABLE_ZOOM_CONTROL = false;

    // unity ad config
    public static String GameID = "3950341";
    public static boolean TEST_MODE = false;
    public static boolean ENABLED_LOAD = true;
    public static String bannerAdPlacement = "banner";
    public static String interstitialAdPlacement = "interstitialVideo";
    public static String REWARD_VIDEO_AD = "rewardedVideo";

}
