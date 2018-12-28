package com.example.why.assignment3;

import android.test.suitebuilder.annotation.LargeTest;

import com.example.why.assignment3.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//@RunWith(AndroidUnitTest4.class)
@LargeTest
public class AndroidUnitTest {
    MainActivity mainm = new MainActivity();

    Follow fol = new Follow();

    Following fli = new Following();

    Login lgi = new Login();

    Item_user x0 = new Item_user("hwu63", "", "https://api.github.com/users/hwu63", "https://github.com/hwu63",
            "https://api.github.com/users/hwu63/followers", "https://api.github.com/users/hwu63/following{/other_user}",
            "https://api.github.com/users/hwu63/repos");

    Item y = new Item("hwu63", "", "https://api.github.com/users/hwu63", "https://github.com/hwu63");


    WebProfile w1 = new WebProfile();


    MainActivity main = new MainActivity();

    Follow fo = new Follow();

    Following fl = new Following();

    Login lg = new Login();

    Item_user x = new Item_user("hwu63", "", "https://api.github.com/users/hwu63", "https://github.com/hwu63",
            "https://api.github.com/users/hwu63/followers", "https://api.github.com/users/hwu63/following{/other_user}",
            "https://api.github.com/users/hwu63/repos");

    Item xy = new Item("hwu63", "", "https://api.github.com/users/hwu63", "https://github.com/hwu63");


    WebProfile w = new WebProfile();

}
