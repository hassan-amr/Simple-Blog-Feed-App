package com.hassanamr.simpleblogfeed.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hassanamr.simpleblogfeed.R;

import java.util.Random;

public class Util {

    public static int getRandomAvatar() {

        int[] avatars = {
                R.drawable.ic_user_avatar_1, R.drawable.ic_user_avatar_2,
                R.drawable.ic_user_avatar_3, R.drawable.ic_user_avatar_4,
                R.drawable.ic_user_avatar_5, R.drawable.ic_user_avatar_6,
                R.drawable.ic_user_avatar_7, R.drawable.ic_user_avatar_8,
                R.drawable.ic_user_avatar_9, R.drawable.ic_user_avatar_10,
        };

        return avatars[new Random().nextInt(avatars.length)];
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

}
