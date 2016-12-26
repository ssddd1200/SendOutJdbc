package dao.pojo;

public class Cousmer {
	
	private int id;
	private String name;
	private String tel;
	private String cadd;
	private String stime;
	private String lltime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCadd() {
		return cadd;
	}
	public void setCadd(String cadd) {
		this.cadd = cadd;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getLltime() {
		return lltime;
	}
	public void setLltime(String lltime) {
		this.lltime = lltime;
	}
	@Override
	public String toString() {
		return "Cousmer [id=" + id + ", name=" + name + ", tel=" + tel + ", cadd=" + cadd + ", stime=" + stime
				+ ", lltime=" + lltime + "]";
	}
	
	
}
