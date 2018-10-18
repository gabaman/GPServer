import com.stan.GPApplication;
import com.stan.service.ConsoleService;
import com.stan.service.GameService;
import com.stan.service.ScriptService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GPApplication.class)
public class ESTest {
    @Autowired
    private GameService service;

    @Autowired
    private ScriptService script;



    @Before
    public void before(){

    }

    @Test
    public void test(){
//        downloadPicture("https://gamepedia-1257100500.cos.ap-shanghai.myqcloud.com/picture/food.jpg","test.jpg");
//        script.downloadWalkthroughPic();


    }



}
