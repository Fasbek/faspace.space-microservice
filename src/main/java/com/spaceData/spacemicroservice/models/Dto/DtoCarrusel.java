package com.spaceData.spacemicroservice.models.Dto;

public class DtoCarrusel {
	
	private Integer id;
	private String disc_facility;
	private Integer count;
	
	public DtoCarrusel(Integer id, String disc_facility, Integer count) {
		super();
		this.id = id;
		this.disc_facility = disc_facility;
		this.count = count;
	}
	public String getDisc_facility() {
		return disc_facility;
	}
	public void setDisc_facility(String disc_facility) {
		this.disc_facility = disc_facility;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	

}
