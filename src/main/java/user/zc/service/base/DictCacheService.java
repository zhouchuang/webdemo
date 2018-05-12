package user.zc.service.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 字典类
 *
 * @author:Administrator
 * @create 2018-01-21 13:39
 */
@Service
public class DictCacheService {
    public static String OUT_JAVA_PATH = "C:/generalCode";
    private final static Logger logger = LoggerFactory.getLogger(DictCacheService.class);
    private static  Map<String,String> position  ;

    public static String UploadPath;
    public static String UploadUrl;
    @PostConstruct
    public  void init(){
        try {
            readConfig();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    //读取配置文件
    public void readConfig()throws Exception{
        Properties properties=new Properties();
        //获得输入流
        InputStream is=DictCacheService.class.getClassLoader().getResourceAsStream("ijob.properties");
        properties.load(is);
        is.close();

        UploadPath = (String)properties.get("upload.path");
        UploadUrl = (String)properties.get("upload.url");


    }

    public  static String  getPosition(String value){
        for(String key : position.keySet()){
            if(position.get(key).equals(value)){
                return key;
            }
        }
        return null;
    }


}
