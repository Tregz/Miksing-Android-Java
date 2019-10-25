package com.tregz.miksing.home.work;

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

import com.tregz.miksing.R;
import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.core.date.DateUtil;
import com.tregz.miksing.data.work.Work;
import com.tregz.miksing.data.work.song.Song;
import com.tregz.miksing.data.work.take.Take;
import com.tregz.miksing.home.HomeActivity;

import java.util.Date;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WorkFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
        WorkView {

    private boolean dirty = false;
    private Date releasedAt = null;
    private int mix = 0;

    // UI ref
    private CheckBox cbDirty;
    private EditText etArtist;
    private EditText etMixedBy;
    private EditText etReleaseDate;
    private EditText etTitle;
    private Spinner spMixVersion;
    private Spinner spWorkType;

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
        setAdapter(spWorkType, R.array.array_work_type);

        // Update UI
        // TODO et_artist.setText(artist)
        // if (releasedAt != null) etReleaseDate.setText(DateUtil.dayOfYear(null, releasedAt));
        // TODO et_title.setText(title)
        // TODO sp selected
    }

    private void dialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Date released = releasedAt != null ? releasedAt : new Date();
            ViewGroup group = ((HomeActivity)getActivity()).getViewGroup();
            WorkDialog dialog = new WorkDialog(group, this, released);
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

    public void save() {
        Work work = null;
        WorkType type = WorkType.values()[spWorkType.getSelectedItemPosition()];
        if (type == WorkType.TAKE) {
            work = new Take(new Date());
            Log.d(TAG, "TAKE");
        }
        else if (type == WorkType.SONG) {
            work = new Song(new Date());
            if (etMixedBy != null) ((Song) work).setMixedBy(etMixedBy.getText().toString());
            if (cbDirty != null) ((Song) work).setDirty(cbDirty.isChecked());
            if (spMixVersion != null) ((Song) work).setMix(spMixVersion.getSelectedItemPosition());
        }
        if (work != null) {
            work.setArtist(etArtist.getText().toString());
            work.setReleasedAt(releasedAt);
            work.setTitle(etTitle.getText().toString());
            WorkCollection.getInstance().add(work);
            listener.saved();
        }
    }

    private void onWorkTypeSelected(int position) {
        boolean show = position == WorkType.SONG.ordinal();
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
                setAdapter(spMixVersion, R.array.array_work_type);

                // Update UI
                // TODO cbDirty.setChecked();
                // et
                // sp
            }
        }
    }

    private void setAdapter(Spinner spinner, int array) {
        int layout = R.layout.spinner_label;
        if (getContext() != null) {
            spinner.setAdapter(ArrayAdapter.createFromResource(getContext(), array, layout));
        }
        spinner.setOnItemSelectedListener(this);
    }

    static {
        TAG = WorkFragment.class.getSimpleName();
    }
}
