package io.jpotts18.android_mvp.domain.repos;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.internal.Utils;
import io.jpotts18.android_mvp.domain.GithubService;
import io.jpotts18.android_mvp.domain.models.Repo;
import io.jpotts18.android_mvp.domain.models.User;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jpotts18 on 5/11/15.
 */
public class RepoListInteractor {

    private OnRepoInteractorFinishedListener listener;
//    private CompositeDisposable mCompositeDisposable;

    public RepoListInteractor(OnRepoInteractorFinishedListener listener) {
        this.listener = listener;
    }

    private Retrofit initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
//                .setEndpoint("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    private void handleResponse(Response<List<Repo>> response) {

        /*List<Repo> list = response.body();
        Collections.sort(list, new Comparator<Repo>() {
            @Override
            public int compare(Repo left, Repo right) {
                return (left.stargazers_count > right.stargazers_count) ? -1 : 1;
            }
        });

        listener.onNetworkSuccess(list, response);*/
    }

    private void handleError(Throwable throwable) {
        listener.onNetworkFailure(throwable);
    }

    public void loadRecentCommits(String username) {
        Retrofit retrofit = initRetrofit();
        GithubService githubService = retrofit.create(GithubService.class);
//        mCompositeDisposable = new CompositeDisposable();

//        mCompositeDisposable.add(githubService.listRepos(username)

        getObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(new Function<List<Repo>, List<User>>() {

                    @Override
                    public List<User> apply(List<Repo> repoList) throws Exception {
                        return convertRepoListToUserList(repoList);
                    }
                })
                .subscribe(getObserver());

//        Call<List<Repo>> call= githubService.listRepos(username);
//        call.enqueue(this);
    }

    private List<User> convertRepoListToUserList(List<Repo> repoList) {

        List<User> userList = new ArrayList<>();

        for (Repo repo : repoList) {
            User user = new User();
            user.id = repo.id;
            user.name = repo.name;
            userList.add(user);
        }

        return userList;
    }

    public void clearSubscriptions() {
//        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
//            mCompositeDisposable.dispose();
//        }
    }

    private Observable<List<Repo>> getObservable() {
        return Observable.create(new ObservableOnSubscribe<List<Repo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Repo>> e) throws Exception {
                if (!e.isDisposed()) {
//                    e.onNext(new ArrayList<Repo>());
//                    e.onComplete();
                }
            }
        });
    }

    public Observer<List<User>> getObserver() {
        return new Observer<List<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<User> value) {

                Collections.sort(value, new Comparator<User>() {
                    @Override
                    public int compare(User left, User right) {
                        return (left.id > right.id) ? -1 : 1;
                    }
                });

                listener.onNetworkSuccess(value, null);

            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }

            @Override
            public void onComplete() {

            }
        };
    }

   /* @Override
    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {

        List<Repo>  list = response.body();
        Collections.sort(list, new Comparator<Repo>() {
            @Override
            public int compare(Repo left, Repo right) {
                return (left.stargazers_count > right.stargazers_count) ? -1 : 1;
            }
        });

        listener.onNetworkSuccess(list, response);
    }

    @Override
    public void onFailure(Call<List<Repo>> call, Throwable t) {
        listener.onNetworkFailure(t);
    }*/

   /* @Override
    public void success(List<Repo> list, Response response) {
        Collections.sort(list, new Comparator<Repo>() {
            @Override
            public int compare(Repo left, Repo right) {
                return (left.stars > right.stars) ? -1 : 1;
            }
        });

        listener.onNetworkSuccess(list, response);
    }

    @Override
    public void failure(RetrofitError error) {
        listener.onNetworkFailure(error);
    }*/
}
