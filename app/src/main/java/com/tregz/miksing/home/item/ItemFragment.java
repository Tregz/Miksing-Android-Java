package com.tregz.miksing.home.item;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.base.spin.SpinUtil;
import com.tregz.miksing.base.date.DateUtil;
import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataItem;
import com.tregz.miksing.data.song.Song;
import com.tregz.miksing.home.HomeView;
import com.tregz.miksing.home.list.ListCollection;

import java.util.Date;
import java.util.HashMap;

public class ItemFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
        Observer<Song> {
    //private final String TAG = ItemFragment.class.getSimpleName();
    private HomeView listener;

    private boolean dirty = false;
    private Date releasedAt = null;
    private int mix = 0;
    private String id;
    private String mixedBy;

    // UI references
    private CheckBox cbDirty;
    private EditText etArtist;
    private EditText etMixedBy;
    private EditText etReleaseDate;
    private EditText etTitle;
    private Spinner spMixVersion;

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
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cbDirty = view.findViewById(R.id.cb_dirty);
        // TODO cbDirty.setChecked();
        cbDirty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dirty = isChecked;
            }
        });

        etArtist = view.findViewById(R.id.et_artist);
        etArtist.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String sequence = ((EditText)view).getText().toString();
                if (!sequence.isEmpty()) {
                    // TODO validate
                }
            }
        });

        etMixedBy = view.findViewById(R.id.et_mixed_by);
        if (mixedBy != null) etMixedBy.setText(mixedBy);
        etMixedBy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String sequence = ((EditText)view).getText().toString();
                if (!sequence.isEmpty()) {
                    // TODO validate
                }
            }
        });

        etReleaseDate = view.findViewById(R.id.et_release_date);
        etReleaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        etTitle = view.findViewById(R.id.et_title);
        etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String sequence = ((EditText)view).getText().toString();
                if (!sequence.isEmpty()) {
                    // TODO validate
                }
            }
        });

        if (getContext() != null) {
            spMixVersion = view.findViewById(R.id.sp_mix_version);
            SpinUtil.setAdapter(getContext(), spMixVersion, R.array.array_song_mix_version);
            spMixVersion.setSelection(mix);
            spMixVersion.setOnItemSelectedListener(this);
        }

        // Update UI
        if (id != null) {
            LiveData<Song> song = DataReference.getInstance(getContext()).accessSong().query(id);
            song.observe(this, this);
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
            ItemDialog dialog = ItemDialog.newInstance(released);
            dialog.show(getActivity().getSupportFragmentManager(), ItemDialog.TAG);
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
        etReleaseDate.setText(DateUtil.dayOfYear(null, at));
        releasedAt = at;
    }

    public void clear() {
        if (cbDirty != null) cbDirty.setChecked(false);
        if (etArtist != null) etArtist.setText("");
        if (etMixedBy != null) etMixedBy.setText("");
        if (etReleaseDate != null) etReleaseDate.setText("");
        if (etTitle != null) etTitle.setText("");
        if (spMixVersion != null) spMixVersion.setSelection(0);
    }

    public void fill(DataItem item) {
        if (item instanceof Song) {
            etArtist.setText(((Song)item).getArtist());
            etReleaseDate.setText(DateUtil.dayOfYear(null, ((Song)item).getReleasedAt()));
            etTitle.setText(((Song)item).getTitle());
            mix = ((Song) item).getMix();
            mixedBy = ((Song) item).getMixedBy();
            if (cbDirty != null) cbDirty.setSelected(((Song) item).isClean());
        }
    }

    public void save() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("song").push();
        if (ref.getKey() != null) {
            Song item = new Song(ref.getKey(), new Date());
            if (etMixedBy != null) item.setMixedBy(etMixedBy.getText().toString());
            if (cbDirty != null) item.setClean(cbDirty.isChecked());
            if (spMixVersion != null)
                item.setMix(spMixVersion.getSelectedItemPosition());
            item.setArtist(etArtist.getText().toString());
            item.setReleasedAt(releasedAt);
            item.setTitle(etTitle.getText().toString());
            ListCollection.getInstance().add(item);
            ref.setValue(map(item)); // song will be inserted on local database onChildAdded
            listener.onSaved();
        }
    }

    private HashMap<String, Object> map(Song song) {
        HashMap<String, Object> map = new HashMap<>();
        if (song.getReleasedAt() != null) map.put(DataNotation.BD, song.getReleasedAt().getTime());
        if (song.getFeaturing() != null) map.put(DataNotation.FS, song.getFeaturing());
        if (song.getArtist() != null) map.put(DataNotation.MS, song.getArtist());
        if (song.getMixedBy() != null) map.put(DataNotation.LS, song.getMixedBy());
        if (song.getTitle() != null) map.put(DataNotation.NS, song.getTitle());
        map.put(DataNotation.WI, song.getWhat());
        return map;
    }
}
