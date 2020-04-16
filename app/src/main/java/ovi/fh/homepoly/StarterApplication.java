package ovi.fh.homepoly;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;


public class StarterApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("351484a55a42d286a5a364874c339b51f8e779ef")
                // if defined
                .clientKey("6201f5e4809b24c03d3961487d300deb9b81edf0")
                .server("http://3.21.232.234:80/parse")
                .build()
        );

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
