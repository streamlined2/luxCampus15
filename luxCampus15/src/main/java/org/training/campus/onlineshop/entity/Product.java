package org.training.campus.onlineshop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(of = { "id" })
public class Product implements Serializable {

	private long id;
	private @NonNull String name;
	private @NonNull BigDecimal price;
	private @NonNull LocalDate creationDate;

}
