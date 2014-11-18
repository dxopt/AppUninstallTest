package me.ycdev.android.demo.appuninstalltest;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "AppUninstallTest";

    private static final String EXTRA_TEST_AUTO = "test.auto";
    private static final String EXTRA_FILES_COUNT = "files.count";

    private static final int DEFAULT_FILES_COUNT = 1000;

    private Button mTestBtn;

    private static boolean sIsTesting;
    private int mFilesCount = DEFAULT_FILES_COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean autoTest = getIntent().getBooleanExtra(EXTRA_TEST_AUTO, false);
        mFilesCount = getIntent().getIntExtra(EXTRA_FILES_COUNT, DEFAULT_FILES_COUNT);
        Log.d(TAG, "enter app: " + autoTest + ", files count: " + mFilesCount);

        if (!sIsTesting && autoTest) {
            startTest(getApplicationContext(), mFilesCount);
        }

        mTestBtn = (Button) findViewById(R.id.start_test);
        mTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sIsTesting) {
                    startTest(getApplicationContext(), mFilesCount);
                } else {
                    stopTest();
                }
                refreshTestButton();
            }
        });
        refreshTestButton();
    }

    private void refreshTestButton() {
        if (!sIsTesting) {
            mTestBtn.setText(R.string.start_test);
        } else {
            mTestBtn.setText(R.string.stop_test);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static void startTest(final Context appContext, final int filesCount) {
        sIsTesting = true;
        new Thread("Test") {
            @Override
            public void run() {
                int i = 0;
                while (sIsTesting) {
                    if (i > filesCount) {
                        Log.d(TAG, "go back: " + Thread.currentThread().getId());
                        i = 0;
                    }
                    SharedPreferences prefs = appContext.getSharedPreferences("test_" + i,
                            Context.MODE_PRIVATE);
                    prefs.edit().putLong("value", i).commit();
                    i++;
                }
            }
        }.start();
    }

    private static void stopTest() {
        sIsTesting = false;
    }
}
