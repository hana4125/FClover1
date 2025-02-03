/*
package hello.fclover.dbreplication;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SetDataSourceAspect {

    @Before("@annotation(hello.fclover.dbreplication.SetDataSource) && @annotation(target)")
    public void setDataSource(SetDataSource target) throws Exception {

        if (target.dataSourceType() == SetDataSource.DataSourceType.MASTER
                || target.dataSourceType() == SetDataSource.DataSourceType.SLAVE) {
            RoutingDataSourceManager.setCurrentDataSourceName(target.dataSourceType());
        } else {
            throw new Exception("Wrong DataSource Type : Should Check Exception");
        }

    }

    @After("@annotation(hello.fclover.dbreplication.SetDataSource)")
    public void clearDataSource() {
        RoutingDataSourceManager.removeCurrentDataSourceName();
    }
}
*/
