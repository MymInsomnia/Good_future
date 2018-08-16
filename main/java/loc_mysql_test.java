package main.java;


import org.codehaus.janino.Java;

import java.sql.*;

public class loc_mysql_test {
    public static void main(String[] args){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到MySQL驱动!");
            e.printStackTrace();
        }
        Connection conn;
        PreparedStatement pstm =null;
        try {
            //String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/spark?useUnicode=true&characterEncoding=utf8";

            conn = DriverManager.getConnection(url,"root","111111");
            //创建一个Statement对象

            System.out.print("成功连接到数据库！");
            String sql = "INSERT INTO record(title,author,dt) VALUES(?,?,?)";
            pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1,"mysql_writing_test");

//            pstm.setInt(1,4);
            pstm.setString(2,"Unknow");
            pstm.setDate(3,new Date(new java.util.Date().getTime()));
//            pstm.addBatch(); //批量处理
//            pstm.executeBatch()
            //setAutoCommit（false）禁止事物自动提交，通过提交Commit()或是回退rollback()来管理事务的操作
            pstm.executeUpdate();
            ResultSet rs = pstm.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            System.out.println(key);
            conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }



}
