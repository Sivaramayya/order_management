package com.amzur.order_management.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name ="LINE_ITEMS")
public class LineItemEntity {
   
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "BOOK_ID")
    private int bookId;
	public LineItemEntity(Long id, Long orderId, int bookId) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.bookId = bookId;
	}
	public LineItemEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

