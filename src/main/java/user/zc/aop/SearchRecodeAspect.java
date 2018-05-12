package user.zc.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import user.zc.models.base.Searchlog;
import user.zc.service.base.SearchlogService;
import user.zc.utils.QueryParam;
import org.aspectj.lang.reflect.MethodSignature;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 搜索记录切口
 *
 * @author:Administrator
 * @create 2018-01-30 18:41
 */
@Aspect
@Component
public class SearchRecodeAspect {
    private  static  final Logger logger = LoggerFactory.getLogger(SearchRecodeAspect. class);

    @Autowired
    private SearchlogService searchlogService;
    @Pointcut("@annotation(SearchRecode)")
    public void controllerAspect() {
    }

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        //System.out.println("=====SysLogAspect前置通知开始=====");
        //handleLog(joinPoint, null);
    }

    @AfterReturning(pointcut="controllerAspect()")
    public  void doAfter(JoinPoint joinPoint) {
        /*try {
            SearchRecode.OpType type = getOpType(joinPoint);
            if(type.equals(OpType.SEARCH)){//搜索记录
                addSearchLog(joinPoint,null);
            }else if(type.equals(OpType.BROWSE_POSITION)){//浏览记录
                addBrowse(joinPoint,1);
            }else if(type.equals(OpType.BROWSE_RESUME)){
                addBrowse(joinPoint,0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    @AfterThrowing(value="controllerAspect()",throwing="e")
    public void doAfter(JoinPoint joinPoint, Exception e) {
        //System.out.println("=====SysLogAspect异常通知开始=====");
        //handleLog(joinPoint, e);
    }

    private SearchRecode.OpType getOpType(JoinPoint joinPoint)throws Exception{
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    SearchRecode searchRecode =  method.getAnnotation(SearchRecode.class);
                    return searchRecode.type();
                }
            }
        }
        return null;
    }


    private void addBrowse(JoinPoint joinPoint ,int type){
        /*Browserecord browserecord  = new Browserecord();
        browserecord.setBrowseType(type);
        browserecord.setUserID(IJobSecurityUtils.getLoginUserId());
        for (Object object :joinPoint.getArgs()) {
            if (object instanceof String && object != null) {
                browserecord.setBrowseID(object.toString());
            }else if(object instanceof HttpServletRequest){
                HttpServletRequest request = (HttpServletRequest)object;
                browserecord.setBrowseAddress(request.getRequestURI());
            }
        }
        try {
            QueryParam queryParam = new QueryParam();
            queryParam.put("userID",IJobSecurityUtils.getLoginUserId());
            queryParam.put("browseID",browserecord.getBrowseID());
            queryParam.put("browseAddress",browserecord.getBrowseAddress());
            queryParam.put("browseType",type);
            Browserecord result  = browserecordService.one(queryParam);
            if(result!=null){
                browserecordService.update(result);
            }else{
                browserecordService.add(browserecord);
            }
            logger.info("=====插入或者修改浏览记录=====");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
    private void addSearchLog(JoinPoint joinPoint,Exception e){
        /*for (Object object :joinPoint.getArgs()){
            if(object instanceof  String && object!=null){
                try {
                    QueryParam queryParam = new QueryParam();
                    queryParam.put("userID",IJobSecurityUtils.getLoginUserId());
                    queryParam.put("keyword",object.toString());
                    Searchlog searchlog = searchlogService.one(queryParam);
                    if(searchlog==null){
                        searchlog = new Searchlog();
                        searchlog.setUserID(IJobSecurityUtils.getLoginUserId());
                        searchlog.setKeyword(object.toString());
                        searchlogService.add(searchlog);
                    }else{
                        searchlogService.update(searchlog);
                    }
                    logger.info("=====插入或者修改搜索记录=====");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            }
        }*/
    }
    private void handleLog(JoinPoint joinPoint,Exception e) {
        /*try {
            //获得注解
            SearchRecode logger = giveController(joinPoint);
            if(logger == null)
            {
                return;
            }

            String signature = joinPoint.getSignature().toString(); // 获取目标方法签名
            String methodName = signature.substring(signature.lastIndexOf(".") + 1,
                    signature.indexOf("("));

            String longTemp = joinPoint.getStaticPart().toLongString();
            String classType = joinPoint.getTarget().getClass().getName();

            Class<?> clazz = Class.forName(classType);

            Method[] methods = clazz.getDeclaredMethods();
            System.out.println("methodName: " + methodName);

            for (Method method : methods) {

                if (method.isAnnotationPresent(SearchRecode.class)
                        && method.getName().equals(methodName)) {
                    String annId = logger.id();
                    OpType type = logger.type();
                    String clazzName = clazz.getName();
                    System.out.println("clazzName: " + clazzName+ ", methodName: "
                            + methodName + ", annId: "+ annId + ", type: "+type.toString());
                }
            }

        } catch (Exception exp) {
            logger.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }*/
    }

    private static SearchRecode giveController(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(SearchRecode.class);
        }
        return null;
    }


    public void insertLogSuccess(JoinPoint jp, SearchRecode logger) {}

    public void writeLogInfo(JoinPoint joinPoint, SearchRecode opLogger)
            throws Exception, IllegalAccessException {}
}
