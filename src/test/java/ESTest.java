import com.stan.GPApplication;
import com.stan.model.vo.ContentResult;
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
import java.util.List;


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

      service.getContentList(Long.valueOf(1001),1,10);
//      service.getContentList(Long.valueOf(1003));

//        downloadPicture("https://gamepedia-1257100500.cos.ap-shanghai.myqcloud.com/picture/food.jpg","test.jpg");
//        script.downloadWalkthroughPic();


    }



}
