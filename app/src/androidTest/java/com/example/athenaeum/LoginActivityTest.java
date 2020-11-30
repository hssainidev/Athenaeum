package com.example.athenaeum;
import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test class for LoginActivity. All the UI tests for the app are written here
 * because of the necessity for authorization when being logged in.
 * Robotium test framework is used.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest{
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Enter a valid username and password and ensure that it switches to MainActivity
     * when the login button is pressed.
     */
    @Test
    public void checkLogin(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
    }

    /**
     * Ensure that the activity switches to the signup activity when sign up is pressed.
     */
    @Test
    public void checkSwitchToSignup(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.clickOnButton("SIGN UP"); //Select SIGN UP Button
        // Asserts that the activity is now SignupActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", SignupActivity.class);
    }

    /**
     * Ensure that the main activity switches to the add book activity when add book is pressed.
     */
    @Test
    public void checkSwitchToAddBook(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.addBook)); //Select ADD BOOK Button
        // Asserts that the activity is now AddBookActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", AddBookActivity.class);
    }

    /**
     * Ensure that the main activity switches to the search activity when search by keyword is pressed.
     */
    @Test
    public void checkSwitchToSearch(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.searchButton)); //Select ADD BOOK Button
        // Asserts that the activity is now SearchActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", SearchActivity.class);
    }

    /**
     * Ensure that the main activity switches to the book activity when a book is pressed.
     */
    @Test
    public void checkSwitchToBook(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickInList(0);
        // Asserts that the activity is now BookInfoActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", BookInfoActivity.class);
    }

    /**
     * Ensure that the navigation menu opens and has all the menu options.
     */
    @Test
    public void checkNavMenu(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnImage(0);
        assertTrue(solo.waitForText("Profile", 1, 2000));
        assertTrue(solo.waitForText("Notifications", 1, 2000));
        assertTrue(solo.waitForText("Owned Books", 1, 2000));
        assertTrue(solo.waitForText("Requested Books", 1, 2000));
        assertTrue(solo.waitForText("Borrowed Books", 1, 2000));
        assertTrue(solo.waitForText("Accepted Requests", 1, 2000));
        assertTrue(solo.waitForText("Scan Book", 1, 2000));
        assertTrue(solo.waitForText("Logout", 1, 2000));
    }

    /**
     * Ensure that the activity switches correctly to Profile activity
     */
    @Test
    public void checkSwitchToProfile(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnImage(0);
        assertTrue(solo.waitForText("Profile", 1, 2000));
        solo.clickOnText("Profile");
        // Asserts that the activity is now ProfileActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", ProfileActivity.class);
    }

    /**
     * Ensure that the activity switches correctly to Notifications activity
     */
    @Test
    public void checkSwitchToNotifications(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnImage(0);
        assertTrue(solo.waitForText("Notifications", 1, 2000));
        solo.clickOnText("Notifications");
        // Asserts that the activity is now NotificationActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", NotificationActivity.class);
    }

    /**
     * Ensure that the activity switches correctly to Owned Books activity
     */
    @Test
    public void checkSwitchToOwned(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnImage(0);
        assertTrue(solo.waitForText("Owned Books", 1, 2000));
        solo.clickOnText("Owned Books");
        // Asserts that the activity is now OwnedBookActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", OwnedBookActivity.class);
    }

    /**
     * Ensure that the activity switches correctly to Requested Books activity
     */
    @Test
    public void checkSwitchToRequested(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnImage(0);
        assertTrue(solo.waitForText("Requested Books", 1, 2000));
        solo.clickOnText("Requested Books");
        // Asserts that the activity is now RequestedBookActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", RequestedBookActivity.class);
    }

    /**
     * Ensure that the activity switches correctly to Borrowed Books activity
     */
    @Test
    public void checkSwitchToBorrowed(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnImage(0);
        assertTrue(solo.waitForText("Borrowed Books", 1, 2000));
        solo.clickOnText("Borrowed Books");
        // Asserts that the activity is now BorrowedBookActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", BorrowedBookActivity.class);
    }

    /**
     * Ensure that the activity switches correctly to Accepted Book activity
     */
    @Test
    public void checkSwitchToAccepted(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnImage(0);
        assertTrue(solo.waitForText("Accepted Requests", 1, 2000));
        solo.clickOnText("Accepted Requests");
        // Asserts that the activity is now AcceptedBookActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", AcceptedBookActivity.class);
    }

    /**
     * Ensure that the activity switches correctly to Scan Book activity
     */
    @Test
    public void checkSwitchToScan(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnImage(0);
        assertTrue(solo.waitForText("Scan Book", 1, 2000));
        solo.clickOnText("Scan Book");
        // Asserts that the activity is now ScanActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", ScanActivity.class);
    }

    /**
     * Ensure that the activity logs out correctly.
     */
    @Test
    public void checkLogout(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //Get view for EditText and enter a valid email
        solo.enterText((EditText) solo.getView(R.id.loginEmail), "testing@gmail.com");
        //Get view for EditText and enter a valid password
        solo.enterText((EditText) solo.getView(R.id.loginPassword), "tester");
        solo.clickOnButton("LOGIN"); //Select LOGIN Button
        // Asserts that the activity is now MainActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", MainActivity.class);
        solo.clickOnImage(0);
        assertTrue(solo.waitForText("Logout", 1, 2000));
        solo.clickOnText("Logout");
        // Asserts that the activity is now LoginActivity, otherwise display "did not switch activity"
        solo.assertCurrentActivity("Did not switch activity.", LoginActivity.class);
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}