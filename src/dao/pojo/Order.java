package dao.pojo;

public class Order {
	
	private int id;
	private String mname;
	private String madd;
	private String mtel;
	private String cname;
	private String cadd;
	private String ctel;
	private String kname;
	private String ktel;
	private String status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getMadd() {
		return madd;
	}
	public void setMadd(String madd) {
		this.madd = madd;
	}
	public String getMtel() {
		return mtel;
	}
	public void setMtel(String mtel) {
		this.mtel = mtel;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCadd() {
		return cadd;
	}
	public void setCadd(String cadd) {
		this.cadd = cadd;
	}
	public String getCtel() {
		return ctel;
	}
	public void setCtel(String ctel) {
		this.ctel = ctel;
	}
	public String getKname() {
		return kname;
	}
	public void setKname(String kname) {
		this.kname = kname;
	}
	public String getKtel() {
		return ktel;
	}
	public void setKtel(String ktel) {
		this.ktel = ktel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", mname=" + mname + ", madd=" + madd + ", mtel=" + mtel + ", cname=" + cname
				+ ", cadd=" + cadd + ", ctel=" + ctel + ", kname=" + kname + ", ktel=" + ktel + ", status=" + status
				+ "]";
	}
	
	
}
