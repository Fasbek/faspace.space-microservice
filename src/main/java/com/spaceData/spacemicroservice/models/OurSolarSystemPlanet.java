package com.spaceData.spacemicroservice.models;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.spaceData.spacemicroservice.models.Dto.DtoMoon;

//@Table(name="OurSolarSystemPlanet")
@Entity
@Table(name="oursolarsystemplanet", catalog="oursolarsystemdb", schema="DBO")
public class OurSolarSystemPlanet {

	@Id
	@NotNull()
	@Size(max=15)
	private String name;
	@NotNull()
	@Size(min=1, max=1)	
	private String isplanet;

	private long semimayoraxis;
	private long perihelion;
	private long aphelion;
	
    @Digits(integer=5, fraction=5)
	private BigDecimal eccentricity;
    
    @Digits(integer=5, fraction=5)
    private BigDecimal inclination;
	
    @Digits(integer=5, fraction=5)
    private BigDecimal massvalue;

    @Digits(integer=5, fraction=5)
    private Integer massexponent;

    @Digits(integer=5, fraction=5)
    private BigDecimal volvalue;

    @Digits(integer=5, fraction=5)
    private Integer volexponent;

    @Digits(integer=5, fraction=5)
    private BigDecimal density;

    @Digits(integer=5, fraction=5)
    private BigDecimal gravity;

    @Digits(integer=5, fraction=5)
    private BigDecimal escapevel;

    @Digits(integer=5, fraction=5)
    private BigDecimal meanradius;

    @Digits(integer=5, fraction=5)
    private BigDecimal equaradius;

    @Digits(integer=5, fraction=5)
    private BigDecimal polarradius;

    @Digits(integer=5, fraction=5)
    private BigDecimal flattening;

    @Digits(integer=5, fraction=5)
    private BigDecimal sideralorbit;

    @Digits(integer=5, fraction=5)
    private BigDecimal sideralrotation;

	private Integer avgtemp;

	@NotNull
	@Size(max=7)
	private String bodytype;

	@NotNull
	@Size(max=20)
	private String texture_path;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name="texture", nullable=true)
	private byte[] texture;
	
	@Transient
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "moon_id")
	private List<DtoMoon> listaMoons;
	
	

	public OurSolarSystemPlanet() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsplanet() {
		return isplanet;
	}

	public void setIsplanet(String isplanet) {
		this.isplanet = isplanet;
	}

	public long getSemimayoraxis() {
		return semimayoraxis;
	}

	public void setSemimayoraxis(long semimayoraxis) {
		this.semimayoraxis = semimayoraxis;
	}

	public long getPerihelion() {
		return perihelion;
	}

	public void setPerihelion(long perihelion) {
		this.perihelion = perihelion;
	}

	public long getAphelion() {
		return aphelion;
	}

	public void setAphelion(long aphelion) {
		this.aphelion = aphelion;
	}

	public BigDecimal getEccentricity() {
		return eccentricity;
	}

	public void setEccentricity(BigDecimal eccentricity) {
		this.eccentricity = eccentricity;
	}

	public BigDecimal getInclination() {
		return inclination;
	}

	public void setInclination(BigDecimal inclination) {
		this.inclination = inclination;
	}

	public BigDecimal getMassvalue() {
		return massvalue;
	}

	public void setMassvalue(BigDecimal massvalue) {
		this.massvalue = massvalue;
	}

	public Integer getMassexponent() {
		return massexponent;
	}

	public void setMassexponent(Integer massexponent) {
		this.massexponent = massexponent;
	}

	public BigDecimal getVolvalue() {
		return volvalue;
	}

	public void setVolvalue(BigDecimal volvalue) {
		this.volvalue = volvalue;
	}

	public Integer getVolexponent() {
		return volexponent;
	}

	public void setVolexponent(Integer volexponent) {
		this.volexponent = volexponent;
	}

	public BigDecimal getDensity() {
		return density;
	}

	public void setDensity(BigDecimal density) {
		this.density = density;
	}

	public BigDecimal getGravity() {
		return gravity;
	}

	public void setGravity(BigDecimal gravity) {
		this.gravity = gravity;
	}

	public BigDecimal getEscapevel() {
		return escapevel;
	}

	public void setEscapevel(BigDecimal escapevel) {
		this.escapevel = escapevel;
	}

	public BigDecimal getMeanradius() {
		return meanradius;
	}

	public void setMeanradius(BigDecimal meanradius) {
		this.meanradius = meanradius;
	}

	public BigDecimal getEquaradius() {
		return equaradius;
	}

	public void setEquaradius(BigDecimal equaradius) {
		this.equaradius = equaradius;
	}

	public BigDecimal getPolarradius() {
		return polarradius;
	}

	public void setPolarradius(BigDecimal polarradius) {
		this.polarradius = polarradius;
	}

	public BigDecimal getFlattening() {
		return flattening;
	}

	public void setFlattening(BigDecimal flattening) {
		this.flattening = flattening;
	}

	public BigDecimal getSideralorbit() {
		return sideralorbit;
	}

	public void setSideralorbit(BigDecimal sideralorbit) {
		this.sideralorbit = sideralorbit;
	}

	public BigDecimal getSideralrotation() {
		return sideralrotation;
	}

	public void setSideralrotation(BigDecimal sideralrotation) {
		this.sideralrotation = sideralrotation;
	}

	public Integer getAvgtemp() {
		return avgtemp;
	}

	public void setAvgtemp(Integer avgtemp) {
		this.avgtemp = avgtemp;
	}

	public String getBodytype() {
		return bodytype;
	}

	public void setBodytype(String bodytype) {
		this.bodytype = bodytype;
	}

	public List<DtoMoon> getListaMoons() {
		return listaMoons;
	}

	public void setListaMoons(List<DtoMoon> listaMoons) {
		this.listaMoons = listaMoons;
	}

	public byte[] getTexture() {
		return texture;
	}

	public void setTexture(byte[] texture) {
		this.texture = texture;
	}

	public String getTexture_path() {
		return texture_path;
	}

	public void setTexture_path(String texture_path) {
		this.texture_path = texture_path;
	}	

}
