# README


I liked using official Wikipedia App on Android because it is well suited for surfing between articles, able to do reading lists and many more features. However Wikipedia is blocked in Turkey since April 2017. 

Even though Im no professional developer, I have some experience and interest with programming. So I said maybe there I can tweak the official app, contunie browsing wikipedia and getting smarter. There are some wikipedia mirror proxies known and people use to in Turkey so my intention was force the app to use that proxy instead of wikipedia.org. After a research I decided the use proxy vikiansiklopedi.org because its legidity and support for more than two languages(thanks to owner of this proxy C.Cantoro). 

So I went on github, searched wikipedia android and in that repository I searched for string "https://wikipedia.org" and and found one on app/src/main/java/org/wikipedia/Constants.java file. So I cloned the official app to my desktop, then changed that line to "https://vikiansiklopedi.org" proxy mirror. Compiled it threw to my mobilephone and it worked. It is forked from october 30 relase of wikipedia which is version beta/2.6.206-beta-2017-10-30.

Then I shared that in google play store. I am planning to update after every new release of official wikipedia as long as it works and unblock continues. Please note that because of proxy usage editing and logging in does not work.


## **tldr;**

This app uses vikiansiklopedi.org instead of wikipedia.org. So that's how it works in Turkey. So far all the other changes are like changing logo, disabling crash reports and making invisible some xml parts etc. Editing doesn't work because of proxt usage.




### note
If you would like to remove empty banner spaces at the bottom, please change occurance of ```"50dp"``` to ```"0dp"``` in app/src/main/res/layout/activity_page.xml and app/src/main/layout/activity_single_fragment_with_toolbar.xml



