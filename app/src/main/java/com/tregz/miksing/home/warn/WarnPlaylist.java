package com.tregz.miksing.home.warn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseWarning;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.home.list.ListArray;

import java.util.ArrayList;
import java.util.List;

public class WarnPlaylist extends BaseWarning {

    public final static String TAG = WarnPlaylist.class.getSimpleName();
    private final static String PLAYLIST = "playlist";

    public static WarnPlaylist newInstance(List<Song> songs) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(PLAYLIST, new ArrayList<Parcelable>(songs));
        WarnPlaylist fragment = new WarnPlaylist();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() != null && getActivity() != null) {
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext(), style);
            alert.setTitle(R.string.fetch_youtube_title);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_list, null);
            final ListView listing = view.findViewById(R.id.list_view);
            if (getArguments() != null) {
                List<Song> list = getArguments().getParcelableArrayList(PLAYLIST);
                listing.setAdapter(new ListArray(getContext(), new ArrayList<>(list)));
            }
            alert.setView(view);
            alert.setNegativeButton(R.string.cancel, null);
            alert.setPositiveButton(R.string.ok, (dialog, which) -> Log.d(TAG, "isList?"));
            return alert.create();
        }
        return super.onCreateDialog(savedInstanceState);
    }
}
