package com.hassanamr.simpleblogfeed.data.repository;

import com.hassanamr.simpleblogfeed.data.api.RetrofitClient;
import com.hassanamr.simpleblogfeed.data.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class UsersRepository {

    public Single<List<User>> getUsers() {
        return RetrofitClient.getInstance().getUsers();
    }

}