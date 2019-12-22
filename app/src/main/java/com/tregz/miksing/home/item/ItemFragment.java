package com.tregz.miksing.home.item;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.tregz.miksing.core.date.DateUtil;
import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.item.Item;
import com.tregz.miksing.data.item.work.Work;
import com.tregz.miksing.data.item.work.song.Song;
import com.tregz.miksing.data.item.work.take.Take;
import com.tregz.miksing.home.HomeActivity;
import com.tregz.miksing.home.HomeView;
import com.tregz.miksing.home.list.ListCollection;

import java.util.Date;
import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ItemFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
        ItemView, Observer<Song> {
    private final String TAG = ItemFragment.class.getSimpleName();

    private boolean dirty = false;
    private Date releasedAt = null;
    private String mixedBy;
    private int mix = 0;
    private String id;

    // UI references
    private CheckBox cbDirty;
    private EditText etArtist;
    private EditText etMixedBy;
    private EditText etReleaseDate;
    private EditText etTitle;
    private Spinner spMixVersion;
    private Spinner spWorkType;

    private HomeView listener;

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
        return inflater.inflate(R.layout.fragment_work, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etArtist = view.findViewById(R.id.et_artist);
        etReleaseDate = view.findViewById(R.id.et_release_date);
        etTitle = view.findViewById(R.id.et_title);
        spWorkType = view.findViewById(R.id.sp_work_type);

        // UI listeners
        etArtist.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String sequence = ((EditText)view).getText().toString();
                if (!sequence.isEmpty()) {
                    // TODO validate
                }
            }
        });
        etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String sequence = ((EditText)view).getText().toString();
                if (!sequence.isEmpty()) {
                    // TODO validate
                }
            }
        });
        etReleaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        String[] types = new String[ItemType.values().length];
        for (ItemType item : ItemType.values()) types[item.ordinal()] = item.getType();
        setAdapter(spWorkType, types);

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
            ViewGroup group = ((HomeActivity)getActivity()).getViewGroup();
            ItemDialog dialog = new ItemDialog(group, this, released);
            ((HomeActivity)getActivity()).add(dialog);
        } else toast("Android version must Lollipop or higher");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.sp_work_type: onWorkTypeSelected(position);
                break;
            case R.id.sp_mix_version: mix = position;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    @Override
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

    public void fill(Item item) {
        if (item instanceof Work) {
            etArtist.setText(((Work)item).getArtist());
            etReleaseDate.setText(DateUtil.dayOfYear(null, ((Work)item).getReleasedAt()));
            etTitle.setText(((Work)item).getTitle());
            if (item instanceof Song) {
                mix = ((Song) item).getMix();
                mixedBy = ((Song) item).getMixedBy();
                spWorkType.setSelection(ItemType.SONG.ordinal());
                if (cbDirty != null) cbDirty.setSelected(((Song) item).isClean());
            } else if (item instanceof Take) {
                spWorkType.setSelection(ItemType.TAKE.ordinal());
            }
        }
    }

    public void save() {
        Work item = null;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("song").push();
        if (ref.getKey() != null) {
            ItemType type = ItemType.values()[spWorkType.getSelectedItemPosition()];
            if (type == ItemType.TAKE) {
                item = new Take(ref.getKey(), new Date());
                Log.d(TAG, "TAKE");
            }
            else if (type == ItemType.SONG) {
                item = new Song(ref.getKey(), new Date());
                if (etMixedBy != null) ((Song) item).setMixedBy(etMixedBy.getText().toString());
                if (cbDirty != null) ((Song) item).setClean(cbDirty.isChecked());
                if (spMixVersion != null)
                    ((Song) item).setMix(spMixVersion.getSelectedItemPosition());
            }
            if (item != null) {
                item.setArtist(etArtist.getText().toString());
                item.setReleasedAt(releasedAt);
                item.setTitle(etTitle.getText().toString());
                ListCollection.getInstance().add(item);
                if (item instanceof Song)
                    // song will be inserted on local database onChildAdded
                    ref.setValue(map((Song)item));
                listener.onSaved();
            }
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

    private void onWorkTypeSelected(int position) {
        boolean show = position == ItemType.SONG.ordinal();
        if (getView() != null) {
            getView().findViewById(R.id.song_details).setVisibility(show ? VISIBLE : GONE);
            if (show) {
                cbDirty = getView().findViewById(R.id.cb_dirty);
                etMixedBy = getView().findViewById(R.id.et_mixed_by);
                spMixVersion = getView().findViewById(R.id.sp_mix_version);

                // UI listeners
                cbDirty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dirty = isChecked;
                    }
                });
                etMixedBy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        String sequence = ((EditText)view).getText().toString();
                        if (!sequence.isEmpty()) {
                            // TODO validate
                        }
                    }
                });
                setAdapter(spMixVersion, R.array.array_song_mix_version);

                // Update UI
                spMixVersion.setSelection(mix);
                if (mixedBy != null) etMixedBy.setText(mixedBy);
                // TODO cbDirty.setChecked();
            }
        }
    }

    private void setAdapter(Spinner spinner, int array) {
        int layout = R.layout.spinner_label;
        if (getContext() != null)
            spinner.setAdapter(ArrayAdapter.createFromResource(getContext(), array, layout));
        spinner.setOnItemSelectedListener(this);
    }

    private void setAdapter(Spinner spinner, String[] array) {
        int layout = R.layout.spinner_label;
        if (getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), layout, array);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        spinner.setOnItemSelectedListener(this);
    }

    public static ItemFragment newInstance(String id) {
        ItemFragment fragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }
}
