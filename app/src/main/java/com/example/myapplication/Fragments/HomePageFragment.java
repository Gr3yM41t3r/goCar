package com.example.myapplication.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Vibrator;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DashBoardActivity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.Utility.ImageResizer;
import com.example.myapplication.R;
import com.example.myapplication.Utility.SaveSharedPreference;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.model.Favorites;
import com.example.myapplication.retrofit.AdvertInterface;
import com.example.myapplication.retrofit.CarInterface;
import com.example.myapplication.retrofit.FavoritesInterface;
import com.example.myapplication.retrofit.LoginInterface;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomePageFragment extends Fragment implements  NavigationView.OnNavigationItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match

    LinearLayout horizontal;
    ShimmerFrameLayout shimmerFrameLayout;
    LayoutInflater inflater2 ;
    ImageView hamburger;
    ImageView navigatetofavorites;
    DrawerLayout sideBar;
    TextView all;
    TextView bb;
    TextView berline;
    TextView citadine;
    TextView sport;
    CardView searchbar;
    NavigationView side_bar_view;
    Fragment carDescription = new CarDescription();
    ArrayList<TextView> filter= new ArrayList<>();
    Fragment favoritesFragment = new FavoritesFragment();
    Fragment addFragment = new AddAdvertFragment();


    public HomePageFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_home_page, container, false);
        inflater2 = getLayoutInflater();

        horizontal =view.findViewById(R.id.horizontal);
        hamburger =view.findViewById(R.id.side_bar_hamburger);
        navigatetofavorites =view.findViewById(R.id.navigatetofavorites);
        sideBar =view.findViewById(R.id.homePage);
        all =view.findViewById(R.id.all);
        bb =view.findViewById(R.id.breaktype);
        berline =view.findViewById(R.id.berline);
        citadine =view.findViewById(R.id.citadine);
        sport =view.findViewById(R.id.sport);
        searchbar =view.findViewById(R.id.search_bar);
        filter.add(all);
        filter.add(bb);
        filter.add(berline);
        filter.add(citadine);
        filter.add(sport);
        addFilerClickListner();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),sideBar,R.string.an_error_occurred_please_login_again_later,R.string.an_error_occurred_please_login_again_later);
        sideBar.addDrawerListener(toggle);
        toggle.syncState();
        side_bar_view=view.findViewById(R.id.side_bar_view);
        side_bar_view.setNavigationItemSelectedListener(this);
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sideBar.openDrawer(GravityCompat.START);
            }
        });

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment searchFragment = new SearchFragment();
                ((DashBoardActivity) requireActivity()).setFragment(searchFragment);
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    getCars();
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        navigatetofavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (SaveSharedPreference.isLogedIn(requireContext())) {
                        ((DashBoardActivity) requireActivity()).setFragment(favoritesFragment);
                    }else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }


            }
        });


        return view;
    }
    public void addFilerClickListner(){

        for (int i = 0; i < filter.size(); i++) {
            int finalI = i;
            filter.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Vibrator vibe = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

                    for (int j = 0; j < filter.size(); j++) {
                        filter.get(j).setTextSize(18);

                    }
                    vibe.vibrate(80);
                    filter.get(finalI).setTextSize(40);

                    if (filter.get(finalI).getText().toString().toLowerCase(Locale.ROOT).equals("all"))
                    {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getCars();
                                } catch (GeneralSecurityException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getCarsByType(filter.get(finalI).getText().toString().toLowerCase(Locale.ROOT));
                                } catch (GeneralSecurityException | IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }

                }
            });

        }
    }

    public void sendBundle(Fragment fragment, String keyword) {
        Bundle bundle = new Bundle();
        bundle.putString("idadvert", keyword);
        fragment.setArguments(bundle);
    }

    public void fillCarList(int id,String tp,String titletxt, String mdl, String odo, String fl, String citytxt, Bitmap bm,boolean isfavorite){

        View cardview= inflater2.inflate(R.layout.big_cardview_car,horizontal,false);
        TextView type = cardview.findViewById(R.id.type);
        TextView title = cardview.findViewById(R.id.title);
        TextView model = cardview.findViewById(R.id.model);
        TextView odometer = cardview.findViewById(R.id.odometer);
        TextView fuel = cardview.findViewById(R.id.fuel);
        TextView city = cardview.findViewById(R.id.city);
        ImageView mainImage = cardview.findViewById(R.id.mainImage);
        ImageView favorite = cardview.findViewById(R.id.favoritebutton);
        type.setText(tp);
        title.setText(titletxt);
        model.setText(mdl);
        odometer.setText(odo+ " Km");
        fuel.setText(fl);
        city.setText(citytxt);
        mainImage.setImageBitmap(bm);
        cardview.setId(id);
        if (isfavorite){
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_24_red));
            favorite.setSelected(true);
        }
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBundle(carDescription,String.valueOf(cardview.getId()));
                ((DashBoardActivity) requireActivity()).setFragment(carDescription);
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibe = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

                try {
                    if(!SaveSharedPreference.isLogedIn(getContext())){

                        new AlertDialog.Builder(getContext())
                                .setTitle("bloqué")
                                .setMessage("you need to login to use this function")
                                .setPositiveButton(getString(R.string.se_connecter), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                               // .setIcon(android.R.drawable.)
                                .show();
                        vibe.vibrate(80);
                        return;
                    }
                    if (!favorite.isSelected()){
                        vibe.vibrate(80);
                        favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_24_red));
                        favorite.setSelected(true);
                        try {
                            Favorites favorites = new Favorites(String.valueOf(cardview.getId()),SaveSharedPreference.getSessionId(getContext()));
                            new addFavorites().doInBackground(favorites);
                        } catch (GeneralSecurityException | IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_border_52));
                        vibe.vibrate(80);
                        favorite.setSelected(false);
                        Favorites favorites = null;
                        try {
                            favorites = new Favorites(String.valueOf(cardview.getId()), SaveSharedPreference.getSessionId(getContext()));
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new deleteFavorite().doInBackground(favorites);
                    }
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        horizontal.addView(cardview);
    }

    private class getCars extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                getCars();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class addFavorites extends AsyncTask<Favorites,Void,Void> {

        @Override
        protected Void doInBackground(Favorites... favorites) {
            addFavorite(favorites[0]);
            return null;

        }
    }

    private class deleteFavorite extends AsyncTask<Favorites,Void,Void> {

        @Override
        protected Void doInBackground(Favorites... favorites) {
            deleteFavorite(favorites[0]);
            return null;

        }
    }

    private void addFavorite(Favorites favorites){

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        FavoritesInterface login = retrofit.create(FavoritesInterface.class);
        Call<Object> call = login.addFavorites(favorites);
        ProgressDialog progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage(getString(R.string.please_wait));
        progressDoalog.setTitle(getString(R.string.connection___));
        progressDoalog.show();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    if (response.code() == 200) {
                        progressDoalog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                        progressDoalog.dismiss();
                    }
            }
            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
            }
        });

    }

    private void deleteFavorite(Favorites favorites){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        FavoritesInterface login = retrofit.create(FavoritesInterface.class);
        Call<Object> call = login.deleteFavorites(favorites);
        ProgressDialog progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage(getString(R.string.please_wait));
        progressDoalog.setTitle(getString(R.string.connection___));
        progressDoalog.show();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.code() == 200) {
                    progressDoalog.dismiss();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                    progressDoalog.dismiss();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
            }
        });

    }




    private void getCars() throws GeneralSecurityException, IOException {
        horizontal.removeAllViews();
        View shimmer= inflater2.inflate(R.layout.loading_carview_car_shimmer,horizontal,false);
        horizontal.addView(shimmer);
        ShimmerFrameLayout shimmerFrameLayout;
        shimmerFrameLayout =shimmer.findViewById(R.id.shimmer);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AdvertInterface car = retrofit.create(AdvertInterface.class);
        Call<Object> call = car.getAdverts(SaveSharedPreference.getSessionId(getContext()));
        shimmerFrameLayout.startShimmer();
        call.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                try {
                    if (response.code() == 200) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        assert response.body() != null;
                        JSONObject jsobj;
                        JSONObject jsonAdvert;

                        JSONArray jsonArray;
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONArray cars = new JSONArray(jsonObject.getString("data"));
                        for (int i = 0; i < cars.length(); i++) {
                            jsobj=new JSONObject(cars.getJSONObject(i).getString("car"));
                            jsonAdvert=new JSONObject(cars.getJSONObject(i).getString("advert"));
                            boolean isfavorite = Boolean.parseBoolean(cars.getJSONObject(i).getString("isfavorite"));
                            Log.e("jjjjjjjjjjjjjjjjjjj", Boolean.toString(isfavorite));
                            jsonArray= (JSONArray) cars.getJSONObject(i).get("photos");;
                            byte[] backToBytes = Base64.getDecoder().decode(jsonArray.get(0).toString());
                            Bitmap bitmap = BitmapFactory.decodeByteArray(backToBytes, 0, backToBytes.length);
                            fillCarList(jsonAdvert.getInt("id"),
                                    jsobj.getString("type"),
                                    jsonAdvert.getString("title"),
                                    jsobj.getString("brand")+" "+jsobj.getString("model"),
                                    jsobj.getString("odometer"),
                                    jsobj.getString("fuel"),
                                    jsonAdvert.getString("city"),
                                    bitmap,
                                    isfavorite
                            );
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.password_email_incorrect), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getCarsByType(String type) throws GeneralSecurityException, IOException, JSONException {
        horizontal.removeAllViews();
        View shimmer= inflater2.inflate(R.layout.loading_carview_car_shimmer,horizontal,false);
        horizontal.addView(shimmer);
        ShimmerFrameLayout shimmerFrameLayout;
        shimmerFrameLayout =shimmer.findViewById(R.id.shimmer);
        JSONObject paramObject = new JSONObject();
        paramObject.put("userid", SaveSharedPreference.getSessionId(getContext()));
        paramObject.put("type", type);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AdvertInterface car = retrofit.create(AdvertInterface.class);
        Call<Object> call = car.getadvertbytype(paramObject.toString());
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        call.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                try {
                    if (response.code() == 200) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        assert response.body() != null;
                        JSONObject jsobj;
                        JSONObject jsonAdvert;
                        JSONArray jsonArray;
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONArray cars = new JSONArray(jsonObject.getString("data"));
                        for (int i = 0; i < cars.length(); i++) {
                            jsobj=new JSONObject(cars.getJSONObject(i).getString("car"));
                            jsonAdvert=new JSONObject(cars.getJSONObject(i).getString("advert"));
                            boolean isfavorite = Boolean.parseBoolean(cars.getJSONObject(i).getString("isfavorite"));
                            jsonArray= (JSONArray) cars.getJSONObject(i).get("photos");;
                            byte[] backToBytes = Base64.getDecoder().decode(jsonArray.get(0).toString());
                            Bitmap bitmap = BitmapFactory.decodeByteArray(backToBytes, 0, backToBytes.length);
                            fillCarList(jsonAdvert.getInt("id"),
                                    jsobj.getString("type"),
                                    jsonAdvert.getString("title"),
                                    jsobj.getString("brand")+" "+jsobj.getString("model"),
                                    jsobj.getString("odometer"),
                                    jsobj.getString("fuel"),
                                    jsonAdvert.getString("city"),
                                    bitmap,
                                    isfavorite
                            );
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable throwable) {
                Toast.makeText(getActivity(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                try {
                    SaveSharedPreference.setSessionId(getActivity(), "","");
                    Toast.makeText(getActivity(), "deconnecté", Toast.LENGTH_LONG).show();

                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_account:
                try {
                    if (SaveSharedPreference.isLogedIn(getContext())) {
                        Fragment userFragment = new UserProfileFragment();
                        ((DashBoardActivity) requireActivity()).setFragment(userFragment);

                    } else {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_add:
                try {
                    if (SaveSharedPreference.isLogedIn(getContext())) {
                        ((DashBoardActivity) requireActivity()).setFragment(addFragment);

                    } else {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.nav_message:
                break;
            case R.id.nav_favourite:
                try {
                    if (SaveSharedPreference.isLogedIn(getContext())) {
                    Fragment favoritesFragment = new FavoritesFragment();
                    ((DashBoardActivity) requireActivity()).setFragment(favoritesFragment);}

                    else {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }
                break;

        }
        sideBar.closeDrawer(GravityCompat.START);
        return true;
    }
}