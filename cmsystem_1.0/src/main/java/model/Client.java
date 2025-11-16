package model;

public class Client {

	private Long id;
	private String name;
	private String cpf;
	private String phoneNumber;
	private String email;
	private String adress;
	
	public Client() {
		super();
	}

	public Client(String name, String cpf, String phoneNumber, String email, String adress) {
		super();
		this.name = name;
		this.cpf = cpf;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.adress = adress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", cpf=" + cpf + ", phoneNumber=" + phoneNumber + ", email="
				+ email + ", adress=" + adress + "]";
	}
	
	
	
}
