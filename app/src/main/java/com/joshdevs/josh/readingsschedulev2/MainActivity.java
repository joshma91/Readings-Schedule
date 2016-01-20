package com.joshdevs.josh.readingsschedulev2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joshdevs.josh.readingsschedulev2.Models.CourseModel;
import com.joshdevs.josh.readingsschedulev2.Models.ImportModel;
import com.joshdevs.josh.readingsschedulev2.Models.ReadingDateModel;
import com.joshdevs.josh.readingsschedulev2.Models.ReadingEntryModel;
import com.joshdevs.josh.readingsschedulev2.Models.RowModel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BaseViewHolder.OnClickListener, NavigationDrawerFragment.NavigationDrawerCallbacks {


    public ArrayList<RowModel> fullFormArray;
    protected BaseFormAdapter adapter;
    public RecyclerView recyclerView;
    SharedPreferences mPrefs;
    private String[] mCourseTitles = new String[7];
    private ListView mDrawerList;
    ImportModel importModel;
    ViewPager viewPager;
    ArrayList<ReadingDateModel> readingDateModelArray = new ArrayList<>();
    ProgressDialog progress;
    boolean shouldOpen = false;


    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    private int adapterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Readings Schedule");
        importModel = ImportModel.getInstance();
        getSavedPrefs();

        Bundle bundle = new Bundle();
        bundle.putParcelable("import", importModel);

        mNavigationDrawerFragment = new NavigationDrawerFragment();

        if (importModel.getCourses().size() > 0) {
            mNavigationDrawerFragment.setArguments(bundle);
        }
        bundle.putParcelable("import", importModel);

        // Set up the drawer.
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager(),
                MainActivity.this, bundle));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tab);
        tabLayout.setupWithViewPager(viewPager);


        if (importModel != null) {
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout),
                    importModel);

        } else {
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout),
                    null);
        }


    }

    private void getSavedPrefs() {
        mPrefs = getPreferences(MODE_PRIVATE);
        Map<String, ?> keys = mPrefs.getAll();

        Gson gson = new Gson();
        String json = mPrefs.getString("importModel", "");
        ImportModel tempImportModel = gson.fromJson(json, ImportModel.class);

        if (tempImportModel != null) {
            importModel.resetImport();
            for (CourseModel model : tempImportModel.getCourses()) {
                importModel.addCourse(model);
            }
            importModel.setDates(tempImportModel.getDates());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (shouldOpen) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DrawerLayout mDrawerLayout;
                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    shouldOpen = false;
                }
            }, 200);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.show(fm, "About Dialog");
        }
        if (id == R.id.action_import) {
            importFromExcel();
        }

        if (id == R.id.action_instructions) {
            launchInstructionActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchInstructionActivity() {
        Intent intent = new Intent(this, InstructionsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(BaseViewHolder holder, View v) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("ShouldOpen", shouldOpen);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        shouldOpen = savedInstanceState.getBoolean("ShouldOpen", false);
    }

    public void importFromExcel() {

        String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/courses.xlsx";
        final File file = new File(PATH);
        importModel = ImportModel.getInstance();
        importModel.resetImport();

        progress = ProgressDialog.show(this, "Loading",
                "Import in progress...", true);

       final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mString = (String) msg.obj;
                Toast.makeText(MainActivity.this, mString, Toast.LENGTH_LONG).show();
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                // do the thing that takes a long time

                try {

                    XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
                    int wbSize = wb.getNumberOfSheets();


                    for (int sheetNum = 0; sheetNum < wbSize; sheetNum++) {
                        XSSFSheet sheet = wb.getSheetAt(sheetNum);
                        CourseModel model = createCourseModel(sheet);
                        importModel.addCourse(model);
                    }

                    SharedPreferences.Editor editor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(importModel);
                    editor.putString("importModel", json);
                    editor.apply();

                    shouldOpen = true;

                } catch (Exception ioe) {
                    ioe.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "There seems to be a problem with your Excel file. Please ensure the following things:\n-The dates in your Excel workbook are recognized by Excel as dates\n-Your entries begin on the top left of each Excel worksheet\n-Your Excel file is titled 'courses.xlsx' and is placed in the Downloads folder of your phone's storage";
                    mHandler.sendMessage(msg);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.recreate();
                        progress.dismiss();
                    }
                });
            }
        }).start();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        String[] courseNamesArray = mNavigationDrawerFragment.getCourseNamesArray();
        final int target = position * 5;

        String selectedCourse = courseNamesArray[position];
        String tagName = "android:switcher:" + R.id.viewpager + ":" + 0; // Your pager name & tab no of Second Fragment
        ByCourseFragment fragment = (ByCourseFragment) this.getSupportFragmentManager().findFragmentByTag(tagName);
        viewPager.setCurrentItem(0, true);

        fragment.scrollFromNavigationDrawer(selectedCourse);

    }

    private CourseModel createCourseModel(XSSFSheet sheet) {

        CourseModel courseModel = new CourseModel();
        courseModel.setCourseTitle(sheet.getSheetName());
        ArrayList<ReadingEntryModel> readingEntryModelArray = new ArrayList<>();

        XSSFRow row;
        XSSFCell cell;

        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 0; // No of columns
        int tmp = 0;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for (int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if (tmp > cols) cols = tmp;
            }
        }

        for (int r = 0; r < rows; r++) {
            ReadingEntryModel readingEntry = new ReadingEntryModel();
            ReadingDateModel dateEntry = new ReadingDateModel();

            dateEntry.setCourseName(sheet.getSheetName());

            row = sheet.getRow(r);
            if (row != null) {
                cell = row.getCell(0);
                if (cell != null && !cell.toString().isEmpty()) {
                    readingEntry.setReadingName(cell.toString());
                    dateEntry.setReadingName(cell.toString());
                }
                cell = row.getCell(1);
                if (cell != null && !cell.toString().isEmpty()) {
                    long readingDate = cell.getDateCellValue().getTime();

                    //For CourseModel's Entry
                    readingEntry.setReadingDateInMillis(readingDate);
                    long nextReadingDate = getNextReadingDate(cell.getDateCellValue().getTime(), sheet);
                    readingEntry.setNextReadingDate(nextReadingDate);

                    //For ReadingDateModel's Entry
                    dateEntry.setReadingDate(readingDate);
                }
            }
            readingEntryModelArray.add(readingEntry);
            importModel.addDate(dateEntry);
        }
        courseModel.setReadingEntryModelArray(readingEntryModelArray);

        return courseModel;
    }

    private long getNextReadingDate(long readingDate, XSSFSheet sheet) {
        long nextReadingDate = 0;

        XSSFRow row;
        XSSFCell cell;

        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 0; // No of columns
        int tmp = 0;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for (int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if (tmp > cols) cols = tmp;
            }
        }

        long currentDifference = 9999999999999L;

        for (int r = 0; r < rows; r++) {
            row = sheet.getRow(r);
            if (row != null) {
                cell = row.getCell(1);
                if (cell != null && !cell.toString().isEmpty() && cell.getDateCellValue().getTime() > readingDate) {
                    long difference = cell.getDateCellValue().getTime() - readingDate;
                    if (difference < currentDifference && difference != 0) {
                        nextReadingDate = cell.getDateCellValue().getTime();

                    }
                    currentDifference = difference;
                }
            }
        }

        return nextReadingDate;
    }

    @Override
    public void onLoseFocus(BaseViewHolder holder) {

    }

    @Override
    public void onGainFocus(BaseViewHolder holder) {

    }
}