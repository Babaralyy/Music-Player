package com.example.azikeamusic.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.azikeamusic.R;
import com.example.azikeamusic.adapter.AlbumDetailAdapter;
import com.example.azikeamusic.databinding.FragmentAlbumDetailBinding;
import com.example.azikeamusic.utils.Constant;


public class AlbumDetailFragment extends Fragment {


    private FragmentAlbumDetailBinding mBinding;
    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAlbumDetailBinding.inflate(inflater);
        inIt();
        return mBinding.getRoot();
    }

    private void inIt() {


    }
}