package com.example.myapplication.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.DashBoardActivity;
import com.example.myapplication.R;
import com.example.myapplication.constant.Constants;
import com.example.myapplication.retrofit.RegisterInterface;
import com.example.myapplication.retrofit.SearchInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private ListView resultListView;
    private CardView filter;
    private CardView filterButton;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;


    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.seachView);
        resultListView = view.findViewById(R.id.resultListView);
        filter = view.findViewById(R.id.filter);
        filterButton = view.findViewById(R.id.filterButton);
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        resultListView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    getSearchResult(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        filter.setVisibility(View.INVISIBLE);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filter.getVisibility()==View.VISIBLE){
                    filter.setVisibility(View.INVISIBLE);

                }else {
                    filter.setVisibility(View.VISIBLE);

                }
            }
        });
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = resultListView.getItemAtPosition(i).toString();
                Fragment searchFragment = new SearchResultsFragment();
                sendBundle(searchFragment,s);
                ((DashBoardActivity) requireActivity()).setFragment(searchFragment);
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    public void sendBundle(Fragment fragment, String keyword) {
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
    }

    private void getSearchResult(String keyword) throws JSONException {

        JSONObject paramObject = new JSONObject();
        paramObject.put("keyword", keyword);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.URL + "api/goCar/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        SearchInterface searchInterface = retrofit.create(SearchInterface.class);
        Call<Object> call = searchInterface.searchResult(paramObject.toString());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, @NonNull Response<Object> response) {
                Log.e("jhlkjl",String.valueOf(response.code()));
                if (response.code() == 200) {
                    assert response.body() != null;
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        JSONArray suggestion = new JSONArray(jsonObject.getString("data"));
                        Log.e("waaaaaaaa3",suggestion.toString());
                        listItems.clear();
                        for (int i = 0; i < suggestion.length(); i++) {
                            listItems.add(suggestion.get(i).toString());
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(getContext(), getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
                } else {
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(),  getString(R.string.an_error_occurred_please_login_again_later), Toast.LENGTH_LONG).show();
            }
        });
    }


}