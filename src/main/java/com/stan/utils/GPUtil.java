package com.stan.utils;

import com.stan.model.pojo.GPItem;
import com.stan.model.pojo.GPItemAttribute;
import com.stan.model.pojo.GPType;
import com.stan.model.pojo.GPWalkthrough;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class GPUtil {

    public static String imageUri = "https://gamepedia-1257100500.cos.ap-shanghai.myqcloud.com/content/";

    public static Map convertItem(GPItem resItem, List<GPItemAttribute> attributeList){
        Map res = new HashMap();
        for (GPItemAttribute attribute:attributeList){
            switch (attribute.getAttributeindex().intValue()){
                case 0:res.put(attribute.getName(),resItem.getAttribute1());
                case 1:res.put(attribute.getName(),resItem.getAttribute2());
                case 2:res.put(attribute.getName(),resItem.getAttribute3());
                case 3:res.put(attribute.getName(),resItem.getAttribute4());
                case 4:res.put(attribute.getName(),resItem.getAttribute5());
                case 5:res.put(attribute.getName(),resItem.getAttribute6());
                case 6:res.put(attribute.getName(),resItem.getAttribute7());
                case 7:res.put(attribute.getName(),resItem.getAttribute8());
            }
        }
        res.put("name",resItem.getName());
        res.put("image",resItem.getImage());
        res.put("typeId",resItem.getTypeid());
        res.put("description",resItem.getDescription());
        return res;
    }

    public static Map getRequestParamMap(HttpServletRequest request)
    {
        Map map = new HashMap();
        //得到枚举类型的参数名称，参数名称若有重复的只能得到第一个
        Enumeration enums = request.getParameterNames();
        while (enums.hasMoreElements())
        {
            String paramName = (String) enums.nextElement();
            String paramValue = request.getParameter(paramName);

            //形成键值对应的map
            map.put(paramName, paramValue);
        }
        return map;
    }

    public  static List<GPWalkthrough>getWalkthroughOne(List<GPWalkthrough> list){
        List<GPWalkthrough> temp = new ArrayList<>();
        Integer tempTag = 0;
        for (int i = 0; i < list.size(); i++) {
            GPWalkthrough wt = list.get(i);
            if (tempTag == wt.getLocid().intValue()){
                continue;
            }
            tempTag = wt.getLocid().intValue();
            temp.add(wt);


        }
        return temp;
    }


    public static void downloadPicture(String urlList,String name,String doc) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            String imageName =  "/Users/kai/Documents/zeldapic/"+doc+"/"+name+".jpg";

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
