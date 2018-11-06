import com.stan.GPApplication;
import com.stan.model.GPResult;
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
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GPApplication.class)
public class ESTest {
    @Autowired
    private GameService service;

    @Autowired
    private ScriptService script;
    @Autowired
    private ConsoleService console;


    @Before
    public void before(){

    }

    @Test
    public void test() throws Exception {

        console.walkthroughUpdateTitle(Long.valueOf(10001),"第二座");

//        console.addWalkthroughImage(Long.valueOf(100126),null);

//        console.walkthroughUpdateTitle(Long.valueOf(10011),"第一座塔");

//      console.replaceKey("强化南瓜","强化大南瓜",Long.valueOf(1),true);
//      service.getContentList(Long.valueOf(1003));

//        downloadPicture("https://gamepedia-1257100500.cos.ap-shanghai.myqcloud.com/picture/food.jpg","test.jpg");
//        script.downloadWalkthroughPic();


    }



}
