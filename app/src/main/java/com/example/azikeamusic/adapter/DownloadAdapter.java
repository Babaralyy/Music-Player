package com.example.azikeamusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.azikeamusic.callbacks.DownloadCallback;
import com.example.azikeamusic.databinding.LayDownloadBinding;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    private Context context;
    private List<String> downloadList;
    private DownloadCallback downloadCallback;

    public DownloadAdapter(Context context, List<String> downloadList, DownloadCallback downloadCallback) {
        this.context = context;
        this.downloadList = downloadList;
        this.downloadCallback = downloadCallback;
    }

    @NonNull
    @Override
    public DownloadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayDownloadBinding mBinding = LayDownloadBinding.inflate(inflater, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String url = downloadList.get(position);


        holder.mBinding.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadCallback.onDownloadClick(position, url, holder.mBinding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LayDownloadBinding mBinding;

        public ViewHolder(LayDownloadBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
