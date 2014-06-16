package com.evologics.polaris.model;
import com.evologics.polaris.util.PolarisUtil.ESTATUS_PRESTAMO;

import java.util.Date;

public class Prestamo {
	
	private String objeto;
	private String prestado_a;
	private String nota;
	private Date fecha_prestamo;
	private Date fecha_devolucion;
	private String categoria;
	private ESTATUS_PRESTAMO estatus;
	
	public Prestamo() {
		
	}
	
	public Prestamo(String objeto, String prestado_a, String nota,
			Date fecha_prestamo, Date fecha_devolucion,String categoria, ESTATUS_PRESTAMO estatus) {
		this.objeto = objeto;
		this.prestado_a = prestado_a;
		this.nota = nota;
		this.fecha_prestamo = fecha_prestamo;
		this.fecha_devolucion = fecha_devolucion;
		this.categoria = categoria;
		this.estatus = estatus;
	}
	
	public String getObjeto() {
		return objeto;
	}
	
	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}
	
	public String getPrestado_a() {
		return prestado_a;
	}
	
	public void setPrestado_a(String prestado_a) {
		this.prestado_a = prestado_a;
	}
	
	public String getNota() {
		return nota;
	}
	
	public void setNota(String nota) {

		this.nota = nota;
	}
	
	public Date getFecha_prestamo() {
		return fecha_prestamo;
	}
	
	public void setFecha_prestamo(Date fecha_prestamo) {
		this.fecha_prestamo = fecha_prestamo;
	}
	
	public Date getFecha_devolucion() {
		return fecha_devolucion;
	}
	
	public void setFecha_devolucion(Date fecha_devolucion) {
		this.fecha_devolucion = fecha_devolucion;
	}
	
	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public ESTATUS_PRESTAMO getEstatus() {
		return estatus;
	}
	
	public void setEstatus(ESTATUS_PRESTAMO estatus) {
		this.estatus = estatus;
	}
	
	
	//Functions out of getters and setters
	public boolean isExpired(){
		return new Date().after(fecha_devolucion) ? true : false;
	}
}
