package com.ldc.bananamuffin;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

public class BreakfastActivityTest extends ActivityInstrumentationTestCase2<BreakfastActivity> {
    Solo solo;

    public BreakfastActivityTest() {
        super(BreakfastActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testCorrectActivityIsPickedUp() {
        solo.assertCurrentActivity("Wrong activity", BreakfastActivity.class);
    }
}
