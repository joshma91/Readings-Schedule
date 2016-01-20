package com.joshdevs.josh.readingsschedulev2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joshdevs.josh.readingsschedulev2.Models.ImportModel;
import com.joshdevs.josh.readingsschedulev2.Models.ReadingDateModel;
import com.joshdevs.josh.readingsschedulev2.Models.ReadingModel;
import com.joshdevs.josh.readingsschedulev2.Models.RowModel;
import com.joshdevs.josh.readingsschedulev2.Models.SubHeaderModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Map;

public class ByDateFragment extends android.support.v4.app.Fragment implements BaseViewHolder.OnClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";

    public ArrayList<RowModel> fullFormArray;
    private OnFragmentInteractionListener mListener;
    public RecyclerView recyclerView;
    protected BaseFormAdapter adapter;
    SharedPreferences mPrefs;
    long currentDate = GregorianCalendar.getInstance().getTimeInMillis();
    private int adapterPosition;
    private int mPage;
    TextView textView;
    ImportModel importModel;

    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static ByDateFragment newInstance(ImportModel model) {
        Bundle args = new Bundle();
        args.putParcelable("import", model);
        ByDateFragment fragment = new ByDateFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        mListener = null;

        if(fullFormArray != null) {
            saveToSharedPrefs();
        }

    }


    private void saveToSharedPrefs() {
        SharedPreferences.Editor editor = mPrefs.edit();

        for (RowModel row : fullFormArray) {

            if (row instanceof SubHeaderModel) {
                for (ReadingModel readingModel : ((SubHeaderModel) row).getReadingModelsArray()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(readingModel);
                    editor.putString(readingModel.getCourseName() + readingModel.getTitle(), json);
                    editor.apply();
                }
            }
        }
    }

    private void updateAdapterOnClick(BaseViewHolder holder) {

                final int selectedPosition = holder.getAdapterPosition();
                holder.itemView.post(new Runnable() {
                    @Override
                    public void run() {
                recyclerView.getAdapter().notifyItemChanged(selectedPosition);
            }
        });
    }

    @Override
    public void onLoseFocus(BaseViewHolder holder) {

    }

    @Override
    public void onGainFocus(BaseViewHolder holder) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().getParcelable("import") != null) {
            Parcelable model = getArguments().getParcelable("import");
            importModel = ImportModel.getInstance();
            importModel = (ImportModel) model;
        }

        mPrefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if(importModel.getCourses().size() > 0) {
            view = inflater.inflate(R.layout.recycler_base, container, false);
            setupRecycler(view);
        } else {
            view = inflater.inflate(R.layout.button_page, container, false);
            Button button = (Button) view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.importFromExcel();
                }
            });
            Button instructionButton = (Button) view.findViewById(R.id.instruction_button);
            instructionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.launchInstructionActivity();
                }
            });
        }

        return view;
    }

    private void setupRecycler(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (importModel.getDates().size() != 0) {
            fullFormArray = initializeFullFormArray();

            adapter = new BaseFormAdapter(fullFormArray, this, this.getActivity());
            recyclerView.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    public ArrayList<RowModel> initializeFullFormArray() {
        ArrayList<Long> dateArray = createDateArray();
        long startingDate = 0;
        long endingDate = 9999999999999L;
        for (Long date : dateArray) {
            if (date > currentDate) {
                startingDate = date;
                break;
            }
        }
        int count = 0;
        for (Long date : dateArray) {
            if (count >= 5) {
                endingDate = date;
            }
            if (date > currentDate) {
                count++;
            }
        }
        int startingIndex = dateArray.indexOf(startingDate);

        fullFormArray = new ArrayList<>();
        for (int i = -1; i < 10; i++) {
            int targetIndex = startingIndex + i;
            if (dateArray.size() <= targetIndex || targetIndex == -1) {
                break;
            }
            long dateInMillis = dateArray.get(targetIndex);

            boolean first = (i==0);

            SubHeaderModel dateHeaderModel = insertDateHeaderModel(dateInMillis, first);
            fullFormArray.add(dateHeaderModel);

        }
        return fullFormArray;
    }

    private ArrayList<Long> createDateArray() {
        ArrayList<Long> dateArrayList = new ArrayList<>();

        for (ReadingDateModel readingDateModel : importModel.getDates()) {
            dateArrayList.add(readingDateModel.getReadingDate());
        }

        dateArrayList = new ArrayList<Long>(new LinkedHashSet<Long>(dateArrayList));

        Collections.sort(dateArrayList);
        return dateArrayList;
    }

    private SubHeaderModel insertDateHeaderModel(long dateInMillis, boolean first) {
        SubHeaderModel dateHeaderModel = new SubHeaderModel();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateInMillis);

        if(first){
            dateHeaderModel.setViewState(RowModel.ViewState.SHOWN);
        }

        String dayOfWeek = new DateFormatSymbols().getShortWeekdays()[c.get(Calendar.DAY_OF_WEEK)];
        String month = new DateFormatSymbols().getMonths()[c.get(Calendar.MONTH) + 1];


        String dateString = dayOfWeek + " " + month + " " + c.get(Calendar.DAY_OF_MONTH) ;
        dateHeaderModel.setTitle(dateString);

        ArrayList<ReadingModel> readingArrayList = new ArrayList<>();
        ArrayList<String> readingStringsList = new ArrayList<>();

        for (ReadingDateModel readingDateModel : importModel.getDates()) {
            if (readingDateModel.getReadingDate() == dateInMillis) {
                ReadingModel readingModel = initializeReadingModel(readingDateModel.getCourseName(), readingDateModel.getReadingName());
                if (readingModel != null) {
                    readingArrayList.add(readingModel);
                }
            }
        }

        dateHeaderModel.setReadingModelsArray(readingArrayList);
        return dateHeaderModel;
    }

    protected ReadingModel initializeReadingModel(String course, String title) {
        ReadingModel readingModel = new ReadingModel();

        Map<String, ?> keys = mPrefs.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            //There's redundancy in checking Course name so that duplicate reading names don't break
            if (entry.getKey() != null && entry.getKey().equals(course + title)) {
                Gson gson = new Gson();
                String json = mPrefs.getString(entry.getKey(), "");

                readingModel = gson.fromJson(json, ReadingModel.class);

            }
        }
        readingModel.setCourseName(course);
        readingModel.setTitle(title);
        readingModel.setDate(true);
        return readingModel;
    }


    @Override
    public void onClick(BaseViewHolder holder, View view) {
        int id = view.getId();
        adapterPosition = holder.getAdapterPosition();

        if (id == R.id.reading_layout) {
            SubHeaderModel subHeaderModel = ((SubHeaderViewHolder) holder).getItem();
            if (subHeaderModel.getViewState() == RowModel.ViewState.SHOWN) {
                subHeaderModel.setViewState(RowModel.ViewState.HIDDEN);
                updateAdapterOnClick(holder);
            } else {
                subHeaderModel.setViewState(RowModel.ViewState.SHOWN);
                updateAdapterOnClick(holder);
            }
            if (adapterPosition == fullFormArray.size() - 1) {
                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(adapterPosition, 0);
            }
        }

        if (id == R.id.reading_button) {
            CheckBox checkBox = ((CheckBox) view.findViewById(R.id.checkbox));
            ReadingModel readingModel = new ReadingModel();

            ArrayList<ReadingModel> readingsList = ((SubHeaderModel) holder.getItem()).getReadingModelsArray();
            int size = readingsList.size();

            String TVText = ((TextView) view.findViewById(R.id.reading_title)).getText().toString();
            String[] array = TVText.split(": ", 2);
            String compareText;
            if(array.length == 1){
                compareText = TVText;
            } else {
                compareText = array[1];
            }

            for (int i = 0; i < size; i++) {
                if (compareText.equals(readingsList.get(i).getTitle())) {
                    if (readingsList.get(i).getTitle().length() == 0) {
                        return;
                    }
                    readingModel = readingsList.get(i);
                }
            }

            setChecked(checkBox, readingModel);
        }
    }




    private void setChecked(CheckBox checkBox, ReadingModel readingModel) {
        if (checkBox.isChecked()) {

            readingModel.setChecked(false);
            checkBox.setChecked(false);
            updateFirstFragment(readingModel, false);
        } else {
            readingModel.setChecked(true);
            checkBox.setChecked(true);
            updateFirstFragment(readingModel, true);
        }
    }


    private void updateFirstFragment(ReadingModel readingModel, boolean checked) {
        //Way to get TagName which generated by FragmentPagerAdapter
        String tagName = "android:switcher:" + R.id.viewpager + ":" + 0; // Your pager name & tab no of Second Fragment
        //Get SecondFragment object from FirstFragment
        ByCourseFragment f2 = (ByCourseFragment) getActivity().getSupportFragmentManager().findFragmentByTag(tagName);
        //Then call your wish method from SecondFragment to update appropriate list
        f2.updateList(readingModel, checked);
    }

    public void updateList(ReadingModel readingModel, boolean checked){

        int size = fullFormArray.size();
        for(int i = 0; i<size; i++){
            RowModel row = fullFormArray.get(i);
            if(row instanceof SubHeaderModel){
                for (ReadingModel model : ((SubHeaderModel) row).getReadingModelsArray()){
                    if (model.getTitle().equals(readingModel.getTitle()) && model.getCourseName().equals(readingModel.getCourseName())) {
                        model.setChecked(checked);
                        recyclerView.getAdapter().notifyItemChanged(i);
                    }
                }
            }
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}