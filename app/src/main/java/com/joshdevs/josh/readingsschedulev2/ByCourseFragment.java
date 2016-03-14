package com.joshdevs.josh.readingsschedulev2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joshdevs.josh.readingsschedulev2.Models.CourseModel;
import com.joshdevs.josh.readingsschedulev2.Models.HeaderModel;
import com.joshdevs.josh.readingsschedulev2.Models.ImportModel;
import com.joshdevs.josh.readingsschedulev2.Models.ReadingEntryModel;
import com.joshdevs.josh.readingsschedulev2.Models.ReadingModel;
import com.joshdevs.josh.readingsschedulev2.Models.RowModel;
import com.joshdevs.josh.readingsschedulev2.Models.SubHeaderModel;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ByCourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ByCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ByCourseFragment extends android.support.v4.app.Fragment implements BaseViewHolder.OnClickListener {
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


    public static ByCourseFragment newInstance(ImportModel model) {
        Bundle args = new Bundle();
        args.putParcelable("import", model);
        ByCourseFragment fragment = new ByCourseFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        mListener = null;
        if (fullFormArray != null) {
            saveToSharedPrefs();
        }
    }

    private void saveToSharedPrefs() {
        SharedPreferences.Editor editor = mPrefs.edit();

        String currentCourse = "";

        for (RowModel row : fullFormArray) {
            if (row instanceof HeaderModel) {
                currentCourse = row.getTitle();
            }
            if (row instanceof SubHeaderModel) {
                for (ReadingModel readingModel : ((SubHeaderModel) row).getReadingModelsArray()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(readingModel);
                    editor.putString(currentCourse + readingModel.getTitle(), json);
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
        mPage = getArguments().getInt(ARG_PAGE);

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
        if (importModel.getCourses().size() > 0) {
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
        fullFormArray = initializeFullFormArray();
        adapter = new BaseFormAdapter(fullFormArray, this, this.getActivity());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    public ArrayList<RowModel> initializeFullFormArray() {
        fullFormArray = new ArrayList<>();

        for (CourseModel course : importModel.getCourses()) {


            //Header Model
            HeaderModel headerModel = new HeaderModel();
            headerModel.setTitle(course.getCourseTitle());
            fullFormArray.add(headerModel);

            if (course.getReadingEntryModelArray().get(0).getReadingDateInMillis() != 0) {

                //Current Readings Model
                SubHeaderModel currentReadings = insertReadings(course, SubHeadingType.CURRENT);
                currentReadings.setViewState(RowModel.ViewState.SHOWN);
                fullFormArray.add(currentReadings);

                //Future Readings Model
                SubHeaderModel futureReadings = insertReadings(course, SubHeadingType.FUTURE);
                fullFormArray.add(futureReadings);

                //Completed Readings Model
                SubHeaderModel completedReadings = insertReadings(course, SubHeadingType.COMPLETED);
                fullFormArray.add(completedReadings);

                //Missed Readings Model
                SubHeaderModel missedReadings = insertReadings(course, SubHeadingType.MISSED);
                fullFormArray.add(missedReadings);
            } else {
                //NO DATE incompleted
                SubHeaderModel incompletedReadings = insertReadings(course, SubHeadingType.NODATE_INCOMPLETED);
                fullFormArray.add(incompletedReadings);

                //NO DATE completed
                SubHeaderModel completedReadings = insertReadings(course, SubHeadingType.NODATE_COMPLETED);
                fullFormArray.add(completedReadings);

            }

        }

        return fullFormArray;

    }

    private SubHeaderModel insertReadings(CourseModel course, SubHeadingType type) {
        SubHeaderModel subHeaderModel = new SubHeaderModel();

        ArrayList<ReadingModel> readingArrayList = new ArrayList<>();
        ArrayList<String> readingStringsList = new ArrayList<>();


        long nextClass = 0;
        long currentReadingDate = 0;
        for (ReadingEntryModel reading : course.getReadingEntryModelArray()) {

            currentReadingDate = reading.getReadingDateInMillis();
            long nextReadingDate = reading.getNextReadingDate();

            if (currentReadingDate > currentDate && currentReadingDate <= nextReadingDate) {
                nextClass = nextReadingDate;
                break;
            } else if (nextReadingDate == 0) {
                nextClass = currentReadingDate+1;
                break;
            }
        }

        for (ReadingEntryModel reading : course.getReadingEntryModelArray()) {
            long readingDate = reading.getReadingDateInMillis();
            long nextReadingDate = reading.getNextReadingDate();

            switch (type) {
                case CURRENT:
                    if (readingDate >= currentDate && readingDate < nextClass) {
                        readingStringsList.add(reading.getReadingName());
                    }
                    break;
                case FUTURE:
                    if (readingDate > currentDate && readingDate >= nextClass) {
                        readingStringsList.add(reading.getReadingName());
                    }
                    break;
                case COMPLETED:
                case MISSED:
                    if (readingDate < currentDate) {
                        readingStringsList.add(reading.getReadingName());
                    }
                    break;
                case NODATE_COMPLETED:
                case NODATE_INCOMPLETED:
                    readingStringsList.add(reading.getReadingName());
                    break;
            }
        }

        for (String reading : readingStringsList) {
            ReadingModel readingModel = initializeReadingModel(course, reading, type);
            if (readingModel != null) {
                readingArrayList.add(readingModel);
            }
        }

        String currentHeader;
        switch (type) {
            case CURRENT:
            case NODATE_INCOMPLETED:
                currentHeader = getString(R.string.current_readings) + " (" + readingArrayList.size() + ")";
                break;
            case FUTURE:
                currentHeader = getString(R.string.future_readings) + " (" + readingArrayList.size() + ")";
                break;
            case COMPLETED:
            case NODATE_COMPLETED:
                currentHeader = getString(R.string.completed_readings) + " (" + readingArrayList.size() + ")";
                break;
            case MISSED:
            default:
                currentHeader = getString(R.string.missed_readings) + " (" + readingArrayList.size() + ")";
                break;
        }

        subHeaderModel.setTitle(currentHeader);
        subHeaderModel.setReadingModelsArray(readingArrayList);

        return subHeaderModel;
    }

    protected ReadingModel initializeReadingModel(CourseModel course, String title, SubHeadingType type) {
        ReadingModel readingModel = new ReadingModel();

        Map<String, ?> keys = mPrefs.getAll();

        if (title == null || (keys.entrySet().size() < 2) && (type == SubHeadingType.COMPLETED || type == SubHeadingType.NODATE_COMPLETED)) {
            return null;
        }

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            //There's redundancy in checking Course name so that duplicate reading names don't break
            if (entry.getKey() != null && entry.getKey().equals(course.getCourseTitle() + title)) {
                Gson gson = new Gson();
                String json = mPrefs.getString(entry.getKey(), "");

                readingModel = gson.fromJson(json, ReadingModel.class);

                if (type == SubHeadingType.MISSED || type == SubHeadingType.NODATE_INCOMPLETED) {
                    if (readingModel.isChecked()) {
                        return null;
                    }
                } else if (type == SubHeadingType.COMPLETED || type == SubHeadingType.NODATE_COMPLETED) {
                    if (!readingModel.isChecked()) {
                        return null;
                    }
                } else {
                    readingModel.setDate(false);
                    return readingModel;
                }

            }
        }
        readingModel.setCourseName(course.getCourseTitle());
        readingModel.setTitle(title);
        readingModel.setDate(false);
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

            for (int i = 0; i < size; i++) {
                if (((TextView) view.findViewById(R.id.reading_title)).getText().equals(readingsList.get(i).getTitle())) {
                    if (readingsList.get(i).getTitle().length() == 0) {
                        return;
                    }
                    readingModel = readingsList.get(i);
                }
            }

            setChecked(checkBox, readingModel);
            swapCompletedAndMissed(holder, readingModel);

            //For courses without dates
            swapCurrentAndCompleted(holder, readingModel);

        }
    }

    private void swapCompletedAndMissed(BaseViewHolder holder, ReadingModel readingModel) {
        int position = holder.getAdapterPosition();
        SubHeaderModel subHeaderModel = (SubHeaderModel) fullFormArray.get(position);

        String firstWord[] = subHeaderModel.getTitle().split(" ", 2);
        String completedFirst = getString(R.string.completed_readings).split(" ", 2)[0];
        String missedFirst = getString(R.string.missed_readings).split(" ", 2)[0];

        if (firstWord[0].equals(completedFirst)) {
            //Check what the previous is - if it's CURRENT, then break
            SubHeaderModel previousModel = (SubHeaderModel) fullFormArray.get(position -1);
            String currentFirst = getString(R.string.current_readings).split(" ", 2)[0];
            String previousFirst[] = previousModel.getTitle().split(" ", 2);

            if(previousFirst[0].equals(currentFirst)){
                return;
            }

            subHeaderModel.getReadingModelsArray().remove(readingModel);

            SubHeaderModel missingHeader = (SubHeaderModel) fullFormArray.get(position + 1);
            if (readingModel.getTitle() != null) {
                missingHeader.getReadingModelsArray().add(readingModel);
            }
            updateHeaders(subHeaderModel, missingHeader, true);
            addModifyAndNotifyRangeChanged(position, position + 1);

        } else if (firstWord[0].equals(missedFirst)) {
            subHeaderModel.getReadingModelsArray().remove(readingModel);

            SubHeaderModel completeHeader = (SubHeaderModel) fullFormArray.get(position - 1);
            if (readingModel.getTitle() != null) {
                completeHeader.getReadingModelsArray().add(readingModel);
            }
            updateHeaders(completeHeader, subHeaderModel, true);
            addModifyAndNotifyRangeChanged(position - 1, position);
        }
    }

    private void swapCurrentAndCompleted(BaseViewHolder holder, ReadingModel readingModel) {
        int position = holder.getAdapterPosition();
        SubHeaderModel subHeaderModel = (SubHeaderModel) fullFormArray.get(position);

        String firstWord[] = subHeaderModel.getTitle().split(" ", 2);
        String currentFirst = getString(R.string.current_readings).split(" ", 2)[0];
        String completedFirst = getString(R.string.completed_readings).split(" ", 2)[0];

        if (firstWord[0].equals(currentFirst)) {

            SubHeaderModel completedHeader = (SubHeaderModel) fullFormArray.get(position + 1);
            String completedFirstWord[] = completedHeader.getTitle().split(" ", 2);
            if (completedFirstWord[0].equals(completedFirst)) {

                subHeaderModel.getReadingModelsArray().remove(readingModel);

                if (readingModel.getTitle() != null) {
                    completedHeader.getReadingModelsArray().add(readingModel);
                }
                updateHeaders(subHeaderModel, completedHeader, false);
                addModifyAndNotifyRangeChanged(position, position + 1);

            }
        } else if (firstWord[0].equals(completedFirst)) {
            SubHeaderModel currentHeader = (SubHeaderModel) fullFormArray.get(position - 1);

            String currentFirstWord[] = currentHeader.getTitle().split(" ", 2);
            if (currentFirstWord[0].equals(currentFirst)) {

                subHeaderModel.getReadingModelsArray().remove(readingModel);

                if (readingModel.getTitle() != null) {
                    currentHeader.getReadingModelsArray().add(readingModel);
                }
                updateHeaders(currentHeader, subHeaderModel, false);
                addModifyAndNotifyRangeChanged(position - 1, position);
            }
        }
    }


    private void addModifyAndNotifyRangeChanged(final int start, final int end) {
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.getAdapter().notifyItemRangeChanged(start, end);
            }
        });

    }


    private void setChecked(CheckBox checkBox, ReadingModel readingModel) {
        if (checkBox.isChecked()) {
            readingModel.setChecked(false);
            checkBox.setChecked(false);
            updateSecondFragment(readingModel, false);
        } else {
            readingModel.setChecked(true);
            checkBox.setChecked(true);
            updateSecondFragment(readingModel, true);
        }
    }

    private void updateHeaders(SubHeaderModel firstHeader, SubHeaderModel secondHeader, boolean hasDate) {
        if (hasDate) {
            String completedTitle = getString(R.string.completed_readings) + " (" + firstHeader.getReadingModelsArray().size() + ")";
            firstHeader.setTitle(completedTitle);

            String missedTitle = getString(R.string.missed_readings) + " (" + secondHeader.getReadingModelsArray().size() + ")";
            secondHeader.setTitle(missedTitle);
        } else {
            String currentTitle = getString(R.string.current_readings)+ " (" + firstHeader.getReadingModelsArray().size() + ")";
            firstHeader.setTitle(currentTitle);

            String completedTitle= getString(R.string.completed_readings) + " (" + secondHeader.getReadingModelsArray().size() + ")";
            secondHeader.setTitle(completedTitle );
        }
    }

    private void updateSecondFragment(ReadingModel readingModel, boolean checked) {
        //Way to get TagName which generated by FragmentPagerAdapter
        String tagName = "android:switcher:" + R.id.viewpager + ":" + 1; // Your pager name & tab no of Second Fragment
        //Get SecondFragment object from FirstFragment
        ByDateFragment f2 = (ByDateFragment) getActivity().getSupportFragmentManager().findFragmentByTag(tagName);
        //Then call your wish method from SecondFragment to update appropriate list
        f2.updateList(readingModel, checked);
    }

    public void updateList(ReadingModel readingModel, boolean checked) {

        int size = fullFormArray.size();
        for (int i = 0; i < size; i++) {
            RowModel row = fullFormArray.get(i);
            if (row instanceof SubHeaderModel) {
                for (ReadingModel model : ((SubHeaderModel) row).getReadingModelsArray()) {
                    if (model.getTitle().equals(readingModel.getTitle()) && model.getCourseName().equals(readingModel.getCourseName())) {
                        model.setChecked(checked);
                        recyclerView.getAdapter().notifyItemChanged(i);
                        break;
                    }
                }
            }
        }
    }

    public void scrollFromNavigationDrawer(String target) {

         int tempPosition = 0;
        int size = fullFormArray.size();
        for(int i = 0; i<size; i++){
            RowModel row = fullFormArray.get(i);
                if(row.getTitle().equals(target)){
                    tempPosition = i;
                }
        }
        final int position = tempPosition;

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                View view = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                view.setBackgroundResource(R.drawable.background_change);

                TransitionDrawable transition = (TransitionDrawable) view.getBackground();
                transition.startTransition(500);
                transition.reverseTransition(500);
            }
        });
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
