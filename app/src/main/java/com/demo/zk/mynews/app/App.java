package com.demo.zk.mynews.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.demo.zk.mynews.BuildConfig;
import com.demo.zk.mynews.common.Constant;
import com.demo.zk.mynews.greendao.DaoMaster;
import com.demo.zk.mynews.greendao.DaoSession;
import com.socks.library.KLog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zk.changeskin.SkinManager;

import de.greenrobot.dao.query.QueryBuilder;
/**
 * Created by zk.
 * time: 2016/8/28 8:33.
 * com.demo.zk.mynews.app
 * function:Application
 */
public class App extends Application {

    //内存泄漏检测的观察组
    private RefWatcher mRefWatcher;

    //dao层连接
    private DaoSession mDaoSession;

    //上下文
    private static Context sApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //更换皮肤初始化
        SkinManager.getInstance().init(this);
        // 如果检测到某个 activity 有内存泄露，LeakCanary 就是自动地显示一个通知
        mRefWatcher = LeakCanary.install(this);
        //Dao
        setupDatabase();
        sApplicationContext = this;
        //Klog初始化
        KLog.init(BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * 获取内存监控
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.mRefWatcher;
    }

    private void setupDatabase() {
        // // 官方推荐将获取 DaoMaster 对象的方法放到 Application 层，这样将避免多次创建生成 Session 对象
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constant.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        //你可能会遇到查询结果并不是预期的那样，这时候你就可以设置两个静态flag参数来打日志定位问题：
        QueryBuilder.LOG_SQL = BuildConfig.DEBUG;
        QueryBuilder.LOG_VALUES = BuildConfig.DEBUG;
        /*QueryBuilder的出现就是为了解决sql使用的问题，提高开发效率。

        看一个略微复杂的例子，查询first name是Joe，并在1970年10月以及之后的所有人：

        QueryBuilder qb = userDao.queryBuilder();

        qb.where(Properties.FirstName.eq("Joe"),

        qb.or(Properties.YearOfBirth.gt(1970),

        qb.and(Properties.YearOfBirth.eq(1970), Properties.MonthOfBirth.ge(10))));

        List youngJoes = qb.list();
        */

    }

    /**
     * 获取dao会话
     *
     * @return
     */
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    // 获取ApplicationContext
    public static Context getContext() {
        return sApplicationContext;
    }
}

