package io.jpotts18.android_mvp.domain.repos;

import java.util.List;

import io.jpotts18.android_mvp.domain.models.Repo;
import io.jpotts18.android_mvp.domain.models.User;
import retrofit2.Response;
//import retrofit.RetrofitError;
//import retrofit.client.Response;

/**
 * Created by jpotts18 on 5/12/15.
 */
public interface OnRepoInteractorFinishedListener {
    void onNetworkSuccess(List<User> list, Response response);
    void onNetworkFailure(Throwable throwable);
}
