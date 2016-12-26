package com.qingke.DAO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.qingke.pojo.Cousmer;
import com.qingke.pojo.Manager;
import com.qingke.pojo.Merchants;
import com.qingke.pojo.Order;
import com.qingke.pojo.knight;

public class Dao {
	
	private Connection conn= null;
	private PreparedStatement statement = null;
	private ResultSet rs = null;
	
	public Dao(){
	}
	
	private static Connection getConnection(){
		try {
			Class.forName(getProper("driver"));

			return DriverManager.getConnection(getProper("url"), getProper("username"), getProper("password"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
	
	protected static String getProper(String key){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("jdbc_conf.properties"));
			
			return properties.getProperty(key);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void cleanup(Connection conn,PreparedStatement statement,ResultSet rs){
		try {
			if(rs != null){
				rs.close();				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(statement != null){
				statement.close();				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(conn != null){
				conn.close();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Manager login(String username,String password){
		conn = getConnection();
		String sql = "select id,name,telphone,email,(select code from employee_limit where id=employee_limit_id) as lim from employee where id =(select employee_id from e_load where username=? and password=?)";
		try {
			statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			
			rs = statement.executeQuery();
			Manager manager = new Manager();
			while(rs.next()){
				manager.setId(rs.getLong("id"));
				System.out.println(rs.getLong("id"));
				manager.setName(rs.getString("name"));
				manager.setTelphone(rs.getString("telphone"));
				manager.setEmail(rs.getString("email"));
				manager.setLimit(rs.getString("lim"));
			}
			return manager;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cleanup(conn, statement, rs);
		}
		return null;
	}

	public boolean changeInfo(String email,String phone,Manager manager){
		conn = getConnection();
		String sql = "update employee set email=?,telphone=? where id=?";
		
		try {
			statement = conn.prepareStatement(sql);
			statement.setString(1, email);
			statement.setString(2, phone);
			statement.setLong(3, manager.getId());
			
			int n = statement.executeUpdate();
			if(n>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			cleanup(conn, statement, rs);			
		}
		return false;
	}

	public boolean changePassword(Manager manager,String oldPassword,String newPassword){
		String sql = "update e_load set password=? where employee_id=?";
		String sql1 = "select 'Y' from e_load where employee_id=? and password=?";
		int n=0;
		PreparedStatement ps1 =null;
		PreparedStatement ps2 = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			ps1 = conn.prepareStatement(sql);
			ps2 = conn.prepareStatement(sql1);
			
			ps2.setLong(1, 1);
			ps2.setString(2, "123456");
			rs = ps2.executeQuery();
			while(rs.next()){
				if( "Y".equals(rs.getString("Y"))){
					ps1.setString(1, newPassword);
					ps1.setLong(2, manager.getId());
					n = ps1.executeUpdate();
				}				
			}
			
			
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			cleanup(conn, ps1, rs);
			cleanup(conn, ps2, rs);
		}
		if(n>0){
			return true;
		}
		return false;
	}
	
	public List<Manager> selectAll(){
		List<Manager> managers = new ArrayList<Manager>();
		String sql = "select id,name,telphone,email,(select code from employee_limit where id=employee_limit_id) as lim from employee";
		
		conn = getConnection();
		try {
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			while(rs.next()){
				Manager manager = new Manager();
				manager.setId(rs.getLong("id"));
				manager.setName(rs.getString("name"));
				manager.setTelphone(rs.getString("telphone"));
				manager.setEmail(rs.getString("email"));
				manager.setLimit(rs.getString("lim"));
				
				managers.add(manager);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			cleanup(conn, statement, rs);
		}
		return managers;
	}

	public boolean changeManInfo(String name,String phone,String email,String limit){
		String sql = "update employee set name=?,telphone=?,email=?,employee_limit_id=(select id from employee_limit where code=?)";
		conn = getConnection();
		try {
			statement = conn.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, phone);
			statement.setString(4, email);
			statement.setString(5, limit);
			
			int n = statement.executeUpdate();
			if(n>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cleanup(conn, statement, rs);
		}
		
		return false;
	}

	public boolean addManager(long id,String name,String phone,String email,String limit){
		
		String sql = "insert into employee(id,name,telphone,email,employee_limit_id)values(?,?,?,?,(select id from employee_limit where code=?))";
		conn = getConnection();
		try {
			statement = conn.prepareStatement(sql);
			statement.setLong(1, id);
			statement.setString(2, name);
			statement.setString(3, phone);
			statement.setString(4, email);
			statement.setString(5, limit);
			int n = statement.executeUpdate();
			if(n>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cleanup(conn, statement, rs);
		}
		return false;
	}

	public boolean delManger(long id){
		String sql = "delete from employee where id=?";
		conn = getConnection();
		try {
			statement = conn.prepareStatement(sql);
			statement.setLong(1, id);
			int n = statement.executeUpdate();
			if(n>0){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cleanup(conn, statement, rs);
		}
		return false;
	}

	public List<Merchants> selectMer(){
		List<Merchants> merList = new ArrayList<>();
		String sql = "select id,name,telphone,address,create_time,(select code from m_status where id=m_states_id) as 'status' from merchants";
		conn = getConnection();
		try {
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			while(rs.next()){
				Merchants mch = new Merchants();
				mch.setId(rs.getLong("id"));
				mch.setName(rs.getString("name"));
				mch.setTelphone(rs.getString("telphone"));
				mch.setAddress(rs.getString("address"));
				mch.setCreateTime(rs.getString("create_time"));
				mch.setStates(rs.getString("status"));
				merList.add(mch);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return merList;
	}

	public boolean changeMerStatus(String status){
		
		String sql="update merchants set m_states_id=(select id from m_status where code=?)";
		try {
			conn = getConnection();
			statement = conn.prepareStatement(sql);
			statement.setString(1, status);
			int n = statement.executeUpdate();
			if(n>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<knight> selectKnight(){
		List<knight> kList = new ArrayList<>();
		String sql = "select id,name,telphone,create_time,(select code from k_status where id=k_states_id) as 'status' from knight";
		conn = getConnection();
		try {
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			while(rs.next()){
				knight kn= new knight();
				kn.setId(rs.getLong("id"));
				kn.setName(rs.getString("name"));
				kn.setTelphone(rs.getString("telphone"));
				kn.setCreateTime(rs.getString("create_time"));
				kn.setStatus(rs.getString("status"));
				kList.add(kn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kList;
	}

	public boolean changeKnightStatus(String status){
		
		String sql="update knight set k_states_id=(select id from k_status where code=?)";
		try {
			conn = getConnection();
			statement = conn.prepareStatement(sql);
			statement.setString(1, status);
			int n = statement.executeUpdate();
			if(n>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Cousmer> selectCousmer(){
		List<Cousmer> list = new ArrayList<>();
		String sql = "select id,name,telphone,sign_time,last_login_time from consumer";
		conn = getConnection();
		try {
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			while(rs.next()){
				Cousmer c = new Cousmer();
				c.setId(rs.getLong("id"));
				c.setName(rs.getString("name"));
				c.setTelphone(rs.getString("telphone"));
				c.setSignTime(rs.getString("sign_time"));
				c.setLastTime(rs.getString("last_login_time"));
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean deleteCousmer(Cousmer cousmer){
		
		String sql="delete cousmer where id=?";
		try {
			conn = getConnection();
			statement = conn.prepareStatement(sql);
			statement.setLong(1, cousmer.getId());
			int n = statement.executeUpdate();
			if(n>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Order> selectOrder(){
		List<Order> list = new ArrayList<>();
		String sql = "SELECT o.id as 'id',m.name as 'name',m.telphone as 'mtel',m.address as 'madd',c.name as 'cname',c.telphone as 'ctel',cd.address as 'cadd',k.name as 'kname',k.telphone as 'ktel',os.code as 'oc' FROM `order` o,o_status os,goods_has_order go,goods g,merchants m,knight k,consumer c,cus_address cd WHERE o.o_states_id = os.id AND o.id = go.order_id AND go.goods_id = g.id AND g.merchants_id = m.id AND o.knight_id = k.id AND o.consumer_id = c.id AND c.id = cd.consumer_id AND cd.static_add = 'Y';";
		conn = getConnection();
		try {
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			while(rs.next()){
				Order order = new Order();
				order.setId(rs.getLong("id"));
				order.setName(rs.getString("name"));
				order.setMerPhone(rs.getString("mtel"));
				order.setMerAddress(rs.getString("madd"));
				order.setCname(rs.getString("cname"));
				order.setCtel(rs.getString("ctel"));
				order.setCadd(rs.getString("cadd"));
				order.setKname(rs.getString("kname"));
				order.setKtel(rs.getString("ktel"));
				order.setStatus(rs.getString("oc"));
				list.add(order);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public boolean changeOrderStatus(String status){
		
		String sql="update order set o_states_id=(select id from o_status where code=?)";
		try {
			conn = getConnection();
			statement = conn.prepareStatement(sql);
			statement.setString(1, status);
			int n = statement.executeUpdate();
			if(n>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		Dao dao = new Dao();
		Manager m = dao.login("123456",	"123456");
		System.out.println(String.format("%03d", 1));
	}
}
