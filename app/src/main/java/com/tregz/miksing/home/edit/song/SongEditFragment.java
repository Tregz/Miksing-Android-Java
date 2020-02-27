package com.tregz.miksing.home.edit.song;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.base.spin.SpinUtil;
import com.tregz.miksing.base.date.DateUtil;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataObject;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.databinding.FragmentSongBinding;
import com.tregz.miksing.home.HomeView;

import java.util.Date;

public class SongEditFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
        Observer<Song> {
    //private final String TAG = ItemFragment.class.getSimpleName();
    private HomeView listener;

    private boolean dirty = false;
    private Date releasedAt = null;
    private int mix = 0;
    private String id;
    private String mixedBy;

    // UI references
    private FragmentSongBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (HomeView) context;
        } catch (ClassCastException e) {
            String name = HomeView.class.getSimpleName();
            throw new ClassCastException(context.toString() + " must implement " + name);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) id = getArguments().getString("id");
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentSongBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO cbDirty.setChecked();
        binding.cbDirty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dirty = isChecked;
            }
        });

        binding.etArtist.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String sequence = ((EditText)view).getText().toString();
                if (!sequence.isEmpty()) {
                    // TODO validate
                }
            }
        });

        if (mixedBy != null) binding.etMixedBy.setText(mixedBy);
        binding.etMixedBy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String sequence = ((EditText)view).getText().toString();
                if (!sequence.isEmpty()) {
                    // TODO validate
                }
            }
        });

        binding.etReleaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        binding.etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String sequence = ((EditText)view).getText().toString();
                if (!sequence.isEmpty()) {
                    // TODO validate
                }
            }
        });

        if (getContext() != null) {
            SpinUtil.setAdapter(getContext(), binding.spMixVersion, R.array.array_song_mix_version);
            binding.spMixVersion.setSelection(mix);
            binding.spMixVersion.setOnItemSelectedListener(this);
        }

        // Update UI
        if (id != null) {
            LiveData<Song> song = DataReference.getInstance(getContext()).accessSong().liveWhereId(id);
            song.observe(getViewLifecycleOwner(), this);
        }
    }

    @Override
    public void onChanged(Song song) {
        fill(song);
    }

    private void dialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getActivity() != null) {
            Date released = releasedAt != null ? releasedAt : new Date();
            //ViewGroup group = ((HomeActivity)getActivity()).getViewGroup();
            SongEditDialog dialog = SongEditDialog.newInstance(released);
            dialog.show(getActivity().getSupportFragmentManager(), SongEditDialog.TAG);
        } else toast("Android version must Lollipop or higher");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mix = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    public void release(Date at) {
        binding.etReleaseDate.setText(DateUtil.dayOfYear(null, at));
        releasedAt = at;
    }

    public void clear() {
        binding.cbDirty.setChecked(false);
        binding.etArtist.setText("");
        binding.etMixedBy.setText("");
        binding.etReleaseDate.setText("");
        binding.etTitle.setText("");
        binding.spMixVersion.setSelection(0);
    }

    public void fill(DataObject item) {
        if (item instanceof Song) {
            binding.etArtist.setText(((Song)item).getArtist());
            binding.etReleaseDate.setText(DateUtil.dayOfYear(null, ((Song)item).getReleasedAt()));
            binding.etTitle.setText(((Song)item).getTitle());
            mix = ((Song) item).getMix();
            mixedBy = ((Song) item).getMixedBy();
            binding.cbDirty.setSelected(((Song) item).isClean());
        }
    }

    public void save() {
        /* TODO * DatabaseReference ref = FirebaseDatabase.getInstance().getReference("song").push();
        if (ref.getKey() != null) {
            Song item = new Song(ref.getKey(), new Date());
            if (etMixedBy != null) item.setMixedBy(etMixedBy.getText().toString());
            if (cbDirty != null) item.setClean(cbDirty.isChecked());
            if (spMixVersion != null)
                item.setMix(spMixVersion.getSelectedItemPosition());
            item.setArtist(etArtist.getText().toString());
            item.setReleasedAt(releasedAt);
            item.setTitle(etTitle.getText().toString());
            //ListCollection.getInstance().add(item);
            new SongInsert(getContext(), item);
            listener.onSaved();
        } */
    }
}
