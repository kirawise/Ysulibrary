package com.example.ysulibrary.bean;

public class User {
	private boolean logined;//�Ƿ��¼
	private String name;//����
	private String password;//����
	private String number;//ѧ��
	private String expiry_date;//ʧЧ����
	private String effective_date;//��Ч����
	private String biggest_lend_num;//���ɽ�ͼ��
	private String biggest_absord_num;//����ԤԼ
	private String reader_type;//��������
	private String lended_num;//�ۼƽ���
	private String violations;//Υ�´���
	private String sex;//�Ա�
	private String phone;//�ֻ���
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}

	public String getEffective_date() {
		return effective_date;
	}

	public void setEffective_date(String effective_date) {
		this.effective_date = effective_date;
	}

	public String getBiggest_lend_num() {
		return biggest_lend_num;
	}

	public void setBiggest_lend_num(String biggest_lend_num) {
		this.biggest_lend_num = biggest_lend_num;
	}

	public String getBiggest_absord_num() {
		return biggest_absord_num;
	}

	public void setBiggest_absord_num(String biggest_absord_num) {
		this.biggest_absord_num = biggest_absord_num;
	}

	public String getReader_type() {
		return reader_type;
	}

	public void setReader_type(String reader_type) {
		this.reader_type = reader_type;
	}

	public String getLended_num() {
		return lended_num;
	}

	public void setLended_num(String lended_num) {
		this.lended_num = lended_num;
	}

	public String getViolations() {
		return violations;
	}

	public void setViolations(String violations) {
		this.violations = violations;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public User(){
		this.logined = false;
	}
	
	public boolean isLogined() {
		return logined;
	}
	public void setLogined(boolean logined) {
		this.logined = logined;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
}
