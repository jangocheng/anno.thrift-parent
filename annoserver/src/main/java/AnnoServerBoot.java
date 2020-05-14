import anno.componentservice.UserInfoModule;
import anno.configuration.AnnoConfig;
import anno.configuration.AopConfig;
import anno.thrift.module.ActionResult;
import anno.thrift.server.AnnoServer;
import anno.thrift.server.ServerInfo;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class AnnoServerBoot {
    public static void main(String[] args) throws InterruptedException, IOException {
//        SpringAop();
//        SpringAopPublishMsg();
        AnnoConfig config=new AnnoConfig("application.yml");
         /*
        配置服务基础信息 从配置文件读取
        为了测试目前写死
         */
        ServerInfo serverInfo = ServerInfo.getDefault();
        serverInfo.setAppName(config.getAppName());
        serverInfo.setPort(config.getPort());
        serverInfo.setWeight(config.getWeight());
        serverInfo.setTimeOut(config.getTimeOut());
        serverInfo.setFuncName(config.getFuncName());
        serverInfo.setMinThreads(config.getMinThreads());
        serverInfo.setMaxThreads(config.getMaxThreads());

        new Thread(() -> {
            new AnnoServer().start(serverInfo.getPort());
        }).start();

        anno.thrift.server.Register.ToCenter(config.getCenterIp(), config.getCenterPort(), config.getReTry());
        while (true) {
            Thread.sleep(3000);
        }
    }
    private static void SpringAop(){
        ApplicationContext context = AopConfig.Default();
        UserInfoModule userInfoModule= context.getBean("userInfoModule",UserInfoModule.class);
        ActionResult<Object> rlt = userInfoModule.HelloWorld("Spring-Aop");
        System.out.println(rlt);
    }

    private static void SpringAopPublishMsg(){
        ApplicationContext context = AopConfig.Default();
        UserInfoModule userInfoModule= context.getBean("userInfoModule",UserInfoModule.class);
        userInfoModule.PublishMsg("Spring-Aop");
        userInfoModule.PublishMsg("杜燕明");
        System.out.println("Publish End");
    }
}
