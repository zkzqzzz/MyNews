package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

//greenDao用来自动生成dao！！！

/**
 * 我们的 Java 工程只有一个类，
 * 它的内容决定了「GreenDao Generator」的输出，你可以在这个类中通过对象、关系等创建数据库结构，
 */

public class Daogenerator {

    public static void main(String[] args) throws Exception {
        int version = 1;
        // 创建了一个用于添加实体（Entity）的模式（Schema）对象。
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        String defaultJavaPackage = "com.demo.zk.mynews.greendao";
        Schema schema = new Schema(version, defaultJavaPackage);
        //也可以分别指定生成的 Bean 与 DAO 类所在的目录：
        // Schema schema = new Schema(1, "com.demo.zk.mynews.bean");
        // schema.setDefaultJavaPackageDao("com.demo.zk.mynews.dao");
        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();
        addTable(schema);
        // /表示根目录， ./表示当前路径， ../表示上一级父目录
        // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，
        // 此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        // 其实，输出目录的路径可以在 build.gradle 中设置，这里就不再详解。
        new DaoGenerator().generateAll(schema, "./app/src/main/java-gen");
    }

    private static void addTable(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("NewsChannelTable");
        // 你也可以重新给表命名
        //note.setTableName("othername");
        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
        // note.addIdProperty("");
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
        // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
        /**
         * 频道名称
         */
        note.addStringProperty("newsChannelName").notNull().primaryKey().index();
        /**
         * 频道id
         */
        note.addStringProperty("newsChannelId").notNull();
        /**
         * 频道类型
         */
        note.addStringProperty("newsChannelType").notNull();
        /**
         * 选中的频道
         */
        note.addBooleanProperty("newsChannelSelect").notNull();
        /**
         * 频道的排序位置
         */
        note.addIntProperty("newsChannelIndex").notNull();
        /**
         * 频道是否是固定的
         */
        note.addBooleanProperty("newsChannelFixed");
    }

}
