
package com.elastic.sample.elastic;

import com.example.elasticsearch.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UserTest {

    private User theUser;

    @Before
    public void setup(){
        theUser = new User();
    }

    @Test
    public void userInitialized(){
        assertNotNull(theUser);
    }

}
